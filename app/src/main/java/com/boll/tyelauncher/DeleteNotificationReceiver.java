package com.boll.tyelauncher;

package com.toycloud.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.google.gson.GsonBuilder;
import com.iflytek.cbg.pushsdk.bean.RemotePushMessage;
import com.iflytek.cbg.pushsdk.report.MessageReportEntity;
import com.iflytek.cbg.pushsdk.report.MessageReportManager;
import com.iflytek.cbg.pushsdk.util.MessageState;
import com.orhanobut.logger.Logger;

public class DeleteNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "DeleteNotificationReceiver";

    public void onReceive(Context context, Intent intent) {
        Logger.d("NotificationClickReceiver", "接收到删除通知栏的广播");
        String messageJson = intent.getStringExtra("message");
        if (!TextUtils.isEmpty(messageJson)) {
            try {
                RemotePushMessage message = (RemotePushMessage) new GsonBuilder().create().fromJson(messageJson, RemotePushMessage.class);
                MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DELETED, 0));
                Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DELETED + " discardCode: " + 0);
            } catch (Exception e) {
                Logger.d(TAG, "接收到删除通知栏的广播反序列化消息失败");
            }
        }
    }
}
