package com.boll.tyelauncher.api.response;


public class FeedbackMessageResponse {
    private DataBean data;
    private String msg;
    private int status;

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg2) {
        this.msg = msg2;
    }

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public String toString() {
        return "FeedbackMessageResponse{msg='" + this.msg + '\'' + ", data=" + this.data + ", status=" + this.status + '}';
    }

    public static class DataBean {
        private int isHaveRead;

        public int getIsHaveRead() {
            return this.isHaveRead;
        }

        public void setIsHaveRead(int isHaveRead2) {
            this.isHaveRead = isHaveRead2;
        }

        @Override
        public String toString() {
            return "DataBean{isHaveRead=" + this.isHaveRead + '}';
        }
    }
}