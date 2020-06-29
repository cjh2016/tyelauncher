package com.boll.tyelauncher.model;


import java.io.Serializable;

public class CityConfigModel implements Serializable {
    private static final long serialVersionUID = 3761963309338668644L;
    public String url;
    public int versionCode;

    public boolean needUpdateConfig(int currentVersion) {
        return this.versionCode > currentVersion;
    }
}
