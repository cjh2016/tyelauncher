package com.boll.tyelauncher.model.launcher;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.model.launcher.interfaces.ISystemPackageManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApkIconCache {
    private final Context mContext;
    private final Map<String, IconInfo> mInfoMap = new ConcurrentHashMap();
    private final ISystemPackageManager mPackageManager;

    public ApkIconCache(Context context, ISystemPackageManager pkgManager) {
        this.mContext = context;
        this.mPackageManager = pkgManager;
    }

    public Drawable getApkDrawable(String gradePeriod, AppInfo appInfo) {
        if (appInfo == null || TextUtils.isEmpty(appInfo.pakageName)) {
            return null;
        }
        String key = IconInfo.buildCacheKey(appInfo);
        IconInfo cache = this.mInfoMap.get(key);
        if (cache != null) {
            return cache.mIconBitmap;
        }
        ApkDrawable drawable = this.mPackageManager.queryAppIcon(gradePeriod, appInfo.getPakageName());
        if (drawable == null) {
            return null;
        }
        IconInfo cache2 = new IconInfo(drawable.mPackageName, drawable.mVersionCode, drawable.mIcon);
        String newKey = cache2.buildCacheKey();
        if (!TextUtils.equals(newKey, key)) {
            throw new RuntimeException("两个key不同：oldKey=" + key + ", newKey=" + newKey);
        }
        this.mInfoMap.put(newKey, cache2);
        return drawable.mIcon;
    }

    public void handleApkChanged(AppInfo appInfo) {
    }
}