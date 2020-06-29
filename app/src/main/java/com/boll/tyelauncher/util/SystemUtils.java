package com.boll.tyelauncher.util;


import android.app.ActivityManager;
import android.os.Build;
import com.orhanobut.logger.Logger;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SystemUtils {
    public static final String TAG = SystemUtils.class.getSimpleName();

    public static void hookWebView() {
        Method getProviderClassMethod;
        int sdkInt = Build.VERSION.SDK_INT;
        try {
            Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
            Field field = factoryClass.getDeclaredField("sProviderInstance");
            field.setAccessible(true);
            Object sProviderInstance = field.get((Object) null);
            if (sProviderInstance != null) {
                Logger.i(TAG, "sProviderInstance isn't null");
                return;
            }
            if (sdkInt > 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass", new Class[0]);
            } else if (sdkInt == 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass", new Class[0]);
            } else {
                Logger.i(TAG, "Don't need to Hook WebView");
                return;
            }
            getProviderClassMethod.setAccessible(true);
            Class<?> factoryProviderClass = (Class) getProviderClassMethod.invoke(factoryClass, new Object[0]);
            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
            Constructor<?> delegateConstructor = delegateClass.getDeclaredConstructor(new Class[0]);
            delegateConstructor.setAccessible(true);
            if (sdkInt < 26) {
                Constructor<?> providerConstructor = factoryProviderClass.getConstructor(new Class[]{delegateClass});
                if (providerConstructor != null) {
                    providerConstructor.setAccessible(true);
                    sProviderInstance = providerConstructor.newInstance(new Object[]{delegateConstructor.newInstance(new Object[0])});
                }
            } else {
                Field chromiumMethodName = factoryClass.getDeclaredField("CHROMIUM_WEBVIEW_FACTORY_METHOD");
                chromiumMethodName.setAccessible(true);
                String chromiumMethodNameStr = (String) chromiumMethodName.get((Object) null);
                if (chromiumMethodNameStr == null) {
                    chromiumMethodNameStr = "create";
                }
                Method staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr, new Class[]{delegateClass});
                if (staticFactory != null) {
                    sProviderInstance = staticFactory.invoke((Object) null, new Object[]{delegateConstructor.newInstance(new Object[0])});
                }
            }
            if (sProviderInstance != null) {
                field.set("sProviderInstance", sProviderInstance);
                Logger.i(TAG, "Hook success!");
                return;
            }
            Logger.i(TAG, "Hook failed!");
        } catch (Throwable e) {
            Logger.w(TAG, "hookWebView error", e);
        }
    }

    public static void removeRecentTask(String packageName, ActivityManager activityManager) {
        for (ActivityManager.RecentTaskInfo recentTask : activityManager.getRecentTasks(100, 2)) {
            if (!(recentTask.baseIntent == null || recentTask.baseIntent.getComponent() == null || !packageName.equals(recentTask.baseIntent.getComponent().getPackageName()))) {
                Class<ActivityManager> cls = ActivityManager.class;
                try {
                    Method removeTask = cls.getMethod("removeTask", new Class[]{Integer.TYPE});
                    removeTask.setAccessible(true);
                    removeTask.invoke(activityManager, new Object[]{Integer.valueOf(recentTask.persistentId)});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}