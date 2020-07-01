package com.boll.tyelauncher.util;


import android.text.TextUtils;
import org.json.JSONObject;

public class SimpleJsonUtil {
    private SimpleJsonUtil() {
    }

    public static String getString(String json, String key, String defaultValue) {
        if (TextUtils.isEmpty(json) || TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        try {
            JSONObject object = new JSONObject(json);
            if (object.has(key)) {
                return object.getString(key);
            }
            return defaultValue;
        } catch (Throwable th) {
            return defaultValue;
        }
    }
}
