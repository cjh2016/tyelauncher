package com.boll.tyelauncher.ui.usercenter;

package com.toycloud.launcher.ui.usercenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.GsonBuilder;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.iflytek.easytrans.watchcore.deviceinfo.DeviceIdentifierUtils;
import com.iflytek.stats.StatsHelper;
import com.iflytek.stats.StatsKeys;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.SubjectListAdapter;
import com.toycloud.launcher.adapter.UserCenterAppsAdapter;
import com.toycloud.launcher.api.ApiProvider;
import com.toycloud.launcher.api.model.AppInfo;
import com.toycloud.launcher.api.model.Constants;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.GetGradeResponse;
import com.toycloud.launcher.api.response.ResponseAreaMessage;
import com.toycloud.launcher.api.response.SchoolBean;
import com.toycloud.launcher.api.response.StudyReportResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.api.service.bean.UpLoadImageFileRequest;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.helper.FeedbackHelper;
import com.toycloud.launcher.model.BookInfo;
import com.toycloud.launcher.model.UploadInfo;
import com.toycloud.launcher.provide.LanucherContentProvider;
import com.toycloud.launcher.ui.common.BindWXActivity;
import com.toycloud.launcher.ui.common.DeveloperSecretActivity;
import com.toycloud.launcher.ui.uploadheadicon.FileUtilcll;
import com.toycloud.launcher.util.DateUtil;
import com.toycloud.launcher.util.FileSizeUtil;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.PadInfoUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.WifiSupport;
import com.toycloud.launcher.view.EditableMenuTitleView;
import com.toycloud.launcher.view.SelectHeadIconPopWin;
import com.toycloud.launcher.view.SettingPopMenu;
import com.toycloud.launcher.webview.WebViewActivity;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.retrofit.BaseSubscriber;
import framework.hz.salmon.util.GlideImageLoader;
import framework.hz.salmon.util.NetworkUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class UserCenterActivity extends BaseFragmentActivity {
    private static final String[] NEEDED_PERMISSIONS = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
    private static final int PERMISSION_REQUEST_CODE = 0;
    public static final int SELECTPHOTO_REQUESTCODE = 10001;
    private static final String TAG = "UserCenterActivity";
    public static final int ZOOMPHOTO_REQUESTCAMERA = 1003;
    public static final int ZOOMPHOTO_REQUESTCODE = 10002;
    @BindView(2131689701)
    LinearLayout activityMain;
    /* access modifiers changed from: private */
    public List<AppInfo> appInfos = new ArrayList();
    /* access modifiers changed from: private */
    public String[] apps = {Constants.EHEAR_APP, Constants.ZXW_APP};
    /* access modifiers changed from: private */
    public UserCenterAppsAdapter appsAdapter;
    @BindView(2131689826)
    TextView birthday;
    @BindView(2131689829)
    EditableMenuTitleView bookInfoMenu;
    /* access modifiers changed from: private */
    public String cameraPath = "";
    @BindView(2131689827)
    TextView classNum;
    /* access modifiers changed from: private */
    public List<BookInfo.DataBean> data = new ArrayList();
    @BindView(2131689822)
    TextView gradeName;
    private boolean isFirstStartCome = false;
    @BindView(2131689661)
    ImageView iv_back;
    @BindView(2131689732)
    RecyclerView jcList;
    /* access modifiers changed from: private */
    public SubjectListAdapter mAdapter;
    /* access modifiers changed from: private */
    public SelectHeadIconPopWin mChangeAddressPopwindow;
    @BindView(2131689817)
    ImageView mDailyDotIV;
    private boolean mHasPermission;
    long[] mHints = new long[10];
    @BindView(2131689807)
    ImageView mImgDotSetting;
    private EditableMenuTitleView.OnClickEditListener mOnClickEditListener = new EditableMenuTitleView.OnClickEditListener() {
        public void onClickEdit(int menuId) {
            switch (menuId) {
                case R.id.menu_user_info:
                    UserCenterActivity.this.onClickEditUserInfo();
                    return;
                case R.id.menu_books:
                    UserCenterActivity.this.onClickEditBooksInfo();
                    return;
                default:
                    return;
            }
        }
    };
    private UserCenterInnerReceiver mReceiver;
    private StudyReportUpdateReceiver mReportUpdateReceiver;
    private Observer<StudyReportResponse> mRequestObserver = new Observer<StudyReportResponse>() {
        public void onComplete() {
            LogUtils.d(UserCenterActivity.TAG, "onCompleted: request msg complete");
            if (StudyDailyReportMsgManager.getInstance().hasNewStudyReportMsg()) {
                LogUtils.d(UserCenterActivity.TAG, "onCompleted: 请求完成，有新的日报信息，展示小红点");
                UserCenterActivity.this.mDailyDotIV.setVisibility(0);
                return;
            }
            LogUtils.d(UserCenterActivity.TAG, "onCompleted: 请求完成，没有信息日报更新信息，不展示小红点");
            UserCenterActivity.this.mDailyDotIV.setVisibility(8);
        }

        public void onError(Throwable e) {
            if (e != null) {
                Log.d(UserCenterActivity.TAG, "onError:" + e.getMessage());
            }
        }

        public void onSubscribe(Disposable d) {
        }

        public void onNext(StudyReportResponse reportResponse) {
            if (reportResponse.hasStudyReportUpdate()) {
                LogUtils.d(UserCenterActivity.TAG, "onNext: 请求日报更新接口完成，有新的日报信息");
                StudyDailyReportMsgManager.getInstance().updateRequestMsgTime(UserCenterActivity.this, System.currentTimeMillis());
                return;
            }
            LogUtils.d(UserCenterActivity.TAG, "onNext: 请求日报更新接口完成，没有日报更新信息");
        }
    };
    @BindView(2131689830)
    EditableMenuTitleView moreMenu;
    @BindView(2131689819)
    View msCourseIV;
    @BindView(2131689815)
    View msStudyDailyFL;
    @BindView(2131689818)
    View psStudyDailyIV;
    @BindView(2131689823)
    TextView region;
    @BindView(2131689813)
    EditableMenuTitleView reportMenu;
    @BindView(2131689814)
    View reportViewLL;
    @BindView(2131689831)
    RecyclerView rlvApps;
    @BindView(2131689824)
    TextView school;
    public int select_position = -1;
    /* access modifiers changed from: private */
    public GetGradeResponse.DataBean selectedGradeBean;
    @BindView(2131689806)
    ImageView setting;
    /* access modifiers changed from: private */
    public SettingPopMenu settingPopMenu;
    @BindView(2131689825)
    TextView sex;
    SharepreferenceUtil sharepferenceInstance;
    @BindView(2131689828)
    TextView studentNum;
    @BindView(2131689809)
    TextView tvParentManger;
    @BindView(2131689812)
    TextView tv_userphone;
    /* access modifiers changed from: private */
    public User userInfo;
    private UserInfoContentObserver userInfoContentObserver;
    @BindView(2131689820)
    EditableMenuTitleView userInfoMenu;
    @BindView(2131689707)
    TextView userName;
    @BindView(2131689811)
    TextView userNameTop;
    @BindView(2131689810)
    ImageView user_icon;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        ButterKnife.bind((Activity) this);
        this.sharepferenceInstance = SharepreferenceUtil.getSharepferenceInstance(this);
        this.userInfo = this.sharepferenceInstance.getUserInfo();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(9984);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        registerMyReceiver();
        registerReportUpdateReceiver();
        this.mAdapter = new SubjectListAdapter(this, this.data);
        this.mAdapter.openLoadAnimation(1);
        this.mAdapter.isFirstOnly(true);
        this.mAdapter.setNotDoAnimationCount(4);
        this.jcList.setHasFixedSize(true);
        this.jcList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
        this.jcList.setAdapter(this.mAdapter);
        this.appsAdapter = new UserCenterAppsAdapter(R.layout.layout_apps_item, this.appInfos);
        this.appsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (UserCenterActivity.this.appInfos != null && UserCenterActivity.this.appInfos.size() > 0) {
                    if (PackageUtils.isAvilible(UserCenterActivity.this.getApplicationContext(), ((AppInfo) UserCenterActivity.this.appInfos.get(position)).getPkgName())) {
                        Intent LaunchIntent = UserCenterActivity.this.getPackageManager().getLaunchIntentForPackage(((AppInfo) UserCenterActivity.this.appInfos.get(position)).getPkgName());
                        LaunchIntent.addFlags(268435456);
                        LaunchIntent.putExtra("token", SharepreferenceUtil.getToken());
                        UserCenterActivity.this.startActivity(LaunchIntent);
                        String pkgName = ((AppInfo) UserCenterActivity.this.appInfos.get(position)).getPkgName();
                        if (Constants.EHEAR_APP.equals(pkgName)) {
                            StatsHelper.userCenterClickedEnglish();
                        } else if (Constants.ZXW_APP.equals(pkgName)) {
                            StatsHelper.userCenterClickedMoreZXW();
                        }
                    } else {
                        Toast.makeText(UserCenterActivity.this, "未安装该应用", 0).show();
                    }
                }
            }
        });
        this.rlvApps.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
        this.rlvApps.setAdapter(this.appsAdapter);
        for (String appInfos2 : this.apps) {
            getAppInfos(appInfos2);
        }
        if (this.appInfos.size() > 0) {
            this.appsAdapter.notifyDataSetChanged();
            showllmore();
        } else {
            hidellmore();
        }
        registerMsgObserver();
        int messageCount = FeedbackHelper.getMessage(this);
        if (messageCount == 0) {
            if (this.mImgDotSetting.getVisibility() == 0) {
                this.mImgDotSetting.setVisibility(8);
            }
        } else if (messageCount > 0 && this.mImgDotSetting.getVisibility() == 8) {
            this.mImgDotSetting.setVisibility(0);
        }
        this.userInfoMenu.setOnEditClickListener(this.mOnClickEditListener);
        this.bookInfoMenu.setOnEditClickListener(this.mOnClickEditListener);
        this.isFirstStartCome = this.sharepferenceInstance.getIsFirstComeUserCenter();
        this.userInfo = this.sharepferenceInstance.getUserInfo();
        if (this.userInfo == null) {
            finish();
        } else if (!GradeUtil.isPrimarySchoolGrade(this.userInfo.getGradecode())) {
            refreshStudyReportDot();
        } else {
            LogUtils.d(TAG, "onCreate: 小学版本，不请求学习日报的状态");
        }
    }

    private void refreshStudyReportDot() {
        if (StudyDailyReportMsgManager.getInstance().hasShowStudyReportToday()) {
            LogUtils.d(TAG, "refreshStudyReportDot: 已经看过了今天的学习报告 不展示小红点了");
        } else if (StudyDailyReportMsgManager.getInstance().hasNewStudyReportMsg()) {
            LogUtils.d(TAG, "refreshStudyReportDot: 有今天的学习报告更新，展示小红点");
            this.mDailyDotIV.setVisibility(0);
        } else {
            LogUtils.d(TAG, "refreshStudyReportDot: 开始请求接口判断是否有学习报告的更新");
            StudyDailyReportMsgManager.getInstance().getStudyReportInfo(this, this.mRequestObserver);
        }
    }

    /* access modifiers changed from: private */
    public void getAppInfos(String pkgName) {
        if (PackageUtils.isAvilible(getApplicationContext(), pkgName)) {
            PackageInfo packageInfo = PackageUtils.getPackageInfo(getApplicationContext(), pkgName);
            AppInfo appInfo = new AppInfo();
            appInfo.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
            appInfo.setPkgName(packageInfo.packageName);
            appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
            this.appInfos.add(appInfo);
            Log.d(TAG, "appInfo:" + appInfo.toString());
        }
    }

    /* access modifiers changed from: private */
    public void showllmore() {
        if (this.moreMenu.getVisibility() != 0) {
            this.moreMenu.setVisibility(0);
        }
        if (this.rlvApps.getVisibility() != 0) {
            this.rlvApps.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void hidellmore() {
        if (this.moreMenu.getVisibility() == 0) {
            this.moreMenu.setVisibility(8);
        }
        if (this.rlvApps.getVisibility() == 0) {
            this.rlvApps.setVisibility(8);
        }
    }

    private void registerMsgObserver() {
        try {
            this.userInfoContentObserver = new UserInfoContentObserver(new Handler());
            getContentResolver().registerContentObserver(Uri.parse(FeedbackHelper.MESSAGE_URI), false, this.userInfoContentObserver);
        } catch (Exception e) {
        }
    }

    private void unregisterMsgObserver() {
        if (this.userInfoContentObserver != null) {
            getContentResolver().unregisterContentObserver(this.userInfoContentObserver);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.sharepferenceInstance.setIsFirstComeUserCenter(false);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LogAgentHelper.onActive();
        setRequestedOrientation(0);
        super.onResume();
        this.userInfo = this.sharepferenceInstance.getUserInfo();
        if (this.userInfo == null) {
            finish();
            return;
        }
        GlideImageLoader.getInstance().displayImageCircle(this, this.userInfo.getUsericonpath(), this.user_icon, R.drawable.pic_tx_default);
        if (!TextUtils.isEmpty(this.userInfo.getRealname())) {
            this.userName.setText(this.userInfo.getRealname());
            this.userNameTop.setText(this.userInfo.getRealname());
        }
        if (!TextUtils.isEmpty(this.userInfo.getUsername())) {
            this.tv_userphone.setText(this.userInfo.getSecurityUserPhone());
        } else {
            this.tv_userphone.setText("");
        }
        initGrade();
        if (GradeUtil.isPrimarySchoolGrade(this.userInfo.getGradecode())) {
            this.msStudyDailyFL.setVisibility(8);
            this.msCourseIV.setVisibility(4);
            this.psStudyDailyIV.setVisibility(0);
        } else {
            this.msStudyDailyFL.setVisibility(0);
            this.msCourseIV.setVisibility(0);
            this.psStudyDailyIV.setVisibility(8);
        }
        initSchool();
        if (!TextUtils.isEmpty(this.userInfo.getBirthday())) {
            this.birthday.setText(DateUtil.getDateFromMillis(new StringBuffer(), this.userInfo.getBirthday()));
        } else {
            this.birthday.setText("/");
        }
        if (!TextUtils.isEmpty(this.userInfo.getSex())) {
            this.sex.setText(this.userInfo.getSex());
        } else {
            this.sex.setText("/");
        }
        if (!TextUtils.isEmpty(this.userInfo.getAreacode())) {
            initRegion();
        }
        if (!TextUtils.isEmpty(this.userInfo.getClassName())) {
            this.classNum.setText(this.userInfo.getClassName());
        } else {
            this.classNum.setText("/");
        }
        if (!TextUtils.isEmpty(this.userInfo.getStudentNumber())) {
            this.studentNum.setText(this.userInfo.getStudentNumber());
        } else {
            this.studentNum.setText("/");
        }
    }

    public boolean handleMessage(Message message) {
        return false;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && this.isFirstStartCome) {
            showParentMangerPopu();
            this.isFirstStartCome = false;
            this.sharepferenceInstance.setIsFirstComeUserCenter(false);
        }
    }

    public void onClick(View view) {
    }

    private boolean checkPermission() {
        for (String permission : NEEDED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != 0) {
                return false;
            }
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, 0);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllPermission = true;
        if (requestCode == 0) {
            int length = grantResults.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (grantResults[i] != 0) {
                    hasAllPermission = false;
                    break;
                } else {
                    i++;
                }
            }
            if (hasAllPermission) {
                this.mHasPermission = true;
                if (!WifiSupport.isOpenWifi(this) || !this.mHasPermission) {
                    Toast.makeText(this, "获取本地图片权限失败", 0).show();
                    return;
                }
                this.mChangeAddressPopwindow = new SelectHeadIconPopWin(this, new SelectHeadIconPopWin.onWhichHeadIconSelect() {
                    public void onHeadIconSelect(int position, boolean isSelectPhoto, int whichposition) {
                        if (whichposition == 5) {
                            UserCenterActivity.this.select_position = -1;
                            Intent intent = new Intent("android.intent.action.PICK", (Uri) null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            UserCenterActivity.this.startActivityForResult(intent, 10001);
                        } else if (whichposition == 4) {
                            UserCenterActivity.this.select_position = -1;
                            String unused = UserCenterActivity.this.cameraPath = System.currentTimeMillis() + ".jpg";
                            Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent2.addFlags(268435456);
                            Uri photoURI = Uri.parse(new File(Environment.getExternalStorageDirectory(), UserCenterActivity.this.cameraPath).getPath());
                            intent2.addFlags(3);
                            intent2.putExtra("output", photoURI);
                            intent2.putExtra("android.intent.extras.CAMERA_FACING", 1);
                            intent2.putExtra("autofocus", true);
                            intent2.putExtra("fullScreen", false);
                            intent2.putExtra("showActionIcons", false);
                            UserCenterActivity.this.startActivityForResult(intent2, 1003);
                        } else {
                            UserCenterActivity.this.select_position = whichposition;
                            new SaveDrawableFile().execute(new Integer[]{Integer.valueOf(position)});
                            UserCenterActivity.this.user_icon.setImageResource(position);
                        }
                    }
                });
                this.mChangeAddressPopwindow.showAtLocation(this.user_icon, 49, 0, 0);
                return;
            }
            this.mHasPermission = false;
            Toast.makeText(this, "获取本地图片权限失败", 0).show();
        }
    }

    public void showParentMangerPopu() {
        Intent intent = new Intent(this, BindWXActivity.class);
        intent.putExtra(BindWXActivity.EXTRA_BIND_WX_ACT_START_FROM, 0);
        startActivity(intent);
    }

    @OnClick({2131689806, 2131689810, 2131689661, 2131689811, 2131689809, 2131689816, 2131689819})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                return;
            case R.id.setting:
                this.settingPopMenu = new SettingPopMenu.Builder(this).build();
                this.settingPopMenu.showPopWin(this.setting);
                Map<String, String> map = new HashMap<>();
                if (this.mImgDotSetting.getVisibility() == 0) {
                    map.put(StatsKeys.K_D_MSG_STATE, "1");
                } else {
                    map.put(StatsKeys.K_D_MSG_STATE, "2");
                }
                map.put(StatsKeys.K_D_MSG_STATE, "2");
                StatsHelper.userCenterClickedMore(map);
                return;
            case R.id.tv_parent_manger:
                showParentMangerPopu();
                StatsHelper.userCenterClickedJZGL();
                return;
            case R.id.user_icon:
                this.mHasPermission = checkPermission();
                if (!this.mHasPermission) {
                    requestPermission();
                    return;
                }
                StatsHelper.userCenterClickedUserIcon();
                this.mChangeAddressPopwindow = new SelectHeadIconPopWin(this, new SelectHeadIconPopWin.onWhichHeadIconSelect() {
                    public void onHeadIconSelect(int position, boolean isSelectPhoto, int whichposition) {
                        Log.e("path--->", position + "");
                        if (whichposition == 5) {
                            UserCenterActivity.this.select_position = -1;
                            Intent intent = new Intent("android.intent.action.PICK", (Uri) null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            UserCenterActivity.this.startActivityForResult(intent, 10001);
                        } else if (whichposition == 4) {
                            UserCenterActivity.this.select_position = -1;
                            String unused = UserCenterActivity.this.cameraPath = Calendar.getInstance().getTimeInMillis() + ".jpg";
                            File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(), UserCenterActivity.this.cameraPath);
                            if (tempFile != null) {
                                Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                                intent2.putExtra("output", Uri.fromFile(tempFile));
                                if (Build.VERSION.SDK_INT >= 24) {
                                    try {
                                        StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke((Object) null, new Object[0]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                intent2.putExtra("autofocus", true);
                                intent2.putExtra("fullScreen", false);
                                intent2.putExtra("showActionIcons", false);
                                UserCenterActivity.this.startActivityForResult(intent2, 1003);
                                return;
                            }
                            Toast.makeText(UserCenterActivity.this, "打开照相机失败", 0).show();
                        } else {
                            UserCenterActivity.this.select_position = whichposition;
                            new SaveDrawableFile().execute(new Integer[]{Integer.valueOf(position)});
                            UserCenterActivity.this.user_icon.setImageResource(position);
                        }
                    }
                });
                this.mChangeAddressPopwindow.showAtLocation(this.user_icon, 49, 0, 0);
                return;
            case R.id.user_name_top:
                if (isClickThreeTime()) {
                    startActivity(new Intent(this, DeveloperSecretActivity.class));
                    return;
                }
                return;
            case R.id.study_daily_tv:
                if (this.mDailyDotIV.getVisibility() == 0) {
                    this.mDailyDotIV.setVisibility(8);
                }
                showWebViewActivity("http://k12-api.openspeech.cn/pages/studyreport/index.html#/dailyReport?sn=" + DeviceIdentifierUtils.getSn(this), StudyDailyReportMsgManager.getInstance().hasNewStudyReportMsg());
                return;
            case R.id.study_report_tv:
                showWebViewActivity("http://k12-api.openspeech.cn/pages/studyreport/index.html#/course?sn=" + DeviceIdentifierUtils.getSn(this), false);
                return;
            default:
                return;
        }
    }

    private void showWebViewActivity(String url, boolean updateShowReportTime) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("key_webview_url", url);
        intent.putExtra(StudyDailyReportMsgManager.EXTRA_INTENT_TO_STUDY_DAILY_REPORT, updateShowReportTime);
        startActivity(intent);
    }

    private void initSchool() {
        if (TextUtils.isEmpty(this.userInfo.getSchoolid())) {
            this.school.setText("/");
        } else {
            LauncherHttpHelper.getLauncherService().getSchoolById(this.userInfo.getSchoolid()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<SchoolBean>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                    UserCenterActivity.this.dismissProgressDialog();
                }

                public void onError(Throwable e) {
                    UserCenterActivity.this.dismissProgressDialog();
                    super.onError(e);
                    String schoolName = UserCenterActivity.this.sharepferenceInstance.getSchoolName();
                    if (!TextUtils.isEmpty(schoolName)) {
                        UserCenterActivity.this.school.setText(schoolName);
                    } else {
                        UserCenterActivity.this.school.setText("/");
                    }
                }

                public void onNext(SchoolBean response) {
                    super.onNext(response);
                    UserCenterActivity.this.dismissProgressDialog();
                    String name = response.getData().getSchoolname();
                    if (!TextUtils.isEmpty(name)) {
                        UserCenterActivity.this.school.setText(name);
                        UserCenterActivity.this.sharepferenceInstance.setSchoolName(name);
                        return;
                    }
                    UserCenterActivity.this.school.setText("/");
                }
            });
        }
    }

    public static void goToSleep(Context context) {
    }

    private void initGrade() {
        Log.e("padInfo--->", new PadInfoUtil(this).getSnCode() + ":" + new PadInfoUtil(this).getSystemVersionCode() + ":" + new PadInfoUtil(this).getIPAddress());
        LauncherHttpHelper.getLauncherService().getGradesByPhase(TextUtils.join(",", new String[]{"03", "04", "05"})).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetGradeResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                UserCenterActivity.this.dismissProgressDialog();
                super.onError(e);
                if (UserCenterActivity.this.userInfo != null) {
                    UserCenterActivity.this.gradeName.setText(GradeUtil.getGradeName(UserCenterActivity.this.userInfo.getGradecode()));
                }
            }

            public void onNext(GetGradeResponse response) {
                super.onNext(response);
                for (int i = 0; i < response.getData().size(); i++) {
                    if (response.getData().get(i).getGradecode().equals(UserCenterActivity.this.userInfo.getGradecode())) {
                        GetGradeResponse.DataBean unused = UserCenterActivity.this.selectedGradeBean = response.getData().get(i);
                        UserCenterActivity.this.gradeName.setText(UserCenterActivity.this.selectedGradeBean.getGradename());
                    }
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data2) {
        switch (requestCode) {
            case PointerIconCompat.TYPE_CONTEXT_MENU:
            case PointerIconCompat.TYPE_HAND:
                if (resultCode == -1) {
                    this.userInfo = this.sharepferenceInstance.getUserInfo();
                    getSubject();
                    SharepreferenceUtil.getSharepferenceInstance(this).setUserProfileChangedForEts(true);
                    SharepreferenceUtil.getSharepferenceInstance(this).setUserProfileChangedForEtsPri(true);
                    break;
                }
                break;
            case 1003:
                if (resultCode == -1) {
                    Uri photoURI = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(), this.cameraPath));
                    if (photoURI != null) {
                        grantUriPermission("com.android.camera.action.CROP", photoURI, 3);
                        startPhotoZoom(photoURI);
                        break;
                    } else {
                        return;
                    }
                }
                break;
            case 10001:
                if (data2 != null && data2.getData() != null) {
                    startPhotoZoom(data2.getData());
                    break;
                } else {
                    return;
                }
                break;
            case ZOOMPHOTO_REQUESTCODE /*10002*/:
                if (data2 != null) {
                    if (this.mChangeAddressPopwindow != null && this.mChangeAddressPopwindow.isShowing()) {
                        this.mChangeAddressPopwindow.dissMissPopWin();
                    }
                    setPicToView(data2);
                    break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data2);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            String urlpath = FileUtilcll.saveFile(this, System.currentTimeMillis() + ".jpg", (Bitmap) extras.getParcelable("data"));
            System.out.println("urlpath--->" + urlpath);
            upLoadImageIcon(urlpath);
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ZOOMPHOTO_REQUESTCODE);
    }

    private void getSubject() {
        if (!TextUtils.isEmpty(this.userInfo.getGradecode())) {
            LauncherHttpHelper.getLauncherService().getBookInfo(this.userInfo.getGradecode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<BookInfo>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                }

                public void onError(Throwable e) {
                    super.onError(e);
                    UserCenterActivity.this.dismissProgressDialog();
                    String subJectString = UserCenterActivity.this.sharepferenceInstance.getSubJectString();
                    if (!TextUtils.isEmpty(subJectString)) {
                        UserCenterActivity.this.data.clear();
                        UserCenterActivity.this.data.addAll(((BookInfo) new GsonBuilder().create().fromJson(subJectString, BookInfo.class)).getData());
                        UserCenterActivity.this.mAdapter.notifyDataSetChanged();
                    }
                }

                public void onNext(BookInfo response) {
                    super.onNext(response);
                    Log.e("BookInfo--->", response.toString());
                    UserCenterActivity.this.data.clear();
                    UserCenterActivity.this.sharepferenceInstance.setSubjectString(new GsonBuilder().create().toJson((Object) response));
                    UserCenterActivity.this.data.addAll(response.getData());
                    UserCenterActivity.this.mAdapter.notifyDataSetChanged();
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
                UserCenterActivity.this.dismissProgressDialog();
                super.onError(e);
                String regionName = UserCenterActivity.this.sharepferenceInstance.getRegionName();
                if (!TextUtils.isEmpty(regionName)) {
                    UserCenterActivity.this.region.setText(regionName);
                }
            }

            public void onNext(ResponseAreaMessage response) {
                String s;
                super.onNext(response);
                ResponseAreaMessage.DataBean dataBean = response.getData();
                String provinceName = dataBean.getProvinceBean().getName();
                if (!TextUtils.isEmpty(provinceName)) {
                    if (provinceName.contains("天津市") || provinceName.contains("北京市") || provinceName.contains("重庆市") || provinceName.contains("上海市")) {
                        s = response.getData().getProvinceBean().getName();
                    } else {
                        s = dataBean.getProvinceBean().getName();
                        ResponseAreaMessage.DataBean.CityBeanBean cityBeanBean = dataBean.getCityBean();
                        if (cityBeanBean != null) {
                            s = s + cityBeanBean.getName();
                        }
                    }
                    UserCenterActivity.this.sharepferenceInstance.setRegion(s);
                    UserCenterActivity.this.region.setText(s);
                }
            }
        });
    }

    class SaveDrawableFile extends AsyncTask<Integer, Integer, String> {
        SaveDrawableFile() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Integer... integers) {
            return FileUtilcll.saveFile(UserCenterActivity.this, System.currentTimeMillis() + ".jpg", BitmapFactory.decodeResource(UserCenterActivity.this.getResources(), integers[0].intValue()));
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String s) {
            Log.e("path--->", s + ":::");
            UserCenterActivity.this.upLoadImageIcon(s);
            super.onPostExecute(s);
        }
    }

    /* access modifiers changed from: private */
    public void upLoadImageIcon(String urlpath) {
        if (TextUtils.isEmpty(urlpath)) {
            Toast.makeText(this, "请选择图片", 0).show();
            return;
        }
        boolean fileOrFilesSize = FileSizeUtil.getFileOrFilesSize(urlpath);
        if (!fileOrFilesSize) {
            Toast.makeText(this, "头像文件大小不能超过800KB", 1).show();
            return;
        }
        Log.e("fileOrFilesSize--->", fileOrFilesSize + ":");
        final File file = new File(urlpath);
        UpLoadImageFileRequest request = new UpLoadImageFileRequest();
        request.type = "UPLOAD";
        request.imgName = file.getName();
        showProgressDialog();
        LauncherHttpHelper.getLauncherService().upLoadHeadIcon(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<UploadInfo>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                UserCenterActivity.this.dismissProgressDialog();
                if (NetworkUtil.isNetworkAvailable(UserCenterActivity.this)) {
                    Toast.makeText(this.mContext, "上传失败", 0).show();
                }
                super.onError(e);
            }

            public void onNext(UploadInfo response) {
                super.onNext(response);
                Log.e("UploadInfo-->", response.toString());
                if (response == null || response.getData() == null) {
                    Toast.makeText(UserCenterActivity.this, "数据异常", 1).show();
                } else {
                    UserCenterActivity.this.uploadImageIconToCloud(response, file);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void uploadImageIconToCloud(final UploadInfo response2, File mFile) {
        UploadInfo.DataBean data2 = response2.getData();
        ApiProvider.getInstance("http://" + data2.getDomain() + "/").mUploadLogService.saveFileToCloud(data2.getDomain(), data2.getContainerName(), mFile.getName(), data2.getToken(), data2.getExpires(), RequestBody.create(MediaType.parse(org.androidannotations.api.rest.MediaType.IMAGE_JPEG), mFile)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ResponseBody>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
                UserCenterActivity.this.dismissProgressDialog();
            }

            public void onError(Throwable e) {
                UserCenterActivity.this.dismissProgressDialog();
                if (NetworkUtil.isNetworkAvailable(UserCenterActivity.this)) {
                    Toast.makeText(this.mContext, "上传失败", 0).show();
                }
                super.onError(e);
            }

            public void onNext(ResponseBody response) {
                super.onNext(response);
                UserCenterActivity.this.dismissProgressDialog();
                try {
                    User userInfo = UserCenterActivity.this.sharepferenceInstance.getUserInfo();
                    String userIconPath = response2.getData().getIconPath();
                    userInfo.setUsericonpath(userIconPath);
                    GlobalVariable.LOGIN_USER = userInfo;
                    GlobalVariable.setLogin(true, userInfo);
                    UserCenterActivity.this.sharepferenceInstance.setUserInfo(userInfo);
                    GlideImageLoader.getInstance().displayImageCircle(UserCenterActivity.this, userIconPath, UserCenterActivity.this.user_icon, R.drawable.pic_tx_default);
                    Uri uri = LanucherContentProvider.PERSON_CONTENT_URI;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("headurl", userIconPath);
                    UserCenterActivity.this.getContentResolver().update(uri, contentValues, "userid=?", new String[]{userInfo.getUserid()});
                    if (UserCenterActivity.this.mChangeAddressPopwindow != null && UserCenterActivity.this.mChangeAddressPopwindow.isShowing()) {
                        UserCenterActivity.this.mChangeAddressPopwindow.dissMissPopWin();
                    }
                    Log.e("ResponseBody--->", response.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        setResult(-1);
        this.sharepferenceInstance.setIsFirstComeUserCenter(false);
        unregisterMsgObserver();
        unregisterMyReceiver();
        unregisterReportUpdateReceiver();
        if (this.settingPopMenu != null && this.settingPopMenu.isShowing()) {
            this.settingPopMenu.dismiss();
        }
    }

    public boolean isClickThreeTime() {
        System.arraycopy(this.mHints, 1, this.mHints, 0, this.mHints.length - 1);
        this.mHints[this.mHints.length - 1] = SystemClock.uptimeMillis();
        if (SystemClock.uptimeMillis() - this.mHints[0] <= 2000) {
            return true;
        }
        return false;
    }

    class UserInfoContentObserver extends ContentObserver {
        public UserInfoContentObserver(Handler handler) {
            super(handler);
        }

        public boolean deliverSelfNotifications() {
            Log.d(UserCenterActivity.TAG, "deliverSelfNotifications");
            return true;
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(UserCenterActivity.TAG, "onChange");
            int msgCount = FeedbackHelper.getMessage(UserCenterActivity.this.getApplicationContext());
            if (msgCount > 0) {
                if (UserCenterActivity.this.mImgDotSetting != null && UserCenterActivity.this.mImgDotSetting.getVisibility() == 8) {
                    UserCenterActivity.this.mImgDotSetting.setVisibility(0);
                }
                if (UserCenterActivity.this.settingPopMenu != null && UserCenterActivity.this.settingPopMenu.isShowing()) {
                    UserCenterActivity.this.settingPopMenu.refreshDot();
                }
            } else if (msgCount == 0) {
                if (UserCenterActivity.this.settingPopMenu != null && UserCenterActivity.this.settingPopMenu.isShowing()) {
                    UserCenterActivity.this.settingPopMenu.refreshDot();
                }
                if (UserCenterActivity.this.mImgDotSetting != null && UserCenterActivity.this.mImgDotSetting.getVisibility() == 0) {
                    UserCenterActivity.this.mImgDotSetting.setVisibility(8);
                }
            }
        }
    }

    class UserCenterInnerReceiver extends BroadcastReceiver {
        UserCenterInnerReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(UserCenterActivity.TAG, "intent:" + action);
            if (TextUtils.equals(action, "android.intent.action.PACKAGE_ADDED")) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Log.d(UserCenterActivity.TAG, "packageName:" + packageName);
                refreshView(packageName);
            } else if (!TextUtils.equals(action, "android.intent.action.PACKAGE_REPLACED") && TextUtils.equals(action, "android.intent.action.PACKAGE_REMOVED")) {
                String packageName2 = intent.getData().getSchemeSpecificPart();
                Log.d(UserCenterActivity.TAG, "packageName:" + packageName2);
                refreshView(packageName2);
            }
        }

        private void refreshView(String packageName) {
            if ((Constants.EHEAR_APP.equals(packageName) || Constants.ZXW_APP.equals(packageName)) && UserCenterActivity.this.appInfos != null) {
                Log.d(UserCenterActivity.TAG, "refreshView---------进行数据刷新");
                UserCenterActivity.this.appInfos.clear();
                for (String access$2100 : UserCenterActivity.this.apps) {
                    UserCenterActivity.this.getAppInfos(access$2100);
                }
                if (UserCenterActivity.this.appInfos.size() > 0) {
                    UserCenterActivity.this.appsAdapter.notifyDataSetChanged();
                    UserCenterActivity.this.showllmore();
                    return;
                }
                UserCenterActivity.this.hidellmore();
            }
        }
    }

    private void registerMyReceiver() {
        this.mReceiver = new UserCenterInnerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addDataScheme("package");
        registerReceiver(this.mReceiver, intentFilter);
    }

    private void unregisterMyReceiver() {
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    private void registerReportUpdateReceiver() {
        this.mReportUpdateReceiver = new StudyReportUpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_STUDY_REPORT_UPDATE);
        filter.addAction(Constants.ACTION_STUDY_REPORT_SHOWED_TIME_UPDATED);
        registerReceiver(this.mReportUpdateReceiver, filter);
    }

    private void unregisterReportUpdateReceiver() {
        if (this.mReportUpdateReceiver != null) {
            unregisterReceiver(this.mReportUpdateReceiver);
            this.mReportUpdateReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public void onClickEditUserInfo() {
        if (!NoDoubleClickUtils.isDoubleClick()) {
            StatsHelper.userCenterClickedEdit();
            if (!NetworkUtil.isNetworkAvailable(this)) {
                Toast.makeText(this, "网络连接错误,请检查网络", 0).show();
                return;
            }
            Intent intent2 = new Intent(getApplicationContext(), EditUserInfoActivity.class);
            intent2.putExtra("whichposition", 1);
            startActivityForResult(intent2, PointerIconCompat.TYPE_CONTEXT_MENU);
        }
    }

    /* access modifiers changed from: private */
    public void onClickEditBooksInfo() {
        if (!NoDoubleClickUtils.isDoubleClick()) {
            StatsHelper.userCenterClickedJCXXBJ();
            if (!NetworkUtil.isNetworkAvailable(this)) {
                Toast.makeText(this, "网络连接错误,请检查网络", 0).show();
                return;
            }
            Intent intent = new Intent(getApplicationContext(), EditUserInfoActivity.class);
            intent.putExtra("whichposition", 2);
            startActivityForResult(intent, PointerIconCompat.TYPE_HAND);
        }
    }

    private class StudyReportUpdateReceiver extends BroadcastReceiver {
        private StudyReportUpdateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            int i = 0;
            String action = intent.getAction();
            if (TextUtils.equals(action, Constants.ACTION_STUDY_REPORT_UPDATE)) {
                if (StudyDailyReportMsgManager.getInstance().hasShowStudyReportToday()) {
                    LogUtils.d(UserCenterActivity.TAG, "onReceive: 今天已经展示过了学习日报的小红点222");
                    return;
                }
                if (UserCenterActivity.this.mDailyDotIV.getVisibility() != 0) {
                    UserCenterActivity.this.mDailyDotIV.setVisibility(0);
                }
                StudyDailyReportMsgManager.getInstance().updateReceiveMsgTime(context, System.currentTimeMillis());
            } else if (TextUtils.equals(action, Constants.ACTION_STUDY_REPORT_SHOWED_TIME_UPDATED)) {
                LogUtils.d(UserCenterActivity.TAG, "onReceive: 展示今日学习报告的时间更新了");
                ImageView imageView = UserCenterActivity.this.mDailyDotIV;
                if (StudyDailyReportMsgManager.getInstance().hasShowStudyReportToday()) {
                    i = 8;
                }
                imageView.setVisibility(i);
            }
        }
    }
}