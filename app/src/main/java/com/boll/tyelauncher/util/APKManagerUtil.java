package com.boll.tyelauncher.util;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class APKManagerUtil {
    public static void uninstall(Activity activity, String packageName) {
        Method[] methods = null;
        int i = 0;
        try {
            PackageManager pm = activity.getPackageManager();
            if (pm != null) {
                methods = pm.getClass().getDeclaredMethods();
            }
            Method mDel = null;
            if (methods != null && methods.length > 0) {
                int length = methods.length;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Method method = methods[i];
                    if (method.getName().toString().equals("deletePackage")) {
                        mDel = method;
                        break;
                    }
                    i++;
                }
            }
            if (mDel != null) {
                mDel.setAccessible(true);
                mDel.invoke(pm, new Object[]{packageName, null, 0});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
