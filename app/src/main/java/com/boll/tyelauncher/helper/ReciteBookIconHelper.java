package com.boll.tyelauncher.helper;


import android.content.Context;

import com.boll.tyelauncher.application.LauncherApplication;
import com.boll.tyelauncher.model.ReciteBookShowStrategyResult;
import com.boll.tyelauncher.util.NetworkUtil;
import com.boll.tyelauncher.util.PadInfoUtil;
import com.boll.tyelauncher.util.SharepreferenceUtil;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.util.Logging;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReciteBookIconHelper {
    private static final String TAG = "ReciteBookIconHelper";
    /* access modifiers changed from: private */
    public static Context sContext = LauncherApplication.getContext();
    /* access modifiers changed from: private */
    public static volatile boolean sIsRequesting;

    public interface IShowReciteCallback {
        boolean showReciteBookIcon();
    }

    public static boolean isNeedShowReciteBookIcon() {
        if (!SharepreferenceUtil.getSharepferenceInstance(sContext).isReciteBookShowStrategyRequested()) {
            return false;
        }
        boolean needShow = SharepreferenceUtil.getSharepferenceInstance(sContext).isReciteBookNeedShow();
        boolean showed = SharepreferenceUtil.getSharepferenceInstance(sContext).isReciteBookShowed();
        boolean unistalled = SharepreferenceUtil.getSharepferenceInstance(sContext).isReciteBookUninstalled();
        if (!needShow || !showed || unistalled) {
            return false;
        }
        return true;
    }

    public static void requestReciteBookShowPolicy(final IShowReciteCallback listener) {
        if (SharepreferenceUtil.getSharepferenceInstance(sContext).isReciteBookShowStrategyRequested()) {
            if (!SharepreferenceUtil.getSharepferenceInstance(sContext).isReciteBookNeedShow() || SharepreferenceUtil.getSharepferenceInstance(sContext).isReciteBookShowed()) {
                Logging.d(TAG, "requestReciteBookShowPolicy() requested = true, and icon have showed, return");
                return;
            }
            Logging.d(TAG, "requestReciteBookShowPolicy() requested = true, but not showed, show it");
            if (listener != null && listener.showReciteBookIcon()) {
                SharepreferenceUtil.getSharepferenceInstance(sContext).setReciteBookShowed(true);
            }
        } else if (!NetworkUtil.isNetworkAvailable(sContext)) {
            Logging.d(TAG, "requestReciteBookShowPolicy() network is not available, return");
        } else if (sIsRequesting) {
            Logging.d(TAG, "requestReciteBookShowPolicy() sIsRequesting = true, return");
        } else {
            sIsRequesting = true;
            Logging.d(TAG, "requestReciteBookShowPolicy() start request now");
            LauncherHttpHelper.getLauncherService().getReciteBookShowStrategy(new PadInfoUtil(sContext).getSnCode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ReciteBookShowStrategyResult>(sContext, false) {
                public void onCompleted() {
                    Logging.d(ReciteBookIconHelper.TAG, "onCompleted()");
                    boolean unused = ReciteBookIconHelper.sIsRequesting = false;
                }

                public void onError(Throwable e) {
                    Logging.d(ReciteBookIconHelper.TAG, "onError() e = " + e);
                    boolean unused = ReciteBookIconHelper.sIsRequesting = false;
                }

                public void onNext(ReciteBookShowStrategyResult response) {
                    ReciteBookShowStrategyResult.DataBean dataBean;
                    Logging.d(ReciteBookIconHelper.TAG, "onNext() response = " + response);
                    if (response != null && (dataBean = response.getData()) != null) {
                        SharepreferenceUtil.getSharepferenceInstance(ReciteBookIconHelper.sContext).setReciteBookShowStrategyRequested(true);
                        SharepreferenceUtil.getSharepferenceInstance(ReciteBookIconHelper.sContext).setReciteBookNeedShow(dataBean.isBefore());
                        if (dataBean.isBefore() && listener != null && listener.showReciteBookIcon()) {
                            SharepreferenceUtil.getSharepferenceInstance(ReciteBookIconHelper.sContext).setReciteBookShowed(true);
                        }
                    }
                }
            });
        }
    }
}
