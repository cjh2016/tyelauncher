package com.boll.tyelauncher.push;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.boll.tyelauncher.R;

public class MyNotificationManager {
    private static final String PRIMARY_CHANNEL_ID = "study";
    private static final String TAG = "MyNotificationManager";
    private static MyNotificationManager mInstance;

    public static MyNotificationManager getInstance() {
        if (mInstance == null) {
            synchronized (MyNotificationManager.class) {
                if (mInstance == null) {
                    mInstance = new MyNotificationManager();
                }
            }
        }
        return mInstance;
    }

    public void showNotification(Context context, int id, Notification notification) {
        NotificationManager nm = (NotificationManager) context.getSystemService("notification");
        if (nm != null) {
            nm.notify(id, notification);
        }
    }

    public Notification getNotification(Context context, String title, String content, PendingIntent pi) {
        NotificationManager nm = (NotificationManager) context.getSystemService("notification");
        if (nm == null) {
            return null;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL_ID, "消息通知", 4);
            channel.setDescription("学习机的学习提醒通知");
            channel.setShowBadge(true);
            channel.setSound(RingtoneManager.getDefaultUri(2), new AudioAttributes.Builder().setUsage(6).build());
            channel.enableVibration(true);
            nm.createNotificationChannel(channel);
        }
        builder.setSmallIcon(R.mipmap.icon_small_alpha).setTicker(title).setWhen(System.currentTimeMillis()).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_learning_daily)).setContentTitle(title).setContentText(content).setContentIntent(pi).setShowWhen(true).setAutoCancel(true);
        return builder.build();
    }
}