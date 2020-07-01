package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.toycloud.launcher.db.DBHelper;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.provide.LanucherContentProvider;
import com.toycloud.launcher.util.SharepreferenceUtil;

public class SdcardReceiver extends BroadcastReceiver {
    private int mStorageType;

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.intent.action.MEDIA_MOUNTED")) {
            Log.d("tag", "sdcard mounted");
        } else if (action.equals("android.intent.action.MEDIA_UNMOUNTED")) {
            Log.d("tag", "sdcard unmounted");
            this.mStorageType = 0;
            updateStorageType(context);
            SharepreferenceUtil.putInt("storageType", 0);
        }
    }

    private void updateStorageType(Context context) {
        new DBHelper(context).getWritableDatabase();
        Uri uri = LanucherContentProvider.PERSON_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudyCPHelper.COLUMN_NAME_UNIQUE_FLAG, 1);
        contentValues.put("storageType", Integer.valueOf(this.mStorageType));
        context.getContentResolver().update(uri, contentValues, "flag=?", new String[]{"1"});
    }
}
