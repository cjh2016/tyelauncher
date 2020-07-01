package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PointerIconCompat;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.common.util.UriUtil;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.response.GetVercodeResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.ui.regist.RegistActivity;
import com.toycloud.launcher.util.PasswordUtil;
import com.toycloud.launcher.util.ToastUtils;
import com.toycloud.launcher.view.DialogUtil;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.retrofit.ApiException;
import framework.hz.salmon.retrofit.BaseResponse;
import framework.hz.salmon.retrofit.BaseSubscriber;
import framework.hz.salmon.util.NetworkUtil;
import framework.hz.salmon.view.AutoHiddenHintEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends BaseFragmentActivity {
    @BindView(2131689745)
    TextView getVercode;
    private boolean isShowPassword = false;
    private boolean isShowPassword1 = false;
    @BindView(2131689685)
    ImageView ivClearpassword;
    @BindView(2131689690)
    ImageView ivClearpassword2;
    @BindView(2131689684)
    ImageView ivShowclearpassword;
    @BindView(2131689689)
    ImageView ivShowclearpassword2;
    @BindView(2131689748)
    LinearLayout linearPassword;
    @BindView(2131689749)
    LinearLayout linearPasswordAgain;
    @BindView(2131689683)
    AutoHiddenHintEditText password;
    @BindView(2131689750)
    AutoHiddenHintEditText passwordAgain;
    @BindView(2131689743)
    RelativeLayout relPhone;
    @BindView(2131689751)
    Button resetPass;
    int timeCount = 60;
    @BindView(2131689686)
    TextView tvPasswodFormat;
    @BindView(2131689691)
    TextView tvPasswodFormatAgain;
    @BindView(2131689747)
    TextView tvPhoneFormat;
    @BindView(2131689746)
    AutoHiddenHintEditText username;
    @BindView(2131689678)
    AutoHiddenHintEditText verCode;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
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
        this.username.addTextChangedListener(new MyTextWatchListener(0));
        this.password.addTextChangedListener(new MyTextWatchListener(1));
        this.passwordAgain.addTextChangedListener(new MyTextWatchListener(2));
        this.verCode.addTextChangedListener(new MyTextWatchListener(3));
        this.username.setOnFocusChangeListener(new MyFocusChange(0, this.username, this.relPhone, this.tvPhoneFormat));
        this.password.setOnFocusChangeListener(new MyFocusChange(1, this.password, this.linearPassword, this.tvPasswodFormat));
        this.passwordAgain.setOnFocusChangeListener(new MyFocusChange(1, this.passwordAgain, this.linearPasswordAgain, this.tvPasswodFormatAgain));
    }

    /* access modifiers changed from: private */
    public void restButtonStyle() {
        int length1 = this.username.getText().length();
        int length2 = this.password.getText().length();
        int length3 = this.verCode.getText().length();
        int length4 = this.passwordAgain.getText().length();
        String phoneString = this.username.getText().toString();
        if (length1 == 11) {
            if (!isChinaPhoneLegal(phoneString)) {
                this.relPhone.setBackgroundResource(R.drawable.edit_bg_red);
                this.tvPhoneFormat.setVisibility(0);
            } else {
                this.relPhone.setBackgroundResource(R.drawable.edit_bg_press);
                this.tvPhoneFormat.setVisibility(8);
            }
        }
        if (length1 <= 0 || length2 <= 0 || length3 <= 0 || length4 <= 0) {
            this.resetPass.setEnabled(false);
            this.resetPass.setBackgroundResource(R.drawable.loginbutton_circle_grey);
            return;
        }
        this.resetPass.setEnabled(true);
        this.resetPass.setBackgroundResource(R.drawable.login_circle_blue);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case PointerIconCompat.TYPE_CONTEXT_MENU:
                if (this.timeCount == 0) {
                    this.getVercode.setText("重新获取");
                    this.getVercode.setTextColor(-16740866);
                    this.getVercode.setBackgroundResource(R.drawable.shape_get_ver_code_enable);
                    return false;
                }
                this.getVercode.setText(this.timeCount + "S");
                this.getVercode.setTextColor(-3355444);
                this.getVercode.setBackgroundResource(R.drawable.shape_get_ver_code_unenable);
                this.timeCount--;
                this.mHandler.sendEmptyMessageDelayed(PointerIconCompat.TYPE_CONTEXT_MENU, 1000);
                return false;
            default:
                return false;
        }
    }

    public void onClick(View view) {
    }

    public boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        return Pattern.compile("^1[3-9]\\d{9}$").matcher(str).matches();
    }

    @OnClick({2131689745, 2131689751, 2131689742, 2131689684, 2131689685, 2131689689, 2131689690})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_showclearpassword:
                if (!this.isShowPassword) {
                    this.isShowPassword = true;
                    this.ivShowclearpassword.setImageResource(R.drawable.but_open);
                    this.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    this.password.setSelection(this.password.getText().toString().length());
                    return;
                }
                this.isShowPassword = false;
                this.ivShowclearpassword.setImageResource(R.drawable.but_close);
                this.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.password.setSelection(this.password.getText().toString().length());
                return;
            case R.id.iv_clearpassword:
                this.password.setText("");
                this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                this.tvPasswodFormat.setVisibility(8);
                return;
            case R.id.iv_showclearpassword_2:
                if (!this.isShowPassword1) {
                    this.isShowPassword1 = true;
                    this.ivShowclearpassword2.setImageResource(R.drawable.but_open);
                    this.passwordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    this.passwordAgain.setSelection(this.passwordAgain.getText().toString().length());
                    return;
                }
                this.isShowPassword1 = false;
                this.ivShowclearpassword2.setImageResource(R.drawable.but_close);
                this.passwordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.passwordAgain.setSelection(this.passwordAgain.getText().toString().length());
                return;
            case R.id.iv_clearpassword_2:
                this.passwordAgain.setText("");
                this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                this.tvPasswodFormat.setVisibility(8);
                return;
            case R.id.iv_back_forget:
                finish();
                return;
            case R.id.get_vercode:
                if (TextUtils.isEmpty(this.username.getText().toString())) {
                    Toast.makeText(this, "请输入手机号", 0).show();
                    return;
                } else if (!isChinaPhoneLegal(this.username.getText().toString())) {
                    Toast.makeText(this, "手机号格式有误", 0).show();
                    return;
                } else if (!this.getVercode.getText().toString().contains("S")) {
                    getVerCode();
                    return;
                } else {
                    return;
                }
            case R.id.reset_pass:
                String password_str = this.password.getText().toString();
                int length1 = password_str.length();
                if (TextUtils.isEmpty(password_str)) {
                    this.password.setEnabled(false);
                    this.password.setBackgroundResource(R.drawable.loginbutton_circle_grey);
                } else {
                    boolean isAllNumber = PasswordUtil.isPasswordAllNumber(password_str);
                    boolean isAllLetter = PasswordUtil.isPasswordAllLetter(password_str);
                    if (length1 < 6) {
                        this.tvPasswodFormat.setVisibility(0);
                        this.linearPassword.setBackgroundResource(R.drawable.edit_bg_red);
                        this.tvPasswodFormat.setText("密码格式有错误：密码长度不足6位");
                        return;
                    } else if (isAllNumber) {
                        this.linearPassword.setBackgroundResource(R.drawable.edit_bg_red);
                        this.tvPasswodFormat.setVisibility(0);
                        this.tvPasswodFormat.setText("密码格式有错误：不能只用纯数字");
                        return;
                    } else if (isAllLetter) {
                        this.linearPassword.setBackgroundResource(R.drawable.edit_bg_red);
                        this.tvPasswodFormat.setVisibility(0);
                        this.tvPasswodFormat.setText("密码格式有错误：不能只用纯字母");
                        return;
                    }
                }
                if (TextUtils.isEmpty(this.username.getText().toString())) {
                    Toast.makeText(this, "请输入手机号", 0).show();
                    return;
                } else if (!isChinaPhoneLegal(this.username.getText().toString())) {
                    Toast.makeText(this, "手机号格式有误", 0).show();
                    return;
                } else if (TextUtils.isEmpty(this.verCode.getText().toString())) {
                    Toast.makeText(this, "请输入验证码", 0).show();
                    return;
                } else if (this.password.getText().toString().equals("")) {
                    Toast.makeText(this, "请输入密码", 0).show();
                    return;
                } else if (!rexCheckPassword(this.password.getText().toString())) {
                    Toast.makeText(this, "密码格式错误，请输入6-16位的字母与数字组合", 0).show();
                    return;
                } else if (!this.password.getText().toString().equals(this.passwordAgain.getText().toString())) {
                    this.linearPasswordAgain.setBackgroundResource(R.drawable.edit_bg_red);
                    this.tvPasswodFormatAgain.setVisibility(0);
                    this.tvPasswodFormatAgain.setText("两次输入密码不一致");
                    return;
                } else {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("newpassword", this.password.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showProgressDialog();
                    RequestBody create = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
                    LauncherHttpHelper.getLauncherService().lostPassword(this.password.getText().toString(), this.username.getText().toString(), this.verCode.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<BaseResponse>(getApplicationContext()) {
                        public void onCompleted() {
                            super.onCompleted();
                            ForgetPasswordActivity.this.dismissProgressDialog();
                        }

                        public void onError(Throwable e) {
                            ForgetPasswordActivity.this.dismissProgressDialog();
                            if (!(e instanceof ApiException)) {
                                super.onError(e);
                            } else if (((ApiException) e).mErrorCode == 3003) {
                                ToastUtils.showShort((CharSequence) "验证码输入错误");
                            } else {
                                super.onError(e);
                            }
                        }

                        public void onNext(BaseResponse response) {
                            super.onNext(response);
                            ForgetPasswordActivity.this.dismissProgressDialog();
                            if (response.getStatus() == 0) {
                                Toast.makeText(ForgetPasswordActivity.this, "重置密码成功", 0).show();
                                ForgetPasswordActivity.this.finish();
                            }
                        }
                    });
                    return;
                }
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

    private void getVerCode() {
        if (TextUtils.isEmpty(this.username.getText().toString())) {
            Toast.makeText(this, "请输入手机号", 0).show();
        } else if (!isChinaPhoneLegal(this.username.getText().toString())) {
            Toast.makeText(this, "手机号格式有误", 0).show();
        } else {
            showProgressDialog();
            LauncherHttpHelper.getLauncherService().getVerCode(this.username.getText().toString(), UriUtil.LOCAL_RESOURCE_SCHEME).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetVercodeResponse>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                    ForgetPasswordActivity.this.dismissProgressDialog();
                }

                public void onError(Throwable e) {
                    ForgetPasswordActivity.this.dismissProgressDialog();
                    if (e instanceof ApiException) {
                        int code = ((ApiException) e).mErrorCode;
                        if (code == 2004) {
                            ForgetPasswordActivity.this.showDialog_reg();
                        } else if (code == 3005) {
                            ForgetPasswordActivity.this.timeCount = 60;
                            ForgetPasswordActivity.this.mHandler.sendEmptyMessage(PointerIconCompat.TYPE_CONTEXT_MENU);
                        } else {
                            super.onError(e);
                        }
                    } else if (!NetworkUtil.isNetworkAvailable(ForgetPasswordActivity.this)) {
                        Toast.makeText(ForgetPasswordActivity.this, "网络连接错误，请检查网络设置后重试", 1).show();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "服务器异常稍候重试...", 1).show();
                    }
                }

                public void onNext(GetVercodeResponse response) {
                    super.onNext(response);
                    ForgetPasswordActivity.this.dismissProgressDialog();
                    Toast.makeText(this.mContext, response.getMsg(), 0).show();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void showDialog_reg() {
        DialogUtil.showDilogToRegs_NewStyle(this, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ForgetPasswordActivity.this.startActivityForResult(new Intent(ForgetPasswordActivity.this.getApplicationContext(), RegistActivity.class), PointerIconCompat.TYPE_CONTEXT_MENU);
            }
        });
    }

    class MyTextWatchListener implements TextWatcher {
        int type = 0;

        public MyTextWatchListener(int i) {
            this.type = i;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            Log.e("afterTextChanged", "-----");
            ForgetPasswordActivity.this.restButtonStyle();
            if (this.type == 1) {
                PasswordUtil.checkPassword(ForgetPasswordActivity.this.password, ForgetPasswordActivity.this.linearPassword, ForgetPasswordActivity.this.tvPasswodFormat);
            }
            if (this.type == 2) {
                PasswordUtil.checkPassword(ForgetPasswordActivity.this.passwordAgain, ForgetPasswordActivity.this.linearPasswordAgain, ForgetPasswordActivity.this.tvPasswodFormatAgain);
            }
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

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (this.type) {
                    case 0:
                        if (!hasFocus && !TextUtils.isEmpty(ForgetPasswordActivity.this.username.getText().toString())) {
                            if (!ForgetPasswordActivity.this.isChinaPhoneLegal(ForgetPasswordActivity.this.username.getText().toString())) {
                                ForgetPasswordActivity.this.relPhone.setBackgroundResource(R.drawable.edit_bg_red);
                                ForgetPasswordActivity.this.tvPhoneFormat.setVisibility(0);
                                return;
                            }
                            ForgetPasswordActivity.this.relPhone.setBackgroundResource(R.drawable.edit_bg_press);
                            ForgetPasswordActivity.this.tvPhoneFormat.setVisibility(8);
                            return;
                        }
                        return;
                    case 1:
                        if (!hasFocus) {
                            String password_str = this.editText.getText().toString();
                            int length1 = password_str.length();
                            if (TextUtils.isEmpty(password_str)) {
                                ForgetPasswordActivity.this.resetPass.setEnabled(false);
                                ForgetPasswordActivity.this.resetPass.setBackgroundResource(R.drawable.loginbutton_circle_grey);
                                return;
                            }
                            boolean isAllNumber = PasswordUtil.isPasswordAllNumber(password_str);
                            boolean isAllLetter = PasswordUtil.isPasswordAllLetter(password_str);
                            if (length1 < 6) {
                                this.tv_hint.setVisibility(0);
                                this.viewGroup.setBackgroundResource(R.drawable.edit_bg_red);
                                this.tv_hint.setText("密码格式有错误：密码长度不足6位");
                                return;
                            } else if (isAllNumber) {
                                this.viewGroup.setBackgroundResource(R.drawable.edit_bg_red);
                                this.tv_hint.setVisibility(0);
                                this.tv_hint.setText("密码格式有错误：不能只用纯数字");
                                return;
                            } else if (isAllLetter) {
                                this.viewGroup.setBackgroundResource(R.drawable.edit_bg_red);
                                this.tv_hint.setVisibility(0);
                                this.tv_hint.setText("密码格式有错误：不能只用纯字母");
                                return;
                            } else {
                                ForgetPasswordActivity.this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                                this.tv_hint.setVisibility(8);
                                return;
                            }
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }
}
