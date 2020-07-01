package com.boll.tyelauncher.ui.regist;

package com.toycloud.launcher.ui.regist;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
import com.iflytek.easytrans.core.async.thread.TaskRunner;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.School;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.GetGradeResponse;
import com.toycloud.launcher.api.response.GetPublisherResponse;
import com.toycloud.launcher.api.response.GetSchoolResponse;
import com.toycloud.launcher.api.response.StudentChangeInfoResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.api.service.bean.UpdateUserInfoRequest;
import com.toycloud.launcher.biz.globalconfig.CityInfoUpgradeManager;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.model.launcher.LauncherModel;
import com.toycloud.launcher.provide.LanucherContentProvider;
import com.toycloud.launcher.ui.MainActivity;
import com.toycloud.launcher.ui.common.BindWXActivity;
import com.toycloud.launcher.ui.listener.GlobalUserInfoListener;
import com.toycloud.launcher.ui.login.LoginActivity;
import com.toycloud.launcher.util.CustomToast;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.ToastUtils;
import com.toycloud.launcher.view.ChangeDatePopwindow;
import com.toycloud.launcher.view.CityPickerPopWin;
import com.toycloud.launcher.view.DialogUtil;
import com.toycloud.launcher.view.SchoolPickerPopWin;
import com.toycloud.launcher.view.UserGradePopWin;
import com.toycloud.launcher.view.UserSexPopWin;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.retrofit.BaseSubscriber;
import framework.hz.salmon.util.NetworkUtil;
import framework.hz.salmon.util.SystemUtils;
import framework.hz.salmon.view.AutoHiddenHintEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Type;

