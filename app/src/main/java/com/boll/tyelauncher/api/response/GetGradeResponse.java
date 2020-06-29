package com.boll.tyelauncher.api.response;

package com.toycloud.launcher.api.response;

import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class GetGradeResponse extends BaseResponse implements Serializable {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String gradecode;
        private String gradename;

        public String getGradecode() {
            return this.gradecode;
        }

        public void setGradecode(String gradecode2) {
            this.gradecode = gradecode2;
        }

        public String getGradename() {
            return this.gradename;
        }

        public void setGradename(String gradename2) {
            this.gradename = gradename2;
        }
    }
}
