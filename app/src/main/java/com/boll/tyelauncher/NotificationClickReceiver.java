package com.boll.tyelauncher;

package com.toycloud.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.google.gson.GsonBuilder;
import com.iflytek.cbg.pushsdk.bean.PushNotification;
import com.iflytek.cbg.pushsdk.bean.RemotePushMessage;
import com.iflytek.cbg.pushsdk.report.MessageReportEntity;
import com.iflytek.cbg.pushsdk.report.MessageReportManager;
import com.iflytek.cbg.pushsdk.util.MessageState;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.webview.NotificationBarWebviewActivity;

public class NotificationClickReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationClickReceiver";

    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "接收到点击通知栏的广播");
        String messageJson = intent.getStringExtra("message");
        if (!TextUtils.isEmpty(messageJson)) {
            try {
                RemotePushMessage message = (RemotePushMessage) new GsonBuilder().create().fromJson(messageJson, RemotePushMessage.class);
                PushNotification notification = message.getNotification();
                MessageReportEntity entity = new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.CLICKED, 0);
                Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.CLICKED + " discardCode: " + 0);
                MessageReportManager.reportMessageEvent(context, entity);
                if (notification != null) {
                    Intent webIntent = new Intent(context, NotificationBarWebviewActivity.class);
                    webIntent.putExtra("key_webview_url", notification.getUrl());
                    webIntent.addFlags(268435456);
                    context.startActivity(webIntent);
                }
            } catch (Exception e) {
                Logger.d(TAG, "接收到点击通知栏的广播反序列化消息失败");
            }
        }
    }
}