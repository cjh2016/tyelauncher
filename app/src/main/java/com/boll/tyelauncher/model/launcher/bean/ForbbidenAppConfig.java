package com.boll.tyelauncher.model.launcher.bean;


import android.support.annotation.Keep;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

@Keep
public class ForbbidenAppConfig {
    public List<String> mForbbidenApps;

    public void addApp(String appPkg) {
        if (!TextUtils.isEmpty(appPkg)) {
            if (this.mForbbidenApps == null) {
                this.mForbbidenApps = new ArrayList();
            }
            this.mForbbidenApps.add(appPkg);
        }
    }
}