package com.boll.tyelauncher.model.launcher.impl;

package com.toycloud.launcher.model.launcher.impl;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.model.launcher.ApkDrawable;
import com.boll.tyelauncher.model.launcher.interfaces.ISystemPMHooker;
import com.boll.tyelauncher.model.launcher.interfaces.ISystemPackageManager;
import com.boll.tyelauncher.util.ListUtils;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LauncherSystemPM implements ISystemPackageManager {
    private static final Map<String, Integer> DEFAULT_ORDER_PACKAGES = new HashMap();
    private static final String TAG = "LauncherSystemPM";
    private final Map<String, ApplicationInfo> mApplicationCache = new HashMap();
    private final Context mContext;
    private final ISystemPMHooker mHooker;
    private final PackageManager mPM;
    private final Map<String, PackageInfo> mPackageCache = new HashMap();
    private final Map<String, ResolveInfo> mResolveInfoCache = new HashMap();

    static {
        initDefaultOrder();
    }

    public LauncherSystemPM(Context context, ISystemPMHooker hooker) {
        this.mContext = context;
        this.mPM = context.getPackageManager();
        this.mHooker = hooker;
    }

    private void initAppMetaInfo(ApplicationInfo applicationInfo, AppInfo appInfo) {
        appInfo.initMetaInfo(applicationInfo.metaData);
    }

    @Override
    public List<AppInfo> queryAll(String gradePeriod) {
        List<AppInfo> result;
        Set<String> sets = new HashSet<>();
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri) null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> apps = this.mPM.queryIntentActivities(mainIntent, 0);
        if (ListUtils.isEmpty((List) apps)) {
            result = new ArrayList<>();
            if (this.mHooker != null) {
                this.mHooker.onQueryAll(gradePeriod, result);
            }
            sort(result);
        } else {
            List<PackageInfo> pis = this.mPM.getInstalledPackages(8192);
            if (ListUtils.isEmpty((List) pis)) {
                result = new ArrayList<>();
                if (this.mHooker != null) {
                    this.mHooker.onQueryAll(gradePeriod, result);
                }
                sort(result);
            } else {
                this.mPackageCache.clear();
                this.mApplicationCache.clear();
                for (PackageInfo pi : pis) {
                    this.mPackageCache.put(pi.packageName, pi);
                }
                ArrayList arrayList = new ArrayList(apps.size());
                ArrayList arrayList2 = new ArrayList(apps.size());
                for (ResolveInfo resolveInfo : apps) {
                    ApplicationInfo applicationInfo = resolveInfo.activityInfo.applicationInfo;
                    if (!sets.contains(applicationInfo.packageName)) {
                        this.mResolveInfoCache.put(applicationInfo.packageName, resolveInfo);
                        this.mApplicationCache.put(applicationInfo.packageName, applicationInfo);
                        if (this.mHooker != null) {
                            if (this.mHooker.shouldIgnoreApp(gradePeriod, applicationInfo.packageName)) {
                            }
                        }
                        AppInfo appInfo = new AppInfo();
                        appInfo.setPakageName(applicationInfo.packageName);
                        String appName = resolveInfo.loadLabel(this.mPM).toString();
                        if (TextUtils.isEmpty(appName)) {
                            appName = resolveInfo.activityInfo.name;
                        }
                        appInfo.appName = appName;
                        PackageInfo pi2 = this.mPackageCache.get(applicationInfo.packageName);
                        appInfo.setIcon(applicationInfo.loadIcon(this.mPM));
                        if (pi2 == null) {
                            appInfo.mVersionCode = 0;
                            appInfo.setSystemApp(false);
                        } else {
                            appInfo.mVersionCode = pi2.versionCode;
                            if ((pi2.applicationInfo.flags & 1) != 0) {
                                appInfo.setSystemApp(true);
                            } else {
                                appInfo.setSystemApp(false);
                            }
                        }
                        if (appInfo.isSystemApp()) {
                            arrayList.add(appInfo);
                        } else {
                            arrayList2.add(appInfo);
                        }
                        initAppMetaInfo(applicationInfo, appInfo);
                        sets.add(applicationInfo.packageName);
                    }
                }
                result = new ArrayList<>(apps.size());
                result.addAll(arrayList);
                result.addAll(arrayList2);
                if (this.mHooker != null) {
                    this.mHooker.onQueryAll(gradePeriod, result);
                }
                sort(result);
            }
        }
        return result;
    }

    public static void sort(List<AppInfo> appInfos) {
        if (appInfos != null) {
            int size = appInfos.size();
            for (int i = 0; i < size; i++) {
                AppInfo appInfo = appInfos.get(i);
                Integer order = DEFAULT_ORDER_PACKAGES.get(appInfo.getPakageName());
                if (order == null) {
                    String name = appInfo.getAppName();
                    if (!TextUtils.isEmpty(name)) {
                        order = DEFAULT_ORDER_PACKAGES.get(name);
                    }
                }
                if (order == null) {
                    appInfo.setSort(Integer.valueOf(DEFAULT_ORDER_PACKAGES.size() + i));
                } else {
                    appInfo.setSort(Integer.valueOf(order.intValue()));
                }
            }
            Collections.sort(appInfos);
        }
    }

    @Override
    public AppInfo query(String gradePeriod, String pkgName) {
        AppInfo appInfo = null;
        if (this.mHooker == null || !this.mHooker.shouldIgnoreApp(gradePeriod, pkgName)) {
            ApplicationInfo applicationInfo = queryApplicationInfo(pkgName);
            PackageInfo pkgInfo = queryPackageInfo(pkgName);
            ResolveInfo resolveInfo = queryResolveInfo(pkgName);
            if (!(applicationInfo == null || pkgInfo == null || resolveInfo == null)) {
                appInfo = new AppInfo();
                appInfo.setPakageName(applicationInfo.packageName);
                String appName = resolveInfo.loadLabel(this.mPM).toString();
                if (TextUtils.isEmpty(appName)) {
                    appName = resolveInfo.activityInfo.name;
                }
                appInfo.appName = appName;
                PackageInfo pi = this.mPackageCache.get(applicationInfo.packageName);
                appInfo.setIcon(applicationInfo.loadIcon(this.mPM));
                if (pi == null) {
                    appInfo.mVersionCode = 0;
                    appInfo.setSystemApp(false);
                } else {
                    appInfo.mVersionCode = pi.versionCode;
                    if ((pi.applicationInfo.flags & 1) != 0) {
                        appInfo.setSystemApp(true);
                    } else {
                        appInfo.setSystemApp(false);
                    }
                }
                initAppMetaInfo(applicationInfo, appInfo);
            }
        }
        return appInfo;
    }

    private ResolveInfo queryResolveInfo(String pkgName) {
        ResolveInfo info = this.mResolveInfoCache.get(pkgName);
        if (info == null) {
            try {
                Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setPackage(pkgName);
                info = this.mPM.resolveActivity(intent, 0);
                if (info == null) {
                    return null;
                }
                this.mResolveInfoCache.put(pkgName, info);
            } catch (Exception e) {
                Logger.e(TAG, "queryResolveInfo error", e);
            }
        }
        return info;
    }

    private ApplicationInfo queryApplicationInfo(String pkgName) {
        ApplicationInfo info = this.mApplicationCache.get(pkgName);
        if (info != null) {
            return info;
        }
        try {
            info = this.mPM.getApplicationInfo(pkgName, 0);
            this.mApplicationCache.put(pkgName, info);
            return info;
        } catch (Exception e) {
            Logger.e(TAG, "queryApplicationInfo error", e);
            return info;
        }
    }

    private PackageInfo queryPackageInfo(String pkgName) {
        PackageInfo pkgInfo = this.mPackageCache.get(pkgName);
        if (pkgInfo != null) {
            return pkgInfo;
        }
        try {
            pkgInfo = this.mPM.getPackageInfo(pkgName, 0);
            this.mPackageCache.put(pkgName, pkgInfo);
            return pkgInfo;
        } catch (Exception e) {
            Logger.e(TAG, "queryPackageInfo error", e);
            return pkgInfo;
        }
    }

    @Override
    public ApkDrawable queryAppIcon(String gradePeriod, String pkgName) {
        PackageInfo packageInfo;
        Drawable drawable;
        ApplicationInfo applicationInfo = queryApplicationInfo(pkgName);
        if (applicationInfo == null || (packageInfo = queryPackageInfo(pkgName)) == null || (drawable = applicationInfo.loadIcon(this.mPM)) == null) {
            return null;
        }
        return new ApkDrawable(pkgName, packageInfo.versionName, packageInfo.versionCode, drawable);
    }

    @Override
    public void updateCache(String pkgName) {
        this.mApplicationCache.remove(pkgName);
        this.mPackageCache.remove(pkgName);
        this.mResolveInfoCache.remove(pkgName);
    }

    private static final void initDefaultOrder() {
        int order = -1 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.android.settings", Integer.valueOf(order));
        int order2 = order + 1;
        DEFAULT_ORDER_PACKAGES.put("com.android.deskclock", Integer.valueOf(order2));
        int order3 = order2 + 1;
        DEFAULT_ORDER_PACKAGES.put("org.codeaurora.gallery", Integer.valueOf(order3));
        int order4 = order3 + 1;
        DEFAULT_ORDER_PACKAGES.put("org.codeaurora.snapcam", Integer.valueOf(order4));
        int order5 = order4 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.android.calculator2", Integer.valueOf(order5));
        int order6 = order5 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.android.documentsui", Integer.valueOf(order6));
        int order7 = order6 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.iflytek.appshop", Integer.valueOf(order7));
        int order8 = order7 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.iflytek.eyecareassistant", Integer.valueOf(order8));
        int order9 = order8 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.toycloud.app.notification", Integer.valueOf(order9));
        int order10 = order9 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.toycloud.app.greenbrowser", Integer.valueOf(order10));
        int order11 = order10 + 1;
        DEFAULT_ORDER_PACKAGES.put(EtsConstant.ETS_PHONETIC_VIRTUAL_PACKAGE, Integer.valueOf(order11));
        int order12 = order11 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.toycloud.queryword", Integer.valueOf(order12));
        int order13 = order12 + 1;
        DEFAULT_ORDER_PACKAGES.put(GlobalVariable.PACKAGE_ENGLISGTOCHINENSE, Integer.valueOf(order13));
        int order14 = order13 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.iflytek.sceneenglish", Integer.valueOf(order14));
        int order15 = order14 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.iflytek.antonym", Integer.valueOf(order15));
        int order16 = order15 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.iflytek.idiominterpretation", Integer.valueOf(order16));
        int order17 = order16 + 1;
        DEFAULT_ORDER_PACKAGES.put(GlobalVariable.SEARCHBYPHOTO_PKG, Integer.valueOf(order17));
        int order18 = order17 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.iflytek.ipa", Integer.valueOf(order18));
        int order19 = order18 + 1;
        DEFAULT_ORDER_PACKAGES.put("com.iflytek.main.bestarticle", Integer.valueOf(order19));
        DEFAULT_ORDER_PACKAGES.put(Constants.ZXW_APP, Integer.valueOf(order19 + 1));
    }
}
