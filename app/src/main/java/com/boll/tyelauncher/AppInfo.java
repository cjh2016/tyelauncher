package com.boll.tyelauncher;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.Serializable;

public class AppInfo implements Serializable, Comparable<AppInfo> {
    public static final int APP_STATUS_FORBBIDEN = 1;
    public static final int APP_STATUS_FORBBIDEN_FUN = 2;
    public static final int APP_STATUS_NORMAL = 0;
    public String appName;
    public int appStatus = 0;
    private Drawable icon;
    private boolean isSystemApp = false;
    private final Bundle mMetaInfo = new Bundle();
    public Integer mSort;
    public int mVersionCode;
    public String pakageName;

    public boolean copyMetaInfo(AppInfo newInfo) {
        if (!TextUtils.equals(this.appName, newInfo.appName)) {
            this.appName = newInfo.appName;
            this.icon = newInfo.icon;
            this.mVersionCode = newInfo.mVersionCode;
            return true;
        } else if (this.mVersionCode == newInfo.mVersionCode) {
            return false;
        } else {
            this.icon = newInfo.icon;
            this.mVersionCode = newInfo.mVersionCode;
            return true;
        }
    }

    public void initMetaInfo(Bundle bundle) {
        synchronized (this) {
            this.mMetaInfo.clear();
            if (bundle != null) {
                this.mMetaInfo.putAll(bundle);
            }
        }
    }

    public Bundle getMetaInfo() {
        Bundle bundle;
        synchronized (this) {
            bundle = new Bundle(this.mMetaInfo);
        }
        return bundle;
    }

    public int getAppStatus() {
        return this.appStatus;
    }

    public void setAppStatus(int appStatus2) {
        this.appStatus = appStatus2;
    }

    public void setSort(Integer sort) {
        this.mSort = sort;
    }

    public Integer getSort() {
        return this.mSort;
    }

    public AppInfo() {
    }

    public AppInfo(String pakageName2, String appName2, Drawable icon2) {
        this.pakageName = pakageName2;
        this.appName = appName2;
        this.icon = icon2;
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

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable icon2) {
        this.icon = icon2;
    }

    @Override
    public String toString() {
        return "AppInfo{pakageName='" + this.pakageName + '\'' + ", appName='" + this.appName + '\'' + ", icon=" + this.icon + ", isSystemApp=" + this.isSystemApp + '}';
    }

    @Override
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

    public int compareTo(@NonNull AppInfo appInfo) {
        return getSort().compareTo(appInfo.getSort());
    }

    public boolean isForbbiden() {
        if (!isSystemApp() && this.appStatus != 0) {
            return true;
        }
        return false;
    }
}