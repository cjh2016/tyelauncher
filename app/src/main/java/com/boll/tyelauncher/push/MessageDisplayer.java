package com.boll.tyelauncher.push;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.boll.tyelauncher.R;

public class MessageDisplayer {
    private static final String CHANNEL_ID = "com.iflytek.launcher.notification.displayer";
    private static MessageDisplayer mInstance;

    public static MessageDisplayer getInstance() {
        if (mInstance == null) {
            synchronized (MessageDisplayer.class) {
                if (mInstance == null) {
                    mInstance = new MessageDisplayer();
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

    public Notification getNotification(Context context, String title, String content, boolean ring, boolean vibrate, PendingIntent pi, PendingIntent delete) {
        NotificationManager nm = (NotificationManager) context.getSystemService("notification");
        if (nm == null) {
            return null;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "消息通知", 3);
            channel.setDescription("学习机消息");
            channel.setShowBadge(true);
            AudioAttributes build = new AudioAttributes.Builder().setUsage(6).build();
            nm.createNotificationChannel(channel);
        }
        builder.setSmallIcon(R.mipmap.icon_system_notification_small).setTicker(title).setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(content).setContentIntent(pi).setDeleteIntent(delete).setShowWhen(true).setAutoCancel(true);
        return builder.build();
    }
}