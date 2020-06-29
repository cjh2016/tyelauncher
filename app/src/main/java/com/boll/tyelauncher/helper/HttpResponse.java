package com.boll.tyelauncher.helper;


import android.os.Handler;
import android.os.Looper;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HttpResponse implements Callback {
    private static final String TAG = "HttpResponse";
    /* access modifiers changed from: private */
    public ICallback callback;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface ICallback {
        void onFailure(Call call, Exception exc);

        void onSuccess(Call call, String str);
    }

    public HttpResponse(ICallback callback2) {
        this.callback = callback2;
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        if (this.mainHandler != null && this.callback != null) {
            this.mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpResponse.this.callback.onFailure(call, e);
                }
            });
        }
    }

    @Override
    public void onResponse(final Call call, Response response) throws IOException {
        if (response != null && response.body() != null) {
            final String strResponse = response.body().string();
            if (this.mainHandler != null && this.callback != null) {
                this.mainHandler.post(new Runnable() {
                    public void run() {
                        HttpResponse.this.callback.onSuccess(call, strResponse);
                    }
                });
            }
        } else if (this.mainHandler != null && this.callback != null) {
            this.mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpResponse.this.callback.onFailure(call, new Exception("response is null"));
                }
            });
        }
    }
}