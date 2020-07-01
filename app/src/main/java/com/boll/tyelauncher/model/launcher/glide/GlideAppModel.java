package com.boll.tyelauncher.model.launcher.glide;

package com.toycloud.launcher.model.launcher.glide;

import com.anarchy.classify.simple.AppInfo;

public class GlideAppModel {
    public String mAppName;
    public String mPackageName;
    public int mVersionCode;

    public GlideAppModel(String packageName, String appName, int versionCode) {
        this.mPackageName = packageName;
        this.mVersionCode = versionCode;
        this.mAppName = appName;
    }

    public GlideAppModel(AppInfo appInfo) {
        this(appInfo.getPakageName(), appInfo.appName, appInfo.mVersionCode);
    }

    public GlideAppModel() {
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        GlideAppModel that = (GlideAppModel) object;
        if (this.mVersionCode != that.mVersionCode) {
            return false;
        }
        if (this.mPackageName != null) {
            return this.mPackageName.equals(that.mPackageName);
        }
        if (that.mPackageName != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((this.mPackageName != null ? this.mPackageName.hashCode() : 0) * 31) + this.mVersionCode;
    }
}
