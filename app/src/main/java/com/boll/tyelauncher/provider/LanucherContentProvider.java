package com.boll.tyelauncher.provider;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.boll.tyelauncher.db.DBHelper;

public class LanucherContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.iflytek.lanucher.userinfo";
    public static final Uri PERSON_CONTENT_URI = Uri.parse("content://com.iflytek.lanucher.userinfo/userinfo");
    private static final int TABLE_CODE_PERSON = 1;
    private static final UriMatcher mUriMatcher = new UriMatcher(-1);
    private final String TAG = getClass().getSimpleName();
    private DBHelper dbHelper;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private String mTable;

    static {
        mUriMatcher.addURI("com.iflytek.lanucher.userinfo", "person", 1);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        Log.d(this.TAG, tableName + " 删除数据");
        int deleteCount = this.mDatabase.delete(tableName, selection, selectionArgs);
        if (deleteCount > 0) {
            this.mContext.getContentResolver().notifyChange(uri, (ContentObserver) null);
        }
        return deleteCount;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        Log.d(this.TAG, tableName + " 插入数据");
        long rowId = this.mDatabase.replace(tableName, (String) null, values);
        if (rowId > 0) {
            Uri rowUri = ContentUris.appendId(PERSON_CONTENT_URI.buildUpon(), rowId).build();
            Log.d(this.TAG, "rowUri------------:" + rowUri);
            getContext().getContentResolver().notifyChange(rowUri, (ContentObserver) null);
            return rowUri;
        }
        throw new SQLException("插入数据失败URI:" + uri);
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new DBHelper(getContext());
        this.mTable = DBHelper.USER_TABLE_NAME;
        this.mContext = getContext();
        this.mDatabase = this.dbHelper.getWritableDatabase();
        return this.dbHelper != null;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        Log.d(this.TAG, tableName + " 查询数据");
        return this.mDatabase.query(tableName, projection, selection, selectionArgs, (String) null, sortOrder, (String) null);
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        Log.d(this.TAG, tableName + " 更新数据");
        int updateCount = this.mDatabase.update(tableName, values, selection, selectionArgs);
        if (updateCount > 0) {
            this.mContext.getContentResolver().notifyChange(uri, (ContentObserver) null);
        }
        return updateCount;
    }

    private String getTableName(Uri uri) {
        String tableName;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case 1:
                tableName = DBHelper.USER_TABLE_NAME;
                break;
            default:
                tableName = DBHelper.USER_TABLE_NAME;
                break;
        }
        Log.d(this.TAG, "UriMatcher " + uri.toString() + ", result: " + match);
        return tableName;
    }
}
