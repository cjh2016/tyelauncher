package com.boll.tyelauncher.model.launcher.interfaces;


import com.boll.tyelauncher.AppInfo;

import java.util.List;

public interface ILauncherView {
    void appendApp(String str, AppInfo appInfo);

    void initAppPages(String str, List<AppInfo> list);

    void onCreateCardPages(String str);

    void removeApp(String str, AppInfo appInfo);

    void updateAppTitleAndIcon(String str, AppInfo appInfo);

    void updateAppUsageStatus(AppInfo appInfo);

    void updateAppUsageStatus(List<AppInfo> list);
}