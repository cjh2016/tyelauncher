package com.boll.tyelauncher.model.launcher.interfaces;


import com.boll.tyelauncher.AppInfo;

import java.util.List;

public interface IAppProvider {
    List<AppInfo> getAllApps();
}