package com.boll.tyelauncher.model.launcher.impl;


import android.support.v4.util.Preconditions;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.holder.BaseHolder;
import com.boll.tyelauncher.model.launcher.LauncherModel;
import com.boll.tyelauncher.model.launcher.interfaces.IAppPosMgr;
import com.boll.tyelauncher.model.launcher.interfaces.ILauncherPageFactory;
import com.boll.tyelauncher.model.launcher.interfaces.ILauncherPagersView;
import com.boll.tyelauncher.model.launcher.interfaces.ILauncherView;
import com.boll.tyelauncher.util.ListUtils;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LauncherModelView implements ILauncherView {
    public static final int STATE_NORMAL = 2;
    public static final int STATE_USER_GUIDING = 1;
    private static final String TAG = "LauncherModelView";
    private final List<BaseHolder> mAppViewHolders = new ArrayList();
    private String mGradePeriod;
    private boolean mIsAppInited = false;
    private int mLaunchState;
    private final ILauncherPageFactory mPageFactory;
    private final ILauncherPagersView mPagersView;
    private final IAppPosMgr mPosMgr;

    public LauncherModelView(ILauncherPageFactory pageFactory, IAppPosMgr posMgr, ILauncherPagersView pagersView, boolean isFirstStart) {
        Preconditions.checkNotNull(pageFactory, "pageFactory is null");
        Preconditions.checkNotNull(pagersView, "pagersView is null");
        Preconditions.checkNotNull(posMgr, "posMgr is null");
        this.mPageFactory = pageFactory;
        this.mPagersView = pagersView;
        this.mPosMgr = posMgr;
        if (isFirstStart) {
            this.mLaunchState = 1;
        } else {
            this.mLaunchState = 2;
        }
    }

    public void finishGuide() {
        if (this.mLaunchState != 1) {
            Logger.e(TAG, "mLaunchState != STATE_USER_GUIDING: " + this.mLaunchState);
        }
        this.mLaunchState = 2;
        if (this.mIsAppInited) {
            addPagesToActivity(this.mGradePeriod);
        }
    }

    public void onCreateCardPages(String gradePeriod) {
        this.mPagersView.onCreateCardPages(gradePeriod);
    }

    public void initAppPages(String gradePeriod, List<AppInfo> appInfos) {
        this.mIsAppInited = true;
        this.mGradePeriod = gradePeriod;
        this.mAppViewHolders.clear();
        List<Launcher_3rdAPP_ViewHold> holders = this.mPageFactory.createHolders(gradePeriod, appInfos);
        if (holders != null) {
            this.mAppViewHolders.addAll(holders);
        }
        if (this.mLaunchState == 2) {
            addPagesToActivity(this.mGradePeriod);
        }
    }

    private void addPagesToActivity(String gradePeriod) {
        for (BaseHolder viewHolder : this.mAppViewHolders) {
            viewHolder.onCreate();
        }
        this.mPagersView.initAppPages(gradePeriod, this.mAppViewHolders);
    }

    @Override
    public void updateAppUsageStatus(List<AppInfo> appInfos) {
        if (!ListUtils.isEmpty((List) appInfos)) {
            Iterator<BaseHolder> it = this.mAppViewHolders.iterator();
            while (it.hasNext()) {
                ((Launcher_3rdAPP_ViewHold) it.next()).updateFull();
            }
        }
    }

    public void updateAppUsageStatus(AppInfo appInfo) {
        int pages = this.mAppViewHolders.size();
        for (int i = 0; i < pages; i++) {
            Launcher_3rdAPP_ViewHold viewHolder = (Launcher_3rdAPP_ViewHold) this.mAppViewHolders.get(i);
            int cellIndex = viewHolder.getAppIndex(appInfo);
            if (cellIndex >= 0) {
                viewHolder.updateAppInfo(appInfo, cellIndex);
                return;
            }
        }
    }

    /* JADX WARNING: type inference failed for: r3v3, types: [com.toycloud.launcher.holder.BaseHolder] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void appendApp(java.lang.String r11, com.anarchy.classify.simple.AppInfo r12) {
        /*
            r10 = this;
            r7 = 0
            r5 = 0
            java.util.List<com.toycloud.launcher.holder.BaseHolder> r8 = r10.mAppViewHolders
            java.util.Iterator r8 = r8.iterator()
        L_0x0008:
            boolean r9 = r8.hasNext()
            if (r9 == 0) goto L_0x0020
            java.lang.Object r3 = r8.next()
            com.toycloud.launcher.holder.BaseHolder r3 = (com.toycloud.launcher.holder.BaseHolder) r3
            r6 = r3
            com.toycloud.launcher.holder.Launcher_3rdAPP_ViewHold r6 = (com.toycloud.launcher.holder.Launcher_3rdAPP_ViewHold) r6
            com.toycloud.launcher.model.launcher.interfaces.ILauncherPageFactory r9 = r10.mPageFactory
            boolean r9 = r9.isViewHolderFull(r6)
            if (r9 != 0) goto L_0x0008
            r5 = r6
        L_0x0020:
            if (r5 == 0) goto L_0x0029
            r5.addApp(r12)
        L_0x0025:
            r10.updateAppPosCache()
            return
        L_0x0029:
            java.util.List<com.toycloud.launcher.holder.BaseHolder> r8 = r10.mAppViewHolders
            java.lang.Object r2 = com.iflytek.cbg.common.utils.ListUtils.getItem(r8, r7)
            com.toycloud.launcher.holder.Launcher_3rdAPP_ViewHold r2 = (com.toycloud.launcher.holder.Launcher_3rdAPP_ViewHold) r2
            if (r2 == 0) goto L_0x005c
            boolean r8 = r2.isEditMode()
            if (r8 == 0) goto L_0x005c
            r1 = 1
        L_0x003a:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r0.add(r12)
            com.toycloud.launcher.model.launcher.interfaces.ILauncherPageFactory r8 = r10.mPageFactory
            java.util.List r4 = r8.createHolders(r11, r0)
            java.lang.Object r3 = r4.get(r7)
            com.toycloud.launcher.holder.Launcher_3rdAPP_ViewHold r3 = (com.toycloud.launcher.holder.Launcher_3rdAPP_ViewHold) r3
            r3.setEditMode(r1)
            java.util.List<com.toycloud.launcher.holder.BaseHolder> r7 = r10.mAppViewHolders
            r7.add(r3)
            com.toycloud.launcher.model.launcher.interfaces.ILauncherPagersView r7 = r10.mPagersView
            r7.notifyPagesCountChanged(r3)
            goto L_0x0025
        L_0x005c:
            r1 = r7
            goto L_0x003a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.model.launcher.impl.LauncherModelView.appendApp(java.lang.String, com.anarchy.classify.simple.AppInfo):void");
    }

    public void updateAppPosCache() {
        List<List<AppInfo>> list = new ArrayList<>(this.mAppViewHolders.size());
        Iterator<BaseHolder> it = this.mAppViewHolders.iterator();
        while (it.hasNext()) {
            list.add(((Launcher_3rdAPP_ViewHold) it.next()).getApps());
        }
        this.mPosMgr.save(LauncherModel.getInstance().getGradePeriod(), list);
    }

    @Override
    public void removeApp(String gradePeriod, AppInfo appInfo) {
        int pages = this.mAppViewHolders.size();
        int pageIndex = -1;
        int cellIndex = -1;
        Launcher_3rdAPP_ViewHold targetHolder = null;
        int i = 0;
        while (true) {
            if (i >= pages) {
                break;
            }
            Launcher_3rdAPP_ViewHold viewHolder = (Launcher_3rdAPP_ViewHold) this.mAppViewHolders.get(i);
            cellIndex = viewHolder.getAppIndex(appInfo);
            if (cellIndex >= 0) {
                pageIndex = i;
                targetHolder = viewHolder;
                break;
            }
            i++;
        }
        if (targetHolder != null) {
            targetHolder.removeApp(appInfo, cellIndex);
            if (this.mPageFactory.isViewHolderEmpty(targetHolder)) {
                this.mAppViewHolders.remove(pageIndex);
                this.mPagersView.notifyPagesCountChanged((BaseHolder) null);
            }
            updateAppPosCache();
        }
    }

    @Override
    public void updateAppTitleAndIcon(String gradePeriod, AppInfo appInfo) {
        int pagesCount = this.mAppViewHolders.size();
        for (int i = 0; i < pagesCount; i++) {
            Launcher_3rdAPP_ViewHold viewHolder = (Launcher_3rdAPP_ViewHold) this.mAppViewHolders.get(i);
            int cellIndex = viewHolder.getAppIndex(appInfo);
            if (cellIndex >= 0) {
                viewHolder.updateAppInfo(appInfo, cellIndex);
                return;
            }
        }
    }
}
