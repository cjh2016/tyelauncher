package com.boll.tyelauncher.webview;

package com.toycloud.launcher.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.toycloud.launcher.BuildConfig;
import com.toycloud.launcher.holder.helper.StudyCPHelper;

public class WebViewHelper {
    public static final int FILECHOOSER_RESULTCODE = 1;
    public static final int FILECHOOSER_RESULTCODE_FOR_ANDORID_5 = 2;
    private static final String TAG = "WebViewHelper";
    protected Context mContext;
    private WebViewJsInject mJsInject;
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    /* access modifiers changed from: private */
    public OnWebChromeListener mWebChromeListener;
    /* access modifiers changed from: private */
    public OnWebViewListener mWebViewListener;

    public interface OnWebChromeListener {
        boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult);

        void onProgressChanged(WebView webView, int i);

        void onReceivedTitle(WebView webView, String str);
    }

    public interface OnWebViewListener {
        boolean onKeyBack();

        void onPageFinished(WebView webView, String str);

        void onPageStarted(WebView webView, String str, Bitmap bitmap);

        void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError);
    }

    public void setWebViewListener(OnWebViewListener listener) {
        this.mWebViewListener = listener;
    }

    public void setWebChromListener(OnWebChromeListener listener) {
        this.mWebChromeListener = listener;
    }

    public WebViewHelper(Context context) {
        this.mContext = context;
    }

    public void setJsInject(WebViewJsInject jsInject) {
        this.mJsInject = jsInject;
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public void initWebView(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.d(WebViewHelper.TAG, "shouldOverrideUrlLoading:url:" + url);
                view.loadUrl(url);
                return false;
            }

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.d(WebViewHelper.TAG, "onPageStarted");
                if (WebViewHelper.this.mWebViewListener != null) {
                    WebViewHelper.this.mWebViewListener.onPageStarted(view, url, favicon);
                }
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.d(WebViewHelper.TAG, "onPageFinished");
                WebViewHelper.this.injectNative(view);
                if (WebViewHelper.this.mWebViewListener != null) {
                    WebViewHelper.this.mWebViewListener.onPageFinished(view, url);
                }
            }

            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtils.d(WebViewHelper.TAG, "onReceivedError: 页面加载失败");
                if (error != null) {
                    LogUtils.d(WebViewHelper.TAG, "onReceivedError:" + error.getDescription() + error.getErrorCode());
                }
                if (WebViewHelper.this.mWebViewListener != null) {
                    WebViewHelper.this.mWebViewListener.onReceivedError(view, request, error);
                }
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                LogUtils.d(WebViewHelper.TAG, "onReceivedSslError: 页面加载失败");
                handler.proceed();
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || keyCode != 4 || WebViewHelper.this.mWebViewListener == null || !WebViewHelper.this.mWebViewListener.onKeyBack()) {
                    return false;
                }
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                LogUtils.d(WebViewHelper.TAG, "onProgressChanged");
                if (WebViewHelper.this.mWebChromeListener != null) {
                    WebViewHelper.this.mWebChromeListener.onProgressChanged(view, newProgress);
                }
            }

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtils.d(WebViewHelper.TAG, "onReceivedTitle");
                if (WebViewHelper.this.mWebChromeListener != null) {
                    WebViewHelper.this.mWebChromeListener.onReceivedTitle(view, title);
                }
            }

            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                LogUtils.d(WebViewHelper.TAG, "onJsAlert : " + message);
                if (WebViewHelper.this.mWebViewListener == null || !WebViewHelper.this.mWebChromeListener.onJsAlert(view, url, message, result)) {
                    return super.onJsAlert(view, url, message, result);
                }
                result.confirm();
                return true;
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                WebViewHelper.this.openFileChooserImplForAndroid5(uploadMsg);
                return true;
            }
        });
        webView.requestFocus(130);
        WebSettings settings = webView.getSettings();
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(2);
        settings.setGeolocationEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setBlockNetworkLoads(false);
        settings.setBlockNetworkImage(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setUseWideViewPort(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(this.mContext.getApplicationContext().getDir("appcache", 0).getPath());
        settings.setMixedContentMode(0);
        settings.setUserAgentString(settings.getUserAgentString() + " " + StudyCPHelper.COLUMN_NAME_VERSION + BuildConfig.VERSION_NAME);
        settings.setJavaScriptEnabled(true);
        injectNative(webView);
        WebView.setWebContentsDebuggingEnabled(false);
        webView.setLayerType(2, (Paint) null);
    }

    /* access modifiers changed from: private */
    public void injectNative(WebView webView) {
        if (this.mJsInject != null) {
            webView.addJavascriptInterface(this.mJsInject, "iflyk12");
        }
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        if (this.mContext != null && (this.mContext instanceof Activity)) {
            this.mUploadMessage = uploadMsg;
            Intent i = new Intent("android.intent.action.GET_CONTENT");
            i.addCategory("android.intent.category.OPENABLE");
            i.setType("image/*");
            ((Activity) this.mContext).startActivityForResult(Intent.createChooser(i, "File Chooser"), 1);
        }
    }

    /* access modifiers changed from: private */
    public void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        if (this.mContext != null && (this.mContext instanceof Activity)) {
            this.mUploadMessageForAndroid5 = uploadMsg;
            Intent contentSelectionIntent = new Intent("android.intent.action.GET_CONTENT");
            contentSelectionIntent.addCategory("android.intent.category.OPENABLE");
            contentSelectionIntent.setType("image/*");
            Intent chooserIntent = new Intent("android.intent.action.CHOOSER");
            chooserIntent.putExtra("android.intent.extra.INTENT", contentSelectionIntent);
            chooserIntent.putExtra("android.intent.extra.TITLE", "Image Chooser");
            ((Activity) this.mContext).startActivityForResult(chooserIntent, 2);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (this.mUploadMessage != null) {
                this.mUploadMessage.onReceiveValue((intent == null || resultCode != -1) ? null : intent.getData());
                this.mUploadMessage = null;
            }
        } else if (requestCode == 2 && this.mUploadMessageForAndroid5 != null) {
            Uri result = (intent == null || resultCode != -1) ? null : intent.getData();
            if (result != null) {
                this.mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                this.mUploadMessageForAndroid5.onReceiveValue(new Uri[0]);
            }
            this.mUploadMessageForAndroid5 = null;
        }
    }
}