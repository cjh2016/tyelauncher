package com.boll.tyelauncher.api;

package com.toycloud.launcher.api;

import com.toycloud.launcher.api.service.UploadLogService;
import com.toycloud.launcher.util.LogSaveManager_Util;
import framework.hz.salmon.retrofit.CustomGsonConverterFactory;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider {
    public static final String BASE_URL = "https://k12-api.openspeech.cn/";
    private static final String TAG = "ApiProvider";
    private static ApiProvider instance_uplaod;
    public UploadLogService mUploadLogService;

    private ApiProvider(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.hostnameVerifier(new TrustAllHostnameVerifier());
        builder.protocols(Arrays.asList(new Protocol[]{Protocol.HTTP_1_1}));
        builder.addInterceptor(new Interceptor() {
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder().method(original.method(), original.body()).build();
                long currentTimeMillis = System.currentTimeMillis();
                Response response = chain.proceed(request);
                LogSaveManager_Util.saveLog(" \n 请求网址: \n" + request.url() + " \n 请求头部信息：\n" + request.headers() + " \n 响应头部信息：\n" + response.headers());
                return response;
            }
        });
        this.mUploadLogService = (UploadLogService) new Retrofit.Builder().baseUrl(baseUrl).client(builder.build()).addConverterFactory(CustomGsonConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(UploadLogService.class);
    }

    public static ApiProvider getInstance(String url) {
        instance_uplaod = new ApiProvider(url);
        return instance_uplaod;
    }

    private SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init((KeyManager[]) null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            return sc.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class TrustAllManager implements X509TrustManager {
        private TrustAllManager() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        private TrustAllHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}