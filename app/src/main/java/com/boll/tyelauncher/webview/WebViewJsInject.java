package com.boll.tyelauncher.webview;


import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.application.LauncherApplication;
import com.toycloud.launcher.util.SharepreferenceUtil;

public class WebViewJsInject {
    public static final String INJECT_OBJECT_ALIAS = "iflyk12";
    private static final String TAG = "WebViewJsInject";
    private Context mContext;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public OnWebViewInjectListener mListener;

    public WebViewJsInject(Context context, Handler handler, OnWebViewInjectListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mHandler = handler;
    }

    @JavascriptInterface
    public void toast(String message) {
        LogUtils.d(TAG, "toast: " + message);
    }

    @JavascriptInterface
    public void onClickWebBackBtn() {
        LogUtils.d(TAG, "goBack: ");
        if (this.mListener == null) {
            return;
        }
        if (this.mHandler != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    WebViewJsInject.this.mListener.onWebBackKeyClick();
                }
            });
        } else {
            this.mListener.onWebBackKeyClick();
        }
    }

    @JavascriptInterface
    public String getToken() {
        String token = SharepreferenceUtil.getToken();
        LogUtils.d(TAG, "getToken: 当前token：" + token);
        return token;
    }

    @JavascriptInterface
    public String getUserPhoneNumber(boolean encrypt) {
        User user = SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).getUserInfo();
        if (user == null) {
            return "";
        }
        if (encrypt) {
            return user.getSecurityUserPhone();
        }
        return user.getUsername();
    }

    @JavascriptInterface
    public String getUserRealName() {
        User user = SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).getUserInfo();
        if (user != null) {
            return user.getRealname();
        }
        return "";
    }

    @JavascriptInterface
    public String getUserIcon() {
        User user = SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).getUserInfo();
        if (user != null) {
            return user.getUsericonpath();
        }
        return "";
    }
}
