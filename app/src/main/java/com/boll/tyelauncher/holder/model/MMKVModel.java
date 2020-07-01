package com.boll.tyelauncher.holder.model;

package com.toycloud.launcher.holder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

public class MMKVModel {
    private static final String TAG = "MMKVModel";
    private static boolean sInited = false;
    public MMKV mModel;

    public MMKVModel(Context context) {
        initIfNeeded(context);
        try {
            this.mModel = MMKV.defaultMMKV();
        } catch (Throwable th) {
        }
    }

    private static void initIfNeeded(Context context) {
        if (!sInited) {
            try {
                MMKV.initialize(context);
                sInited = true;
            } catch (Throwable exp) {
                Logger.e(TAG, "initIfNeeded error", exp);
            }
        }
    }

    public void putString(String key, String value) {
        if (sInited && this.mModel != null) {
            this.mModel.edit().putString(key, value).commit();
        }
    }

    public String getString(String key, String defValue) {
        return (!sInited || this.mModel == null) ? defValue : this.mModel.getString(key, defValue);
    }

    public String getString(String key) {
        if (!sInited || this.mModel == null) {
            return null;
        }
        return this.mModel.getString(key, (String) null);
    }

    public void remove(String... keys) {
        if (sInited && this.mModel != null && keys != null && keys.length != 0) {
            SharedPreferences.Editor editor = this.mModel.edit();
            boolean changed = false;
            for (String key : keys) {
                if (!TextUtils.isEmpty(key)) {
                    editor.remove(key);
                    changed = true;
                }
            }
            if (changed) {
                editor.apply();
            }
        }
    }
}
