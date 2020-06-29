package com.boll.tyelauncher.model;


import java.io.Serializable;

public class AppUpdateBean implements Serializable {
    private int hasBeenShowed;
    private String pkgName;
    private int versionCode;

    public void setPkgName(String pkgName2) {
        this.pkgName = pkgName2;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setVersionCode(int versionCode2) {
        this.versionCode = versionCode2;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public void setHasBeenShowed(int hasBeenShowed2) {
        this.hasBeenShowed = hasBeenShowed2;
    }

    public int getHasBeenShowed() {
        return this.hasBeenShowed;
    }
}
