package com.boll.tyelauncher.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.toycloud.launcher.util.WifiSupport;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.isAvailable()) {
        }
        if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction())) {
            switch (intent.getIntExtra("wifi_state", 0)) {
                case 0:
                    Log.d(TAG, "正在关闭");
                    return;
                case 1:
                    Log.d(TAG, "已经关闭");
                    Toast.makeText(context, "网络连接错误，请检查网络设置后重试!", 1).show();
                    return;
                case 2:
                    Log.d(TAG, "正在打开");
                    return;
                case 3:
                    Log.d(TAG, "已经打开");
                    return;
                case 4:
                    Log.d(TAG, "未知状态");
                    return;
                default:
                    return;
            }
        } else if ("android.net.wifi.STATE_CHANGE".equals(intent.getAction())) {
            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            Log.d(TAG, "--NetworkInfo--" + info.toString());
            if (NetworkInfo.State.DISCONNECTED == info.getState()) {
                return;
            }
            if (NetworkInfo.State.CONNECTED == info.getState()) {
                Log.d(TAG, "wifi连接上了");
                WifiSupport.getConnectedWifiInfo(context);
            } else if (NetworkInfo.State.CONNECTING == info.getState()) {
                Log.d(TAG, "wifi正在连接");
            }
        } else if ("android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
            Log.d(TAG, "网络列表变化了");
        }
    }
}
