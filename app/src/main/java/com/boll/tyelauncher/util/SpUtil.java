package com.boll.tyelauncher.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpUtil {
    public static String APPLISTSTRING = "appliststring";
    private static SharedPreferences sp;

    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("SpUtil", 0);
        }
        return sp;
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        return getSp(context).getString(key, "");
    }

    public static String getString(Context context, String key, String value) {
        return getSp(context).getString(key, value);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSp(context).getBoolean(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSp(context).getLong(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSp(context).getInt(key, defValue);
    }

    public static <T extends Serializable> void putObject(Context context, String key, T obj) {
        try {
            put(context, key, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> T getObject(Context context, String key) {
        try {
            return (Serializable) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void putList(Context context, String key, List<? extends Serializable> list) {
        try {
            put(context, key, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <E extends Serializable> ArrayList<E> getList(Context context, String key) {
        try {
            return (ArrayList) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <K extends Serializable, V extends Serializable> void putMap(Context context, String key, Map<K, V> map) {
        try {
            put(context, key, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <K extends Serializable, V extends Serializable> Map<K, V> getMap(Context context, String key) {
        try {
            return (Map) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void put(Context context, String key, Object obj) throws IOException {
        if (obj != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            String objectStr = new String(Base64.encode(baos.toByteArray(), 0));
            baos.close();
            oos.close();
            putString(context, key, objectStr);
        }
    }

    private static Object get(Context context, String key) throws IOException, ClassNotFoundException {
        String wordBase64 = getString(context, key);
        if (TextUtils.isEmpty(wordBase64)) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(wordBase64.getBytes(), 0));
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object readObject = ois.readObject();
        bais.close();
        ois.close();
        return readObject;
    }
}
