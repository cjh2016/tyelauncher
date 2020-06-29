package com.boll.tyelauncher.model.launcher.helper;

package com.toycloud.launcher.model.launcher.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.boll.tyelauncher.application.LauncherApplication;
import com.boll.tyelauncher.easytrans.LogAgent;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.BuildConfig;
import com.toycloud.launcher.exception.AppLockException;
import java.util.HashSet;
import java.util.Set;

public class AppLockHelper {
    private static final String FORBIDDEN_AUTO_PACKAGE = "forbidden_auto_package";
    private static final String KEY_PKG = "forbidden_package";
    private static final String TAG = "AppLockHelper";

    private static boolean isEmpty(Set<String> set) {
        return set == null || set.isEmpty();
    }

    private static int size(Set<String> set) {
        if (set == null) {
            return 0;
        }
        return set.size();
    }

    public static final void replace(Context context, Set<String> forbbidenApps, Set<String> funApps) {
        String pkgs;
        if (!isEmpty(forbbidenApps) || !isEmpty(funApps)) {
            Set<String> set = new HashSet<>();
            set.add(BuildConfig.APPLICATION_ID);
            StringBuilder sb = new StringBuilder();
            if (!isEmpty(forbbidenApps)) {
                for (String pkg : forbbidenApps) {
                    if (!set.contains(pkg)) {
                        set.add(pkg);
                        sb.append(VoiceWakeuperAidl.PARAMS_SEPARATE).append(pkg).append(":0");
                    }
                }
            }
            if (!isEmpty(funApps)) {
                for (String pkg2 : funApps) {
                    if (!set.contains(pkg2)) {
                        set.add(pkg2);
                        sb.append(VoiceWakeuperAidl.PARAMS_SEPARATE).append(pkg2).append(":1");
                    }
                }
            }
            pkgs = sb.toString();
        } else {
            pkgs = "";
        }
        Settings.Global.putString(context.getContentResolver(), KEY_PKG, pkgs);
    }

    public static final void updateAutoBootPackageWhiteList(Context context, String pkgs) {
        if (context == null && (context = LauncherApplication.getContext()) == null) {
            Logger.w(TAG, "updateAutoBootPackageWhiteList失败：context为null");
            LogAgent.onError(new AppLockException("updateAutoBootPackageWhiteList失败：context为null"));
            return;
        }
        if (pkgs == null) {
            pkgs = "";
            LogAgent.onError(new AppLockException("Warnning: updateAutoBootPackageWhiteList: pkgs == null"));
        }
        String[] values = pkgs.split(",");
        if (values == null) {
            values = new String[0];
        }
        try {
            ContentResolver resolver = context.getContentResolver();
            StringBuffer bf = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                if (i == 0) {
                    bf.append(VoiceWakeuperAidl.PARAMS_SEPARATE + values[i] + VoiceWakeuperAidl.PARAMS_SEPARATE);
                } else {
                    bf.append(values[i] + VoiceWakeuperAidl.PARAMS_SEPARATE);
                }
            }
            Settings.Global.putString(resolver, FORBIDDEN_AUTO_PACKAGE, bf.toString());
        } catch (Throwable exp) {
            LogAgent.onError(new AppLockException("updateAutoBootPackageWhiteList失败", exp));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int getAppLockStatus(Context context, String pkg) {
        String pkgs = Settings.Global.getString(context.getContentResolver(), KEY_PKG);
        if (TextUtils.isEmpty(pkgs)) {
            return -1;
        }
        if (pkgs.contains(VoiceWakeuperAidl.PARAMS_SEPARATE + pkg + ":0")) {
            return 0;
        }
        if (pkgs.contains(VoiceWakeuperAidl.PARAMS_SEPARATE + pkg + ":1")) {
            return 1;
        }
        return -1;
    }
}
