package com.boll.tyelauncher.model.launcher.impl;


import android.content.Context;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.model.launcher.interfaces.IAppPosMgr;
import com.boll.tyelauncher.model.launcher.interfaces.ILauncherPageFactory;
import com.boll.tyelauncher.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class LauncherAppViewHolderMgr implements ILauncherPageFactory {
    private static final int PAGE_SIZE = 24;
    private final Context mContext;
    private boolean mIsPrepared = false;
    private final IAppPosMgr mPosMgr;

    public LauncherAppViewHolderMgr(Context context, IAppPosMgr posMgr) {
        this.mContext = context;
        this.mPosMgr = posMgr;
    }

    public List<Launcher_3rdAPP_ViewHold> createHolders(String gradePeriod, List<AppInfo> appInfoList) {
        List<List<AppInfo>> pages = this.mPosMgr.buildPages(gradePeriod, appInfoList);
        if (ListUtils.isEmpty((List) pages)) {
            return createNormal(appInfoList);
        }
        return createByPage(pages);
    }

    private List<Launcher_3rdAPP_ViewHold> createNormal(List<AppInfo> appInfoList) {
        int endIndex;
        int allAppCount = appInfoList.size();
        List<Launcher_3rdAPP_ViewHold> result = new ArrayList<>(((allAppCount + 24) - 1) / 24);
        for (int i = 0; i < allAppCount; i += 24) {
            ArrayList<AppInfo> appList = new ArrayList<>(24);
            if (i + 24 >= allAppCount) {
                endIndex = allAppCount;
            } else {
                endIndex = i + 24;
            }
            for (int j = i; j < endIndex; j++) {
                appList.add(appInfoList.get(j));
            }
            Launcher_3rdAPP_ViewHold viewHold = new Launcher_3rdAPP_ViewHold(this.mContext, appList);
            viewHold.onCreate();
            result.add(viewHold);
        }
        return result;
    }

    private List<Launcher_3rdAPP_ViewHold> createByPage(List<List<AppInfo>> appInfoList) {
        int pages = appInfoList.size();
        List<Launcher_3rdAPP_ViewHold> result = new ArrayList<>(pages);
        for (int i = 0; i < pages; i++) {
            ArrayList<AppInfo> appList = new ArrayList<>(appInfoList.get(i));
            if (i == pages - 1) {
                List<Launcher_3rdAPP_ViewHold> last = createNormal(appList);
                if (last != null) {
                    result.addAll(last);
                }
            } else {
                Launcher_3rdAPP_ViewHold viewHold = new Launcher_3rdAPP_ViewHold(this.mContext, appList);
                viewHold.onCreate();
                result.add(viewHold);
            }
        }
        return result;
    }

    @Override
    public boolean isViewHolderFull(Launcher_3rdAPP_ViewHold holder) {
        return ListUtils.size(holder.getApps()) >= 24;
    }

    @Override
    public boolean isViewHolderEmpty(Launcher_3rdAPP_ViewHold holder) {
        return ListUtils.isEmpty((List) holder.getApps());
    }
}