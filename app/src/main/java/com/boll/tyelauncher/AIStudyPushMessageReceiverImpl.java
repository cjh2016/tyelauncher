package com.boll.tyelauncher;

package com.toycloud.launcher;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Keep;
import android.support.v4.view.PointerIconCompat;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.cbg.aistudy.biz.user.UserAccInfo;
import com.iflytek.cbg.pushsdk.PushMessageReceiver;
import com.iflytek.cbg.pushsdk.bean.PushNotification;
import com.iflytek.cbg.pushsdk.bean.RemotePushMessage;
import com.iflytek.cbg.pushsdk.report.MessageReportEntity;
import com.iflytek.cbg.pushsdk.report.MessageReportManager;
import com.iflytek.cbg.pushsdk.util.MessageDiscardCode;
import com.iflytek.cbg.pushsdk.util.MessageState;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.iflytek.easytrans.watchcore.deviceinfo.DeviceIdentifierUtils;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.api.model.Constants;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.ForbiddenAPP;
import com.toycloud.launcher.model.ForbiddenTime;
import com.toycloud.launcher.model.K12PushMessage;
import com.toycloud.launcher.model.launcher.RemoteController;
import com.toycloud.launcher.push.MessageDisplayer;
import com.toycloud.launcher.push.MyNotificationManager;
import com.toycloud.launcher.push.PushMessageManager;
import com.toycloud.launcher.ui.usercenter.StudyDailyReportMsgManager;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.GsonUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.webview.WebViewActivity;

@Keep
public class AIStudyPushMessageReceiverImpl extends PushMessageReceiver {
    public static final String ACTION_APP_FORBBIDEN_STATE_CHANGED = "action.app.forbbiden.CHANGED";
    public static final String ACTION_APP_FORBBIDEN_TIME_CHANGED = "action.app.forbbiden.time.CHANGED";
    public static final String KEY_FORBBIDEN_APP = "key.forbbiden.app";
    public static final String KEY_FORBBIDEN_TIME = "key.forbbiden.time";
    private static final String TAG = "AIStudyPushMessageReceiverImpl";
    private Context mContext;

    /* access modifiers changed from: protected */
    public UserAccInfo getUserInfo() {
        if (this.mContext != null) {
            return UserAccInfo.readFromContentProvider(this.mContext);
        }
        return null;
    }

