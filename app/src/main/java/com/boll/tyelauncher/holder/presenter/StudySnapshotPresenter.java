package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.iflytek.alarm.manager.bean.AppInfo;
import com.iflytek.alarm.manager.service.AlarmClockService;
import com.iflytek.cbg.aistudy.biz.subjects.SubjectHelper;
import com.iflytek.cloud.SpeechConstant;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.BuildConfig;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.debug.MainDebugger;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.holder.model.StudySnapshotItem;
import com.toycloud.launcher.holder.presenter.SubjectMapQuerier;
import com.toycloud.launcher.receiver.voicehelper.AiStudySystemHandler;
import com.toycloud.launcher.ui.listener.GlobalUserInfoListener;
import com.toycloud.launcher.ui.listener.UserListener;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.receiver.EventReceiver;
import com.toycloud.launcher.util.receiver.ReceiverHandler;
import framework.hz.salmon.retrofit.ApiException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import retrofit2.HttpException;

public class StudySnapshotPresenter implements UserListener {
    private static final String ACTION_LOOP = "com.iflytek.cbg.launcher.LOOP";
    private static final String ACTION_USER_SOLVE_PROBLEM = "com.iflytek.cbg.action.SOLVE_PROBLEM";
    private static final long LOOP_DURATION_SECONDS_DEBUG = 300;
    private static final long LOOP_DURATION_SECONDS_RELEASE = 3600;
    public static final int MAPPING_TYPE_ERROR = 1;
    public static final int MAPPING_TYPE_HTTP_ERROR = 2;
    public static final int MAPPING_TYPE_LOADING = 0;
    public static final int MAPPING_TYPE_NOT_START_STUDY = 4;
    public static final int MAPPING_TYPE_NO_MAP = 3;
    private static final String TAG = "StudySnapshotPresenter";
    /* access modifiers changed from: private */
    public RequestCache mCache;
    private ConnectivityManager mConnectivityManager;
    /* access modifiers changed from: private */
    public ContentObserver mContentObserver;
    private final Context mContext;
    /* access modifiers changed from: private */
    public String mCurrentSubject;
    private EventReceiver mEventReceiver;
    private boolean mIsLoadingCache = true;
    /* access modifiers changed from: private */
    public final MapQuerier mMapQuerier;
    /* access modifiers changed from: private */
    public NetworkCallback mNetworkCallback;
    private SegmentsPresenter mSegmentsPresenter;
    /* access modifiers changed from: private */
    public IUserStudyView mStudyView;
    /* access modifiers changed from: private */
    public IUserStudyView mStudyViewProxy;
    /* access modifiers changed from: private */
    public UserStudyState mUserStudyState;

    public interface IUserStudyView {
        void showGlobalContent(boolean z);

        void showMapping(String str, MappingInfoResponse mappingInfoResponse);

        void showMappingType(int i);

        void showStudySectionInfo(CatalogItem catalogItem);

        void showSubjects(User user, int i);
    }

