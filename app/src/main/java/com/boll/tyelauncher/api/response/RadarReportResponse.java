package com.boll.tyelauncher.api.response;


public class RadarReportResponse {
    private Object data;
    private String msg;
    private int status;

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg2) {
        this.msg = msg2;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data2) {
        this.data = data2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    @Override
    public String toString() {
        return "RadarReportResponse{msg='" + this.msg + '\'' + ", data=" + this.data + ", status=" + this.status + '}';
    }
}
