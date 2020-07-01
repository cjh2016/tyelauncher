package com.boll.tyelauncher.util;


import android.content.Context;
import android.content.Intent;

import com.boll.tyelauncher.api.model.User;
import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.model.EtsConstant;

public class EtsUtils {
    private static final String TAG = "EtsUtils";

    public static void launchEts(Context context, String action, User userInfo) {
        Logging.d(TAG, "launchEts() action = " + action + ", userInfo = " + userInfo);
        if (context != null) {
            if (!GlobalVariable.isLogin()) {
                DialogUtil.showDialogToLogin(context);
                return;
            }
            Intent intent = new Intent(action);
            intent.setPackage("com.ets100.study");
            intent.putExtra("token", userInfo.getToken());
            intent.putExtra("from", "iflystudypad");
            intent.putExtra("userProfileChanged", SharepreferenceUtil.getSharepferenceInstance(context).isUserProfileChangedForEts());
            intent.addFlags(268435456);
            if (context.getPackageManager().resolveActivity(intent, 65536) != null) {
                context.startActivity(intent);
                SharepreferenceUtil.getSharepferenceInstance(context).setUserProfileChangedForEts(false);
                return;
            }
            CustomToast.showToast(context, "未找到功能");
        }
    }

    public static void launchEtsLite(Context context, String action, User userInfo) {
        Logging.d(TAG, "launchEtsLite() action = " + action + ", userInfo = " + userInfo);
        if (context != null) {
            if (!GlobalVariable.isLogin()) {
                DialogUtil.showDialogToLogin(context);
                return;
            }
            Intent intent = new Intent(action);
            intent.setPackage(EtsConstant.ETS_LITE_PACKAGE);
            intent.putExtra("token", userInfo.getToken());
            intent.putExtra("from", "iflystudypad");
            intent.putExtra("userProfileChanged", SharepreferenceUtil.getSharepferenceInstance(context).isUserProfileChangedForEtsPri());
            intent.addFlags(268435456);
            if (context.getPackageManager().resolveActivity(intent, 65536) != null) {
                context.startActivity(intent);
                SharepreferenceUtil.getSharepferenceInstance(context).setUserProfileChangedForEtsPri(false);
                return;
            }
            CustomToast.showToast(context, "未找到功能");
        }
    }
}
