package com.boll.tyelauncher.model;

package com.toycloud.launcher.model;

import framework.hz.salmon.retrofit.BaseResponse;

public class DiagnosisInofo extends BaseResponse {
    private DataBean data;

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private int count;

        public int getCount() {
            return this.count;
        }

        public void setCount(int count2) {
            this.count = count2;
        }
    }
}