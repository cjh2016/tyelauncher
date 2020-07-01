package com.boll.tyelauncher.util;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.api.service.LauncherHttpHelper;
import com.boll.tyelauncher.framework.retrofit.BaseSubscriber;
import com.boll.tyelauncher.model.GetAllApps;
import com.boll.tyelauncher.model.UploadLocalAppBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Iterator;

public class MyAsnyncTaskToUploadApp extends AsyncTask<Void, Integer, ArrayList<AppInfo>> {
    private Context mContext;

    public MyAsnyncTaskToUploadApp(Context mContext2) {
        this.mContext = mContext2;
    }

    /* access modifiers changed from: protected */
    @Override
    public ArrayList<AppInfo> doInBackground(Void... voids) {
        return new GetAllApps(this.mContext).getDatas();
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(ArrayList<AppInfo> list) {
        super.onPostExecute(list);
        upLoadLocalApps(this.mContext, list);
    }

    public void upLoadLocalApps(Context context, ArrayList<AppInfo> appList) {
        LogSaveManager_Util.saveLog(context, "upLoadLocalApps");
        String snCode = new PadInfoUtil(this.mContext).getSnCode();
        UploadLocalAppsRequest request = new UploadLocalAppsRequest();
        request.sn = snCode;
        Iterator<AppInfo> it = appList.iterator();
        while (it.hasNext()) {
            AppInfo appInfo = it.next();
            UploadAppInfo info = new UploadAppInfo();
            info.sn = snCode;
            info.appCode = appInfo.getPakageName();
            info.appName = appInfo.appName;
            request.appList.add(info);
        }
        LauncherHttpHelper.getLauncherService().upLoadLocalApps(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<UploadLocalAppBean>(context, false) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e("MainActivity-->", "上传应用列表失败");
                SharepreferenceUtil.getSharepferenceInstance(this.mContext).setAppChanged(true);
            }

            @Override
            public void onNext(UploadLocalAppBean response) {
                super.onNext(response);
                SharepreferenceUtil.getSharepferenceInstance(this.mContext).setAppChanged(false);
            }
        });
    }
}
