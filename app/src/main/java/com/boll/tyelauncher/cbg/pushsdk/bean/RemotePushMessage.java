package com.boll.tyelauncher.cbg.pushsdk.bean;


import android.support.annotation.Keep;
import java.io.Serializable;
import java.util.List;

@Keep
public class RemotePushMessage implements Serializable {
    private static final long serialVersionUID = -6837722844902858328L;
    private int areaLevel;
    private List<String> areaList;
    private String data;
    private String eventCode;
    private List<String> gradeList;
    private String msgId;
    private int msgType;
    private PushNotification notification;
    private String phaseCode;
    private String pkgname;
    private int romOp;
    private String romVer;
    private String type;

    public String toString() {
        return "{msgid=" + this.msgId + ",eventCode=" + this.eventCode + ",msgType=" + this.msgType + ",data=" + this.data + ",type=" + this.type + ",pkgname=" + this.pkgname + ",romVer=" + this.romVer + ",romOp=" + this.romOp + ",gradeList=" + this.gradeList + ",areaList=" + this.areaList + ",areaLevel=" + this.areaLevel + ",phaseCode=" + this.phaseCode + ",notification=" + this.notification + ",}";
    }

    public void setMsgId(String msgid) {
        this.msgId = msgid;
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

    public void setMsgType(int msgType2) {
        this.msgType = msgType2;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public void setData(String data2) {
        this.data = data2;
    }

    public String getData() {
        return this.data;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getType() {
        return this.type;
    }

    public void setPkgname(String pkgname2) {
        this.pkgname = pkgname2;
    }

    public String getPkgname() {
        return this.pkgname;
    }

    public void setRomVer(String romVer2) {
        this.romVer = romVer2;
    }

    public String getRomVer() {
        return this.romVer;
    }

    public void setRomOp(int romOp2) {
        this.romOp = romOp2;
    }

    public int getRomOp() {
        return this.romOp;
    }

    public void setGradeList(List<String> gradeList2) {
        this.gradeList = gradeList2;
    }

    public List<String> getGradeList() {
        return this.gradeList;
    }

    public void setAreaList(List<String> areaList2) {
        this.areaList = areaList2;
    }

    public List<String> getAreaList() {
        return this.areaList;
    }

    public void setAreaLevel(int areaLevel2) {
        this.areaLevel = areaLevel2;
    }

    public int getAreaLevel() {
        return this.areaLevel;
    }

    public void setPhaseCode(String phaseCode2) {
        this.phaseCode = phaseCode2;
    }

    public String getPhaseCode() {
        return this.phaseCode;
    }

    public void setNotification(PushNotification notification2) {
        this.notification = notification2;
    }

    public PushNotification getNotification() {
        return this.notification;
    }
}
