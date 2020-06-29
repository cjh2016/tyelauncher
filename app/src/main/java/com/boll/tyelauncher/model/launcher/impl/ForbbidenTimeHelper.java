package com.boll.tyelauncher.model.launcher.impl;


import android.content.Context;

import com.boll.tyelauncher.model.ForbiddenAppTimeList;
import com.boll.tyelauncher.model.ForbiddenTime;
import com.boll.tyelauncher.util.ListUtils;
import com.boll.tyelauncher.util.PadInfoUtil;
import framework.hz.salmon.retrofit.BaseSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class ForbbidenTimeHelper {
    private final Context mContext;
    /* access modifiers changed from: private */
    public final IForbbidenTimeListener mListener;
    /* access modifiers changed from: private */
    public Disposable mSubscription;
    /* access modifiers changed from: private */
    public ForbiddenTime mWeekDay;
    /* access modifiers changed from: private */
    public ForbiddenTime mWeekEnd;

    public interface IForbbidenTimeListener {
        void onForbbidenTimeConfig(List<String> list, ForbiddenTime forbiddenTime, ForbiddenTime forbiddenTime2);
    }

    public ForbbidenTimeHelper(Context context, IForbbidenTimeListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void query() {
        if (this.mSubscription != null && !this.mSubscription.isDisposed()) {
            this.mSubscription.dispose();
        }
        this.mSubscription = null;
        LauncherHttpHelper.getLauncherService().getForbiddenAPPTime(new PadInfoUtil(this.mContext).getSnCode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ForbiddenAppTimeList>(this.mContext, false) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                Disposable unused = ForbbidenTimeHelper.this.mSubscription = d;
            }

            public void onError(Throwable e) {
                super.onError(e);
            }

            public void onNext(ForbiddenAppTimeList response) {
                super.onNext(response);
                if (response != null) {
                    List<ForbiddenAppTimeList.DataBean> data = response.getData();
                    if (!ListUtils.isEmpty((List) data)) {
                        for (ForbiddenAppTimeList.DataBean dataBean : data) {
                            if (dataBean != null) {
                                if (dataBean.getType() == 0) {
                                    ForbiddenTime unused = ForbbidenTimeHelper.this.mWeekDay = ForbbidenTimeHelper.this.convert(dataBean);
                                } else if (dataBean.getType() == 1) {
                                    ForbiddenTime unused2 = ForbbidenTimeHelper.this.mWeekEnd = ForbbidenTimeHelper.this.convert(dataBean);
                                }
                            }
                        }
                        if (ForbbidenTimeHelper.this.mWeekDay != null || ForbbidenTimeHelper.this.mWeekEnd != null) {
                            ForbbidenTimeHelper.this.queryAppConfigs();
                        }
                    }
                }
            }
        });
    }

    public void queryAppConfigs() {
        LauncherHttpHelper.getLauncherService().getForbiddenTimeApp(new PadInfoUtil(this.mContext).getSnCode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<HappApp>(this.mContext, false) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                Disposable unused = ForbbidenTimeHelper.this.mSubscription = d;
            }

            public void onError(Throwable e) {
                super.onError(e);
            }

            public void onNext(HappApp response) {
                super.onNext(response);
                List<String> data = response.getData();
                if (!ListUtils.isEmpty((List) data) && ForbbidenTimeHelper.this.mListener != null) {
                    ForbbidenTimeHelper.this.mListener.onForbbidenTimeConfig(data, ForbbidenTimeHelper.this.mWeekDay, ForbbidenTimeHelper.this.mWeekEnd);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public ForbiddenTime convert(ForbiddenAppTimeList.DataBean dataBean) {
        if (dataBean == null) {
            return null;
        }
        ForbiddenTime time = new ForbiddenTime();
        time.setType(dataBean.getType());
        time.setStartTime(dataBean.getStartTime());
        time.setEndTime(dataBean.getEndTime());
        return time;
    }
}
