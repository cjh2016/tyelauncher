package com.boll.tyelauncher.model.launcher.bean;


import android.support.annotation.Keep;

import com.boll.tyelauncher.model.ForbiddenTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Keep
public class ForbbidenTimeConfig {
    public List<String> mFunApps;
    public ForbiddenTime mWeekDayConfig;
    public ForbiddenTime mWeekendConfig;

    public void setApps(Collection<String> apps) {
        if (apps != null) {
            this.mFunApps = new ArrayList();
            this.mFunApps.addAll(apps);
        }
    }
}
