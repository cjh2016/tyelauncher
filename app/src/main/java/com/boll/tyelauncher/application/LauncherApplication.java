package com.boll.tyelauncher.application;

/**
 * @author: caijianhui
 * @date: 2020/6/29 15:45
 * @description:
 */
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.model.AppContants;
import com.boll.tyelauncher.util.ProcessUtils;

public class LauncherApplication extends Application {
    private static final String BROADCAST_ACTION_DISC = "com.toycloud.permissions.broadcast_token_error";
    private static final String BROADCAST_PERMISSION_DISC = "com.toycloud.permissions.BROADCAST_TOKEN_ERROR";
    private static final String TAG = "LauncherApplication";
    private static Context context;

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = this;
    }

    @Override
    public void onCreate() {
        long time = System.currentTimeMillis();
        super.onCreate();
        String processName = null;
        try {
            processName = ProcessUtils.getProcessName();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        int pid = Process.myPid();
        try {
            LogUtils.setDebugLogging(false);
            Logger.addLogAdapter(new AndroidLogAdapter() {
                public boolean isLoggable(int priority, String tag) {
                    return false;
                }
            });
            Utils.init(this);
            boolean isMainProcess = true;
            if (processName != null) {
                isMainProcess = TextUtils.equals(processName, BuildConfig.APPLICATION_ID);
            }
            LogAgentHelper.init(this, "CGBHC1IM", BuildConfig.VERSION_NAME);
            BaseParameterManager.getInstance().setupBaseParameters(this, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, "CGBHC1IM", "5d979ff4", AiStudyConstants.MODEL_ID);
            HttpBaseHelper.setUserInfo(SharepreferenceUtil.getSharepferenceInstance(this).getUserInfo());
            if (isMainProcess) {
                registerReceiver(new TokenErrorReceiver(), new IntentFilter(BROADCAST_ACTION_DISC), BROADCAST_PERMISSION_DISC, (Handler) null);
            }
            if (isMainProcess) {
                SafeTask.setErrorHandler(LauncherApplication$$Lambda$0.$instance);
                FileCacheFactory.initialize(this);
                initForbiddenShowApp();
                loadAllAppIcon();
                AlarmClockService.startLoop(this);
                SystemUtils.hookWebView();
                StudyDailyReportMsgManager.getInstance().restore(this);
            }
            Log.d("fgtian", "LauncherApplication耗时统计 (" + processName + "：" + pid + "-V2): " + (System.currentTimeMillis() - time));
            MessageReportManager.mBaseUrl = "https://k12-api.openspeech.cn/";
        } catch (Throwable th) {
            Log.d("fgtian", "LauncherApplication耗时统计 (" + processName + "：" + pid + "-V2): " + (System.currentTimeMillis() - time));
            throw th;
        }
    }

    public static Context getContext() {
        return context;
    }

    /* access modifiers changed from: protected */
    public String getIflytekId() {
        return "57c99cfa";
    }

    /* access modifiers changed from: protected */
    public boolean enableBaiduLoc() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean enableJPush() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean enableExceptionCaught() {
        return false;
    }

    private void initForbiddenShowApp() {
        GlobalVariable.appInfoList_forbidden.add("com.android.calendar");
        GlobalVariable.appInfoList_forbidden.add("com.cyanogenmod.filemanager");
        GlobalVariable.appInfoList_forbidden.add("com.android.dialer");
        GlobalVariable.appInfoList_forbidden.add("com.android.music");
        GlobalVariable.appInfoList_forbidden.add("com.qualcomm.qti.logkit");
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.ChineseStroke");
        GlobalVariable.appInfoList_forbidden.add("com.guo2");
        GlobalVariable.appInfoList_forbidden.add("corg.chromium.webview_shell");
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.xiri");
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.inputmethod");
        GlobalVariable.appInfoList_forbidden.add("com.toycloud.app.myskill");
        GlobalVariable.appInfoList_forbidden.add("com.toycloud.updateservice");
        GlobalVariable.appInfoList_forbidden.add("com.android.inputmethod.latin");
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.recommend_tsp");
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.dictatewords");
        GlobalVariable.appInfoList_forbidden.add(AppContants.RECITE_BOOK);
        GlobalVariable.appInfoList_forbidden.add(GlobalVariable.ENGLISH_LSP_PKG);
        GlobalVariable.appInfoList_forbidden.add(GlobalVariable.SEARCHBYPHOTO_PKG);
        GlobalVariable.appInfoList_forbidden.add(GlobalVariable.SEARCHBYPHOTO_PKG);
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.xiri");
        GlobalVariable.appInfoList_forbidden.add(GlobalVariable.MICROCLASS_PKG);
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.k12aitstatistics");
        GlobalVariable.appInfoList_forbidden.add("com.ets100.study");
        GlobalVariable.appInfoList_forbidden.add(BuildConfig.APPLICATION_ID);
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.wrongnotebook");
        GlobalVariable.appInfoList_forbidden.add("com.iflytek.recommend_tsp");
    }

    public void loadAllAppIcon() {
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri) null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        GlobalVariable.allapps = getPackageManager().queryIntentActivities(mainIntent, 0);
    }
}