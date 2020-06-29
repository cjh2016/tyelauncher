package com.boll.tyelauncher.helper;


import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class OkHttpClientHelper {
    private static OkHttpClient _instance = null;

    public static OkHttpClient getOkHttpClient() {
        if (_instance == null) {
            _instance = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();
        }
        return _instance;
    }
}
