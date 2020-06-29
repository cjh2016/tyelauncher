package com.boll.tyelauncher.model;

package com.toycloud.launcher.model;

import framework.hz.salmon.retrofit.BaseResponse;

public class UploadInfo extends BaseResponse {
    private DataBean data;

    public DataBean getData() {
        return this.data;
    }

    public String toString() {
        return "UploadInfo{data=" + this.data + '}';
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String containerName;
        private String domain;
        private String expires;
        private String iconPath;
        private String token;

        @Override
        public String toString() {
            return "DataBean{domain='" + this.domain + '\'' + ", containerName='" + this.containerName + '\'' + ", token='" + this.token + '\'' + ", expires='" + this.expires + '\'' + '}';
        }

        public String getDomain() {
            return this.domain;
        }

        public String getIconPath() {
            return this.iconPath;
        }

        public void setIconPath(String iconPath2) {
            this.iconPath = iconPath2;
        }

        public void setDomain(String domain2) {
            this.domain = domain2;
        }

        public String getContainerName() {
            return this.containerName;
        }

        public void setContainerName(String containerName2) {
            this.containerName = containerName2;
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token2) {
            this.token = token2;
        }

        public String getExpires() {
            return this.expires;
        }

        public void setExpires(String expires2) {
            this.expires = expires2;
        }
    }
}
