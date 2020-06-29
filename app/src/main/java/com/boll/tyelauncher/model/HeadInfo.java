package com.boll.tyelauncher.model;


import framework.hz.salmon.retrofit.BaseResponse;

public class HeadInfo extends BaseResponse {
    private DataBean data;

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String userIconPath;

        public String getUserIconPath() {
            return this.userIconPath;
        }

        public void setUserIconPath(String userIconPath2) {
            this.userIconPath = userIconPath2;
        }
    }
}