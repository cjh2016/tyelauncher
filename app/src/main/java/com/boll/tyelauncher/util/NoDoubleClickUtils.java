package com.boll.tyelauncher.util;

package com.toycloud.launcher.util;

import android.util.Log;

public class NoDoubleClickUtils {
    private static final long SPACE_TIME = 1000;
    private static long lastClickTime = 0;

    public static void initLastClickTime() {
        lastClickTime = 0;
    }

    public static synchronized boolean isDoubleClick() {
        boolean isClick2;
        synchronized (NoDoubleClickUtils.class) {
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - lastClickTime) > SPACE_TIME) {
                isClick2 = false;
                lastClickTime = currentTime;
            } else {
                isClick2 = true;
            }
            Log.e("isDoubleClick", "currentTime - lastClickTime=:" + (currentTime - lastClickTime) + "currentTime:" + currentTime + "lastClickTime:" + lastClickTime);
        }
        return isClick2;
    }
}
