package com.boll.tyelauncher.model;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.common.GlobalVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetAllApps {
    private static final String TAG = "GetAllApps";
    private ArrayList<AppInfo> datas = new ArrayList<>();
    private Context mContext;
    private int mIconDpi;
    private PackageManager packageManager;
    private String tag = TAG;

    public GetAllApps(Context mContext2) {
        this.mContext = mContext2;
        this.packageManager = mContext2.getPackageManager();
        this.mIconDpi = ((ActivityManager) mContext2.getSystemService("activity")).getLauncherLargeIconDensity();
    }

    public AppInfo getAppInfo(String pName) {
        AppInfo app = null;
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri) null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> apps = this.packageManager.queryIntentActivities(mainIntent, 0);
        for (int i = 0; i < apps.size(); i++) {
            String packageName = apps.get(i).activityInfo.applicationInfo.packageName;
            if (pName.equals(packageName) && !packageName.contains("toyCloud")) {
                String title = apps.get(i).loadLabel(this.packageManager).toString();
                if (title == null) {
                    title = apps.get(i).activityInfo.name;
                }
                app = new AppInfo(packageName, title, getFullResIcon(apps.get(i).activityInfo));
            }
        }
        return app;
    }

    public void loadAllAppsByBatch() {
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri) null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        GlobalVariable.appInfoList_forbidden.clear();
        this.datas.clear();
        List<ResolveInfo> apps = this.packageManager.queryIntentActivities(mainIntent, 0);
        for (int i = 0; i < apps.size(); i++) {
            String packageName = apps.get(i).activityInfo.applicationInfo.packageName;
            if (packageName.equals("com.android.calendar")) {
                GlobalVariable.appInfoList_forbidden.add("com.android.calendar");
            } else if (packageName.equals("com.cyanogenmod.filemanager")) {
                GlobalVariable.appInfoList_forbidden.add("com.cyanogenmod.filemanager");
            } else if (packageName.equals("com.android.dialer")) {
                GlobalVariable.appInfoList_forbidden.add("com.android.dialer");
            } else if (packageName.equals("com.android.music")) {
                GlobalVariable.appInfoList_forbidden.add("com.android.music");
            } else if (packageName.equals("com.qualcomm.qti.logkit")) {
                GlobalVariable.appInfoList_forbidden.add("com.qualcomm.qti.logkit");
            } else if (packageName.equals("com.iflytek.ChineseStroke") || packageName.equals("com.guo2")) {
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.ChineseStroke");
                GlobalVariable.appInfoList_forbidden.add("com.guo2");
            } else if (!packageName.equals("corg.chromium.webview_shell") && !packageName.equals(BuildConfig.APPLICATION_ID) && !"com.ets100.study".equals(packageName) && !packageName.equals("com.iflytek.xiri") && !packageName.equals("com.toycloud.updateservice") && !packageName.equals("com.iflytek.inputmethod") && !packageName.equals("com.toycloud.app.myskill") && !packageName.equals(AppContants.RECITE_BOOK) && !packageName.equals("com.android.inputmethod.latin") && !packageName.equals("com.qualcomm.qti.sensors.qsensortest") && !packageName.equals(GlobalVariable.MICROCLASS_PKG) && !packageName.equals("com.iflytek.recommend_tsp") && !packageName.equals("com.iflytek.wrongnotebook")) {
                GlobalVariable.appInfoList_forbidden.add("corg.chromium.webview_shell");
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.xiri");
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.inputmethod");
                GlobalVariable.appInfoList_forbidden.add("com.toycloud.app.myskill");
                GlobalVariable.appInfoList_forbidden.add(AppContants.RECITE_BOOK);
                GlobalVariable.appInfoList_forbidden.add("com.toycloud.updateservice");
                GlobalVariable.appInfoList_forbidden.add("com.android.inputmethod.latin");
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.recommend_tsp");
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.dictatewords");
                GlobalVariable.appInfoList_forbidden.add(GlobalVariable.ENGLISH_LSP_PKG);
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.wrongnotebook");
                GlobalVariable.appInfoList_forbidden.add(GlobalVariable.SEARCHBYPHOTO_PKG);
                GlobalVariable.appInfoList_forbidden.add(GlobalVariable.SEARCHBYPHOTO_PKG);
                GlobalVariable.appInfoList_forbidden.add(GlobalVariable.MICROCLASS_PKG);
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.k12aitstatistics");
                GlobalVariable.appInfoList_forbidden.add(BuildConfig.APPLICATION_ID);
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.wrongnotebook");
                GlobalVariable.appInfoList_forbidden.add("com.iflytek.recommend_tsp");
                GlobalVariable.appInfoList_forbidden.add("com.ets100.study");
                String title = apps.get(i).loadLabel(this.packageManager).toString();
                if (TextUtils.isEmpty(title)) {
                    title = apps.get(i).activityInfo.name;
                }
                AppInfo appInfo = new AppInfo(packageName, title, getFullResIcon(apps.get(i).activityInfo));
                try {
                    appInfo.setSystemApp(isSystemApp(this.packageManager.getPackageInfo(packageName, 0)));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                this.datas.add(appInfo);
            }
        }
        try {
            if (this.mContext.getPackageManager().getPackageInfo("com.ets100.study", 64) != null) {
                Logging.d(TAG, "loadAllAppsByBatch() ets installed, add phonetic study icon");
                AppInfo appInfo2 = new AppInfo(EtsConstant.ETS_PHONETIC_VIRTUAL_PACKAGE, this.mContext.getString(R.string.ets_phonetic), this.mContext.getDrawable(R.drawable.ets_phonetic_study_logo));
                appInfo2.setSystemApp(true);
                this.datas.add(appInfo2);
                return;
            }
            Logging.d(TAG, "loadAllAppsByBatch() ets not installed, don't add phonetic study icon");
        } catch (PackageManager.NameNotFoundException e2) {
        }
    }

    public static boolean isSystemApp(PackageInfo pInfo) {
        return (pInfo.applicationInfo.flags & 1) != 0;
    }

    public Drawable getFullResIcon(ActivityInfo info) {
        Resources resources;
        int iconId;
        try {
            resources = this.packageManager.getResourcesForApplication(info.applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            resources = null;
        }
        if (resources == null || (iconId = info.getIconResource()) == 0) {
            return getFullResDefaultActivityIcon();
        }
        return getFullResIcon(resources, iconId);
    }

    public Drawable getFullResDefaultActivityIcon() {
        return getFullResIcon(Resources.getSystem(), 17629184);
    }

    public Drawable getFullResIcon(Resources resources, int iconId) {
        Drawable d;
        try {
            d = resources.getDrawableForDensity(iconId, this.mIconDpi);
        } catch (Resources.NotFoundException e) {
            d = null;
        }
        return d != null ? d : getFullResDefaultActivityIcon();
    }

    public ArrayList<AppInfo> getDatas() {
        loadAllAppsByBatch();
        for (int i = 0; i < this.datas.size(); i++) {
            AppInfo appInfo = this.datas.get(i);
            String pakageName = appInfo.getPakageName();
            if (pakageName.equals("com.android.deskclock")) {
                appInfo.setSort(1);
            } else if (pakageName.equals("com.android.settings")) {
                appInfo.setSort(2);
            } else if (pakageName.equals("org.codeaurora.gallery")) {
                appInfo.setSort(3);
            } else if (pakageName.equals("org.codeaurora.snapcam")) {
                appInfo.setSort(4);
            } else if (pakageName.equals("com.android.calculator2")) {
                appInfo.setSort(5);
            } else if (pakageName.equals("com.android.documentsui")) {
                appInfo.setSort(6);
            } else if (pakageName.equals("com.toycloud.queryword")) {
                appInfo.setSort(7);
            } else if (pakageName.equals("com.iflytek.ipa")) {
                appInfo.setSort(8);
            } else if (pakageName.equals("com.iflytek.appshop")) {
                appInfo.setSort(9);
            } else if (pakageName.equals("com.iflytek.eyecareassistant")) {
                appInfo.setSort(10);
            } else if (pakageName.equals("com.toycloud.app.notification")) {
                appInfo.setSort(11);
            } else if (pakageName.equals("com.toycloud.app.greenbrowser")) {
                appInfo.setSort(12);
            } else if (pakageName.equals("com.iflytek.main.bestarticle")) {
                appInfo.setSort(13);
            } else if (pakageName.equals(Constants.ZXW_APP)) {
                appInfo.setSort(14);
            } else if (pakageName.equals("com.ets100.ets")) {
                appInfo.setSort(15);
            } else {
                appInfo.setSort(Integer.valueOf(i + 15));
            }
        }
        Collections.sort(this.datas);
        return this.datas;
    }
}
