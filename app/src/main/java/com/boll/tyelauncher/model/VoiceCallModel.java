package com.boll.tyelauncher.model;


public class VoiceCallModel {
    private String action;
    private String className;
    private String extendJson;
    private String extras;
    private String name;
    private boolean needCheck;
    private String packageName;
    private int proxy;
    private String startType;
    private int version;

    public int getProxy() {
        return this.proxy;
    }

    public void setProxy(int proxy2) {
        this.proxy = proxy2;
    }

    public String getExtendJson() {
        return this.extendJson;
    }

    public void setExtendJson(String extendJson2) {
        this.extendJson = extendJson2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getStartType() {
        return this.startType;
    }

    public void setStartType(String startType2) {
        this.startType = startType2;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action2) {
        this.action = action2;
    }

    public String getExtras() {
        return this.extras;
    }

    public void setExtras(String extras2) {
        this.extras = extras2;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className2) {
        this.className = className2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName2) {
        this.packageName = packageName2;
    }

    public boolean isNeedCheck() {
        return this.needCheck;
    }

    public void setNeedCheck(boolean needCheck2) {
        this.needCheck = needCheck2;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version2) {
        this.version = version2;
    }
}
