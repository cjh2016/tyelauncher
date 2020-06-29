package com.boll.tyelauncher.model;


import java.util.List;

public class ForbiddenAppTimeList {
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
        private int endTime;
        private int startTime;
        private int type;

        public int getType() {
            return this.type;
        }

        public void setType(int type2) {
            this.type = type2;
        }

        public int getStartTime() {
            return this.startTime;
        }

        public void setStartTime(int startTime2) {
            this.startTime = startTime2;
        }

        public int getEndTime() {
            return this.endTime;
        }

        public void setEndTime(int endTime2) {
            this.endTime = endTime2;
        }
    }
}
