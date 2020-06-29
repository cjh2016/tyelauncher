package com.boll.tyelauncher.easytrans;


import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LogAgent {
    private static final String ACTION_LOG_SERVICE = "com.iflytek.easytrans.logservice.service.EventService";
    private static final String APP_ID = "appid";
    /* access modifiers changed from: private */
    public static String APP_SESSION_ID = null;
    private static final String APP_VERSION = "version";
    private static final String CHANNEL_ID = "99010001";
    public static final String KEY_SESSION_ID = "sessionid";
    private static final String PACKAGE_NAME_LOG = "com.iflytek.cbg.aistudy.logservice";
    private static final int SEND_ACTIVE_EVENT_LOG = 32;
    private static final int SEND_ALL_CACHE_LOG = 1;
    private static final int SEND_CONTROL_EVENT_LOG = 4;
    private static final int SEND_EVENT_LOG = 2;
    private static final int SEND_JSON_EVENT_LOG = 64;
    private static final int SEND_LIST_EVENT_LOG = 8;
    private static final int SEND_STATUS_EVENT_LOG = 16;
    private static final String TAG = "LogAgent";
    private static ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d(LogAgent.TAG, "onServiceConnected | ComponentName = " + name + ", IBinder = " + service);
            IDataReportInterface unused = LogAgent.mDataReportInterface = IDataReportInterface.Stub.asInterface(service);
            LogAgent.mHandler.sendEmptyMessage(1);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d(LogAgent.TAG, "onServiceDisconnected | ComponentName = " + name);
            IDataReportInterface unused = LogAgent.mDataReportInterface = null;
        }

        @Override
        public void onBindingDied(ComponentName name) {
            LogUtils.d(LogAgent.TAG, "onBindingDied | ComponentName = " + name);
            IDataReportInterface unused = LogAgent.mDataReportInterface = null;
        }
    };
    private static ArrayList<Params.ActiveEventParams> mActiveEventParams = new ArrayList<>();
    private static AppInfo mAppInfo = null;
    private static Context mContext = null;
    private static ArrayList<Params.ControlEventParams> mControlEventParams = new ArrayList<>();
    /* access modifiers changed from: private */
    public static IDataReportInterface mDataReportInterface = null;
    private static ArrayList<Params.EventJsonParams> mEventJsonParams = new ArrayList<>();
    private static final ArrayList<Params.EventParams> mEventParams = new ArrayList<>();
    /* access modifiers changed from: private */
    public static AgentHandler mHandler = null;
    private static ArrayList<Params.ListEventParams> mListEventParams = new ArrayList<>();
    private static ArrayList<Params.StatusEventParams> mStatusEventParams = new ArrayList<>();

    public static void initWithPageStats(Application application, AppInfo appInfo) {
        LogUtils.d(TAG, "init appInfo = " + appInfo);
        Context context = application.getApplicationContext();
        mContext = context;
        mAppInfo = appInfo;
        PageTracker.init(application, new Config.Builder().setOpenActivityAutoTrack(false).setRecorder(new ILogger() {
            public void onEvent(String eventType, Map map) {
                Log.d(LogAgent.TAG, "PageTracker.onEvent: " + eventType + " -> " + map);
                LogAgent.onEvent(eventType, (String) null, map);
            }

            public void onSessionBegin(String s) {
                String unused = LogAgent.APP_SESSION_ID = s;
            }

            public void onSessionEnd() {
                String unused = LogAgent.APP_SESSION_ID = null;
            }
        }).build());
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mHandler = new AgentHandler(handlerThread.getLooper());
        bindLogService(context);
    }

    public static void init(@NonNull Context context, AppInfo appInfo) {
        LogUtils.d(TAG, "init appInfo = " + appInfo);
        mContext = context;
        mAppInfo = appInfo;
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mHandler = new AgentHandler(handlerThread.getLooper());
        bindLogService(context);
    }

    public static void onPageEnterEvent(String pageName) {
        Log.d(TAG, "onPageEnterEvent: " + pageName);
        PageTracker.onPageEnter(pageName);
    }

    public static void onPageExitEvent(String pageName) {
        Log.d(TAG, "onPageExitEvent: " + pageName);
        PageTracker.onPageExit(pageName);
    }

    public static void onEvent(String eventType, String name, Map<String, String> data) {
        if (eventType == null) {
            LogUtils.d(TAG, "eventType is null");
            return;
        }
        Map<String, String> cacheMap = null;
        if (data != null) {
            cacheMap = new HashMap<>(data);
        }
        Map<String, String> map = addAppInfo(cacheMap);
        Message message = Message.obtain();
        message.obj = new Params.EventParams(eventType, name, map);
        message.what = 2;
        mHandler.sendMessage(message);
    }

    public static void onEvent(String eventType, String controlCode, String eventName, Map<String, String> parameters) {
        if (eventType == null) {
            LogUtils.d(TAG, "eventType is null");
            return;
        }
        Map<String, String> cacheMap = null;
        if (parameters != null) {
            cacheMap = new HashMap<>(parameters);
        }
        Map<String, String> map = addAppInfo(cacheMap);
        Message message = Message.obtain();
        message.obj = new Params.ControlEventParams(eventType, controlCode, eventName, map);
        message.what = 4;
        mHandler.sendMessage(message);
    }

    public static void onEventList(String eventType, String controlCode, String jsonArrayLogs) {
        if (eventType == null) {
            LogUtils.d(TAG, "eventType is null");
            return;
        }
        Message message = Message.obtain();
        message.obj = new Params.ListEventParams(eventType, controlCode, jsonArrayLogs);
        message.what = 8;
        mHandler.sendMessage(message);
    }

    public static void onEventJson(String eventType, String controlCode, String eventName, String jsonObject) {
        if (eventType == null) {
            LogUtils.d(TAG, "eventType is null");
            return;
        }
        Message message = Message.obtain();
        message.obj = new Params.EventJsonParams(eventType, controlCode, eventName, jsonObject);
        message.what = 64;
        mHandler.sendMessage(message);
    }

    public static void onStatusEvent(String eventType, String controlCode, String name, int count) {
        if (eventType == null) {
            LogUtils.d(TAG, "eventType is null");
            return;
        }
        Message message = Message.obtain();
        message.obj = new Params.StatusEventParams(eventType, controlCode, name, count);
        message.what = 16;
        mHandler.sendMessage(message);
    }

    public static void onActiveEvent(String name) {
        Message message = Message.obtain();
        message.obj = new Params.ActiveEventParams(name, mAppInfo.getAppid(), mAppInfo.getVersion(), CHANNEL_ID, "");
        message.what = 32;
        mHandler.sendMessage(message);
    }

    public static void onActiveEvent(String name, String bundleInfo) {
        Message message = Message.obtain();
        message.obj = new Params.ActiveEventParams(name, mAppInfo.getAppid(), mAppInfo.getVersion(), CHANNEL_ID, bundleInfo);
        message.what = 32;
        mHandler.sendMessage(message);
    }

    public static void onError(Throwable error) {
        if (error != null) {
            CrashCollector.postCatchedException(error);
        }
    }

    private static void bindLogService(Context context) {
        if (context == null || !PackageUtils.isPackageInstalled(context, PACKAGE_NAME_LOG) || mDataReportInterface != null) {
            LogUtils.d(TAG, "not inited or the logservice App is not install or the connection is ready");
            return;
        }
        LogUtils.d(TAG, "bindLogService");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PACKAGE_NAME_LOG, ACTION_LOG_SERVICE));
        context.bindService(intent, connection, 1);
    }

    static class AgentHandler extends Handler {
        private AgentHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LogAgent.sendAllCacheLog();
                    return;
                case 2:
                    LogAgent.sendEventLog((Params.EventParams) msg.obj);
                    return;
                case 4:
                    LogAgent.sendControlEventLog((Params.ControlEventParams) msg.obj);
                    return;
                case 8:
                    LogAgent.sendListEventLog((Params.ListEventParams) msg.obj);
                    return;
                case 16:
                    LogAgent.sendStatusEventLog((Params.StatusEventParams) msg.obj);
                    return;
                case 32:
                    LogAgent.sendActiveEventLog((Params.ActiveEventParams) msg.obj);
                    return;
                case 64:
                    LogAgent.sendJsonEventLog((Params.EventJsonParams) msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public static void sendAllCacheLog() {
        if (mEventParams.size() != 0 || mControlEventParams.size() != 0 || mListEventParams.size() != 0 || mStatusEventParams.size() != 0 || mActiveEventParams.size() != 0 || mEventJsonParams.size() != 0) {
            if (mEventParams.size() != 0) {
                synchronized (mEventParams) {
                    Iterator<Params.EventParams> it = mEventParams.iterator();
                    while (it.hasNext()) {
                        Params.EventParams params = it.next();
                        try {
                            mDataReportInterface.onEvent(params.eventType, params.name, params.map);
                            it.remove();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (mControlEventParams.size() != 0) {
                synchronized (mControlEventParams) {
                    Iterator<Params.ControlEventParams> it2 = mControlEventParams.iterator();
                    while (it2.hasNext()) {
                        Params.ControlEventParams params2 = it2.next();
                        try {
                            mDataReportInterface.onControlEvent(params2.eventType, params2.controlCode, params2.name, params2.map);
                            it2.remove();
                        } catch (RemoteException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
            if (mActiveEventParams.size() != 0) {
                synchronized (mActiveEventParams) {
                    Iterator<Params.ActiveEventParams> it3 = mActiveEventParams.iterator();
                    while (it3.hasNext()) {
                        Params.ActiveEventParams params3 = it3.next();
                        try {
                            mDataReportInterface.onActiveEvent(params3.name, params3.appId, params3.appVersion, params3.channelId, params3.bundleInfo);
                            it3.remove();
                        } catch (RemoteException e3) {
                            e3.printStackTrace();
                        }
                    }
                }
            }
            if (mStatusEventParams.size() != 0) {
                synchronized (mStatusEventParams) {
                    Iterator<Params.StatusEventParams> it4 = mStatusEventParams.iterator();
                    while (it4.hasNext()) {
                        Params.StatusEventParams params4 = it4.next();
                        try {
                            mDataReportInterface.onStatsEvent(params4.eventType, params4.controlCode, params4.name, params4.count);
                            it4.remove();
                        } catch (RemoteException e4) {
                            e4.printStackTrace();
                        }
                    }
                }
            }
            if (mListEventParams.size() != 0) {
                synchronized (mListEventParams) {
                    Iterator<Params.ListEventParams> it5 = mListEventParams.iterator();
                    while (it5.hasNext()) {
                        Params.ListEventParams params5 = it5.next();
                        try {
                            mDataReportInterface.onEventList(params5.eventType, params5.controlCode, params5.jsonArrayLogs);
                            it5.remove();
                        } catch (RemoteException e5) {
                            e5.printStackTrace();
                        }
                    }
                }
            }
            if (mEventJsonParams.size() != 0) {
                synchronized (mEventJsonParams) {
                    Iterator<Params.EventJsonParams> it6 = mEventJsonParams.iterator();
                    while (it6.hasNext()) {
                        Params.EventJsonParams params6 = it6.next();
                        try {
                            mDataReportInterface.onEventJson(params6.eventType, params6.controlCode, params6.name, params6.object);
                            it6.remove();
                        } catch (RemoteException e6) {
                            e6.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void sendEventLog(Params.EventParams params) {
        Context context = mContext;
        try {
            LogUtils.d(TAG, "onEventParams =  " + params);
            if (mDataReportInterface != null) {
                if (mEventParams.size() != 0) {
                    synchronized (mEventParams) {
                        Iterator<Params.EventParams> it = mEventParams.iterator();
                        while (it.hasNext()) {
                            Params.EventParams param = it.next();
                            mDataReportInterface.onEvent(param.eventType, param.name, param.map);
                            it.remove();
                        }
                    }
                }
                mDataReportInterface.onEvent(params.eventType, params.name, params.map);
                return;
            }
            synchronized (mEventParams) {
                mEventParams.add(params);
            }
            bindLogService(context);
        } catch (RemoteException var2) {
            var2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public static void sendControlEventLog(Params.ControlEventParams params) {
        Context context = mContext;
        try {
            LogUtils.d(TAG, " onContrloEventParams = " + params);
            if (mDataReportInterface != null) {
                if (mControlEventParams.size() != 0) {
                    synchronized (mControlEventParams) {
                        Iterator<Params.ControlEventParams> it = mControlEventParams.iterator();
                        while (it.hasNext()) {
                            Params.ControlEventParams param = it.next();
                            mDataReportInterface.onControlEvent(param.eventType, param.controlCode, param.name, param.map);
                            it.remove();
                        }
                    }
                }
                mDataReportInterface.onControlEvent(params.eventType, params.controlCode, params.name, params.map);
                return;
            }
            synchronized (mControlEventParams) {
                mControlEventParams.add(params);
            }
            bindLogService(context);
        } catch (RemoteException var2) {
            var2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public static void sendStatusEventLog(Params.StatusEventParams params) {
        Context context = mContext;
        try {
            LogUtils.d(TAG, "onStatusEventParams = " + params);
            if (mDataReportInterface != null) {
                if (mStatusEventParams.size() != 0) {
                    synchronized (mStatusEventParams) {
                        Iterator<Params.StatusEventParams> it = mStatusEventParams.iterator();
                        while (it.hasNext()) {
                            Params.StatusEventParams param = it.next();
                            mDataReportInterface.onStatsEvent(param.eventType, param.controlCode, param.name, param.count);
                            it.remove();
                        }
                    }
                }
                mDataReportInterface.onStatsEvent(params.eventType, params.controlCode, params.name, params.count);
                return;
            }
            synchronized (mStatusEventParams) {
                mStatusEventParams.add(params);
            }
            bindLogService(context);
        } catch (RemoteException var2) {
            var2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public static void sendActiveEventLog(Params.ActiveEventParams params) {
        Context context = mContext;
        try {
            LogUtils.d(TAG, "onActiveEventParams = " + params);
            if (mDataReportInterface != null) {
                if (mActiveEventParams.size() != 0) {
                    synchronized (mActiveEventParams) {
                        Iterator<Params.ActiveEventParams> it = mActiveEventParams.iterator();
                        while (it.hasNext()) {
                            Params.ActiveEventParams param = it.next();
                            mDataReportInterface.onActiveEvent(param.name, param.appId, param.appVersion, param.channelId, param.bundleInfo);
                            it.remove();
                        }
                    }
                }
                mDataReportInterface.onActiveEvent(params.name, params.appId, params.appVersion, params.channelId, params.bundleInfo);
                return;
            }
            synchronized (mActiveEventParams) {
                mActiveEventParams.add(params);
            }
            bindLogService(context);
        } catch (RemoteException var2) {
            var2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public static void sendListEventLog(Params.ListEventParams params) {
        Context context = mContext;
        try {
            LogUtils.d(TAG, " onListEventParams = " + params);
            if (mDataReportInterface != null) {
                if (mListEventParams.size() != 0) {
                    synchronized (mListEventParams) {
                        Iterator<Params.ListEventParams> it = mListEventParams.iterator();
                        while (it.hasNext()) {
                            Params.ListEventParams param = it.next();
                            mDataReportInterface.onEventList(param.eventType, param.controlCode, param.jsonArrayLogs);
                            it.remove();
                        }
                    }
                }
                mDataReportInterface.onEventList(params.eventType, params.controlCode, params.jsonArrayLogs);
                return;
            }
            synchronized (mListEventParams) {
                mListEventParams.add(params);
            }
            bindLogService(context);
        } catch (RemoteException var2) {
            var2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public static void sendJsonEventLog(Params.EventJsonParams params) {
        Context context = mContext;
        try {
            LogUtils.d(TAG, " onEventJsonParams = " + params);
            if (mDataReportInterface != null) {
                if (mEventJsonParams.size() != 0) {
                    synchronized (mEventJsonParams) {
                        Iterator<Params.EventJsonParams> it = mEventJsonParams.iterator();
                        while (it.hasNext()) {
                            Params.EventJsonParams param = it.next();
                            mDataReportInterface.onEventJson(param.eventType, param.controlCode, param.name, param.object);
                            it.remove();
                        }
                    }
                }
                mDataReportInterface.onEventJson(params.eventType, params.controlCode, params.name, params.object);
                return;
            }
            synchronized (mEventJsonParams) {
                mEventJsonParams.add(params);
            }
            bindLogService(context);
        } catch (RemoteException var2) {
            var2.printStackTrace();
        }
    }

    private static Map<String, String> addAppInfo(Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (mAppInfo != null) {
            if (mAppInfo.getAppid() != null) {
                map.put("appid", mAppInfo.getAppid());
            }
            if (mAppInfo.getVersion() != null) {
                map.put("version", mAppInfo.getVersion());
            }
        }
        if (APP_SESSION_ID != null) {
            map.put(KEY_SESSION_ID, APP_SESSION_ID);
        }
        return map;
    }
}
