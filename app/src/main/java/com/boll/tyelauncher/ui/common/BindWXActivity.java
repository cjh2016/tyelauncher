package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PointerIconCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.iflytek.common.util.data.IniUtils;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.iflytek.easytrans.dependency.logagent.LogAgent;
import com.iflytek.easytrans.watchcore.deviceinfo.DeviceIdentifierUtils;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.exception.LoadRQImageException;
import com.toycloud.launcher.ui.usercenter.UserCenterActivity;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.ToastUtils;
import com.toycloud.launcher.view.DialogUtil;
import com.toycloud.launcher.view.MyDialog_NewStly;
import framework.hz.salmon.base.BaseActivity;

public class BindWXActivity extends BaseActivity {
    public static final int BIND_WX_ACT_START_FROM_REGISTER = 1;
    public static final int BIND_WX_ACT_START_FROM_USER_CENTER = 0;
    public static final String EXTRA_BIND_WX_ACT_START_FROM = "extra_bind_wx_act_start_from";
    private static final int MAX_TIME_BINDING_COUNT_DOWN = 5;
    private static final int MSG_WHAT_BINDING_COUNT_DOWN = 101;
    private static final String TAG = "BindWXActivity";
    private int mActStartFrom = 0;
    @BindView(2131689661)
    View mBackIV;
    @BindView(2131689670)
    Button mBindOkBtn;
    @BindView(2131689669)
    ImageView mBindQRCodeIV;
    private int mBindingCountDownTime = 5;
    /* access modifiers changed from: private */
    public MyDialog_NewStly mHintBindDlg;
    /* access modifiers changed from: private */
    public boolean mIsLoadRQCodeImageError = false;
    @BindView(2131689804)
    TextView mTitleTV;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_newwx_layout);
        ButterKnife.bind((Activity) this);
        this.mActStartFrom = getIntent().getIntExtra(EXTRA_BIND_WX_ACT_START_FROM, 0);
        this.mTitleTV.setText(R.string.parent_manager_new);
        this.mBackIV.setOnClickListener(this);
        this.mBindOkBtn.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
            if (this.mActStartFrom == 1) {
                this.mBindOkBtn.setClickable(false);
                this.mBindOkBtn.setBackgroundResource(R.drawable.bind_ok_prepare);
                this.mBindOkBtn.setText(getString(R.string.bind_wx_already_with_time_count_down, new Object[]{Integer.valueOf(this.mBindingCountDownTime)}));
                this.mBindOkBtn.setTextColor(getColor(R.color.bind_prepare));
                this.mHandler.sendEmptyMessageDelayed(101, 1500);
            } else {
                this.mBindOkBtn.setClickable(true);
                this.mBindOkBtn.setText(R.string.bind_wx_already);
                this.mBindOkBtn.setTextColor(-1);
                this.mBindOkBtn.setBackgroundResource(R.drawable.bind_ok_success);
            }
        }
        loadRQ();
    }

    public boolean hintBindWx() {
        if (this.mActStartFrom != 1) {
            return false;
        }
        if (this.mHintBindDlg == null) {
            this.mHintBindDlg = new DialogUtil.MyDialogBuilder().hintContent(getString(R.string.bind_wx_hint_txt)).positiveTxt(getString(R.string.continue_bind_wx)).otherTxt(getString(R.string.cancel)).positiveListener(new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (BindWXActivity.this.mHintBindDlg != null) {
                        BindWXActivity.this.mHintBindDlg.dissMissDialog();
                    }
                }
            }).otherListener(new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    BindWXActivity.this.finish();
                }
            }).build(this);
        }
        if (this.mHintBindDlg.isShowing()) {
            return true;
        }
        this.mHintBindDlg.show();
        return true;
    }

    @OnClick({2131689669})
    public void onClickRQ() {
        if (this.mIsLoadRQCodeImageError) {
            loadRQ();
        }
    }

    private void loadRQ() {
        String sn = DeviceIdentifierUtils.getSn(this);
        if (TextUtils.isEmpty(sn)) {
            this.mIsLoadRQCodeImageError = true;
            User userInfo = SharepreferenceUtil.getSharepferenceInstance(this).getUserInfo();
            String userId = "";
            if (userInfo != null) {
                userId = userInfo.getUserid();
            }
            LogAgent.onError(new LoadRQImageException("load rqimage error: cannot get sn, userId=[" + userId + "], deviceId=[" + DeviceIdentifierUtils.getDeviceId(this) + IniUtils.PROPERTY_END_TAG));
            return;
        }
        this.mIsLoadRQCodeImageError = false;
        String bindQRCodeUrl = "https://k12-api.openspeech.cn/user/auth/qr.png?sn=" + sn;
        LogUtils.d(TAG, "onCreate: bindQRCodeUrl:" + bindQRCodeUrl);
        doLoadRQ(sn, bindQRCodeUrl, this.mBindQRCodeIV, R.drawable.def_qr_shape);
    }

    public void doLoadRQ(final String sn, String path, final ImageView imageView, int defaultDefault) {
        Glide.with((FragmentActivity) this).load(path).placeholder(defaultDefault).error(defaultDefault).skipMemoryCache(true).into(new ImageViewTarget<GlideDrawable>(imageView) {
            /* access modifiers changed from: protected */
            public void setResource(GlideDrawable resource) {
                imageView.setImageDrawable(resource);
            }

            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Logger.e(BindWXActivity.TAG, "load rq error", e);
                boolean unused = BindWXActivity.this.mIsLoadRQCodeImageError = true;
                LogAgent.onError(new LoadRQImageException("load rqimage error, sn=" + sn, e));
            }

            public void setRequest(Request request) {
                imageView.setTag(R.id.adapter_item_tag_key, request);
            }

            public Request getRequest() {
                return (Request) imageView.getTag(R.id.adapter_item_tag_key);
            }
        });
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 101:
                int i = this.mBindingCountDownTime - 1;
                this.mBindingCountDownTime = i;
                if (i > 0) {
                    this.mBindOkBtn.setText(getString(R.string.bind_wx_already_with_time_count_down, new Object[]{Integer.valueOf(this.mBindingCountDownTime)}));
                    this.mHandler.sendEmptyMessageDelayed(101, 1000);
                    break;
                } else {
                    this.mBindOkBtn.setText(R.string.bind_wx_already);
                    this.mBindOkBtn.setClickable(true);
                    this.mBindOkBtn.setTextColor(-1);
                    this.mBindOkBtn.setBackgroundResource(R.drawable.bind_ok_success);
                    break;
                }
        }
        return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                return;
            case R.id.bind_wx_ok_btn:
                if (this.mActStartFrom == 0) {
                    finish();
                    return;
                } else if (this.mBindingCountDownTime <= 0) {
                    ToastUtils.showShort((int) R.string.bind_wx_success);
                    Intent intent2 = new Intent(this, UserCenterActivity.class);
                    intent2.putExtra("whichposition", 1);
                    startActivityForResult(intent2, PointerIconCompat.TYPE_CONTEXT_MENU);
                    finish();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onBackPressed() {
        if (!hintBindWx()) {
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        super.onDestroy();
    }
}
