package com.boll.tyelauncher.model.launcher;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.util.KeyEventUtils;
import com.boll.tyelauncher.util.ListUtils;
import com.boll.tyelauncher.util.PadInfoUtil;
import com.boll.tyelauncher.util.SystemUtils;
import com.orhanobut.logger.Logger;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KillProcessTask implements Runnable {
    private static Method METHOD_forceStopPackage = null;
    private static final String TAG = "KillProcessTask";
    private static Set<String> WHITE_BLANK = new HashSet();
    private final List<String> mAppList;
    private final boolean mClearAllPkgs;
    private final Context mContext;

    static {
        initStatic();
    }

    private static final void initStatic() {
        WHITE_BLANK.add(BuildConfig.APPLICATION_ID);
        WHITE_BLANK.add("com.iflytek.xiri");
        WHITE_BLANK.add(GlobalVariable.FEEDBACKPACKAGENAME);
        WHITE_BLANK.add("com.iflytek.pushservices");
        try {
            METHOD_forceStopPackage = ActivityManager.class.getMethod("forceStopPackage", new Class[]{String.class});
            METHOD_forceStopPackage.setAccessible(true);
        } catch (Throwable exp) {
            Logger.e(TAG, "initStatic error", exp);
        }
    }

    public KillProcessTask(Context context, List<String> pkgList, boolean clearAllPkgs) {
        this.mAppList = pkgList;
        this.mContext = context;
        this.mClearAllPkgs = clearAllPkgs;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void run() {
        ActivityManager activityManager;
        boolean forgroundApp;
        if (this.mContext != null && !ListUtils.isEmpty((List) this.mAppList) && (activityManager = (ActivityManager) this.mContext.getSystemService("activity")) != null) {
            for (String pkg : this.mAppList) {
                if (!TextUtils.isEmpty(pkg) && !WHITE_BLANK.contains(pkg)) {
                    Logger.d(TAG, "正在杀死进程：" + pkg);
                    try {
                        String packageName = PadInfoUtil.getPropString("sys.toycloud.curpkg", "");
                        String activityName = PadInfoUtil.getPropString("sys.toycloud.curactivity", "");
                        if (pkg.equals(packageName) || (packageName.equals("com.android.systemui") && activityName.equals("com.android.systemui.recents.RecentsActivity"))) {
                            forgroundApp = true;
                        } else {
                            forgroundApp = false;
                        }
                        if (forgroundApp) {
                            KeyEventUtils.sendKeyEvent(this.mContext, 257, 3, false);
                        }
                    } catch (Throwable exp) {
                        Logger.e(TAG, "sendKeyEvent failed: " + pkg, exp);
                    }
                    try {
                        SystemUtils.removeRecentTask(pkg, activityManager);
                    } catch (Throwable exp2) {
                        Logger.e(TAG, "removeRecentTask error", exp2);
                    }
                }
            }
        }
    }
}