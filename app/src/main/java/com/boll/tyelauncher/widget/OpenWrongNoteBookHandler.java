package com.boll.tyelauncher.widget;

package com.toycloud.launcher.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.UpdateUtil;
import com.toycloud.launcher.view.DialogUtil;

public class OpenWrongNoteBookHandler {
    private static final String TAG = "OpenWrongNoteBookHandler";

    public static final void openWrongNoteBook(Context context) {
        if (!GlobalVariable.isLogin()) {
            DialogUtil.showDialogToLogin(context);
        }
        new UpdateUtil(context, new OpenWrongNoteBookHandler$$Lambda$0(context)).checkUpdate("com.iflytek.wrongnotebook", new PackageUtils(context).getVersionCode("com.iflytek.wrongnotebook"));
    }

    static final /* synthetic */ void lambda$openWrongNoteBook$5$OpenWrongNoteBookHandler(Context context, boolean isUpdate, boolean isMustUpdate) {
        if (!isMustUpdate && !isUpdate) {
            try {
                User userInfo = SharepreferenceUtil.getSharepferenceInstance(context).getUserInfo();
                if (userInfo != null) {
                    Intent intent = new Intent();
                    intent.addFlags(268435456);
                    intent.putExtra("token", userInfo.getToken());
                    intent.putExtra(AppContants.KEY_GRADE_NAME, GradeUtil.getGradeName(userInfo.getGradecode()));
                    intent.putExtra(AppContants.KEY_GRADE_CODE, userInfo.getGradecode());
                    intent.putExtra(GlobalVariable.KEY_SUBJECT_CODE, "02");
                    intent.setComponent(GlobalVariable.createWrongNotebookComponentName());
                    context.startActivity(intent);
                }
            } catch (Throwable exp) {
                Log.e(TAG, "启动错题本失败", exp);
            }
        }
    }
}