package com.boll.tyelauncher.api.service;


import com.boll.tyelauncher.util.SharepreferenceUtil;
import com.iflytek.biz.http.HttpServiceHelper;
import framework.hz.salmon.retrofit.CustomGsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LauncherHttpHelper extends HttpServiceHelper {
    private static final LauncherHttpHelper sInstance = new LauncherHttpHelper();

    /* access modifiers changed from: protected */
    public String getBaseUrl(Class<?> cls) {
        return "https://k12-api.openspeech.cn/";
    }

    public String getToken() {
        return SharepreferenceUtil.getToken();
    }

    public boolean useBase64() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDebug() {
        return false;
    }

    public static LauncherService getLauncherService() {
        return (LauncherService) sInstance.getHttpService(LauncherService.class);
    }

    public static HttpService getHttpService() {
        return (HttpService) sInstance.getHttpService(HttpService.class);
    }

    /* access modifiers changed from: protected */
    public Retrofit createRetrofit(Class<?> clazz, String clazzName) {
        return new Retrofit.Builder().baseUrl(getBaseUrl(clazz)).addConverterFactory(CustomGsonConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(this.sOkHttpClient).build();
    }
}
