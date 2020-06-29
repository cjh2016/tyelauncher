package com.boll.tyelauncher.model;


public class WifiBean implements Comparable<WifiBean> {
    private String capabilities;
    private boolean isConnect = false;
    private String level;
    private String state;
    private String wifiName;

    public boolean isConnect() {
        return this.isConnect;
    }

    public void setConnect(boolean connect) {
        this.isConnect = connect;
    }

    @Override
    public String toString() {
        return "WifiBean{wifiName='" + this.wifiName + '\'' + ", level='" + this.level + '\'' + ", state='" + this.state + '\'' + ", capabilities='" + this.capabilities + '\'' + '}';
    }

    public String getCapabilities() {
        return this.capabilities;
    }

    public void setCapabilities(String capabilities2) {
        this.capabilities = capabilities2;
    }

    public String getWifiName() {
        return this.wifiName;
    }

    public void setWifiName(String wifiName2) {
        this.wifiName = wifiName2;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level2) {
        this.level = level2;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state2) {
        this.state = state2;
    }

    @Override
    public int compareTo(WifiBean o) {
        return Integer.parseInt(getLevel()) - Integer.parseInt(o.getLevel());
    }
}