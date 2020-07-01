package com.boll.tyelauncher.biz.globalconfig;

package com.toycloud.launcher.biz.globalconfig;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class GlobalConfigProvider extends ContentProvider {
    private static final String QUERY_SINGLE_URI = "content://com.iflytek.launcher.config/query_single";

    public boolean onCreate() {
        return false;
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String value = null;
        String uriStr = uri.toString();
        if (uriStr.startsWith(QUERY_SINGLE_URI)) {
            String key = uriStr.substring(QUERY_SINGLE_URI.length());
            if (key.startsWith("/")) {
                key = key.substring(1);
            }
            value = GlobalConfigManager.getInstance().getConfig(key);
        }
        return wrapValue(value);
    }

    private MatrixCursor wrapValue(String value) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"value"});
        matrixCursor.addRow(new String[]{value});
        return matrixCursor;
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String uriStr = uri.toString();
        if (!uriStr.startsWith(QUERY_SINGLE_URI)) {
            return null;
        }
        String key = uriStr.substring(QUERY_SINGLE_URI.length());
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        String value = null;
        if (values != null) {
            value = values.getAsString("value");
        }
        GlobalConfigManager.getInstance().putConfig(key, value);
        getContext().getContentResolver().notifyChange(uri, (ContentObserver) null);
        return uri;
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}