public class CompleteUserInfoActivity extends BaseFragmentActivity {
    @BindView(2131689701)
    LinearLayout activityMain;
    @BindView(2131689706)
    ImageView back;
    @BindView(2131689719)
    AutoHiddenHintEditText classroom;
    /* access modifiers changed from: private */
    public String defaultDay = "1";
    /* access modifiers changed from: private */
    public String defaultMonth = "1";
    /* access modifiers changed from: private */
    public String defaultYear = "2000";
    private long exitTime = 0;
    /* access modifiers changed from: private */
    public CityInfoUpgradeManager.OnInitProvinceDataListener mOnInitProvinceListener = new CityInfoUpgradeManager.OnInitProvinceDataListener() {
        public void onLoadComplete(final boolean success) {
            TaskRunner.getUIHandler().post(new Runnable() {
                public void run() {
                    CompleteUserInfoActivity.this.dismissProgressDialog();
                    if (success) {
                        CompleteUserInfoActivity.this.showRegionSelectPopWindow();
                    } else {
                        ToastUtils.showShort((CharSequence) "系统异常，请稍后重试");
                    }
                }
            });
        }
    };
    @BindView(2131689721)
    AutoHiddenHintEditText number;
    @BindView(2131689705)
    Button save;
    /* access modifiers changed from: private */
    public StringBuffer selectedBirthDay = new StringBuffer();
    /* access modifiers changed from: private */
    public GetGradeResponse.DataBean selectedGradeBean;
    /* access modifiers changed from: private */
    public String[] selectedRegion = {"安徽省", "合肥市"};
    /* access modifiers changed from: private */
    public String selectedRegionCode;
    /* access modifiers changed from: private */
    public School selectedSchoolBean;
    /* access modifiers changed from: private */
    public String selectedSchoolName;
    /* access modifiers changed from: private */
    public int selectedSex = -1;
    @BindView(2131689714)
    LinearLayout userBirthdayLayout;
    @BindView(2131689715)
    TextView userBirthdayValue;
    @BindView(2131689712)
    LinearLayout userCityLayout;
    @BindView(2131689713)
    TextView userCityValue;
    @BindView(2131689708)
    RelativeLayout userGradeLayout;
    @BindView(2131689709)
    TextView userGradeValue;
    @BindView(2131689707)
    AutoHiddenHintEditText userName;
    @BindView(2131689716)
    LinearLayout userSchoolLayout;
    @BindView(2131689717)
    TextView userSchoolValue;
    @BindView(2131689710)
    LinearLayout userSexLayout;
    @BindView(2131689711)
    TextView userSexValue;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_infos);
        Log.e("liangge--->", System.currentTimeMillis() + "");
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
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }

    private void initDefault() {
        User userInfo = SharepreferenceUtil.getSharepferenceInstance(this).getUserInfo();
        if (userInfo != null) {
            String name = userInfo.getRealname();
            String sex = userInfo.getSex();
            String gradeName = userInfo.getGradeName();
            String city = userInfo.getAddress();
            String schoolName = userInfo.getSchoolName();
            AutoHiddenHintEditText autoHiddenHintEditText = this.userName;
            if (TextUtils.isEmpty(name)) {
                name = "";
            }
            autoHiddenHintEditText.setText(name);
            TextView textView = this.userSexValue;
            if (TextUtils.isEmpty(sex)) {
                sex = "";
            }
            textView.setText(sex);
            TextView textView2 = this.userGradeValue;
            if (TextUtils.isEmpty(gradeName)) {
                gradeName = "";
            }
            textView2.setText(gradeName);
            TextView textView3 = this.userCityValue;
            if (TextUtils.isEmpty(city)) {
                city = "";
            }
            textView3.setText(city);
            TextView textView4 = this.userSchoolValue;
            if (TextUtils.isEmpty(schoolName)) {
                schoolName = "";
            }
            textView4.setText(schoolName);
        }
        this.userName.setText(userInfo.getRealname());
    }

    private void initListener() {
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                CompleteUserInfoActivity.this.restButtonStyle();
            }
        };
        this.userName.addTextChangedListener(textWatcher);
        this.classroom.addTextChangedListener(textWatcher);
        this.number.addTextChangedListener(textWatcher);
    }

    public boolean handleMessage(Message message) {
        return false;
    }

    public void onClick(View view) {
    }

    @OnClick({2131689708, 2131689710, 2131689712, 2131689714, 2131689716, 2131689705, 2131689706})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save:
                if (!NetworkUtil.isNetworkAvailable(this)) {
                    CustomToast.showToast((Context) this, "网络连接错误，请检查网络设置后重试");
                    return;
                } else {
                    save();
                    return;
                }
            case R.id.iv_back_information:
                exit();
                return;
            case R.id.user_grade_layout:
                if (!NetworkUtil.isNetworkAvailable(this)) {
                    CustomToast.showToast((Context) this, "网络连接错误，请检查网络设置后重试");
                    return;
                } else if (!NoDoubleClickUtils.isDoubleClick()) {
                    getGrade();
                    return;
                } else {
                    return;
                }
            case R.id.user_sex_layout:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    new UserSexPopWin.Builder(this, new UserSexPopWin.OnSexPickListener() {
                        public void onPicked(int sex, String desc) {
                            int unused = CompleteUserInfoActivity.this.selectedSex = sex;
                            CompleteUserInfoActivity.this.userSexValue.setText(desc);
                            CompleteUserInfoActivity.this.userSexValue.setTextColor(Color.parseColor("#333333"));
                            CompleteUserInfoActivity.this.restButtonStyle();
                        }
                    }).setSex(this.selectedSex).build().showPopWin(this);
                    return;
                }
                return;
            case R.id.user_city_layout:
                if (!NetworkUtil.isNetworkAvailable(this)) {
                    CustomToast.showToast((Context) this, "网络连接错误，请检查网络设置后重试");
                    return;
                } else if (!NoDoubleClickUtils.isDoubleClick()) {
                    showProgressDialog();
                    TaskRunner.getBackHandler().post(new Runnable() {
                        public void run() {
                            CityInfoUpgradeManager.getInstance().loadData(CompleteUserInfoActivity.this, CompleteUserInfoActivity.this.mOnInitProvinceListener);
                        }
                    });
                    return;
                } else {
                    return;
                }
            case R.id.user_birthday_layout:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(this, this.defaultYear, this.defaultMonth, this.defaultDay);
                    mChangeBirthDialog.showAtLocation(this.userCityLayout, 17, 0, 0);
                    mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {
                        public void onClick(String year, String month, String day) {
                            CompleteUserInfoActivity.this.userBirthdayValue.setText(year + month + day);
                            CompleteUserInfoActivity.this.userBirthdayValue.setTextColor(Color.parseColor("#333333"));
                            CompleteUserInfoActivity.this.restButtonStyle();
                            CompleteUserInfoActivity.this.selectedBirthDay.delete(0, CompleteUserInfoActivity.this.selectedBirthDay.length());
                            String string_year = year.substring(0, year.length() - 1);
                            String string_month = month.substring(0, month.length() - 1);
                            String string_day = day.substring(0, day.length() - 1);
                            String unused = CompleteUserInfoActivity.this.defaultYear = string_year;
                            String unused2 = CompleteUserInfoActivity.this.defaultMonth = string_month;
                            String unused3 = CompleteUserInfoActivity.this.defaultDay = string_day;
                            CompleteUserInfoActivity.this.selectedBirthDay.append(string_year + "-" + string_month + "-" + string_day);
                            Log.e("birthday-->", CompleteUserInfoActivity.this.selectedBirthDay.toString());
                        }
                    });
                    return;
                }
                return;
            case R.id.user_school_layout:
                if (!NetworkUtil.isNetworkAvailable(this)) {
                    CustomToast.showToast((Context) this, "网络连接错误，请检查网络设置后重试");
                    return;
                } else if (!NoDoubleClickUtils.isDoubleClick()) {
                    getSchool();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void showRegionSelectPopWindow() {
        CityPickerPopWin mChangeAddressPopwindow = new CityPickerPopWin(this, this.selectedRegion[0], this.selectedRegion[1]);
        mChangeAddressPopwindow.showAtLocation(this.activityMain, 80, 0, 0);
        mChangeAddressPopwindow.setAddresskListener(new CityPickerPopWin.OnAddressCListener() {
            public void onClick(String province, String city, String id) {
                CompleteUserInfoActivity.this.selectedRegion[0] = province;
                CompleteUserInfoActivity.this.selectedRegion[1] = city;
                if (!TextUtils.isEmpty(CompleteUserInfoActivity.this.selectedRegionCode) && !CompleteUserInfoActivity.this.selectedRegionCode.equals(id)) {
                    CompleteUserInfoActivity.this.userSchoolValue.setText("");
                    CompleteUserInfoActivity.this.userSchoolValue.setTextColor(Color.parseColor("#333333"));
                    School unused = CompleteUserInfoActivity.this.selectedSchoolBean = null;
                }
                String unused2 = CompleteUserInfoActivity.this.selectedRegionCode = id;
                if (CompleteUserInfoActivity.this.selectedRegion[0].contains("天津市") || CompleteUserInfoActivity.this.selectedRegion[0].contains("北京市") || CompleteUserInfoActivity.this.selectedRegion[0].contains("重庆市") || CompleteUserInfoActivity.this.selectedRegion[0].contains("上海市")) {
                    CompleteUserInfoActivity.this.userCityValue.setText(CompleteUserInfoActivity.this.selectedRegion[0]);
                } else {
                    CompleteUserInfoActivity.this.userCityValue.setText(CompleteUserInfoActivity.this.selectedRegion[0] + CompleteUserInfoActivity.this.selectedRegion[1]);
                }
                CompleteUserInfoActivity.this.userCityValue.setTextColor(Color.parseColor("#333333"));
                CompleteUserInfoActivity.this.restButtonStyle();
            }
        });
    }

    public void showDialogConfirm(String message) {
        DialogUtil.showDilog(this, false, message, "返回", "退出", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void save() {
        String name = this.userName.getText().toString();
        if (name.startsWith(" ") || name.endsWith(" ")) {
            Toast.makeText(this, "用户名不允许空格开头或结尾", 0).show();
        } else if (TextUtils.isEmpty(this.userName.getText().toString())) {
            showDialogConfirm("请输入用户名");
        } else if (this.userName.length() > 8) {
            Toast.makeText(this, "姓名长度超过限制", 0).show();
        } else if (this.selectedGradeBean == null) {
            showDialogConfirm("请先设置年级");
        } else if (this.selectedRegionCode == null) {
            showDialogConfirm("请设置所在城市");
        } else {
            UpdateUserInfoRequest request = new UpdateUserInfoRequest();
            request.sex = this.userSexValue.getText().toString();
            request.realname = this.userName.getText().toString();
            request.gradecode = this.selectedGradeBean.getGradecode();
            if (!TextUtils.isEmpty(this.selectedSchoolName)) {
                request.schoolName = this.selectedSchoolName;
            } else if (this.selectedSchoolBean != null) {
                request.schoolid = this.selectedSchoolBean.getSchoolid();
            }
            request.areacode = this.selectedRegionCode;
            if (TextUtils.isEmpty(this.selectedBirthDay.toString())) {
                request.birthday = null;
            } else {
                request.birthday = this.selectedBirthDay.toString();
            }
            request.className = this.classroom.getText().toString();
            request.studentNumber = this.number.getText().toString();
            LauncherHttpHelper.getLauncherService().studentChangeInfo(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<StudentChangeInfoResponse>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                    CompleteUserInfoActivity.this.dismissProgressDialog();
                }

                public void onError(Throwable e) {
                    CompleteUserInfoActivity.this.dismissProgressDialog();
                    super.onError(e);
                }

                public void onNext(StudentChangeInfoResponse response) {
                    super.onNext(response);
                    CompleteUserInfoActivity.this.dismissProgressDialog();
                    GlobalVariable.LOGIN_USER = response.getData();
                    String token = SharepreferenceUtil.getToken();
                    Gson gson = new GsonBuilder().create();
                    User user = (User) gson.fromJson(gson.toJson((Object) response.getData(), (Type) User.class), User.class);
                    user.setGradeName(CompleteUserInfoActivity.this.selectedGradeBean.getGradename());
                    user.setToken(token);
                    SharepreferenceUtil.getSharepferenceInstance(CompleteUserInfoActivity.this).setUserInfo(user);
                    GlobalUserInfoListener.getInstance().onUserInfoChanged(true, GlobalVariable.LOGIN_USER);
                    CustomToast.showToast(this.mContext, "填写个人资料成功");
                    Uri uri = LanucherContentProvider.PERSON_CONTENT_URI;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(StudyCPHelper.COLUMN_NAME_UNIQUE_FLAG, 1);
                    contentValues.put("username", user.getUsername());
                    contentValues.put("realname", user.getRealname());
                    contentValues.put(StudyCPHelper.COLUMN_NAME_GRADE_CODE, user.getGradecode());
                    contentValues.put("areacode", user.getAreacode());
                    contentValues.put("userid", user.getUserid());
                    if (!TextUtils.isEmpty(user.getPublisher())) {
                        contentValues.put("publisher", user.getPublisher());
                    }
                    if (!TextUtils.isEmpty(user.getSchoolid())) {
                        contentValues.put("schoolid", user.getSchoolid());
                    }
                    contentValues.put("sex", user.getSex());
                    contentValues.put("token", user.getToken());
                    CompleteUserInfoActivity.this.getContentResolver().insert(uri, contentValues);
                    Intent intent = new Intent(CompleteUserInfoActivity.this, BindWXActivity.class);
                    intent.putExtra(BindWXActivity.EXTRA_BIND_WX_ACT_START_FROM, 1);
                    CompleteUserInfoActivity.this.startActivity(intent);
                    LauncherModel.getInstance().login(user);
                    CompleteUserInfoActivity.this.finish();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                if (resultCode == -1) {
                    setResult(-1);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(268435456);
                    startActivity(intent);
                    finish();
                    break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getSchool() {
        if (this.selectedRegionCode == null) {
            Toast.makeText(this, "请先设置所在城市", 0).show();
        } else if (this.selectedGradeBean == null) {
            Toast.makeText(this, "请先选择年级", 1).show();
        } else {
            showProgressDialog();
            LauncherHttpHelper.getLauncherService().getSchool(this.selectedRegionCode, this.selectedGradeBean.getGradecode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetSchoolResponse>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                    CompleteUserInfoActivity.this.dismissProgressDialog();
                }

                public void onError(Throwable e) {
                    CompleteUserInfoActivity.this.dismissProgressDialog();
                    CustomToast.showToast((Context) CompleteUserInfoActivity.this, "服务器异常，请稍后重试");
                    super.onError(e);
                }

                public void onNext(GetSchoolResponse response) {
                    super.onNext(response);
                    CompleteUserInfoActivity.this.dismissProgressDialog();
                    new SchoolPickerPopWin.Builder(CompleteUserInfoActivity.this, new SchoolPickerPopWin.OnSchoolPickedListener() {
                        public void onPick(School bean, String schoolName) {
                            School unused = CompleteUserInfoActivity.this.selectedSchoolBean = bean;
                            if (bean == null) {
                                CompleteUserInfoActivity.this.userSchoolValue.setText(schoolName);
                                String unused2 = CompleteUserInfoActivity.this.selectedSchoolName = schoolName;
                                CompleteUserInfoActivity.this.userSchoolValue.setTextColor(Color.parseColor("#333333"));
                                CompleteUserInfoActivity.this.restButtonStyle();
                                return;
                            }
                            String unused3 = CompleteUserInfoActivity.this.selectedSchoolName = "";
                            CompleteUserInfoActivity.this.userSchoolValue.setText(bean.getSchoolname());
                            CompleteUserInfoActivity.this.userSchoolValue.setTextColor(Color.parseColor("#333333"));
                            CompleteUserInfoActivity.this.restButtonStyle();
                            SharepreferenceUtil.getSharepferenceInstance(CompleteUserInfoActivity.this).setSchoolSelectPosition(bean.getSelectPosition());
                        }
                    }).setData(response.getData(), new StringBuffer(CompleteUserInfoActivity.this.selectedSchoolBean == null ? "" : CompleteUserInfoActivity.this.selectedSchoolBean.getSchoolname())).build().showPopWin(CompleteUserInfoActivity.this);
                }
            });
        }
    }

    private void getGrade() {
        showProgressDialog();
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.userGradeValue.getWindowToken(), 0);
        LauncherHttpHelper.getLauncherService().getGradesByPhase(TextUtils.join(",", new String[]{"03", "04", "05"})).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetGradeResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
                CompleteUserInfoActivity.this.dismissProgressDialog();
            }

            public void onError(Throwable e) {
                CompleteUserInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(GetGradeResponse response) {
                super.onNext(response);
                CompleteUserInfoActivity.this.dismissProgressDialog();
                Log.e("GetGradeResponse-->", response.toString());
                new UserGradePopWin.Builder(CompleteUserInfoActivity.this, new UserGradePopWin.OnGradePickedListener() {
                    public void onPicked(GetGradeResponse.DataBean bean) {
                        GetGradeResponse.DataBean unused = CompleteUserInfoActivity.this.selectedGradeBean = bean;
                        CompleteUserInfoActivity.this.userGradeValue.setText(bean.getGradename());
                        CompleteUserInfoActivity.this.userGradeValue.setTextColor(Color.parseColor("#333333"));
                        CompleteUserInfoActivity.this.restButtonStyle();
                    }
                }).setData(response.getData()).setGrade(CompleteUserInfoActivity.this.selectedGradeBean).build().showPopWin(CompleteUserInfoActivity.this);
            }
        });
    }

    private void getPublisher(String grade, String subject) {
        LauncherHttpHelper.getLauncherService().getPublisher(grade, subject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetPublisherResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
                CompleteUserInfoActivity.this.dismissProgressDialog();
            }

            public void onError(Throwable e) {
                CompleteUserInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(GetPublisherResponse response) {
                super.onNext(response);
                CompleteUserInfoActivity.this.dismissProgressDialog();
            }
        });
    }

    private void initGrade() {
        LauncherHttpHelper.getLauncherService().getGradesByPhase(TextUtils.join(",", new String[]{"03", "04", "05"})).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetGradeResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                CompleteUserInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(GetGradeResponse response) {
                super.onNext(response);
                for (int i = 0; i < response.getData().size(); i++) {
                    if (response.getData().get(i).getGradecode().equals(GlobalVariable.LOGIN_USER.getGradecode())) {
                        GetGradeResponse.DataBean unused = CompleteUserInfoActivity.this.selectedGradeBean = response.getData().get(i);
                        CompleteUserInfoActivity.this.userGradeValue.setText(CompleteUserInfoActivity.this.selectedGradeBean.getGradename());
                        CompleteUserInfoActivity.this.userGradeValue.setTextColor(Color.parseColor("#333333"));
                    }
                }
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
        if (System.currentTimeMillis() - this.exitTime > 2000) {
            this.exitTime = System.currentTimeMillis();
            showQuitDialog();
        }
    }

    public void showQuitDialog() {
        DialogUtil.showDilogToCancel(this, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharepreferenceUtil.getSharepferenceInstance(CompleteUserInfoActivity.this).Logout();
                Intent intent = new Intent(CompleteUserInfoActivity.this, LoginActivity.class);
                intent.addFlags(268435456);
                CompleteUserInfoActivity.this.startActivity(intent);
                SystemUtils.removeRecentTask((String) null, (ActivityManager) CompleteUserInfoActivity.this.getSystemService("activity"));
                CompleteUserInfoActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: private */
    public void restButtonStyle() {
        int length1 = this.userName.getText().toString().length();
        int length2 = this.userGradeValue.getText().toString().length();
        int length3 = this.userCityValue.getText().toString().length();
        int length = this.userBirthdayValue.getText().toString().length();
        int length4 = this.userSchoolValue.getText().toString().length();
        int length5 = this.userSexValue.getText().toString().length();
        String charSequence = this.userSexValue.getText().toString();
        if (length1 <= 0 || length2 <= 0 || length3 <= 0) {
            this.save.setEnabled(false);
            this.save.setTextColor(-2130706433);
            this.save.setBackgroundResource(R.drawable.bg_next_information);
            return;
        }
        this.save.setEnabled(true);
        this.save.setTextColor(-1);
        this.save.setBackgroundResource(R.drawable.login_circle_blue);
    }
}
