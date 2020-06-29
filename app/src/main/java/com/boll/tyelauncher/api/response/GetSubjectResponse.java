package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class GetSubjectResponse extends BaseResponse implements Serializable {
    private List<DataBean> data;

    public String toString() {
        return "GetSubjectResponse{data=" + this.data + '}';
    }

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String subjectcode;
        private String subjectname;

        @Override
        public String toString() {
            return "DataBean{subjectcode='" + this.subjectcode + '\'' + ", subjectname='" + this.subjectname + '\'' + '}';
        }

        public String getSubjectcode() {
            return this.subjectcode;
        }

        public void setSubjectcode(String subjectcode2) {
            this.subjectcode = subjectcode2;
        }

        public String getSubjectname() {
            return this.subjectname;
        }

        public void setSubjectname(String subjectname2) {
            this.subjectname = subjectname2;
        }
    }
}
