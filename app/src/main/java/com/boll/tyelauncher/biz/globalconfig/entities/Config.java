package com.boll.tyelauncher.biz.globalconfig.entities;

package com.toycloud.launcher.biz.globalconfig.entities;

public class Config {
    private String mKey;
    private int mType;
    private String mValue;

    public String getKey() {
        return this.mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getValue() {
        return this.mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String toString() {
        return "Config{mKey='" + this.mKey + '\'' + ", mValue='" + this.mValue + '\'' + ", mType=" + this.mType + '}';
    }
}
