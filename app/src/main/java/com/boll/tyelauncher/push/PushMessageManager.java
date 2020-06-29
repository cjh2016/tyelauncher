package com.boll.tyelauncher.push;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.boll.tyelauncher.cbg.pushsdk.bean.RemotePushMessage;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

public class PushMessageManager {
    private static final String PUSH_ID = "5c32f6ef";
    public static final String PUSH_MESSAGE_EXTRA_KEY = "message";
    private static final String RECEIVER_PERMISSION = "com.iflytek.cbg.ai.study.push.message.service.RECEIVER_PERMISSION";
    private static final String SEND_BROADCAST_ACTION = "com.iflytek.cbg.ai.study.push.message.SEND.BROADCAST.ACTION";
    private static final String TAG = "PushMessageManager";
    private static PushMessageManager mPushMessageManager = null;
    private String mLastReceivedMsgId;

    public static PushMessageManager getInstance() {
        if (mPushMessageManager == null) {
            synchronized (PushMessageManager.class) {
                if (mPushMessageManager == null) {
                    mPushMessageManager = new PushMessageManager();
                }
            }
        }
        return mPushMessageManager;
    }

    public void initPushSDK(Context context) {
    }

    public void register(Context context) {
    }

    public void unregister(Context context) {
    }

    /* access modifiers changed from: package-private */
    public void sendMessage(Context context, RemotePushMessage message) {
        String type = message.getType();
        if (!TextUtils.isEmpty(message.getPkgname())) {
            if (!TextUtils.isEmpty(type)) {
                processMessageTypeNonNull(context, message);
            } else {
                processMessagePkgNameNonNull(context, message);
            }
        } else if (!TextUtils.isEmpty(type)) {
            processMessageTypeNonNull(context, message);
        }
    }

    private void processMessageTypeNonNull(Context context, RemotePushMessage message) {
        String type = message.getType();
        if (type.equals("app_control") || type.equals("time_control")) {
            Intent intent = new Intent(SEND_BROADCAST_ACTION);
            intent.putExtra("message", message);
            intent.setPackage(BuildConfig.APPLICATION_ID);
            context.sendBroadcast(intent, RECEIVER_PERMISSION);
            Logger.d(TAG, "发送广播: " + message.toString());
        } else if (type.equals("app_download_control")) {
            Logger.e(TAG, "下载管控的广播已发送");
            Logger.d(TAG, "发送广播: " + message.toString());
            context.sendBroadcast(new Intent("com.iflytek.appshop.authoritychanged"));
        } else if (type.equals("answer_control")) {
            Logger.e(TAG, "答案推送的广播已发送");
            Logger.d(TAG, "发送广播: " + message.toString());
            Intent intent2 = new Intent("com.iflytek.searchbyphoto.lockstatus");
            intent2.putExtra("message", new Gson().toJson((Object) message));
            context.sendBroadcast(intent2);
        } else if (type.equals("app_shouhu_control")) {
            Logger.e(TAG, "守护模式的广播已发送");
            Logger.d(TAG, "发送广播: " + message.getData());
            Intent intent3 = new Intent("com.iflytek.appshop.bindstatuschanged");
            intent3.setPackage("com.iflytek.appshop");
            intent3.putExtra("bindstatus", message.getData());
            context.sendBroadcast(intent3);
        } else if (type.equals("app_apply_control")) {
            Logger.e(TAG, "审核结果的广播已发送");
            Logger.d(TAG, "发送广播: " + message.getData());
            Intent intent4 = new Intent("com.iflytek.appshop.auditchanged");
            intent4.setPackage("com.iflytek.appshop");
            intent4.putExtra("auditresult", message.getData());
            context.sendBroadcast(intent4);
        }
    }

    private void processMessagePkgNameNonNull(Context context, RemotePushMessage message) {
        String pkgName = message.getPkgname();
        if (pkgName != null) {
            Intent intent = new Intent(SEND_BROADCAST_ACTION);
            intent.putExtra("message", message);
            intent.setPackage(pkgName);
            context.sendBroadcast(intent, RECEIVER_PERMISSION);
        }
    }

    public static void stop(Context context) {
    }

    public String getLastReceivedMsgId() {
        return this.mLastReceivedMsgId;
    }

    public void updateLastReceivedMsgId(String id) {
        this.mLastReceivedMsgId = id;
    }
}
