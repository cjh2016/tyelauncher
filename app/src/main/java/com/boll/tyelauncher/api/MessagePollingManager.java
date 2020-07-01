package com.boll.tyelauncher.api;

package com.toycloud.launcher.api;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;
import com.toycloud.launcher.api.response.FeedbackMessageResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.helper.FeedbackHelper;
import com.toycloud.launcher.helper.UserInfoHelper;
import com.toycloud.launcher.util.DeviceUtil;
import com.toycloud.launcher.util.receiver.ReceiverHandler;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessagePollingManager {
    private static final String TAG = "MessageManager";
    private static MessagePollingManager mPollingManager;
    private Context mApplicationContext;
    private Disposable mDisposable;
    public ReceiverHandler mWifiHandler = new ReceiverHandler() {
        public void handleEvent(Intent intent, String action) {
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                if (intent.getIntExtra("wifi_state", 0) == 1) {
                    MessagePollingManager.this.stopPolling();
                }
            } else if ("android.net.wifi.STATE_CHANGE".equals(intent.getAction()) && NetworkInfo.State.CONNECTED == ((NetworkInfo) intent.getParcelableExtra("networkInfo")).getState()) {
                MessagePollingManager.this.startPolling(0, 30, TimeUnit.MINUTES);
            }
        }
    };
    /* access modifiers changed from: private */
    public List<Disposable> messageRequests = new ArrayList();

    private MessagePollingManager(Context context) {
        this.mApplicationContext = context;
    }

    public static MessagePollingManager getInstance(Context context) {
        if (mPollingManager == null) {
            synchronized (MessagePollingManager.class) {
                if (mPollingManager == null) {
                    mPollingManager = new MessagePollingManager(context.getApplicationContext());
                }
            }
        }
        return mPollingManager;
    }

    public void startPolling(long initialDelay, long period, TimeUnit unit) {
        if (this.mDisposable == null) {
            Log.d(TAG, "startPolling");
            this.mDisposable = Observable.interval(initialDelay, period, unit).subscribe(new Consumer<Long>() {
                public void accept(Long aLong) throws Exception {
                    Log.d(MessagePollingManager.TAG, "--------" + aLong);
                    MessagePollingManager.this.requestMessage(MessagePollingManager.this.messageRequests);
                }
            });
        }
    }

    public void stopPolling() {
        Log.d(TAG, "stopPolling");
        if (this.mDisposable != null) {
            this.mDisposable.dispose();
            this.mDisposable = null;
        }
        if (this.messageRequests != null) {
            for (int i = 0; i < this.messageRequests.size(); i++) {
                try {
                    Disposable subscription = this.messageRequests.get(i);
                    if (!subscription.isDisposed()) {
                        subscription.dispose();
                    }
                } catch (Throwable th) {
                }
            }
            this.messageRequests.clear();
        }
    }

    /* access modifiers changed from: private */
    public void requestMessage(List<Disposable> disposables) {
        String sn = DeviceUtil.getSnCode(this.mApplicationContext);
        String userId = UserInfoHelper.getInstance(this.mApplicationContext).getUserId();
        Log.d(TAG, "sn:" + sn + "\n" + "userId:" + userId);
        LauncherHttpHelper.getHttpService().getFeedbackMessage(sn, userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MessagePollingManager$$Lambda$0(this), MessagePollingManager$$Lambda$1.$instance, MessagePollingManager$$Lambda$2.$instance, new MessagePollingManager$$Lambda$3(disposables));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$requestMessage$1$MessagePollingManager(FeedbackMessageResponse feedbackMessageResponse) throws Exception {
        FeedbackMessageResponse.DataBean dataBean;
        Log.d(TAG, "feedbackMessage:" + feedbackMessageResponse.toString());
        if (feedbackMessageResponse != null && feedbackMessageResponse.getStatus() == 0 && (dataBean = feedbackMessageResponse.getData()) != null && dataBean.getIsHaveRead() >= 0) {
            Log.d(TAG, "result:" + FeedbackHelper.updateMessage(this.mApplicationContext, dataBean.getIsHaveRead()));
        }
    }

    static final /* synthetic */ void lambda$requestMessage$2$MessagePollingManager(Throwable throwable) throws Exception {
        if (throwable != null) {
            Log.d(TAG, "onError:" + throwable.getMessage());
        }
    }

    static final /* synthetic */ void lambda$requestMessage$3$MessagePollingManager() throws Exception {
    }
}