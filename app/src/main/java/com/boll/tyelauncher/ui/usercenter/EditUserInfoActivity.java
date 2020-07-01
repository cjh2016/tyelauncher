package com.boll.tyelauncher.ui.usercenter;

package com.toycloud.launcher.ui.usercenter;

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
import com.iflytek.easytrans.dependency.logagent.LogAgent;
import com.iflytek.stats.StatsCode;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.School;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.GetGradeResponse;
import com.toycloud.launcher.api.response.GetPublisherResponse;
import com.toycloud.launcher.api.response.GetSchoolResponse;
import com.toycloud.launcher.api.response.ResponseAreaMessage;
import com.toycloud.launcher.api.response.SchoolBean;
import com.toycloud.launcher.api.response.StudentChangeInfoResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.api.service.bean.UpdateUserInfoRequest;
import com.toycloud.launcher.biz.globalconfig.CityInfoUpgradeManager;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.exception.UpdateContentProviderException;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.model.launcher.LauncherModel;
import com.toycloud.launcher.provide.LanucherContentProvider;
import com.toycloud.launcher.ui.MainActivity;
import com.toycloud.launcher.ui.listener.GlobalUserInfoListener;
import com.toycloud.launcher.ui.login.LoginActivity;
import com.toycloud.launcher.util.CustomToast;
import com.toycloud.launcher.util.DateUtil;
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
import java.util.Map;

