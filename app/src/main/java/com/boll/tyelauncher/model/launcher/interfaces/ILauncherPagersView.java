package com.boll.tyelauncher.model.launcher.interfaces;


import com.boll.tyelauncher.holder.BaseHolder;

import java.util.List;

public interface ILauncherPagersView {
    void initAppPages(String str, List<BaseHolder> list);

    void notifyPagesCountChanged(BaseHolder baseHolder);

    void onCreateCardPages(String str);
}