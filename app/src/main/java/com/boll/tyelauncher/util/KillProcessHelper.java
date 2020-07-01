package com.boll.tyelauncher.util;


import android.app.ActivityManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KillProcessHelper {
    public static void killBackgroundGress(ActivityManager systemService, String appcode) {
        try {
            Method forceStopPackage = ActivityManager.class.getMethod("forceStopPackage", new Class[]{String.class});
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(systemService, new Object[]{appcode});
            SystemUtils.removeRecentTask(appcode, systemService);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
    }
}
