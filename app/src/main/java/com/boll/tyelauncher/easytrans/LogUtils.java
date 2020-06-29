package com.boll.tyelauncher.easytrans;


import android.util.Log;

public class LogUtils {
    private static final String PRE_TAG = "SDK_";
    protected static boolean mLoggingEnabled = true;
    private static String mPreTag = PRE_TAG;

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
        return "IFLY_" + mPreTag;
    }
}
