package com.boll.tyelauncher.helper;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonBuilder {
    private static final String TAG = "JsonBuilder";
    private HashMap<String, Object> mValuesMap = new LinkedHashMap();

    private JsonBuilder() {
    }

    public static JsonBuilder newBuilder() {
        return new JsonBuilder();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonBuilder put(String key, Object value) {
        if (!TextUtils.isEmpty(key)) {
            this.mValuesMap.put(key, JSONObject.wrap(value));
        }
        return this;
    }

    public String build() {
        JSONObject jsonObject = buildJsonObject();
        if (jsonObject != null) {
            return jsonObject.toString();
        }
        return null;
    }

    public JSONObject buildJsonObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> entry : this.mValuesMap.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            return jsonObject;
        } catch (JSONException e) {
            return null;
        }
    }
}