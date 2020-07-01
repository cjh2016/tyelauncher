package com.boll.tyelauncher.ui.regist;

package com.toycloud.launcher.ui.regist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.GetVercodeResponse;
import com.toycloud.launcher.api.response.StudentRegistResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.util.CustomToast;
import com.toycloud.launcher.util.PadInfoUtil;
import com.toycloud.launcher.util.PasswordUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.ToastUtils;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.retrofit.ApiException;
import framework.hz.salmon.retrofit.BaseSubscriber;
import framework.hz.salmon.view.AutoHiddenHintEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Type;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistActivity extends BaseFragmentActivity {
    @BindView(2131689745)
    TextView getVercode;
    private boolean isShowPassword = false;
    @BindView(2131689685)
    ImageView ivClearpassword;
    @BindView(2131689684)
    ImageView ivShowclearpassword;
    @BindView(2131689661)
    ImageView iv_back;
    @BindView(2131689748)
    LinearLayout linearPassword;
    long[] mHints = new long[3];
    @BindView(2131689683)
    AutoHiddenHintEditText password;
    @BindView(2131689757)
    Button regist;
    @BindView(2131689743)
    RelativeLayout relPhone;
    /* access modifiers changed from: private */
    public String tel;
    int timeCount = 60;
    @BindView(2131689686)
    TextView tvPasswodFormat;
    @BindView(2131689747)
    TextView tvPhoneFormat;
    @BindView(2131689746)
    AutoHiddenHintEditText username;
    @BindView(2131689678)
    AutoHiddenHintEditText verCode;

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
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

    private void initListener() {
        this.username.addTextChangedListener(new MyTextWatchListener(0));
        this.password.addTextChangedListener(new MyTextWatchListener(1));
        this.verCode.addTextChangedListener(new MyTextWatchListener(2));
        this.username.setOnFocusChangeListener(new MyFocusChange(0));
        this.password.setOnFocusChangeListener(new MyFocusChange(1));
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 1101:
                if (this.timeCount == 0) {
                    this.getVercode.setText("重新获取");
                    this.getVercode.setTextColor(-16740866);
                    return false;
                }
                this.getVercode.setText(this.timeCount + "S");
                this.getVercode.setTextColor(-3355444);
                this.timeCount--;
                this.mHandler.sendEmptyMessageDelayed(1101, 1000);
                return false;
            default:
                return false;
        }
    }

    public void onClick(View view) {
    }

    @OnClick({2131689745, 2131689757, 2131689661, 2131689685, 2131689684, 2131689793})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                return;
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
            case R.id.regist:
                if (TextUtils.isEmpty(this.username.getText().toString())) {
                    Toast.makeText(this, "请输入手机号", 0).show();
                    return;
                } else if (!isChinaPhoneLegal(this.username.getText().toString())) {
                    Toast.makeText(this, "手机号格式有误", 0).show();
                    return;
                } else if (this.tel != null && !this.tel.equals(this.username.getText().toString())) {
                    Toast.makeText(this, "当前手机号与验证码不匹配，请重新获取", 0).show();
                    return;
                } else if (TextUtils.isEmpty(this.verCode.getText().toString())) {
                    Toast.makeText(this, "请输入验证码", 0).show();
                    return;
                } else if (TextUtils.isEmpty(this.password.getText().toString())) {
                    Toast.makeText(this, "请输入密码", 0).show();
                    return;
                } else if (this.password.getText().toString().length() < 6 || this.password.getText().toString().length() > 16) {
                    Toast.makeText(this, "密码长度为6~16位", 0).show();
                    return;
                } else {
                    String password_str = this.password.getText().toString();
                    int length1 = password_str.length();
                    if (TextUtils.isEmpty(password_str)) {
                        this.regist.setEnabled(false);
                        this.regist.setBackgroundResource(R.drawable.loginbutton_circle_grey);
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
                        } else {
                            this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                            this.tvPasswodFormat.setVisibility(8);
                        }
                    }
                    regist();
                    return;
                }
            case R.id.iv_icon_title:
                if (isClickThreeTime()) {
                    Intent intent = new Intent(this, CompleteUserInfoActivity.class);
                    intent.addFlags(268435456);
                    intent.putExtra(StudyCPHelper.COLUMN_NAME_UNIQUE_FLAG, true);
                    setResult(-1);
                    startActivityForResult(intent, 101);
                    GlobalVariable.list_Activity.get(0).finish();
                    finish();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public boolean isClickThreeTime() {
        System.arraycopy(this.mHints, 1, this.mHints, 0, this.mHints.length - 1);
        this.mHints[this.mHints.length - 1] = SystemClock.uptimeMillis();
        if (SystemClock.uptimeMillis() - this.mHints[0] <= 500) {
            return true;
        }
        return false;
    }

    private void regist() {
        showProgressDialog();
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", this.username.getText().toString());
            obj.put("password", this.password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PadInfoUtil padInfoUtil = new PadInfoUtil(this);
        RequestBody create = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        LauncherHttpHelper.getLauncherService().studentRegist(this.username.getText().toString(), this.password.getText().toString(), this.verCode.getText().toString(), padInfoUtil.getSnCode(), padInfoUtil.getIPAddress(), padInfoUtil.getSystemVersionCode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<StudentRegistResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
                RegistActivity.this.dismissProgressDialog();
            }

            public void onError(Throwable e) {
                RegistActivity.this.dismissProgressDialog();
                if (!(e instanceof ApiException)) {
                    super.onError(e);
                } else if (((ApiException) e).mErrorCode == 3003) {
                    ToastUtils.showShort((CharSequence) "验证码输入错误");
                } else {
                    super.onError(e);
                }
            }

            public void onNext(StudentRegistResponse response) {
                super.onNext(response);
                RegistActivity.this.dismissProgressDialog();
                SharepreferenceUtil.getSharepferenceInstance(RegistActivity.this).Logout();
                Toast.makeText(this.mContext, "注册成功", 0).show();
                GlobalVariable.setTOKEN(response.getData().getToken(), true);
                Gson gson = new GsonBuilder().create();
                String userinfo = gson.toJson((Object) response.getData(), (Type) User.class);
                User user = (User) gson.fromJson(userinfo, User.class);
                GlobalVariable.LOGIN_USER = user;
                SharepreferenceUtil.getSharepferenceInstance(RegistActivity.this).setUserInfo(user, userinfo);
                GlobalVariable.setLogin(true, user);
                RegistActivity.this.setResult(-1);
                Intent intent = new Intent(RegistActivity.this.getApplicationContext(), CompleteUserInfoActivity.class);
                intent.addFlags(268435456);
                RegistActivity.this.startActivityForResult(intent, 101);
                GlobalVariable.list_Activity.get(0).finish();
                RegistActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                if (resultCode == -1) {
                    finish();
                    break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        return Pattern.compile("^1[3-9]\\d{9}$").matcher(str).matches();
    }

    private void getVerCode() {
        if (TextUtils.isEmpty(this.username.getText().toString())) {
            Toast.makeText(this, "请输入手机号", 0).show();
        } else if (!isChinaPhoneLegal(this.username.getText().toString())) {
            Toast.makeText(this, "手机号格式有误", 0).show();
        } else {
            this.verCode.requestFocus();
            showProgressDialog();
            LauncherHttpHelper.getLauncherService().getVerCode(this.username.getText().toString(), "reg").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetVercodeResponse>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                    RegistActivity.this.dismissProgressDialog();
                }

                public void onError(Throwable e) {
                    super.onError(e);
                    RegistActivity.this.dismissProgressDialog();
                    try {
                        int code = ((ApiException) e).mErrorCode;
                        if (code == 3005) {
                            RegistActivity.this.timeCount = 60;
                            RegistActivity.this.mHandler.sendEmptyMessage(1101);
                            String unused = RegistActivity.this.tel = RegistActivity.this.username.getText().toString();
                        } else if (code == 2003) {
                            CustomToast.showToast((Context) RegistActivity.this, "该手机号码已注册");
                        }
                    } catch (Exception e2) {
                    }
                }

                public void onNext(GetVercodeResponse response) {
                    super.onNext(response);
                    RegistActivity.this.dismissProgressDialog();
                    if (response != null) {
                        Toast.makeText(this.mContext, response.getMsg(), 0).show();
                        RegistActivity.this.timeCount = 60;
                        RegistActivity.this.mHandler.sendEmptyMessage(1101);
                        String unused = RegistActivity.this.tel = RegistActivity.this.username.getText().toString();
                    }
                }
            });
        }
    }

    class MyTextWatchListener implements TextWatcher {
        int type = 0;

        MyTextWatchListener(int type2) {
            this.type = type2;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (this.type == 1) {
                PasswordUtil.checkPassword(RegistActivity.this.password, RegistActivity.this.linearPassword, RegistActivity.this.tvPasswodFormat);
            }
            RegistActivity.this.restButtonStyle(this.type);
        }
    }

    class MyFocusChange implements View.OnFocusChangeListener {
        int type = 0;

        MyFocusChange(int type2) {
            this.type = type2;
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (this.type) {
                    case 0:
                        if (!RegistActivity.this.isChinaPhoneLegal(RegistActivity.this.username.getText().toString())) {
                            RegistActivity.this.relPhone.setBackgroundResource(R.drawable.edit_bg_red);
                            RegistActivity.this.tvPhoneFormat.setVisibility(0);
                            return;
                        }
                        RegistActivity.this.relPhone.setBackgroundResource(R.drawable.edit_bg_press);
                        RegistActivity.this.tvPhoneFormat.setVisibility(8);
                        return;
                    case 1:
                        String password_str = RegistActivity.this.password.getText().toString();
                        int length1 = password_str.length();
                        if (TextUtils.isEmpty(password_str)) {
                            RegistActivity.this.regist.setEnabled(false);
                            RegistActivity.this.regist.setBackgroundResource(R.drawable.loginbutton_circle_grey);
                            return;
                        }
                        boolean isAllNumber = PasswordUtil.isPasswordAllNumber(password_str);
                        boolean isAllLetter = PasswordUtil.isPasswordAllLetter(password_str);
                        if (length1 < 6) {
                            RegistActivity.this.tvPasswodFormat.setVisibility(0);
                            RegistActivity.this.linearPassword.setBackgroundResource(R.drawable.edit_bg_red);
                            RegistActivity.this.tvPasswodFormat.setText("密码格式有错误：密码长度不足6位");
                            return;
                        } else if (isAllNumber) {
                            RegistActivity.this.linearPassword.setBackgroundResource(R.drawable.edit_bg_red);
                            RegistActivity.this.tvPasswodFormat.setVisibility(0);
                            RegistActivity.this.tvPasswodFormat.setText("密码格式有错误：不能只用纯数字");
                            return;
                        } else if (isAllLetter) {
                            RegistActivity.this.linearPassword.setBackgroundResource(R.drawable.edit_bg_red);
                            RegistActivity.this.tvPasswodFormat.setVisibility(0);
                            RegistActivity.this.tvPasswodFormat.setText("密码格式有错误：不能只用纯字母");
                            return;
                        } else {
                            RegistActivity.this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                            RegistActivity.this.tvPasswodFormat.setVisibility(8);
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void restButtonStyle(int type) {
        int length1 = this.username.getText().length();
        int length2 = this.password.getText().length();
        int length3 = this.verCode.getText().length();
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
        if (length1 <= 0 || length2 <= 0 || length3 <= 0) {
            this.regist.setEnabled(false);
            this.regist.setTextColor(-2130706433);
            this.regist.setBackgroundResource(R.drawable.bg_regist);
        } else {
            this.regist.setEnabled(true);
            this.regist.setTextColor(-1);
            this.regist.setBackgroundResource(R.drawable.login_circle_blue);
        }
        if (length3 == 6 && type == 2) {
            this.password.requestFocus();
        }
    }
}