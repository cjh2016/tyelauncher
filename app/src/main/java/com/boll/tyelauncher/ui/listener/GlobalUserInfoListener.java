package com.boll.tyelauncher.ui.listener;


import android.util.Log;

import com.boll.tyelauncher.api.model.User;

import java.util.ArrayList;
import java.util.List;

public final class GlobalUserInfoListener implements UserListener {
    private static final String TAG = "GlobalUserInfoListener";
    public static final GlobalUserInfoListener sInstance = new GlobalUserInfoListener();
    private List<UserListener> mListeners = new ArrayList();

    public static final GlobalUserInfoListener getInstance() {
        return sInstance;
    }

    public void addListener(UserListener listener) {
        if (listener != null) {
            synchronized (this) {
                if (!this.mListeners.contains(listener)) {
                    this.mListeners.add(listener);
                }
            }
        }
    }

    public void removeListener(UserListener listener) {
        if (listener != null) {
            synchronized (this) {
                this.mListeners.remove(listener);
            }
        }
    }

    @Override
    public void onUserInfoChanged(boolean isLogin, User userInfo) {
        Log.d(TAG, "onUserInfoChanged | isLogin=" + isLogin + ", userInfo=" + userInfo);
        synchronized (this) {
            for (UserListener l : this.mListeners) {
                l.onUserInfoChanged(isLogin, userInfo);
            }
        }
    }
}