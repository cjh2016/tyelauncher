package com.boll.tyelauncher.ui.uploadheadicon;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;

public class UploadImageBean extends BaseResponse implements Serializable {
    private DataBean data;

    public String toString() {
        return "UploadImageBean{data=" + this.data + '}';
    }

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String resid;

        public String toString() {
            return "DataBean{resid='" + this.resid + '\'' + '}';
        }

        public String getResid() {
            return this.resid;
        }

        public void setResid(String resid2) {
            this.resid = resid2;
        }
    }
}
