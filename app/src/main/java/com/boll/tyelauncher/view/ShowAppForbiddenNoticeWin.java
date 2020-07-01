package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.toycloud.launcher.R;
import java.util.Timer;
import java.util.TimerTask;

public class ShowAppForbiddenNoticeWin extends PopupWindow implements View.OnClickListener, Handler.Callback {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public String BASE_URL = "https://k12-api.openspeech.cn/";
    public View contentView;
    /* access modifiers changed from: private */
    public int count = 5;
    View footerView;
    private LinearLayout linearLayout_1;
    private LinearLayout linearLayout_2;
    private Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(this);
    private String mSnId;
    public View pickerContainerV;
    private Timer timer;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_eria2;
    /* access modifiers changed from: private */
    public int type = 1;

    static /* synthetic */ int access$210(ShowAppForbiddenNoticeWin x0) {
        int i = x0.count;
        x0.count = i - 1;
        return i;
    }

    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            this.tv_1.setText(this.count + "");
            return false;
        } else if (msg.what == 2) {
            this.tv_2.setText(this.count + "");
            return false;
        } else if (msg.what != 3) {
            return false;
        } else {
            dismissPopWin();
            return false;
        }
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public String mSnId;
        private int type = 1;

        public Builder(Context context2, String SnId, int type2) {
            this.context = context2;
            this.mSnId = SnId;
            this.type = type2;
        }

        public ShowAppForbiddenNoticeWin build() {
            return new ShowAppForbiddenNoticeWin(this, this.type);
        }
    }

    public ShowAppForbiddenNoticeWin(Builder builder, int type2) {
        this.mContext = builder.context;
        this.mSnId = builder.mSnId;
        this.type = type2;
        this.timer = new Timer();
        this.timer.schedule(new MyTimeTask(), 1000, 1000);
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_show_forbidden_notice, (ViewGroup) null);
        this.contentView.setOnClickListener(this);
        this.linearLayout_1 = (LinearLayout) this.contentView.findViewById(R.id.linear_to_forbidden1);
        this.linearLayout_2 = (LinearLayout) this.contentView.findViewById(R.id.linear_to_forbidden2);
        this.tv_1 = (TextView) this.contentView.findViewById(R.id.tv1_forbidden_2);
        this.tv_2 = (TextView) this.contentView.findViewById(R.id.tv2_forbidden_2);
        if (this.type == 1) {
            this.linearLayout_1.setVisibility(0);
            this.linearLayout_2.setVisibility(8);
        } else if (this.type == 2) {
            this.linearLayout_1.setVisibility(8);
            this.linearLayout_2.setVisibility(0);
        }
        setTouchable(true);
        setFocusable(true);
        setClippingEnabled(false);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(this.contentView);
        setWidth(-1);
        setHeight(-1);
    }

    public void showPopWin(Context context) {
        if (context != null && (context instanceof Activity)) {
            showAtLocation(((Activity) context).getWindow().getDecorView(), 17, 0, 0);
        }
    }

    public void dismissPopWin() {
        if (this.timer != null) {
            try {
                this.timer.cancel();
                this.timer = null;
            } catch (Exception e) {
            }
        }
        dismiss();
    }

    public void onClick(View v) {
        if (v == this.contentView) {
            dismissPopWin();
        }
    }

    public class MyTimeTask extends TimerTask {
        public MyTimeTask() {
        }

        public void run() {
            if (ShowAppForbiddenNoticeWin.this.count > 1) {
                ShowAppForbiddenNoticeWin.access$210(ShowAppForbiddenNoticeWin.this);
                if (ShowAppForbiddenNoticeWin.this.type == 1) {
                    ShowAppForbiddenNoticeWin.this.mHandler.sendEmptyMessage(1);
                } else if (ShowAppForbiddenNoticeWin.this.type == 2) {
                    ShowAppForbiddenNoticeWin.this.mHandler.sendEmptyMessage(2);
                }
            } else {
                int unused = ShowAppForbiddenNoticeWin.this.count = 0;
                ShowAppForbiddenNoticeWin.this.mHandler.sendEmptyMessage(3);
            }
        }
    }
}
