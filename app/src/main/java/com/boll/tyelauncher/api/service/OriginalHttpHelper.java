package com.boll.tyelauncher.api.service;

package com.toycloud.launcher.api.service;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;

public class OriginalHttpHelper {
    private static Retrofit mRetrofit;
    private static originalHttpService mService;

    public static originalHttpService getHttpService() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder().baseUrl("https://k12-api.openspeech.cn/").client(new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).retryOnConnectionFailure(true).sslSocketFactory(createSSLSocketFactory(), new TrustAllManager()).hostnameVerifier(new TrustAllHostnameVerifier()).protocols(Collections.singletonList(Protocol.HTTP_1_1)).build()).build();
        }
        if (mService == null) {
            mService = (originalHttpService) mRetrofit.create(originalHttpService.class);
        }
        return mService;
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

    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init((KeyManager[]) null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            return sc.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
