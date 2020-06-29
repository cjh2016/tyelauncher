package com.boll.tyelauncher.cbg.pushsdk.bean;


import android.support.annotation.Keep;
import java.io.Serializable;
import java.util.List;

@Keep
public class MessageReportBody implements Serializable {
    private List<MessageReportRequestBean> msgStatus;
    private String sn;

    public void setSn(String sn2) {
        this.sn = sn2;
    }

    public String getSn() {
        return this.sn;
    }

    public void setMsgStatus(List<MessageReportRequestBean> msgStatus2) {
        this.msgStatus = msgStatus2;
    }

    public List<MessageReportRequestBean> getMsgStatus() {
        return this.msgStatus;
    }
}