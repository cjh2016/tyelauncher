package com.boll.tyelauncher.helper;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class FeedbackHelper {
    private static final String COLUMNNAME = "SPCOLUMNNAME";
    private static final String MESSAGE_COUNT = "message_count";
    public static final String MESSAGE_URI = "content://com.iflytek.feedback.message.provider/message";

    public static int updateMessage(Context context, int messageCount) {
        int result = -1;
        try {
            Uri uri = Uri.parse(MESSAGE_URI);
            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver == null) {
                return -1;
            }
            ContentValues values = new ContentValues();
            values.put(MESSAGE_COUNT, Integer.valueOf(messageCount));
            result = contentResolver.update(uri, values, (String) null, (String[]) null);
            return result;
        } catch (Exception e) {
        }
    }

    public static int getMessage(Context context) {
        int result = -1;
        Uri uri = Uri.parse(MESSAGE_URI);
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            return -1;
        }
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
            if (cursor == null) {
                return -1;
            }
            if (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(COLUMNNAME));
            }
            if (cursor != null) {
                cursor.close();
            }
            return result;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
