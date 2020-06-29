package com.boll.tyelauncher.easytrans;


public class AppInfo {
    private static final String TAG = "AppInfo";
    private String appid;
    private String version;

    public AppInfo(String appid2, String version2) {
        this.appid = appid2;
        this.version = version2;
    }

    private void setAppId(String appid2) {
        this.appid = appid2;
    }

    private void setVersion(String version2) {
        this.version = version2;
    }

    public String getAppid() {
        return this.appid;
    }

    public String getVersion() {
        return this.version;
    }

    public String toString() {
        return "AppInfo{appid='" + this.appid + '\'' + ", version='" + this.version + '\'' + '}';
    }
}