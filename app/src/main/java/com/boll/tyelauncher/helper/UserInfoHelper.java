package com.boll.tyelauncher.helper;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

public class UserInfoHelper {
    private static final String USER_PATH = "content://com.iflytek.lanucher.userinfo/userinfo";
    private static UserInfoHelper mUserInfoHelper;
    private Context mApplicationContext;

    private UserInfoHelper(Context context) {
        this.mApplicationContext = context;
    }

    public static UserInfoHelper getInstance(Context context) {
        if (mUserInfoHelper == null) {
            synchronized (UserInfoHelper.class) {
                if (mUserInfoHelper == null) {
                    mUserInfoHelper = new UserInfoHelper(context.getApplicationContext());
                }
            }
        }
        return mUserInfoHelper;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public String getUserId() {
        String userId = null;
        Cursor cursor = null;
        try {
            Cursor cursor2 = this.mApplicationContext.getContentResolver().query(Uri.parse(USER_PATH), new String[]{"userid"}, (String) null, (String[]) null, (String) null, (CancellationSignal) null);
            if (cursor2 != null) {
                while (cursor2.moveToNext()) {
                    userId = cursor2.getString(0);
                }
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return userId;
    }
}