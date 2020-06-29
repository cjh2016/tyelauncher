package com.boll.tyelauncher.model.launcher.interfaces;


import com.boll.tyelauncher.AppInfo;

import java.util.Collection;

public interface AppFilter {
    boolean filter(String str, AppInfo appInfo);

    Collection<String> getBuildInHideApps();
}
