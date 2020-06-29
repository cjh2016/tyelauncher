package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class GetRegionPathResponse extends BaseResponse implements Serializable {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String code;
        private int level;
        private String name;
        private String parentcode;

        public String getCode() {
            return this.code;
        }

        public void setCode(String code2) {
            this.code = code2;
        }

        public int getLevel() {
            return this.level;
        }

        public void setLevel(int level2) {
            this.level = level2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getParentcode() {
            return this.parentcode;
        }

        public void setParentcode(String parentcode2) {
            this.parentcode = parentcode2;
        }
    }
}