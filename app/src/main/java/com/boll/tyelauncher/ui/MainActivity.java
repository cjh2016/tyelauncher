package com.boll.tyelauncher.ui;

package com.toycloud.launcher.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.anarchy.classify.simple.AppInfo;
import com.iflytek.cbg.aistudy.biz.grade.GradeHelper;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.iflytek.stats.StatsHelper;
import com.iflytek.stats.StatsKeys;
import com.iflytek.utils.LauncherExecutors;
import com.iflytek.utils.LogAgentHelper;
import com.orhanobut.logger.Logger;
import com.rd.PageIndicatorView;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.LauncherAdapter;
import com.toycloud.launcher.api.MessagePollingManager;
import com.toycloud.launcher.api.model.Constants;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.biz.evalhint.EvalHintHelper;
import com.toycloud.launcher.biz.globalconfig.GlobalConfigManager;
import com.toycloud.launcher.checkappversion.MyService_CheckVersion;
import com.toycloud.launcher.checkappversion.MyService_OnScreen;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.debug.MainDebugger;
import com.toycloud.launcher.helper.FeedbackHelper;
import com.toycloud.launcher.helper.ReciteBookIconHelper;
import com.toycloud.launcher.helper.UserInfoHelper;
import com.toycloud.launcher.holder.BaseHolder;
import com.toycloud.launcher.holder.CardPrimaryChinese;
import com.toycloud.launcher.holder.CardPrimaryEnglish;
import com.toycloud.launcher.holder.CardPrimaryMath;
import com.toycloud.launcher.holder.Launcher_3rdAPP_ViewHold;
import com.toycloud.launcher.holder.Launcher_ETS_ViewHolder;
import com.toycloud.launcher.holder.Launcher_EnglishPage_ViewHolder;
import com.toycloud.launcher.holder.Launcher_FirstPage_ViewHolder;
import com.toycloud.launcher.holder.Launcher_Third_ViewHolder;
import com.toycloud.launcher.holder.Launcher_Welcome_ViewHolder;
import com.toycloud.launcher.holder.Launcher_WiFiConnect_ViewHolder;
import com.toycloud.launcher.model.ActiveModel;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.EtsConstant;
import com.toycloud.launcher.model.GetAllApps;
import com.toycloud.launcher.model.launcher.LauncherModel;
import com.toycloud.launcher.model.launcher.RemoteController;
import com.toycloud.launcher.model.launcher.impl.AppInstalReporter;
import com.toycloud.launcher.model.launcher.impl.LauncherAppPosMgr;
import com.toycloud.launcher.model.launcher.impl.LauncherAppViewHolderMgr;
import com.toycloud.launcher.model.launcher.impl.LauncherModelView;
import com.toycloud.launcher.model.launcher.interfaces.ILauncherPagersView;
import com.toycloud.launcher.model.launcher.interfaces.ISystemPMHooker;
import com.toycloud.launcher.myinterface.WifiConnectInterface;
import com.toycloud.launcher.provide.LanucherContentProvider;
import com.toycloud.launcher.receiver.ScreenBroadcastReceiver;
import com.toycloud.launcher.ui.login.LoginActivity;
import com.toycloud.launcher.ui.regist.CompleteUserInfoActivity;
import com.toycloud.launcher.ui.usercenter.EditUserInfoActivity;
import com.toycloud.launcher.ui.usercenter.StudyDailyReportMsgManager;
import com.toycloud.launcher.ui.usercenter.UserCenterActivity;
import com.toycloud.launcher.util.CustomToast;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.LogSaveManager_Util;
import com.toycloud.launcher.util.Logging;
import com.toycloud.launcher.util.MyAsyncTaskUtil;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.PadInfoUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.ToastUtils;
import com.toycloud.launcher.view.ParallaxViewPager;
import framework.hz.salmon.retrofit.BaseSubscriber;
import framework.hz.salmon.util.GlideImageLoader;
import framework.hz.salmon.util.NetworkUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseMainActivity implements Launcher_Welcome_ViewHolder.MainClickListener, WifiConnectInterface {
    private static final String ACTION_LAUNCHER_RESUME = "com.aistudy.launcher.onResume";
    private static final String TAG = "MainActivity";
    public static MainActivity mActivity;
    @BindView(2131689701)
    FrameLayout activityMain;
    GetAllApps appUtil;
    @BindView(2131689759)
    LinearLayout bottomLayout;
    @BindView(2131689761)
    LinearLayout bottomMenu;
    @BindView(2131689772)
    ImageView head;
    /* access modifiers changed from: private */
    public Boolean homeClickFlag = true;
    @BindView(2131689773)
    ImageView imgDot;
    boolean isFirstStart;
    /* access modifiers changed from: private */
    public Launcher_EnglishPage_ViewHolder launcher_EnglishPate_ViewHolder;
    /* access modifiers changed from: private */
    public BaseHolder launcher_FirstPate_ViewHolder;
    /* access modifiers changed from: private */
    public BaseHolder launcher_ThreePate_ViewHolder;
    /* access modifiers changed from: private */
    public Launcher_ETS_ViewHolder launcher_ets_viewHolder;
    /* access modifiers changed from: private */
    public Launcher_WiFiConnect_ViewHolder launcher_wiFiConnect_viewHolder;
    private AppLoginReceiver mAppLoginReceiver;
    private AppInstallRecevier mAppRecevier;
    /* access modifiers changed from: private */
    public List<BaseHolder> mCardHolders;
    /* access modifiers changed from: private */
    public int mCurrentPosition = 0;
    long[] mHints = new long[10];
    private HomeWatcherReceiver mHomeWatcherReceiver = null;
    /* access modifiers changed from: private */
    public boolean mIsResumed = false;
    /* access modifiers changed from: private */
    public LauncherAdapter mLauncherAdapter;
    /* access modifiers changed from: private */
    public LauncherModel mLauncherModel;
    /* access modifiers changed from: private */
    public LauncherModelView mLauncherView;
    /* access modifiers changed from: private */
    public int mLayoutId = 0;
    private StudyReportUpdateReceiver mReportUpdateReceiver;
    @BindView(2131689758)
    public ParallaxViewPager mViewPager;
    private MessageContentObserver messageContentObserver;
    @BindView(2131689760)
    PageIndicatorView pageIndicatorView;
    private ScreenBroadcastReceiver screenBroadcastReceiver;
    /* access modifiers changed from: private */
    public SharepreferenceUtil sharepferenceInstance;
    @BindView(2131689777)
    ImageView studyReportUpdateIV;
    @BindView(2131689770)
    RelativeLayout userCenter;
    @BindView(2131689776)
    TextView userGrade;
    private User userInfo;
    @BindView(2131689775)
    TextView userName;
    private UserObserver userObserver;
    private WifiBroadcastReceiver wifiReceiver;

    private void startModel() {
        RemoteController remoteController = RemoteController.getInstance(this);
        LauncherAppPosMgr posMgr = new LauncherAppPosMgr(this);
        LauncherAppViewHolderMgr factory = new LauncherAppViewHolderMgr(this, posMgr);
        this.mLauncherModel = LauncherModel.getInstance();
        AppInstalReporter reporter = new AppInstalReporter(this, this.mLauncherModel);
        this.mLauncherModel.init(this, new ISystemPMHooker() {
            public List<AppInfo> onQueryAll(String gradePeriod, List<AppInfo> appInfos) {
                AppInfo appInfo;
                try {
                    if (MainActivity.this.getPackageManager().getPackageInfo("com.ets100.study", 64) != null) {
                        Logging.d(MainActivity.TAG, "loadAllAppsByBatch() ets installed, add phonetic study icon");
                        AppInfo appInfo2 = new AppInfo(EtsConstant.ETS_PHONETIC_VIRTUAL_PACKAGE, MainActivity.this.getString(R.string.ets_phonetic), MainActivity.this.getDrawable(R.drawable.ets_phonetic_study_logo));
                        appInfo2.setSystemApp(true);
                        appInfos.add(appInfo2);
                    } else {
                        Logging.d(MainActivity.TAG, "loadAllAppsByBatch() ets not installed, don't add phonetic study icon");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                }
                boolean needShowReciteBookIcon = ReciteBookIconHelper.isNeedShowReciteBookIcon();
                Logging.d(MainActivity.TAG, "onQueryAll() needShowReciteBookIcon " + needShowReciteBookIcon);
                if (needShowReciteBookIcon && (appInfo = MainActivity.this.getReciteBookAppInfo()) != null) {
                    appInfos.add(appInfo);
                }
                return appInfos;
            }

            public boolean shouldIgnoreApp(String gradePeriod, String pkgName) {
                if (AppContants.RECITE_BOOK.equals(pkgName)) {
                    return true;
                }
                return false;
            }
        });
        this.mLauncherModel.addPrepareWork(posMgr);
        this.mLauncherModel.addPrepareWork(remoteController);
        this.mLauncherModel.setRemoteController(remoteController);
        this.mLauncherModel.addPrepareWork(reporter);
        this.mLauncherModel.setAppInstalReporter(reporter);
        this.mLauncherView = new LauncherModelView(factory, posMgr, new AppListViewHolderManager(), this.isFirstStart);
        this.mLauncherModel.setLauncherView(this.mLauncherView);
        String gradePeriod = GradeHelper.GRADE_SEGMENT_NOT_LOGIN;
        if (this.userInfo != null) {
            String gradeCode = this.userInfo.getGradecode();
            if (!TextUtils.isEmpty(gradeCode)) {
                gradePeriod = GradeHelper.getGradeSegment(gradeCode);
            }
        }
        this.mLauncherModel.start(gradePeriod);
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long time = System.currentTimeMillis();
        try {
            mActivity = this;
            initVariable();
            startModel();
            if (this.isFirstStart) {
                setContentView(new Launcher_Welcome_ViewHolder(this, this).getRootView());
            }
            registWifiChangeReceiver();
            startBackgroundServices();
            registReceiver();
            registScreenOnOffReceiver();
            registPushReceiver();
            registerMessageObserver();
            registerUserObserver();
            registerAppInstallRecevier();
            registerAppLoginReceiver();
            registerReportUpdateReceiver();
            Log.d(TAG, "MainActivity::onCreate耗时：" + (System.currentTimeMillis() - time));
            EvalHintHelper.getInstance().init();
            GlobalConfigManager.getInstance().requestConfig(this, true);
        } catch (Throwable th) {
            Log.d(TAG, "MainActivity::onCreate耗时：" + (System.currentTimeMillis() - time));
            throw th;
        }
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.mLayoutId = layoutResID;
    }

    public void setContentView(View view) {
        super.setContentView(view);
        this.mLayoutId = 0;
    }

    private void registerUserObserver() {
        this.userObserver = new UserObserver(new Handler());
        getContentResolver().registerContentObserver(LanucherContentProvider.PERSON_CONTENT_URI, true, this.userObserver);
    }

    private void unregisterUserObserver() {
        if (this.userObserver != null) {
            getContentResolver().unregisterContentObserver(this.userObserver);
        }
    }

    private void registerMessageObserver() {
        try {
            this.messageContentObserver = new MessageContentObserver(new Handler());
            getContentResolver().registerContentObserver(Uri.parse(FeedbackHelper.MESSAGE_URI), false, this.messageContentObserver);
        } catch (Exception e) {
        }
    }

    private void unregisterMessageObserver() {
        if (this.messageContentObserver != null) {
            getContentResolver().unregisterContentObserver(this.messageContentObserver);
        }
    }

    private void sendOnResumeBroadcast() {
        Log.d(TAG, "sendOnResumeBroadcast");
        sendBroadcast(new Intent(ACTION_LAUNCHER_RESUME));
    }

    private void registWifiChangeReceiver() {
        this.wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.SCAN_RESULTS");
        filter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        registerReceiver(this.wifiReceiver, filter);
    }

    private void initVariable() {
        this.sharepferenceInstance = SharepreferenceUtil.getSharepferenceInstance(this);
        this.sharepferenceInstance.setAppEditAble(false);
        this.isFirstStart = this.sharepferenceInstance.isFirstStart();
        this.userInfo = this.sharepferenceInstance.getUserInfo();
        if (this.userInfo != null) {
            GlobalVariable.setLogin(true, this.userInfo);
        }
    }

    /* access modifiers changed from: private */
    public void initViewHolders(String gradePeriod) {
        this.mCardHolders = new ArrayList();
        if (TextUtils.equals(gradePeriod, "03")) {
            CardPrimaryMath math = new CardPrimaryMath(this);
            math.onCreate();
            CardPrimaryEnglish english = new CardPrimaryEnglish(this);
            english.onCreate();
            CardPrimaryChinese chinese = new CardPrimaryChinese(this);
            chinese.onCreate();
            this.mCardHolders.add(math);
            this.mCardHolders.add(english);
            this.mCardHolders.add(chinese);
            return;
        }
        requestReciteBook();
        this.launcher_FirstPate_ViewHolder = new Launcher_FirstPage_ViewHolder(this, this);
        this.launcher_FirstPate_ViewHolder.onCreate();
        this.launcher_EnglishPate_ViewHolder = new Launcher_EnglishPage_ViewHolder(this, getSupportFragmentManager());
        this.launcher_EnglishPate_ViewHolder.onCreate();
        this.launcher_ThreePate_ViewHolder = new Launcher_Third_ViewHolder(this);
        this.launcher_ThreePate_ViewHolder.onCreate();
        this.launcher_ets_viewHolder = new Launcher_ETS_ViewHolder(this);
        this.launcher_ets_viewHolder.onCreate();
        this.mCardHolders.add(this.launcher_FirstPate_ViewHolder);
        this.mCardHolders.add(this.launcher_EnglishPate_ViewHolder);
        this.mCardHolders.add(this.launcher_ThreePate_ViewHolder);
        if ((this.sharepferenceInstance.isETSLogin() && PackageUtils.isAvilible(this, Constants.EHEAR_APP)) || (this.sharepferenceInstance.isZXWLogin() && PackageUtils.isAvilible(this, Constants.ZXW_APP))) {
            this.mCardHolders.add(this.launcher_ets_viewHolder);
            if (this.sharepferenceInstance.isETSLogin() && PackageUtils.isAvilible(this, Constants.EHEAR_APP)) {
                this.launcher_ets_viewHolder.refreshView(Launcher_ETS_ViewHolder.PkgName.ETS, true);
            }
            if (this.sharepferenceInstance.isZXWLogin() && PackageUtils.isAvilible(this, Constants.ZXW_APP)) {
                this.launcher_ets_viewHolder.refreshView(Launcher_ETS_ViewHolder.PkgName.ZXW, true);
            }
        }
    }

    private void startBackgroundServices() {
        startService(new Intent(this, MyService_CheckVersion.class));
        startService(new Intent(this, MyService_OnScreen.class));
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        String str;
        super.onPause();
        this.mIsResumed = false;
        long time = System.currentTimeMillis();
        try {
            this.homeClickFlag = false;
            setAppStatus();
            dispatchActivityPause();
        } finally {
            str = "MainActivity::onPause耗时：";
            Log.d(TAG, str + (System.currentTimeMillis() - time));
        }
    }

    private Launcher_3rdAPP_ViewHold get3rdAppTag(View view) {
        Object objTag = view.getTag();
        if (objTag == null) {
            return null;
        }
        if (!(objTag instanceof Launcher_3rdAPP_ViewHold)) {
            return null;
        }
        return (Launcher_3rdAPP_ViewHold) objTag;
    }

    public void setAppStatus() {
        List<BaseHolder> holders;
        if (this.mLauncherAdapter != null && (holders = this.mLauncherAdapter.getAllAppListHolders()) != null) {
            for (BaseHolder holder : holders) {
                holder.onPause();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        int i;
        super.onResume();
        this.mIsResumed = true;
        Log.d(TAG, "onResume");
        long time = System.currentTimeMillis();
        try {
            LogAgentHelper.onActive();
            this.homeClickFlag = true;
            this.userInfo = this.sharepferenceInstance.getUserInfo();
            if (this.launcher_FirstPate_ViewHolder != null) {
                this.launcher_FirstPate_ViewHolder.setUserInfo(this.userInfo);
            }
            if (this.launcher_ThreePate_ViewHolder != null) {
                this.launcher_ThreePate_ViewHolder.setUserInfo(this.userInfo);
            }
            if (this.userInfo != null) {
                GlobalVariable.setLogin(true, this.userInfo);
            }
            if (!(this.userGrade == null || this.userName == null)) {
                this.userGrade.setText((this.userInfo == null || TextUtils.isEmpty(this.userInfo.getGradecode())) ? "点击登录" : GradeUtil.getGradeName(this.userInfo.getGradecode()));
                TextView textView = this.userName;
                if (this.userInfo == null) {
                    i = 8;
                } else {
                    i = 0;
                }
                textView.setVisibility(i);
                this.userName.setText(this.userInfo == null ? "" : this.userInfo.getRealname());
            }
            if (this.mLauncherAdapter != null) {
                for (BaseHolder holder : this.mLauncherAdapter.getAllHolders()) {
                    holder.setUserInfo(this.userInfo);
                }
            }
            getHeadIcon();
            dispatchActivityResume();
            requestReciteBook();
            LauncherExecutors.sAppLoaderExecutor.execute(new Runnable() {
                public void run() {
                    Intent intent = new Intent();
                    intent.setClassName("com.iflytek.pushservices", "com.iflytek.pushservices.PushManagerService");
                    MainActivity.this.startService(intent);
                }
            });
            Intent intent = new Intent(AppContants.ACTION_OTA_SERVICE);
            intent.setPackage(AppContants.OTA);
            startService(intent);
        } catch (Exception e) {
            Logging.d(TAG, "onResume()", e);
        } catch (Throwable th) {
            Log.d(TAG, "MainActivity::onResume耗时：" + (System.currentTimeMillis() - time));
            throw th;
        }
        Log.d(TAG, "MainActivity::onResume耗时：" + (System.currentTimeMillis() - time));
        this.mLauncherModel.updateAllAppUsageState();
        GlobalConfigManager.getInstance().requestConfig(this, false);
        sendOnResumeBroadcast();
    }

    private void registReceiver() {
        this.mHomeWatcherReceiver = new HomeWatcherReceiver();
        registerReceiver(this.mHomeWatcherReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    private void toLogin() {
        if (this.userInfo == null) {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 101);
        } else if (TextUtils.isEmpty(this.userInfo.getRealname())) {
            Intent intent = new Intent(getApplicationContext(), EditUserInfoActivity.class);
            intent.addFlags(268435456);
            startActivityForResult(intent, 101);
        } else if (this.userGrade != null) {
            this.userGrade.setText(this.userInfo == null ? "点击登录" : GradeUtil.getGradeName(this.userInfo.getGradecode()));
            this.userName.setVisibility(this.userInfo == null ? 8 : 0);
            this.userName.setText(this.userInfo == null ? "" : this.userInfo.getRealname());
        }
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        Log.d(TAG, "onRestart-----------");
        super.onRestart();
        this.userInfo = this.sharepferenceInstance.getUserInfo();
        checkLogin();
        getHeadIcon();
    }

    /* access modifiers changed from: private */
    public void checkLogin() {
        int i = 8;
        this.userInfo = this.sharepferenceInstance.getUserInfo();
        if (this.userInfo != null && TextUtils.isEmpty(this.userInfo.getRealname())) {
            CustomToast.showToast((Context) this, "请先完善个人信息", 0);
            Intent intent = new Intent(getApplicationContext(), CompleteUserInfoActivity.class);
            intent.addFlags(268435456);
            startActivityForResult(intent, 101);
        } else if (this.userInfo == null) {
            if (this.userName != null && this.userGrade != null) {
                this.userGrade.setText("点击登录");
                this.userName.setVisibility(8);
                this.userName.setText("");
            }
        } else if (this.userGrade != null && this.userName != null) {
            this.userGrade.setText(GradeUtil.getGradeName(this.userInfo.getGradecode()));
            TextView textView = this.userName;
            if (!TextUtils.isEmpty(this.userInfo.getRealname())) {
                i = 0;
            }
            textView.setVisibility(i);
            this.userName.setText(TextUtils.isEmpty(this.userInfo.getRealname()) ? "" : this.userInfo.getRealname());
            getHeadIcon();
        }
    }

    public void registScreenOnOffReceiver() {
        this.screenBroadcastReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.USER_PRESENT");
        getApplicationContext().registerReceiver(this.screenBroadcastReceiver, filter);
    }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
    }

    private void getHeadIcon() {
        Log.e(TAG, "getHeadIcon1");
        if (this.sharepferenceInstance.getUserInfo() != null && this.head != null) {
            Log.e(TAG, "getHeadIcon2");
            GlideImageLoader.getInstance().displayImageCircle(this, this.sharepferenceInstance.getUserInfo().getUsericonpath(), this.head, R.drawable.pic_tx_default);
        } else if (this.head != null) {
            this.head.setImageResource(R.drawable.pic_tx_default);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                checkLogin();
                getHeadIcon();
                break;
            case 102:
                toLogin();
                getHeadIcon();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy------------");
        if (this.mLauncherModel != null) {
            this.mLauncherModel.unInit();
            this.mLauncherModel = null;
        }
        if (this.wifiReceiver != null) {
            unregisterReceiver(this.wifiReceiver);
        }
        if (this.mHomeWatcherReceiver != null) {
            unregisterReceiver(this.mHomeWatcherReceiver);
        }
        if (this.screenBroadcastReceiver != null) {
            getApplicationContext().unregisterReceiver(this.screenBroadcastReceiver);
            this.screenBroadcastReceiver = null;
        }
        unregisterReportUpdateReceiver();
        dispatchActivityDestroy();
        unRegistPushReceiver();
        unregisterMessageObserver();
        unregisterUserObserver();
        unregisterAppInstallRecevier();
        unregisterAppLoginReceiver();
        monitorException("MainActivity.onDestroy");
        Process.killProcess(Process.myPid());
    }

    public void setEditMode(boolean editMode) {
        if (this.mLauncherAdapter != null) {
            this.mLauncherAdapter.setEditMode(editMode);
        }
    }

    class MyViewPageChangeListener implements ViewPager.OnPageChangeListener {
        MyViewPageChangeListener() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            Log.d(MainActivity.TAG, "onPageSelected");
            int access$200 = MainActivity.this.mCurrentPosition;
            int unused = MainActivity.this.mCurrentPosition = position;
            MainActivity.this.refreshUserCenterIcon(position);
            List<BaseHolder> holders = null;
            if (MainActivity.this.mLauncherAdapter != null) {
                holders = MainActivity.this.mLauncherAdapter.getAllHolders();
            }
            if (holders != null) {
                int size = holders.size();
                int i = 0;
                while (i < size) {
                    boolean isCurrent = i == MainActivity.this.mCurrentPosition;
                    BaseHolder holder = holders.get(i);
                    if (isCurrent) {
                        holder.onScrollIn();
                    } else {
                        holder.onScrollOut();
                    }
                    i++;
                }
            }
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    /* access modifiers changed from: private */
    public void refreshUserCenterIcon(int position) {
        Log.d(TAG, "position:" + position);
        int cardSize = -1;
        if (this.mLauncherAdapter != null) {
            cardSize = this.mLauncherAdapter.getCardViewCount();
        }
        if (position >= cardSize) {
            this.bottomMenu.setVisibility(8);
            this.userCenter.setVisibility(8);
            return;
        }
        this.bottomMenu.setVisibility(8);
        this.userCenter.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void setToolBarStatus() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
    }

    public boolean handleMessage(Message message) {
        return true;
    }

    /* access modifiers changed from: private */
    public AppInfo getReciteBookAppInfo() {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(AppContants.RECITE_BOOK, 128);
            Drawable appIcon = getPackageManager().getApplicationIcon(applicationInfo);
            CharSequence applicationLabel = getPackageManager().getApplicationLabel(applicationInfo);
            AppInfo appInfo = new AppInfo();
            appInfo.setAppName(applicationLabel + "");
            appInfo.setIcon(appIcon);
            appInfo.setPakageName(AppContants.RECITE_BOOK);
            appInfo.setSystemApp(false);
            return appInfo;
        } catch (Exception e) {
            Logging.d(TAG, "showReciteBookIcon()", e);
            return null;
        }
    }

    private void requestReciteBook() {
        ReciteBookIconHelper.requestReciteBookShowPolicy(new ReciteBookIconHelper.IShowReciteCallback() {
            public boolean showReciteBookIcon() {
                Logging.d(MainActivity.TAG, "showReciteBookIcon()");
                AppInfo appInfo = MainActivity.this.getReciteBookAppInfo();
                if (appInfo != null) {
                    Logging.d(MainActivity.TAG, "showReciteBookIcon() show success");
                    MainActivity.this.mLauncherModel.addFakeAppInfo(appInfo);
                    return true;
                }
                Logging.d(MainActivity.TAG, "showReciteBookIcon() show fail");
                return false;
            }
        });
    }

    public void reMoveAppFromList(String pName) {
        if (this.mLauncherAdapter != null && AppContants.RECITE_BOOK.equals(pName)) {
            this.mLauncherModel.removeFackAppInfo(AppContants.RECITE_BOOK);
        }
    }

    @OnClick({2131689774, 2131689772})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head:
            case R.id.user_name_ll:
                if (this.userInfo != null) {
                    if (this.studyReportUpdateIV.getVisibility() == 0) {
                        this.studyReportUpdateIV.setVisibility(8);
                    }
                    Intent intent = new Intent(getApplicationContext(), UserCenterActivity.class);
                    intent.addFlags(268435456);
                    startActivityForResult(intent, 102);
                    Map<String, String> map = new HashMap<>();
                    if (FeedbackHelper.getMessage(getApplicationContext()) > 0) {
                        map.put(StatsKeys.K_D_USER_STATE, StatsKeys.V_D_USER_STATE_LOGIN_HAS_MSG);
                    } else if (FeedbackHelper.getMessage(getApplicationContext()) == 0) {
                        map.put(StatsKeys.K_D_USER_STATE, "2");
                    }
                    StatsHelper.desktopClickUserIcon(map);
                    return;
                }
                Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                intent2.addFlags(268435456);
                startActivityForResult(intent2, 101);
                Map<String, String> map2 = new HashMap<>();
                map2.put(StatsKeys.K_D_USER_STATE, "1");
                StatsHelper.desktopClickUserIcon(map2);
                return;
            default:
                return;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.mViewPager != null) {
            this.mLauncherAdapter.setEditMode(false);
        }
        return false;
    }

    public void Next() {
        this.launcher_wiFiConnect_viewHolder = new Launcher_WiFiConnect_ViewHolder(this, this, this.mHandler);
        setContentView(this.launcher_wiFiConnect_viewHolder.getRootView());
        if (NetworkUtil.isNetworkAvailable(this) && this.launcher_wiFiConnect_viewHolder != null) {
            this.launcher_wiFiConnect_viewHolder.wifiConnect();
        }
    }

    public void toMain() {
        if (this.isFirstStart) {
            this.sharepferenceInstance.setAlreadyStart(false);
            this.isFirstStart = false;
            if (this.launcher_wiFiConnect_viewHolder != null) {
                this.launcher_wiFiConnect_viewHolder.onDestroy();
                this.launcher_wiFiConnect_viewHolder = null;
            }
            this.mLauncherView.finishGuide();
        }
    }

    public void gouiderFinish() {
        LogSaveManager_Util.saveLog(this, "toMain:");
        this.userCenter.setVisibility(0);
        this.pageIndicatorView.setVisibility(0);
        this.activityMain.setEnabled(false);
        this.activityMain.setClickable(false);
        getWindow().clearFlags(1024);
        this.mViewPager.setScrollable(true);
        this.sharepferenceInstance.setAlreadyStart(false);
        this.isFirstStart = this.sharepferenceInstance.isFirstStart();
        if (this.launcher_FirstPate_ViewHolder != null) {
            this.launcher_FirstPate_ViewHolder.onResume();
        }
    }

    public class HomeWatcherReceiver extends BroadcastReceiver {
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";

        public HomeWatcherReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), "android.intent.action.CLOSE_SYSTEM_DIALOGS") && TextUtils.equals(SYSTEM_DIALOG_REASON_HOME_KEY, intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)) && MainActivity.this.mViewPager != null) {
                boolean editMode = false;
                if (MainActivity.this.homeClickFlag.booleanValue()) {
                    Launcher_3rdAPP_ViewHold viewHolder = MainActivity.this.mLauncherAdapter.getAppViewHolder(MainActivity.this.mViewPager.getCurrentItem());
                    if (viewHolder != null) {
                        editMode = viewHolder.isEditMode();
                    }
                    if (editMode) {
                        MainActivity.this.mLauncherAdapter.setEditMode(false);
                    } else {
                        MainActivity.this.mViewPager.setCurrentItem(0, false);
                    }
                }
            }
        }
    }

    public class WifiBroadcastReceiver extends BroadcastReceiver {
        public WifiBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (MainActivity.this.isFirstStart && MainActivity.this.launcher_wiFiConnect_viewHolder != null) {
                MainActivity.this.launcher_wiFiConnect_viewHolder.handleWifiEvent(context, intent);
            }
            MessagePollingManager.getInstance(context.getApplicationContext()).mWifiHandler.handleEvent(intent, intent.getAction());
            if ("android.net.wifi.STATE_CHANGE".equals(intent.getAction())) {
                NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (NetworkInfo.State.DISCONNECTED != info.getState() && NetworkInfo.State.CONNECTED == info.getState()) {
                    if (TextUtils.isEmpty(SharepreferenceUtil.getSharepferenceInstance(MainActivity.this).getActiveTime())) {
                        MainActivity.this.setActiveTime();
                    }
                    if (MainActivity.this.mLauncherModel != null) {
                        MainActivity.this.mLauncherModel.onWifiConnected();
                    }
                }
            }
        }
    }

    public void setActiveTime() {
        PadInfoUtil padInfoUtil = new PadInfoUtil(this);
        LauncherHttpHelper.getLauncherService().setActiveTime(padInfoUtil.getSnCode(), padInfoUtil.getIPAddress(), padInfoUtil.getSystemVersionCode(), PadInfoUtil.getMacFromHardware()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ActiveModel>(this, false) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
            }

            public void onNext(ActiveModel response) {
                super.onNext(response);
                if (response != null && response.getStatus() == 0) {
                    SharepreferenceUtil.getSharepferenceInstance(MainActivity.this).setActiveTime(response.getData() + "");
                }
            }
        });
    }

    public void registPushReceiver() {
    }

    public void unRegistPushReceiver() {
    }

    public boolean isClickTenTimes() {
        System.arraycopy(this.mHints, 1, this.mHints, 0, this.mHints.length - 1);
        this.mHints[this.mHints.length - 1] = SystemClock.uptimeMillis();
        if (SystemClock.uptimeMillis() - this.mHints[0] > 3500) {
            return false;
        }
        this.mHints = null;
        this.mHints = new long[10];
        return true;
    }

    class MessageContentObserver extends ContentObserver {
        public MessageContentObserver(Handler handler) {
            super(handler);
        }

        public boolean deliverSelfNotifications() {
            Log.d(MainActivity.TAG, "deliverSelfNotifications");
            return true;
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(MainActivity.TAG, "onChange---------" + selfChange + Thread.currentThread().getName());
            int messeageCount = FeedbackHelper.getMessage(MainActivity.this.getApplicationContext());
            if (messeageCount != 0 || MainActivity.this.imgDot == null) {
                if (messeageCount > 0 && MainActivity.this.imgDot != null) {
                    String userId = UserInfoHelper.getInstance(MainActivity.this.getApplicationContext()).getUserId();
                    Log.d(MainActivity.TAG, "userId:" + userId);
                    if (TextUtils.isEmpty(userId)) {
                        if (MainActivity.this.imgDot.getVisibility() == 0) {
                            MainActivity.this.imgDot.setVisibility(8);
                        }
                    } else if (MainActivity.this.imgDot.getVisibility() == 8) {
                        MainActivity.this.imgDot.setVisibility(0);
                    }
                }
            } else if (MainActivity.this.imgDot.getVisibility() == 0) {
                MainActivity.this.imgDot.setVisibility(8);
            }
        }
    }

    class UserObserver extends ContentObserver {
        public UserObserver(Handler handler) {
            super(handler);
        }

        public boolean deliverSelfNotifications() {
            return false;
        }

        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d(MainActivity.TAG, "selfChange:" + uri.toString());
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(MainActivity.TAG, "userInfo-----changed");
            if (TextUtils.isEmpty(UserInfoHelper.getInstance(MainActivity.this.getApplicationContext()).getUserId())) {
                Log.d(MainActivity.TAG, "用户未登录");
                return;
            }
            FeedbackHelper.updateMessage(MainActivity.this.getApplicationContext(), 0);
            MessagePollingManager.getInstance(MainActivity.this.getApplicationContext()).stopPolling();
            MessagePollingManager.getInstance(MainActivity.this.getApplicationContext()).startPolling(0, 30, TimeUnit.MINUTES);
        }
    }

    class AppInstallRecevier extends BroadcastReceiver {
        private static final String eHearPkgName = "com.ets100.secondary";
        private static final String zxwPkgName = "com.iflytek.elpmobile.student";

        AppInstallRecevier() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.d(MainActivity.TAG, "intent:" + intent.getAction());
            if (TextUtils.equals(intent.getAction(), "android.intent.action.PACKAGE_ADDED")) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Log.d(MainActivity.TAG, "packageName:" + packageName);
                if ("com.ets100.secondary".equals(packageName) && MainActivity.this.sharepferenceInstance.isETSLogin()) {
                    MainActivity.this.refreshViewHolder(Launcher_ETS_ViewHolder.PkgName.ETS);
                } else if ("com.iflytek.elpmobile.student".equals(packageName) && MainActivity.this.sharepferenceInstance.isZXWLogin()) {
                    MainActivity.this.refreshViewHolder(Launcher_ETS_ViewHolder.PkgName.ZXW);
                }
            } else if (TextUtils.equals(intent.getAction(), "android.intent.action.PACKAGE_REPLACED")) {
                String packageName2 = intent.getData().getSchemeSpecificPart();
                if ("com.ets100.secondary".equals(packageName2) && MainActivity.this.sharepferenceInstance.isETSLoginExtra()) {
                    MainActivity.this.sharepferenceInstance.setETSLogin(true);
                    MainActivity.this.refreshViewHolder(Launcher_ETS_ViewHolder.PkgName.ETS);
                } else if ("com.iflytek.elpmobile.student".equals(packageName2) && MainActivity.this.sharepferenceInstance.isZXWLoginExtra()) {
                    MainActivity.this.sharepferenceInstance.setZxwLogin(true);
                    MainActivity.this.refreshViewHolder(Launcher_ETS_ViewHolder.PkgName.ZXW);
                }
            } else if (TextUtils.equals(intent.getAction(), "android.intent.action.PACKAGE_REMOVED")) {
                String packageName3 = intent.getData().getSchemeSpecificPart();
                Log.d(MainActivity.TAG, "packageName:" + packageName3);
                if ("com.ets100.secondary".equals(packageName3) && MainActivity.this.sharepferenceInstance.isETSLogin()) {
                    MainActivity.this.sharepferenceInstance.setETSLogin(false);
                    removeViewHolder(Launcher_ETS_ViewHolder.PkgName.ETS);
                } else if ("com.iflytek.elpmobile.student".equals(packageName3) && MainActivity.this.sharepferenceInstance.isZXWLogin()) {
                    MainActivity.this.sharepferenceInstance.setZxwLogin(false);
                    removeViewHolder(Launcher_ETS_ViewHolder.PkgName.ZXW);
                }
            } else if (TextUtils.equals(MainDebugger.DACTION_APPLIST_PRINT, intent.getAction())) {
                printAllApps();
            }
        }

        private void printAllApps() {
        }

        private void removeViewHolder(String pkgName) {
            if (MainActivity.this.mCardHolders.contains(MainActivity.this.launcher_ets_viewHolder)) {
                MainActivity.this.launcher_ets_viewHolder.refreshView(pkgName, false);
                if (!MainActivity.this.launcher_ets_viewHolder.isEtsShow() && !MainActivity.this.launcher_ets_viewHolder.isZXWShow() && MainActivity.this.mLauncherAdapter != null) {
                    MainActivity.this.pageIndicatorView.setCount(MainActivity.this.mLauncherAdapter.getCount() - 1);
                    MainActivity.this.mLauncherAdapter.removeCardViewHolder(MainActivity.this.launcher_ets_viewHolder);
                    MainActivity.this.refreshUserCenterIcon(MainActivity.this.mCurrentPosition);
                }
            }
        }
    }

    private void registerAppInstallRecevier() {
        this.mAppRecevier = new AppInstallRecevier();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction(MainDebugger.DACTION_APPLIST_PRINT);
        intentFilter.addDataScheme("package");
        registerReceiver(this.mAppRecevier, intentFilter);
    }

    private void unregisterAppInstallRecevier() {
        if (this.mAppRecevier != null) {
            unregisterReceiver(this.mAppRecevier);
        }
    }

    class AppLoginReceiver extends BroadcastReceiver {
        private static final String ACTION = "com.android.launcher.study.CREATE_SHORT_CUT";
        private static final String EHEAR_KEY = "packageName";
        private static final String ZXW_KEY = "packageName";
        private static final String eHearPkgName = "com.ets100.secondary";
        private static final String zxwPkgName = "com.iflytek.elpmobile.student";

        AppLoginReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && "com.android.launcher.study.CREATE_SHORT_CUT".equals(intent.getAction())) {
                Log.d(MainActivity.TAG, "接受到了登录广播" + intent.getStringExtra("packageName"));
                if ("com.ets100.secondary".equals(intent.getStringExtra("packageName"))) {
                    Log.d(MainActivity.TAG, "获取到了app数据");
                    if (!MainActivity.this.sharepferenceInstance.isETSLogin()) {
                        Log.d(MainActivity.TAG, "ETS用户未登录");
                        MainActivity.this.sharepferenceInstance.setETSLogin(true);
                        MainActivity.this.sharepferenceInstance.setEtsLoginExtra(true);
                        MainActivity.this.refreshViewHolder(Launcher_ETS_ViewHolder.PkgName.ETS);
                    }
                } else if ("com.iflytek.elpmobile.student".equals(intent.getStringExtra("packageName")) && !MainActivity.this.sharepferenceInstance.isZXWLogin()) {
                    Log.d(MainActivity.TAG, "智学网未登录");
                    MainActivity.this.sharepferenceInstance.setZxwLogin(true);
                    MainActivity.this.sharepferenceInstance.setZxwLoginExtra(true);
                    MainActivity.this.refreshViewHolder(Launcher_ETS_ViewHolder.PkgName.ZXW);
                }
            }
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
                    LogUtils.d(MainActivity.TAG, "onReceive: 今天已经展示过了学习日报的小红点");
                    return;
                }
                if (MainActivity.this.studyReportUpdateIV.getVisibility() != 0) {
                    MainActivity.this.studyReportUpdateIV.setVisibility(0);
                }
                StudyDailyReportMsgManager.getInstance().updateReceiveMsgTime(context, System.currentTimeMillis());
            } else if (TextUtils.equals(action, Constants.ACTION_STUDY_REPORT_SHOWED_TIME_UPDATED)) {
                ImageView imageView = MainActivity.this.studyReportUpdateIV;
                if (StudyDailyReportMsgManager.getInstance().hasShowStudyReportToday()) {
                    i = 8;
                }
                imageView.setVisibility(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshViewHolder(String pkgName) {
        if (this.launcher_ets_viewHolder != null) {
            if (this.mCardHolders.contains(this.launcher_ets_viewHolder)) {
                Log.d(TAG, "刷新");
                this.launcher_ets_viewHolder.refreshView(pkgName, true);
                return;
            }
            Log.d(TAG, "建立");
            if (this.mLauncherAdapter != null) {
                this.pageIndicatorView.setCount(this.mLauncherAdapter.getCount() + 1);
                this.mLauncherAdapter.addCardViewHolder(this.launcher_ets_viewHolder);
                this.launcher_ets_viewHolder.refreshView(pkgName, true);
                refreshUserCenterIcon(this.mCurrentPosition);
            }
        }
    }

    private void registerAppLoginReceiver() {
        this.mAppLoginReceiver = new AppLoginReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.launcher.study.CREATE_SHORT_CUT");
        registerReceiver(this.mAppLoginReceiver, intentFilter);
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

    private void unregisterAppLoginReceiver() {
        if (this.mAppLoginReceiver != null) {
            unregisterReceiver(this.mAppLoginReceiver);
        }
    }

    public void finish() {
        super.finish();
        monitorException("MainActivity.finish() call stack");
    }

    private void monitorException(String prefix) {
    }

    private class AppListViewHolderManager implements ILauncherPagersView {
        private AppListViewHolderManager() {
        }

        public void onCreateCardPages(String gradePeriod) {
            MainActivity.this.initViewHolders(gradePeriod);
        }

        public void initAppPages(String gradePeriod, List<BaseHolder> viewHolders) {
            List<BaseHolder> holders;
            Logger.d(MainActivity.TAG, "initAppPages: " + viewHolders.size());
            if (MainActivity.this.mLayoutId != R.layout.activity_main) {
                long time = System.currentTimeMillis();
                MainActivity.this.setContentView((int) R.layout.activity_main);
                Logger.d("fgtian", "activity_main耗时：" + (System.currentTimeMillis() - time));
                ButterKnife.bind((Activity) MainActivity.this);
                MainActivity.this.setToolBarStatus();
                MainActivity.this.activityMain.setClickable(false);
                MainActivity.this.activityMain.setEnabled(false);
            }
            MainActivity.this.checkLogin();
            if (TextUtils.equals(gradePeriod, "03")) {
                if (MainActivity.this.activityMain != null) {
                    MainActivity.this.activityMain.setBackgroundResource(R.drawable.bg_primary);
                }
            } else if (MainActivity.this.activityMain != null) {
                MainActivity.this.activityMain.setBackgroundResource(R.drawable.bg_middle_school);
            }
            Logger.d(MainActivity.TAG, "AAAAA: call prepare to call onDetachFromAdapter");
            if (!(MainActivity.this.mLauncherAdapter == null || (holders = MainActivity.this.mLauncherAdapter.getAllHolders()) == null)) {
                int currentItem = MainActivity.this.mViewPager.getCurrentItem();
                int size = holders.size();
                int i = 0;
                while (i < size) {
                    boolean isCurrentItem = i == currentItem;
                    BaseHolder holder = holders.get(i);
                    Logger.d(MainActivity.TAG, "AAAAA: call onDetachFromAdapter");
                    holder.onDetachFromAdapter(isCurrentItem);
                    holder.onDestroy();
                    i++;
                }
            }
            if (TextUtils.equals(gradePeriod, "03")) {
                BaseHolder unused = MainActivity.this.launcher_FirstPate_ViewHolder = null;
                Launcher_EnglishPage_ViewHolder unused2 = MainActivity.this.launcher_EnglishPate_ViewHolder = null;
                BaseHolder unused3 = MainActivity.this.launcher_ThreePate_ViewHolder = null;
                Launcher_ETS_ViewHolder unused4 = MainActivity.this.launcher_ets_viewHolder = null;
            }
            Iterator<BaseHolder> it = viewHolders.iterator();
            while (it.hasNext()) {
                Launcher_3rdAPP_ViewHold viewHold = (Launcher_3rdAPP_ViewHold) it.next();
                viewHold.setMainActivity(MainActivity.this);
                viewHold.setModelView(MainActivity.this.mLauncherView);
            }
            LauncherAdapter unused5 = MainActivity.this.mLauncherAdapter = new LauncherAdapter(MainActivity.this.mCardHolders, viewHolders);
            List<BaseHolder> holders2 = MainActivity.this.mLauncherAdapter.getAllHolders();
            if (holders2 != null) {
                int size2 = holders2.size();
                int i2 = 0;
                while (i2 < size2) {
                    holders2.get(i2).onAttachToAdapter(i2 == 0, MainActivity.this.mIsResumed);
                    i2++;
                }
            }
            MainActivity.this.mViewPager.setOffscreenPageLimit(7);
            MainActivity.this.mViewPager.setAdapter(MainActivity.this.mLauncherAdapter);
            MainActivity.this.mViewPager.setCurrentItem(0);
            MainActivity.this.refreshUserCenterIcon(0);
            MainActivity.this.mViewPager.add3rdOnPageChangeListener(new MyViewPageChangeListener());
            MainActivity.this.pageIndicatorView.setViewPager(MainActivity.this.mViewPager);
            MainActivity.this.pageIndicatorView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (MainActivity.this.isClickTenTimes()) {
                        ToastUtils.showShort((CharSequence) "开始上传");
                        new MyAsyncTaskUtil(MainActivity.this).execute(new Void[0]);
                    }
                }
            });
            MainActivity.this.pageIndicatorView.setSelection(0);
        }

        public void notifyPagesCountChanged(BaseHolder newItem) {
            Logger.d(MainActivity.TAG, "notifyPagesCountChanged");
            if (newItem != null) {
                Launcher_3rdAPP_ViewHold viewHold = (Launcher_3rdAPP_ViewHold) newItem;
                viewHold.setMainActivity(MainActivity.this);
                viewHold.setModelView(MainActivity.this.mLauncherView);
            }
            if (MainActivity.this.mLauncherAdapter != null) {
                MainActivity.this.mLauncherAdapter.notifyDataSetChanged();
                MainActivity.this.pageIndicatorView.setCount(MainActivity.this.mLauncherAdapter.getCount());
            }
        }
    }

    private void dispatchActivityResume() {
        List<BaseHolder> holders;
        if (this.mLauncherAdapter != null && (holders = this.mLauncherAdapter.getAllHolders()) != null) {
            int size = holders.size();
            int pageIndex = this.mViewPager.getCurrentItem();
            int i = 0;
            while (i < size) {
                holders.get(i).onActivityResume(pageIndex == i);
                i++;
            }
        }
    }

    private void dispatchActivityPause() {
        List<BaseHolder> holders;
        if (this.mLauncherAdapter != null && (holders = this.mLauncherAdapter.getAllHolders()) != null) {
            int size = holders.size();
            int pageIndex = this.mViewPager.getCurrentItem();
            int i = 0;
            while (i < size) {
                holders.get(i).onActivityPause(pageIndex == i);
                i++;
            }
        }
    }

    private void dispatchActivityDestroy() {
        List<BaseHolder> holders;
        if (this.mLauncherAdapter != null && (holders = this.mLauncherAdapter.getAllHolders()) != null) {
            for (BaseHolder holder : holders) {
                holder.onDestroy();
            }
        }
    }
}