package com.boll.tyelauncher.model;


import java.util.List;

public class ForbiddenAppListBean {
    private List<DataBean> data;
    private String msg;
    private int status;

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg2) {
        this.msg = msg2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String appCode;
        private String appName;
        private int status;

        public String getAppCode() {
            return this.appCode;
        }

        public void setAppCode(String appCode2) {
            this.appCode = appCode2;
        }

        public String getAppName() {
            return this.appName;
        }

        public void setAppName(String appName2) {
            this.appName = appName2;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status2) {
            this.status = status2;
        }
    }
}
