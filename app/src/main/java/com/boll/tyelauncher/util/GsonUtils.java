package com.boll.tyelauncher.util;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GsonUtils {
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T changeJsonToBean(String jsonString, Class<T> cls) {
        return new Gson().fromJson(jsonString, cls);
    }

    public static <T> T changeJsonToBeanSafe(String jsonString, Class<T> cls) {
        try {
            return new Gson().fromJson(jsonString, cls);
        } catch (Throwable th) {
            return null;
        }
    }

    public static <T> List<T> changeJsonToList(String jsonString, Class<T> cls) {
        return (List) new Gson().fromJson(jsonString, new TypeToken<List<T>>() {
        }.getType());
    }

    public static <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        Gson gson = new Gson();
        ArrayList<T> mList = new ArrayList<>();
        Iterator<JsonElement> it = new JsonParser().parse(json).getAsJsonArray().iterator();
        while (it.hasNext()) {
            mList.add(gson.fromJson(it.next(), cls));
        }
        return mList;
    }

    public static <T> List<Map<String, T>> changeJsonToListMaps(String jsonString) {
        return (List) new Gson().fromJson(jsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
    }

    public static <T> Map<String, T> changeJsonToMaps(String jsonString) {
        return (Map) new Gson().fromJson(jsonString, new TypeToken<Map<String, T>>() {
        }.getType());
    }
}
