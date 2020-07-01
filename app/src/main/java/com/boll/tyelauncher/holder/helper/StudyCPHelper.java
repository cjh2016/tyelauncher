package com.boll.tyelauncher.holder.helper;

package com.toycloud.launcher.holder.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.holder.model.StudySnapshotItem;
import com.toycloud.launcher.holder.presenter.CatalogItem;
import com.toycloud.launcher.util.GsonUtils;
import java.util.HashMap;
import java.util.Map;

public class StudyCPHelper {
    public static final String COLUMN_NAME_CATALOG = "catalog";
    public static final String COLUMN_NAME_GRADE_CODE = "gradecode";
    public static final String COLUMN_NAME_MAPPING = "mapping";
    public static final String COLUMN_NAME_SUBJECT_CODE = "subjectcode";
    public static final String COLUMN_NAME_UNIQUE_FLAG = "flag";
    public static final String COLUMN_NAME_USERID = "userid";
    public static final String COLUMN_NAME_VERSION = "version";
    public static final String SELECTION_USERID = "userid=? ";
    public static final String SELECTION_USERID_AND_SUBJECT = "userid=? AND subjectcode=? ";
    public static final String TAG = "StudyCPHelper";

    public interface IStudyContentObserver {
        void onStudyContentChanged(Map<String, StudySnapshotItem> map);
    }

