package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.iflytek.utils.LogAgentHelper;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.WifiListAdapter;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.WifiBean;
import com.toycloud.launcher.ui.MainActivity;
import com.toycloud.launcher.ui.usercenter.EditUserInfoActivity;
import com.toycloud.launcher.util.CollectionUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.WifiSupport;
import com.toycloud.launcher.view.MyClickButton;
import framework.hz.salmon.base.BaseFragmentActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WifiConnectActivity extends BaseFragmentActivity {
    private static final String[] NEEDED_PERMISSIONS = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    private static final int PERMISSION_REQUEST_CODE = 0;
    /* access modifiers changed from: private */
    public String TAG = "WifiConnectActivity";
    private WifiListAdapter adapter;
    /* access modifiers changed from: private */
    public int connectType = 0;
    private boolean mHasPermission;
    @BindView(2131689849)
    MyClickButton mb_wifiswitch;
    List<WifiBean> realWifiList = new ArrayList();
    @BindView(2131689850)
    RecyclerView recy_list_wifi;
    @BindView(2131689852)
    TextView tv_first_to_next;
    @BindView(2131689851)
    TextView tv_nonet;
    @BindView(2131689848)
    TextView tv_wifistatus;
    WifiBean wifiBean;
    private WifiBroadcastReceiver wifiReceiver;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect);
        ButterKnife.bind((Activity) this);
        initView();
        this.mHasPermission = checkPermission();
        if (!this.mHasPermission) {
            requestPermission();
        } else if (WifiSupport.isOpenWifi(this)) {
            initRecycler();
        }
    }

    private boolean checkPermission() {
        for (String permission : NEEDED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != 0) {
                return false;
            }
        }
        return true;
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
            window.addFlags(134217728);
            window.getDecorView().setSystemUiVisibility(9216);
        }
        this.adapter = new WifiListAdapter(this, this.realWifiList);
        this.recy_list_wifi.setLayoutManager(new LinearLayoutManager(this));
        this.recy_list_wifi.setAdapter(this.adapter);
        this.mb_wifiswitch.setOnMbClickListener(new MyClickButton.OnMClickListener() {
            public void onClick(boolean isRight) {
                if (isRight) {
                    WifiSupport.openWifi(WifiConnectActivity.this);
                    WifiConnectActivity.this.mb_wifiswitch.setEnabled(false);
                    WifiConnectActivity.this.tv_wifistatus.setText("WLAN开启");
                    return;
                }
                WifiSupport.closeWifi(WifiConnectActivity.this);
                WifiConnectActivity.this.mb_wifiswitch.setEnabled(false);
                WifiConnectActivity.this.tv_wifistatus.setText("WLAN关闭");
            }
        });
        this.adapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
            public void onItemClick(View view, int postion, Object o) {
                WifiConnectActivity.this.wifiBean = WifiConnectActivity.this.realWifiList.get(postion);
                if (WifiConnectActivity.this.wifiBean == null) {
                    Toast.makeText(WifiConnectActivity.this, "网络连接异常，稍候重试", 1).show();
                } else if (!WifiConnectActivity.this.wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) && !WifiConnectActivity.this.wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT) && !WifiConnectActivity.this.wifiBean.getState().equals(AppContants.WIFI_STATE_FAIl)) {
                } else {
                    if (WifiSupport.getWifiCipher(WifiConnectActivity.this.realWifiList.get(postion).getCapabilities()) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS) {
                        WifiConfiguration tempConfig = WifiSupport.isExsits(WifiConnectActivity.this.wifiBean.getWifiName(), WifiConnectActivity.this);
                        if (tempConfig == null) {
                            WifiSupport.addNetWork(WifiSupport.createWifiConfig(WifiConnectActivity.this.wifiBean.getWifiName(), (String) null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS), WifiConnectActivity.this);
                        } else {
                            WifiSupport.addNetWork(tempConfig, WifiConnectActivity.this);
                        }
                    } else if (WifiConnectActivity.this.realWifiList.get(postion).isConnect()) {
                        Toast.makeText(WifiConnectActivity.this, "wifi已连接，不需要重新连接了", 0).show();
                    } else {
                        WifiConfiguration tempConfig2 = WifiSupport.isExsits(WifiConnectActivity.this.wifiBean.getWifiName(), WifiConnectActivity.this);
                        if (tempConfig2 == null) {
                            WifiConnectActivity.this.noConfigurationWifi(postion);
                        } else {
                            WifiSupport.addNetWork(tempConfig2, WifiConnectActivity.this);
                        }
                    }
                }
            }
        });
    }

    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({2131689852})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_first_next:
                SharepreferenceUtil.getSharepferenceInstance(this).setAlreadyStart(false);
                checkLogin();
                return;
            default:
                return;
        }
    }

    private void checkLogin() {
        User user = SharepreferenceUtil.getSharepferenceInstance(this).getUserInfo();
        if (user == null || TextUtils.isEmpty(user.getUsername())) {
            GlobalVariable.setLogin(false, (User) null);
            startActivity(new Intent(this, MainActivity.class));
        } else if (user == null || !TextUtils.isEmpty(user.getRealname())) {
            User userl = SharepreferenceUtil.getSharepferenceInstance(this).getUserInfo();
            GlobalVariable.LOGIN_USER = userl;
            GlobalVariable.setTOKEN(userl.getToken(), true);
            Logger.e("token-->", GlobalVariable.getTOKEN() + "::");
            GlobalVariable.setLogin(true, GlobalVariable.LOGIN_USER);
            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "请先完善个人信息", 0).show();
            startActivityForResult(new Intent(getApplicationContext(), EditUserInfoActivity.class), 101);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, 0);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllPermission = true;
        if (requestCode == 0) {
            int length = grantResults.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (grantResults[i] != 0) {
                    hasAllPermission = false;
                    break;
                } else {
                    i++;
                }
            }
            if (hasAllPermission) {
                this.mHasPermission = true;
                if (!WifiSupport.isOpenWifi(this) || !this.mHasPermission) {
                    Toast.makeText(this, "WIFI处于关闭状态请打开WIFI", 0).show();
                } else {
                    initRecycler();
                }
            } else {
                this.mHasPermission = false;
                Toast.makeText(this, "您未同意设置wifi权限，可以通过系统设置页面连接wifi", 0).show();
            }
        }
    }

    public class WifiBroadcastReceiver extends BroadcastReceiver {
        public WifiBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.net.wifi.supplicant.STATE_CHANGE") && intent.getIntExtra("supplicantError", 123) == 1 && WifiConnectActivity.this.wifiBean != null) {
                WifiConnectActivity.this.wifiBean.setState(AppContants.WIFI_STATE_FAIl);
                if (!(WifiConnectActivity.this.wifiBean == null || WifiConnectActivity.this.wifiBean.getWifiName() == null)) {
                    WifiConfiguration tempConfig = WifiSupport.isExsits(WifiConnectActivity.this.wifiBean.getWifiName(), WifiConnectActivity.this);
                    WifiManager wifimanager = (WifiManager) context.getSystemService("wifi");
                    if (tempConfig != null) {
                        wifimanager.removeNetwork(tempConfig.networkId);
                        wifimanager.saveConfiguration();
                    }
                }
                WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(WifiConnectActivity.this);
                int unused = WifiConnectActivity.this.connectType = 3;
                WifiConnectActivity.this.wifiListSet(connectedWifiInfo.getSSID(), WifiConnectActivity.this.connectType);
            }
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction())) {
                switch (intent.getIntExtra("wifi_state", 0)) {
                    case 0:
                        Logger.d(WifiConnectActivity.this.TAG, "正在关闭");
                        return;
                    case 1:
                        Logger.d(WifiConnectActivity.this.TAG, "已经关闭");
                        WifiConnectActivity.this.mb_wifiswitch.setEnabled(true);
                        WifiConnectActivity.this.tv_nonet.setVisibility(0);
                        WifiConnectActivity.this.recy_list_wifi.setVisibility(8);
                        return;
                    case 2:
                        Logger.d(WifiConnectActivity.this.TAG, "正在打开");
                        return;
                    case 3:
                        Logger.d(WifiConnectActivity.this.TAG, "已经打开");
                        WifiConnectActivity.this.mb_wifiswitch.setEnabled(true);
                        WifiConnectActivity.this.tv_nonet.setVisibility(8);
                        WifiConnectActivity.this.recy_list_wifi.setVisibility(0);
                        WifiConnectActivity.this.sortScaResult();
                        return;
                    case 4:
                        Logger.d(WifiConnectActivity.this.TAG, "未知状态");
                        return;
                    default:
                        return;
                }
            } else if ("android.net.wifi.STATE_CHANGE".equals(intent.getAction())) {
                NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                Logger.d(WifiConnectActivity.this.TAG, "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {
                    Logger.d(WifiConnectActivity.this.TAG, "wifi没连接上");
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {
                    Logger.d(WifiConnectActivity.this.TAG, "wifi连接上了");
                    WifiInfo connectedWifiInfo2 = WifiSupport.getConnectedWifiInfo(WifiConnectActivity.this);
                    Toast.makeText(WifiConnectActivity.this, "wifi连接上了", 0).show();
                    int unused2 = WifiConnectActivity.this.connectType = 1;
                    WifiConnectActivity.this.wifiListSet(connectedWifiInfo2.getSSID(), WifiConnectActivity.this.connectType);
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {
                    Logger.d(WifiConnectActivity.this.TAG, "wifi正在连接");
                    WifiInfo connectedWifiInfo3 = WifiSupport.getConnectedWifiInfo(WifiConnectActivity.this);
                    int unused3 = WifiConnectActivity.this.connectType = 2;
                    WifiConnectActivity.this.wifiListSet(connectedWifiInfo3.getSSID(), WifiConnectActivity.this.connectType);
                }
            } else if ("android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
                Logger.d(WifiConnectActivity.this.TAG, "网络列表变化了");
                WifiConnectActivity.this.wifiListChange();
            }
        }
    }

    public void wifiListChange() {
        sortScaResult();
        WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(this);
        if (connectedWifiInfo != null) {
            wifiListSet(connectedWifiInfo.getSSID(), this.connectType);
        }
    }

    public void sortScaResult() {
        List<ScanResult> scanResults = WifiSupport.getWifiScanResult(this);
        if (!CollectionUtils.isNullOrEmpty(scanResults)) {
            this.realWifiList.clear();
            for (int i = 0; i < scanResults.size(); i++) {
                WifiBean wifiBean2 = new WifiBean();
                wifiBean2.setWifiName(scanResults.get(i).SSID);
                wifiBean2.setState(AppContants.WIFI_STATE_UNCONNECT);
                wifiBean2.setCapabilities(scanResults.get(i).capabilities);
                wifiBean2.setLevel(WifiSupport.getLevel(scanResults.get(i).level) + "");
                this.realWifiList.add(wifiBean2);
            }
            if (this.adapter != null) {
                this.adapter.setResultList(this.realWifiList);
                Logger.e("realWifiList", this.realWifiList.size() + "《----》");
            }
        }
    }

    public void wifiListSet(String wifiName, int type) {
        int index = -1;
        WifiBean wifiInfo = new WifiBean();
        if (!CollectionUtils.isNullOrEmpty(this.realWifiList)) {
            Collections.sort(this.realWifiList);
            for (int i = 0; i < this.realWifiList.size(); i++) {
                WifiBean wifiBean2 = this.realWifiList.get(i);
                if (index == -1 && ("\"" + wifiBean2.getWifiName() + "\"").equals(wifiName)) {
                    index = i;
                    wifiInfo.setLevel(wifiBean2.getLevel());
                    wifiInfo.setWifiName(wifiBean2.getWifiName());
                    wifiInfo.setCapabilities(wifiBean2.getCapabilities());
                    if (type == 1) {
                        wifiInfo.setState(AppContants.WIFI_STATE_CONNECT);
                    } else if (type == 2) {
                        wifiInfo.setState(AppContants.WIFI_STATE_ON_CONNECTING);
                    } else if (type == 3) {
                        wifiInfo.setState(AppContants.WIFI_STATE_FAIl);
                    }
                }
            }
            if (index != -1) {
                this.realWifiList.remove(index);
                this.realWifiList.add(0, wifiInfo);
                if (this.adapter != null) {
                    this.adapter.notifyDataSetChanged();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.SCAN_RESULTS");
        filter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        registerReceiver(this.wifiReceiver, filter);
        LogAgentHelper.onActive();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        unregisterReceiver(this.wifiReceiver);
    }

    private void initRecycler() {
        if (!WifiSupport.isOpenWifi(this) || !this.mHasPermission) {
            Toast.makeText(this, "WIFI处于关闭状态或权限获取失败", 0).show();
        } else {
            sortScaResult();
        }
    }

    /* access modifiers changed from: private */
    public void noConfigurationWifi(int position) {
    }
}