package com.boll.tyelauncher.model.launcher.bean;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.Keep;
import android.support.annotation.RequiresApi;

import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.util.FileUtils;

@Keep
public class ForbbidenConfigs {
    public static final String[] QUERY_PROJECTION = {"ForbiddenAPP", "forbiddenAPP_List", "time_control_weekday", "time_control_weekend", "time_control_list"};
    public String mForbiddenAppList;
    public String mForbiddenApps;
    public String mTimeControlList;
    public String mTimeControlWeekDay;
    public String mTimeControlWeekEnd;

    private void readCursor(Cursor cursor) {
        int index = 0 + 1;
        this.mForbiddenApps = cursor.getString(0);
        int index2 = index + 1;
        this.mForbiddenAppList = cursor.getString(index);
        int index3 = index2 + 1;
        this.mTimeControlWeekDay = cursor.getString(index2);
        int index4 = index3 + 1;
        this.mTimeControlWeekEnd = cursor.getString(index3);
        int i = index4 + 1;
        this.mTimeControlList = cursor.getString(index4);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static ForbbidenConfigs queryFromContentProvider(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = GlobalVariable.PERSON_CONTENT_URI;
        try {
            ForbbidenConfigs result = new ForbbidenConfigs();
            Cursor cursor = contentResolver.query(uri, QUERY_PROJECTION, (String) null, (String[]) null, (String) null, (CancellationSignal) null);
            while (cursor.moveToNext()) {
                result.readCursor(cursor);
            }
            FileUtils.closeCursor(cursor);
            return result;
        } catch (Exception e) {
            FileUtils.closeCursor((Cursor) null);
            return null;
        } catch (Throwable th) {
            FileUtils.closeCursor((Cursor) null);
            throw th;
        }
    }
}