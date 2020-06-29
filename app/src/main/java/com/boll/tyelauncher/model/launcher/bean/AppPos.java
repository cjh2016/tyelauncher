package com.boll.tyelauncher.model.launcher.bean;


import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import java.io.Serializable;

@Keep
public class AppPos implements Comparable<AppPos>, Serializable {
    public int mCellIndex;
    public int mPageIndex;
    public String mPkgName;

    @Override
    public int compareTo(@NonNull AppPos o) {
        if (this.mPageIndex > o.mPageIndex) {
            return 1;
        }
        if (this.mPageIndex < o.mPageIndex) {
            return -1;
        }
        if (this.mCellIndex > o.mCellIndex) {
            return 1;
        }
        if (this.mCellIndex < o.mCellIndex) {
            return -1;
        }
        return 0;
    }
}