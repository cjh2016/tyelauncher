package com.boll.tyelauncher.model.launcher.impl;

package com.toycloud.launcher.model.launcher.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.Preconditions;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.model.launcher.LauncherModel;
import com.boll.tyelauncher.model.launcher.interfaces.IAppInstallReporter;
import com.boll.tyelauncher.model.launcher.interfaces.IAppProvider;
import com.boll.tyelauncher.util.SharepreferenceUtil;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.util.PadInfoUtil;
import framework.hz.salmon.retrofit.BaseSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import okhttp3.ResponseBody;

public class AppInstalReporter implements IAppInstallReporter, LauncherModel.IPrepareWork {
    private static final String KEY_APP_CHANGE_VERSION = "key_app_change_version";
    private static final String KEY_APP_REPORT_VERSION = "key_app_report_version";
    private static final String KEY_VERSION_SWITCH = "key_report_version_change";
    private static final String TAG = "AppInstalReporter";
    private int mAppChangeVersion = -1;
    private final IAppProvider mAppProvider;
    /* access modifiers changed from: private */
    public int mAppReportVersion = -1;
    private final Context mContext;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public final SharepreferenceUtil mSharePref;

    @SuppressLint("RestrictedApi")
    public AppInstalReporter(Context context, IAppProvider appProvider) {
        Preconditions.checkNotNull(context, "context is null");
        Preconditions.checkNotNull(appProvider, "appProvider is null");
        this.mContext = context.getApplicationContext();
        this.mSharePref = SharepreferenceUtil.getSharepferenceInstance(this.mContext);
        this.mAppProvider = appProvider;
    }

    @Override
    public void reportAppRemoved(String pkgName) {
        this.mAppChangeVersion++;
        SharepreferenceUtil.putInt(KEY_APP_CHANGE_VERSION, this.mAppChangeVersion);
        if (Math.abs(this.mAppReportVersion - this.mAppChangeVersion) >= 2) {
            checkUploadAllApps();
            return;
        }
        final int changeVersion = this.mAppChangeVersion;
        LauncherHttpHelper.getLauncherService().unInstallApp(new PadInfoUtil(this.mContext).getSnCode(), pkgName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ResponseBody>(this.mContext, false) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                super.onError(e);
                Logger.e(AppInstalReporter.TAG, "reportAppRemoved error", e);
                AppInstalReporter.this.mHandler.removeCallbacksAndMessages((Object) null);
                AppInstalReporter.this.mHandler.postDelayed(new CheckTask(), 60000);
            }

            public void onNext(ResponseBody response) {
                super.onNext(response);
                int unused = AppInstalReporter.this.mAppReportVersion = changeVersion;
                SharepreferenceUtil.putInt(AppInstalReporter.KEY_APP_REPORT_VERSION, AppInstalReporter.this.mAppReportVersion);
                AppInstalReporter.this.checkUploadAllApps();
            }
        });
    }

    @Override
    public void reportAppAdded(AppInfo appInfo) {
        this.mAppChangeVersion++;
        SharepreferenceUtil.putInt(KEY_APP_CHANGE_VERSION, this.mAppChangeVersion);
        if (Math.abs(this.mAppReportVersion - this.mAppChangeVersion) >= 2) {
            checkUploadAllApps();
            return;
        }
        final int changeVersion = this.mAppChangeVersion;
        LauncherHttpHelper.getLauncherService().inStallAPP(new PadInfoUtil(this.mContext).getSnCode(), appInfo.getPakageName(), appInfo.appName, "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ResponseBody>(this.mContext, false) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                super.onError(e);
                Logger.e(AppInstalReporter.TAG, "reportAppAdded error", e);
                AppInstalReporter.this.mHandler.removeCallbacksAndMessages((Object) null);
                AppInstalReporter.this.mHandler.postDelayed(new CheckTask(), 60000);
            }

            public void onNext(ResponseBody response) {
                super.onNext(response);
                int unused = AppInstalReporter.this.mAppReportVersion = changeVersion;
                SharepreferenceUtil.putInt(AppInstalReporter.KEY_APP_REPORT_VERSION, AppInstalReporter.this.mAppReportVersion);
                AppInstalReporter.this.checkUploadAllApps();
            }
        });
    }

    @Override
    public void checkUploadAllApps() {
        if (this.mAppChangeVersion != this.mAppReportVersion) {
            final int changeVer = this.mAppChangeVersion;
            List<AppInfo> apps = this.mAppProvider.getAllApps();
            String snCode = new PadInfoUtil(this.mContext).getSnCode();
            UploadLocalAppsRequest request = new UploadLocalAppsRequest();
            request.sn = snCode;
            for (AppInfo appInfo : apps) {
                if (!appInfo.isSystemApp()) {
                    UploadAppInfo info = new UploadAppInfo();
                    info.sn = snCode;
                    info.appCode = appInfo.getPakageName();
                    info.appName = appInfo.appName;
                    request.appList.add(info);
                }
            }
            LauncherHttpHelper.getLauncherService().upLoadLocalApps(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<UploadLocalAppBean>(this.mContext, false) {
                public void onCompleted() {
                    super.onCompleted();
                }

                public void onError(Throwable e) {
                    super.onError(e);
                    Logger.e(AppInstalReporter.TAG, "checkUploadAllApps error", e);
                    AppInstalReporter.this.mHandler.removeCallbacksAndMessages((Object) null);
                    AppInstalReporter.this.mHandler.postDelayed(new CheckTask(), 60000);
                }

                public void onNext(UploadLocalAppBean response) {
                    super.onNext(response);
                    if (response.getStatus() != 0) {
                        AppInstalReporter.this.mHandler.removeCallbacksAndMessages((Object) null);
                        AppInstalReporter.this.mHandler.postDelayed(new CheckTask(), 60000);
                        return;
                    }
                    int unused = AppInstalReporter.this.mAppReportVersion = changeVer;
                    SharepreferenceUtil unused2 = AppInstalReporter.this.mSharePref;
                    SharepreferenceUtil.putInt(AppInstalReporter.KEY_APP_REPORT_VERSION, AppInstalReporter.this.mAppReportVersion);
                }
            });
        }
    }

    @Override
    public void prepareOnWorkThread() {
        if (this.mSharePref.getBoolean(KEY_VERSION_SWITCH, true)) {
            this.mSharePref.setKV(KEY_VERSION_SWITCH, false);
            this.mAppChangeVersion = 1;
            if (this.mSharePref.hasAppChanged()) {
                this.mAppReportVersion = 0;
            } else {
                this.mAppReportVersion = 1;
            }
            SharepreferenceUtil sharepreferenceUtil = this.mSharePref;
            SharepreferenceUtil.putInt(KEY_APP_CHANGE_VERSION, this.mAppChangeVersion);
            SharepreferenceUtil sharepreferenceUtil2 = this.mSharePref;
            SharepreferenceUtil.putInt(KEY_APP_REPORT_VERSION, this.mAppReportVersion);
            return;
        }
        this.mAppChangeVersion = SharepreferenceUtil.getInt(KEY_APP_CHANGE_VERSION, 1);
        this.mAppReportVersion = SharepreferenceUtil.getInt(KEY_APP_REPORT_VERSION, 0);
    }

    private class CheckTask implements Runnable {
        private CheckTask() {
        }

        @Override
        public void run() {
            AppInstalReporter.this.checkUploadAllApps();
        }
    }
}
