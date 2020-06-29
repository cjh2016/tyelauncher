package com.boll.tyelauncher.easytrans;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import java.util.Map;

public class LogAgentHelper {
    public static final String OPLOG = "oplog";
    public static final String TAG = "LogAgentHelper";

    public static final void init(Application application, AppInfo appInfo) {
        LogAgent.initWithPageStats(application, appInfo);
        MobilequalityHelper.init(application, appInfo.getAppid(), appInfo.getVersion(), false);
    }

    public static void onOPLogEvent(String name, Map<String, String> params) {
        try {
            LogAgent.onEvent("oplog", name, params);
        } catch (Throwable th) {
        }
    }

    public static void onActive() {
        try {
            LogAgent.onActiveEvent("", (String) null);
        } catch (Throwable th) {
        }
    }

    public static void onPageStart(String pageName) {
        try {
            if (!TextUtils.isEmpty(pageName)) {
                Log.d("LogAgentHelper", "onPageStart: " + pageName);
                LogAgent.onPageEnterEvent(pageName);
            }
        } catch (Throwable th) {
        }
    }

    public static void onPageExit(String pageName) {
        try {
            if (!TextUtils.isEmpty(pageName)) {
                Log.d("LogAgentHelper", "onPageExit: " + pageName);
                LogAgent.onPageExitEvent(pageName);
            }
        } catch (Throwable th) {
        }
    }
}