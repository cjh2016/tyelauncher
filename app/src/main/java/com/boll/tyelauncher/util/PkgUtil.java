package com.boll.tyelauncher.util;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;

public class PkgUtil {
    private PkgUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean checkApkIsInstall(Context context, String packName) {
        PackageManager manager;
        List<PackageInfo> infos;
        boolean isInstall = false;
        try {
            if (!TextUtils.isEmpty(packName) && context != null && (manager = context.getPackageManager()) != null && (infos = manager.getInstalledPackages(0)) != null && infos.size() > 0) {
                Iterator<PackageInfo> it = infos.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().packageName.equalsIgnoreCase(packName)) {
                            isInstall = true;
                            infos.clear();
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInstall;
    }

    public static int getAppCode(Context context, String pkgName) {
        PackageInfo info;
        if (context == null) {
            return -1;
        }
        try {
            if (TextUtils.isEmpty(pkgName.trim()) || (info = context.getPackageManager().getPackageInfo(pkgName, 0)) == null) {
                return -1;
            }
            return info.versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    public static void startAppByPackName(Context context, String packName) {
        ResolveInfo resolveinfo;
        try {
            PackageInfo packageinfo = context.getPackageManager().getPackageInfo(packName, 0);
            if (packageinfo != null) {
                Intent resolveIntent = new Intent("android.intent.action.MAIN", (Uri) null);
                resolveIntent.setPackage(packageinfo.packageName);
                List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
                if (resolveinfoList != null && !resolveinfoList.isEmpty() && (resolveinfo = resolveinfoList.iterator().next()) != null) {
                    String packageName = resolveinfo.activityInfo.packageName;
                    String className = resolveinfo.activityInfo.name;
                    Intent intent = new Intent("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.LAUNCHER");
                    intent.addFlags(268435456);
                    intent.setComponent(new ComponentName(packageName, className));
                    context.startActivity(intent);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}