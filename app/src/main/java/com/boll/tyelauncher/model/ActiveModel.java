package com.boll.tyelauncher.model;


public class ActiveModel {
    private long data;
    private String msg;
    private int status;

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg2) {
        this.msg = msg2;
    }

    public long getData() {
        return this.data;
    }

    public void setData(long data2) {
        this.data = data2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }
}