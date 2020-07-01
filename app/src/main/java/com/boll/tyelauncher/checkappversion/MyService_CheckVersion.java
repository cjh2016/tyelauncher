package com.boll.tyelauncher.checkappversion;

package com.toycloud.launcher.checkappversion;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

public class MyService_CheckVersion extends Service {
    AlarmManager manager;
    PendingIntent pi;

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.manager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
        long triggerAtTime = SystemClock.elapsedRealtime() + ((long) 600000);
        Intent i = new Intent(this, MyReceiver_CheckVersion.class);
        i.setAction("com.iflytek.action.CHECK_VERSIOL");
        this.pi = PendingIntent.getBroadcast(this, 0, i, 0);
        this.manager.set(2, triggerAtTime, this.pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.manager != null) {
            this.manager.cancel(this.pi);
        }
    }
}