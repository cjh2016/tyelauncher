package com.boll.tyelauncher.model.launcher.glide;

package com.toycloud.launcher.model.launcher.glide;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

public class GlideUtil {
    public static boolean checkContext(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 17 && ((Activity) context).isDestroyed()) {
                return false;
            }
        }
        return true;
    }
}
