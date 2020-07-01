package com.boll.tyelauncher.receiver;

package com.toycloud.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import com.toycloud.launcher.BuildConfig;
import com.toycloud.launcher.checkappversion.MyService_OnScreen;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.myinterface.Listener_Update;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.UpdateUtil;

public class ScreenBroadcastReceiver extends BroadcastReceiver {
    public static final String SWITCH_SPIRIT = "com.toycloud.action.SWITCH_SPIRIT";

    public void onReceive(Context context, Intent intent) {
        boolean z = false;
        boolean z2 = true;
        String action = intent.getAction();
        if (action.equals("android.intent.action.SCREEN_OFF")) {
            if (1 - Settings.System.getInt(context.getContentResolver(), MyEyecareReceiver.EYECARE_TAG, 0) == 1) {
                z = true;
            }
            if (!Boolean.valueOf(z).booleanValue()) {
                Intent intent2 = new Intent("android.intent.action.MYONOFFRECEIVER");
                intent2.putExtra(AppContants.KEY_TYPE, 1);
                context.sendBroadcast(intent2);
            }
            context.stopService(new Intent(context, MyService_OnScreen.class));
        } else if (action.equals("android.intent.action.SCREEN_ON")) {
            if (1 - Settings.System.getInt(context.getContentResolver(), MyEyecareReceiver.EYECARE_TAG, 0) != 1) {
                z2 = false;
            }
            if (!Boolean.valueOf(z2).booleanValue()) {
                Intent intent22 = new Intent("android.intent.action.MYONOFFRECEIVER");
                intent22.putExtra(AppContants.KEY_TYPE, 3);
                context.sendBroadcast(intent22);
            }
        } else if (action.equals("android.intent.action.USER_PRESENT")) {
            if (1 - Settings.System.getInt(context.getContentResolver(), MyEyecareReceiver.EYECARE_TAG, 0) != 1) {
                z2 = false;
            }
            if (!Boolean.valueOf(z2).booleanValue()) {
                Intent intent23 = new Intent("android.intent.action.MYONOFFRECEIVER");
                intent23.putExtra(AppContants.KEY_TYPE, 2);
                context.sendBroadcast(intent23);
            }
            new UpdateUtil(context, new Listener_Update() {
                public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                    if (isMustUpdate) {
                    }
                }
            }).checkUpdate(BuildConfig.APPLICATION_ID, new PackageUtils(context).getVersionCode(BuildConfig.APPLICATION_ID));
            context.startService(new Intent(context, MyService_OnScreen.class));
        }
    }
}
