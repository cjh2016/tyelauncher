package com.boll.tyelauncher.receiver;

package com.toycloud.launcher.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.toycloud.launcher.ui.login.LoginActivity;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.ToastUtils;
import framework.hz.salmon.util.SystemUtils;

public class TokenErrorReceiver extends BroadcastReceiver {
    private static final String BROADCAST_ACTION_DISC = "com.toycloud.permissions.broadcast_token_error";

    public void onReceive(Context context, Intent intent) {
        Log.e("TokenErrorReceiver", "用户退出广播");
        String token = SharepreferenceUtil.getToken();
        if (BROADCAST_ACTION_DISC.equals(intent.getAction()) && !TextUtils.isEmpty(token)) {
            try {
                context.startActivity(new Intent(context, LoginActivity.class));
                ToastUtils.showLong((CharSequence) "你的帐号在另一台设备登录，请重新登录。");
                SystemUtils.removeRecentTask((String) null, (ActivityManager) context.getSystemService("activity"));
                SharepreferenceUtil.getSharepferenceInstance(context).Logout();
            } catch (Throwable th) {
            }
        }
    }
}
