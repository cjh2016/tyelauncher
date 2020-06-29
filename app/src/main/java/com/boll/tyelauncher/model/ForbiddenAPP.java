package com.boll.tyelauncher.model;


import java.io.Serializable;

public class ForbiddenAPP implements Serializable {
    private String appCode;
    private String appName;
    private int status;

    public String getAppCode() {
        return this.appCode;
    }

    public void setAppCode(String appCode2) {
        this.appCode = appCode2;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName2) {
        this.appName = appName2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }
}