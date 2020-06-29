package com.boll.tyelauncher.cbg.pushsdk.bean;


import android.support.annotation.Keep;
import java.io.Serializable;

@Keep
public class PushNotificationOption implements Serializable {
    private String key;
    private String value;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    @Override
    public String toString() {
        return "{key=" + this.key + ",value=" + this.value + "}";
    }
}
