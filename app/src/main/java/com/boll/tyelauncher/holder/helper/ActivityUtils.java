package com.boll.tyelauncher.holder.helper;

package com.toycloud.launcher.holder.helper;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import java.util.List;

public class ActivityUtils {
    public static final boolean moveToFront(Context context, String pkgName) {
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(100);
        if (taskList == null) {
            return false;
        }
        int index = 0;
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            if (!TextUtils.equals(pkgName, rti.topActivity.getPackageName())) {
                index++;
            } else if (index == 0) {
                return true;
            } else {
                am.moveTaskToFront(rti.id, 0);
                return true;
            }
        }
        return false;
    }

    public static boolean isForegroundApp(Context context, String pkgName) {
        List<ActivityManager.RunningTaskInfo> list = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (list == null || list.isEmpty()) {
            return false;
        }
        return TextUtils.equals(pkgName, list.get(0).topActivity.getPackageName());
    }

    public static boolean isGlobalFrontActivity(Context context, ComponentName componentName) {
        List<ActivityManager.RunningTaskInfo> list = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (list == null || list.isEmpty()) {
            return false;
        }
        ComponentName cpn = list.get(0).topActivity;
        if (!TextUtils.equals(cpn.getPackageName(), componentName.getPackageName()) || !TextUtils.equals(cpn.getShortClassName(), componentName.getShortClassName())) {
            return false;
        }
        return true;
    }

    public static boolean startActivity(Context context, ComponentName componentName) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(componentName);
            intent.setFlags(270532608);
            context.startActivity(intent);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }
}