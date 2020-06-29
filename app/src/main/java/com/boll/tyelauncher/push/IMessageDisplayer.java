package com.boll.tyelauncher.push;


import android.content.Context;

import com.boll.tyelauncher.cbg.pushsdk.bean.RemotePushMessage;

public interface IMessageDisplayer {
    void displayMessage(Context context, RemotePushMessage remotePushMessage, String str);
}
