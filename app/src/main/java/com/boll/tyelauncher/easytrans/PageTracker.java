package com.boll.tyelauncher.easytrans;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class PageTracker {
    private static final String TAG = "PageTracker";
    private static boolean initialized;
    private static b mTrackerImpl = new b();

    public static void destroy() {
        if (initialized) {
            mTrackerImpl.a(Message.obtain((Handler) null, 9));
        } else if (Logging.isDebugLogging()) {
            a.a(TAG, "Please initialize PageTracker first, call PageTracker.init(application, config)");
        }
    }

    public static void init(Application application, Config config) {
        if (application == null && Logging.isDebugLogging()) {
            a.b(TAG, "application cannot be null");
        }
        b bVar = mTrackerImpl;
        if (application == null) {
            throw new IllegalArgumentException("Application can't be null");
        }
        if (bVar.d == null) {
            if (Logging.isDebugLogging()) {
                a.a("PageAnalysis", "Logger | tarcker init");
            }
            b.b = config;
            bVar.a = application;
            bVar.a.registerActivityLifecycleCallbacks(new com.iflytek.pagestat.a.b.a(config.openActivityAutoTrack));
            bVar.c = new b.a(Thread.getDefaultUncaughtExceptionHandler(), bVar);
            Thread.setDefaultUncaughtExceptionHandler(bVar.c);
            HandlerThread handlerThread = new HandlerThread("StatsSdkHandlerThread", 10);
            handlerThread.start();
            bVar.d = new b.C0009b(handlerThread.getLooper(), application.getApplicationContext());
            bVar.d.obtainMessage(100001).sendToTarget();
        }
        initialized = true;
    }

    public static void onPageEnter(String pageName) {
        if (initialized) {
            b bVar = mTrackerImpl;
            if (Logging.isDebugLogging()) {
                a.a("PageAnalysis", "onPageEnter | page:" + pageName);
            }
            Message obtain = Message.obtain((Handler) null, 2);
            Bundle bundle = new Bundle();
            bundle.putString("pkgn", pageName);
            obtain.setData(bundle);
            bVar.a(obtain);
        } else if (Logging.isDebugLogging()) {
            a.a(TAG, "Please initialize PageTracker first, call PageTracker.init(application, config)");
        }
    }

    public static void onPageExit(String pageName) {
        if (initialized) {
            b bVar = mTrackerImpl;
            if (Logging.isDebugLogging()) {
                a.a("PageAnalysis", "onPageExit | page:" + pageName);
            }
            Message obtain = Message.obtain((Handler) null, 3);
            Bundle bundle = new Bundle();
            bundle.putString("pkgn", pageName);
            obtain.setData(bundle);
            bVar.a(obtain);
        } else if (Logging.isDebugLogging()) {
            a.a(TAG, "Please initialize PageTracker first, call PageTracker.init(application, config)");
        }
    }

    public static void onPause(Context context) {
        if (initialized) {
            b bVar = mTrackerImpl;
            if (Logging.isDebugLogging()) {
                a.a("PageAnalysis", "onPause");
            }
            bVar.a(Message.obtain((Handler) null, 5));
        } else if (Logging.isDebugLogging()) {
            a.a(TAG, "Please initialize PageTracker first, call PageTracker.init(application, config)");
        }
    }

    public static void onResume(Context context) {
        if (initialized) {
            mTrackerImpl.a(Message.obtain((Handler) null, 4));
        } else if (Logging.isDebugLogging()) {
            a.a(TAG, "Please initialize PageTracker first, call PageTracker.init(application, config)");
        }
    }
}
