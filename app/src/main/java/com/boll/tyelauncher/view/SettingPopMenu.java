package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.iflytek.stats.StatsCode;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.helper.FeedbackHelper;
import com.toycloud.launcher.ui.common.ChangePasswordActivity;
import com.toycloud.launcher.ui.login.LoginActivity;
import com.toycloud.launcher.util.PkgUtil;
import java.util.Map;

public class SettingPopMenu extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "SettingPopMenu";
    RelativeLayout changePass;
    LinearLayout containerPicker;
    private View contentView;
    RelativeLayout feedback;
    RelativeLayout logout;
    /* access modifiers changed from: private */
    public Activity mContext;
    private ImageView mImgDot;
    private int mWindowWidth;

    public static class Builder {
        /* access modifiers changed from: private */
        public Activity context;
        private int sex;

        public Builder(Activity context2) {
            this.context = context2;
        }

        public SettingPopMenu build() {
            return new SettingPopMenu(this);
        }
    }

    public SettingPopMenu(Builder builder) {
        this.mContext = builder.context;
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_setting_pop, (ViewGroup) null);
        this.logout = (RelativeLayout) this.contentView.findViewById(R.id.logout_layout);
        this.changePass = (RelativeLayout) this.contentView.findViewById(R.id.changePass_layout);
        this.mImgDot = (ImageView) this.contentView.findViewById(R.id.img_dot);
        refreshDot();
        this.feedback = (RelativeLayout) this.contentView.findViewById(R.id.feeback_layout);
        this.containerPicker = (LinearLayout) this.contentView.findViewById(R.id.container_picker);
        this.changePass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingPopMenu.this.dismissPopWin();
                SettingPopMenu.this.mContext.startActivity(new Intent(SettingPopMenu.this.mContext, ChangePasswordActivity.class));
            }
        });
        this.feedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingPopMenu.this.dismissPopWin();
                if (PkgUtil.checkApkIsInstall(SettingPopMenu.this.mContext, GlobalVariable.FEEDBACKPACKAGENAME)) {
                    PkgUtil.startAppByPackName(SettingPopMenu.this.mContext, GlobalVariable.FEEDBACKPACKAGENAME);
                } else {
                    Toast.makeText(SettingPopMenu.this.mContext, "未安装问题反馈应用", 0).show();
                }
                Log.d(SettingPopMenu.TAG, "埋点:FT07004");
                LogAgentHelper.onOPLogEvent(StatsCode.CLICK_USER_CENTER_MORE_WTYJY, (Map<String, String>) null);
            }
        });
        this.logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingPopMenu.this.dismissPopWin();
                Intent intent = new Intent(SettingPopMenu.this.mContext, LoginActivity.class);
                intent.addFlags(268435456);
                SettingPopMenu.this.mContext.startActivity(intent);
            }
        });
        setTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(this.contentView);
        setWidth(280);
        setHeight(-2);
        this.contentView.measure(0, 0);
        this.mWindowWidth = this.contentView.getMeasuredHeight();
    }

    public void refreshDot() {
        if (this.mImgDot != null) {
            int msgCount = FeedbackHelper.getMessage(this.mContext);
            if (msgCount > 0) {
                if (this.mImgDot.getVisibility() == 8) {
                    this.mImgDot.setVisibility(0);
                }
            } else if (msgCount == 0 && this.mImgDot.getVisibility() == 0) {
                this.mImgDot.setVisibility(8);
            }
        }
    }

    public void showPopWin(View anchor) {
        TranslateAnimation trans = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
        showAsDropDown(anchor, (-this.mWindowWidth) + (anchor.getWidth() / 2), 0, 3);
        trans.setDuration(250);
        trans.setInterpolator(new AccelerateDecelerateInterpolator());
        this.containerPicker.startAnimation(trans);
    }

    public void dismissPopWin() {
        TranslateAnimation trans = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        trans.setDuration(250);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SettingPopMenu.this.dismiss();
            }
        });
        this.containerPicker.startAnimation(trans);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.female_layout:
                dismissPopWin();
                return;
            default:
                return;
        }
    }
}
