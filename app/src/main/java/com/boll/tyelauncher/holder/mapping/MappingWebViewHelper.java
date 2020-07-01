package com.boll.tyelauncher.holder.mapping;

package com.toycloud.launcher.holder.mapping;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.toycloud.launcher.api.model.AnchorPointData;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.util.GsonUtils;
import java.util.List;

public class MappingWebViewHelper {
    public static final String MAPPING_URL = "file:///android_asset/mapping/anchors.html";
    private static final String TAG = "MappingWebViewHelper";

    public static void initWebView(WebView webView, WebViewClient webViewClient) {
        webView.requestFocus(130);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(2);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(webViewClient);
        webView.setBackgroundColor(0);
        Drawable bg = webView.getBackground();
        if (bg != null) {
            bg.setAlpha(0);
        }
        loadUrl(webView);
    }

    public static void loadUrl(WebView webView) {
        webView.loadUrl(MAPPING_URL);
    }

    public static void refreshEmptyMapping(WebView webView, String chapterMapping) {
        webView.evaluateJavascript("javascript:showAnchorGraph(" + chapterMapping + "," + chapterMapping + ",'" + "null" + "'," + true + "," + false + "," + false + ")", (ValueCallback) null);
    }

    public static void showMapping(WebView webView, MappingInfoResponse data, boolean isShowAnimation) {
        String userAnchorGraph;
        List<AnchorPointData> anchorDtoParamBeans;
        boolean z = false;
        MappingInfoResponse.PresentKGMasteryItemsBean userAnchorGraphToday = data.getData().getPresentKGMasteryItems();
        String chapterMapping = "";
        if (!(data.getData() == null || (anchorDtoParamBeans = data.getData().getAnchorDtoParamBeans()) == null)) {
            chapterMapping = GsonUtils.toJson(anchorDtoParamBeans);
        }
        if (userAnchorGraphToday == null || userAnchorGraphToday.getKGMasteryItems() == null) {
            userAnchorGraph = chapterMapping;
        } else {
            userAnchorGraph = GsonUtils.toJson(userAnchorGraphToday.getKGMasteryItems());
        }
        StringBuilder append = new StringBuilder().append("javascript:showAnchorGraph(").append(chapterMapping).append(",").append(userAnchorGraph).append(",'").append("null").append("',").append(false).append(",").append(isShowAnimation).append(",");
        if (!(userAnchorGraphToday == null || userAnchorGraphToday.getMeasureStatus() == 0 || data.getData().getIsInit() == 0)) {
            z = true;
        }
        webView.evaluateJavascript(append.append(z).append(")").toString(), (ValueCallback) null);
        Log.d(TAG, "正在显示图谱(chapterMapping)：" + chapterMapping);
        Log.d(TAG, "正在显示图谱(userAnchorGraph)：" + userAnchorGraph);
    }

    public static final int getTotalPoint(MappingInfoResponse data) {
        if (data == null || data.getData() == null || data.getData().getAnchorDtoParamBeans() == null) {
            return 0;
        }
        return data.getData().getAnchorDtoParamBeans().size();
    }

    public static final int getCover2Point(MappingInfoResponse data) {
        List<MappingInfoResponse.KGMasteryItemsBean> kgMasteryItems;
        int coverPoint2 = 0;
        MappingInfoResponse.PresentKGMasteryItemsBean userAnchorGraphToday = data.getData().getPresentKGMasteryItems();
        if (!(userAnchorGraphToday == null || (kgMasteryItems = userAnchorGraphToday.getKGMasteryItems()) == null)) {
            for (MappingInfoResponse.KGMasteryItemsBean kgMasteryItem : kgMasteryItems) {
                if (kgMasteryItem.getRealMastery() >= 0.0d) {
                    coverPoint2++;
                }
            }
        }
        return coverPoint2;
    }

    public static void setWebViewClickListener(WebView webView, View.OnClickListener clickListener) {
        webView.setOnTouchListener(new WebViewTouchListener(webView, clickListener));
    }

    private static final class WebViewTouchListener implements View.OnTouchListener {
        private static int slop;
        private static int tapTimeout;
        private long mDownMillis;
        private final PointF mDownPoint = new PointF();
        private boolean mIsTouched = false;
        private final View.OnClickListener mOnClickListener;
        private final WebView mWebView;

        public WebViewTouchListener(WebView webView, View.OnClickListener clickListener) {
            slop = ViewConfiguration.get(webView.getContext()).getScaledTouchSlop();
            tapTimeout = ViewConfiguration.getTapTimeout();
            this.mWebView = webView;
            this.mOnClickListener = clickListener;
        }

        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (event.getPointerCount() > 1) {
                this.mIsTouched = false;
            } else {
                float x = event.getX();
                float y = event.getY();
                if (action == 0) {
                    this.mIsTouched = true;
                    this.mDownPoint.set(x, y);
                    this.mDownMillis = System.currentTimeMillis();
                } else if (action == 2) {
                    if (this.mIsTouched) {
                        float dx = Math.abs(x - this.mDownPoint.x);
                        float dy = Math.abs(y - this.mDownPoint.y);
                        if (dx > ((float) slop) || dy > ((float) slop) || System.currentTimeMillis() - this.mDownMillis > ((long) tapTimeout)) {
                            this.mIsTouched = false;
                        }
                    }
                } else if (action == 1 && this.mIsTouched) {
                    Log.d("WebViewClick", "clicked");
                    float dx2 = Math.abs(x - this.mDownPoint.x);
                    float dy2 = Math.abs(y - this.mDownPoint.y);
                    if (dx2 > ((float) slop) || dy2 > ((float) slop) || System.currentTimeMillis() - this.mDownMillis > ((long) tapTimeout)) {
                        this.mIsTouched = false;
                    } else if (this.mOnClickListener != null) {
                        this.mOnClickListener.onClick(this.mWebView);
                    }
                }
            }
            return false;
        }
    }
}
