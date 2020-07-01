package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.util.SystemUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.androidannotations.api.rest.MediaType;

public class UserHelpActivity extends BaseFragmentActivity implements Handler.Callback {
    /* access modifiers changed from: private */
    public String htmlSource = "";
    boolean isNetError = false;
    private ImageView iv_back;
    /* access modifiers changed from: private */
    public LinearLayout linear_error;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(this);
    /* access modifiers changed from: private */
    public WebView mWebview;
    private TextView tv_content;
    /* access modifiers changed from: private */
    public TextView tv_title;
    /* access modifiers changed from: private */
    public URL url_help = null;
    /* access modifiers changed from: private */
    public String url_net = "";

    public boolean handleMessage(Message msg) {
        if (msg.what == 0) {
            this.isNetError = false;
            this.linear_error.setVisibility(8);
            this.mWebview.loadDataWithBaseURL(this.url_net, this.htmlSource, MediaType.TEXT_HTML, "utf-8", (String) null);
        }
        return super.handleMessage(msg);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.isNetError = false;
        intitUrl(this.url_net);
        LogAgentHelper.onActive();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtils.hookWebView();
        setContentView(R.layout.activity_user_help);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().getDecorView().setSystemUiVisibility(9216);
        }
        this.linear_error = (LinearLayout) findViewById(R.id.linear_error);
        this.iv_back = (ImageView) findViewById(R.id.iv_back);
        this.tv_title = (TextView) findViewById(R.id.title);
        this.mWebview = (WebView) findViewById(R.id.webView);
        this.tv_content = (TextView) findViewById(R.id.tv_content);
        this.linear_error.setVisibility(8);
        this.linear_error.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserHelpActivity.this.linear_error.setVisibility(8);
                UserHelpActivity.this.intitUrl(UserHelpActivity.this.url_net);
            }
        });
        this.mWebview.setVisibility(0);
        this.tv_content.setVisibility(8);
        this.tv_title.setText("用户手册");
        this.mWebview.getSettings().setJavaScriptEnabled(true);
        this.mWebview.addJavascriptInterface(new WebAppInterface(), "android");
        this.mWebview.setInitialScale(100);
        this.mWebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                UserHelpActivity.this.intitUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("onReceivedHttpError", "onPageStarted");
            }

            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.e("onReceivedHttpError", "onReceivedHttpError");
            }

            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                if (Build.VERSION.SDK_INT < 23) {
                    Log.e("onReceivedHttpError", "onReceivedError1");
                }
            }

            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                Log.e("onReceivedHttpError", "onReceivedError2");
            }
        });
        intitUrl("https://k12-api.openspeech.cn/pages/manual/index.html");
        this.iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (UserHelpActivity.this.mWebview == null) {
                    UserHelpActivity.this.finish();
                } else if (UserHelpActivity.this.isNetError) {
                    UserHelpActivity.this.finish();
                } else if (UserHelpActivity.this.mWebview.canGoBack()) {
                    UserHelpActivity.this.mWebview.goBack();
                } else {
                    UserHelpActivity.this.finish();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void intitUrl(final String url) {
        new Thread() {
            public void run() {
                super.run();
                try {
                    String unused = UserHelpActivity.this.url_net = url;
                    URL unused2 = UserHelpActivity.this.url_help = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) UserHelpActivity.this.url_help.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    String unused3 = UserHelpActivity.this.htmlSource = new String(UserHelpActivity.this.readInputStream(conn.getInputStream()), "UTF-8");
                    UserHelpActivity.this.mHandler.sendEmptyMessage(0);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                } catch (ProtocolException e3) {
                    e3.printStackTrace();
                } catch (IOException e4) {
                    e4.printStackTrace();
                    UserHelpActivity.this.isNetError = true;
                    UserHelpActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            UserHelpActivity.this.linear_error.setVisibility(0);
                        }
                    });
                }
            }
        }.start();
    }

    public void onClick(View v) {
    }

    private class WebAppInterface {
        private WebAppInterface() {
        }

        @JavascriptInterface
        public void finishActivity() {
            Log.e("finishActivity", "js调用了本地结束方法");
            UserHelpActivity.this.finish();
        }

        @JavascriptInterface
        public void setContentTitle(final String title) {
            UserHelpActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    UserHelpActivity.this.tv_title.setText(title);
                }
            });
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        if (this.mWebview == null) {
            finish();
            return false;
        } else if (this.isNetError) {
            finish();
            return false;
        } else if (this.mWebview.canGoBack()) {
            this.mWebview.goBack();
            return false;
        } else {
            finish();
            return false;
        }
    }

    public byte[] readInputStream(InputStream instream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1204];
        while (true) {
            int len = instream.read(buffer);
            if (len != -1) {
                outStream.write(buffer, 0, len);
            } else {
                instream.close();
                return outStream.toByteArray();
            }
        }
    }
}
