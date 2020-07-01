package com.boll.tyelauncher.util;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boll.tyelauncher.R;

public class CustomToast {
    public static final int LENGTH_LONG = 3000;
    public static final int LENGTH_SHORT = 1000;
    private static Handler mHandler = new Handler();
    private static Toast mToast;
    /* access modifiers changed from: private */
    public static Toast mViewToast;
    private static Runnable toastCel = new Runnable() {
        @Override
        public void run() {
            if (CustomToast.mViewToast != null) {
                CustomToast.mViewToast.cancel();
            }
        }
    };

    public static void showToast(Context mContext, String text, int duration) {
        if (duration <= 1) {
            duration = 1000;
        }
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        if (duration == 3000) {
            mToast = Toast.makeText(mContext, text, 1);
        } else {
            mToast = Toast.makeText(mContext, text, 0);
        }
        try {
            mToast.show();
        } catch (Throwable th) {
        }
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

    public static void showToast(Context mContext, int resId) {
        showToast(mContext, mContext.getResources().getString(resId), 3000);
    }

    public static void showToast(Context mContext, String text) {
        showToast(mContext, text, 3000);
    }

    public static void showViewToast(Context mContext, int layoutId, int duration) {
        mHandler.removeCallbacks(toastCel);
        if (mViewToast == null) {
            mViewToast = new Toast(mContext);
        }
        mViewToast.setView(View.inflate(mContext, layoutId, (ViewGroup) null));
        mViewToast.setGravity(17, 0, 0);
        mViewToast.show();
        mHandler.postDelayed(toastCel, (long) duration);
    }

    public static void showViewToast(Context mContext, View view) {
        mHandler.removeCallbacks(toastCel);
        if (mViewToast == null) {
            mViewToast = new Toast(mContext);
        }
        mViewToast.setView(view);
        mViewToast.setGravity(17, 0, mContext.getResources().getDimensionPixelSize(R.dimen.px600));
        mViewToast.show();
        mHandler.postDelayed(toastCel, 3000);
    }
}
