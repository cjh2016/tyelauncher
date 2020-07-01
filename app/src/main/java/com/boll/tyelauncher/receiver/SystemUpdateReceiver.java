package com.boll.tyelauncher.receiver;

package com.toycloud.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.iflytek.easytrans.watchcore.deviceinfo.DeviceIdentifierUtils;
import com.iflytek.easytrans.watchcore.utils.GlobalConfigs;
import com.toycloud.launcher.model.AppContants;

public class SystemUpdateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        int i = 0;
        if ("com.toycloud.action.SETTINGS_OPEN_UPDATE".equals(intent.getAction())) {
            String openPackage = "com.toycloud.updateservice";
            if (!GlobalConfigs.getBoolean(context, "ota_gray_control", true)) {
                openPackage = AppContants.OTA;
            } else {
                String snList = GlobalConfigs.getString(context, "ota_gray_control_sn_list", (String) null, false);
                String deviceSn = DeviceIdentifierUtils.getSn(context);
                if (!TextUtils.isEmpty(snList) && !TextUtils.isEmpty(deviceSn)) {
                    String[] sns = snList.split("\\|");
                    int length = sns.length;
                    while (true) {
                        if (i < length) {
                            if (deviceSn.equalsIgnoreCase(sns[i])) {
                                openPackage = AppContants.OTA;
                                break;
                            }
                            i++;
                        }
                    }
                }
            }
            try {
                Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(openPackage);
                LaunchIntent.addFlags(268435456);
                context.startActivity(LaunchIntent);
                break;
            } catch (Throwable th) {
            }
        }
        Log.e("SystemUpdateReceiver-->", intent.getAction() + ":");
    }
}