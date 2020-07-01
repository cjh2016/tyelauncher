package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.iflytek.stats.StatsCode;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.response.ChangePasswordResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.api.service.bean.ChangePasswordRequest;
import com.toycloud.launcher.util.CustomToast;
import com.toycloud.launcher.util.PasswordUtil;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.retrofit.ApiException;
import framework.hz.salmon.retrofit.BaseSubscriber;
import framework.hz.salmon.view.AutoHiddenHintEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;

public class ChangePasswordActivity extends BaseFragmentActivity {
    private static final String TAG = "ChangePasswordActivity";
    @BindView(2131689693)
    Button changePass;
    @BindView(2131689688)
    AutoHiddenHintEditText confirmPassword;
    private boolean isShowPassword = false;
    private boolean isShowPassword1 = false;
    private boolean isShowPassword2 = false;
    @BindView(2131689685)
    ImageView ivClearpassword;
    @BindView(2131689690)
    ImageView ivClearpassword2;
    @BindView(2131689680)
    ImageView ivClearpasswordOld;
    @BindView(2131689684)
    ImageView ivShowclearpassword;
    @BindView(2131689689)
    ImageView ivShowclearpassword2;
    @BindView(2131689679)
    ImageView ivShowclearpasswordOld;
    @BindView(2131689661)
    ImageView iv_back;
    @BindView(2131689682)
    LinearLayout linearNewpassword;
    @BindView(2131689687)
    LinearLayout linearNewpasswordAgain;
    @BindView(2131689677)
    LinearLayout linearOldpassword;
    @BindView(2131689678)
    AutoHiddenHintEditText oldPass;
    @BindView(2131689683)
    AutoHiddenHintEditText password;
    @BindView(2131689681)
    TextView tvOldpasswordFormat;
    @BindView(2131689686)
    TextView tvPasswodFormat;
    @BindView(2131689691)
    TextView tvPasswodFormatAgain;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind((Activity) this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        initListener();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }

    private void initListener() {
        this.oldPass.addTextChangedListener(new MyTextWatchListener(0));
        this.password.addTextChangedListener(new MyTextWatchListener(1));
        this.confirmPassword.addTextChangedListener(new MyTextWatchListener(2));
        this.oldPass.setOnFocusChangeListener(new MyFocusChange(0, this.oldPass, this.linearOldpassword, this.tvOldpasswordFormat));
        this.password.setOnFocusChangeListener(new MyFocusChange(1, this.password, this.linearNewpassword, this.tvPasswodFormat));
        this.confirmPassword.setOnFocusChangeListener(new MyFocusChange(2, this.confirmPassword, this.linearNewpasswordAgain, this.tvPasswodFormatAgain));
    }

    public boolean handleMessage(Message message) {
        return true;
    }

    public void onClick(View view) {
    }