    public StudySnapshotPresenter(Context context, SegmentsPresenter p) {
        String str;
        long time = System.currentTimeMillis();
        try {
            this.mContext = context.getApplicationContext();
            this.mSegmentsPresenter = p;
            if (this.mSegmentsPresenter == null) {
                Logger.e(TAG, "AAAAA: mSegmentsPresenter is null");
            }
            this.mCache = new RequestCache(context);
            GlobalUserInfoListener.getInstance().addListener(this);
            this.mStudyViewProxy = (IUserStudyView) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{IUserStudyView.class}, new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (StudySnapshotPresenter.this.mStudyView == null) {
                        return null;
                    }
                    try {
                        return method.invoke(StudySnapshotPresenter.this.mStudyView, args);
                    } catch (Throwable exp) {
                        exp.printStackTrace();
                        return null;
                    }
                }
            });
            listenNetwork();
            listenSystemEvent();
            this.mMapQuerier = new MapQuerier(context, new MapCallback());
            this.mUserStudyState = new UserStudyState(SharepreferenceUtil.getSharepferenceInstance(this.mContext).getUserInfo());
            selectSubjectsWithGrade();
            Log.d(TAG, "AAAAA: loading cache A: " + this);
            loadCache();
        } finally {
            str = "StudySnapshotPresenter构造函数耗时：";
            Log.d(TAG, str + (System.currentTimeMillis() - time));
        }
    }

    public boolean isLogin() {
        return this.mUserStudyState.mUser != null;
    }

    private boolean isIn(String subject, String... subjects) {
        if (subject == null) {
            return false;
        }
        for (String sub : subjects) {
            if (TextUtils.equals(sub, subject)) {
                return true;
            }
        }
        return false;
    }

    private void selectSubjectsWithGrade() {
        int grade;
        if (this.mUserStudyState.mUser != null && (grade = this.mUserStudyState.mUser.getGradeCodeInt(-1)) >= 0) {
            if (this.mUserStudyState.mUser.isZheJiang()) {
                if (!isIn(this.mCurrentSubject, "02", "05", "06", "19")) {
                    this.mCurrentSubject = "02";
                }
            } else {
                if (!isIn(this.mCurrentSubject, "02", "05", "06")) {
                    this.mCurrentSubject = "02";
                }
            }
            AiStudySystemHandler.getInstance().setSubjectCode(this.mCurrentSubject);
            if (this.mStudyView != null) {
                this.mStudyView.showSubjects(this.mUserStudyState.mUser, grade);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean matchCurrentSubject(String subject) {
        if (this.mCurrentSubject == null) {
            return TextUtils.equals(subject, "02");
        }
        return TextUtils.equals(subject, this.mCurrentSubject);
    }

    private void loadCache() {
        if (!this.mUserStudyState.isUserValid()) {
            this.mIsLoadingCache = false;
            return;
        }
        Log.d(TAG, "AAAAA: loading cache");
        String userId = this.mUserStudyState.getUserId();
        String gradeCode = this.mUserStudyState.getGradeCode();
        this.mIsLoadingCache = true;
        RxCacheLoader.loadCache(this.mContext, this.mCache, userId, gradeCode).subscribe(new StudySnapshotPresenter$$Lambda$0(this), new StudySnapshotPresenter$$Lambda$1(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$loadCache$6$StudySnapshotPresenter(Object object) throws Exception {
        this.mIsLoadingCache = false;
        parseCacheResult(object);
        queryForLoadCacheComplete();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$loadCache$7$StudySnapshotPresenter(Throwable throwable) throws Exception {
        this.mIsLoadingCache = false;
        queryForLoadCacheComplete();
    }

    private void parseCacheResult(Object cache) {
        if (cache != GlobalVariable.NULL) {
            Map<String, Object> cacheMap = (Map) cache;
            this.mUserStudyState.mMathSnapshot.mCurrentCatalog = (CatalogItem) cacheMap.get(RequestCache.KEY_MAP_REQUEST_MATH);
            this.mUserStudyState.mMathSnapshot.mNewCatalog = (CatalogItem) cacheMap.get(RequestCache.KEY_MAP_REQUEST_MATH_NEW);
            this.mUserStudyState.mMathSnapshot.mMappingInfo = (MappingInfoResponse) cacheMap.get(RequestCache.KEY_MAP_RESPONSE_MATH);
            this.mUserStudyState.mPhysicalSnapshot.mCurrentCatalog = (CatalogItem) cacheMap.get(RequestCache.KEY_MAP_REQUEST_PHYSICAL);
            this.mUserStudyState.mPhysicalSnapshot.mNewCatalog = (CatalogItem) cacheMap.get(RequestCache.KEY_MAP_REQUEST_PHYSICAL_NEW);
            this.mUserStudyState.mPhysicalSnapshot.mMappingInfo = (MappingInfoResponse) cacheMap.get(RequestCache.KEY_MAP_RESPONSE_PHYSICAL);
            this.mUserStudyState.mChemicalSnapshot.mCurrentCatalog = (CatalogItem) cacheMap.get(RequestCache.KEY_MAP_REQUEST_CHEMICAL);
            this.mUserStudyState.mChemicalSnapshot.mNewCatalog = (CatalogItem) cacheMap.get(RequestCache.KEY_MAP_REQUEST_CHEMICAL_NEW);
            this.mUserStudyState.mChemicalSnapshot.mMappingInfo = (MappingInfoResponse) cacheMap.get(RequestCache.KEY_MAP_RESPONSE_CHEMICAL);
            this.mUserStudyState.mScienceSnapshot.mCurrentCatalog = (CatalogItem) cacheMap.get(RequestCache.KEY_MAP_REQUEST_SCIENCE);
            this.mUserStudyState.mScienceSnapshot.mNewCatalog = (CatalogItem) cacheMap.get(RequestCache.KEY_MAP_REQUEST_SCIENCE_NEW);
            this.mUserStudyState.mScienceSnapshot.mMappingInfo = (MappingInfoResponse) cacheMap.get(RequestCache.KEY_MAP_RESPONSE_SCIENCE);
        }
    }

    private void tryQueryDataFromCP(String userId, String subject, UserStudySnapshot snapshot) {
        StudySnapshotItem item;
        if (snapshot.mCurrentCatalog == null && (item = StudyCPHelper.query(this.mContext, userId, subject)) != null) {
            snapshot.mCurrentCatalog = item.mCatalogItem;
            snapshot.mMappingInfo = item.mMappingInfo;
        }
    }

    private void displayCacheAndTryQuery(boolean queryAll) {
        if (!this.mIsLoadingCache && this.mUserStudyState.isUserValid()) {
            String userId = this.mUserStudyState.getUserId();
            tryQueryDataFromCP(userId, "02", this.mUserStudyState.mMathSnapshot);
            tryQueryDataFromCP(userId, "05", this.mUserStudyState.mPhysicalSnapshot);
            tryQueryDataFromCP(userId, "06", this.mUserStudyState.mChemicalSnapshot);
            tryQueryDataFromCP(userId, "19", this.mUserStudyState.mScienceSnapshot);
            if (matchCurrentSubject("02")) {
                UserStudySnapshot snapshot = this.mUserStudyState.mMathSnapshot;
                if (snapshot.mCurrentCatalog == null) {
                    this.mStudyViewProxy.showMappingType(4);
                } else {
                    this.mStudyViewProxy.showStudySectionInfo(snapshot.mCurrentCatalog);
                    if (snapshot.mMappingInfo != null) {
                        this.mStudyViewProxy.showMapping("02", snapshot.mMappingInfo);
                        checkMathVerAndQuery();
                    } else {
                        if (!snapshot.hasNewCatalog()) {
                            this.mMapQuerier.queryImmediately(snapshot.mCurrentCatalog);
                        } else if (snapshot.mNewCatalog == null) {
                            this.mMapQuerier.queryImmediately(snapshot.mCurrentCatalog);
                        } else {
                            this.mMapQuerier.queryImmediately(snapshot.mNewCatalog);
                        }
                        this.mStudyViewProxy.showMappingType(0);
                    }
                }
                if (queryAll) {
                    checkPhysicalVerAndQuery();
                    checkChemicalVerAndQuery();
                    checkScienceVerAndQuery();
                }
            } else if (matchCurrentSubject("05")) {
                UserStudySnapshot snapshot2 = this.mUserStudyState.mPhysicalSnapshot;
                if (snapshot2.mCurrentCatalog == null) {
                    this.mStudyViewProxy.showMappingType(4);
                } else {
                    this.mStudyViewProxy.showStudySectionInfo(snapshot2.mCurrentCatalog);
                    if (snapshot2.mMappingInfo != null) {
                        this.mStudyViewProxy.showMapping("05", snapshot2.mMappingInfo);
                        checkPhysicalVerAndQuery();
                    } else {
                        if (!snapshot2.hasNewCatalog()) {
                            this.mMapQuerier.queryImmediately(snapshot2.mCurrentCatalog);
                        } else if (snapshot2.mNewCatalog == null) {
                            this.mMapQuerier.queryImmediately(snapshot2.mCurrentCatalog);
                        } else {
                            this.mMapQuerier.queryImmediately(snapshot2.mNewCatalog);
                        }
                        this.mStudyViewProxy.showMappingType(0);
                    }
                }
                if (queryAll) {
                    checkMathVerAndQuery();
                    checkChemicalVerAndQuery();
                    checkScienceVerAndQuery();
                }
            } else if (matchCurrentSubject("06")) {
                UserStudySnapshot snapshot3 = this.mUserStudyState.mChemicalSnapshot;
                if (snapshot3.mCurrentCatalog == null) {
                    this.mStudyViewProxy.showMappingType(4);
                } else {
                    this.mStudyViewProxy.showStudySectionInfo(snapshot3.mCurrentCatalog);
                    if (snapshot3.mMappingInfo != null) {
                        this.mStudyViewProxy.showMapping("06", snapshot3.mMappingInfo);
                        checkChemicalVerAndQuery();
                    } else if (!snapshot3.hasNewCatalog()) {
                        this.mMapQuerier.queryImmediately(snapshot3.mCurrentCatalog);
                        this.mStudyViewProxy.showMappingType(0);
                    } else if (snapshot3.mNewCatalog == null) {
                        this.mMapQuerier.queryImmediately(snapshot3.mCurrentCatalog);
                    } else {
                        this.mMapQuerier.queryImmediately(snapshot3.mNewCatalog);
                    }
                }
                if (queryAll) {
                    checkMathVerAndQuery();
                    checkPhysicalVerAndQuery();
                    checkScienceVerAndQuery();
                }
            } else {
                UserStudySnapshot snapshot4 = this.mUserStudyState.mScienceSnapshot;
                if (snapshot4.mCurrentCatalog == null) {
                    this.mStudyViewProxy.showMappingType(4);
                } else {
                    this.mStudyViewProxy.showStudySectionInfo(snapshot4.mCurrentCatalog);
                    if (snapshot4.mMappingInfo != null) {
                        this.mStudyViewProxy.showMapping("19", snapshot4.mMappingInfo);
                        checkScienceVerAndQuery();
                    } else if (!snapshot4.hasNewCatalog()) {
                        this.mMapQuerier.queryImmediately(snapshot4.mCurrentCatalog);
                        this.mStudyViewProxy.showMappingType(0);
                    } else if (snapshot4.mNewCatalog == null) {
                        this.mMapQuerier.queryImmediately(snapshot4.mCurrentCatalog);
                    } else {
                        this.mMapQuerier.queryImmediately(snapshot4.mNewCatalog);
                    }
                }
                if (queryAll) {
                    checkMathVerAndQuery();
                    checkPhysicalVerAndQuery();
                    checkChemicalVerAndQuery();
                }
            }
        }
    }

    private void queryForLoadCacheComplete() {
        if (this.mSegmentsPresenter == null) {
            Logger.d(TAG, "AAAAA: queryForLoadCacheComplete: " + this);
            return;
        }
        displayCacheAndTryQuery(true);
        this.mSegmentsPresenter.query(this.mCurrentSubject);
        observeContentProvider();
    }

    private void observeContentProvider() {
        if (this.mUserStudyState.isUserValid()) {
            String userId = this.mUserStudyState.getUserId();
            StudyCPHelper.unRegisterObserver(this.mContext, this.mContentObserver);
            this.mContentObserver = StudyCPHelper.registerObserver(this.mContext, userId, new UserCBObserver());
        }
    }

    private void listenNetwork() {
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        try {
            if (this.mNetworkCallback == null && Build.VERSION.SDK_INT >= 24) {
                this.mNetworkCallback = new NetworkCallback();
                this.mConnectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback);
            }
        } catch (Throwable exp) {
            Log.e(TAG, "listen network error", exp);
            this.mNetworkCallback = null;
        }
    }

    private void listenSystemEvent() {
        Logger.d(TAG, "listenSystemEvent");
        try {
            this.mEventReceiver = new EventReceiver();
            this.mEventReceiver.addAction("android.intent.action.SCREEN_ON", new ScreenOnEventHandler());
            this.mEventReceiver.addAction(ACTION_LOOP, new LoopReceiverHandler());
            this.mEventReceiver.addAction(ACTION_USER_SOLVE_PROBLEM, new UserExerciseHandler());
            this.mEventReceiver.register(this.mContext);
        } catch (Throwable exp) {
            Log.e(TAG, "listenSystemEvent error", exp);
        }
        AppInfo appInfo = new AppInfo();
        appInfo.mAction = ACTION_LOOP;
        appInfo.mPackageName = BuildConfig.APPLICATION_ID;
        appInfo.mAlarmPeriodSeconds = LOOP_DURATION_SECONDS_RELEASE;
        AlarmClockService.addAppInfo(this.mContext, appInfo);
    }

    public void onEnter() {
        Log.d(TAG, "onEnter");
        this.mSegmentsPresenter.displayCache(this.mCurrentSubject);
        this.mSegmentsPresenter.query(this.mCurrentSubject);
        displayCacheAndTryQuery(false);
    }

    public void onSelectedSubject(String subject) {
        Log.d("DEBUG", "onSelectedSubject: " + subject);
        this.mCurrentSubject = subject;
        AiStudySystemHandler.getInstance().setSubjectCode(this.mCurrentSubject);
        this.mSegmentsPresenter.displayCache(subject);
        this.mSegmentsPresenter.query(subject);
        displayCacheAndTryQuery(false);
    }

    public void onHide() {
        Logger.d(TAG, "onHide");
    }

    /* access modifiers changed from: private */
    public void onNetworkConnected() {
        Logger.d(TAG, "onNetworkConnected");
        new Handler(Looper.getMainLooper()).post(new StudySnapshotPresenter$$Lambda$2(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onNetworkConnected$8$StudySnapshotPresenter() {
        if (!this.mIsLoadingCache && this.mUserStudyState.isUserValid()) {
            checkAll();
        }
    }

    /* access modifiers changed from: private */
    public void onLoop() {
        Logger.d(TAG, "onLoop");
        if (!this.mIsLoadingCache) {
            checkMathVerAndQuery();
            checkPhysicalVerAndQuery();
            checkChemicalVerAndQuery();
            checkScienceVerAndQuery();
        }
    }

    public void onDestroy() {
        Logger.d(TAG, "AAAAA: onDestroy: " + this);
        if (this.mEventReceiver != null) {
            this.mEventReceiver.unRegister(this.mContext);
            this.mEventReceiver = null;
        }
        if (!(this.mConnectivityManager == null || this.mNetworkCallback == null)) {
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            this.mNetworkCallback = null;
        }
        GlobalUserInfoListener.getInstance().removeListener(this);
        this.mMapQuerier.cancelAll();
        if (this.mSegmentsPresenter != null) {
            this.mSegmentsPresenter.detachView();
            this.mSegmentsPresenter = null;
        }
    }

    private void checkVersionAndQuery(UserStudySnapshot snapshot, String subject) {
        StudySnapshotItem item;
        if (this.mUserStudyState.isUserValid()) {
            if (snapshot.mCurrentCatalog == null && (item = StudyCPHelper.query(this.mContext, this.mUserStudyState.getUserId(), subject)) != null) {
                snapshot.mCurrentCatalog = item.mCatalogItem;
                snapshot.mMappingInfo = item.mMappingInfo;
                if (TextUtils.equals(subject, this.mCurrentSubject) && this.mStudyView != null) {
                    this.mStudyView.showStudySectionInfo(snapshot.mCurrentCatalog);
                    this.mStudyView.showMapping(subject, snapshot.mMappingInfo);
                }
            }
            if (snapshot.hasNewCatalog()) {
                if (snapshot.mNewCatalog == null) {
                    this.mMapQuerier.queryImmediately(snapshot.mCurrentCatalog);
                } else {
                    this.mMapQuerier.queryImmediately(snapshot.mNewCatalog);
                }
            } else if (snapshot.mCurrentCatalog != null) {
                this.mMapQuerier.queryMaybeDelay(snapshot.mCurrentCatalog);
            }
        }
    }

    private void checkMathVerAndQuery() {
        checkVersionAndQuery(this.mUserStudyState.mMathSnapshot, "02");
    }

    private void checkPhysicalVerAndQuery() {
        checkVersionAndQuery(this.mUserStudyState.mPhysicalSnapshot, "05");
    }

    private void checkChemicalVerAndQuery() {
        checkVersionAndQuery(this.mUserStudyState.mChemicalSnapshot, "06");
    }

    private void checkScienceVerAndQuery() {
        checkVersionAndQuery(this.mUserStudyState.mScienceSnapshot, "19");
    }

    private void checkAll() {
        checkMathVerAndQuery();
        checkPhysicalVerAndQuery();
        checkChemicalVerAndQuery();
        checkScienceVerAndQuery();
    }

    /* access modifiers changed from: private */
    public void onScreenOn() {
        Logger.d(TAG, "onScreenOn");
        displayCacheAndTryQuery(false);
    }

    public void attachView(IUserStudyView view) {
        this.mStudyView = view;
    }

    public void detachView() {
        this.mStudyView = null;
    }

    public String getCurrentSubject() {
        return this.mCurrentSubject;
    }

    /* access modifiers changed from: private */
    public IUserStudyView getStudyView() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return this.mStudyViewProxy;
        }
        throw new RuntimeException("getStudyView must be called in ui thread");
    }

    public void onUserInfoChanged(boolean isLogin, User userInfo) {
        getStudyView().showGlobalContent(isLogin);
        if (isLogin) {
            int oldGrade = this.mUserStudyState.getGrade(-1);
            boolean changed = this.mUserStudyState.reset(userInfo);
            int grade = userInfo.getGradeCodeInt(-1);
            String oldUserId = this.mUserStudyState.getUserId();
            if (!(oldUserId == null || TextUtils.equals(oldUserId, userInfo.getUserid()) || this.mSegmentsPresenter == null)) {
                this.mSegmentsPresenter.cancel();
            }
            if (grade >= 7 && changed) {
                selectSubjectsWithGrade();
                if (!this.mIsLoadingCache) {
                    if (oldGrade < 0 || oldGrade != grade) {
                        this.mMapQuerier.cancelAll();
                        StudyCPHelper.unRegisterObserver(this.mContext, this.mContentObserver);
                        this.mContentObserver = null;
                        Log.d(TAG, "AAAAA: loading cache B: " + this);
                        loadCache();
                    }
                }
            } else if (grade < 7) {
                this.mMapQuerier.cancelAll();
                StudyCPHelper.unRegisterObserver(this.mContext, this.mContentObserver);
                this.mContentObserver = null;
            } else {
                selectSubjectsWithGrade();
            }
        } else {
            if (this.mSegmentsPresenter != null) {
                this.mSegmentsPresenter.cancel();
            }
            StudyCPHelper.unRegisterObserver(this.mContext, this.mContentObserver);
            this.mContentObserver = null;
            this.mUserStudyState.reset();
            this.mMapQuerier.cancelAll();
        }
    }

    private class NetworkCallback extends ConnectivityManager.NetworkCallback {
        private NetworkCallback() {
        }

        public void onAvailable(Network network) {
            super.onAvailable(network);
            if (network != null) {
                StudySnapshotPresenter.this.onNetworkConnected();
            }
        }
    }

    private class ScreenOnEventHandler implements ReceiverHandler {
        private ScreenOnEventHandler() {
        }

        public void handleEvent(Intent intent, String action) {
            StudySnapshotPresenter.this.onScreenOn();
        }
    }

    private class LoopReceiverHandler implements ReceiverHandler {
        private LoopReceiverHandler() {
        }

        public void handleEvent(Intent intent, String action) {
            StudySnapshotPresenter.this.onLoop();
        }
    }

    private class UserExerciseHandler implements ReceiverHandler {
        private UserExerciseHandler() {
        }

        public void handleEvent(Intent intent, String action) {
            if (StudySnapshotPresenter.this.mUserStudyState.isUserValid()) {
                String subject = intent.getStringExtra(SpeechConstant.SUBJECT);
                if (SubjectHelper.isMath(subject)) {
                    StudySnapshotPresenter.this.mUserStudyState.mUserExeciseCount.mMathCount++;
                    if (StudySnapshotPresenter.this.mUserStudyState.mUserExeciseCount.mMathCount < 5) {
                        return;
                    }
                    if (StudySnapshotPresenter.this.mUserStudyState.mMathSnapshot.mNewCatalog != null) {
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(StudySnapshotPresenter.this.mUserStudyState.mMathSnapshot.mNewCatalog);
                    } else if (StudySnapshotPresenter.this.mUserStudyState.mMathSnapshot.mCurrentCatalog != null) {
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(StudySnapshotPresenter.this.mUserStudyState.mMathSnapshot.mCurrentCatalog);
                    }
                } else if (SubjectHelper.isPhysical(subject)) {
                    StudySnapshotPresenter.this.mUserStudyState.mUserExeciseCount.mPhysicalCount++;
                    if (StudySnapshotPresenter.this.mUserStudyState.mUserExeciseCount.mPhysicalCount < 5) {
                        return;
                    }
                    if (StudySnapshotPresenter.this.mUserStudyState.mPhysicalSnapshot.mNewCatalog != null) {
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(StudySnapshotPresenter.this.mUserStudyState.mPhysicalSnapshot.mNewCatalog);
                    } else if (StudySnapshotPresenter.this.mUserStudyState.mPhysicalSnapshot.mCurrentCatalog != null) {
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(StudySnapshotPresenter.this.mUserStudyState.mPhysicalSnapshot.mCurrentCatalog);
                    }
                } else if (SubjectHelper.isChemical(subject)) {
                    StudySnapshotPresenter.this.mUserStudyState.mUserExeciseCount.mChemicalConnt++;
                    if (StudySnapshotPresenter.this.mUserStudyState.mUserExeciseCount.mChemicalConnt < 5) {
                        return;
                    }
                    if (StudySnapshotPresenter.this.mUserStudyState.mChemicalSnapshot.mNewCatalog != null) {
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(StudySnapshotPresenter.this.mUserStudyState.mChemicalSnapshot.mNewCatalog);
                    } else if (StudySnapshotPresenter.this.mUserStudyState.mChemicalSnapshot.mCurrentCatalog != null) {
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(StudySnapshotPresenter.this.mUserStudyState.mChemicalSnapshot.mCurrentCatalog);
                    }
                } else if (TextUtils.equals(subject, "19")) {
                    StudySnapshotPresenter.this.mUserStudyState.mUserExeciseCount.mScienceCount++;
                    if (StudySnapshotPresenter.this.mUserStudyState.mUserExeciseCount.mScienceCount < 5) {
                        return;
                    }
                    if (StudySnapshotPresenter.this.mUserStudyState.mScienceSnapshot.mNewCatalog != null) {
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(StudySnapshotPresenter.this.mUserStudyState.mScienceSnapshot.mNewCatalog);
                    } else if (StudySnapshotPresenter.this.mUserStudyState.mScienceSnapshot.mCurrentCatalog != null) {
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(StudySnapshotPresenter.this.mUserStudyState.mScienceSnapshot.mCurrentCatalog);
                    }
                }
            }
        }
    }

    private class MapCallback implements SubjectMapQuerier.Callback {
        private MapCallback() {
        }

        public void onQueryMapSuccess(String subjectCode, CatalogItem catalogInfo, MappingInfoResponse response) {
            if (TextUtils.equals(subjectCode, "02")) {
                StudySnapshotPresenter.this.mCache.saveCurMapAsync(1, catalogInfo, response);
                StudySnapshotPresenter.this.mUserStudyState.updateMathMap(catalogInfo, response);
            } else if (TextUtils.equals(subjectCode, "05")) {
                StudySnapshotPresenter.this.mCache.saveCurMapAsync(2, catalogInfo, response);
                StudySnapshotPresenter.this.mUserStudyState.updatePhysicalMap(catalogInfo, response);
            } else if (TextUtils.equals(subjectCode, "06")) {
                StudySnapshotPresenter.this.mCache.saveCurMapAsync(3, catalogInfo, response);
                StudySnapshotPresenter.this.mUserStudyState.updateChemical(catalogInfo, response);
            } else {
                StudySnapshotPresenter.this.mCache.saveCurMapAsync(4, catalogInfo, response);
                StudySnapshotPresenter.this.mUserStudyState.updateScience(catalogInfo, response);
            }
            if (StudySnapshotPresenter.this.mStudyView != null && StudySnapshotPresenter.this.matchCurrentSubject(subjectCode)) {
                StudySnapshotPresenter.this.getStudyView().showMapping(subjectCode, response);
            }
        }

        public void onQueryMapError(String subjectCode, CatalogItem catalogInfo, Throwable e) {
            if (TextUtils.equals(subjectCode, StudySnapshotPresenter.this.mCurrentSubject)) {
                boolean shouldRefresh = false;
                if (SubjectHelper.isMath(subjectCode) && StudySnapshotPresenter.this.mUserStudyState.mMathSnapshot.mMappingInfo == null) {
                    shouldRefresh = true;
                } else if (SubjectHelper.isPhysical(subjectCode) && StudySnapshotPresenter.this.mUserStudyState.mPhysicalSnapshot.mMappingInfo == null) {
                    shouldRefresh = true;
                } else if (SubjectHelper.isChemical(subjectCode) && StudySnapshotPresenter.this.mUserStudyState.mChemicalSnapshot.mMappingInfo == null) {
                    shouldRefresh = true;
                } else if (TextUtils.equals(subjectCode, "19") && StudySnapshotPresenter.this.mUserStudyState.mScienceSnapshot.mMappingInfo == null) {
                    shouldRefresh = true;
                }
                if (!shouldRefresh) {
                    return;
                }
                if (e == null || !(e instanceof HttpException)) {
                    if (e instanceof ApiException) {
                    }
                    StudySnapshotPresenter.this.mStudyViewProxy.showMappingType(1);
                    return;
                }
                StudySnapshotPresenter.this.mStudyViewProxy.showMappingType(2);
            }
        }
    }

    public class UserCBObserver implements StudyCPHelper.IStudyContentObserver {
        public UserCBObserver() {
        }

        public void onStudyContentChanged(Map<String, StudySnapshotItem> items) {
            StudySnapshotItem curItem = StudySnapshotItem.findBySubject(items, "02");
            if (!(curItem == null || curItem.mCatalogItem == null)) {
                UserStudySnapshot snapshot = StudySnapshotPresenter.this.mUserStudyState.mMathSnapshot;
                StudySnapshotPresenter.this.mMapQuerier.cancelMath();
                if (curItem.mMappingInfo != null) {
                    snapshot.mMappingInfo = curItem.mMappingInfo;
                    snapshot.mCurrentCatalog = curItem.mCatalogItem;
                    snapshot.mNewCatalog = null;
                    StudySnapshotPresenter.this.mCache.saveMapRequestParams(1, curItem.mCatalogItem, true);
                    StudySnapshotPresenter.this.mCache.saveMapResp(1, curItem.mMappingInfo);
                    StudySnapshotPresenter.this.mCache.removeForKey(RequestCache.KEY_MAP_RESPONSE_MATH);
                    if (SubjectHelper.isMath(StudySnapshotPresenter.this.mCurrentSubject)) {
                        StudySnapshotPresenter.this.mStudyViewProxy.showMapping(StudySnapshotPresenter.this.mCurrentSubject, curItem.mMappingInfo);
                    }
                } else if (snapshot.mMappingInfo != null) {
                    if (!CatalogItem.isEquals(snapshot.mCurrentCatalog, curItem.mCatalogItem)) {
                        snapshot.mNewCatalog = curItem.mCatalogItem;
                        StudySnapshotPresenter.this.mCache.saveMapRequestParams(1, curItem.mCatalogItem, false);
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(curItem.mCatalogItem);
                    } else {
                        return;
                    }
                } else if (!CatalogItem.isEquals(snapshot.mCurrentCatalog, curItem.mCatalogItem)) {
                    snapshot.mCurrentCatalog = curItem.mCatalogItem;
                    snapshot.mNewCatalog = null;
                    StudySnapshotPresenter.this.mCache.saveMapRequestParams(1, curItem.mCatalogItem, true);
                    StudySnapshotPresenter.this.mCache.removeForKey(RequestCache.KEY_MAP_RESPONSE_MATH);
                    if (SubjectHelper.isMath(StudySnapshotPresenter.this.mCurrentSubject)) {
                        StudySnapshotPresenter.this.mStudyViewProxy.showMappingType(0);
                    }
                    StudySnapshotPresenter.this.mMapQuerier.queryImmediately(curItem.mCatalogItem);
                } else {
                    return;
                }
            }
            StudySnapshotItem curItem2 = StudySnapshotItem.findBySubject(items, "05");
            if (!(curItem2 == null || curItem2.mCatalogItem == null)) {
                UserStudySnapshot snapshot2 = StudySnapshotPresenter.this.mUserStudyState.mPhysicalSnapshot;
                StudySnapshotPresenter.this.mMapQuerier.cancelPhysical();
                if (curItem2.mMappingInfo != null) {
                    snapshot2.mMappingInfo = curItem2.mMappingInfo;
                    snapshot2.mCurrentCatalog = curItem2.mCatalogItem;
                    snapshot2.mNewCatalog = null;
                    StudySnapshotPresenter.this.mCache.saveMapRequestParams(2, curItem2.mCatalogItem, true);
                    StudySnapshotPresenter.this.mCache.saveMapResp(2, curItem2.mMappingInfo);
                    StudySnapshotPresenter.this.mCache.removeForKey(RequestCache.KEY_MAP_RESPONSE_PHYSICAL);
                    if (SubjectHelper.isPhysical(StudySnapshotPresenter.this.mCurrentSubject)) {
                        StudySnapshotPresenter.this.mStudyViewProxy.showMapping(StudySnapshotPresenter.this.mCurrentSubject, curItem2.mMappingInfo);
                    }
                } else if (snapshot2.mMappingInfo != null) {
                    if (!CatalogItem.isEquals(snapshot2.mCurrentCatalog, curItem2.mCatalogItem)) {
                        snapshot2.mNewCatalog = curItem2.mCatalogItem;
                        StudySnapshotPresenter.this.mCache.saveMapRequestParams(2, curItem2.mCatalogItem, false);
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(curItem2.mCatalogItem);
                    } else {
                        return;
                    }
                } else if (!CatalogItem.isEquals(snapshot2.mCurrentCatalog, curItem2.mCatalogItem)) {
                    snapshot2.mCurrentCatalog = curItem2.mCatalogItem;
                    snapshot2.mNewCatalog = null;
                    StudySnapshotPresenter.this.mCache.saveMapRequestParams(2, curItem2.mCatalogItem, true);
                    StudySnapshotPresenter.this.mCache.removeForKey(RequestCache.KEY_MAP_RESPONSE_PHYSICAL);
                    if (SubjectHelper.isPhysical(StudySnapshotPresenter.this.mCurrentSubject)) {
                        StudySnapshotPresenter.this.mStudyViewProxy.showMappingType(0);
                    }
                    StudySnapshotPresenter.this.mMapQuerier.queryImmediately(curItem2.mCatalogItem);
                } else {
                    return;
                }
            }
            StudySnapshotItem curItem3 = StudySnapshotItem.findBySubject(items, "06");
            if (!(curItem3 == null || curItem3.mCatalogItem == null)) {
                UserStudySnapshot snapshot3 = StudySnapshotPresenter.this.mUserStudyState.mChemicalSnapshot;
                StudySnapshotPresenter.this.mMapQuerier.cancelChemical();
                if (curItem3.mMappingInfo != null) {
                    snapshot3.mMappingInfo = curItem3.mMappingInfo;
                    snapshot3.mCurrentCatalog = curItem3.mCatalogItem;
                    snapshot3.mNewCatalog = null;
                    StudySnapshotPresenter.this.mCache.saveMapRequestParams(3, curItem3.mCatalogItem, true);
                    StudySnapshotPresenter.this.mCache.saveMapResp(3, curItem3.mMappingInfo);
                    StudySnapshotPresenter.this.mCache.removeForKey(RequestCache.KEY_MAP_RESPONSE_CHEMICAL);
                    if (SubjectHelper.isChemical(StudySnapshotPresenter.this.mCurrentSubject)) {
                        StudySnapshotPresenter.this.mStudyViewProxy.showMapping(StudySnapshotPresenter.this.mCurrentSubject, curItem3.mMappingInfo);
                    }
                } else if (snapshot3.mMappingInfo != null) {
                    if (!CatalogItem.isEquals(snapshot3.mCurrentCatalog, curItem3.mCatalogItem)) {
                        snapshot3.mNewCatalog = curItem3.mCatalogItem;
                        StudySnapshotPresenter.this.mCache.saveMapRequestParams(3, curItem3.mCatalogItem, false);
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(curItem3.mCatalogItem);
                    } else {
                        return;
                    }
                } else if (!CatalogItem.isEquals(snapshot3.mCurrentCatalog, curItem3.mCatalogItem)) {
                    snapshot3.mCurrentCatalog = curItem3.mCatalogItem;
                    snapshot3.mNewCatalog = null;
                    StudySnapshotPresenter.this.mCache.saveMapRequestParams(3, curItem3.mCatalogItem, true);
                    StudySnapshotPresenter.this.mCache.removeForKey(RequestCache.KEY_MAP_RESPONSE_CHEMICAL);
                    if (SubjectHelper.isChemical(StudySnapshotPresenter.this.mCurrentSubject)) {
                        StudySnapshotPresenter.this.mStudyViewProxy.showMappingType(0);
                    }
                    StudySnapshotPresenter.this.mMapQuerier.queryImmediately(curItem3.mCatalogItem);
                } else {
                    return;
                }
            }
            StudySnapshotItem curItem4 = StudySnapshotItem.findBySubject(items, "19");
            if (curItem4 != null && curItem4.mCatalogItem != null) {
                UserStudySnapshot snapshot4 = StudySnapshotPresenter.this.mUserStudyState.mScienceSnapshot;
                StudySnapshotPresenter.this.mMapQuerier.cancelScience();
                if (curItem4.mMappingInfo != null) {
                    snapshot4.mMappingInfo = curItem4.mMappingInfo;
                    snapshot4.mCurrentCatalog = curItem4.mCatalogItem;
                    snapshot4.mNewCatalog = null;
                    StudySnapshotPresenter.this.mCache.saveMapRequestParams(4, curItem4.mCatalogItem, true);
                    StudySnapshotPresenter.this.mCache.saveMapResp(4, curItem4.mMappingInfo);
                    StudySnapshotPresenter.this.mCache.removeForKey(RequestCache.KEY_MAP_RESPONSE_SCIENCE);
                    if (TextUtils.equals(StudySnapshotPresenter.this.mCurrentSubject, "19")) {
                        StudySnapshotPresenter.this.mStudyViewProxy.showMapping(StudySnapshotPresenter.this.mCurrentSubject, curItem4.mMappingInfo);
                    }
                } else if (snapshot4.mMappingInfo != null) {
                    if (!CatalogItem.isEquals(snapshot4.mCurrentCatalog, curItem4.mCatalogItem)) {
                        snapshot4.mNewCatalog = curItem4.mCatalogItem;
                        StudySnapshotPresenter.this.mCache.saveMapRequestParams(4, curItem4.mCatalogItem, false);
                        StudySnapshotPresenter.this.mMapQuerier.queryImmediately(curItem4.mCatalogItem);
                    }
                } else if (!CatalogItem.isEquals(snapshot4.mCurrentCatalog, curItem4.mCatalogItem)) {
                    snapshot4.mCurrentCatalog = curItem4.mCatalogItem;
                    snapshot4.mNewCatalog = null;
                    StudySnapshotPresenter.this.mCache.saveMapRequestParams(4, curItem4.mCatalogItem, true);
                    StudySnapshotPresenter.this.mCache.removeForKey(RequestCache.KEY_MAP_RESPONSE_SCIENCE);
                    if (TextUtils.equals(StudySnapshotPresenter.this.mCurrentSubject, "19")) {
                        StudySnapshotPresenter.this.mStudyViewProxy.showMappingType(0);
                    }
                    StudySnapshotPresenter.this.mMapQuerier.queryImmediately(curItem4.mCatalogItem);
                }
            }
        }
    }

    public static final class DebugHandler implements ReceiverHandler {
        private final StudySnapshotPresenter mPresenter;

        DebugHandler(StudySnapshotPresenter p) {
            this.mPresenter = p;
        }

        public void handleEvent(Intent intent, String action) {
            if (TextUtils.equals(action, MainDebugger.DACTION_LISTEN_SYSTEM_INFO)) {
                StringBuilder sb = new StringBuilder("DEBUG: 监听系统事件详情：\n");
                if (this.mPresenter.mContentObserver == null) {
                    sb.append("监听精准学习系统ContentProvider: 失败");
                } else {
                    sb.append("监听精准学习系统ContentProvider: 正在监听");
                }
                if (this.mPresenter.mNetworkCallback == null) {
                    sb.append("\n监听网络事件：失败");
                } else {
                    sb.append("\n监听网络事件：正在监听");
                }
                Log.d(StudySnapshotPresenter.TAG, sb.toString());
            } else if (TextUtils.equals(action, MainDebugger.DACTION_USER_INFO_PRINT)) {
                StringBuilder sb2 = new StringBuilder();
                if (this.mPresenter.mUserStudyState.isUserValid()) {
                    sb2.append("用户信息合法\n").append("userId = ").append(this.mPresenter.mUserStudyState.getUserId()).append("\n").append("gradeCode = ").append(this.mPresenter.mUserStudyState.getGradeCode());
                } else {
                    sb2.append("用户信息不合法\n");
                    if (this.mPresenter.mUserStudyState.mUser == null) {
                        sb2.append("null == mUser");
                    } else {
                        sb2.append("userId = ").append(this.mPresenter.mUserStudyState.getUserId()).append("\n").append("gradeCode = ").append(this.mPresenter.mUserStudyState.getGradeCode());
                    }
                }
                Log.d(StudySnapshotPresenter.TAG, "DEBUG: " + sb2.toString());
            }
        }
    }
}
