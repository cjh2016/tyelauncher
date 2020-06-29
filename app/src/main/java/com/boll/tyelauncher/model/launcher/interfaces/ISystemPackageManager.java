package com.boll.tyelauncher.model.launcher.interfaces;


import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.model.launcher.ApkDrawable;

import java.util.List;

public interface ISystemPackageManager {
    AppInfo query(String str, String str2);

    List<AppInfo> queryAll(String str);

    ApkDrawable queryAppIcon(String str, String str2);

    void updateCache(String str);
}
