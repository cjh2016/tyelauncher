package com.boll.tyelauncher.model.launcher.interfaces;


import com.boll.tyelauncher.AppInfo;

import java.util.List;

public interface IAppPosMgr {
    List<List<AppInfo>> buildPages(String str, List<AppInfo> list);

    void save(String str, List<List<AppInfo>> list);
}
