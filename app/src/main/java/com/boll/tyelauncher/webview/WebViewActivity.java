package com.boll.tyelauncher.webview;

package com.toycloud.launcher.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.iflytek.easytrans.core.utils.system.NetworkUtils;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.Constants;
import com.toycloud.launcher.ui.usercenter.StudyDailyReportMsgManager;
import com.toycloud.launcher.webview.WebViewHelper;
import framework.hz.salmon.base.BaseActivity;

public class WebViewActivity extends BaseActivity implements OnWebViewInjectListener, View.OnClickListener, WebViewHelper.OnWebViewListener, WebViewHelper.OnWebChromeListener {
    public static final String KEY_WEBVIEW_TITLE = "key_webview_title";
    public static final String KEY_WEBVIEW_URL = "key_webview_url";
    private static final String TAG = "WebViewActivity";
    private TextView mEmptyTipsTv;
    protected View mFailedLLyt;
    private ViewStub mFailedViewStub;
    protected boolean mIsLoadFailed;
    private WebViewJsInject mJsInject;
    protected boolean mNeedShowProgress = true;
    private ProgressBar mProgressBar;
    private String mTitleStr;
    protected String mUrl;
    protected WebView mWebView;
    private WebViewHelper mWebViewHelper;

    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_layout);
        Window window = getWindow();
        window.clearFlags(201326592);
        window.getDecorView().setSystemUiVisibility(9984);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(0);
        window.setNavigationBarColor(0);
        Intent intent = getIntent();
        if (intent != null) {
            this.mTitleStr = intent.getStringExtra("key_webview_title");
            this.mUrl = intent.getStringExtra("key_webview_url");
            if (intent.getBooleanExtra(StudyDailyReportMsgManager.EXTRA_INTENT_TO_STUDY_DAILY_REPORT, false)) {
                LogUtils.d(TAG, "onCreate: 更新学习日报查看的时间");
                StudyDailyReportMsgManager.getInstance().updateStudyReportShowTime(this, System.currentTimeMillis());
                sendBroadcast(new Intent(Constants.ACTION_STUDY_REPORT_SHOWED_TIME_UPDATED));
            }
            LogUtils.d(TAG, "页面地址: " + this.mUrl);
            initView();
            buildWebView();
            if (this.mWebView != null) {
                this.mWebView.setVisibility(0);
                this.mWebView.loadUrl(this.mUrl);
                return;
            }
            return;
        }
        LogUtils.d(TAG, "onCreate: 没有设置任何可展示的页面");
        finish();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String url = intent.getStringExtra("key_webview_url");
            if (TextUtils.equals(url, this.mUrl)) {
                LogUtils.d(TAG, "onNewIntent: 相同的页面地址，不做重复加载");
                return;
            }
            this.mTitleStr = intent.getStringExtra("key_webview_title");
            this.mUrl = url;
            if (intent.getBooleanExtra(StudyDailyReportMsgManager.EXTRA_INTENT_TO_STUDY_DAILY_REPORT, false)) {
                LogUtils.d(TAG, "onCreate: 更新学习日报查看的时间");
                StudyDailyReportMsgManager.getInstance().updateStudyReportShowTime(this, System.currentTimeMillis());
                sendBroadcast(new Intent(Constants.ACTION_STUDY_REPORT_SHOWED_TIME_UPDATED));
            }
            if (this.mWebView != null) {
                this.mWebView.setVisibility(0);
                this.mWebView.loadUrl(this.mUrl);
                return;
            }
            return;
        }
        LogUtils.d(TAG, "onNewIntent: 没有设置任何可展示的页面");
    }

    /* access modifiers changed from: protected */
    public void initView() {
        this.mProgressBar = (ProgressBar) findViewById(R.id.web_view_pb);
        this.mWebView = (WebView) findViewById(R.id.web_view);
        this.mFailedViewStub = (ViewStub) findViewById(R.id.vstub_query_failed);
        refreshTitle();
    }

    private void refreshTitle() {
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public void buildWebView() {
        this.mWebViewHelper = new WebViewHelper(this);
        this.mWebViewHelper.setWebViewListener(this);
        this.mWebViewHelper.setWebChromListener(this);
        if (this.mJsInject == null) {
            this.mJsInject = new WebViewJsInject(this, this.mHandler, this);
        }
        this.mWebViewHelper.setJsInject(this.mJsInject);
        this.mWebViewHelper.initWebView(this.mWebView);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LogUtils.d(TAG, "onPageStarted:url = " + url);
        pageStart();
    }

    public void onPageFinished(WebView view, String url) {
        LogUtils.d(TAG, "onPageFinished:url = " + url);
        if (!this.mIsLoadFailed) {
            this.mWebView.setVisibility(0);
        }
        pageEnd();
    }

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (request.isForMainFrame()) {
            pageEnd();
            this.mIsLoadFailed = true;
            showFailedLayout(true);
        }
    }

    public void onProgressChanged(WebView view, int newProgress) {
        changeProgress(newProgress);
    }

    public void onReceivedTitle(WebView view, String title) {
        this.mTitleStr = title;
        refreshTitle();
    }

    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return false;
    }

    public boolean onKeyBack() {
        handleGoBack();
        return true;
    }

    private void handleGoBack() {
        if (this.mWebView.canGoBack()) {
            this.mWebView.goBack();
        } else {
            finish();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mWebView != null) {
            this.mWebView.setVisibility(8);
            this.mWebView.removeAllViews();
            this.mWebView.destroy();
        }
    }

    private void pageStart() {
        if (this.mNeedShowProgress) {
            this.mProgressBar.setVisibility(0);
        }
    }

    private void pageEnd() {
        if (this.mNeedShowProgress) {
            this.mProgressBar.setVisibility(8);
        }
    }

    private void changeProgress(int progress) {
        if (this.mNeedShowProgress) {
            this.mProgressBar.setProgress(progress);
        }
    }

    public void onWebBackKeyClick() {
        handleGoBack();
    }

    /* access modifiers changed from: protected */
    public void initFailedLayout() {
        if (this.mFailedLLyt == null) {
            this.mFailedLLyt = this.mFailedViewStub.inflate();
            this.mEmptyTipsTv = (TextView) this.mFailedLLyt.findViewById(R.id.tv_empty);
            this.mFailedViewStub = null;
        }
    }

    /* access modifiers changed from: protected */
    public void showFailedLayout(boolean show) {
        Drawable drawable;
        if (!isFinishing() && !isDestroyed()) {
            if (show) {
                initFailedLayout();
                this.mFailedLLyt.setVisibility(0);
                this.mWebView.setVisibility(8);
                this.mWebView.loadUrl("about:blank");
                this.mFailedLLyt.setOnClickListener(this);
                if (NetworkUtils.isNetworkAvailable(this)) {
                    drawable = getDrawable(R.mipmap.def_net_weak);
                    this.mEmptyTipsTv.setText(R.string.load_h5_page_failed);
                } else {
                    drawable = getDrawable(R.mipmap.def_net_weak);
                    this.mEmptyTipsTv.setText(R.string.net_work_disconnected);
                }
                this.mEmptyTipsTv.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, drawable, (Drawable) null, (Drawable) null);
                return;
            }
            if (this.mFailedLLyt != null) {
                this.mFailedLLyt.setVisibility(8);
            }
            if (this.mWebView != null) {
                this.mWebView.setVisibility(0);
            }
        }
    }

    public void onClick(View v) {
        if (v == this.mFailedLLyt) {
            showFailedLayout(false);
            this.mIsLoadFailed = false;
            this.mWebView.loadUrl(this.mUrl);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mWebViewHelper != null) {
            this.mWebViewHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean handleMessage(Message msg) {
        return false;
    }
}
