package com.boll.tyelauncher.model.launcher.interfaces;


import com.boll.tyelauncher.AppInfo;

import java.util.List;

public interface ISystemPMHooker {
    List<AppInfo> onQueryAll(String str, List<AppInfo> list);

    boolean shouldIgnoreApp(String str, String str2);
}