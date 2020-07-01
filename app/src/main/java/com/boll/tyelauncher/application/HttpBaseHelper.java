package com.boll.tyelauncher.application;

package com.toycloud.launcher.application;

import com.iflytek.biz.http.BaseParameterManager;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.api.model.User;

public class HttpBaseHelper {
    private static final String TAG = "HttpBaseHelper";

    public static final void setUserId(String userId) {
        Logger.d(TAG, "setUserId: updating userid = " + userId);
        BaseParameterManager.getInstance().setUserId(userId);
    }

    public static final void setUserInfo(User user) {
        Logger.d(TAG, "setUserInfo: updating user = " + user);
        if (user == null) {
            BaseParameterManager.getInstance().setUserId((String) null);
            return;
        }
        Logger.d(TAG, "setUserInfo: updating userid = " + user.getUserid());
        BaseParameterManager.getInstance().setUserId(user.getUserid());
    }
}
