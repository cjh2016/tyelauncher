package com.boll.tyelauncher.biz.globalconfig;

package com.toycloud.launcher.biz.globalconfig;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.biz.globalconfig.entities.Config;
import com.toycloud.launcher.biz.globalconfig.entities.GetGlobalConfigResult;
import com.toycloud.launcher.util.Logging;
import framework.hz.salmon.retrofit.BaseSubscriber;
import framework.hz.salmon.util.NetworkUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalConfigManager {
    private static final String CLIENT_KEY_PREFIX = "_client_";
    private static final String FORBIDDEN_AUTO_PACKAGE = "forbidden_auto_package";
    private static final String TAG = "GlobalConfigManager";
    private static volatile GlobalConfigManager sInstance = null;
    private volatile boolean mCacheLoaded;
    /* access modifiers changed from: private */
    public final Map<String, Config> mConfigMap = new LinkedHashMap();
    /* access modifiers changed from: private */
    public boolean mIsRequesting;
    /* access modifiers changed from: private */
    public long mLastRequestTime;

    private GlobalConfigManager() {
    }

    private void loadCacheIfNeeded() {
        if (!this.mCacheLoaded) {
            synchronized (this.mConfigMap) {
                this.mConfigMap.clear();
                Map<String, Config> configMap = GlobalConfigDatabase.queryAll();
                if (configMap != null) {
                    this.mConfigMap.putAll(configMap);
                }
            }
            this.mCacheLoaded = true;
            Logging.d(TAG, "loadCacheIfNeeded() config size = " + this.mConfigMap.size());
            Logging.d(TAG, "loadCacheIfNeeded()--------------------------------------");
            for (Map.Entry<String, Config> entry : this.mConfigMap.entrySet()) {
                Logging.d(TAG, "loadCacheIfNeeded() config = " + entry.getValue());
            }
            Logging.d(TAG, "loadCacheIfNeeded()--------------------------------------");
        }
    }

    public static GlobalConfigManager getInstance() {
        if (sInstance == null) {
            synchronized (GlobalConfigManager.class) {
                if (sInstance == null) {
                    sInstance = new GlobalConfigManager();
                }
            }
        }
        return sInstance;
    }

    public String getConfig(String key) {
        String str;
        loadCacheIfNeeded();
        synchronized (this.mConfigMap) {
            Config config = this.mConfigMap.get(key);
            if (config != null) {
                str = config.getValue();
            } else {
                str = null;
            }
        }
        return str;
    }

    public void putConfig(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            synchronized (this.mConfigMap) {
                Config config = new Config();
                config.setKey(key);
                config.setValue(value);
                config.setType(1);
                this.mConfigMap.put(key, config);
                GlobalConfigDatabase.update(key, value);
            }
        }
    }

    public void requestConfig(final Context context, boolean force) {
        Logging.d(TAG, "requestConfig() force = " + force);
        if (context == null) {
            Logging.d(TAG, "requestConfig() context is empty, return");
        } else if (!NetworkUtil.isNetworkAvailable(context)) {
            Logging.d(TAG, "requestConfig() network is not available, return");
        } else if (!force && DateUtils.isToday(this.mLastRequestTime)) {
            Logging.d(TAG, "requestConfig() today has requested, return");
        } else if (this.mIsRequesting) {
            Logging.d(TAG, "requestConfig() mIsRequesting = true, return");
        } else {
            this.mIsRequesting = true;
            Logging.d(TAG, "requestConfig() start request global config");
            LauncherHttpHelper.getLauncherService().getConfig((String) null).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetGlobalConfigResult>(false, context) {
                public void onCompleted() {
                    Logging.d(GlobalConfigManager.TAG, "onCompleted()");
                    boolean unused = GlobalConfigManager.this.mIsRequesting = false;
                }

                public void onError(Throwable e) {
                    Logging.d(GlobalConfigManager.TAG, "onError() e = " + e);
                    boolean unused = GlobalConfigManager.this.mIsRequesting = false;
                }

                /* JADX WARNING: Code restructure failed: missing block: B:2:0x001b, code lost:
                    r0 = r5.getData();
                 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void onNext(com.toycloud.launcher.biz.globalconfig.entities.GetGlobalConfigResult r5) {
                    /*
                        r4 = this;
                        java.lang.String r1 = "GlobalConfigManager"
                        java.lang.StringBuilder r2 = new java.lang.StringBuilder
                        r2.<init>()
                        java.lang.String r3 = "onNext() response = "
                        java.lang.StringBuilder r2 = r2.append(r3)
                        java.lang.StringBuilder r2 = r2.append(r5)
                        java.lang.String r2 = r2.toString()
                        com.toycloud.launcher.util.Logging.d(r1, r2)
                        if (r5 != 0) goto L_0x001b
                    L_0x001a:
                        return
                    L_0x001b:
                        java.util.List r0 = r5.getData()
                        if (r0 == 0) goto L_0x001a
                        com.toycloud.launcher.biz.globalconfig.GlobalConfigManager r1 = com.toycloud.launcher.biz.globalconfig.GlobalConfigManager.this
                        long r2 = java.lang.System.currentTimeMillis()
                        long unused = r1.mLastRequestTime = r2
                        android.os.Handler r1 = com.iflytek.easytrans.core.async.thread.TaskRunner.getBackHandler()
                        com.toycloud.launcher.biz.globalconfig.GlobalConfigManager$1$1 r2 = new com.toycloud.launcher.biz.globalconfig.GlobalConfigManager$1$1
                        r2.<init>(r0)
                        r1.post(r2)
                        goto L_0x001a
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.biz.globalconfig.GlobalConfigManager.AnonymousClass1.onNext(com.toycloud.launcher.biz.globalconfig.entities.GetGlobalConfigResult):void");
                }
            });
        }
    }
}
