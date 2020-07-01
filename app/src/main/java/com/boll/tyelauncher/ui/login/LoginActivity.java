package com.boll.tyelauncher.ui.login;

package com.toycloud.launcher.ui.login;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PointerIconCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.iflytek.easytrans.dependency.logagent.LogAgent;
import com.iflytek.stats.StatsCode;
import com.iflytek.utils.LogAgentHelper;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.LoginResponse;
import com.toycloud.launcher.api.service.LoginHttpHelper;
import com.toycloud.launcher.api.service.bean.LoginRequest;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.db.DBHelper;
import com.toycloud.launcher.exception.InvalidTokenException;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.model.launcher.LauncherModel;
import com.toycloud.launcher.provide.LanucherContentProvider;
import com.toycloud.launcher.ui.common.ForgetPasswordActivity;
import com.toycloud.launcher.ui.regist.CompleteUserInfoActivity;
import com.toycloud.launcher.ui.regist.RegistActivity;
import com.toycloud.launcher.util.PadInfoUtil;
import com.toycloud.launcher.util.PasswordUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.view.DialogUtil;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.retrofit.ApiException;
import framework.hz.salmon.retrofit.BaseSubscriber;
import framework.hz.salmon.util.NetworkUtil;
import framework.hz.salmon.util.SystemUtils;
import framework.hz.salmon.view.AutoHiddenHintEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class LoginActivity extends BaseFragmentActivity {
    private static final String TAG = "LoginActivity";
    public static boolean isForgound = false;
    private long exitTime = 0;
    @BindView(2131689756)
    TextView forgetPassword;
    private boolean isShowPassword = false;
    @BindView(2131689684)
    ImageView ivShowclearpassword;
    @BindView(2131689685)
    ImageView iv_clearpassword;
    @BindView(2131689753)
    ImageView iv_icon_password;
    @BindView(2131689748)
    LinearLayout linearPassword;
    @BindView(2131689752)
    LinearLayout linearUsername;
    @BindView(2131689755)
    Button login;
    @BindView(2131689683)
    AutoHiddenHintEditText password;
    @BindView(2131689757)
    TextView regist;
    @BindView(2131689754)
    TextView tvPasswordFormat;
    @BindView(2131689747)
    TextView tvPhoneFormat;
    @BindView(2131689746)
    AutoHiddenHintEditText username;

    /* access modifiers changed from: protected */
    @RequiresApi(api = 24)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GlobalVariable.list_Activity.clear();
        GlobalVariable.list_Activity.add(this);
        ButterKnife.bind((Activity) this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        if (!NetworkUtil.isNetworkAvailable(this)) {
            showDilogToConnectWifi();
        }
        editTextListener();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        isForgound = true;
        LogAgentHelper.onActive();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        isForgound = false;
    }

    private void editTextListener() {
        this.username.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                int length1 = LoginActivity.this.password.getText().length();
                int length2 = LoginActivity.this.username.getText().length();
                if (length2 == 11) {
                    if (!LoginActivity.this.isChinaPhoneLegal(LoginActivity.this.username.getText().toString())) {
                        LoginActivity.this.linearUsername.setBackgroundResource(R.drawable.edit_bg_red);
                        LoginActivity.this.tvPhoneFormat.setVisibility(0);
                    } else {
                        LoginActivity.this.linearUsername.setBackgroundResource(R.drawable.edit_bg_press);
                        LoginActivity.this.tvPhoneFormat.setVisibility(8);
                    }
                }
                if (length1 <= 0 || length2 <= 0) {
                    LoginActivity.this.login.setEnabled(false);
                    LoginActivity.this.login.setBackgroundResource(R.drawable.loginbutton_circle_grey);
                    return;
                }
                LoginActivity.this.login.setEnabled(true);
                LoginActivity.this.login.setBackgroundResource(R.drawable.login_circle_blue);
            }
        });
        this.password.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                int length1 = LoginActivity.this.password.getText().length();
                int length2 = LoginActivity.this.username.getText().length();
                PasswordUtil.checkPassword(LoginActivity.this.password, LoginActivity.this.linearPassword, LoginActivity.this.tvPasswordFormat);
                if (length1 <= 0 || length2 <= 0) {
                    LoginActivity.this.login.setEnabled(false);
                    LoginActivity.this.login.setBackgroundResource(R.drawable.loginbutton_circle_grey);
                    return;
                }
                LoginActivity.this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                LoginActivity.this.tvPasswordFormat.setVisibility(8);
                LoginActivity.this.login.setEnabled(true);
                LoginActivity.this.tvPasswordFormat.setVisibility(8);
                LoginActivity.this.login.setBackgroundResource(R.drawable.login_circle_blue);
            }
        });
        this.username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !TextUtils.isEmpty(LoginActivity.this.username.getText().toString())) {
                    if (!LoginActivity.this.isChinaPhoneLegal(LoginActivity.this.username.getText().toString())) {
                        LoginActivity.this.linearUsername.setBackgroundResource(R.drawable.edit_bg_red);
                        LoginActivity.this.tvPhoneFormat.setVisibility(0);
                        return;
                    }
                    LoginActivity.this.linearUsername.setBackgroundResource(R.drawable.edit_bg_press);
                    LoginActivity.this.tvPhoneFormat.setVisibility(8);
                }
            }
        });
        this.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    LoginActivity.this.iv_icon_password.setImageResource(R.drawable.login_password);
                    String password_str = LoginActivity.this.password.getText().toString();
                    int length1 = password_str.length();
                    if (TextUtils.isEmpty(password_str)) {
                        LoginActivity.this.login.setEnabled(false);
                        LoginActivity.this.login.setBackgroundResource(R.drawable.loginbutton_circle_grey);
                        return;
                    }
                    boolean isPasswordAllNumber = PasswordUtil.isPasswordAllNumber(password_str);
                    boolean isPasswordAllLetter = PasswordUtil.isPasswordAllLetter(password_str);
                    if (length1 < 6) {
                        LoginActivity.this.tvPasswordFormat.setVisibility(0);
                        LoginActivity.this.linearPassword.setBackgroundResource(R.drawable.edit_bg_red);
                        LoginActivity.this.tvPasswordFormat.setText("密码格式有错误：密码长度不足6位");
                        return;
                    }
                    LoginActivity.this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                    LoginActivity.this.tvPasswordFormat.setVisibility(8);
                    return;
                }
                LoginActivity.this.iv_icon_password.setImageResource(R.drawable.login_password_his);
            }
        });
    }

    public boolean handleMessage(Message message) {
        return false;
    }

    public void onClick(View view) {
    }

    @OnClick({2131689755, 2131689756, 2131689757, 2131689685, 2131689661, 2131689684})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                exit();
                return;
            case R.id.iv_showclearpassword:
                if (!this.isShowPassword) {
                    this.isShowPassword = true;
                    this.ivShowclearpassword.setImageResource(R.drawable.but_open);
                    this.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    this.password.setSelection(this.password.getText().toString().length());
                    return;
                }
                this.ivShowclearpassword.setImageResource(R.drawable.but_close);
                this.isShowPassword = false;
                this.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.password.setSelection(this.password.getText().toString().length());
                return;
            case R.id.iv_clearpassword:
                this.password.setText("");
                this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                this.tvPasswordFormat.setVisibility(8);
                return;
            case R.id.login:
                Log.d(TAG, "埋点:FT12005");
                LogAgentHelper.onOPLogEvent(StatsCode.CLICK_QHZH_LOGIN_BTN, (Map<String, String>) null);
                boolean networkAvailable = NetworkUtil.isNetworkAvailable(this);
                String password_str = this.password.getText().toString();
                int length1 = password_str.length();
                if (TextUtils.isEmpty(password_str)) {
                    this.login.setEnabled(false);
                    this.login.setBackgroundResource(R.drawable.loginbutton_circle_grey);
                } else {
                    boolean isPasswordAllNumber = PasswordUtil.isPasswordAllNumber(password_str);
                    boolean isPasswordAllLetter = PasswordUtil.isPasswordAllLetter(password_str);
                    if (length1 < 6) {
                        this.tvPasswordFormat.setVisibility(0);
                        this.linearPassword.setBackgroundResource(R.drawable.edit_bg_red);
                        this.tvPasswordFormat.setText("密码格式有错误：密码长度不足6位");
                        return;
                    }
                    this.linearPassword.setBackgroundResource(R.drawable.edit_bg_press);
                    this.tvPasswordFormat.setVisibility(8);
                }
                if (networkAvailable) {
                    login();
                    return;
                } else {
                    showDilogToConnectWifi();
                    return;
                }
            case R.id.forget_password:
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
                return;
            case R.id.regist:
                startActivityForResult(new Intent(getApplicationContext(), RegistActivity.class), PointerIconCompat.TYPE_CONTEXT_MENU);
                return;
            default:
                return;
        }
    }

    public void showDilogToConnectWifi() {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("网络提示");
        normalDialog.setMessage("当前无网络，是否前往设置");
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    LoginActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                } catch (Throwable th) {
                }
            }
        });
        normalDialog.create().show();
    }

    public boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        return Pattern.compile("^1[3-9]\\d{9}$").matcher(str).matches();
    }

    private void login() {
        if (TextUtils.isEmpty(this.username.getText().toString())) {
            Toast.makeText(this, "请输入手机号", 0).show();
        } else if (!isChinaPhoneLegal(this.username.getText().toString())) {
            this.tvPhoneFormat.setVisibility(0);
            this.linearUsername.setBackgroundResource(R.drawable.edit_bg_red);
        } else if (TextUtils.isEmpty(this.password.getText().toString())) {
            Toast.makeText(this, "请输入密码", 0).show();
        } else {
            showProgressDialog();
            PadInfoUtil padInfoUtil = new PadInfoUtil(this);
            LoginRequest request = new LoginRequest();
            request.username = this.username.getText().toString();
            request.password = this.password.getText().toString();
            request.sn = padInfoUtil.getSnCode();
            request.loginIp = padInfoUtil.getIPAddress();
            request.systemVersionCode = padInfoUtil.getSystemVersionCode();
            request.macAddress = PadInfoUtil.getMacFromHardware();
            LoginHttpHelper.getInstance().getLoginService().login(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<LoginResponse>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                    LoginActivity.this.dismissProgressDialog();
                }

                public void onError(Throwable e) {
                    LoginActivity.this.dismissProgressDialog();
                    if (e instanceof ApiException) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), 0).show();
                        if (((ApiException) e).mErrorCode == 2004) {
                            LoginActivity.this.showDialog_reg();
                        } else {
                            super.onError(e);
                        }
                    } else {
                        super.onError(e);
                    }
                }

                public void onNext(LoginResponse response) {
                    Log.e("logininfo---->", response.toString());
                    super.onNext(response);
                    LoginActivity.this.dismissProgressDialog();
                    SharepreferenceUtil.putInt("storageType", 0);
                    User data = response.getData();
                    GlobalVariable.LOGIN_USER = data;
                    String token = data.getToken();
                    if (TextUtils.isEmpty(token) || TextUtils.isEmpty(token.trim())) {
                        Logger.e(LoginActivity.TAG, "服务端返回的token为空");
                        LogAgent.onError(new InvalidTokenException("服务端返回了空token"));
                    }
                    GlobalVariable.setTOKEN(token, true);
                    GlobalVariable.setLogin(true, GlobalVariable.LOGIN_USER);
                    LoginActivity.this.inSertUserInfo(data);
                    SharepreferenceUtil.getSharepferenceInstance(LoginActivity.this).setUserInfo(data);
                    LauncherModel.getInstance().login(data);
                    if (TextUtils.isEmpty(data.getRealname())) {
                        Intent intent = new Intent(LoginActivity.this, CompleteUserInfoActivity.class);
                        intent.addFlags(268435456);
                        LoginActivity.this.startActivityForResult(intent, PointerIconCompat.TYPE_CONTEXT_MENU);
                        LoginActivity.this.finish();
                        return;
                    }
                    SystemUtils.removeRecentTask((String) null, (ActivityManager) LoginActivity.this.getSystemService("activity"));
                    Toast.makeText(LoginActivity.this, "登录成功", 0).show();
                    LoginActivity.this.finish();
                }
            });
        }
    }

    private void killBackgroundGress(ActivityManager systemService, String appcode) {
        try {
            Method forceStopPackage = ActivityManager.class.getMethod("forceStopPackage", new Class[]{String.class});
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(systemService, new Object[]{appcode});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void inSertUserInfo(User data) {
        new DBHelper(this).getWritableDatabase();
        Uri uri = LanucherContentProvider.PERSON_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudyCPHelper.COLUMN_NAME_UNIQUE_FLAG, 1);
        contentValues.put("username", data.getUsername());
        contentValues.put("realname", data.getRealname());
        contentValues.put(StudyCPHelper.COLUMN_NAME_GRADE_CODE, data.getGradecode());
        contentValues.put("areacode", data.getAreacode());
        contentValues.put("userid", data.getUserid());
        contentValues.put("schoolid", data.getSchoolid());
        contentValues.put("sex", data.getSex());
        contentValues.put("token", data.getToken());
        contentValues.put("headurl", data.getUsericonpath());
        Log.d("logininfo---->", "uri1:" + getContentResolver().insert(uri, contentValues).toString());
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PointerIconCompat.TYPE_CONTEXT_MENU:
                if (resultCode == -1) {
                    finish();
                    return;
                }
                return;
            default:
                return;
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
                LoginActivity.this.startActivityForResult(new Intent(LoginActivity.this.getApplicationContext(), RegistActivity.class), PointerIconCompat.TYPE_CONTEXT_MENU);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        exit();
        return false;
    }

    public void exit() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void killBackgroundTask() {
        ActivityManager activityManager = (ActivityManager) getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance != 100) {
                    SystemUtils.removeRecentTask(appProcess.processName, activityManager);
                }
            }
        }
    }
}