public class EditUserInfoActivity extends BaseFragmentActivity {
    private static final String TAG = "EditUserInfoActivity";
    @BindView(2131689701)
    LinearLayout activityMain;
    @BindView(2131689706)
    ImageView back;
    @BindView(2131689719)
    AutoHiddenHintEditText classNum;
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
                    EditUserInfoActivity.this.dismissProgressDialog();
                    if (success) {
                        EditUserInfoActivity.this.showRegionSelectPopWindow();
                    } else {
                        ToastUtils.showShort((CharSequence) "系统异常，请稍后重试");
                    }
                }
            });
        }
    };
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
    @BindView(2131689721)
    AutoHiddenHintEditText studentNumber;
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
    /* access modifiers changed from: private */
    public User userInfo;
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
        setContentView(R.layout.activity_edit_userinfo_new);
        Log.e("liangge--->", System.currentTimeMillis() + "");
        ButterKnife.bind((Activity) this);
        this.userInfo = SharepreferenceUtil.getSharepferenceInstance(this).getUserInfo();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        initListener();
        setData();
    }

    private void setData() {
        if (this.userInfo != null && !TextUtils.isEmpty(this.userInfo.getRealname())) {
            this.userName.setText(this.userInfo.getRealname());
        }
        if (this.userInfo != null && !TextUtils.isEmpty(this.userInfo.getGradecode())) {
            initGrade();
        }
        initSchool();
        if (this.userInfo != null && !TextUtils.isEmpty(this.userInfo.getBirthday())) {
            this.userBirthdayValue.setText(DateUtil.getDateFromMillis(this.selectedBirthDay, this.userInfo.getBirthday()));
        }
        if (this.userInfo != null && !TextUtils.isEmpty(this.userInfo.getSex())) {
            this.userSexValue.setText(this.userInfo.getSex());
            this.selectedSex = this.userInfo.getSex().equals("男") ? 1 : 2;
        }
        if (this.userInfo != null && !TextUtils.isEmpty(this.userInfo.getAreacode())) {
            initRegion();
            this.selectedRegionCode = this.userInfo.getAreacode();
        }
        if (this.userInfo == null || TextUtils.isEmpty(this.userInfo.getClassName())) {
            this.classNum.setText("");
        } else {
            this.classNum.setText(this.userInfo.getClassName());
        }
        if (this.userInfo == null || TextUtils.isEmpty(this.userInfo.getStudentNumber())) {
            this.studentNumber.setText("");
        } else {
            this.studentNumber.setText(this.userInfo.getStudentNumber());
        }
    }

    private void initGrade() {
        LauncherHttpHelper.getLauncherService().getGradesByPhase(TextUtils.join(",", new String[]{"03", "04", "05"})).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetGradeResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                EditUserInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(GetGradeResponse response) {
                super.onNext(response);
                for (int i = 0; i < response.getData().size(); i++) {
                    if (response.getData().get(i).getGradecode().equals(EditUserInfoActivity.this.userInfo.getGradecode())) {
                        GetGradeResponse.DataBean unused = EditUserInfoActivity.this.selectedGradeBean = response.getData().get(i);
                        EditUserInfoActivity.this.userGradeValue.setText(EditUserInfoActivity.this.selectedGradeBean.getGradename());
                    }
                }
            }
        });
    }

    private void initSchool() {
        if (this.userInfo != null) {
            if (TextUtils.isEmpty(this.userInfo.getSchoolid())) {
                this.userSchoolValue.setText(this.userInfo.getSchoolName());
                this.selectedSchoolName = this.userInfo.getSchoolName();
                return;
            }
            LauncherHttpHelper.getLauncherService().getSchoolById(this.userInfo.getSchoolid()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<SchoolBean>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                    EditUserInfoActivity.this.dismissProgressDialog();
                }

                public void onError(Throwable e) {
                    EditUserInfoActivity.this.dismissProgressDialog();
                    ToastUtils.showShort((CharSequence) e.getMessage() + ":" + EditUserInfoActivity.this.userInfo.getSchoolid());
                    super.onError(e);
                }

                public void onNext(SchoolBean response) {
                    super.onNext(response);
                    EditUserInfoActivity.this.dismissProgressDialog();
                    School unused = EditUserInfoActivity.this.selectedSchoolBean = new School();
                    EditUserInfoActivity.this.selectedSchoolBean.setSchoolid(response.getData().getSchoolid());
                    String name = response.getData().getSchoolname();
                    EditUserInfoActivity.this.selectedSchoolBean.setSchoolname(name);
                    if (!TextUtils.isEmpty(name)) {
                        EditUserInfoActivity.this.userSchoolValue.setText(name);
                    } else {
                        CustomToast.showToast((Context) EditUserInfoActivity.this, "学校查询失败，请重新选择");
                    }
                }
            });
        }
    }

    private void initRegion() {
        LauncherHttpHelper.getLauncherService().getRegionPath(this.userInfo.getAreacode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ResponseAreaMessage>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                EditUserInfoActivity.this.dismissProgressDialog();
                ToastUtils.showShort((CharSequence) e.getMessage() + ":" + EditUserInfoActivity.this.userInfo.getAreacode());
                super.onError(e);
            }

            public void onNext(ResponseAreaMessage response) {
                super.onNext(response);
                Log.e("ResponseAreaMessage-->", response.toString() + "::");
                ResponseAreaMessage.DataBean dataBean = response.getData();
                EditUserInfoActivity.this.selectedRegion[0] = dataBean.getProvinceBean().getName();
                if (dataBean.getCityBean() != null) {
                    EditUserInfoActivity.this.selectedRegion[1] = dataBean.getCityBean().getName();
                } else {
                    EditUserInfoActivity.this.selectedRegion[1] = "";
                }
                if (EditUserInfoActivity.this.selectedRegion[0].contains("天津市") || EditUserInfoActivity.this.selectedRegion[0].contains("北京市") || EditUserInfoActivity.this.selectedRegion[0].contains("重庆市") || EditUserInfoActivity.this.selectedRegion[0].contains("上海市")) {
                    EditUserInfoActivity.this.userCityValue.setText(EditUserInfoActivity.this.selectedRegion[0]);
                } else {
                    EditUserInfoActivity.this.userCityValue.setText(EditUserInfoActivity.this.selectedRegion[0] + EditUserInfoActivity.this.selectedRegion[1]);
                }
            }
        });
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
        User userInfo2 = SharepreferenceUtil.getSharepferenceInstance(this).getUserInfo();
        if (userInfo2 != null) {
            String name = userInfo2.getRealname();
            String sex = userInfo2.getSex();
            String gradeName = userInfo2.getGradeName();
            String city = userInfo2.getAddress();
            String schoolName = userInfo2.getSchoolName();
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
        this.userName.setText(userInfo2.getRealname());
    }

    private void initListener() {
        this.userName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                EditUserInfoActivity.this.restButtonStyle();
            }
        });
    }

    public boolean handleMessage(Message message) {
        return false;
    }

    public void onClick(View view) {
    }

    @OnClick({2131689708, 2131689710, 2131689712, 2131689714, 2131689716, 2131689705, 2131689706, 2131689735})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save:
                if (!NetworkUtil.isNetworkAvailable(this)) {
                    CustomToast.showToast((Context) this, "网络连接错误，请检查网络设置后重试");
                    return;
                } else if (!NoDoubleClickUtils.isDoubleClick()) {
                    save();
                    LogAgentHelper.onOPLogEvent(StatsCode.CLICK_PERSONAL_INFO_SAVE_BTN, (Map<String, String>) null);
                    return;
                } else {
                    return;
                }
            case R.id.iv_back_information:
                finish();
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
                            int unused = EditUserInfoActivity.this.selectedSex = sex;
                            EditUserInfoActivity.this.userSexValue.setText(desc);
                            EditUserInfoActivity.this.userSexValue.setTextColor(Color.parseColor("#333333"));
                            EditUserInfoActivity.this.restButtonStyle();
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
                            CityInfoUpgradeManager.getInstance().loadData(EditUserInfoActivity.this, EditUserInfoActivity.this.mOnInitProvinceListener);
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
                            EditUserInfoActivity.this.userBirthdayValue.setText(year + month + day);
                            EditUserInfoActivity.this.userBirthdayValue.setTextColor(Color.parseColor("#333333"));
                            EditUserInfoActivity.this.restButtonStyle();
                            EditUserInfoActivity.this.selectedBirthDay.delete(0, EditUserInfoActivity.this.selectedBirthDay.length());
                            String string_year = year.substring(0, year.length() - 1);
                            String string_month = month.substring(0, month.length() - 1);
                            String string_day = day.substring(0, day.length() - 1);
                            String unused = EditUserInfoActivity.this.defaultYear = string_year;
                            String unused2 = EditUserInfoActivity.this.defaultMonth = string_month;
                            String unused3 = EditUserInfoActivity.this.defaultDay = string_day;
                            EditUserInfoActivity.this.selectedBirthDay.append(string_year + "-" + string_month + "-" + string_day);
                            Log.e("birthday-->", EditUserInfoActivity.this.selectedBirthDay.toString());
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
            case R.id.button_cancel:
                finish();
                return;
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
                EditUserInfoActivity.this.selectedRegion[0] = province;
                EditUserInfoActivity.this.selectedRegion[1] = city;
                if (!TextUtils.isEmpty(EditUserInfoActivity.this.selectedRegionCode) && !EditUserInfoActivity.this.selectedRegionCode.equals(id)) {
                    EditUserInfoActivity.this.userSchoolValue.setText("");
                    EditUserInfoActivity.this.userSchoolValue.setTextColor(Color.parseColor("#333333"));
                    School unused = EditUserInfoActivity.this.selectedSchoolBean = null;
                }
                String unused2 = EditUserInfoActivity.this.selectedRegionCode = id;
                EditUserInfoActivity.this.restButtonStyle();
                if (EditUserInfoActivity.this.selectedRegion[0].contains("天津市") || EditUserInfoActivity.this.selectedRegion[0].contains("北京市") || EditUserInfoActivity.this.selectedRegion[0].contains("重庆市") || EditUserInfoActivity.this.selectedRegion[0].contains("上海市")) {
                    EditUserInfoActivity.this.userCityValue.setText(EditUserInfoActivity.this.selectedRegion[0]);
                } else {
                    EditUserInfoActivity.this.userCityValue.setText(EditUserInfoActivity.this.selectedRegion[0] + EditUserInfoActivity.this.selectedRegion[1]);
                }
                EditUserInfoActivity.this.userCityValue.setTextColor(Color.parseColor("#333333"));
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
            CustomToast.showToast((Context) this, "请输入用户名");
        } else if (this.selectedGradeBean == null) {
            CustomToast.showToast((Context) this, "请选择年级");
        } else if (this.selectedRegionCode == null) {
            CustomToast.showToast((Context) this, "请选择城市");
        } else {
            showProgressDialog();
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
            request.className = this.classNum.getText().toString();
            request.studentNumber = this.studentNumber.getText().toString();
            LauncherHttpHelper.getLauncherService().studentChangeInfo(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<StudentChangeInfoResponse>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                }

                public void onError(Throwable e) {
                    EditUserInfoActivity.this.dismissProgressDialog();
                    ToastUtils.showShort((CharSequence) "保存失败，稍候重试");
                    super.onError(e);
                }

                public void onNext(StudentChangeInfoResponse response) {
                    super.onNext(response);
                    Gson gson = new GsonBuilder().create();
                    User user = (User) gson.fromJson(gson.toJson((Object) response.getData(), (Type) User.class), User.class);
                    user.setGradeName(EditUserInfoActivity.this.selectedGradeBean.getGradename());
                    user.setToken(SharepreferenceUtil.getToken());
                    if (EditUserInfoActivity.this.selectedSchoolBean != null) {
                        user.setSchoolid(EditUserInfoActivity.this.selectedSchoolBean.getSchoolid());
                    }
                    User unused = EditUserInfoActivity.this.userInfo = user;
                    GlobalVariable.setLogin(true, user);
                    Uri uri = LanucherContentProvider.PERSON_CONTENT_URI;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("realname", user.getRealname());
                    contentValues.put(StudyCPHelper.COLUMN_NAME_GRADE_CODE, user.getGradecode());
                    contentValues.put("areacode", user.getAreacode());
                    if (!TextUtils.isEmpty(user.getSchoolid())) {
                        contentValues.put("schoolid", user.getSchoolid());
                    }
                    contentValues.put("sex", user.getSex());
                    contentValues.put("token", user.getToken());
                    int count = EditUserInfoActivity.this.getContentResolver().update(uri, contentValues, "userid=?", new String[]{user.getUserid()});
                    if (count <= 0) {
                        LogAgent.onError(new UpdateContentProviderException("contentResolver.update error: " + count));
                    }
                    SharepreferenceUtil.getSharepferenceInstance(EditUserInfoActivity.this).setUserInfo(user);
                    GlobalUserInfoListener.getInstance().onUserInfoChanged(true, user);
                    EditUserInfoActivity.this.setResult(-1);
                    LauncherModel.getInstance().convertGradeCode(user.getGradecode());
                    EditUserInfoActivity.this.finish();
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
                    EditUserInfoActivity.this.dismissProgressDialog();
                }

                public void onError(Throwable e) {
                    EditUserInfoActivity.this.dismissProgressDialog();
                    CustomToast.showToast((Context) EditUserInfoActivity.this, "服务器异常，请稍后重试");
                    super.onError(e);
                }

                public void onNext(GetSchoolResponse response) {
                    super.onNext(response);
                    EditUserInfoActivity.this.dismissProgressDialog();
                    new SchoolPickerPopWin.Builder(EditUserInfoActivity.this, new SchoolPickerPopWin.OnSchoolPickedListener() {
                        public void onPick(School bean, String schoolName) {
                            School unused = EditUserInfoActivity.this.selectedSchoolBean = bean;
                            if (bean == null) {
                                EditUserInfoActivity.this.userSchoolValue.setText(schoolName);
                                String unused2 = EditUserInfoActivity.this.selectedSchoolName = schoolName;
                                EditUserInfoActivity.this.userSchoolValue.setTextColor(Color.parseColor("#333333"));
                                EditUserInfoActivity.this.restButtonStyle();
                                return;
                            }
                            String unused3 = EditUserInfoActivity.this.selectedSchoolName = "";
                            EditUserInfoActivity.this.userSchoolValue.setText(bean.getSchoolname());
                            EditUserInfoActivity.this.userSchoolValue.setTextColor(Color.parseColor("#333333"));
                            EditUserInfoActivity.this.restButtonStyle();
                            SharepreferenceUtil.getSharepferenceInstance(EditUserInfoActivity.this).setSchoolSelectPosition(bean.getSelectPosition());
                        }
                    }).setData(response.getData(), new StringBuffer(EditUserInfoActivity.this.selectedSchoolBean == null ? "" : EditUserInfoActivity.this.selectedSchoolBean.getSchoolname())).build().showPopWin(EditUserInfoActivity.this);
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
                EditUserInfoActivity.this.dismissProgressDialog();
            }

            public void onError(Throwable e) {
                EditUserInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(GetGradeResponse response) {
                super.onNext(response);
                EditUserInfoActivity.this.dismissProgressDialog();
                Log.e("GetGradeResponse-->", response.toString());
                new UserGradePopWin.Builder(EditUserInfoActivity.this, new UserGradePopWin.OnGradePickedListener() {
                    public void onPicked(GetGradeResponse.DataBean bean) {
                        GetGradeResponse.DataBean unused = EditUserInfoActivity.this.selectedGradeBean = bean;
                        EditUserInfoActivity.this.userGradeValue.setText(bean.getGradename());
                        EditUserInfoActivity.this.userGradeValue.setTextColor(Color.parseColor("#333333"));
                        EditUserInfoActivity.this.restButtonStyle();
                    }
                }).setData(response.getData()).setGrade(EditUserInfoActivity.this.selectedGradeBean).build().showPopWin(EditUserInfoActivity.this);
            }
        });
    }

    private void getPublisher(String grade, String subject) {
        LauncherHttpHelper.getLauncherService().getPublisher(grade, subject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetPublisherResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
                EditUserInfoActivity.this.dismissProgressDialog();
            }

            public void onError(Throwable e) {
                EditUserInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(GetPublisherResponse response) {
                super.onNext(response);
                EditUserInfoActivity.this.dismissProgressDialog();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if (System.currentTimeMillis() - this.exitTime > 2000) {
            Toast.makeText(getApplicationContext(), R.string.notice_back_one, 0).show();
            this.exitTime = System.currentTimeMillis();
            return;
        }
        finish();
    }

    public void showQuitDialog() {
        DialogUtil.showDilogToCancel(this, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharepreferenceUtil.getSharepferenceInstance(EditUserInfoActivity.this).Logout();
                Intent intent = new Intent(EditUserInfoActivity.this, LoginActivity.class);
                intent.addFlags(268435456);
                EditUserInfoActivity.this.startActivity(intent);
                SystemUtils.removeRecentTask((String) null, (ActivityManager) EditUserInfoActivity.this.getSystemService("activity"));
                EditUserInfoActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: private */
    public void restButtonStyle() {
        int length1 = this.userName.getText().toString().length();
        int length2 = this.userGradeValue.getText().toString().length();
        int length3 = this.userCityValue.getText().toString().length();
        int length4 = this.userBirthdayValue.getText().toString().length();
        int length5 = this.userSchoolValue.getText().toString().length();
        int length6 = this.userSexValue.getText().toString().length();
        String sex = this.userSexValue.getText().toString();
        if (length1 <= 0 || length2 <= 0 || length3 <= 0 || length4 <= 0 || length5 <= 0 || length6 <= 0 || sex.length() <= 0) {
            this.save.setEnabled(true);
            this.save.setBackgroundResource(R.drawable.login_circle_blue);
            return;
        }
        this.save.setEnabled(true);
        this.save.setBackgroundResource(R.drawable.login_circle_blue);
    }
}