    private static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable th) {
            }
        }
    }

    public static final Map<String, StudySnapshotItem> query(Context context, String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        return doQuery(context, SELECTION_USERID, new String[]{userId});
    }

    public static final StudySnapshotItem query(Context context, String userId, String subject) {
        Map<String, StudySnapshotItem> result;
        if (!TextUtils.isEmpty(userId) && (result = doQuery(context, SELECTION_USERID_AND_SUBJECT, new String[]{userId, subject})) != null) {
            return result.get(subject);
        }
        return null;
    }

    private static final Map<String, StudySnapshotItem> doQuery(Context context, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GlobalVariable.STUDY_SNAPSHOT_CONTENT_URI, (String[]) null, selection, selectionArgs, (String) null, (CancellationSignal) null);
            int count = cursor.getCount();
            if (count <= 0) {
                return null;
            }
            HashMap hashMap = new HashMap(count);
            while (cursor.moveToNext()) {
                StudySnapshotItem item = readCursor(cursor);
                if (item != null && item.isMiddleSchool()) {
                    StudySnapshotItem prev = (StudySnapshotItem) hashMap.get(item.mSubjectCode);
                    if (prev != null) {
                        if (item.mVersion >= prev.mVersion) {
                            hashMap.put(item.mSubjectCode, item);
                        }
                    } else {
                        hashMap.put(item.mSubjectCode, item);
                    }
                }
            }
            if (hashMap.isEmpty()) {
                closeCursor(cursor);
                return null;
            }
            closeCursor(cursor);
            return hashMap;
        } catch (Throwable exp) {
            Log.e(TAG, "read content provider error", exp);
            return null;
        } finally {
            closeCursor(cursor);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v15, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v12, resolved type: com.toycloud.launcher.api.response.MappingInfoResponse} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.toycloud.launcher.holder.model.StudySnapshotItem readCursor(android.database.Cursor r18) {
        /*
            java.lang.String r3 = "userid"
            r0 = r18
            int r15 = r0.getColumnIndex(r3)     // Catch:{ Throwable -> 0x0087 }
            r0 = r18
            java.lang.String r4 = r0.getString(r15)     // Catch:{ Throwable -> 0x0087 }
            boolean r3 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x0087 }
            if (r3 == 0) goto L_0x0016
            r3 = 0
        L_0x0015:
            return r3
        L_0x0016:
            java.lang.String r3 = "subjectcode"
            r0 = r18
            int r14 = r0.getColumnIndex(r3)     // Catch:{ Throwable -> 0x0087 }
            r0 = r18
            java.lang.String r5 = r0.getString(r14)     // Catch:{ Throwable -> 0x0087 }
            boolean r3 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x0087 }
            if (r3 == 0) goto L_0x002c
            r3 = 0
            goto L_0x0015
        L_0x002c:
            java.lang.String r3 = "catalog"
            r0 = r18
            int r10 = r0.getColumnIndex(r3)     // Catch:{ Throwable -> 0x0087 }
            r0 = r18
            java.lang.String r2 = r0.getString(r10)     // Catch:{ Throwable -> 0x0087 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x0087 }
            if (r3 == 0) goto L_0x0042
            r3 = 0
            goto L_0x0015
        L_0x0042:
            java.lang.Class<com.toycloud.launcher.holder.presenter.CatalogItem> r3 = com.toycloud.launcher.holder.presenter.CatalogItem.class
            java.lang.Object r6 = com.toycloud.launcher.util.GsonUtils.changeJsonToBean(r2, r3)     // Catch:{ Throwable -> 0x0087 }
            com.toycloud.launcher.holder.presenter.CatalogItem r6 = (com.toycloud.launcher.holder.presenter.CatalogItem) r6     // Catch:{ Throwable -> 0x0087 }
            if (r6 != 0) goto L_0x004e
            r3 = 0
            goto L_0x0015
        L_0x004e:
            java.lang.String r3 = "mapping"
            r0 = r18
            int r13 = r0.getColumnIndex(r3)     // Catch:{ Throwable -> 0x0087 }
            r0 = r18
            java.lang.String r12 = r0.getString(r13)     // Catch:{ Throwable -> 0x0087 }
            r7 = 0
            boolean r3 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Throwable -> 0x0087 }
            if (r3 != 0) goto L_0x006d
            java.lang.Class<com.toycloud.launcher.api.response.MappingInfoResponse> r3 = com.toycloud.launcher.api.response.MappingInfoResponse.class
            java.lang.Object r3 = com.toycloud.launcher.util.GsonUtils.changeJsonToBean(r12, r3)     // Catch:{ Throwable -> 0x0093 }
            r0 = r3
            com.toycloud.launcher.api.response.MappingInfoResponse r0 = (com.toycloud.launcher.api.response.MappingInfoResponse) r0     // Catch:{ Throwable -> 0x0093 }
            r7 = r0
        L_0x006d:
            r8 = 0
            java.lang.String r3 = "version"
            r0 = r18
            int r16 = r0.getColumnIndex(r3)     // Catch:{ Throwable -> 0x0087 }
            if (r16 < 0) goto L_0x0081
            r0 = r18
            r1 = r16
            long r8 = r0.getLong(r1)     // Catch:{ Throwable -> 0x0087 }
        L_0x0081:
            com.toycloud.launcher.holder.model.StudySnapshotItem r3 = new com.toycloud.launcher.holder.model.StudySnapshotItem     // Catch:{ Throwable -> 0x0087 }
            r3.<init>(r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x0087 }
            goto L_0x0015
        L_0x0087:
            r11 = move-exception
            java.lang.String r3 = "StudyCPHelper"
            java.lang.String r17 = "readCursor error"
            r0 = r17
            android.util.Log.e(r3, r0, r11)
            r3 = 0
            goto L_0x0015
        L_0x0093:
            r11 = move-exception
            java.lang.String r3 = "StudyCPHelper"
            java.lang.String r17 = "changeJsonToBean error"
            r0 = r17
            android.util.Log.e(r3, r0, r11)     // Catch:{ Throwable -> 0x0087 }
            goto L_0x006d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.holder.helper.StudyCPHelper.readCursor(android.database.Cursor):com.toycloud.launcher.holder.model.StudySnapshotItem");
    }

    public static final String buildFlag(String userId, CatalogItem item) {
        return userId + "/" + item.subjectCode + "/" + item.gradeCode;
    }

    public static final boolean insertOrUpdate(Context context, String userId, CatalogItem item, MappingInfoResponse response) {
        if (TextUtils.isEmpty(userId) || item == null || TextUtils.isEmpty(item.subjectCode)) {
            return false;
        }
        String catalog = GsonUtils.toJson(item);
        String mapping = "";
        if (response != null) {
            mapping = GsonUtils.toJson(response);
        }
        try {
            String flag = buildFlag(userId, item);
            Uri uri = GlobalVariable.STUDY_SNAPSHOT_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put("userid", userId);
            values.put(COLUMN_NAME_UNIQUE_FLAG, flag);
            values.put(COLUMN_NAME_SUBJECT_CODE, item.subjectCode);
            values.put(COLUMN_NAME_GRADE_CODE, item.gradeCode);
            values.put(COLUMN_NAME_CATALOG, catalog);
            values.put(COLUMN_NAME_MAPPING, mapping);
            contentResolver.insert(uri, values);
            return true;
        } catch (Throwable exp) {
            Log.e(TAG, "content provider insert error", exp);
            return false;
        }
    }

    public static final void unRegisterObserver(Context context, ContentObserver contentObserver) {
        if (contentObserver != null) {
            Log.d(TAG, "unRegisterObserver");
            context.getApplicationContext().getContentResolver().unregisterContentObserver(contentObserver);
        }
    }

    public static final ContentObserver registerObserver(Context context, final String userId, final IStudyContentObserver observer) {
        Log.d(TAG, "registerObserver");
        try {
            final Context appContext = context.getApplicationContext();
            ContentObserver contentObserver = new ContentObserver((Handler) null) {
                public void onChange(boolean selfChange) {
                    Log.d(StudyCPHelper.TAG, "snapshot onChange: querying");
                    final Map<String, StudySnapshotItem> items = StudyCPHelper.query(appContext, userId);
                    if (items == null || items.isEmpty() || observer == null) {
                        Log.d(StudyCPHelper.TAG, "query items: 0");
                        return;
                    }
                    Log.d(StudyCPHelper.TAG, "query items: " + items.size());
                    GlobalVariable.sUIHandler.post(new Runnable() {
                        public void run() {
                            observer.onStudyContentChanged(items);
                        }
                    });
                }
            };
            appContext.getContentResolver().registerContentObserver(GlobalVariable.STUDY_SNAPSHOT_CONTENT_URI, true, contentObserver);
            return contentObserver;
        } catch (Throwable exp) {
            Log.e(TAG, "registerContentObserver error", exp);
            return null;
        }
    }
}