    @OnClick({2131689693, 2131689661, 2131689680, 2131689679, 2131689685, 2131689684, 2131689690, 2131689689})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                return;
            case R.id.iv_showclearpassword_old:
                if (!this.isShowPassword) {
                    this.isShowPassword = true;
                    this.ivShowclearpasswordOld.setImageResource(R.drawable.but_open);
                    this.oldPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    this.oldPass.setSelection(this.oldPass.getText().toString().length());
                    return;
                }
                this.isShowPassword = false;
                this.ivShowclearpasswordOld.setImageResource(R.drawable.but_close);
                this.oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.oldPass.setSelection(this.oldPass.getText().toString().length());
                return;
            case R.id.iv_clearpassword_old:
                this.oldPass.setText("");
                this.linearOldpassword.setBackgroundResource(R.drawable.edit_bg_press);
                this.tvOldpasswordFormat.setVisibility(8);
                return;
            case R.id.iv_showclearpassword:
                if (!this.isShowPassword1) {
                    this.isShowPassword1 = true;
                    this.ivShowclearpassword.setImageResource(R.drawable.but_open);
                    this.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    this.password.setSelection(this.password.getText().toString().length());
                    return;
                }
                this.isShowPassword1 = false;
                this.ivShowclearpassword.setImageResource(R.drawable.but_close);
                this.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.password.setSelection(this.password.getText().toString().length());
                return;
            case R.id.iv_clearpassword:
                this.password.setText("");
                this.linearNewpassword.setBackgroundResource(R.drawable.edit_bg_press);
                this.tvPasswodFormat.setVisibility(8);
                return;
            case R.id.iv_showclearpassword_2:
                if (!this.isShowPassword2) {
                    this.isShowPassword2 = true;
                    this.ivShowclearpassword2.setImageResource(R.drawable.but_open);
                    this.confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    this.confirmPassword.setSelection(this.confirmPassword.getText().toString().length());
                    return;
                }
                this.isShowPassword2 = false;
                this.ivShowclearpassword2.setImageResource(R.drawable.but_close);
                this.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.confirmPassword.setSelection(this.confirmPassword.getText().toString().length());
                return;
            case R.id.iv_clearpassword_2:
                this.confirmPassword.setText("");
                this.linearNewpasswordAgain.setBackgroundResource(R.drawable.edit_bg_press);
                this.tvPasswodFormat.setVisibility(8);
                return;
            case R.id.change_pass:
                Log.d(TAG, "埋点:FT12006");
                LogAgentHelper.onOPLogEvent(StatsCode.CLICK_XGMM_SAVE_BTN, (Map<String, String>) null);
                submit();
                return;
            default:
                return;
        }
    }

    public static boolean rexCheckPassword(String input) {
        if (input.length() < 6 || input.length() > 16) {
            return false;
        }
        return true;
    }

    private void submit() {
        if (this.oldPass.getText().toString().equals("")) {
            Toast.makeText(this, "请输入原密码", 0).show();
        } else if (this.password.getText().toString().equals("")) {
            Toast.makeText(this, "请输入新密码", 0).show();
        } else if (this.confirmPassword.getText().toString().equals("")) {
            Toast.makeText(this, "请再次输入新密码", 0).show();
        } else if (!rexCheckPassword(this.password.getText().toString())) {
            Toast.makeText(this, "新密码为6-16位的字母与数字组合", 0).show();
        } else if (!this.password.getText().toString().equals(this.confirmPassword.getText().toString())) {
            this.linearNewpasswordAgain.setBackgroundResource(R.drawable.edit_bg_red);
            this.tvPasswodFormatAgain.setVisibility(0);
            this.tvPasswodFormatAgain.setText("两次输入的新密码不一致");
        } else {
            showProgressDialog();
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.oldpassword = this.oldPass.getText().toString();
            request.newpassword = this.password.getText().toString();
            LauncherHttpHelper.getLauncherService().changePassword(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ChangePasswordResponse>(getApplicationContext()) {
                public void onCompleted() {
                    ChangePasswordActivity.this.dismissProgressDialog();
                    super.onCompleted();
                }

                public void onError(Throwable e) {
                    ChangePasswordActivity.this.dismissProgressDialog();
                    if (!(e instanceof ApiException)) {
                        super.onError(e);
                    } else if (((ApiException) e).mErrorCode == 3006) {
                        CustomToast.showToast((Context) ChangePasswordActivity.this, "原密码输入错误，请重试");
                    } else {
                        super.onError(e);
                    }
                }

                public void onNext(ChangePasswordResponse response) {
                    super.onNext(response);
                    ChangePasswordActivity.this.dismissProgressDialog();
                    Toast.makeText(this.mContext, "修改成功", 0).show();
                    ChangePasswordActivity.this.finish();
                }
            });
        }
    }

    class MyTextWatchListener implements TextWatcher {
        int type = 1;

        MyTextWatchListener(int type2) {
            this.type = type2;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            ChangePasswordActivity.this.restButtonStyle(this.type);
            switch (this.type) {
                case 0:
                    PasswordUtil.checkPassword(ChangePasswordActivity.this.oldPass, ChangePasswordActivity.this.linearOldpassword, ChangePasswordActivity.this.tvOldpasswordFormat);
                    return;
                case 1:
                    PasswordUtil.checkPassword(ChangePasswordActivity.this.password, ChangePasswordActivity.this.linearNewpassword, ChangePasswordActivity.this.tvPasswodFormat);
                    return;
                case 2:
                    PasswordUtil.checkPassword(ChangePasswordActivity.this.confirmPassword, ChangePasswordActivity.this.linearNewpasswordAgain, ChangePasswordActivity.this.tvPasswodFormatAgain);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void restButtonStyle(int type) {
        int length1 = this.oldPass.getText().length();
        int length2 = this.password.getText().length();
        int length3 = this.confirmPassword.getText().length();
        String string_oldPass = this.oldPass.getText().toString();
        String string_password = this.password.getText().toString();
        String obj = this.confirmPassword.getText().toString();
        if (length1 <= 0 || length2 <= 0 || length3 <= 0) {
            this.changePass.setEnabled(false);
            this.changePass.setBackgroundResource(R.drawable.loginbutton_circle_grey);
        } else if (!string_oldPass.equals(string_password)) {
            this.changePass.setEnabled(true);
            this.changePass.setBackgroundResource(R.drawable.login_circle_blue);
        } else {
            this.changePass.setEnabled(false);
            this.changePass.setBackgroundResource(R.drawable.loginbutton_circle_grey);
        }
    }

    class MyFocusChange implements View.OnFocusChangeListener {
        AutoHiddenHintEditText editText;
        TextView tv_hint;
        int type = 0;
        ViewGroup viewGroup;

        MyFocusChange(int type2, AutoHiddenHintEditText editText2, ViewGroup viewGroup2, TextView tv_hint2) {
            this.type = type2;
            this.editText = editText2;
            this.viewGroup = viewGroup2;
            this.tv_hint = tv_hint2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:30:0x00c7, code lost:
            if (r5.equals(r4) != false) goto L_0x00c9;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onFocusChange(android.view.View r12, boolean r13) {
            /*
                r11 = this;
                r10 = 2130837742(0x7f0200ee, float:1.7280447E38)
                r9 = 0
                if (r13 != 0) goto L_0x000b
                int r7 = r11.type
                switch(r7) {
                    case 0: goto L_0x000c;
                    case 1: goto L_0x000c;
                    case 2: goto L_0x000c;
                    default: goto L_0x000b;
                }
            L_0x000b:
                return
            L_0x000c:
                if (r13 != 0) goto L_0x000b
                framework.hz.salmon.view.AutoHiddenHintEditText r7 = r11.editText
                android.text.Editable r7 = r7.getText()
                java.lang.String r3 = r7.toString()
                int r2 = r3.length()
                boolean r7 = android.text.TextUtils.isEmpty(r3)
                if (r7 == 0) goto L_0x0034
                com.toycloud.launcher.ui.common.ChangePasswordActivity r7 = com.toycloud.launcher.ui.common.ChangePasswordActivity.this
                android.widget.Button r7 = r7.changePass
                r7.setEnabled(r9)
                com.toycloud.launcher.ui.common.ChangePasswordActivity r7 = com.toycloud.launcher.ui.common.ChangePasswordActivity.this
                android.widget.Button r7 = r7.changePass
                r8 = 2130837890(0x7f020182, float:1.7280747E38)
                r7.setBackgroundResource(r8)
                goto L_0x000b
            L_0x0034:
                boolean r1 = com.toycloud.launcher.util.PasswordUtil.isPasswordAllNumber(r3)
                boolean r0 = com.toycloud.launcher.util.PasswordUtil.isPasswordAllLetter(r3)
                r7 = 6
                if (r2 >= r7) goto L_0x0051
                android.widget.TextView r7 = r11.tv_hint
                r7.setVisibility(r9)
                android.view.ViewGroup r7 = r11.viewGroup
                r7.setBackgroundResource(r10)
                android.widget.TextView r7 = r11.tv_hint
                java.lang.String r8 = "密码格式有错误：密码长度不足6位"
                r7.setText(r8)
                goto L_0x000b
            L_0x0051:
                if (r1 == 0) goto L_0x0065
                android.view.ViewGroup r7 = r11.viewGroup
                r7.setBackgroundResource(r10)
                android.widget.TextView r7 = r11.tv_hint
                r7.setVisibility(r9)
                android.widget.TextView r7 = r11.tv_hint
                java.lang.String r8 = "密码格式有错误：不能只用纯数字"
                r7.setText(r8)
                goto L_0x000b
            L_0x0065:
                if (r0 == 0) goto L_0x0079
                android.view.ViewGroup r7 = r11.viewGroup
                r7.setBackgroundResource(r10)
                android.widget.TextView r7 = r11.tv_hint
                r7.setVisibility(r9)
                android.widget.TextView r7 = r11.tv_hint
                java.lang.String r8 = "密码格式有错误：不能只用纯字母"
                r7.setText(r8)
                goto L_0x000b
            L_0x0079:
                int r7 = r11.type
                r8 = 1
                if (r7 == r8) goto L_0x0083
                int r7 = r11.type
                r8 = 2
                if (r7 != r8) goto L_0x00dc
            L_0x0083:
                com.toycloud.launcher.ui.common.ChangePasswordActivity r7 = com.toycloud.launcher.ui.common.ChangePasswordActivity.this
                framework.hz.salmon.view.AutoHiddenHintEditText r7 = r7.oldPass
                android.text.Editable r7 = r7.getText()
                java.lang.String r5 = r7.toString()
                com.toycloud.launcher.ui.common.ChangePasswordActivity r7 = com.toycloud.launcher.ui.common.ChangePasswordActivity.this
                framework.hz.salmon.view.AutoHiddenHintEditText r7 = r7.password
                android.text.Editable r7 = r7.getText()
                java.lang.String r6 = r7.toString()
                com.toycloud.launcher.ui.common.ChangePasswordActivity r7 = com.toycloud.launcher.ui.common.ChangePasswordActivity.this
                framework.hz.salmon.view.AutoHiddenHintEditText r7 = r7.confirmPassword
                android.text.Editable r7 = r7.getText()
                java.lang.String r4 = r7.toString()
                boolean r7 = android.text.TextUtils.isEmpty(r5)
                if (r7 != 0) goto L_0x00dc
                boolean r7 = android.text.TextUtils.isEmpty(r6)
                if (r7 == 0) goto L_0x00b5
                java.lang.String r6 = "-1"
            L_0x00b5:
                boolean r7 = r5.equals(r6)
                if (r7 != 0) goto L_0x00c9
                boolean r7 = android.text.TextUtils.isEmpty(r4)
                if (r7 == 0) goto L_0x00c3
                java.lang.String r4 = "-1"
            L_0x00c3:
                boolean r7 = r5.equals(r4)
                if (r7 == 0) goto L_0x00dc
            L_0x00c9:
                android.view.ViewGroup r7 = r11.viewGroup
                r7.setBackgroundResource(r10)
                android.widget.TextView r7 = r11.tv_hint
                r7.setVisibility(r9)
                android.widget.TextView r7 = r11.tv_hint
                java.lang.String r8 = "新密码不能和旧密码相同"
                r7.setText(r8)
                goto L_0x000b
            L_0x00dc:
                android.view.ViewGroup r7 = r11.viewGroup
                r8 = 2130837741(0x7f0200ed, float:1.7280445E38)
                r7.setBackgroundResource(r8)
                android.widget.TextView r7 = r11.tv_hint
                r8 = 8
                r7.setVisibility(r8)
                goto L_0x000b
            */
            throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.ui.common.ChangePasswordActivity.MyFocusChange.onFocusChange(android.view.View, boolean):void");
        }
    }
}