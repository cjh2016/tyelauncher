package com.boll.tyelauncher.framework.retrofit;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.boll.tyelauncher.util.NetworkUtil;

import framework.hz.salmon.util.CustomToast;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpStatus;
import retrofit2.HttpException;

public class BaseSubscriber<T> implements Observer<T> {
    private static final String BROADCAST_ACTION_DISC = "com.toycloud.permissions.broadcast_token_error";
    private static final String BROADCAST_PERMISSION_DISC = "com.toycloud.permissions.BROADCAST_TOKEN_ERROR";
    private static String TAG = "BaseSubscriber";
    private Disposable disposable;
    private boolean isShowNetError = true;
    /* access modifiers changed from: protected */
    public Context mContext;

    public BaseSubscriber(Context context) {
        this.mContext = context;
    }

    public BaseSubscriber(Context context, boolean isShowNetError2) {
        this.mContext = context;
        this.isShowNetError = isShowNetError2;
    }

    @Override
    public void onComplete() {
        Log.e("", "");
        if (this.disposable != null && !this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
        onCompleted();
    }

    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Log.w(TAG, e.getMessage() + ":" + this.isShowNetError);
        if (!NetworkUtil.isNetworkAvailable(this.mContext) && this.isShowNetError) {
            CustomToast.showToast(this.mContext, "网络连接错误，请检查网络设置后重试");
        }
        if (e instanceof HttpException) {
            Log.e("Throwable-->", ((HttpException) e).toString() + ":");
            switch (((HttpException) e).code()) {
                case HttpStatus.SC_UNAUTHORIZED /*401*/:
                    Intent intent = new Intent();
                    intent.setAction(BROADCAST_ACTION_DISC);
                    this.mContext.sendBroadcast(intent, BROADCAST_PERMISSION_DISC);
                    try {
                        ((Activity) this.mContext).finish();
                        return;
                    } catch (Exception e2) {
                        return;
                    }
                default:
                    return;
            }
        } else if (e instanceof IOException) {
            if ((e instanceof SocketTimeoutException) && this.isShowNetError) {
                CustomToast.showToast(this.mContext, "连接超时");
            }
        } else if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            if (e.getMessage().contains("token")) {
                Toast.makeText(this.mContext, "您的账号已在其他设备中登录，如非本人操作，则密码可能已经泄露，建议及时修改密码", 0).show();
            } else if (!NetworkUtil.isNetworkAvailable(this.mContext) && this.isShowNetError) {
                CustomToast.showToast(this.mContext, "网络连接错误，请检查网络设置后重试");
            }
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
        onStart();
    }

    public void onStart() {
    }

    @Override
    public void onNext(T t) {
    }
}