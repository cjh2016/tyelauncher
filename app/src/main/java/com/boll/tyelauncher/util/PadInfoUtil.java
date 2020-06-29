package com.boll.tyelauncher.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.android.arouter.utils.Consts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class PadInfoUtil {
    private static String SN = null;
    private Context mContext;
    private SharepreferenceUtil sharepferenceInstance;

    public PadInfoUtil(Context mContext2) {
        this.mContext = mContext2;
        this.sharepferenceInstance = SharepreferenceUtil.getSharepferenceInstance(mContext2);
    }

    public String getSnCode() {
        if (SN != null) {
            return SN;
        }
        SN = doGetSN();
        return SN;
    }

    private String doGetSN() {
        String sNcode = this.sharepferenceInstance.getSNcode();
        if (TextUtils.isEmpty(sNcode)) {
            sNcode = Settings.System.getString(this.mContext.getContentResolver(), "settings_system_ssn");
            this.sharepferenceInstance.setSNcode(sNcode);
        }
        LogSaveManager_Util.saveLog(this.mContext, "SN号：" + sNcode);
        return sNcode;
    }

    public String getSystemVersionCode() {
        return getProp("ro.build.display.id");
    }

    public static String getProp(String key) throws InvocationTargetException, IllegalAccessException {
        Method getIntMethod = null;
        if (0 == 0) {
            try {
                getIntMethod = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
            } catch (Exception e) {
                Log.e("padinfo", "Platform error: " + e.toString());
                return "";
            }
        }
        return (String) getIntMethod.invoke((Object) null, new Object[]{key});
    }

    public String getIPAddress() {
        NetworkInfo info = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == 0) {
                try {
                    Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                    while (en.hasMoreElements()) {
                        Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses();
                        while (true) {
                            if (enumIpAddr.hasMoreElements()) {
                                InetAddress inetAddress = enumIpAddr.nextElement();
                                if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                                    return inetAddress.getHostAddress();
                                }
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == 1) {
                return intIP2StringIP(((WifiManager) this.mContext.getSystemService("wifi")).getConnectionInfo().getIpAddress());
            }
        }
        return null;
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 255) + Consts.DOT + ((ip >> 8) & 255) + Consts.DOT + ((ip >> 16) & 255) + Consts.DOT + ((ip >> 24) & 255);
    }

    public static String getPropString(String key, String def) throws InvocationTargetException, IllegalAccessException {
        Method getBooleanMethod = null;
        if (0 == 0) {
            try {
                getBooleanMethod = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class});
            } catch (Exception e) {
                Log.e("getPropString", "Platform error: " + e.toString());
                return def;
            }
        }
        return (String) getBooleanMethod.invoke((Object) null, new Object[]{key, def});
    }

    public static String getMacFromHardware() {
        try {
            for (NetworkInterface nif : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }
                    StringBuilder res1 = new StringBuilder();
                    int length = macBytes.length;
                    for (int i = 0; i < length; i++) {
                        res1.append(String.format("%02x:", new Object[]{Byte.valueOf(macBytes[i])}));
                    }
                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取mac地址失败";
    }
}