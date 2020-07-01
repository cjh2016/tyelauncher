package com.boll.tyelauncher.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class AppReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    public void onReceive(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        if (TextUtils.equals(intent.getAction(), "android.intent.action.PACKAGE_ADDED")) {
            intent.getData().getSchemeSpecificPart();
        } else if (TextUtils.equals(intent.getAction(), "android.intent.action.PACKAGE_REPLACED")) {
            intent.getData().getSchemeSpecificPart();
        } else if (TextUtils.equals(intent.getAction(), "android.intent.action.PACKAGE_REMOVED")) {
            intent.getData().getSchemeSpecificPart();
        }
    }
}
