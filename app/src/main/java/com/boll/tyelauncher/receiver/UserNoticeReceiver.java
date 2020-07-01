package com.boll.tyelauncher.receiver;

package com.toycloud.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.ui.common.ParentMangerActivity;
import com.toycloud.launcher.ui.common.UserHelpActivity;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.PkgUtil;
import com.toycloud.launcher.util.ToastUtils;

public class UserNoticeReceiver extends BroadcastReceiver {
    private static String USEFAMILYHELP = "com.toycloud.action.SETTINGS_OPEN_PARENT_CTRL";
    private static String USERFEEDBACK = "com.toycloud.action.SETTINGS_OPEN_FEEDBACK";
    private static String USERHELP = "com.toycloud.action.SETTINGS_OPEN_USER_MANUAL";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(USERHELP)) {
            Intent intent_activity = new Intent(context, UserHelpActivity.class);
            intent_activity.putExtra(AppContants.KEY_TYPE, 1);
            context.startActivity(intent_activity);
        } else if (action.equals(USERFEEDBACK)) {
            if (PackageUtils.isAvilible(context, GlobalVariable.FEEDBACKPACKAGENAME)) {
                try {
                    PkgUtil.startAppByPackName(context, GlobalVariable.FEEDBACKPACKAGENAME);
                } catch (Throwable th) {
                }
            } else {
                ToastUtils.showShort((CharSequence) "未安装问题反馈应用");
            }
        } else if (action.equals(USEFAMILYHELP)) {
            Intent intent_activity2 = new Intent(context, ParentMangerActivity.class);
            intent_activity2.putExtra(AppContants.KEY_TYPE, 2);
            context.startActivity(intent_activity2);
        }
    }
}