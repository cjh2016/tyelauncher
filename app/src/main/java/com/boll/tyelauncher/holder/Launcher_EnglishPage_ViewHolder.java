package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.iflytek.stats.StatsHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.fragment.EnglishContentFragment;
import com.toycloud.launcher.helper.EnglishModelHelper;
import com.toycloud.launcher.helper.FunctionModuleBean;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.EtsConstant;
import com.toycloud.launcher.myinterface.Listener_Update;
import com.toycloud.launcher.util.EtsUtils;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.UpdateUtil;
import com.toycloud.launcher.view.DialogUtil;
import com.toycloud.launcher.view.SegmentCardView;
import java.util.ArrayList;
import java.util.List;

public class Launcher_EnglishPage_ViewHolder extends BaseHolder implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    public static final String ACTION_ETS_DATA_CHANGED = "com.ets100.study.ACTION_ETS_DATA_CHANGED";
    private static final String TAG = "EnglishHolder";
    public static boolean isNeedReport = true;
    private List<Fragment> fragments;
    private boolean isCanRefresh = false;
    private boolean isFirstStart = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Launcher_EnglishPage_ViewHolder.ACTION_ETS_DATA_CHANGED.equals(intent.getAction())) {
                Launcher_EnglishPage_ViewHolder.this.refreshView();
            }
        }
    };
    private FrameLayout mContentEnglish;
    /* access modifiers changed from: private */
    public Context mContext;
    private FragmentManager mFragmentManager;
    private Handler mHandler;
    private SegmentCardView mImgBookRecite;
    private SegmentCardView mImgReciteTexts;
    private SegmentCardView mImgReciteWords;
    private SegmentCardView mImgWriting;
    private EnglishContentFragment mPractiseFragment;
    private RadioButton mRadioBtnPractise;
    private RadioButton mRadioBtnTest;
    private RadioGroup mRadioGroupEnglish;
    private EnglishContentFragment mTestFragment;

    public Launcher_EnglishPage_ViewHolder(Context context, FragmentManager fragmentManager) {
        super(context);
        HandlerThread handlerThread = new HandlerThread("Launcher_EnglishPage");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
        this.mFragmentManager = fragmentManager;
        this.isFirstStart = true;
        try {
            this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter(ACTION_ETS_DATA_CHANGED));
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        Log.d(TAG, "initView");
        this.fragments = new ArrayList();
        this.mContext = context;
        this.mRadioGroupEnglish = (RadioGroup) this.mRootView.findViewById(R.id.tab_english);
        this.mImgReciteWords = (SegmentCardView) this.mRootView.findViewById(R.id.img_recite_words);
        this.mImgReciteTexts = (SegmentCardView) this.mRootView.findViewById(R.id.img_recite_texts);
        this.mImgBookRecite = (SegmentCardView) this.mRootView.findViewById(R.id.img_book_recite);
        this.mImgWriting = (SegmentCardView) this.mRootView.findViewById(R.id.img_writing);
        this.mRadioBtnPractise = (RadioButton) this.mRootView.findViewById(R.id.rb_practice);
        this.mRadioBtnTest = (RadioButton) this.mRootView.findViewById(R.id.rb_test);
        this.mContentEnglish = (FrameLayout) this.mRootView.findViewById(R.id.content_english);
        this.mImgReciteWords.setOnClickListener(this);
        this.mImgReciteTexts.setOnClickListener(this);
        this.mImgWriting.setOnClickListener(this);
        this.mImgBookRecite.setOnClickListener(this);
        this.mRadioGroupEnglish.setOnCheckedChangeListener(this);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        Log.d(TAG, "getLayoutID");
        return R.layout.fragment_english_page;
    }

    public void setCanRefresh(boolean isRefresh) {
        this.isCanRefresh = isRefresh;
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(boolean isVisible) {
        super.onVisibilityChanged(isVisible);
        if (isVisible) {
            Log.d(TAG, "onVisibilityChanged:" + isVisible);
            onResume();
        }
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (Launcher_EnglishPage_ViewHolder.this.mContext != null && (Launcher_EnglishPage_ViewHolder.this.mContext instanceof AppCompatActivity)) {
                    ((AppCompatActivity) Launcher_EnglishPage_ViewHolder.this.mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            Launcher_EnglishPage_ViewHolder.this.refreshView();
                        }
                    });
                }
            }
        }, 400);
    }

    /* access modifiers changed from: private */
    public void refreshView() {
        if (isVisibie()) {
            Log.d(TAG, "刷新数据");
            User userInfo = this.sharepreferenceUtil.getUserInfo();
            if (userInfo == null) {
                Log.d(TAG, "userInfo is null");
                showDefaultView();
                return;
            }
            showTabFromFunctionModule(EnglishModelHelper.getInstance(this.mContext).queryFunctionModule(userInfo.getToken()));
        }
    }

    private void showTabFromFunctionModule(FunctionModuleBean functionModuleBean) {
        if (functionModuleBean == null || functionModuleBean.getCode() != 0) {
            showDefaultView();
            return;
        }
        Log.d(TAG, "functionModuleBean:" + functionModuleBean.toString());
        FunctionModuleBean.DataBean dataBean = functionModuleBean.getData();
        if (dataBean == null || dataBean.getFunctionModuleData() == null || dataBean.getFunctionModuleData().size() <= 0) {
            Log.d(TAG, "数据条目为0条");
            showDefaultView();
            return;
        }
        List<String> functionModuleDatas = dataBean.getFunctionModuleData();
        if (functionModuleDatas.contains(EtsConstant.BOOK_SYNC) || functionModuleDatas.contains("10") || functionModuleDatas.contains("1") || functionModuleDatas.contains("12")) {
            if (functionModuleDatas.contains(EtsConstant.BOOK_SYNC) || functionModuleDatas.contains("10")) {
                showPractiseTab();
                if (functionModuleDatas.contains("1") || functionModuleDatas.contains("12")) {
                    showTestTab();
                    configTestTabBg();
                    configPractiseBg();
                    chooseCheckedTab();
                } else {
                    hideTestTab();
                    configPractiseTransparent();
                    this.mRadioBtnPractise.setChecked(true);
                }
            } else {
                hidePractiseTab();
                showTestTab();
                configTestTabTransparent();
                this.mRadioBtnTest.setChecked(true);
            }
            refreshData(functionModuleDatas);
            return;
        }
        showDefaultView();
    }

    private void chooseCheckedTab() {
        if (this.mRadioGroupEnglish != null) {
            switch (this.mRadioGroupEnglish.getCheckedRadioButtonId()) {
                case R.id.rb_practice:
                    this.mRadioBtnPractise.setChecked(true);
                    return;
                case R.id.rb_test:
                    this.mRadioBtnTest.setChecked(true);
                    return;
                default:
                    Log.d(TAG, "默认都没有被选中");
                    this.mRadioBtnPractise.setChecked(true);
                    return;
            }
        }
    }

    private void showTestTab() {
        if (this.mRadioBtnTest != null && this.mRadioBtnTest.getVisibility() == 8) {
            this.mRadioBtnTest.setVisibility(0);
        }
    }

    private void hideTestTab() {
        if (this.mRadioBtnTest != null && this.mRadioBtnTest.getVisibility() == 0) {
            this.mRadioBtnTest.setVisibility(8);
        }
    }

    private void showPractiseTab() {
        if (this.mRadioBtnPractise != null && this.mRadioBtnPractise.getVisibility() == 8) {
            this.mRadioBtnPractise.setVisibility(0);
        }
    }

    private void hidePractiseTab() {
        if (this.mRadioBtnPractise != null && this.mRadioBtnPractise.getVisibility() == 0) {
            this.mRadioBtnPractise.setVisibility(8);
        }
    }

    private void showDefaultView() {
        showPractiseTab();
        configPractiseBg();
        showTestTab();
        configTestTabBg();
        refreshData((List<String>) null);
        chooseCheckedTab();
    }

    private void configTestTabBg() {
        if (this.mRadioBtnTest != null) {
            this.mRadioBtnTest.setBackground(this.mContext.getDrawable(R.drawable.selector_monikaoshi));
        }
    }

    private void configTestTabTransparent() {
        if (this.mRadioBtnTest != null) {
            this.mRadioBtnTest.setBackgroundColor(this.mContext.getColor(R.color.color_transparent));
        }
    }

    private void configPractiseBg() {
        if (this.mRadioBtnPractise != null) {
            this.mRadioBtnPractise.setBackground(this.mContext.getDrawable(R.drawable.selector_tongbu));
        }
    }

    private void configPractiseTransparent() {
        if (this.mRadioBtnPractise != null) {
            this.mRadioBtnPractise.setBackgroundColor(this.mContext.getColor(R.color.color_transparent));
        }
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d(TAG, "onCheckedChanged");
        FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        Fragment presentFragment = null;
        switch (checkedId) {
            case R.id.rb_practice:
                if (this.mPractiseFragment == null) {
                    this.mPractiseFragment = EnglishContentFragment.getInstance();
                    this.mPractiseFragment.setCurrentFragmentName(EnglishContentFragment.FragmentName.PRACTICE);
                    fragmentTransaction.add(this.mContentEnglish.getId(), (Fragment) this.mPractiseFragment);
                }
                presentFragment = this.mPractiseFragment;
                if (!this.isFirstStart) {
                    StatsHelper.englishPageClickedTBLX();
                    break;
                } else {
                    this.isFirstStart = false;
                    break;
                }
            case R.id.rb_test:
                if (this.mTestFragment == null) {
                    this.mTestFragment = EnglishContentFragment.getInstance();
                    this.mTestFragment.setCurrentFragmentName(EnglishContentFragment.FragmentName.TEST);
                    fragmentTransaction.add(this.mContentEnglish.getId(), (Fragment) this.mTestFragment);
                }
                presentFragment = this.mTestFragment;
                if (!this.isFirstStart) {
                    StatsHelper.englishPageClickedMNKS();
                    break;
                } else {
                    this.isFirstStart = false;
                    break;
                }
        }
        this.fragments.add(presentFragment);
        fragmentTransaction.show(presentFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void refreshData(List<String> functionModuleDatas) {
        if (this.mPractiseFragment != null) {
            this.mPractiseFragment.refreshData(true, functionModuleDatas);
        }
        if (this.mTestFragment != null) {
            this.mTestFragment.refreshData(true, functionModuleDatas);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_recite_words:
                Log.d(TAG, "img_recite_words");
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    StatsHelper.englishPageClickedJIDC();
                    if (!GlobalVariable.isLogin()) {
                        DialogUtil.showDialogToLogin(this.mContext);
                        return;
                    } else if (isAvilible(this.mContext, "com.iflytek.dictatewords")) {
                        new UpdateUtil(this.mContext, new Listener_Update() {
                            public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                                if (!isMustUpdate && !isUpdate) {
                                    try {
                                        Intent i = new Intent();
                                        i.putExtra("token", "Bearer " + SharepreferenceUtil.getToken());
                                        i.putExtra(AppContants.KEY_USER_ID, SharepreferenceUtil.getSharepferenceInstance(Launcher_EnglishPage_ViewHolder.this.mContext).getUserInfo().getUserid());
                                        i.setComponent(new ComponentName("com.iflytek.dictatewords", AppContants.RECITE_WORDS_START_ACTIVITY));
                                        i.addFlags(268435456);
                                        Launcher_EnglishPage_ViewHolder.this.mContext.startActivity(i);
                                    } catch (Throwable th) {
                                    }
                                }
                            }
                        }).checkUpdate("com.iflytek.dictatewords", new PackageUtils(this.mContext).getVersionCode("com.iflytek.dictatewords"));
                        return;
                    } else {
                        Toast.makeText(this.mContext, "请检查是否安装了记单词应用", 0).show();
                        return;
                    }
                } else {
                    return;
                }
            case R.id.img_recite_texts:
                Log.d(TAG, "img_recite_texts");
                EtsUtils.launchEts(this.mContext, EtsConstant.ACTION_READ_COURSE, this.userInfo);
                StatsHelper.englishPageClickedBKW();
                return;
            case R.id.img_book_recite:
                Log.d(TAG, "img_book_recite");
                EtsUtils.launchEts(this.mContext, EtsConstant.ACTION_BOOK_RECITE, this.userInfo);
                StatsHelper.englishPageClickedBookReicte();
                return;
            case R.id.img_writing:
                Log.d(TAG, "img_writing");
                EtsUtils.launchEts(this.mContext, "com.ets100.study.ACTION_COMPOSITION_CHECK", this.userInfo);
                StatsHelper.englishPageClickedXZW();
                return;
            default:
                return;
        }
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        for (Fragment fragment : this.fragments) {
            if (fragment != null) {
                try {
                    transaction.hide(fragment);
                    this.fragments.remove(fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getStatPageName() {
        return "Fragment_English";
    }
}
