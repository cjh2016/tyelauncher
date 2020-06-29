package com.boll.tyelauncher.model;


import com.boll.tyelauncher.AppInfo;

import java.io.Serializable;

public class AppInfoSave implements Serializable {
    public String appName;
    public int appStatus = 0;
    private boolean isSystemApp = false;
    public Integer mSort;
    public String pakageName;

    public int getAppStatus() {
        return this.appStatus;
    }

    public void setAppStatus(int appStatus2) {
        this.appStatus = appStatus2;
    }

    public Integer getmSort() {
        return this.mSort;
    }

    public void setSort(Integer mSort2) {
        this.mSort = mSort2;
    }

    public AppInfoSave() {
    }

    public AppInfoSave(String pakageName2, String appName2) {
        this.pakageName = pakageName2;
        this.appName = appName2;
    }

    public boolean isSystemApp() {
        return this.isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        this.isSystemApp = systemApp;
    }

    public String getPakageName() {
        return this.pakageName;
    }

    public void setPakageName(String pakageName2) {
        this.pakageName = pakageName2;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName2) {
        this.appName = appName2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppInfo appInfo = (AppInfo) o;
        if (this.pakageName != null) {
            return this.pakageName.equals(appInfo.pakageName);
        }
        if (appInfo.pakageName != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (this.pakageName != null) {
            return this.pakageName.hashCode();
        }
        return 0;
    }
}
