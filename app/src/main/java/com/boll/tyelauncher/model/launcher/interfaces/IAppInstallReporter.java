package com.boll.tyelauncher.model.launcher.interfaces;

import com.boll.tyelauncher.AppInfo;

public interface IAppInstallReporter {
    void checkUploadAllApps();

    void reportAppAdded(AppInfo appInfo);

    void reportAppRemoved(String str);
}
