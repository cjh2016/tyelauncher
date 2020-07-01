package com.boll.tyelauncher.checkappversion;

package com.toycloud.launcher.checkappversion;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.toycloud.launcher.util.LogSaveManager_Util;
import com.toycloud.launcher.util.MyCountDownTimer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyService_OnScreen extends Service {
    private MyCountDownTimer timer;

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.timer = new MyCountDownTimer(86400000, 15000) {
            public void onTick(long millisUntilFinished) {
                Log.e("MyCountDownTimer-->", "开始计时：" + new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis())));
                if (!MyService_OnScreen.this.isServiceRunning(MyService_OnScreen.this, "com.iflytek.appshop.service.DemonService")) {
                    try {
                        Intent intent_service = new Intent();
                        intent_service.setComponent(new ComponentName("com.iflytek.appshop", "com.iflytek.appshop.service.DemonService"));
                        MyService_OnScreen.this.startService(intent_service);
                    } catch (Exception e) {
                        LogSaveManager_Util.saveLog(MyService_OnScreen.this, "启动保活的应用市场的服务失败");
                        e.printStackTrace();
                    }
                }
            }

            public void onFinish(long millisUntilFinished) {
                Log.e("MyCountDownTimer-->", "计时器异常关闭");
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    public boolean isServiceRunning(Context context, String ServiceName) {
        if ("".equals(ServiceName) || ServiceName == null) {
            return false;
        }
        try {
            ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList) ((ActivityManager) context.getSystemService("activity")).getRunningServices(50);
            for (int i = 0; i < runningService.size(); i++) {
                if (runningService.get(i).service.getClassName().toString().contains(ServiceName)) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }
}
