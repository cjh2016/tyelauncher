package com.boll.tyelauncher.model;


public class ReciteBookShowStrategyResult {
    private DataBean data;
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

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private boolean isBefore;

        public boolean isBefore() {
            return this.isBefore;
        }

        public void setBefore(boolean before) {
            this.isBefore = before;
        }

        @Override
        public String toString() {
            return "DataBean{isBefore=" + this.isBefore + '}';
        }
    }

    @Override
    public String toString() {
        return "ReciteBookShowStrategyResult{msg='" + this.msg + '\'' + ", status=" + this.status + ", data=" + this.data + '}';
    }
}
