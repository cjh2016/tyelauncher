package com.boll.tyelauncher.receiver;

package com.toycloud.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.provider.Settings;
import com.toycloud.launcher.model.EtsConstant;
import com.toycloud.launcher.ui.common.VoiceAssistantActivity;

public class VoiceHelperReceiver extends BroadcastReceiver {
    public static final String LONG_PRESS = "com.toycloud.action.ELF_LONG_PRESS";
    public static final String SETTINGS_OPEN = "com.toycloud.action.SETTINGS_OPEN_ELF";
    public static final String SHORT_PRESS = "com.toycloud.action.ELF_SHORT_PRESS";
    public static final String SWITCH_SPIRIT = "com.toycloud.action.SWITCH_SPIRIT";
    public static final String SWITCH_SPIRIT_IS_OPEN = "SWITCH_SPIRIT_IS_OPEN";
    private static final String TOYCLOUD_ELF = "toycloud_elf";

    public void onReceive(Context context, Intent intent) {
        boolean z;
        boolean z2 = true;
        String action = intent.getAction();
        if (action != null) {
            char c = 65535;
            switch (action.hashCode()) {
                case -594339118:
                    if (action.equals(SHORT_PRESS)) {
                        c = 0;
                        break;
                    }
                    break;
                case 360315406:
                    if (action.equals(LONG_PRESS)) {
                        c = 1;
                        break;
                    }
                    break;
                case 1834972472:
                    if (action.equals(SETTINGS_OPEN)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    int voiceHelper = Settings.System.getInt(context.getContentResolver(), TOYCLOUD_ELF, 0);
                    Settings.System.putInt(context.getContentResolver(), TOYCLOUD_ELF, 1 - voiceHelper);
                    if (1 - voiceHelper == 1) {
                        z = true;
                    } else {
                        z = false;
                    }
                    switchSpirit(context, z);
                    Intent intent1 = new Intent("com.toycloud.action.SWITCH_SPIRIT");
                    if (1 - voiceHelper != 1) {
                        z2 = false;
                    }
                    intent1.putExtra("SWITCH_SPIRIT_IS_OPEN", z2);
                    context.sendBroadcast(intent1);
                    return;
                case 1:
                    openVoiceAssistant(context);
                    return;
                case 2:
                    openVoiceAssistant(context);
                    return;
                default:
                    return;
            }
        }
    }

    private void openVoiceAssistant(Context context) {
        context.startActivity(new Intent(context, VoiceAssistantActivity.class));
    }

    private void switchSpirit(Context context, boolean isOpen) {
        try {
            ContentValues values = new ContentValues();
            values.put("switch", isOpen ? "1" : EtsConstant.SUCCESS);
            if (context.getContentResolver().query(VoiceAssistantActivity.CONTENT_URI_FIRST, (String[]) null, (String) null, (String[]) null, (String) null).getCount() > 0) {
                context.getContentResolver().update(VoiceAssistantActivity.CONTENT_URI_FIRST, values, "_id = 1", (String[]) null);
                context.getContentResolver().notifyChange(VoiceAssistantActivity.CONTENT_URI_FIRST, (ContentObserver) null);
                return;
            }
            context.getContentResolver().insert(VoiceAssistantActivity.CONTENT_URI_FIRST, values);
            context.getContentResolver().notifyChange(VoiceAssistantActivity.CONTENT_URI_FIRST, (ContentObserver) null);
        } catch (Throwable th) {
        }
    }
}
