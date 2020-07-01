package com.boll.tyelauncher.checkappversion;

package com.toycloud.launcher.checkappversion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.model.AppUpdateBean;
import com.toycloud.launcher.model.AppVersionInfo;
import com.toycloud.launcher.util.GsonUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.UpdateUtil;
import framework.hz.salmon.retrofit.BaseSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyReceiver_CheckVersion extends BroadcastReceiver {
    /* access modifiers changed from: private */
    public static String TAG = MyReceiver_CheckVersion.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG, ":" + action);
        if (TextUtils.isEmpty(action)) {
            Log.e(TAG, "广播异常");
        } else if (action.equals("com.iflytek.action.CHECK_VERSIOL")) {
            Log.e(TAG, "检查时间到了" + new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis())));
            getVersionData(context);
        } else {
            if (action.equals("com.iflytek.action.CHECK_APPSTATE")) {
            }
        }
    }

    private void getVersionData(final Context context) {
        LauncherHttpHelper.getLauncherService().getLatestVersion().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<AppVersionInfo>(false, context) {
            public void onStart() {
                super.onStart();
            }

            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                super.onError(e);
                Log.e(MyReceiver_CheckVersion.TAG, e.toString());
                context.startService(new Intent(context, MyService_CheckVersion.class));
            }

            public void onNext(AppVersionInfo appVersionInfo) {
                super.onNext(appVersionInfo);
                if (appVersionInfo == null || appVersionInfo.getData() == null || appVersionInfo.getStatus() != 0) {
                    if (appVersionInfo != null) {
                        Log.e(MyReceiver_CheckVersion.TAG, "检查接口出现错误status:" + appVersionInfo.getStatus() + "message:" + appVersionInfo.getMsg());
                    } else {
                        Log.e(MyReceiver_CheckVersion.TAG, "检查接口出现错误");
                    }
                }
                Log.e(MyReceiver_CheckVersion.TAG, appVersionInfo.toString());
                String appVersionInfoStr = "";
                try {
                    appVersionInfoStr = GsonUtils.toJson(appVersionInfo);
                } catch (Throwable th) {
                    appVersionInfo = null;
                }
                Logger.d(MyReceiver_CheckVersion.TAG, "appVersionInfoStr: " + appVersionInfoStr);
                SharepreferenceUtil.getSharepferenceInstance(context).setLatestAppInfo(appVersionInfoStr);
                if (!(appVersionInfo == null || appVersionInfo.getData() == null || appVersionInfo.getData().size() == 0)) {
                    for (AppVersionInfo.DataBean da : appVersionInfo.getData()) {
                        String pkgName = da.getAppCode();
                        AppUpdateBean updateBean = UpdateUtil.getAppUpdateBean(context, pkgName);
                        if (updateBean == null) {
                            AppUpdateBean newBean = new AppUpdateBean();
                            newBean.setPkgName(pkgName);
                            newBean.setHasBeenShowed(0);
                            newBean.setVersionCode(da.getVersionCode());
                            UpdateUtil.setAppUpdateBean(context, newBean);
                        } else if (da.getVersionCode() != updateBean.getVersionCode()) {
                            AppUpdateBean newBean2 = new AppUpdateBean();
                            newBean2.setPkgName(pkgName);
                            newBean2.setHasBeenShowed(0);
                            newBean2.setVersionCode(da.getVersionCode());
                            UpdateUtil.setAppUpdateBean(context, newBean2);
                        }
                    }
                }
                context.startService(new Intent(context, MyService_CheckVersion.class));
            }
        });
    }
}
