package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.iflytek.cbg.common.utils.ListUtils;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.WifiListAdapter;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.WifiBean;
import com.toycloud.launcher.myinterface.WifiConnectInterface;
import com.toycloud.launcher.util.CollectionUtils;
import com.toycloud.launcher.util.CustomToast;
import com.toycloud.launcher.util.WifiSupport;
import com.toycloud.launcher.view.MySwitchButton;
import com.toycloud.launcher.view.WifiLinkDialog;
import framework.hz.salmon.view.ProgressDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Launcher_WiFiConnect_ViewHolder extends BaseHolder implements View.OnClickListener, WifiLinkDialog.ToSureConnectNet {
    private static final String TAG = "WiFiConnViewHolder";
    private WifiListAdapter adapter;
    /* access modifiers changed from: private */
    public int connectType = 0;
    /* access modifiers changed from: private */
    public Handler handler;
    boolean isCheck = true;
    /* access modifiers changed from: private */
    public Context mContext;
    private boolean mIsDestried = false;
    /* access modifiers changed from: private */
    public ProgressDialog mProgressDialog;
    /* access modifiers changed from: private */
    public MySwitchButton mb_wifiswitch;
    MyTimeTask myTimeTask;
    boolean openWifi = false;
    List<WifiBean> realWifiList;
    RecyclerView recy_list_wifi;
    private Timer timer;
    TextView tv_first_to_next;
    TextView tv_nonet;
    TextView tv_wifistatus;
    /* access modifiers changed from: private */
    public WifiBean wifiBean;
    WifiConnectInterface wifiConnectInterface;

    public Launcher_WiFiConnect_ViewHolder(Context context, WifiConnectInterface wifiConnectInterface2, Handler handler2) {
        super(context);
        this.mContext = context;
        this.wifiConnectInterface = wifiConnectInterface2;
        this.handler = handler2;
    }

    public void refresh() {
        if (this.mContext != null && !this.mIsDestried) {
            if (ListUtils.isEmpty((List) this.realWifiList) || this.isCheck) {
                ((WifiManager) this.mContext.getSystemService("wifi")).startScan();
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.activity_wifi_connect;
    }

    private void cancelTimer() {
        if (this.timer != null) {
            try {
                this.timer.cancel();
            } catch (Exception e) {
            }
            this.timer = null;
        }
        if (this.myTimeTask != null) {
            try {
                this.myTimeTask.cancel();
            } catch (Exception e2) {
            }
            this.myTimeTask = null;
        }
    }

    public void onDestroy() {
        this.mIsDestried = true;
        cancelTimer();
        super.onDestroy();
    }

    private void setTimeTask() {
        cancelTimer();
        this.timer = new Timer();
        this.myTimeTask = new MyTimeTask();
    }

    /* access modifiers changed from: protected */
    public void initView(final Context context, View rootView) {
        this.openWifi = WifiSupport.isOpenWifi(context);
        this.mProgressDialog = new ProgressDialog((Activity) context);
        this.mb_wifiswitch = (MySwitchButton) rootView.findViewById(R.id.mb_wifiswitch);
        this.recy_list_wifi = (RecyclerView) rootView.findViewById(R.id.recy_list_wifi);
        this.tv_wifistatus = (TextView) rootView.findViewById(R.id.tv_wifistatus);
        this.tv_first_to_next = (TextView) rootView.findViewById(R.id.tv_first_next);
        this.tv_nonet = (TextView) rootView.findViewById(R.id.tv_nonet);
        this.adapter = new WifiListAdapter(context, this.realWifiList);
        this.recy_list_wifi.setLayoutManager(new LinearLayoutManager(context));
        this.recy_list_wifi.setAdapter(this.adapter);
        this.tv_first_to_next.setOnClickListener(this);
        if (this.openWifi) {
            setTimeTask();
            this.timer.schedule(this.myTimeTask, 1000, 10000);
            this.mb_wifiswitch.setOpenStatus();
            sortScaResult(context);
            this.tv_wifistatus.setText("WLAN开启");
        } else {
            this.mb_wifiswitch.setCloseStatus();
            this.tv_wifistatus.setText("WLAN关闭");
        }
        this.mb_wifiswitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Launcher_WiFiConnect_ViewHolder.this.mb_wifiswitch.isOpen) {
                    Launcher_WiFiConnect_ViewHolder.this.showProgressDialog();
                    WifiSupport.closeWifi(Launcher_WiFiConnect_ViewHolder.this.mContext);
                    return;
                }
                Launcher_WiFiConnect_ViewHolder.this.showProgressDialog();
                WifiSupport.openWifi(Launcher_WiFiConnect_ViewHolder.this.mContext);
            }
        });
        this.adapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
            public void onItemClick(View view, int postion, Object o) {
                WifiBean unused = Launcher_WiFiConnect_ViewHolder.this.wifiBean = Launcher_WiFiConnect_ViewHolder.this.realWifiList.get(postion);
                if (Launcher_WiFiConnect_ViewHolder.this.wifiBean == null) {
                    Toast.makeText(context, "网络连接异常，稍候重试", 1).show();
                } else if (!Launcher_WiFiConnect_ViewHolder.this.wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) && !Launcher_WiFiConnect_ViewHolder.this.wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT) && !Launcher_WiFiConnect_ViewHolder.this.wifiBean.getState().equals(AppContants.WIFI_STATE_FAIl)) {
                } else {
                    if (WifiSupport.getWifiCipher(Launcher_WiFiConnect_ViewHolder.this.realWifiList.get(postion).getCapabilities()) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS) {
                        WifiConfiguration tempConfig = WifiSupport.isExsits(Launcher_WiFiConnect_ViewHolder.this.wifiBean.getWifiName(), context);
                        if (tempConfig == null) {
                            WifiSupport.addNetWork(WifiSupport.createWifiConfig(Launcher_WiFiConnect_ViewHolder.this.wifiBean.getWifiName(), (String) null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS), context);
                        } else {
                            WifiSupport.addNetWork(tempConfig, context);
                        }
                    } else if (Launcher_WiFiConnect_ViewHolder.this.realWifiList.get(postion).isConnect()) {
                        CustomToast.showToast(context, "wifi已连接，不需要重新连接了");
                    } else {
                        WifiConfiguration tempConfig2 = WifiSupport.isExsits(Launcher_WiFiConnect_ViewHolder.this.wifiBean.getWifiName(), context);
                        if (tempConfig2 == null) {
                            Launcher_WiFiConnect_ViewHolder.this.noConfigurationWifi(postion, context);
                            return;
                        }
                        Log.e("isConnect-->", WifiSupport.addNetWork(tempConfig2, context) + ":");
                    }
                }
            }
        });
    }

    public void autoLinkWifi(String name) {
        if (!ListUtils.isEmpty((List) this.realWifiList)) {
            for (int i = 0; i < this.realWifiList.size(); i++) {
                WifiBean wifiBean2 = this.realWifiList.get(i);
                WifiConfiguration tempConfig = WifiSupport.isExsits(wifiBean2.getWifiName(), this.mContext);
                if (TextUtils.equals(name, wifiBean2.getWifiName())) {
                    return;
                }
                if (tempConfig != null) {
                    this.connectType = 2;
                    wifiBean2.setState(AppContants.WIFI_STATE_ON_CONNECTING);
                    WifiSupport.addNetWork(tempConfig, this.mContext);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void noConfigurationWifi(int position, Context context) {
        WifiBean wifiBean2 = this.realWifiList.get(position);
        new WifiLinkDialog(context, R.style.dialog_download, wifiBean2.getWifiName(), wifiBean2.getCapabilities(), this).show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_first_next:
                onDestroy();
                this.wifiConnectInterface.toMain();
                return;
            default:
                return;
        }
    }

    public void wifiClose() {
        dismissProgressDialog();
        this.mb_wifiswitch.setCloseStatus();
        this.tv_wifistatus.setText("WLAN关闭");
        onDestroy();
        this.tv_nonet.setVisibility(0);
        this.recy_list_wifi.setVisibility(8);
    }

    public void wifiOpen() {
        dismissProgressDialog();
        this.tv_wifistatus.setText("WLAN开启");
        this.mb_wifiswitch.setOpenStatus();
        setTimeTask();
        this.timer.schedule(this.myTimeTask, 1000, 10000);
        this.tv_nonet.setVisibility(8);
        this.recy_list_wifi.setVisibility(0);
    }

    public void wifilinkerror() {
        this.wifiBean = this.realWifiList.get(0);
        if (this.wifiBean != null) {
            String wifiName = this.wifiBean.getWifiName();
            this.wifiBean.setState(AppContants.WIFI_STATE_FAIl);
            if (!TextUtils.isEmpty(wifiName)) {
                WifiConfiguration tempConfig = WifiSupport.isExsits(this.wifiBean.getWifiName(), this.mContext);
                WifiManager wifimanager = (WifiManager) this.mContext.getSystemService("wifi");
                if (tempConfig != null) {
                    wifimanager.removeNetwork(tempConfig.networkId);
                    wifimanager.saveConfiguration();
                }
            }
            CustomToast.showToast(this.mContext, "密码输入错误");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(Launcher_WiFiConnect_ViewHolder.this.mContext);
                    int unused = Launcher_WiFiConnect_ViewHolder.this.connectType = 3;
                    Launcher_WiFiConnect_ViewHolder.this.wifiListSet(connectedWifiInfo.getSSID(), Launcher_WiFiConnect_ViewHolder.this.connectType);
                    Launcher_WiFiConnect_ViewHolder.this.autoLinkWifi(connectedWifiInfo.getSSID());
                }
            }, 1000);
        }
    }

    public void wifiConnect() {
        WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(this.mContext);
        this.connectType = 1;
        wifiListSet(connectedWifiInfo.getSSID(), this.connectType);
    }

    public void wifiConnecting() {
        WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(this.mContext);
        this.connectType = 2;
        wifiListSet(connectedWifiInfo.getSSID(), this.connectType);
    }

    public void wifiChange() {
        wifiListChange();
    }

    public void wifiListChange() {
        sortScaResult(this.mContext);
        WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(this.mContext);
        if (connectedWifiInfo != null) {
            wifiListSet(connectedWifiInfo.getSSID(), this.connectType);
        }
    }

    public void setProgressDialog() {
        showProgressDialog();
    }

    /* access modifiers changed from: protected */
    public void showProgressDialog() {
        if (this.mProgressDialog != null && !this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        ((Activity) this.mContext).runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (Launcher_WiFiConnect_ViewHolder.this.mProgressDialog != null && Launcher_WiFiConnect_ViewHolder.this.mProgressDialog.isShowing()) {
                        Launcher_WiFiConnect_ViewHolder.this.mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void sortScaResult(Context mContext2) {
        List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(mContext2));
        if (!CollectionUtils.isNullOrEmpty(scanResults)) {
            if (this.realWifiList == null) {
                this.realWifiList = new ArrayList();
            } else {
                this.realWifiList.clear();
            }
            WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(mContext2);
            for (int i = 0; i < scanResults.size(); i++) {
                WifiBean wifiBean2 = new WifiBean();
                wifiBean2.setWifiName(scanResults.get(i).SSID);
                if (connectedWifiInfo == null || !connectedWifiInfo.getSSID().equals("\"" + scanResults.get(i).SSID + "\"")) {
                    if (this.isCheck) {
                        WifiConfiguration tempConfig = WifiSupport.isExsits(wifiBean2.getWifiName(), mContext2);
                        if (tempConfig == null) {
                            wifiBean2.setState(AppContants.WIFI_STATE_UNCONNECT);
                        } else if (WifiSupport.addNetWork(tempConfig, mContext2)) {
                            this.connectType = 1;
                            wifiBean2.setState(AppContants.WIFI_STATE_CONNECT);
                        } else {
                            this.connectType = 0;
                            wifiBean2.setState(AppContants.WIFI_STATE_UNCONNECT);
                        }
                    } else {
                        wifiBean2.setState(AppContants.WIFI_STATE_UNCONNECT);
                    }
                } else if (WifiSupport.isExsits(scanResults.get(i).SSID, mContext2) != null) {
                    this.isCheck = false;
                    this.connectType = 1;
                    wifiBean2.setState(AppContants.WIFI_STATE_CONNECT);
                } else {
                    this.connectType = 0;
                    wifiBean2.setState(AppContants.WIFI_STATE_UNCONNECT);
                }
                wifiBean2.setCapabilities(scanResults.get(i).capabilities);
                wifiBean2.setLevel(WifiSupport.getLevel(scanResults.get(i).level) + "");
                this.realWifiList.add(wifiBean2);
            }
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
            this.adapter.setResultList(this.realWifiList);
            Log.e("realWifiList", this.realWifiList.size() + "《----》");
        }
    }

    public void wifiListSet(String wifiName, int type) {
        int index = -1;
        WifiBean wifiInfo = new WifiBean();
        if (!CollectionUtils.isNullOrEmpty(this.realWifiList)) {
            Collections.sort(this.realWifiList);
            for (int i = 0; i < this.realWifiList.size(); i++) {
                WifiBean wifiBean2 = this.realWifiList.get(i);
                if (("\"" + wifiBean2.getWifiName() + "\"").equals(wifiName)) {
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
                    } else {
                        wifiInfo.setState(AppContants.WIFI_STATE_UNCONNECT);
                    }
                }
            }
            if (index != -1) {
                this.realWifiList.remove(index);
                this.realWifiList.add(0, wifiInfo);
                if (this.adapter != null) {
                    this.adapter.setResultList(this.realWifiList);
                    Log.e("adapter-->1", "realWifiList:" + this.realWifiList.toString());
                }
            }
        }
    }

    public void connect(String wifiName, int type) {
        if (!CollectionUtils.isNullOrEmpty(this.realWifiList)) {
            int i = 0;
            while (i < this.realWifiList.size()) {
                WifiBean wifiBean2 = this.realWifiList.get(i);
                if (wifiBean2.getWifiName().equals(wifiName)) {
                    this.realWifiList.remove(i);
                    wifiBean2.setState(AppContants.WIFI_STATE_ON_CONNECTING);
                    this.realWifiList.add(0, wifiBean2);
                } else {
                    wifiBean2.setState(AppContants.WIFI_STATE_UNCONNECT);
                }
                if (this.adapter != null) {
                    this.adapter.setResultList(this.realWifiList);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void handleWifiEvent(Context context, Intent intent) {
        Logger.d(TAG, "WIFI-----:" + intent.getAction());
        if (intent.getAction().equals("android.net.wifi.supplicant.STATE_CHANGE")) {
            int linkWifiResult = intent.getIntExtra("supplicantError", 123);
            if (linkWifiResult == 1) {
                wifilinkerror();
            }
            Log.e("linkWifiResult-->", linkWifiResult + ":");
        }
        if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction())) {
            switch (intent.getIntExtra("wifi_state", 0)) {
                case 0:
                    Log.d(TAG, "正在关闭");
                    setProgressDialog();
                    return;
                case 1:
                    Log.d(TAG, "已经关闭");
                    wifiClose();
                    return;
                case 2:
                    Log.d(TAG, "正在打开");
                    setProgressDialog();
                    return;
                case 3:
                    Log.d(TAG, "已经打开");
                    wifiOpen();
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
                wifiConnect();
            } else if (NetworkInfo.State.CONNECTING == info.getState()) {
                Log.d(TAG, "wifi正在连接");
                wifiConnecting();
            }
        } else if ("android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
            Log.d(TAG, "网络列表变化了");
            wifiChange();
        }
    }

    class MyTimeTask extends TimerTask {
        MyTimeTask() {
        }

        public void run() {
            Launcher_WiFiConnect_ViewHolder.this.handler.post(new Runnable() {
                public void run() {
                    Launcher_WiFiConnect_ViewHolder.this.refresh();
                }
            });
        }
    }
}