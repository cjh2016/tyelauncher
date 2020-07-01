package com.boll.tyelauncher.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

@SuppressLint({"MissingPermission"})
public class DeviceUtil {
    public static String getIMEI(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMac(Context context) {
        String wifiMac = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        if (!TextUtils.isEmpty(wifiMac)) {
        }
        return wifiMac;
    }

    public static String getSimId(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
    }

    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), "android_id");
    }

    public static String getSerialNumber() {
        return Build.SERIAL;
    }

    public static String getSnCode(Context context) {
        return Settings.System.getString(context.getContentResolver(), "settings_system_ssn");
    }
}
