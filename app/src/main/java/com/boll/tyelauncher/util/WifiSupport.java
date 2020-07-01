package com.boll.tyelauncher.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.alibaba.android.arouter.utils.Consts;
import java.util.ArrayList;
import java.util.List;

public class WifiSupport {
    private static final String TAG = "WifiSupport";

    public enum WifiCipherType {
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID
    }

    public static List<ScanResult> getWifiScanResult(Context context) {
        if (context == null) {
        }
        return ((WifiManager) context.getSystemService("wifi")).getScanResults();
    }

    public static boolean isWifiEnable(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).isWifiEnabled();
    }

    public static WifiInfo getConnectedWifiInfo(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
    }

    public static List getConfigurations(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).getConfiguredNetworks();
    }

    public static WifiConfiguration createWifiConfig(String SSID, String password, WifiCipherType type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(0);
        }
        if (type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(0);
            config.allowedGroupCiphers.set(3);
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(0);
            config.allowedGroupCiphers.set(1);
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }
        if (type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(0);
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(3);
            config.allowedKeyManagement.set(1);
            config.allowedPairwiseCiphers.set(1);
            config.allowedPairwiseCiphers.set(2);
            config.status = 2;
        }
        return config;
    }

    public static boolean addNetWork(WifiConfiguration config, Context context) {
        boolean result;
        WifiManager wifimanager = (WifiManager) context.getSystemService("wifi");
        WifiInfo wifiinfo = wifimanager.getConnectionInfo();
        if (wifiinfo != null) {
            wifimanager.disableNetwork(wifiinfo.getNetworkId());
        }
        if (config.networkId >= 0) {
            result = wifimanager.enableNetwork(config.networkId, true);
            wifimanager.updateNetwork(config);
        } else {
            int i = wifimanager.addNetwork(config);
            result = false;
            if (i > 0) {
                wifimanager.saveConfiguration();
                return wifimanager.enableNetwork(i, true);
            }
        }
        return result;
    }

    public static WifiCipherType getWifiCipher(String s) {
        if (s.isEmpty()) {
            return WifiCipherType.WIFICIPHER_INVALID;
        }
        if (s.contains("WEP")) {
            return WifiCipherType.WIFICIPHER_WEP;
        }
        if (s.contains("WPA") || s.contains("WPA2") || s.contains("WPS")) {
            return WifiCipherType.WIFICIPHER_WPA;
        }
        return WifiCipherType.WIFICIPHER_NOPASS;
    }

    public static WifiConfiguration isExsits(String SSID, Context context) {
        for (WifiConfiguration existingConfig : ((WifiManager) context.getSystemService("wifi")).getConfiguredNetworks()) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    @SuppressLint({"WrongConstant"})
    public static void openWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService("wifi");
        if (!wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(true);
        } else if (wifimanager.getWifiState() == 2) {
            Toast.makeText(context, "亲，Wifi正在开启，不要着急", 0).show();
        } else {
            Toast.makeText(context, "亲，Wifi已经开启了", 0).show();
        }
    }

    @SuppressLint({"WrongConstant"})
    public static void closeWifi(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        } else if (mWifiManager.getWifiState() != 1 && mWifiManager.getWifiState() != 0) {
            Toast.makeText(context, "请重新关闭", 0).show();
        }
    }

    public static boolean isOpenWifi(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).isWifiEnabled();
    }

    public static String getStringId(int idString) {
        StringBuffer sb = new StringBuffer();
        sb.append(((idString >> 0) & 255) + Consts.DOT);
        sb.append(((idString >> 8) & 255) + Consts.DOT);
        sb.append(((idString >> 16) & 255) + Consts.DOT);
        sb.append((idString >> 24) & 255);
        return sb.toString();
    }

    public static String getCapabilitiesString(String capabilities) {
        if (capabilities.contains("WEP")) {
            return "WEP";
        }
        if (capabilities.contains("WPA") || capabilities.contains("WPA2") || capabilities.contains("WPS")) {
            return "WPA/WPA2";
        }
        return "OPEN";
    }

    public static boolean getIsWifiEnabled(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).isWifiEnabled();
    }

    public static List<ScanResult> noSameName(List<ScanResult> oldSr) {
        List<ScanResult> newSr = new ArrayList<>();
        for (ScanResult result : oldSr) {
            if (!TextUtils.isEmpty(result.SSID) && !containName(newSr, result.SSID)) {
                newSr.add(result);
            }
        }
        return newSr;
    }

    public static boolean containName(List<ScanResult> sr, String name) {
        for (ScanResult result : sr) {
            if (!TextUtils.isEmpty(result.SSID) && result.SSID.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static int getLevel(int level) {
        if (Math.abs(level) < 50) {
            return 1;
        }
        if (Math.abs(level) < 75) {
            return 2;
        }
        if (Math.abs(level) < 90) {
            return 3;
        }
        return 4;
    }
}