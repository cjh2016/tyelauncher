package com.boll.tyelauncher.model.launcher;


import android.content.Context;
import android.content.pm.LauncherApps;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.model.AppContants;
import com.boll.tyelauncher.model.ForbiddenAPP;
import com.boll.tyelauncher.model.launcher.interfaces.AppFilter;
import com.boll.tyelauncher.model.launcher.interfaces.IAppInstallReporter;
import com.boll.tyelauncher.model.launcher.interfaces.IAppProvider;
import com.boll.tyelauncher.model.launcher.interfaces.ILauncherView;
import com.boll.tyelauncher.model.launcher.interfaces.IRemoteControllerListener;
import com.boll.tyelauncher.model.launcher.interfaces.ISystemPMHooker;
import com.boll.tyelauncher.model.launcher.interfaces.ISystemPackageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class LauncherModel implements IRemoteControllerListener, IAppProvider {
    private static final String GRADE_PERIOD_KEY = "app_grade_period";
    private static final String HIDE_APP_FOR_LAUNCHER_VERSIONS_KEY = "app_hide_icon_for_launcher_version";
    private static final String HIDE_APP_KEY = "app_hide_icon";
    private static final String KEY_STUDY_PERIOD = "app_study_period";
    public static final int KEY_STUDY_PERIOD_ALL = 0;
    public static final int KEY_STUDY_PERIOD_MIDDLE = 2;
    public static final int KEY_STUDY_PERIOD_PRIMARY = 1;
    private static final String LAUNCER_VERSION_CODE_STR = String.valueOf(BuildConfig.VERSION_CODE);
    private static final String TAG = "LauncherModel";
    public static final AppFilter sAppFilter = new LauncherApkFilter();
    private static final LauncherModel sModel = new LauncherModel();
    /* access modifiers changed from: private */
    public final List<AppInfo> mAllAppList = new ArrayList();
    /* access modifiers changed from: private */
    public IAppInstallReporter mAppInstalReporter;
    /* access modifiers changed from: private */
    public final Map<String, AppInfo> mAppListIndex = new HashMap();
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public String mGradePeriod;
    /* access modifiers changed from: private */
    public boolean mHasStarted = false;
    private LauncherApps mLauncherApps;
    /* access modifiers changed from: private */
    public ILauncherView mLauncherView;
    /* access modifiers changed from: private */
    public ISystemPackageManager mPM;
    private PackageCallback mPackageCallback;
    /* access modifiers changed from: private */
    public List<IPrepareWork> mPrepareWorks = new ArrayList();
    /* access modifiers changed from: private */
    public RemoteController mRemoteController;
    /* access modifiers changed from: private */
    public final List<AppInfo> mShowAppList = new ArrayList();
    /* access modifiers changed from: private */
    public final MainThreadExecutor mUIExecutor = LauncherExecutors.sMainExecutor;
    /* access modifiers changed from: private */
    public final WorkThreadExecutor mWorkExecutor = LauncherExecutors.sAppLoaderExecutor;

    public interface IPrepareWork {
        void prepareOnWorkThread();
    }

    public static LauncherModel getInstance() {
        return sModel;
    }

    private LauncherModel() {
    }

    public void addPrepareWork(IPrepareWork work) {
        if (work != null && !this.mPrepareWorks.contains(work)) {
            this.mPrepareWorks.add(work);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public final void init(Context context, ISystemPMHooker hooker) {
        if (this.mContext == null) {
            this.mContext = context.getApplicationContext();
            this.mPM = new LauncherSystemPM(this.mContext, hooker);
            this.mPackageCallback = new PackageCallback();
            this.mLauncherApps = (LauncherApps) this.mContext.getSystemService("launcherapps");
            this.mLauncherApps.registerCallback(this.mPackageCallback);
        }
    }

    public void setAppInstalReporter(IAppInstallReporter appInstalReporter) {
        this.mAppInstalReporter = appInstalReporter;
    }

    public void setRemoteController(RemoteController remoteController) {
        this.mRemoteController = remoteController;
        this.mRemoteController.setListener(this);
    }

    public void addFakeAppInfo(AppInfo appInfo) {
        this.mWorkExecutor.execute(new QueueTask(this.mUIExecutor, new PackageAddedOKTask(this.mGradePeriod, appInfo, true)));
    }

    public void removeFakeAppInfo(AppInfo appInfo) {
        PackageRemovedOKTask task = new PackageRemovedOKTask(this.mGradePeriod, appInfo.getPakageName(), (UserHandle) null);
        if (Looper.getMainLooper() == Looper.myLooper()) {
            task.run();
        } else {
            this.mUIExecutor.execute(task);
        }
    }

    public void removeFackAppInfo(String pkgName) {
        PackageRemovedOKTask task = new PackageRemovedOKTask(this.mGradePeriod, pkgName, (UserHandle) null);
        if (Looper.getMainLooper() == Looper.myLooper()) {
            task.run();
        } else {
            this.mUIExecutor.execute(task);
        }
    }

    public final void unInit() {
        if (!(this.mLauncherApps == null || this.mPackageCallback == null)) {
            this.mLauncherApps.unregisterCallback(this.mPackageCallback);
        }
        if (this.mRemoteController != null) {
            this.mRemoteController.unInit();
        }
        this.mPackageCallback = null;
        this.mLauncherApps = null;
        this.mContext = null;
        this.mPM = null;
    }

    public void setLauncherView(ILauncherView launcherView) {
        this.mLauncherView = launcherView;
    }

    public static final boolean isPrimaryGradePeriod(String gradePeriod) {
        return TextUtils.equals(gradePeriod, "03");
    }

    public final void start(String gradePeriod) {
        this.mGradePeriod = gradePeriod;
        this.mWorkExecutor.execute(new GetAllAppsTask(this.mGradePeriod));
        if (this.mLauncherView != null) {
            this.mLauncherView.onCreateCardPages(this.mGradePeriod);
        }
    }

    public final void convertGradePeriod(String gradePeriod) {
        if (isPrimaryGradePeriod(this.mGradePeriod) != isPrimaryGradePeriod(gradePeriod)) {
            doConvertGradePeriod(gradePeriod);
        }
    }

    public final void login(User user) {
        String gradePeriod;
        try {
            gradePeriod = GradeHelper.getGradeSegment(user.getGradecode());
        } catch (Throwable th) {
            gradePeriod = GradeHelper.GRADE_SEGMENT_NOT_LOGIN;
        }
        convertGradePeriod(gradePeriod);
    }

    public final void logout() {
        convertGradePeriod(GradeHelper.GRADE_SEGMENT_NOT_LOGIN);
    }

    public final void convertGradeCode(String newGradeCode) {
        String gradePeriod;
        try {
            gradePeriod = GradeHelper.getGradeSegment(newGradeCode);
        } catch (Throwable exp) {
            Logger.e(TAG, "getGradeSegment error: newGradeCode=" + newGradeCode, exp);
            gradePeriod = GradeHelper.GRADE_SEGMENT_NOT_LOGIN;
        }
        convertGradePeriod(gradePeriod);
    }

    private final void doConvertGradePeriod(final String gradePeriod) {
        this.mWorkExecutor.execute(new QueueTask(this.mUIExecutor, new Runnable() {
            public void run() {
                String unused = LauncherModel.this.mGradePeriod = gradePeriod;
                List<String> processList = new ArrayList<>(LauncherModel.this.mAllAppList.size());
                for (AppInfo appInfo : LauncherModel.this.mShowAppList) {
                    processList.add(appInfo.getPakageName());
                }
                processList.addAll(LauncherModel.sAppFilter.getBuildInHideApps());
                LauncherModel.this.mWorkExecutor.execute(new KillProcessTask(LauncherModel.this.mContext, processList, true));
                List<AppInfo> showAppInfos = LauncherModel.this.filterAndSort(LauncherModel.this.mGradePeriod, LauncherModel.this.mAllAppList);
                LauncherModel.this.mShowAppList.clear();
                LauncherModel.this.mShowAppList.addAll(showAppInfos);
                if (LauncherModel.this.mLauncherView != null) {
                    LauncherModel.this.mLauncherView.onCreateCardPages(LauncherModel.this.mGradePeriod);
                    LauncherModel.this.mLauncherView.initAppPages(LauncherModel.this.mGradePeriod, LauncherModel.this.mShowAppList);
                }
            }
        }));
    }

    @Override
    public void onRemoteConfigChanged(int configType) {
        this.mRemoteController.updateAppUsageStatus(this.mShowAppList);
        if (this.mLauncherView != null) {
            this.mLauncherView.updateAppUsageStatus(this.mShowAppList);
        }
    }

    public void onWifiConnected() {
        if (this.mHasStarted) {
            if (this.mRemoteController != null) {
                this.mRemoteController.prepareRemote(true);
            }
            if (this.mAppInstalReporter != null) {
                this.mAppInstalReporter.checkUploadAllApps();
            }
        }
    }

    @Override
    public void updateAppUsageState(ForbiddenAPP app) {
        AppInfo appInfo = this.mAppListIndex.get(app.getAppCode());
        if (appInfo != null) {
            int oldStatus = appInfo.getAppStatus();
            this.mRemoteController.updateAppUsageStatus(appInfo);
            if (oldStatus != appInfo.getAppStatus() && this.mLauncherView != null) {
                this.mLauncherView.updateAppUsageStatus(appInfo);
            }
        }
    }

    public void updateAllAppUsageState() {
        this.mWorkExecutor.execute(new QueueTask(this.mUIExecutor, new UpdateUsageStateTask()));
    }

    public List<AppInfo> getAllApps() {
        return this.mShowAppList;
    }

    public AppInfo getAppInfo(String pkg, boolean all) {
        List<AppInfo> appInfoList;
        if (all) {
            appInfoList = this.mAllAppList;
        } else {
            appInfoList = this.mShowAppList;
        }
        for (AppInfo appInfo : appInfoList) {
            if (TextUtils.equals(pkg, appInfo.getPakageName())) {
                return appInfo;
            }
        }
        return null;
    }

    public String getGradePeriod() {
        return this.mGradePeriod;
    }

    /* access modifiers changed from: private */
    public List<AppInfo> filterAndSort(String gradePeriod, List<AppInfo> appInfos) {
        List<AppInfo> result = new ArrayList<>(appInfos.size());
        for (AppInfo appInfo : appInfos) {
            if (!shouldHide(gradePeriod, appInfo) && sAppFilter.filter(gradePeriod, appInfo)) {
                result.add(appInfo);
            }
        }
        return result;
    }

    public static boolean shouldHide(String gradePeriod, AppInfo appInfo) {
        String[] vers;
        String[] periods;
        if (EtsConstant.ETS_PHONETIC_VIRTUAL_PACKAGE.equals(appInfo.pakageName) && TextUtils.equals(gradePeriod, "03")) {
            return true;
        }
        Bundle bundle = appInfo.getMetaInfo();
        String appGradePeriod = bundle.getString(GRADE_PERIOD_KEY, (String) null);
        if (!TextUtils.isEmpty(appGradePeriod) && (periods = appGradePeriod.split(",")) != null) {
            boolean find = false;
            int length = periods.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (TextUtils.equals(periods[i], gradePeriod)) {
                    find = true;
                    break;
                } else {
                    i++;
                }
            }
            if (!find) {
                return false;
            }
        }
        if (bundle.getBoolean(HIDE_APP_KEY, false)) {
            return true;
        }
        String hideVersions = bundle.getString(HIDE_APP_FOR_LAUNCHER_VERSIONS_KEY, (String) null);
        if (TextUtils.isEmpty(hideVersions) || (vers = hideVersions.split(",")) == null || vers.length == 0) {
            return false;
        }
        for (String ver : vers) {
            if (ver != null && TextUtils.equals(ver, LAUNCER_VERSION_CODE_STR)) {
                return true;
            }
        }
        return false;
    }

    public static final int getStudyPeriod(AppInfo appInfo) {
        return appInfo.getMetaInfo().getInt(KEY_STUDY_PERIOD, 0);
    }

    public class QueueTask implements Runnable {
        private final Runnable mRealTask;
        private final ExecutorService mService;

        public QueueTask(ExecutorService service, Runnable realTask) {
            this.mRealTask = realTask;
            this.mService = service;
        }

        @Override
        public void run() {
            this.mService.execute(this.mRealTask);
        }
    }

    private class GetAllAppsTask implements Runnable {
        private final String mLocalGradePeriod;

        public GetAllAppsTask(String gradePeriod) {
            this.mLocalGradePeriod = gradePeriod;
        }

        @Override
        public void run() {
            List<AppInfo> appInfos = LauncherModel.this.mPM.queryAll(this.mLocalGradePeriod);
            List<AppInfo> showAppInfos = LauncherModel.this.filterAndSort(this.mLocalGradePeriod, appInfos);
            Map<String, AppInfo> indexMap = createIndex(appInfos);
            for (IPrepareWork work : LauncherModel.this.mPrepareWorks) {
                work.prepareOnWorkThread();
            }
            LauncherModel.this.mUIExecutor.execute(new GetAllAppsResultTask(this.mLocalGradePeriod, appInfos, showAppInfos, indexMap));
            LauncherModel.this.mUIExecutor.execute(new InitTaskAfterAppInited(this.mLocalGradePeriod));
        }

        private Map<String, AppInfo> createIndex(List<AppInfo> appInfos) {
            Map<String, AppInfo> index = new HashMap<>(appInfos.size());
            for (AppInfo appInfo : appInfos) {
                index.put(appInfo.getPakageName(), appInfo);
            }
            return index;
        }
    }

    private class GetAllAppsResultTask implements Runnable {
        private final List<AppInfo> allAppInfos;
        private final Map<String, AppInfo> indexMap;
        private final String mLocalGradePeriod;
        private final List<AppInfo> showAppInfos;

        public GetAllAppsResultTask(String gradePeriod, List<AppInfo> allAppInfos2, List<AppInfo> showAppInfos2, Map<String, AppInfo> indexMap2) {
            this.mLocalGradePeriod = gradePeriod;
            this.allAppInfos = allAppInfos2;
            this.showAppInfos = showAppInfos2;
            this.indexMap = indexMap2;
        }

        @Override
        public void run() {
            boolean unused = LauncherModel.this.mHasStarted = true;
            if (!LauncherModel.this.mAllAppList.isEmpty()) {
                Logger.e(LauncherModel.TAG, "mAllAppList非空");
            }
            if (!LauncherModel.this.mShowAppList.isEmpty()) {
                Logger.e(LauncherModel.TAG, "mShowAppList非空");
            }
            if (!LauncherModel.this.mAppListIndex.isEmpty()) {
                Logger.e(LauncherModel.TAG, "mAppListIndex非空");
            }
            LauncherModel.this.mAllAppList.clear();
            LauncherModel.this.mAllAppList.addAll(this.allAppInfos);
            LauncherModel.this.mShowAppList.clear();
            LauncherModel.this.mShowAppList.addAll(this.showAppInfos);
            LauncherModel.this.mAppListIndex.clear();
            LauncherModel.this.mAppListIndex.putAll(this.indexMap);
            if (LauncherModel.this.mRemoteController != null) {
                LauncherModel.this.mRemoteController.updateAppUsageStatus((List<AppInfo>) LauncherModel.this.mShowAppList);
            }
            if (LauncherModel.this.mLauncherView != null) {
                LauncherModel.this.mLauncherView.initAppPages(this.mLocalGradePeriod, LauncherModel.this.mShowAppList);
            }
            if (LauncherModel.this.mAppInstalReporter != null) {
                LauncherModel.this.mAppInstalReporter.checkUploadAllApps();
            }
        }
    }

    private class InitTaskAfterAppInited implements Runnable {
        private final String mLocalGradePeriod;

        public InitTaskAfterAppInited(String gradePeriod) {
            this.mLocalGradePeriod = gradePeriod;
        }

        public void run() {
            if (LauncherModel.this.mRemoteController != null) {
                LauncherModel.this.mRemoteController.prepareRemote(false);
            }
        }
    }

    private class PackageRemovedTask implements Runnable {
        private final AppInfo mAppInfo;
        private final String mLocalGradePeriod;
        private final String mPackageName;
        private final UserHandle mUserHandle;

        public PackageRemovedTask(String gradePeriod, String packageName, AppInfo appInfo, UserHandle user) {
            this.mLocalGradePeriod = gradePeriod;
            this.mPackageName = packageName;
            this.mAppInfo = appInfo;
            this.mUserHandle = user;
        }

        public void run() {
            LauncherModel.this.mPM.updateCache(this.mPackageName);
            LauncherModel.this.mUIExecutor.execute(new PackageRemovedOKTask(this.mLocalGradePeriod, this.mPackageName, this.mUserHandle));
        }
    }

    private class PackageRemovedOKTask implements Runnable {
        private final String mLocalGradePeriod;
        private final String mPackageName;
        private final UserHandle mUserHandle;

        public PackageRemovedOKTask(String gradePeriod, String packageName, UserHandle user) {
            this.mLocalGradePeriod = gradePeriod;
            this.mPackageName = packageName;
            this.mUserHandle = user;
        }

        @Override
        public void run() {
            AppInfo appInfo = (AppInfo) LauncherModel.this.mAppListIndex.remove(this.mPackageName);
            if (appInfo != null) {
                LauncherModel.this.mAllAppList.remove(appInfo);
                if (LauncherModel.this.mShowAppList.remove(appInfo) && LauncherModel.this.mLauncherView != null) {
                    LauncherModel.this.mLauncherView.removeApp(this.mLocalGradePeriod, appInfo);
                }
                if (LauncherModel.this.mAppInstalReporter != null) {
                    LauncherModel.this.mAppInstalReporter.reportAppRemoved(this.mPackageName);
                }
            }
        }
    }

    private class PackageAddedTask implements Runnable {
        private final String mLocalGradePeriod;
        private final String mPackageName;
        private final UserHandle mUserHandle;

        public PackageAddedTask(String gradePeriod, String packageName, UserHandle user) {
            this.mLocalGradePeriod = gradePeriod;
            this.mPackageName = packageName;
            this.mUserHandle = user;
        }

        @Override
        public void run() {
            AppInfo appInfo = LauncherModel.this.mPM.query(this.mLocalGradePeriod, this.mPackageName);
            if (appInfo == null) {
                Logger.e(LauncherModel.TAG, "mPM.query失败： " + this.mPackageName);
                return;
            }
            LauncherModel.this.mUIExecutor.execute(new PackageAddedOKTask(this.mLocalGradePeriod, appInfo, LauncherModel.sAppFilter.filter(this.mLocalGradePeriod, appInfo)));
        }
    }

    private class PackageAddedOKTask implements Runnable {
        private final AppInfo mAppInfo;
        private final String mLocalGradePeriod;
        private final boolean mShouldShow;

        public PackageAddedOKTask(String gradePeriod, AppInfo appInfo, boolean shouldShow) {
            this.mLocalGradePeriod = gradePeriod;
            this.mAppInfo = appInfo;
            this.mShouldShow = shouldShow;
        }

        @Override
        public void run() {
            if (LauncherModel.this.mRemoteController != null) {
                LauncherModel.this.mRemoteController.updateAppUsageStatus(this.mAppInfo);
            }
            LauncherModel.this.mAppListIndex.put(this.mAppInfo.getPakageName(), this.mAppInfo);
            LauncherModel.this.mAllAppList.add(this.mAppInfo);
            if (this.mShouldShow) {
                LauncherModel.this.mShowAppList.add(this.mAppInfo);
                if (LauncherModel.this.mLauncherView != null) {
                    Logger.d(LauncherModel.TAG, "call appendApp: " + this.mAppInfo.getAppName());
                    LauncherModel.this.mLauncherView.appendApp(this.mLocalGradePeriod, this.mAppInfo);
                }
                if (LauncherModel.this.mAppInstalReporter != null && this.mAppInfo != null) {
                    LauncherModel.this.mAppInstalReporter.reportAppAdded(this.mAppInfo);
                }
            }
        }
    }

    private class PackageChangedTask implements Runnable {
        private final String mLocalGradePeriod;
        private final String mPackageName;
        private final AppInfo mRawAppInfo;
        private final UserHandle mUserHandle;

        public PackageChangedTask(String gradePeriod, String packageName, UserHandle user, AppInfo rawAppInfo) {
            this.mLocalGradePeriod = gradePeriod;
            this.mPackageName = packageName;
            this.mUserHandle = user;
            this.mRawAppInfo = rawAppInfo;
        }

        @Override
        public void run() {
            LauncherModel.this.mPM.updateCache(this.mPackageName);
            AppInfo appInfo = LauncherModel.this.mPM.query(this.mLocalGradePeriod, this.mPackageName);
            if (appInfo == null) {
                Logger.e(LauncherModel.TAG, "mPM.query失败： " + this.mPackageName);
                return;
            }
            LauncherModel.this.mUIExecutor.execute(new PackageChangedOKTask(this.mLocalGradePeriod, appInfo, this.mRawAppInfo, LauncherModel.sAppFilter.filter(this.mLocalGradePeriod, appInfo)));
        }
    }

    private class PackageChangedOKTask implements Runnable {
        private final AppInfo mAppInfo;
        private final String mLocalGradePeriod;
        private final AppInfo mRawAppInfo;
        private final boolean mShouldShow;

        public PackageChangedOKTask(String gradePeriod, AppInfo appInfo, AppInfo rawAppInfo, boolean shouldShow) {
            this.mLocalGradePeriod = gradePeriod;
            this.mAppInfo = appInfo;
            this.mRawAppInfo = rawAppInfo;
            this.mShouldShow = shouldShow;
        }

        @Override
        public void run() {
            boolean changed = this.mRawAppInfo.copyMetaInfo(this.mAppInfo);
            if (changed && this.mShouldShow && LauncherModel.this.mLauncherView != null) {
                LauncherModel.this.mLauncherView.updateAppTitleAndIcon(this.mLocalGradePeriod, this.mRawAppInfo);
            } else if (!changed) {
                Logger.w(LauncherModel.TAG, "信息没有更新：" + this.mAppInfo.getPakageName());
            }
        }
    }

    private class PackageCallback extends LauncherApps.Callback {
        private PackageCallback() {
        }

        @Override
        public void onPackageRemoved(String packageName, UserHandle user) {
            LauncherModel.this.mWorkExecutor.execute(new PackageRemovedTask(LauncherModel.this.mGradePeriod, packageName, (AppInfo) LauncherModel.this.mAppListIndex.get(packageName), user));
        }

        @Override
        public void onPackageAdded(String packageName, UserHandle user) {
            if (((AppInfo) LauncherModel.this.mAppListIndex.get(packageName)) != null) {
                Logger.e(LauncherModel.TAG, "该应用已经在列表中了: " + packageName);
            } else {
                LauncherModel.this.mWorkExecutor.execute(new PackageAddedTask(LauncherModel.this.mGradePeriod, packageName, user));
            }
        }

        @Override
        public void onPackageChanged(String packageName, UserHandle user) {
            if (!AppContants.RECITE_BOOK.equals(packageName)) {
                AppInfo appInfo = (AppInfo) LauncherModel.this.mAppListIndex.get(packageName);
                if (appInfo == null) {
                    Logger.w(LauncherModel.TAG, "onPackageChanged: 原来找不到信息: " + packageName);
                    LauncherModel.this.mWorkExecutor.execute(new PackageAddedTask(LauncherModel.this.mGradePeriod, packageName, user));
                    return;
                }
                LauncherModel.this.mWorkExecutor.execute(new PackageChangedTask(LauncherModel.this.mGradePeriod, packageName, user, appInfo));
            }
        }

        @Override
        public void onPackagesAvailable(String[] packageNames, UserHandle user, boolean replacing) {
        }

        @Override
        public void onPackagesUnavailable(String[] packageNames, UserHandle user, boolean replacing) {
        }
    }

    private class ConvertStudyPeriodTask implements Runnable {
        private final String mTargetGradeCode;

        public ConvertStudyPeriodTask(String targetGradeCode) {
            this.mTargetGradeCode = targetGradeCode;
        }

        @Override
        public void run() {
            LauncherModel.this.mUIExecutor.execute(new ConvertStudyPeriodOKTask(this.mTargetGradeCode));
        }
    }

    private class ConvertStudyPeriodOKTask implements Runnable {
        private final String mTargetGradeCode;

        public ConvertStudyPeriodOKTask(String targetGradeCode) {
            this.mTargetGradeCode = targetGradeCode;
        }

        @Override
        public void run() {
        }
    }

    private class UpdateUsageStateTask implements Runnable {
        private UpdateUsageStateTask() {
        }

        @Override
        public void run() {
            LauncherModel.this.mRemoteController.updateAppUsageStatus((List<AppInfo>) LauncherModel.this.mShowAppList);
            if (LauncherModel.this.mLauncherView != null) {
                LauncherModel.this.mLauncherView.updateAppUsageStatus((List<AppInfo>) LauncherModel.this.mShowAppList);
            }
        }
    }
}
