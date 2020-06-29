package com.boll.tyelauncher.framework.retrofit;


import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("msg")
    private String msg;
    @SerializedName("status")
    private int status;

    @Override
    public String toString() {
        return "BaseResponse{status=" + this.status + ", msg='" + this.msg + '\'' + '}';
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg2) {
        this.msg = msg2;
    }

    public boolean isCodeInvalid() {
        return this.status != 0;
    }
}
