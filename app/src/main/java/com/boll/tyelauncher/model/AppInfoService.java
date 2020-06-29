package com.boll.tyelauncher.model;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.boll.tyelauncher.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppInfoService {
    private Context context;
    private PackageManager pm;

    public AppInfoService(Context context2) {
        this.context = context2;
        this.pm = context2.getPackageManager();
    }

    public List<AppInfo> getAppInfos() {
        List<AppInfo> appInfos = new ArrayList<>();
        for (ApplicationInfo info : this.pm.getInstalledApplications(8192)) {
            if (!info.loadLabel(this.pm).toString().startsWith("org.") && !info.loadLabel(this.pm).toString().startsWith("com.") && !info.sourceDir.contains("system") && !info.packageName.contains("com.android") && !info.packageName.contains("com.google")) {
                AppInfo appInfo = new AppInfo();
                appInfo.setIcon(info.loadIcon(this.pm));
                appInfo.setAppName(info.loadLabel(this.pm).toString());
                appInfo.setPakageName(info.packageName);
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }
}