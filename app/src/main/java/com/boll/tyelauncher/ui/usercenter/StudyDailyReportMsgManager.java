package com.boll.tyelauncher.ui.usercenter;

package com.toycloud.launcher.ui.usercenter;

import android.content.Context;
import com.iflytek.easytrans.core.utils.common.TimeUtils;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.toycloud.launcher.api.response.StudyReportResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.util.DeviceUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StudyDailyReportMsgManager {
    public static final String EXTRA_INTENT_TO_STUDY_DAILY_REPORT = "intent_to_study_daily_report";
    public static final String SP_NAME_LAST_RECEIVER_MSG_TIME = "last_receiver_msg_time";
    public static final String SP_NAME_LAST_REQUEST_MSG_TIME = "last_request_msg_time";
    public static final String SP_NAME_LAST_SHOW_REPORT_TIME = "last_show_report_time";
    private static final String TAG = "StudyDailyReportMsg";
    private static final long TIMESTAMP_ONE_DAY = 86400000;
    private static StudyDailyReportMsgManager mInstance;
    private long mLastReceiveMsgTime = 0;
    private long mLastRequestMsgTime = 0;
    private long mLastShowMsgTime = 0;

    public static StudyDailyReportMsgManager getInstance() {
        if (mInstance == null) {
            synchronized (StudyDailyReportMsgManager.class) {
                if (mInstance == null) {
                    mInstance = new StudyDailyReportMsgManager();
                }
            }
        }
        return mInstance;
    }

    public void restore(Context context) {
        this.mLastShowMsgTime = SharepreferenceUtil.getSharepferenceInstance(context).getLong(SP_NAME_LAST_SHOW_REPORT_TIME);
        this.mLastReceiveMsgTime = SharepreferenceUtil.getSharepferenceInstance(context).getLong(SP_NAME_LAST_RECEIVER_MSG_TIME);
        this.mLastRequestMsgTime = SharepreferenceUtil.getSharepferenceInstance(context).getLong(SP_NAME_LAST_REQUEST_MSG_TIME);
    }

    public void updateStudyReportShowTime(Context context, long timestamp) {
        this.mLastShowMsgTime = timestamp;
        SharepreferenceUtil.getSharepferenceInstance(context).putLong(SP_NAME_LAST_SHOW_REPORT_TIME, timestamp);
    }

    public void updateRequestMsgTime(Context context, long timestamp) {
        this.mLastRequestMsgTime = timestamp;
        SharepreferenceUtil.getSharepferenceInstance(context).putLong(SP_NAME_LAST_REQUEST_MSG_TIME, timestamp);
    }

    public void updateReceiveMsgTime(Context context, long timestamp) {
        this.mLastReceiveMsgTime = timestamp;
        SharepreferenceUtil.getSharepferenceInstance(context).putLong(SP_NAME_LAST_RECEIVER_MSG_TIME, timestamp);
    }

    public boolean hasShowStudyReportToday() {
        return TimeUtils.isOneDay(this.mLastShowMsgTime);
    }

    public boolean hasNewStudyReportMsg() {
        LogUtils.d(TAG, "hasNewStudyReportMsg: 上次展示页面的时间:" + this.mLastShowMsgTime + " 上次收到学习报告推送的时间:" + this.mLastReceiveMsgTime + " 上次请求到学习报告更新的时间:" + this.mLastRequestMsgTime);
        return !TimeUtils.isOneDay(this.mLastShowMsgTime) && (TimeUtils.isOneDay(this.mLastReceiveMsgTime) || TimeUtils.isOneDay(this.mLastRequestMsgTime));
    }

    public void getStudyReportInfo(Context context, Observer<StudyReportResponse> observer) {
        String sn = DeviceUtil.getSnCode(context);
        LogUtils.d(TAG, "getStudyReportInfo: sn:" + sn);
        LauncherHttpHelper.getHttpService().getStudyReportInfo(sn, System.currentTimeMillis() - TIMESTAMP_ONE_DAY).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
