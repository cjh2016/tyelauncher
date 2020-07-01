package com.boll.tyelauncher.util;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;

import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.db.DBHelper;
import com.boll.tyelauncher.model.ForbiddenTime;
import com.boll.tyelauncher.provider.LanucherContentProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Calendar;

public class ForbiddenUtil {
    public static String getForbiddenApp(Context context) {
        String result = "";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GlobalVariable.PERSON_CONTENT_URI, new String[]{"ForbiddenAPP"}, (String) null, (String[]) null, (String) null, (CancellationSignal) null);
        } catch (Exception e) {
        }
        if (cursor == null) {
            return result;
        }
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
            Log.e("getForbiddenApp", "DB 查询结果getForbiddenApp：" + result);
        }
        cursor.close();
        return result;
    }

    public static String getForbiddenHappyAppList(Context context) {
        String result = "";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GlobalVariable.PERSON_CONTENT_URI, new String[]{"time_control_list"}, (String) null, (String[]) null, (String) null, (CancellationSignal) null);
        } catch (Exception e) {
            ToastUtils.showShort((CharSequence) "查询异常");
        }
        if (cursor == null) {
            return result;
        }
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
            Log.e("getForbiddenApp", "DB 查询结果getForbiddenHappyAppList：" + result);
        }
        cursor.close();
        return result;
    }

    public static void inSertForbiddenAppList(Context context, String liststring) {
        new DBHelper(context).getWritableDatabase();
        Uri uri = LanucherContentProvider.PERSON_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudyCPHelper.COLUMN_NAME_UNIQUE_FLAG, 1);
        contentValues.put("time_control_list", liststring);
        context.getContentResolver().update(uri, contentValues, "flag=?", new String[]{"1"});
        Log.e("getForbiddenApp", "DB 更新数据ForbiddenAPP：" + liststring);
    }

    public static String getForbiddenTime_WeekDay(Context context) {
        String result = "";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GlobalVariable.PERSON_CONTENT_URI, new String[]{"time_control_weekday"}, (String) null, (String[]) null, (String) null, (CancellationSignal) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            return result;
        }
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
            Log.e("getForbiddenApp", "DB 查询结果：" + result);
        }
        cursor.close();
        return result;
    }

    public static String getForbiddenTime_Weekend(Context context) {
        String result = "";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GlobalVariable.PERSON_CONTENT_URI, new String[]{"time_control_weekend"}, (String) null, (String[]) null, (String) null, (CancellationSignal) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            return result;
        }
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
            Log.e("getForbiddenApp", "DB 查询结果：" + result);
        }
        cursor.close();
        return result;
    }

    public static boolean isForbiddenOpen(Context mContext, String packageNme) {
        boolean isForbidden;
        String forbiddenApp = getForbiddenApp(mContext);
        if (!TextUtils.isEmpty(forbiddenApp)) {
            Log.e("forbiddenApp", forbiddenApp + ":");
            if (forbiddenApp.contains(packageNme)) {
                showNotice(1, mContext);
                return true;
            }
        }
        String forbiddenHappyAppList = getForbiddenHappyAppList(mContext);
        if (TextUtils.isEmpty(forbiddenHappyAppList)) {
            return false;
        }
        if (!forbiddenHappyAppList.contains(packageNme)) {
            return false;
        }
        String[] split = getCurrentTime().split(",");
        int day = Integer.parseInt(split[0]);
        int hour = Integer.parseInt(split[1]);
        int currentMinute = (hour * 60) + Integer.parseInt(split[2]);
        if (day == 1 || day == 7) {
            String forbiddenTime_weekend = getForbiddenTime_Weekend(mContext);
            if (TextUtils.isEmpty(forbiddenTime_weekend)) {
                return false;
            }
            Gson gson = new GsonBuilder().create();
            Log.e("forbiddenTime_weekend", forbiddenTime_weekend + ":");
            ForbiddenTime forbiddenTime = (ForbiddenTime) gson.fromJson(forbiddenTime_weekend, ForbiddenTime.class);
            int startTime = forbiddenTime.getStartTime();
            int endTime = forbiddenTime.getEndTime();
            if (currentMinute < startTime || currentMinute > endTime) {
                showNotice(2, mContext);
                isForbidden = true;
            } else {
                isForbidden = false;
            }
        } else {
            String forbiddenTime_weekDay = getForbiddenTime_WeekDay(mContext);
            if (TextUtils.isEmpty(forbiddenTime_weekDay)) {
                return false;
            }
            ForbiddenTime forbiddenTime2 = (ForbiddenTime) new GsonBuilder().create().fromJson(forbiddenTime_weekDay, ForbiddenTime.class);
            int startTime2 = forbiddenTime2.getStartTime();
            int endTime2 = forbiddenTime2.getEndTime();
            if (currentMinute < startTime2 || currentMinute > endTime2) {
                showNotice(2, mContext);
                isForbidden = true;
            } else {
                isForbidden = false;
            }
        }
        return isForbidden;
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(1);
        int i2 = calendar.get(2) + 1;
        int day = calendar.get(7);
        int hour = calendar.get(11);
        return day + "," + hour + "," + calendar.get(12);
    }

    public static void showNotice(int type, Context context) {
        new ShowAppForbiddenNoticeWin.Builder(context, new PadInfoUtil(context).getSnCode(), type).build().showPopWin(context);
    }

    public static boolean isForbiddenHappyApp(Context mContext) {
        boolean isForbidden;
        if (TextUtils.isEmpty(getForbiddenHappyAppList(mContext))) {
            return false;
        }
        String[] split = getCurrentTime().split(",");
        int day = Integer.parseInt(split[0]);
        int hour = Integer.parseInt(split[1]);
        int currentMinute = (hour * 60) + Integer.parseInt(split[2]);
        if (day == 1 || day == 7) {
            String forbiddenTime_weekend = getForbiddenTime_Weekend(mContext);
            if (TextUtils.isEmpty(forbiddenTime_weekend)) {
                return false;
            }
            Gson gson = new GsonBuilder().create();
            Log.e("forbiddenTime_weekend", forbiddenTime_weekend + ":");
            ForbiddenTime forbiddenTime = (ForbiddenTime) gson.fromJson(forbiddenTime_weekend, ForbiddenTime.class);
            int startTime = forbiddenTime.getStartTime();
            int endTime = forbiddenTime.getEndTime();
            if (currentMinute >= startTime && currentMinute <= endTime) {
                return false;
            }
            isForbidden = true;
        } else {
            String forbiddenTime_weekDay = getForbiddenTime_WeekDay(mContext);
            if (TextUtils.isEmpty(forbiddenTime_weekDay)) {
                return false;
            }
            ForbiddenTime forbiddenTime2 = (ForbiddenTime) new GsonBuilder().create().fromJson(forbiddenTime_weekDay, ForbiddenTime.class);
            int startTime2 = forbiddenTime2.getStartTime();
            int endTime2 = forbiddenTime2.getEndTime();
            Log.e("forbiddenTime_weekday", startTime2 + "startTime:endTime" + endTime2 + "currentMinute:" + currentMinute);
            if (currentMinute < startTime2 || currentMinute > endTime2) {
                isForbidden = true;
            } else {
                isForbidden = false;
            }
        }
        return isForbidden;
    }

    public static boolean isForBiddenApp(String packageName, Context context) {
        String forbiddenApp = getForbiddenApp(context);
        String forbiddenHappyAppList = getForbiddenHappyAppList(context);
        if (!TextUtils.isEmpty(forbiddenApp) && forbiddenApp.contains(packageName)) {
            return true;
        }
        if (TextUtils.isEmpty(forbiddenHappyAppList)) {
            return false;
        }
        if (!isForbiddenHappyApp(context)) {
            return false;
        }
        if (!forbiddenHappyAppList.contains(packageName)) {
            return false;
        }
        return true;
    }
}