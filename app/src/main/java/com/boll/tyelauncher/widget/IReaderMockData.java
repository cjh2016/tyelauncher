package com.boll.tyelauncher.widget;

package com.toycloud.launcher.widget;

import com.anarchy.classify.simple.AppInfo;

public class IReaderMockData {
    private AppInfo appInfo;
    private int color = -16776961;
    private boolean isChecked;
    private IReaderMockDataGroup mParent;

    public AppInfo getAppInfo() {
        return this.appInfo;
    }

    public void setAppInfo(AppInfo appInfo2) {
        this.appInfo = appInfo2;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color2) {
        this.color = color2;
    }

    public IReaderMockDataGroup getParent() {
        return this.mParent;
    }

    public void setParent(IReaderMockDataGroup parent) {
        this.mParent = parent;
    }

    public boolean isAppValid() {
        return (this.appInfo == null || this.appInfo.getIcon() == null) ? false : true;
    }

    public String toString() {
        return "IReaderMockData{isChecked=" + this.isChecked + ", color=" + this.color + ", mParent=" + this.mParent + ", appInfo=" + this.appInfo + '}';
    }
}