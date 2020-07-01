package com.boll.tyelauncher.util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.boll.tyelauncher.model.AppUpdateBean;
import com.boll.tyelauncher.model.AppVersionInfo;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UpdateUtil {
    /* access modifiers changed from: private */
    public static String TAG = UpdateUtil.class.getSimpleName();
    boolean isNeedUpdate = false;
    /* access modifiers changed from: private */
    public Listener_Update listener_update;
    /* access modifiers changed from: private */
    public Context mContext;
    public ProgressDialog mProgressDialog;

    public UpdateUtil(Context mContext2, Listener_Update listener_update2) {
        this.mContext = mContext2;
        this.listener_update = listener_update2;
    }

    public boolean checkUpdate(String packageName, int versionCode) {
        AppVersionInfo appVersionInfo;
        boolean showDialog;
        String appInfo = SharepreferenceUtil.getSharepferenceInstance(this.mContext).getLatestAppInfo();
        if (TextUtils.isEmpty(appInfo)) {
            this.isNeedUpdate = false;
            this.listener_update.isNeedUpdate(false, false);
            Log.e(TAG, "从本地没有获取到更新信息");
            return false;
        }
        try {
            appVersionInfo = (AppVersionInfo) GsonUtils.changeJsonToBean(appInfo, AppVersionInfo.class);
        } catch (Throwable th) {
            appVersionInfo = null;
        }
        if (appVersionInfo == null || appVersionInfo.getData() == null) {
            Log.e(TAG, "数据解析异常,没有获取到应用信息");
            this.isNeedUpdate = false;
            this.listener_update.isNeedUpdate(false, false);
            return false;
        }
        int versionCode_Net = getVersionCodeFormNet(packageName, appVersionInfo.getData());
        boolean isMustUpdate = getNeedUpdate(packageName, appVersionInfo.getData());
        String appName = getAppName(packageName, appVersionInfo.getData());
        String appVersion = getAppVersion(packageName, appVersionInfo.getData());
        String appSize = getAppSize(packageName, appVersionInfo.getData());
        String updateContent = getAppContent(packageName, appVersionInfo.getData());
        Logger.d("UpdateUtil", "----appVersionInfo: " + appVersionInfo.getData());
        Logger.d("UpdateUtil", "----versionCode: " + versionCode + " versionCode_Net: " + versionCode_Net + " isMustUpdate: " + isMustUpdate + " appName: " + appName + " appVersion: " + appVersion + " appSize: " + appSize + " updateContent: " + updateContent);
        if (versionCode_Net == -1) {
            this.isNeedUpdate = false;
            this.listener_update.isNeedUpdate(false, false);
            return false;
        }
        if (versionCode_Net > versionCode) {
            this.isNeedUpdate = true;
            if (isMustUpdate) {
                final String str = packageName;
                final int i = versionCode;
                DialogUtil.showUpdateDialog(true, this.mContext, appName, appVersion, appSize, updateContent, true, new Listener_Update() {
                    public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                        UpdateUtil.this.listener_update.isNeedUpdate(isUpdate, isMustUpdate);
                        if (!isUpdate) {
                            return;
                        }
                        if (UpdateUtil.this.isAvilible(UpdateUtil.this.mContext, "com.iflytek.appshop")) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.iflytek.appshop", "com.iflytek.appshop.ui.activity.MainActivity"));
                            intent.addFlags(268435456);
                            intent.putExtra("token", SharepreferenceUtil.getToken());
                            intent.putExtra("update", true);
                            Bundle bundle = new Bundle();
                            bundle.putString("key_extra_pending_intent", "goupdate");
                            intent.putExtra("key_extra_pending_intent", "goupdate");
                            intent.putExtras(bundle);
                            intent.putExtra("app_package_name", str);
                            intent.putExtra("app_version_code", String.valueOf(i));
                            UpdateUtil.this.mContext.startActivity(intent);
                            return;
                        }
                        CustomToast.showToast(UpdateUtil.this.mContext, "请安装应用商城APP");
                    }
                });
            } else {
                if (packageName.equals(BuildConfig.APPLICATION_ID)) {
                }
                AppUpdateBean updateBean = getAppUpdateBean(this.mContext, packageName);
                if (updateBean == null || updateBean.getHasBeenShowed() == 0) {
                    showDialog = true;
                    if (updateBean == null) {
                        updateBean = new AppUpdateBean();
                        updateBean.setPkgName(packageName);
                    }
                    updateBean.setHasBeenShowed(1);
                    updateBean.setVersionCode(versionCode_Net);
                    setAppUpdateBean(this.mContext, updateBean);
                } else {
                    showDialog = false;
                }
                final String str2 = packageName;
                final int i2 = versionCode;
                DialogUtil.showUpdateDialog(showDialog, this.mContext, appName, appVersion, appSize, updateContent, false, new Listener_Update() {
                    public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                        UpdateUtil.this.listener_update.isNeedUpdate(isUpdate, isMustUpdate);
                        if (!isUpdate) {
                            return;
                        }
                        if (UpdateUtil.this.isAvilible(UpdateUtil.this.mContext, "com.iflytek.appshop")) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.iflytek.appshop", "com.iflytek.appshop.ui.activity.MainActivity"));
                            intent.addFlags(268435456);
                            intent.putExtra("token", SharepreferenceUtil.getToken());
                            intent.putExtra("update", true);
                            Bundle bundle = new Bundle();
                            bundle.putString("key_extra_pending_intent", "goupdate");
                            intent.putExtra("key_extra_pending_intent", "goupdate");
                            intent.putExtras(bundle);
                            intent.putExtra("app_package_name", str2);
                            intent.putExtra("app_version_code", String.valueOf(i2));
                            UpdateUtil.this.mContext.startActivity(intent);
                            return;
                        }
                        CustomToast.showToast(UpdateUtil.this.mContext, "请安装应用商城APP");
                    }
                });
            }
            this.listener_update.isNeedUpdate(true, isMustUpdate);
        } else {
            this.isNeedUpdate = false;
            this.listener_update.isNeedUpdate(false, false);
        }
        return this.isNeedUpdate;
    }

    public static boolean checkUpdateAndStart(final Context context, final String packageName, int versionCode, final Intent intent) {
        return new UpdateUtil(context, new Listener_Update() {
            public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                if (!isMustUpdate && !isUpdate) {
                    try {
                        context.startActivity(intent);
                    } catch (Throwable exp) {
                        Logger.e(UpdateUtil.TAG, "启动app失败：" + packageName, exp);
                    }
                }
            }
        }).checkUpdate(packageName, versionCode);
    }

    private boolean getNeedUpdate(String packageName, List<AppVersionInfo.DataBean> data) {
        int updateFlag = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getAppCode().equals(packageName)) {
                updateFlag = data.get(i).getIsForceUpdate();
            }
        }
        if (updateFlag != 0 && updateFlag == 1) {
            return true;
        }
        return false;
    }

    private int getVersionCodeFormNet(String packageName, List<AppVersionInfo.DataBean> data) {
        AppVersionInfo.DataBean bean = getMatchData(packageName, data);
        if (bean == null) {
            return -1;
        }
        return bean.getVersionCode();
    }

    private String getAppName(String packageName, List<AppVersionInfo.DataBean> data) {
        AppVersionInfo.DataBean bean = getMatchData(packageName, data);
        if (bean == null) {
            return "";
        }
        return bean.getAppName();
    }

    private static final AppVersionInfo.DataBean getMatchData(String packageName, List<AppVersionInfo.DataBean> data) {
        if (ListUtils.isEmpty((List) data)) {
            return null;
        }
        for (int i = 0; i < data.size(); i++) {
            AppVersionInfo.DataBean bean = (AppVersionInfo.DataBean) ListUtils.getItem(data, i);
            if (bean != null && TextUtils.equals(bean.getAppCode(), packageName)) {
                return bean;
            }
        }
        return null;
    }

    private String getAppVersion(String packageName, List<AppVersionInfo.DataBean> data) {
        AppVersionInfo.DataBean bean = getMatchData(packageName, data);
        if (bean == null) {
            return "";
        }
        return bean.getVersion();
    }

    private String getAppSize(String packageName, List<AppVersionInfo.DataBean> data) {
        AppVersionInfo.DataBean bean = getMatchData(packageName, data);
        if (bean == null) {
            return "";
        }
        return new DecimalFormat(".00").format(bean.getFileSize()) + "MB";
    }

    private String getAppContent(String packageName, List<AppVersionInfo.DataBean> data) {
        AppVersionInfo.DataBean bean = getMatchData(packageName, data);
        if (bean == null) {
            return "";
        }
        return bean.getUpdateDetail();
    }

    /* access modifiers changed from: protected */
    public void showProgressDialog() {
        if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        ((Activity) this.mContext).runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (UpdateUtil.this.mProgressDialog.isShowing()) {
                        UpdateUtil.this.mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }

    public static AppUpdateBean getAppUpdateBean(Context context, String pkgName) {
        String beanStr = SharepreferenceUtil.getSharepferenceInstance(context).getAppUpdateState(pkgName);
        if (TextUtils.isEmpty(beanStr)) {
            Logger.d("UpdateUtil", "----getAppUpdateBean Failure: null");
            return null;
        }
        Logger.d("UpdateUtil", "----getAppUpdateBean Success: " + beanStr);
        return (AppUpdateBean) new GsonBuilder().create().fromJson(beanStr, AppUpdateBean.class);
    }

    public static void setAppUpdateBean(Context context, AppUpdateBean updateBean) {
        String str = new GsonBuilder().create().toJson((Object) updateBean);
        Logger.d("UpdateUtil", "----setAppUpdateBean Success: " + str);
        SharepreferenceUtil.getSharepferenceInstance(context).setAppUpdateState(updateBean.getPkgName(), str);
    }
}
