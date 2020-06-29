package com.boll.tyelauncher.model.launcher;


import android.graphics.drawable.Drawable;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.model.launcher.ApkDrawable;

public class IconInfo {
    public final Drawable mIconBitmap;
    public final String mPackageName;
    public final int mVersionCode;

    public IconInfo(String pkg, int versionCode, Drawable bitmap) {
        this.mPackageName = pkg;
        this.mVersionCode = versionCode;
        this.mIconBitmap = bitmap;
    }

    public String buildCacheKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mPackageName).append(":").append(this.mVersionCode);
        return sb.toString();
    }

    public static String buildCacheKey(AppInfo appInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(appInfo.getPakageName()).append(":").append(appInfo.mVersionCode);
        return sb.toString();
    }

    public static String buildCacheKey(ApkDrawable drawable) {
        StringBuilder sb = new StringBuilder();
        sb.append(drawable.mPackageName).append(":").append(drawable.mVersionCode);
        return sb.toString();
    }
}