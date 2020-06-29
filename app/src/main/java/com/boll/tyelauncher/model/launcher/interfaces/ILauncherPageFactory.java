package com.boll.tyelauncher.model.launcher.interfaces;


import com.boll.tyelauncher.AppInfo;
import java.util.List;

public interface ILauncherPageFactory {
    List<Launcher_3rdAPP_ViewHold> createHolders(String str, List<AppInfo> list);

    boolean isViewHolderEmpty(Launcher_3rdAPP_ViewHold launcher_3rdAPP_ViewHold);

    boolean isViewHolderFull(Launcher_3rdAPP_ViewHold launcher_3rdAPP_ViewHold);
}
