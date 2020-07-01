package com.boll.tyelauncher.holder.presenter;


import android.text.TextUtils;

public class SimpleCatalogBean {
    public String code;
    public SimpleCatalogBean course;
    public String name;

    public static boolean isEquals(SimpleCatalogBean a, SimpleCatalogBean b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null || !TextUtils.equals(a.code, b.code)) {
            return false;
        }
        return isEquals(a.course, b.course);
    }
}
