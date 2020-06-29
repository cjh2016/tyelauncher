package com.boll.tyelauncher.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author: caijianhui
 * @date: 2020/6/29 15:41
 * @description:
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "lanuch.db";
    private static final int DATABASE_VERSION = 10;
    public static final String USER_TABLE_NAME = "userinfo";
    private final String SQL_CREATE_TABLE = "create table if not exists userinfo(_id integer primary key autoincrement,flag integer unique, username text,realname text,gradecode text,areacode text,userid text,schoolid text,sex text,token text,storageType integer,ForbiddenAPP text,forbiddenAPP_List text,time_control_weekday text,time_control_weekend text,time_control_list text,headurl text)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQLiteDatabase", "CREATE TABLE IF NOT EXISTS ");
        db.execSQL("create table if not exists userinfo(_id integer primary key autoincrement,flag integer unique, username text,realname text,gradecode text,areacode text,userid text,schoolid text,sex text,token text,storageType integer,ForbiddenAPP text,forbiddenAPP_List text,time_control_weekday text,time_control_weekend text,time_control_list text,headurl text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("onUpgrade-->", "oldVersion:" + oldVersion + "newVersion:" + newVersion);
        switch (newVersion) {
            case 1:
                db.execSQL("create table if not exists userinfo(_id integer primary key autoincrement,flag integer unique, username text,realname text,gradecode text,areacode text,userid text,schoolid text,sex text,token text,storageType integer,ForbiddenAPP text,forbiddenAPP_List text,time_control_weekday text,time_control_weekend text,time_control_list text,headurl text)");
                return;
            case 10:
                db.execSQL("alter table userinfo add column ForbiddenAPP text");
                db.execSQL("alter table userinfo add column time_control_weekday text");
                db.execSQL("alter table userinfo add column time_control_weekend text");
                db.execSQL("alter table userinfo add column forbiddenAPP_List text");
                db.execSQL("alter table userinfo add column time_control_list text");
                return;
            default:
                return;
        }
    }
}