    private void handleCompactMessage(Context context, RemotePushMessage message, String msgBody) {
        Gson gson = new GsonBuilder().create();
        if (message.getType().equals("app_control")) {
            ForbiddenAPP forbiddenAPP = (ForbiddenAPP) gson.fromJson(message.getData(), ForbiddenAPP.class);
            if (forbiddenAPP == null) {
                Log.w(TAG, "onMessage | app_control | forbiddenAPP is null -> ignore it");
                return;
            }
            Log.i(TAG, "onMessage | app_control | calling handleForbbidenApp | pkg/name/status = " + forbiddenAPP.getAppCode() + "/" + forbiddenAPP.getAppName() + "/" + forbiddenAPP.getStatus());
            RemoteController.getInstance(context).handleForbbidenApp(forbiddenAPP);
        } else if (message.getType().equals("time_control")) {
            ForbiddenTime forbiddenTime = (ForbiddenTime) gson.fromJson(message.getData(), ForbiddenTime.class);
            if (forbiddenTime == null) {
                Log.w(TAG, "onMessage | time_control | forbiddenTime is null -> ignore it");
            } else if (!forbiddenTime.isValid()) {
                Log.w(TAG, "onMessage | time_control | forbiddenTime is not valid -> ignore it");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(forbiddenTime.getType()).append(" : ").append(forbiddenTime.getStartTime()).append(" -> ").append(forbiddenTime.getEndTime());
                Logger.i(TAG, "onMessage | time_control | calling handleForbbidenTime | " + sb.toString());
                RemoteController.getInstance(context).handleForbbidenTime(forbiddenTime);
            }
        } else {
            Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISCARD + " discardCode: " + MessageDiscardCode.FILTER_FAILURE_DISCARD);
            MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISCARD, MessageDiscardCode.FILTER_FAILURE_DISCARD));
        }
    }

    private void handleStudyMessage(Context context, RemotePushMessage message, String msgBody) {
        K12PushMessage k12PushMessage = (K12PushMessage) GsonUtils.changeJsonToBeanSafe(message.getData(), K12PushMessage.class);
        if (k12PushMessage == null) {
            Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISCARD + " discardCode: " + MessageDiscardCode.FILTER_FAILURE_DISCARD);
            MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISCARD, MessageDiscardCode.FILTER_FAILURE_DISCARD));
        } else if (TextUtils.equals(PushMessageManager.getInstance().getLastReceivedMsgId(), k12PushMessage.msgId)) {
            LogUtils.d(TAG, "onReceive: 已经处理了相同id的消息，不再重复处理");
        } else {
            PushMessageManager.getInstance().updateLastReceivedMsgId(k12PushMessage.msgId);
            if (!TextUtils.equals(k12PushMessage.type, "1")) {
                Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISCARD + " discardCode: " + MessageDiscardCode.FILTER_FAILURE_DISCARD);
                MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISCARD, MessageDiscardCode.FILTER_FAILURE_DISCARD));
            } else if (!TextUtils.isEmpty(k12PushMessage.actionUri)) {
                Uri uri = Uri.parse(k12PushMessage.actionUri);
                String scheme = uri.getScheme();
                if (!TextUtils.equals(scheme, "iflyk12")) {
                    LogUtils.d(TAG, "getNotificationByK12Msg: 不支持的跳转协议名称，不做任何响应：scheme:" + scheme);
                } else if (TextUtils.equals(uri.getAuthority(), Constants.K12_PUSH_AUTHORITY_STUDY_DAILY_REPORT)) {
                    onReceiverStudyReport(context, k12PushMessage, uri);
                }
            }
        }
    }

    private void handleNotificationMessage(Context context, RemotePushMessage message, String msgBody) {
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
                if (TextUtils.equals(scheme, "http") || TextUtils.equals(scheme, UriUtil.HTTPS_SCHEME)) {
                    Intent clickIntent = new Intent(this.mContext, NotificationClickReceiver.class);
                    clickIntent.putExtra("message", msgBody);
                    Intent deleteIntent = new Intent(this.mContext, DeleteNotificationReceiver.class);
                    deleteIntent.putExtra("message", msgBody);
                    int id = (int) System.currentTimeMillis();
                    Notification notification = MessageDisplayer.getInstance().getNotification(context, title, content, ring, vibrate, PendingIntent.getBroadcast(context, id, clickIntent, 134217728), PendingIntent.getBroadcast(context, id, deleteIntent, 268435456));
                    if (notification != null) {
                        MessageReportEntity entity = new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISPLAYED, 0);
                        Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISPLAYED + " discardCode: " + 0);
                        MessageReportManager.reportMessageEvent(context, entity);
                        MessageDisplayer.getInstance().showNotification(context, id, notification);
                        return;
                    }
                    Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISCARD + " discardCode: " + MessageDiscardCode.FILTER_FAILURE_DISCARD);
                    MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISCARD, MessageDiscardCode.FILTER_FAILURE_DISCARD));
                    return;
                }
                Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISCARD + " discardCode: " + MessageDiscardCode.FILTER_FAILURE_DISCARD);
                MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISCARD, MessageDiscardCode.FILTER_FAILURE_DISCARD));
            } catch (Exception e) {
                Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISCARD + " discardCode: " + MessageDiscardCode.FILTER_FAILURE_DISCARD);
                MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISCARD, MessageDiscardCode.FILTER_FAILURE_DISCARD));
            }
        }
    }

    public void onMessage(Context context, RemotePushMessage message, String msgBody) {
        if (message == null) {
            LogUtils.d(TAG, "onReceive: 推送消息为空");
            return;
        }
        LogUtils.d(TAG, "onReceive: message：" + message.toString());
        Gson create = new GsonBuilder().create();
        if (!TextUtils.isEmpty(message.getType())) {
            handleCompactMessage(context, message, msgBody);
        } else if (message.getMsgType() == 0) {
            handleStudyMessage(context, message, msgBody);
        } else if (message.getMsgType() == 1) {
            handleNotificationMessage(context, message, msgBody);
        } else {
            Logger.d(TAG, "Launcher 调用SDK上报接口: getMsgId" + message.getMsgId() + " eventCode: " + message.getEventCode() + " status: " + MessageState.DISCARD + " discardCode: " + MessageDiscardCode.FILTER_FAILURE_DISCARD);
            MessageReportManager.reportMessageEvent(context, new MessageReportEntity(message.getMsgId(), message.getEventCode(), MessageState.DISCARD, MessageDiscardCode.FILTER_FAILURE_DISCARD));
        }
    }

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        super.onReceive(context, intent);
    }

    private void onReceiverStudyReport(Context context, K12PushMessage k12PushMessage, Uri uri) {
        User user = SharepreferenceUtil.getSharepferenceInstance(context).getUserInfo();
        if (user == null || !GlobalVariable.isLogin()) {
            LogUtils.d(TAG, "onReceiverStudyReport: 当前为未登录状态，不处理学习报告的推送");
        } else if (GradeUtil.isPrimarySchoolGrade(user.getGradecode())) {
            LogUtils.d(TAG, "onReceiverStudyReport: 当前用户为小学用户，不处理学习报告的推送");
        } else {
            String url = uri.getQueryParameter(Constants.K12_PUSH_QUERY_PARAMS_URL);
            if (TextUtils.isEmpty(url)) {
                LogUtils.d(TAG, "onReceiverStudyReport: 没有地址参数，使用默认地址参数");
                url = BuildConfig.URL_STUDY_DAILY;
            } else {
                LogUtils.d(TAG, "onReceiverStudyReport: 推送的h5地址：" + url);
            }
            String url2 = (url + "?sn=") + DeviceIdentifierUtils.getSn(context);
            if (!StudyDailyReportMsgManager.getInstance().hasShowStudyReportToday()) {
                context.sendBroadcast(new Intent(Constants.ACTION_STUDY_REPORT_UPDATE));
                Intent webIntent = new Intent(context, WebViewActivity.class);
                webIntent.putExtra("key_webview_url", url2);
                webIntent.putExtra(StudyDailyReportMsgManager.EXTRA_INTENT_TO_STUDY_DAILY_REPORT, true);
                webIntent.addFlags(268435456);
                Notification notification = MyNotificationManager.getInstance().getNotification(context, k12PushMessage.title, k12PushMessage.content, PendingIntent.getActivity(context, PointerIconCompat.TYPE_CONTEXT_MENU, webIntent, 134217728));
                if (notification != null) {
                    MyNotificationManager.getInstance().showNotification(context, 100, notification);
                    return;
                }
                return;
            }
            LogUtils.d(TAG, "onReceiverStudyReport: 今天的报告已经看过，不再展示推送的响应");
        }
    }
}