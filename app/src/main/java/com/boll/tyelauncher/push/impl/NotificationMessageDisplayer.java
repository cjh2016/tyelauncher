package com.boll.tyelauncher.push.impl;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.boll.tyelauncher.cbg.pushsdk.bean.PushNotification;
import com.boll.tyelauncher.cbg.pushsdk.bean.RemotePushMessage;
import com.boll.tyelauncher.cbg.pushsdk.report.MessageReportEntity;
import com.boll.tyelauncher.push.IMessageDisplayer;
import com.boll.tyelauncher.push.MessageDisplayer;
import com.facebook.common.util.UriUtil;
import com.orhanobut.logger.Logger;

public class NotificationMessageDisplayer implements IMessageDisplayer {
    private static final String TAG = "NotificationMessageDisplayer";

    @Override
    public void displayMessage(Context context, RemotePushMessage message, String msgStr) {
        Logger.d(TAG, "接收到消息类型为1的消息");
        PushNotification notificationBean = message.getNotification();
        Logger.d(TAG, "notificationBean: " + notificationBean);
        boolean ring = false;
        boolean vibrate = false;
        if (notificationBean != null) {
            String title = notificationBean.getTitle();
            String content = notificationBean.getContent();
            String url = notificationBean.getUrl();
            if (notificationBean.getRing() == 1) {
                ring = true;
            }
            if (notificationBean.getVibrate() == 1) {
                vibrate = true;
            }
            try {
                String scheme = Uri.parse(url).getScheme();
                Logger.d(TAG, "scheme: " + scheme);
                if (!TextUtils.isEmpty(scheme)) {
                    if (TextUtils.equals(scheme, "http") || TextUtils.equals(scheme, UriUtil.HTTPS_SCHEME)) {
                        Intent clickIntent = new Intent(context, NotificationClickReceiver.class);
                        clickIntent.putExtra("message", msgStr);
                        Intent deleteIntent = new Intent(context, DeleteNotificationReceiver.class);
                        deleteIntent.putExtra("message", msgStr);
                        int id = (int) (System.currentTimeMillis() / 1000);
                        Notification notification = MessageDisplayer.getInstance().getNotification(context, title, content, ring, vibrate, PendingIntent.getBroadcast(context, id, clickIntent, 134217728), PendingIntent.getBroadcast(context, id, deleteIntent, 268435456));
                        if (notification != null) {
                            MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISPLAYED, 0));
                            Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISPLAYED + " discardCode: " + 0);
                            MessageDisplayer.getInstance().showNotification(context, id, notification);
                            return;
                        }
                        MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISCARD, MessageDiscardCode.FILTER_FAILURE_OTHER));
                        Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISCARD + " discardCode: " + MessageDiscardCode.FILTER_FAILURE_DISCARD);
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}