package com.boll.tyelauncher.model.launcher;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.model.ForbiddenAPP;
import com.boll.tyelauncher.model.ForbiddenAppListBean;
import com.boll.tyelauncher.model.ForbiddenTime;
import com.boll.tyelauncher.model.launcher.bean.ForbbidenAppConfig;
import com.boll.tyelauncher.model.launcher.bean.ForbbidenConfigs;
import com.boll.tyelauncher.model.launcher.bean.ForbbidenTimeConfig;
import com.boll.tyelauncher.model.launcher.helper.AppLockHelper;
import com.boll.tyelauncher.model.launcher.impl.ForbbidenTimeHelper;
import com.boll.tyelauncher.model.launcher.interfaces.IRemoteControllerListener;
import com.boll.tyelauncher.util.GsonUtils;
import com.boll.tyelauncher.util.ListUtils;
import com.boll.tyelauncher.util.PadInfoUtil;
import com.boll.tyelauncher.util.SharepreferenceUtil;
import com.iflytek.utils.LauncherExecutors;
import com.iflytek.utils.WorkThreadExecutor;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.util.receiver.EventReceiver;
import com.toycloud.launcher.util.receiver.ReceiverHandler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class RemoteController implements LauncherModel.IPrepareWork, ForbbidenTimeHelper.IForbbidenTimeListener {
    private static final String FORBBIDEN_CONFIG_KEY = "forbbiden_configs";
    private static final String FORBBIDEN_TIME_CONFIG_KEY = "forbbiden_time_configs";
    private static final String KEY = "use_old_forbbiden_config";
    private static final String TAG = "RemoteController";
    private static RemoteController sInstance;
    /* access modifiers changed from: private */
    public final Context mContext;
    private EventReceiver mEventReceiver;
    /* access modifiers changed from: private */
    public WorkThreadExecutor mExecutor = LauncherExecutors.sAppLoaderExecutor;
    /* access modifiers changed from: private */
    public Set<String> mFobbidenApps = new ConcurrentSkipListSet();
    /* access modifiers changed from: private */
    public Set<String> mFunApps = new ConcurrentSkipListSet();
    private boolean mIsDestroy = false;
    private boolean mIsInited = false;
    /* access modifiers changed from: private */
    public IRemoteControllerListener mListener;
    /* access modifiers changed from: private */
    public final SharepreferenceUtil mSharedPref;
    /* access modifiers changed from: private */
    public final ForbbidenTimeHelper mTimeHelper;
    /* access modifiers changed from: private */
    public ForbiddenTime mWeekDayConfig;
    /* access modifiers changed from: private */
    public ForbiddenTime mWeekendConfig;

    public static RemoteController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RemoteController(context);
        }
        return sInstance;
    }

    private RemoteController(Context context) {
        this.mContext = context.getApplicationContext();
        this.mSharedPref = SharepreferenceUtil.getSharepferenceInstance(this.mContext);
        this.mTimeHelper = new ForbbidenTimeHelper(this.mContext, this);
    }

    public void setListener(IRemoteControllerListener listener) {
        this.mListener = listener;
    }

    @Override
    public void prepareOnWorkThread() {
        if (this.mSharedPref.getBoolean(KEY, true)) {
            this.mSharedPref.setKV(KEY, false);
            loadCacheOld();
        } else {
            loadCacheNew();
        }
        this.mExecutor.execute(new UpdateForbiddenContentProviderTask(this.mFobbidenApps, this.mFunApps));
    }

    private void processConfigs(ForbbidenConfigs cache) {
        String[] apps;
        String[] apps2;
        if (cache != null) {
            if (!TextUtils.isEmpty(cache.mForbiddenApps) && (apps2 = cache.mForbiddenApps.split(",")) != null) {
                for (String app : apps2) {
                    if (app != null) {
                        this.mFobbidenApps.add(app.trim());
                    }
                }
            }
            if (!TextUtils.isEmpty(cache.mTimeControlList) && (apps = cache.mTimeControlList.split(",")) != null) {
                for (String app2 : apps) {
                    if (app2 != null) {
                        this.mFunApps.add(app2.trim());
                    }
                }
            }
            if (!TextUtils.isEmpty(cache.mTimeControlWeekDay)) {
                this.mWeekDayConfig = (ForbiddenTime) GsonUtils.changeJsonToBean(cache.mTimeControlWeekDay, ForbiddenTime.class);
                if (this.mWeekDayConfig != null && !this.mWeekDayConfig.isValid()) {
                    this.mWeekDayConfig = null;
                }
            }
            if (!TextUtils.isEmpty(cache.mTimeControlWeekEnd)) {
                this.mWeekendConfig = (ForbiddenTime) GsonUtils.changeJsonToBean(cache.mTimeControlWeekEnd, ForbiddenTime.class);
                if (this.mWeekendConfig != null && !this.mWeekendConfig.isValid()) {
                    this.mWeekendConfig = null;
                }
            }
        }
    }

    private void loadCacheOld() {
        ForbbidenConfigs cache = ForbbidenConfigs.queryFromContentProvider(this.mContext);
        processConfigs(cache);
        if (cache != null) {
            if (!this.mFobbidenApps.isEmpty()) {
                ForbbidenAppConfig config = new ForbbidenAppConfig();
                for (String pkg : this.mFobbidenApps) {
                    config.addApp(pkg);
                }
                this.mSharedPref.setKV(FORBBIDEN_CONFIG_KEY, GsonUtils.toJson(config));
            }
            if (this.mFunApps.isEmpty()) {
                return;
            }
            if (this.mWeekDayConfig != null || this.mWeekendConfig != null) {
                ForbbidenTimeConfig config2 = new ForbbidenTimeConfig();
                config2.setApps(this.mFunApps);
                config2.mWeekDayConfig = this.mWeekDayConfig;
                config2.mWeekendConfig = this.mWeekendConfig;
                this.mSharedPref.setKV(FORBBIDEN_TIME_CONFIG_KEY, GsonUtils.toJson(config2));
            }
        }
    }

    private void loadCacheNew() {
        ForbbidenTimeConfig config;
        ForbbidenAppConfig config2;
        String json = this.mSharedPref.getString(FORBBIDEN_CONFIG_KEY, (String) null);
        if (!(TextUtils.isEmpty(json) || (config2 = (ForbbidenAppConfig) GsonUtils.changeJsonToBean(json, ForbbidenAppConfig.class)) == null || config2.mForbbidenApps == null)) {
            for (String pkg : config2.mForbbidenApps) {
                this.mFobbidenApps.add(pkg);
            }
        }
        String json2 = this.mSharedPref.getString(FORBBIDEN_TIME_CONFIG_KEY, (String) null);
        if (!TextUtils.isEmpty(json2) && (config = (ForbbidenTimeConfig) GsonUtils.changeJsonToBean(json2, ForbbidenTimeConfig.class)) != null && config.mFunApps != null) {
            if (config.mWeekendConfig != null || config.mWeekDayConfig != null) {
                for (String pkg2 : config.mFunApps) {
                    this.mFunApps.add(pkg2);
                }
                this.mWeekDayConfig = config.mWeekDayConfig;
                this.mWeekendConfig = config.mWeekendConfig;
            }
        }
    }

    public void prepareRemote(boolean isNetworkConnected) {
        if (!this.mIsDestroy) {
            long delay = 0;
            if (isNetworkConnected) {
                delay = 3000;
            }
            LauncherExecutors.sMainExecutor.executeDelay(new Runnable() {
                @Override
                public void run() {
                    RemoteController.this.queryForbiddenList(0);
                }
            }, delay);
            registerReceiver();
            this.mIsInited = true;
        }
    }

    private void registerReceiver() {
        if (this.mEventReceiver == null) {
            this.mEventReceiver = new EventReceiver();
            PushEventHandler handler = new PushEventHandler();
            this.mEventReceiver.addAction("action.app.forbbiden.CHANGED", handler);
            this.mEventReceiver.addAction("action.app.forbbiden.time.CHANGED", handler);
            this.mEventReceiver.addAction("android.intent.action.TIME_TICK", new TimeTickHandler());
            this.mEventReceiver.register(this.mContext);
        }
    }

    public void unInit() {
        this.mIsDestroy = true;
        if (this.mEventReceiver != null) {
            this.mEventReceiver.unRegister(this.mContext);
            this.mEventReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public void queryForbiddenList(final int retryIndex) {
        Logger.d(TAG, "正在请求禁止app列表");
        LauncherHttpHelper.getLauncherService().getForbiddenAPPList(new PadInfoUtil(this.mContext).getSnCode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ForbiddenAppListBean>(this.mContext, false) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                super.onError(e);
                Logger.e(RemoteController.TAG, "getForbiddenAPPList error", e);
                if (retryIndex == 0) {
                    Logger.d(RemoteController.TAG, "正在请求娱乐app列表");
                    RemoteController.this.mTimeHelper.query();
                }
                if (retryIndex < 3) {
                    RemoteController.this.queryForbiddenList(retryIndex + 1);
                }
            }

            public void onNext(ForbiddenAppListBean response) {
                super.onNext(response);
                if (retryIndex == 0) {
                    Logger.d(RemoteController.TAG, "正在请求娱乐app列表");
                    RemoteController.this.mTimeHelper.query();
                }
                if (response != null && response.getData() != null && response.getData().size() != 0) {
                    List<ForbiddenAppListBean.DataBean> data = response.getData();
                    RemoteController.this.mFobbidenApps.clear();
                    for (ForbiddenAppListBean.DataBean bean : data) {
                        RemoteController.this.mFobbidenApps.add(bean.getAppCode());
                    }
                    if (RemoteController.this.mListener != null) {
                        RemoteController.this.mListener.onRemoteConfigChanged(1);
                    }
                    RemoteController.this.mExecutor.execute(new UpdateForbbidenListTask(new ArrayList<>(RemoteController.this.mFobbidenApps)));
                    RemoteController.this.mExecutor.execute(new KillProcessTask(this.mContext, new ArrayList(RemoteController.this.mFobbidenApps), false));
                } else if (response.getStatus() == 0) {
                    if (!RemoteController.this.mFobbidenApps.isEmpty()) {
                        RemoteController.this.mFobbidenApps.clear();
                        if (RemoteController.this.mListener != null) {
                            RemoteController.this.mListener.onRemoteConfigChanged(1);
                        }
                        RemoteController.this.mExecutor.execute(new UpdateForbbidenListTask(new ArrayList<>(RemoteController.this.mFobbidenApps)));
                        RemoteController.this.mExecutor.execute(new KillProcessTask(this.mContext, new ArrayList(RemoteController.this.mFobbidenApps), false));
                    }
                } else if (retryIndex < 3) {
                    RemoteController.this.queryForbiddenList(retryIndex + 1);
                }
            }
        });
    }

    public boolean isForbbiden(String pkg) {
        ForbiddenTime time;
        if (this.mFobbidenApps.contains(pkg)) {
            return true;
        }
        if (!this.mFunApps.contains(pkg)) {
            return false;
        }
        Calendar now = Calendar.getInstance();
        int weekDay = now.get(7);
        int minuteOfDay = (now.get(11) * 60) + now.get(12);
        if (weekDay == 1 || weekDay == 7) {
            time = this.mWeekendConfig;
        } else {
            time = this.mWeekDayConfig;
        }
        if (time == null || !time.isTimeMatch(minuteOfDay)) {
            return false;
        }
        return true;
    }

    public void updateAppUsageStatus(AppInfo appInfo) {
        String log;
        ForbiddenTime time;
        Calendar now = Calendar.getInstance();
        int weekDay = now.get(7);
        int minuteOfDay = (now.get(11) * 60) + now.get(12);
        String pkg = appInfo.getPakageName();
        if (this.mFobbidenApps.contains(pkg)) {
            appInfo.setAppStatus(1);
            log = "被单独限制: FORBBIDEN";
        } else if (this.mFunApps.contains(pkg)) {
            if (weekDay == 1 || weekDay == 7) {
                time = this.mWeekendConfig;
            } else {
                time = this.mWeekDayConfig;
            }
            if (time == null || !time.isTimeMatch(minuteOfDay)) {
                appInfo.setAppStatus(0);
                log = "正常使用";
            } else {
                log = "被时间限制: FORBBIDEN";
                appInfo.setAppStatus(2);
            }
        } else {
            appInfo.setAppStatus(0);
            log = "正常使用";
        }
        Logger.d(TAG, "pkg = " + appInfo.appName + ": -> " + log);
    }

    public void updateAppUsageStatus(List<AppInfo> appInfos) {
        ForbiddenTime time;
        if (appInfos != null) {
            Calendar now = Calendar.getInstance();
            int weekDay = now.get(7);
            int minuteOfDay = (now.get(11) * 60) + now.get(12);
            for (AppInfo appInfo : appInfos) {
                String pkg = appInfo.getPakageName();
                if (this.mFobbidenApps.contains(pkg)) {
                    appInfo.setAppStatus(1);
                } else if (this.mFunApps.contains(pkg)) {
                    if (weekDay == 1 || weekDay == 7) {
                        time = this.mWeekendConfig;
                    } else {
                        time = this.mWeekDayConfig;
                    }
                    if (time == null || !time.isTimeMatch(minuteOfDay)) {
                        appInfo.setAppStatus(0);
                    } else {
                        appInfo.setAppStatus(2);
                    }
                } else {
                    appInfo.setAppStatus(0);
                }
            }
        }
    }

    public void onForbbidenTimeConfig(List<String> apps, ForbiddenTime weekDay, ForbiddenTime weekEnd) {
        this.mFunApps.clear();
        if (apps != null) {
            this.mFunApps.addAll(apps);
        }
        this.mWeekendConfig = weekEnd;
        this.mWeekDayConfig = weekDay;
        this.mExecutor.execute(new UpdateForbbidenTimeTask(new ArrayList<>(this.mFunApps), this.mWeekDayConfig, this.mWeekendConfig));
        if (this.mListener != null) {
            this.mListener.onRemoteConfigChanged(2);
        }
        checkTimeAndKillProcess();
    }

    /* access modifiers changed from: private */
    public int getMinuteOfDay(Calendar time) {
        return (time.get(11) * 60) + time.get(12);
    }

    private void checkTimeAndKillProcess() {
        Calendar now = Calendar.getInstance();
        int dayOfWeek = now.get(7);
        int minuteOfDay = getMinuteOfDay(now);
        if (dayOfWeek == 7 || dayOfWeek == 1) {
            if (this.mWeekendConfig != null && this.mWeekendConfig.isTimeMatch(minuteOfDay)) {
                this.mExecutor.execute(new KillProcessTask(this.mContext, new ArrayList(this.mFunApps), false));
            }
        } else if (this.mWeekDayConfig != null && this.mWeekDayConfig.isTimeMatch(minuteOfDay)) {
            this.mExecutor.execute(new KillProcessTask(this.mContext, new ArrayList(this.mFunApps), false));
        }
    }

    public void handleForbbidenApp(ForbiddenAPP app) {
        boolean changed;
        if (app == null) {
            Logger.w(TAG, "handleForbbidenApp | app is null -> return");
        } else if (!this.mIsInited) {
            Logger.w(TAG, "handleForbbidenApp | 初始化尚未完成 -> return");
        } else {
            if (app.getStatus() == 0) {
                changed = this.mFobbidenApps.remove(app.getAppCode());
            } else {
                changed = this.mFobbidenApps.add(app.getAppCode());
            }
            if (changed) {
                List<String> list = new ArrayList<>(this.mFobbidenApps);
                this.mExecutor.execute(new UpdateForbbidenListTask(list));
                if (app.getStatus() == 1) {
                    this.mExecutor.execute(new KillProcessTask(this.mContext, list, false));
                }
                if (this.mListener != null) {
                    this.mListener.updateAppUsageState(app);
                }
            }
        }
    }

    public void handleForbbidenTime(ForbiddenTime forbiddenTime) {
        String str;
        if (forbiddenTime == null) {
            Logger.w(TAG, "handleForbbidenTime | forbiddenTime is null -> return");
        } else if (!this.mIsInited) {
            Logger.w(TAG, "handleForbbidenTime | 初始化尚未完成 -> return");
        } else {
            try {
                int type = forbiddenTime.getType();
                if (type == 0) {
                    if (!ForbiddenTime.equals(this.mWeekDayConfig, forbiddenTime)) {
                        this.mWeekDayConfig = forbiddenTime;
                    } else {
                        return;
                    }
                } else if (type != 1) {
                    Logger.d(TAG, "正在请求娱乐app列表");
                    this.mTimeHelper.query();
                    return;
                } else if (ForbiddenTime.equals(this.mWeekendConfig, forbiddenTime)) {
                    Logger.d(TAG, "正在请求娱乐app列表");
                    this.mTimeHelper.query();
                    return;
                } else {
                    this.mWeekendConfig = forbiddenTime;
                }
                this.mExecutor.execute(new UpdateForbbidenTimeTask(new ArrayList<>(this.mFunApps), this.mWeekDayConfig, this.mWeekendConfig));
                if (this.mListener != null) {
                    this.mListener.onRemoteConfigChanged(2);
                }
                checkTimeAndKillProcess();
                Logger.d(TAG, "正在请求娱乐app列表");
                this.mTimeHelper.query();
            } finally {
                str = "正在请求娱乐app列表";
                Logger.d(TAG, str);
                this.mTimeHelper.query();
            }
        }
    }

    private class PushEventHandler implements ReceiverHandler {
        private PushEventHandler() {
        }

        public void handleEvent(Intent intent, String action) {
            if (TextUtils.equals(action, "action.app.forbbiden.CHANGED")) {
                RemoteController.this.handleForbbidenApp((ForbiddenAPP) intent.getSerializableExtra("key.forbbiden.app"));
            } else if (TextUtils.equals(action, "action.app.forbbiden.time.CHANGED")) {
                RemoteController.this.handleForbbidenTime((ForbiddenTime) intent.getSerializableExtra("key.forbbiden.time"));
            }
        }
    }

    private class UpdateForbbidenListTask implements Runnable {
        private final List<String> mForbbidenList;
        private final Set<String> mFunApps;

        public UpdateForbbidenListTask(List<String> list) {
            this.mForbbidenList = list;
            this.mFunApps = new HashSet(RemoteController.this.mFunApps);
        }

        @Override
        public void run() {
            String json;
            if (ListUtils.isEmpty((List) this.mForbbidenList)) {
                json = "";
            } else {
                ForbbidenAppConfig config = new ForbbidenAppConfig();
                config.mForbbidenApps = this.mForbbidenList;
                json = GsonUtils.toJson(config);
            }
            RemoteController.this.mSharedPref.setKV(RemoteController.FORBBIDEN_CONFIG_KEY, json);
            RemoteController.this.mExecutor.execute(new UpdateForbiddenContentProviderTask(new HashSet<>(this.mForbbidenList), this.mFunApps));
        }
    }

    private class UpdateForbbidenTimeTask implements Runnable {
        private final Set<String> mForbiddenApps;
        private final List<String> mFunApps;
        private final ForbiddenTime mWeekDay;
        private final ForbiddenTime mWeekEnd;

        public UpdateForbbidenTimeTask(List<String> funApps, ForbiddenTime weekDay, ForbiddenTime weekEnd) {
            this.mFunApps = funApps;
            this.mForbiddenApps = new HashSet(RemoteController.this.mFobbidenApps);
            this.mWeekDay = weekDay;
            this.mWeekEnd = weekEnd;
        }

        @Override
        public void run() {
            ForbbidenTimeConfig config = new ForbbidenTimeConfig();
            config.mFunApps = this.mFunApps;
            config.mWeekDayConfig = this.mWeekDay;
            config.mWeekendConfig = this.mWeekEnd;
            RemoteController.this.mSharedPref.setKV(RemoteController.FORBBIDEN_TIME_CONFIG_KEY, GsonUtils.toJson(config));
            RemoteController.this.mExecutor.execute(new UpdateForbiddenContentProviderTask(this.mForbiddenApps, new HashSet<>(this.mFunApps)));
        }
    }

    private class TimeTickHandler implements ReceiverHandler {
        private TimeTickHandler() {
        }

        public void handleEvent(Intent intent, String action) {
            Calendar now = Calendar.getInstance();
            int dayOfWeek = now.get(7);
            int minuteOfDay = RemoteController.this.getMinuteOfDay(now);
            if (dayOfWeek == 7 || dayOfWeek == 1) {
                if (RemoteController.this.mWeekendConfig != null) {
                    if (minuteOfDay == RemoteController.this.mWeekendConfig.getStartTime()) {
                        RemoteController.this.mListener.onRemoteConfigChanged(2);
                    } else if (minuteOfDay == RemoteController.this.mWeekendConfig.getEndTime()) {
                        RemoteController.this.mListener.onRemoteConfigChanged(2);
                        RemoteController.this.mExecutor.execute(new KillProcessTask(RemoteController.this.mContext, new ArrayList(RemoteController.this.mFunApps), false));
                    }
                }
            } else if (RemoteController.this.mWeekDayConfig != null) {
                if (minuteOfDay == RemoteController.this.mWeekDayConfig.getStartTime()) {
                    RemoteController.this.mListener.onRemoteConfigChanged(2);
                } else if (minuteOfDay == RemoteController.this.mWeekDayConfig.getEndTime()) {
                    RemoteController.this.mListener.onRemoteConfigChanged(2);
                    RemoteController.this.mExecutor.execute(new KillProcessTask(RemoteController.this.mContext, new ArrayList(RemoteController.this.mFunApps), false));
                }
            }
            RemoteController.this.mExecutor.execute(new UpdateForbiddenContentProviderTask(new HashSet(RemoteController.this.mFobbidenApps), new HashSet(RemoteController.this.mFunApps)));
        }
    }

    private class UpdateForbiddenContentProviderTask implements Runnable {
        private final Set<String> mForbbidenApps;
        private final Set<String> mFunApps;

        public UpdateForbiddenContentProviderTask(Set<String> forbbidenApps, Set<String> funApps) {
            if (forbbidenApps == null || forbbidenApps.isEmpty()) {
                this.mForbbidenApps = null;
            } else {
                this.mForbbidenApps = new HashSet(forbbidenApps);
            }
            if (funApps == null || funApps.isEmpty()) {
                this.mFunApps = new HashSet();
            } else {
                this.mFunApps = new HashSet(funApps);
            }
        }

        @Override
        public void run() {
            try {
                Calendar now = Calendar.getInstance();
                int dayOfWeek = now.get(7);
                Set<String> funApps = new HashSet<>();
                int minuteOfDay = RemoteController.this.getMinuteOfDay(now);
                if (dayOfWeek == 7 || dayOfWeek == 1) {
                    if (RemoteController.this.mWeekendConfig != null && RemoteController.this.mWeekendConfig.isTimeMatch(minuteOfDay)) {
                        funApps.addAll(this.mFunApps);
                    }
                } else if (RemoteController.this.mWeekDayConfig != null && RemoteController.this.mWeekDayConfig.isTimeMatch(minuteOfDay)) {
                    funApps.addAll(this.mFunApps);
                }
                AppLockHelper.replace(RemoteController.this.mContext, this.mForbbidenApps, funApps);
            } catch (Throwable exp) {
                Logger.e(RemoteController.TAG, "replace error", exp);
            }
        }
    }
}
