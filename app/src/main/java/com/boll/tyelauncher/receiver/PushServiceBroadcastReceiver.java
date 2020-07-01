package com.boll.tyelauncher.receiver;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PointerIconCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.iflytek.easytrans.watchcore.deviceinfo.DeviceIdentifierUtils;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.BuildConfig;
import com.toycloud.launcher.api.model.Constants;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.ForbiddenAPP;
import com.toycloud.launcher.model.ForbiddenTime;
import com.toycloud.launcher.model.K12PushMessage;
import com.toycloud.launcher.model.launcher.RemoteController;
import com.toycloud.launcher.push.MyNotificationManager;
import com.toycloud.launcher.push.PushMessageManager;
import com.toycloud.launcher.push.RemotePushMessage;
import com.toycloud.launcher.ui.usercenter.StudyDailyReportMsgManager;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.webview.WebViewActivity;

public class PushServiceBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_APP_FORBBIDEN_STATE_CHANGED = "action.app.forbbiden.CHANGED";
    public static final String ACTION_APP_FORBBIDEN_TIME_CHANGED = "action.app.forbbiden.time.CHANGED";
    public static final String KEY_FORBBIDEN_APP = "key.forbbiden.app";
    public static final String KEY_FORBBIDEN_TIME = "key.forbbiden.time";
    private static final String TAG = "PushServiceBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
        RemotePushMessage message = (RemotePushMessage) new GsonBuilder().create().fromJson(intent.getStringExtra("message"), RemotePushMessage.class);
        if (message == null) {
            LogUtils.d(TAG, "onReceive: 推送消息为空");
            return;
        }
        LogUtils.d(TAG, "onReceive: message：" + message.toString());
        Gson gson = new GsonBuilder().create();
        if (TextUtils.isEmpty(message.getType())) {
            K12PushMessage k12PushMessage = (K12PushMessage) gson.fromJson(message.getData(), K12PushMessage.class);
            if (k12PushMessage == null) {
                return;
            }
            if (TextUtils.equals(PushMessageManager.getInstance().getLastReceivedMsgId(), k12PushMessage.msgId)) {
                LogUtils.d(TAG, "onReceive: 已经处理了相同id的消息，不再重复处理");
                return;
            }
            PushMessageManager.getInstance().updateLastReceivedMsgId(k12PushMessage.msgId);
            if (TextUtils.equals(k12PushMessage.type, "1") && !TextUtils.isEmpty(k12PushMessage.actionUri)) {
                Uri uri = Uri.parse(k12PushMessage.actionUri);
                String scheme = uri.getScheme();
                if (!TextUtils.equals(scheme, "iflyk12")) {
                    LogUtils.d(TAG, "getNotificationByK12Msg: 不支持的跳转协议名称，不做任何响应：scheme:" + scheme);
                } else if (TextUtils.equals(uri.getAuthority(), Constants.K12_PUSH_AUTHORITY_STUDY_DAILY_REPORT)) {
                    onReceiverStudyReport(context, k12PushMessage, uri);
                }
            }
        } else if (message.getType().equals("app_control")) {
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
        }
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
