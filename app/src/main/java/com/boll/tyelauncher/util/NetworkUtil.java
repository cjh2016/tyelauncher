package com.boll.tyelauncher.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null || (info = connectivity.getActiveNetworkInfo()) == null || !info.isConnected() || info.getState() != NetworkInfo.State.CONNECTED) {
            return false;
        }
        return true;
    }
}
