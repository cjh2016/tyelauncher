package com.boll.tyelauncher.holder;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boll.tyelauncher.api.model.User;
import com.boll.tyelauncher.easytrans.LogAgentHelper;
import com.boll.tyelauncher.util.SharepreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: caijianhui
 * @date: 2020/6/29 15:38
 * @description:
 */
public abstract class BaseHolder {
    private static final String TAG = "BaseHolder";
    private boolean mActivityResumed = false;
    private boolean mIsAttached = false;
    private boolean mIsCurrentPage = false;
    private boolean mIsDetached = false;
    public View mRootView;
    protected SharepreferenceUtil sharepreferenceUtil;
    protected User userInfo;

    /* access modifiers changed from: protected */
    public abstract int getLayoutId();

    /* access modifiers changed from: protected */
    public abstract void initView(Context context, View view);

    public final void setUserInfo(User userInfo2) {
        this.userInfo = userInfo2;
    }

    public BaseHolder(Context context) {
        this.sharepreferenceUtil = SharepreferenceUtil.getSharepferenceInstance(context);
        this.userInfo = this.sharepreferenceUtil.getUserInfo();
        initDataBeforeInflate(context);
        long time = System.currentTimeMillis();
        this.mRootView = LayoutInflater.from(context).inflate(getLayoutId(), (ViewGroup) null, false);
        Log.d("fgtian", "inflate耗时：(" + getClass().getSimpleName() + "):" + (System.currentTimeMillis() - time));
        initView(context, this.mRootView);
    }

    /* access modifiers changed from: protected */
    public void initDataBeforeInflate(Context context) {
    }

    public View getRootView() {
        return this.mRootView;
    }

    public void onCreate() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onDestroy() {
        boolean isVisible = isVisibie();
        this.mIsDetached = true;
        if (isVisible) {
            onVisibilityChanged(false);
        }
    }

    public boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }

    public void onScrollIn() {
        Log.d(TAG, "被滑入");
        boolean prevVisible = isVisibie();
        this.mIsCurrentPage = true;
        boolean curVisible = isVisibie();
        if (prevVisible != curVisible) {
            onVisibilityChanged(curVisible);
        }
    }

    public void onScrollOut() {
        Log.d(TAG, "被划出");
        boolean prevVisible = isVisibie();
        this.mIsCurrentPage = false;
        boolean curVisible = isVisibie();
        if (prevVisible != curVisible) {
            onVisibilityChanged(curVisible);
        }
    }

    public void onActivityResume(boolean isCurrentPage) {
        boolean curVisible;
        boolean prevVisible = isVisibie();
        this.mIsCurrentPage = isCurrentPage;
        this.mActivityResumed = true;
        if (this.mIsAttached && !this.mIsDetached && prevVisible != (curVisible = isVisibie())) {
            onVisibilityChanged(curVisible);
        }
    }

    public void onActivityPause(boolean isCurrentPage) {
        boolean curVisible;
        boolean prevVisible = isVisibie();
        this.mIsCurrentPage = isCurrentPage;
        this.mActivityResumed = false;
        if (this.mIsAttached && !this.mIsDetached && prevVisible != (curVisible = isVisibie())) {
            onVisibilityChanged(curVisible);
        }
    }

    public void onAttachToAdapter(boolean isCurrentPage, boolean isTopActivity) {
        boolean curVisible;
        boolean prevVisible = isVisibie();
        this.mIsCurrentPage = isCurrentPage;
        this.mActivityResumed = isTopActivity;
        this.mIsAttached = true;
        if (this.mIsAttached && !this.mIsDetached && prevVisible != (curVisible = isVisibie())) {
            onVisibilityChanged(curVisible);
        }
    }

    public void onDetachFromAdapter(boolean isCurrentPage) {
        boolean curVisible;
        boolean prevVisible = isVisibie();
        this.mIsCurrentPage = isCurrentPage;
        this.mIsDetached = true;
        if (this.mIsAttached && !this.mIsDetached && prevVisible != (curVisible = isVisibie())) {
            onVisibilityChanged(curVisible);
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(boolean isVisible) {
        String pageName = getStatPageName();
        Log.d(TAG, "onVisibilityChanged:" + pageName + " : " + isVisible);
        if (isVisible) {
            LogAgentHelper.onPageStart(pageName);
        } else {
            LogAgentHelper.onPageExit(pageName);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isVisibie() {
        return this.mIsAttached && !this.mIsDetached && this.mIsCurrentPage && this.mActivityResumed;
    }

    public boolean isAttached() {
        return this.mIsAttached;
    }

    public boolean isDetached() {
        return this.mIsDetached;
    }

    public boolean isActivityResumed() {
        return this.mActivityResumed;
    }

    public boolean isCurrentPage() {
        return this.mIsCurrentPage;
    }

    public String getStatPageName() {
        return null;
    }
}
