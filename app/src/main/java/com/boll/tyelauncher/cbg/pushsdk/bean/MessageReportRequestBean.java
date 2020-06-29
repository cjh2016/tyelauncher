package com.boll.tyelauncher.cbg.pushsdk.bean;


import android.support.annotation.Keep;
import java.io.Serializable;

@Keep
public class MessageReportRequestBean implements Serializable {
    private int discardCode;
    private String eventCode;
    private String msgId;
    private int status;

    public void setMsgId(String msgId2) {
        this.msgId = msgId2;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setEventCode(String eventCode2) {
        this.eventCode = eventCode2;
    }

    public String getEventCode() {
        return this.eventCode;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setDiscardCode(int discardCode2) {
        this.discardCode = discardCode2;
    }

    public int getDiscardCode() {
        return this.discardCode;
    }
}
