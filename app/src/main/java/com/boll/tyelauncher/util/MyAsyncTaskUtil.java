package com.boll.tyelauncher.util;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import java.io.IOException;

public class MyAsyncTaskUtil extends AsyncTask<Void, Integer, Integer> {
    private Context mContext;
    private String mPath;

    public MyAsyncTaskUtil(Context mContext2) {
        this.mContext = mContext2;
        try {
            this.mPath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/appLogInfo/";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    @Override
    public Integer doInBackground(Void... voids) {
        return Integer.valueOf(LogOp.getInstance(this.mContext).writeLogcatToFile(this.mPath + "logcat.txt", 10000));
    }

    /* access modifiers changed from: protected */
    @Override
    public void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        LogSaveManager_Util.upLoadLogTxt(this.mContext);
    }
}