package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.zhouwei.library.CustomPopWindow;
import com.iflytek.framelib.base.Constants;
import com.iflytek.stats.StatsCode;
import com.iflytek.stats.StatsHelper;
import com.iflytek.utils.LogAgentHelper;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.SubjectEntity;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.ChapterPracticeResponse;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.api.response.StepExamResponse;
import com.toycloud.launcher.api.response.SubjectResponse;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.holder.helper.RatingBarHelper;
import com.toycloud.launcher.holder.mapping.MappingWebViewHelper;
import com.toycloud.launcher.holder.presenter.CatalogItem;
import com.toycloud.launcher.holder.presenter.SegmentsPresenter;
import com.toycloud.launcher.holder.presenter.StudySnapshotPresenter;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.EtsConstant;
import com.toycloud.launcher.myinterface.Listener_Update;
import com.toycloud.launcher.myinterface.WifiConnectInterface;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.UpdateUtil;
import com.toycloud.launcher.view.CircularProgressBar;
import com.toycloud.launcher.view.DialogUtil;
import com.toycloud.launcher.view.RiseNumberTextView;
import com.toycloud.launcher.view.SegmentCardView;
import com.toycloud.launcher.view.TabLayout;
import framework.hz.salmon.util.NetworkUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Launcher_FirstPage_ViewHolder extends BaseHolder implements View.OnClickListener, StudySnapshotPresenter.IUserStudyView, TabLayout.OnTabSelectedListener, SegmentsPresenter.ISegmentsView {
    public static final DecimalFormat SCORE_FORMAT = new DecimalFormat("##0.0");
    private static final TabLayout.TabInfo TAB_CHEMICAL = new TabLayout.TabInfo("化学");
    private static final int TAB_INDEX_CHEMICAL = 2;
    private static final int TAB_INDEX_MATH = 0;
    private static final int TAB_INDEX_PHYSICAL = 1;
    private static final int TAB_INDEX_SCIENCE = 3;
    private static final TabLayout.TabInfo TAB_MATH = new TabLayout.TabInfo("数学");
    private static final TabLayout.TabInfo TAB_PHYSICAL = new TabLayout.TabInfo("物理");
    private static final TabLayout.TabInfo TAB_SCIENCE = new TabLayout.TabInfo("科学");
    private static final String TAG = "FirstPage_ViewHolder";
    private static final List<TabLayout.TabInfo> sTABS_A = new ArrayList(1);
    private static final List<TabLayout.TabInfo> sTABS_B = new ArrayList(2);
    private static final List<TabLayout.TabInfo> sTABS_C = new ArrayList(3);
    private static final List<TabLayout.TabInfo> sTABS_D = new ArrayList(4);
    private View activity_main;
    private boolean isDiagnosis = false;
    private boolean isFirstStart;
    ImageView iv_start;
    private View mAskView;
    private SegmentCardView mChapterSegmentCardView;
    /* access modifiers changed from: private */
    public Context mContext;
    private RiseNumberTextView mCoverPointsView;
    private SegmentCardView mExamSegmentCardView;
    /* access modifiers changed from: private */
    public boolean mIsLoadingWeb = false;
    private View mLoginnedLayout;
    /* access modifiers changed from: private */
    public MappingInfoResponse mMappingInfo;
    private TextView mMappingStateTV;
    private View mMappingStateView;
    private View mNeedLoginLayout;
    private View mNotStartView;
    private CustomPopWindow mPopWindowMasteryDegree;
    private CustomPopWindow mPopWindowStudyRecommend;
    /* access modifiers changed from: private */
    public StudySnapshotPresenter mPresenter;
    private CircularProgressBar mProgressBar;
    private RatingBarHelper mRatingBarHelper;
    private View mSectionLayout;
    private TextView mSectionView;
    private TabLayout mSubjectsTab;
    private ViewStub mViewStub;
    /* access modifiers changed from: private */
    public WebView mWebView;
    private SegmentCardView mWrongSegmentCardView;
    private WifiConnectInterface mainClickListener;
    private View rel_guider_first;
    private TextView tv_notice_top;
    private View view_half_tru;

    static {
        sTABS_A.add(TAB_MATH);
        sTABS_B.add(TAB_MATH);
        sTABS_C.add(TAB_MATH);
        sTABS_D.add(TAB_MATH);
        sTABS_B.add(TAB_PHYSICAL);
        sTABS_C.add(TAB_PHYSICAL);
        sTABS_D.add(TAB_PHYSICAL);
        sTABS_C.add(TAB_CHEMICAL);
        sTABS_D.add(TAB_CHEMICAL);
        sTABS_D.add(TAB_SCIENCE);
    }

    private static final void resetSelections(List<TabLayout.TabInfo> tabInfos) {
        for (TabLayout.TabInfo info : tabInfos) {
            info.setSelected(false);
        }
    }

    public Launcher_FirstPage_ViewHolder(Context context, WifiConnectInterface mainClickListener2) {
        super(context);
        this.mContext = context;
        this.mainClickListener = mainClickListener2;
        SegmentsPresenter p = new SegmentsPresenter(this.mContext);
        p.attachView(this);
        this.mPresenter = new StudySnapshotPresenter(this.mContext, p);
        this.mPresenter.attachView(this);
        showGlobalContent(this.mPresenter.isLogin());
    }

    /* access modifiers changed from: protected */
    public void initDataBeforeInflate(Context context) {
        super.initDataBeforeInflate(context);
        this.isFirstStart = SharepreferenceUtil.getSharepferenceInstance(context).isFirstStart();
        Logger.d(TAG, "isFirstStart = " + this.isFirstStart);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.viewhold_first_recommend;
    }

    private void inflateFromViewStub() {
        if (this.mViewStub != null) {
            View rootView = this.mViewStub.inflate();
            this.mNeedLoginLayout = rootView.findViewById(R.id.activity_need_login);
            this.iv_start = (ImageView) rootView.findViewById(R.id.iv_start);
            this.iv_start.setOnClickListener(this);
            this.rel_guider_first = rootView.findViewById(R.id.rel_guider_first);
            this.rel_guider_first.setOnClickListener(this);
            this.view_half_tru = rootView.findViewById(R.id.view_half_tru);
            this.view_half_tru.setOnClickListener(this);
            this.tv_notice_top = (TextView) rootView.findViewById(R.id.tv_notice_top);
            this.mViewStub = null;
            ((AnimationDrawable) this.iv_start.getDrawable()).start();
        }
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.mViewStub = (ViewStub) rootView.findViewById(R.id.first_page_viewstub);
        this.activity_main = rootView.findViewById(R.id.activity_main);
        this.activity_main.setOnClickListener(this);
        if (this.isFirstStart) {
            inflateFromViewStub();
            this.tv_notice_top.setVisibility(8);
            this.rel_guider_first.setVisibility(0);
            Logger.d(TAG, "intView | rel_guider_first->VISIBLE");
            this.view_half_tru.setVisibility(8);
        } else {
            Logger.d(TAG, "intView | rel_guider_first->DO NOTHING");
        }
        initMappingLayout();
    }

    private void initMappingLayout() {
        View rootView = getRootView();
        this.mRatingBarHelper = new RatingBarHelper(rootView);
        this.mNotStartView = rootView.findViewById(R.id.mapping_not_started);
        this.mAskView = rootView.findViewById(R.id.iv_tip_mastery_degree);
        this.mCoverPointsView = (RiseNumberTextView) rootView.findViewById(R.id.tv_cover_point);
        this.mCoverPointsView.setText(EtsConstant.SUCCESS);
        this.mLoginnedLayout = rootView.findViewById(R.id.activity_mapping);
        this.mSubjectsTab = (TabLayout) rootView.findViewById(R.id.tab_subjects);
        this.mSectionLayout = rootView.findViewById(R.id.left_layout);
        this.mSectionView = (TextView) rootView.findViewById(R.id.chapter_section_info);
        this.mWebView = (WebView) rootView.findViewById(R.id.webview_map);
        this.mMappingStateView = rootView.findViewById(R.id.layout_mapping_state);
        this.mMappingStateTV = (TextView) rootView.findViewById(R.id.tv_mapping_state);
        this.mProgressBar = (CircularProgressBar) rootView.findViewById(R.id.loading_progress);
        this.mChapterSegmentCardView = (SegmentCardView) rootView.findViewById(R.id.segment_chapter);
        this.mExamSegmentCardView = (SegmentCardView) rootView.findViewById(R.id.segment_exam);
        this.mWrongSegmentCardView = (SegmentCardView) rootView.findViewById(R.id.segment_wrong);
        this.mIsLoadingWeb = true;
        MappingWebViewHelper.setWebViewClickListener(this.mWebView, this);
        MappingWebViewHelper.initWebView(this.mWebView, new WebViewClient() {
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(Launcher_FirstPage_ViewHolder.TAG, "onReceivedError: " + error + ", retry load url...");
                MappingWebViewHelper.loadUrl(Launcher_FirstPage_ViewHolder.this.mWebView);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                boolean unused = Launcher_FirstPage_ViewHolder.this.mIsLoadingWeb = false;
                if (Launcher_FirstPage_ViewHolder.this.mMappingInfo != null) {
                    MappingWebViewHelper.showMapping(Launcher_FirstPage_ViewHolder.this.mWebView, Launcher_FirstPage_ViewHolder.this.mMappingInfo, false);
                }
            }
        });
        setClickListener(this.mChapterSegmentCardView);
        setClickListener(this.mExamSegmentCardView);
        setClickListener(this.mWrongSegmentCardView);
        setClickListener(this.mAskView);
        setClickListener(this.mSectionLayout);
        this.mSubjectsTab.setOnTabSelectedListener(this);
    }

    private void setClickListener(View view) {
        view.setOnClickListener(this);
    }

    public void onCreate() {
        super.onCreate();
        if (this.isFirstStart) {
            inflateFromViewStub();
            this.tv_notice_top.setVisibility(8);
            this.rel_guider_first.setVisibility(0);
            Logger.d(TAG, "onCreate | rel_guider_first->VISIBLE");
            this.view_half_tru.setVisibility(8);
        } else {
            if (this.tv_notice_top != null) {
                this.tv_notice_top.setVisibility(8);
                this.rel_guider_first.setVisibility(8);
                this.view_half_tru.setVisibility(8);
            }
            Logger.d(TAG, "onCreate | rel_guider_first->DO NOTHING");
        }
        LogAgentHelper.onPageStart(getStatPageName());
    }

    private void recordStudyLog(String tabCode) {
        String opCode;
        String subject = this.mPresenter.getCurrentSubject();
        if (TextUtils.equals(subject, "02")) {
            if (TextUtils.equals(tabCode, GlobalVariable.TAB_CODE_JJQ)) {
                opCode = StatsCode.CLICK_MATH_JJQ;
            } else if (TextUtils.equals(tabCode, GlobalVariable.TAB_CODE_ZZL)) {
                opCode = StatsCode.CLICK_MATH_ZZL;
            } else {
                opCode = StatsCode.CLICK_MATH_JDC;
            }
        } else if (TextUtils.equals(subject, "05")) {
            if (TextUtils.equals(tabCode, GlobalVariable.TAB_CODE_JJQ)) {
                opCode = StatsCode.CLICK_PHYSICAL_JJQ;
            } else if (TextUtils.equals(tabCode, GlobalVariable.TAB_CODE_ZZL)) {
                opCode = StatsCode.CLICK_PHYSICAL_ZZL;
            } else {
                opCode = StatsCode.CLICK_PHYSICAL_JDC;
            }
        } else if (!TextUtils.equals(subject, "06")) {
            return;
        } else {
            if (TextUtils.equals(tabCode, GlobalVariable.TAB_CODE_JJQ)) {
                opCode = StatsCode.CLICK_CHEMICAL_JJQ;
            } else if (TextUtils.equals(tabCode, GlobalVariable.TAB_CODE_ZZL)) {
                opCode = StatsCode.CLICK_CHEMICAL_ZZL;
            } else {
                opCode = StatsCode.CLICK_CHEMICAL_JDC;
            }
        }
        StatsHelper.firstPageClickedJZJ(opCode);
    }

    private void clickStart(final String tabCode) {
        if (!NoDoubleClickUtils.isDoubleClick()) {
            if (this.isFirstStart) {
                if (this.view_half_tru != null) {
                    this.view_half_tru.setVisibility(8);
                    this.tv_notice_top.setVisibility(8);
                }
                this.mainClickListener.gouiderFinish();
                LogAgentHelper.onOPLogEvent(StatsCode.CLICK_STUDY_SYSTEM_NOT_LOGIN, (Map<String, String>) null);
                this.isFirstStart = SharepreferenceUtil.getSharepferenceInstance(this.mContext).isFirstStart();
            } else if (!GlobalVariable.isLogin()) {
                LogAgentHelper.onOPLogEvent(StatsCode.CLICK_STUDY_SYSTEM_NOT_LOGIN, (Map<String, String>) null);
                DialogUtil.showDialogToLogin(this.mContext);
            } else {
                recordStudyLog(tabCode);
                if (isAvilible(this.mContext, "com.iflytek.recommend_tsp")) {
                    new UpdateUtil(this.mContext, new Listener_Update() {
                        public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                            String subjectCode;
                            if (!isMustUpdate && !isUpdate) {
                                try {
                                    if (Launcher_FirstPage_ViewHolder.this.mPresenter == null) {
                                        subjectCode = "02";
                                    } else {
                                        subjectCode = Launcher_FirstPage_ViewHolder.this.mPresenter.getCurrentSubject();
                                    }
                                    Intent intent = new Intent();
                                    ComponentName cn = new ComponentName("com.iflytek.recommend_tsp", GlobalVariable.RECOMMEND_TSP_STUDY_SYSTEM__ACTIVITY);
                                    intent.putExtra("token", SharepreferenceUtil.getToken());
                                    intent.putExtra(Constants.GRADE_CODE, Launcher_FirstPage_ViewHolder.this.userInfo.getGradecode());
                                    intent.putExtra("username", Launcher_FirstPage_ViewHolder.this.userInfo.getUsername());
                                    intent.putExtra(AppContants.KEY_USER_ID, Launcher_FirstPage_ViewHolder.this.userInfo.getUserid());
                                    intent.putExtra(GlobalVariable.KEY_SUBJECT_CODE, subjectCode);
                                    intent.putExtra(GlobalVariable.KEY_TAB_CODE, tabCode);
                                    intent.setComponent(cn);
                                    intent.addFlags(32768);
                                    Launcher_FirstPage_ViewHolder.this.mContext.startActivity(intent);
                                    Log.e("getGradecode-->", Launcher_FirstPage_ViewHolder.this.userInfo.getGradecode());
                                } catch (Throwable exp) {
                                    Log.e(Launcher_FirstPage_ViewHolder.TAG, "启动精准学习系统失败", exp);
                                }
                            }
                        }
                    }).checkUpdate("com.iflytek.recommend_tsp", new PackageUtils(this.mContext).getVersionCode("com.iflytek.recommend_tsp"));
                } else {
                    Toast.makeText(this.mContext, "请检查是否安装了个性化推荐应用", 0).show();
                }
            }
        }
    }

    private void recordWrongEvent() {
        String opCode;
        String subject = this.mPresenter.getCurrentSubject();
        if (TextUtils.equals(subject, "02")) {
            opCode = StatsCode.CLICK_MATH_WRONG;
        } else if (TextUtils.equals(subject, "05")) {
            opCode = StatsCode.CLICK_PHYSICAL_WRONG;
        } else {
            opCode = StatsCode.CLICK_CHEMICAL_WRONG;
        }
        StatsHelper.firstPageClickedWrong(opCode);
    }

    private void clickWrongQuestionNoteBook() {
        Logger.d(TAG, "clickWrongQuestionNoteBook");
        if (!GlobalVariable.isLogin() || NoDoubleClickUtils.isDoubleClick()) {
            LogAgentHelper.onOPLogEvent(StatsCode.CLICK_STUDY_SYSTEM_NOT_LOGIN, (Map<String, String>) null);
            DialogUtil.showDialogToLogin(this.mContext);
            return;
        }
        try {
            recordWrongEvent();
            if (isAvilible(this.mContext, "com.iflytek.wrongnotebook")) {
                new UpdateUtil(this.mContext, new Listener_Update() {
                    public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                        if (!isMustUpdate && !isUpdate) {
                            try {
                                Intent intent = new Intent();
                                String subject = null;
                                if (Launcher_FirstPage_ViewHolder.this.mPresenter != null) {
                                    subject = Launcher_FirstPage_ViewHolder.this.mPresenter.getCurrentSubject();
                                }
                                if (TextUtils.isEmpty(subject)) {
                                    subject = "02";
                                }
                                intent.addFlags(268435456);
                                intent.putExtra("token", Launcher_FirstPage_ViewHolder.this.userInfo.getToken());
                                intent.putExtra(AppContants.KEY_GRADE_NAME, GradeUtil.getGradeName(Launcher_FirstPage_ViewHolder.this.userInfo.getGradecode()));
                                intent.putExtra(AppContants.KEY_GRADE_CODE, Launcher_FirstPage_ViewHolder.this.userInfo.getGradecode());
                                intent.putExtra(GlobalVariable.KEY_SUBJECT_CODE, subject);
                                intent.setComponent(GlobalVariable.createWrongNotebookComponentName());
                                Launcher_FirstPage_ViewHolder.this.mContext.startActivity(intent);
                            } catch (Throwable exp) {
                                Log.e(Launcher_FirstPage_ViewHolder.TAG, "启动错题本失败", exp);
                            }
                        }
                    }
                }).checkUpdate("com.iflytek.wrongnotebook", new PackageUtils(this.mContext).getVersionCode("com.iflytek.wrongnotebook"));
            } else {
                Toast.makeText(this.mContext, "请检查是否安装了错题本应用", 0).show();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.segment_chapter:
                clickStart(GlobalVariable.TAB_CODE_ZZL);
                return;
            case R.id.segment_exam:
                clickStart(GlobalVariable.TAB_CODE_JDC);
                return;
            case R.id.segment_wrong:
                clickWrongQuestionNoteBook();
                return;
            case R.id.left_layout:
            case R.id.webview_map:
                clickStart(GlobalVariable.TAB_CODE_JJQ);
                return;
            case R.id.iv_tip_mastery_degree:
                onClickAskMasteryDegree();
                return;
            case R.id.view_half_tru:
                this.view_half_tru.setVisibility(8);
                this.tv_notice_top.setVisibility(8);
                this.mainClickListener.gouiderFinish();
                return;
            case R.id.iv_start:
                clickStart(GlobalVariable.TAB_CODE_JJQ);
                return;
            case R.id.rel_guider_first:
                this.rel_guider_first.setVisibility(8);
                this.view_half_tru.setVisibility(0);
                this.tv_notice_top.setVisibility(0);
                Logger.d(TAG, "onClick | rel_guider_first->GONE");
                return;
            default:
                return;
        }
    }

    private void onClickAskMasteryDegree() {
        if (this.mPopWindowMasteryDegree == null) {
            this.mPopWindowMasteryDegree = new CustomPopWindow.PopupWindowBuilder(this.mContext).setView(LayoutInflater.from(this.mContext).inflate(R.layout.pop_tip_mastery_degree, (ViewGroup) null)).create();
        }
        this.mPopWindowMasteryDegree.showAsDropDown(this.mAskView, -96, 0);
    }

    public void onClickAskMethod() {
    }

    private void onClickMath() {
        Logger.d(TAG, "onClickMath");
        StatsHelper.firstPageClickMathTab();
        if (this.mPresenter != null && !TextUtils.equals(this.mPresenter.getCurrentSubject(), "02")) {
            this.mPresenter.onSelectedSubject("02");
        }
    }

    private void onClickPhysical() {
        Logger.d(TAG, "onClickPhysical");
        StatsHelper.firstPageClickPhysicalTab();
        if (this.mPresenter != null && !TextUtils.equals(this.mPresenter.getCurrentSubject(), "05")) {
            this.mPresenter.onSelectedSubject("05");
        }
    }

    private void onClickChemical() {
        StatsHelper.firstPageClickChemical();
        Logger.d(TAG, "onClickChemical");
        if (this.mPresenter != null && !TextUtils.equals(this.mPresenter.getCurrentSubject(), "06")) {
            this.mPresenter.onSelectedSubject("06");
        }
    }

    private void onClickScience() {
        StatsHelper.firstPageClickChemical();
        Logger.d(TAG, "onClickScience");
        if (this.mPresenter != null && !TextUtils.equals(this.mPresenter.getCurrentSubject(), "19")) {
            this.mPresenter.onSelectedSubject("19");
        }
    }

    public void showGlobalContent(boolean isLogin) {
        if (!isLogin) {
            inflateFromViewStub();
            this.mNeedLoginLayout.setVisibility(0);
            this.mLoginnedLayout.setVisibility(8);
            return;
        }
        if (this.mNeedLoginLayout != null) {
            this.mNeedLoginLayout.setVisibility(8);
        }
        this.mLoginnedLayout.setVisibility(0);
    }

    public void showSubjects(User user, int grade) {
        int index;
        if (this.mPresenter != null) {
            boolean isSubjectChanged = false;
            String currentSubject = this.mPresenter.getCurrentSubject();
            if (currentSubject == null) {
                currentSubject = "02";
                isSubjectChanged = true;
            }
            if (user == null || !user.isZheJiang()) {
                this.mSubjectsTab.initTabLayout(sTABS_C);
            } else {
                this.mSubjectsTab.initTabLayout(sTABS_D);
            }
            if (TextUtils.equals(currentSubject, "02")) {
                index = 0;
            } else if (TextUtils.equals(currentSubject, "05")) {
                index = 1;
            } else if (TextUtils.equals(currentSubject, "06")) {
                index = 2;
            } else if (!TextUtils.equals(currentSubject, "19") || user == null || !user.isZheJiang()) {
                currentSubject = "02";
                isSubjectChanged = true;
                index = 0;
            } else {
                index = 3;
            }
            this.mSubjectsTab.setCurrentItem(index);
            if (isSubjectChanged) {
                this.mPresenter.onSelectedSubject(currentSubject);
            }
        }
    }

    public void showMapping(String subjectCode, MappingInfoResponse response) {
        int totalPoints = MappingWebViewHelper.getTotalPoint(response);
        if (totalPoints <= 0) {
            showMappingType(3);
            return;
        }
        int coverPoints = MappingWebViewHelper.getCover2Point(response);
        this.mNotStartView.setVisibility(8);
        this.mWebView.setVisibility(0);
        this.mMappingStateView.setVisibility(8);
        this.mMappingInfo = response;
        if (!this.mIsLoadingWeb) {
            MappingWebViewHelper.showMapping(this.mWebView, response, false);
        }
        this.mCoverPointsView.setText(String.valueOf(totalPoints));
        this.mRatingBarHelper.showLights(response, coverPoints);
    }

    private void showMappingResource(String text, int imageResId) {
        this.mMappingStateTV.setVisibility(0);
        this.mMappingStateTV.setText(text);
        this.mMappingStateTV.setCompoundDrawablesWithIntrinsicBounds(0, imageResId, 0, 0);
    }

    public void showMappingType(int mappingType) {
        Log.d(TAG, "showMappingType: " + mappingType);
        this.mWebView.setVisibility(8);
        if (mappingType == 4) {
            this.mSectionView.setText((CharSequence) null);
        }
        this.mCoverPointsView.setText(EtsConstant.SUCCESS);
        this.mRatingBarHelper.showLights((MappingInfoResponse) null, 0);
        this.mMappingStateView.setVisibility(0);
        if (mappingType == 1 || mappingType == 2) {
            if (mappingType == 2 || !NetworkUtil.isNetworkAvailable(this.mMappingStateView.getContext())) {
                showMappingResource("加载出错", R.drawable.ic_network_error);
            } else {
                showMappingResource("加载出错", R.drawable.ic_exception);
            }
            this.mProgressBar.setVisibility(8);
            this.mNotStartView.setVisibility(8);
        } else if (mappingType == 0) {
            showMappingResource("加载中...", 0);
            this.mProgressBar.setVisibility(0);
            this.mNotStartView.setVisibility(8);
        } else if (mappingType == 3) {
            showMappingResource("该章节下没有考点", R.drawable.ic_empty_mapping);
            this.mProgressBar.setVisibility(8);
            this.mNotStartView.setVisibility(8);
        } else if (mappingType == 4) {
            this.mMappingStateTV.setVisibility(8);
            this.mProgressBar.setVisibility(8);
            this.mNotStartView.setVisibility(0);
        }
    }

    public void showStudySectionInfo(CatalogItem catalogInfo) {
        if (catalogInfo != null && catalogInfo.chapter != null) {
            StringBuilder sb = new StringBuilder();
            List<String> nameList = catalogInfo.getFullName();
            int size = nameList.size();
            for (int i = 0; i < size; i++) {
                String name = nameList.get(i);
                if (i < size - 1) {
                    sb.append(name).append(" > ");
                } else {
                    sb.append(name);
                }
            }
            String text = sb.toString();
            try {
                Log.d(TAG, "当前进度：" + text);
                this.mSectionView.setText(text);
            } catch (Throwable th) {
            }
        }
    }

    public void onTabSelected(int position) {
        if (position == 0) {
            onClickMath();
        } else if (position == 1) {
            onClickPhysical();
        } else if (position == 2) {
            onClickChemical();
        } else {
            onClickScience();
        }
    }

    public void showChapterInfo(String subjectCode, ChapterPracticeResponse response) {
        if (response == null || response.getStatus() != 0) {
            if (response == null || response.getStatus() != 10003) {
                this.mChapterSegmentCardView.setDetail((CharSequence) null);
                this.mChapterSegmentCardView.setProgress((CharSequence) null);
                this.mChapterSegmentCardView.setTip((String) null);
                return;
            }
            this.mChapterSegmentCardView.setDetail((CharSequence) null);
            this.mChapterSegmentCardView.setProgress((CharSequence) null);
            this.mChapterSegmentCardView.setTip("还未开始练习");
        } else if (response.data == null) {
            this.mChapterSegmentCardView.setDetail((CharSequence) null);
            this.mChapterSegmentCardView.setProgress((CharSequence) null);
            this.mChapterSegmentCardView.setTip("还未开始练习");
        } else {
            this.mChapterSegmentCardView.setProgress(response.data.chapterName);
            this.mChapterSegmentCardView.setDetail(new SpannableString("得分：" + SCORE_FORMAT.format(response.data.score)));
            this.mChapterSegmentCardView.setTip((String) null);
        }
    }

    public void showExamInfo(String subjectCode, StepExamResponse response) {
        if (response == null || response.getStatus() != 0) {
            if (response == null || response.getStatus() != 10003) {
                this.mExamSegmentCardView.setTip((String) null);
                this.mExamSegmentCardView.setProgress((CharSequence) null);
                this.mExamSegmentCardView.setDetail((CharSequence) null);
                return;
            }
            this.mExamSegmentCardView.setTip("还未开始练习");
            this.mExamSegmentCardView.setProgress((CharSequence) null);
            this.mExamSegmentCardView.setDetail((CharSequence) null);
        } else if (response.data != null) {
            this.mExamSegmentCardView.setProgress(response.data.name);
            this.mExamSegmentCardView.setDetail(new SpannableString("得分：" + SCORE_FORMAT.format(response.data.score)));
            this.mExamSegmentCardView.setTip((String) null);
        } else {
            this.mExamSegmentCardView.setTip("还未开始练习");
            this.mExamSegmentCardView.setProgress((CharSequence) null);
            this.mExamSegmentCardView.setDetail((CharSequence) null);
        }
    }

    public void showWrongNoteBookInfo(SubjectResponse entity) {
        SubjectEntity cur;
        if (entity != null && entity.data != null && this.mPresenter != null && (cur = entity.getEntityBySubjectCode(this.mPresenter.getCurrentSubject())) != null) {
            if (cur.getTotalReviewCount() <= 0) {
                this.mWrongSegmentCardView.setTip("暂无错题");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("共 ").append(cur.getTotalReviewCount()).append("道题，完成复习").append(cur.getTotalReviewCount() - cur.getUnReviewCount()).append("道");
            this.mWrongSegmentCardView.setTip(sb.toString());
        }
    }

    public void onScrollOut() {
        super.onScrollOut();
        if (this.mPresenter != null) {
            this.mPresenter.onHide();
        }
    }

    public void onScrollIn() {
        super.onScrollIn();
        if (this.mPresenter != null) {
            this.mPresenter.onEnter();
        }
    }

    public void onActivityResume(boolean isCurrentPage) {
        super.onActivityResume(isCurrentPage);
        if (this.mPresenter != null) {
            this.mPresenter.onEnter();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "AAAAA: onDestroy");
        if (this.mPresenter != null) {
            this.mPresenter.onDestroy();
            this.mPresenter = null;
        }
    }

    public void onDetachFromAdapter(boolean isCurrentPage) {
        Logger.d(TAG, "AAAAA: onDetachFromAdapter");
        super.onDetachFromAdapter(isCurrentPage);
        if (this.mPresenter != null) {
            this.mPresenter.onDestroy();
            this.mPresenter = null;
        }
    }

    public void onAttachToAdapter(boolean isCurrentPage, boolean isTopActivity) {
        super.onAttachToAdapter(isCurrentPage, isTopActivity);
        if (this.mPresenter == null) {
            SegmentsPresenter p = new SegmentsPresenter(this.mContext);
            p.attachView(this);
            this.mPresenter = new StudySnapshotPresenter(this.mContext, p);
            this.mPresenter.attachView(this);
            showGlobalContent(this.mPresenter.isLogin());
        }
    }

    public String getStatPageName() {
        return "Fragment_StudySystem";
    }
}
