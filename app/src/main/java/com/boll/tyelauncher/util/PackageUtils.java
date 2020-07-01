package com.boll.tyelauncher.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public class PackageUtils {
    private Context mContext;

    public PackageUtils(Context context) {
        this.mContext = context;
    }

    public PackageInfo getPackageInfo(String packageName) {
        return getPackageInfo(this.mContext, packageName);
    }

    public static PackageInfo getPackageInfo(Context context, String packageName) {
        if (context == null) {
            return null;
        }
        if (TextUtils.isEmpty(packageName)) {
            packageName = context.getPackageName();
        }
        try {
            return context.getPackageManager().getPackageInfo(packageName, 8192);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public int getVersionCode(String packageName) {
        PackageInfo info = getPackageInfo(packageName);
        if (info != null) {
            return info.versionCode;
        }
        return -1;
    }

    public String getVersionName() {
        PackageInfo info = getPackageInfo((String) null);
        if (info != null) {
            return info.versionName;
        }
        return "";
    }

    public String getPackageName() {
        PackageInfo info = getPackageInfo((String) null);
        if (info != null) {
            return info.packageName;
        }
        return "";
    }

    public static boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }

    public static String getAppName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getApplicationLabel(pm.getApplicationInfo(packageName, 128)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
}
