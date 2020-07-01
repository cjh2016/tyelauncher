package com.boll.tyelauncher.biz.globalconfig;


package com.toycloud.launcher.biz.globalconfig;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.toycloud.launcher.application.LauncherApplication;
import com.toycloud.launcher.biz.globalconfig.entities.Config;
import java.util.Map;

public class GlobalConfigDatabase {
    private static final String CREATE_CONFIG_TABLE = "create table global_config(name text primary key,value text,type int)";
    private static final String DB_NAME = "global_config.db";
    private static final int DB_VERSION = 2;
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_VALUE = "value";
    private static final String FILED_NAME = "name";
    private static final String TABLE_NAME = "global_config";
    private static final String UPGRADE_TO_2 = "alter table global_config add column type int default 0";
    private static SQLiteOpenHelper sSqLiteOpenHelper = new SQLiteOpenHelper(LauncherApplication.getContext(), DB_NAME, (SQLiteDatabase.CursorFactory) null, 2) {
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(GlobalConfigDatabase.CREATE_CONFIG_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:
                    db.execSQL(GlobalConfigDatabase.UPGRADE_TO_2);
                    return;
                default:
                    return;
            }
        }
    };

    /* JADX WARNING: Removed duplicated region for block: B:10:0x006c A[DONT_GENERATE] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map<java.lang.String, com.toycloud.launcher.biz.globalconfig.entities.Config> queryAll() {
        /*
            android.database.sqlite.SQLiteOpenHelper r2 = sSqLiteOpenHelper
            android.database.sqlite.SQLiteDatabase r1 = r2.getReadableDatabase()
            r11 = 0
            java.lang.String r2 = "global_config"
            r3 = 3
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ all -> 0x0073 }
            r4 = 0
            java.lang.String r5 = "name"
            r3[r4] = r5     // Catch:{ all -> 0x0073 }
            r4 = 1
            java.lang.String r5 = "value"
            r3[r4] = r5     // Catch:{ all -> 0x0073 }
            r4 = 2
            java.lang.String r5 = "type"
            r3[r4] = r5     // Catch:{ all -> 0x0073 }
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r11 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0073 }
            java.util.LinkedHashMap r10 = new java.util.LinkedHashMap     // Catch:{ all -> 0x0073 }
            r10.<init>()     // Catch:{ all -> 0x0073 }
            if (r11 == 0) goto L_0x006a
            boolean r2 = r11.moveToFirst()     // Catch:{ all -> 0x0073 }
            if (r2 == 0) goto L_0x006a
            java.lang.String r2 = "name"
            int r13 = r11.getColumnIndex(r2)     // Catch:{ all -> 0x0073 }
            java.lang.String r2 = "value"
            int r17 = r11.getColumnIndex(r2)     // Catch:{ all -> 0x0073 }
            java.lang.String r2 = "type"
            int r15 = r11.getColumnIndex(r2)     // Catch:{ all -> 0x0073 }
        L_0x0043:
            java.lang.String r12 = r11.getString(r13)     // Catch:{ all -> 0x0073 }
            r0 = r17
            java.lang.String r16 = r11.getString(r0)     // Catch:{ all -> 0x0073 }
            int r14 = r11.getInt(r15)     // Catch:{ all -> 0x0073 }
            com.toycloud.launcher.biz.globalconfig.entities.Config r9 = new com.toycloud.launcher.biz.globalconfig.entities.Config     // Catch:{ all -> 0x0073 }
            r9.<init>()     // Catch:{ all -> 0x0073 }
            r9.setKey(r12)     // Catch:{ all -> 0x0073 }
            r0 = r16
            r9.setValue(r0)     // Catch:{ all -> 0x0073 }
            r9.setType(r14)     // Catch:{ all -> 0x0073 }
            r10.put(r12, r9)     // Catch:{ all -> 0x0073 }
            boolean r2 = r11.moveToNext()     // Catch:{ all -> 0x0073 }
            if (r2 != 0) goto L_0x0043
        L_0x006a:
            if (r11 == 0) goto L_0x006f
            r11.close()
        L_0x006f:
            r1.close()
            return r10
        L_0x0073:
            r2 = move-exception
            if (r11 == 0) goto L_0x0079
            r11.close()
        L_0x0079:
            r1.close()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.biz.globalconfig.GlobalConfigDatabase.queryAll():java.util.Map");
    }

    public static void saveAllServerConfigs(Map<String, Config> configMap) {
        SQLiteDatabase database = sSqLiteOpenHelper.getWritableDatabase();
        try {
            database.execSQL("delete from global_config where type = 0");
            if (configMap != null && !configMap.isEmpty()) {
                database.beginTransaction();
                for (Map.Entry<String, Config> entry : configMap.entrySet()) {
                    if (entry.getValue().getType() != 1) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(FILED_NAME, entry.getKey());
                        contentValues.put(FIELD_VALUE, entry.getValue().getValue());
                        contentValues.put("type", 0);
                        database.insert(TABLE_NAME, (String) null, contentValues);
                    }
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                database.close();
            }
        } finally {
            database.close();
        }
    }

    public static void update(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            SQLiteDatabase database = sSqLiteOpenHelper.getWritableDatabase();
            try {
                database.beginTransaction();
                database.execSQL("delete from global_config where name = ?", new Object[]{key});
                database.execSQL("insert into global_config values(?,?,?)", new Object[]{key, value, 1});
                database.setTransactionSuccessful();
                database.endTransaction();
            } finally {
                database.close();
            }
        }
    }
}
