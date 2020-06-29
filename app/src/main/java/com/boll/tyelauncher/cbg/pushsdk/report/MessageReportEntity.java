package com.boll.tyelauncher.cbg.pushsdk.report;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Keep;
import java.io.Serializable;

@Keep
@Entity
public class MessageReportEntity implements Serializable {
    @ColumnInfo(name = "discardCode")
    private int discardCode;
    @ColumnInfo(name = "eventCode")
    private String eventCode;
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "msgId")
    private String msgId;
    @ColumnInfo(name = "reported")
    private int reported;
    @ColumnInfo(name = "status")
    private int status;

    public long getId() {
        return this.id;
    }

    public void setId(long id2) {
        this.id = id2;
    }

    public MessageReportEntity() {
    }

    @Ignore
    public MessageReportEntity(String msgId2, String eventCode2, int status2, int discardCode2) {
        this.msgId = msgId2;
        this.eventCode = eventCode2;
        this.status = status2;
        this.discardCode = discardCode2;
        this.reported = 0;
    }

    @Ignore
    public MessageReportEntity(String msgId2, String eventCode2, int status2, int discardCode2, int reported2) {
        this.msgId = msgId2;
        this.eventCode = eventCode2;
        this.status = status2;
        this.discardCode = discardCode2;
        this.reported = reported2;
    }

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

    public void setReported(int reported2) {
        this.reported = reported2;
    }

    public int getReported() {
        return this.reported;
    }

    public String toString() {
        return "id: " + this.id + " msgId: " + this.msgId + " eventCode: " + this.eventCode + " status: " + this.status + " discardCode: " + this.discardCode + " reported: " + this.reported + "\n";
    }

    public MessageReportRequestBean toRequestBean() {
        MessageReportRequestBean bean = new MessageReportRequestBean();
        bean.setMsgId(getMsgId());
        bean.setEventCode(getEventCode());
        bean.setStatus(getStatus());
        bean.setDiscardCode(getDiscardCode());
        return bean;
    }
}
