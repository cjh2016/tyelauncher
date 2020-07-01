package com.boll.tyelauncher.util;


import android.util.Log;

public class Logging {
    private static final String PRE_TAG = "LAUNCHER_";
    protected static boolean mLoggingEnabled = true;
    private static String mPreTag = PRE_TAG;
    private static long sLogBeginTime;

    public static void setPreTag(String preTag) {
        if (preTag != null && preTag.length() > 0 && PRE_TAG.equals(mPreTag)) {
            mPreTag = preTag;
        }
    }

    public static void setDebugLogging(boolean enabled) {
        mLoggingEnabled = enabled;
    }

    public static boolean isDebugLogging() {
        return mLoggingEnabled;
    }

    public static int v(String tag, String msg) {
        if (mLoggingEnabled) {
            return Log.v(getPreTag() + tag, msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (mLoggingEnabled) {
            return Log.v(getPreTag() + tag, msg, tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (mLoggingEnabled) {
            return Log.d(getPreTag() + tag, msg);
        }
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (mLoggingEnabled) {
            return Log.d(getPreTag() + tag, msg, tr);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if (mLoggingEnabled) {
            return Log.i(getPreTag() + tag, msg);
        }
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (mLoggingEnabled) {
            return Log.i(getPreTag() + tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (mLoggingEnabled) {
            return Log.w(getPreTag() + tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (mLoggingEnabled) {
            return Log.w(getPreTag() + tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, Throwable tr) {
        if (mLoggingEnabled) {
            return Log.w(getPreTag() + tag, tr);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (mLoggingEnabled) {
            return Log.e(getPreTag() + tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (mLoggingEnabled) {
            return Log.e(getPreTag() + tag, msg, tr);
        }
        return 0;
    }

    public static String getPreTag() {
        return mPreTag;
    }

    public static void begin() {
        sLogBeginTime = System.currentTimeMillis();
    }

    public static void end(String tag, String str) {
        if (0 != sLogBeginTime) {
            d(tag, str + (System.currentTimeMillis() - sLogBeginTime));
            sLogBeginTime = 0;
        }
    }
}
