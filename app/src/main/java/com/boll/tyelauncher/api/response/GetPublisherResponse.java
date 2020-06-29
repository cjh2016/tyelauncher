package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class GetPublisherResponse extends BaseResponse implements Serializable {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public static class DataBean implements Serializable {
        private String publishercode;
        private String publishername;

        public String getPublishercode() {
            return this.publishercode;
        }

        public void setPublishercode(String publishercode2) {
            this.publishercode = publishercode2;
        }

        public String getPublishername() {
            return this.publishername;
        }

        public void setPublishername(String publishername2) {
            this.publishername = publishername2;
        }
    }
}
