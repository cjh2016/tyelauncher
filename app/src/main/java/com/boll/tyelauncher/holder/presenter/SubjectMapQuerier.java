package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.iflytek.utils.LauncherExecutors;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.util.GsonUtils;
import framework.hz.salmon.retrofit.ApiException;
import framework.hz.salmon.retrofit.BaseSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.AbstractExecutorService;

public class SubjectMapQuerier {
    public static final Handler mUIHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public Callback mCallback;
    public final Context mContext;
    public final long mDelay;
    private long mLastQueryTime = 0;
    public final String mSubjectCode;
    /* access modifiers changed from: private */
    public Disposable mSubscription;

    public interface Callback {
        void onQueryMapError(String str, CatalogItem catalogItem, Throwable th);

        void onQueryMapSuccess(String str, CatalogItem catalogItem, MappingInfoResponse mappingInfoResponse);
    }

    public SubjectMapQuerier(Context context, String subjectCode, long delay) {
        this.mContext = context;
        this.mSubjectCode = subjectCode;
        this.mDelay = delay;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void queryImmediately(final CatalogItem catalogInfo) {
        if (catalogInfo != null && TextUtils.equals(catalogInfo.subjectCode, this.mSubjectCode)) {
            this.mLastQueryTime = System.currentTimeMillis();
            mUIHandler.removeCallbacksAndMessages((Object) null);
            cancel();
            LauncherHttpHelper.getHttpService().getMappingInfo(this.mSubjectCode, catalogInfo.phaseCode, catalogInfo.chapterCode).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<MappingInfoResponse>(this.mContext) {
                public void onStart() {
                }

                public void onSubscribe(Disposable d) {
                    super.onSubscribe(d);
                    Disposable unused = SubjectMapQuerier.this.mSubscription = d;
                }

                public void onNext(MappingInfoResponse mappingInfoResponse) {
                    super.onNext(mappingInfoResponse);
                    Disposable unused = SubjectMapQuerier.this.mSubscription = null;
                    dispatchResp(mappingInfoResponse, (AbstractExecutorService) null);
                }

                private void dispatchResp(MappingInfoResponse mappingInfoResponse, AbstractExecutorService executor) {
                    if (executor != null) {
                        executor.execute(new SubjectMapQuerier$1$$Lambda$0(this, catalogInfo, mappingInfoResponse));
                    } else if (SubjectMapQuerier.this.mCallback != null) {
                        SubjectMapQuerier.this.mCallback.onQueryMapSuccess(SubjectMapQuerier.this.mSubjectCode, catalogInfo, mappingInfoResponse);
                    }
                }

                /* access modifiers changed from: package-private */
                public final /* synthetic */ void lambda$dispatchResp$15$SubjectMapQuerier$1(CatalogItem catalogInfo, MappingInfoResponse mappingInfoResponse) {
                    if (SubjectMapQuerier.this.mCallback != null) {
                        SubjectMapQuerier.this.mCallback.onQueryMapSuccess(SubjectMapQuerier.this.mSubjectCode, catalogInfo, mappingInfoResponse);
                    }
                }

                private void inflateRespAndDisptch(ApiException apiException) {
                    LauncherExecutors.sAppLoaderExecutor.execute(new SubjectMapQuerier$1$$Lambda$1(this, apiException));
                }

                /* access modifiers changed from: package-private */
                public final /* synthetic */ void lambda$inflateRespAndDisptch$16$SubjectMapQuerier$1(ApiException apiException) {
                    try {
                        dispatchResp((MappingInfoResponse) GsonUtils.changeJsonToBean(apiException.mResp, MappingInfoResponse.class), LauncherExecutors.sMainExecutor);
                    } catch (Throwable th) {
                        dispatchError(apiException);
                    }
                }

                private void dispatchError(ApiException apiException) {
                    LauncherExecutors.sMainExecutor.execute(new SubjectMapQuerier$1$$Lambda$2(this, apiException, catalogInfo));
                }

                /* access modifiers changed from: package-private */
                public final /* synthetic */ void lambda$dispatchError$17$SubjectMapQuerier$1(ApiException apiException, CatalogItem catalogInfo) {
                    super.onError(apiException);
                    if (SubjectMapQuerier.this.mCallback != null) {
                        SubjectMapQuerier.this.mCallback.onQueryMapError(SubjectMapQuerier.this.mSubjectCode, catalogInfo, apiException);
                    }
                }

                public void onError(Throwable e) {
                    Disposable unused = SubjectMapQuerier.this.mSubscription = null;
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        if (apiException.mErrorCode == 3007) {
                            inflateRespAndDisptch(apiException);
                            return;
                        }
                    }
                    super.onError(e);
                    if (SubjectMapQuerier.this.mCallback != null) {
                        SubjectMapQuerier.this.mCallback.onQueryMapError(SubjectMapQuerier.this.mSubjectCode, catalogInfo, e);
                    }
                }

                public void onCompleted() {
                    super.onCompleted();
                }
            });
        }
    }

    public void queryMaybeDelay(final CatalogItem catalogInfo) {
        if (catalogInfo != null && TextUtils.equals(catalogInfo.subjectCode, this.mSubjectCode)) {
            mUIHandler.removeCallbacksAndMessages((Object) null);
            if (System.currentTimeMillis() - this.mLastQueryTime >= MapQuerier.DELAY) {
                queryImmediately(catalogInfo);
            } else {
                mUIHandler.postDelayed(new Runnable() {
                    public void run() {
                        SubjectMapQuerier.this.queryImmediately(catalogInfo);
                    }
                }, this.mDelay);
            }
        }
    }

    public void cancel() {
        if (this.mSubscription != null && !this.mSubscription.isDisposed()) {
            this.mSubscription.dispose();
        }
        this.mSubscription = null;
    }

    private boolean isQuering() {
        return this.mSubscription != null;
    }
}
