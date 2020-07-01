package com.boll.tyelauncher.receiver;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.boll.tyelauncher.model.AppContants;

public class MyEyecareReceiver extends BroadcastReceiver {
    public static final String AUTHORITY = "com.iflytek.provider.SwitchContentProvider";
    public static final Uri CONTENT_URI_FIRST = Uri.parse("content://com.iflytek.provider.SwitchContentProvider/time");
    public static String EYECARE_TAG = "toycloud_eyeguaid";
    public static final String SWITCH_SPIRIT = "com.toycloud.action.SWITCH_SPIRIT";
    public static final String SWITCH_SPIRIT_IS_OPEN = "SWITCH_SPIRIT_IS_OPEN";
    public static String TAG = "MyEyecareReceiver";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("com.toycloud.action.EYEGUAID_SHORT_PRESS")) {
            Settings.System.putInt(context.getContentResolver(), EYECARE_TAG, 1 - Settings.System.getInt(context.getContentResolver(), EYECARE_TAG, 0));
            Intent intent2 = new Intent("android.intent.action.MYONOFFRECEIVER");
            intent2.putExtra(AppContants.KEY_TYPE, 0);
            context.sendBroadcast(intent2);
        } else if (action.equals("com.toycloud.action.EYEGUAID_LONG_PRESS")) {
            try {
                Intent intent_eye = new Intent();
                intent_eye.setComponent(new ComponentName("com.iflytek.eyecareassistant", "com.iflytek.eyecareassistant.ui.activity.MainActivity"));
                context.startActivity(intent_eye);
            } catch (Throwable th) {
            }
        }
        Log.e(TAG, "护眼助手广播已接受");
    }
}