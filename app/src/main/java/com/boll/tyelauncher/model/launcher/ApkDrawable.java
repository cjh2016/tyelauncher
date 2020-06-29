package com.boll.tyelauncher.model.launcher;


import android.graphics.drawable.Drawable;

public class ApkDrawable {
    public final Drawable mIcon;
    public final String mPackageName;
    public final int mVersionCode;
    public final String mVersionName;

    public ApkDrawable(String pkgName, String versionName, int versionCode, Drawable icon) {
        this.mPackageName = pkgName;
        this.mIcon = icon;
        this.mVersionName = versionName;
        this.mVersionCode = versionCode;
    }
}