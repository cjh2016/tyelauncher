package com.boll.tyelauncher.biz.evalhint;

package com.toycloud.launcher.biz.evalhint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.iflytek.easytrans.core.async.thread.TaskRunner;
import com.iflytek.easytrans.watchcore.utils.GlobalConfigs;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.application.LauncherApplication;
import com.toycloud.launcher.biz.evalhint.activity.EvalHintActivity;
import com.toycloud.launcher.biz.globalconfig.CityInfoUpgradeManager;
import com.toycloud.launcher.util.Logging;
import com.toycloud.launcher.util.SharepreferenceUtil;

public class EvalHintHelper {
    private static final String ACTION = "com.iflytek.launcher.ACTION_SHOULD_SHOW_EVAL_HINT";
    private static final String KEY_EVAL_HINT_CONFIG = "eval_hint_config";
    private static final String KEY_EVAL_HINT_SHOWN = "eval_hint_shown";
    private static final String TAG = "EvalHintHelper";
    private static volatile EvalHintHelper sInstance;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(final Context context, Intent intent) {
            Logging.d(EvalHintHelper.TAG, "onReceive() received eval hint receiver, try show eval hint dialog");
            final User user = SharepreferenceUtil.getSharepferenceInstance(context).getUserInfo();
            if (user == null) {
                Logging.d(EvalHintHelper.TAG, "onReceive() user not login, return");
                return;
            }
            final String key = "eval_hint_shown_" + user.getUserid();
            if (!GlobalConfigs.getBoolean(context, key, false, true)) {
                Logging.d(EvalHintHelper.TAG, "onReceive() eval hint dialog is not shown to current user, show it");
                if (TextUtils.isEmpty(user.getAreacode())) {
                    Logging.d(EvalHintHelper.TAG, "onReceive() area_code is empty, return");
                } else if (TextUtils.isEmpty(user.getGradecode())) {
                    Logging.d(EvalHintHelper.TAG, "onReceive() grade_code is empty, return");
                } else {
                    TaskRunner.getBackHandler().post(new Runnable() {
                        public void run() {
                            CityInfoUpgradeManager.getInstance().loadData(context, new CityInfoUpgradeManager.OnInitProvinceDataListener() {
                                public void onLoadComplete(boolean success) {
                                    String evalHintConfig = GlobalConfigs.getString(context, EvalHintHelper.KEY_EVAL_HINT_CONFIG, "[{\"area_code\":\"440100\",\"area_name\":\"广州市\",\"grade_region\":\"7-12\"},{\"area_code\":\"445200\",\"area_name\":\"揭阳市\",\"grade_region\":\"10-12\"},{\"area_code\":\"442000\",\"area_name\":\"中山市\",\"grade_region\":\"7-12\"},{\"area_code\":\"441800\",\"area_name\":\"清远市\",\"grade_region\":\"10-12\"},{\"area_code\":\"441500\",\"area_name\":\"汕尾市\",\"grade_region\":\"10-12\"},{\"area_code\":\"440200\",\"area_name\":\"韶关市\",\"grade_region\":\"10-12\"},{\"area_code\":\"445300\",\"area_name\":\"云浮市\",\"grade_region\":\"10-12\"},{\"area_code\":\"441600\",\"area_name\":\"河源市\",\"grade_region\":\"10-12\"},{\"area_code\":\"440500\",\"area_name\":\"汕头市\",\"grade_region\":\"10-12\"},{\"area_code\":\"445100\",\"area_name\":\"潮州市\",\"grade_region\":\"10-12\"},{\"area_code\":\"440300\",\"area_name\":\"深圳市\",\"grade_region\":\"7-12\"},{\"area_code\":\"440600\",\"area_name\":\"佛山市\",\"grade_region\":\"10-12\"},{\"area_code\":\"440400\",\"area_name\":\"珠海市\",\"grade_region\":\"10-12\"},{\"area_code\":\"441300\",\"area_name\":\"惠州市\",\"grade_region\":\"10-12\"},{\"area_code\":\"441900\",\"area_name\":\"东莞市\",\"grade_region\":\"7-12\"},{\"area_code\":\"440700\",\"area_name\":\"江门市\",\"grade_region\":\"7-12\"},{\"area_code\":\"440900\",\"area_name\":\"茂名市\",\"grade_region\":\"10-12\"},{\"area_code\":\"440800\",\"area_name\":\"湛江市\",\"grade_region\":\"10-12\"},{\"area_code\":\"441200\",\"area_name\":\"肇庆市\",\"grade_region\":\"7-12\"},{\"area_code\":\"441700\",\"area_name\":\"阳江市\",\"grade_region\":\"10-12\"},{\"area_code\":\"441400\",\"area_name\":\"梅州市\",\"grade_region\":\"10-12\"},{\"area_code\":\"330300\",\"area_name\":\"温州市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320100\",\"area_name\":\"南京市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320500\",\"area_name\":\"苏州市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320400\",\"area_name\":\"常州市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320200\",\"area_name\":\"无锡市\",\"grade_region\":\"7-9\"},{\"area_code\":\"321100\",\"area_name\":\"镇江市\",\"grade_region\":\"7-9\"},{\"area_code\":\"321200\",\"area_name\":\"泰州市\",\"grade_region\":\"7-9\"},{\"area_code\":\"321000\",\"area_name\":\"扬州市\",\"grade_region\":\"7-9\"},{\"area_code\":\"321300\",\"area_name\":\"宿迁市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320800\",\"area_name\":\"淮安市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320700\",\"area_name\":\"连云港市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320600\",\"area_name\":\"南通市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320900\",\"area_name\":\"盐城市\",\"grade_region\":\"7-9\"},{\"area_code\":\"320300\",\"area_name\":\"徐州市\",\"grade_region\":\"7-9\"},{\"area_code\":\"310000\",\"area_name\":\"上海市\",\"grade_region\":\"10-12\"},{\"area_code\":\"110000\",\"area_name\":\"北京市\",\"grade_region\":\"7-9\"},{\"area_code\":\"370200\",\"area_name\":\"青岛市\",\"grade_region\":\"7-9\"},{\"area_code\":\"370300\",\"area_name\":\"淄博市\",\"grade_region\":\"7-9\"},{\"area_code\":\"371000\",\"area_name\":\"威海市\",\"grade_region\":\"7-9\"},{\"area_code\":\"370700\",\"area_name\":\"潍坊市\",\"grade_region\":\"7-9\"},{\"area_code\":\"371500\",\"area_name\":\"聊城市\",\"grade_region\":\"7-9\"},{\"area_code\":\"371100\",\"area_name\":\"日照市\",\"grade_region\":\"7-9\"},{\"area_code\":\"370600\",\"area_name\":\"烟台市\",\"grade_region\":\"7-9\"},{\"area_code\":\"210100\",\"area_name\":\"沈阳市\",\"grade_region\":\"7-9\"},{\"area_code\":\"430100\",\"area_name\":\"长沙市\",\"grade_region\":\"7-9\"},{\"area_code\":\"430700\",\"area_name\":\"常德市\",\"grade_region\":\"7-9\"},{\"area_code\":\"430400\",\"area_name\":\"衡阳市\",\"grade_region\":\"7-9\"},{\"area_code\":\"420500\",\"area_name\":\"宜昌市\",\"grade_region\":\"7-9\"},{\"area_code\":\"420600\",\"area_name\":\"襄阳市\",\"grade_region\":\"7-9\"},{\"area_code\":\"500000\",\"area_name\":\"重庆市\",\"grade_region\":\"7-9\"},{\"area_code\":\"510700\",\"area_name\":\"绵阳市\",\"grade_region\":\"7-9\"},{\"area_code\":\"620100\",\"area_name\":\"兰州市\",\"grade_region\":\"7-9\"}]", false);
                                    String regionName = CityInfoUpgradeManager.getInstance().getRegionName(user.getAreacode());
                                    GlobalConfigs.putBoolean(context, key, true);
                                    EvalHintActivity.start(context, user, regionName, evalHintConfig);
                                }
                            });
                        }
                    });
                }
            } else {
                Logging.d(EvalHintHelper.TAG, "onReceive() eval hint dialog is shown to current user, return");
            }
        }
    };

    private EvalHintHelper() {
    }

    public static EvalHintHelper getInstance() {
        synchronized (EvalHintHelper.class) {
            if (sInstance == null) {
                synchronized (EvalHintHelper.class) {
                    if (sInstance == null) {
                        sInstance = new EvalHintHelper();
                    }
                }
            }
        }
        return sInstance;
    }

    public void init() {
        Logging.d(TAG, "init() register eval hint receiver");
        try {
            LauncherApplication.getContext().registerReceiver(this.mBroadcastReceiver, new IntentFilter(ACTION));
        } catch (Exception e) {
        }
    }
}
