package com.boll.tyelauncher.model.launcher.interfaces;



public interface IRemoteControllerListener {
    public static final int CONFIG_TYPE_FORBBIDEN = 1;
    public static final int CONFIG_TYPE_FUN_CONFIG = 2;

    void onRemoteConfigChanged(int i);

    void updateAppUsageState(ForbiddenAPP forbiddenAPP);
}