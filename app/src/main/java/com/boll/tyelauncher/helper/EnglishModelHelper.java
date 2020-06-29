package com.boll.tyelauncher.helper;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.boll.tyelauncher.application.LauncherApplication;
import com.boll.tyelauncher.util.GsonUtils;
import com.boll.tyelauncher.util.SharepreferenceUtil;

public class EnglishModelHelper {
    private static final String PROTOCOL_FUNCTION_MODULE = "content://com.ets100.study.provider/function_module";
    private static final String PROTOCOL_RADAR = "content://com.ets100.study.provider/radar";
    private static final String PROTOCOL_RECENT_UNIT = "content://com.ets100.study.provider/recent_unit";
    private static final String TAG = "EnglishModelHelper";
    private static EnglishModelHelper mEnglishModelHelper;
    private ContentResolver mContentResolver;

    private EnglishModelHelper(Context context) {
        this.mContentResolver = context.getContentResolver();
    }

    public static EnglishModelHelper getInstance(Context context) {
        if (mEnglishModelHelper == null) {
            synchronized (EnglishModelHelper.class) {
                if (mEnglishModelHelper == null) {
                    mEnglishModelHelper = new EnglishModelHelper(context);
                }
            }
        }
        return mEnglishModelHelper;
    }

    public FunctionModuleBean queryFunctionModule(String token) {
        Uri functionModuleUri = Uri.parse(PROTOCOL_FUNCTION_MODULE);
        String param = JsonBuilder.newBuilder().put("token", token).put("userProfileChanged", Boolean.valueOf(SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).isUserProfileChangedForEts())).build();
        SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).setUserProfileChangedForEts(false);
        return (FunctionModuleBean) GsonUtils.changeJsonToBean(queryInternal(functionModuleUri, param), FunctionModuleBean.class);
    }

    public UnitModelBean queryRecentUnit(String token, String moduleId) {
        Uri functionModuleUri = Uri.parse(PROTOCOL_RECENT_UNIT);
        String param = JsonBuilder.newBuilder().put("token", token).put("userProfileChanged", Boolean.valueOf(SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).isUserProfileChangedForEts())).put("moduleId", moduleId).build();
        SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).setUserProfileChangedForEts(false);
        return (UnitModelBean) GsonUtils.changeJsonToBean(queryInternal(functionModuleUri, param), UnitModelBean.class);
    }

    public RadarModuleBean queryRadar(String token) {
        Uri functionModuleUri = Uri.parse(PROTOCOL_RADAR);
        String param = JsonBuilder.newBuilder().put("token", token).put("userProfileChanged", Boolean.valueOf(SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).isUserProfileChangedForEts())).build();
        SharepreferenceUtil.getSharepferenceInstance(LauncherApplication.getContext()).setUserProfileChangedForEts(false);
        return (RadarModuleBean) GsonUtils.changeJsonToBean(queryInternal(functionModuleUri, param), RadarModuleBean.class);
    }

    private String queryInternal(Uri uri, String param) {
        String result = null;
        Cursor cursor = null;
        try {
            Cursor cursor2 = this.mContentResolver.query(uri, (String[]) null, param, (String[]) null, (String) null);
            if (cursor2 == null) {
                if (cursor2 != null) {
                    cursor2.close();
                }
                return null;
            }
            if (cursor2.moveToFirst()) {
                result = cursor2.getString(0);
            }
            if (cursor2 != null) {
                cursor2.close();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e:" + e.getMessage());
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }
}
