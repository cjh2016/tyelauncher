package com.boll.tyelauncher.model;


import java.util.List;

public class AppVersionInfo {
    private List<DataBean> data;
    private String msg;
    private int status;

    public String toString() {
        return "{msg='" + this.msg + '\'' + ", status=" + this.status + ", data=" + this.data + '}';
    }

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

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String appCode;
        private String appName;
        private Float fileSize;
        private int isForceUpdate;
        private String updateDetail;
        private String version;
        private int versionCode;

        public String toString() {
            return "{appCode='" + this.appCode + '\'' + ", versionCode=" + this.versionCode + ", isForceUpdate=" + this.isForceUpdate + ", appName=" + this.appName + ", version=" + this.version + ", fileSize=" + this.fileSize + ", updateDetail=" + this.updateDetail + '}';
        }

        public String getAppName() {
            if (this.appName == null) {
                return "";
            }
            return this.appName;
        }

        public void setAppName(String appName2) {
            this.appName = appName2;
        }

        public String getVersion() {
            if (this.version == null) {
                return "";
            }
            return this.version;
        }

        public void setVersion(String version2) {
            this.version = version2;
        }

        public Float getFileSize() {
            if (this.fileSize == null) {
                return Float.valueOf(0.0f);
            }
            return this.fileSize;
        }

        public void setFileSize(Float fileSize2) {
            this.fileSize = fileSize2;
        }

        public String getUpdateDetail() {
            if (this.updateDetail == null) {
                return "";
            }
            return this.updateDetail;
        }

        public void setUpdateDetail(String updateDetail2) {
            this.updateDetail = updateDetail2;
        }

        public int getIsForceUpdate() {
            return this.isForceUpdate;
        }

        public void setIsForceUpdate(int isForceUpdate2) {
            this.isForceUpdate = isForceUpdate2;
        }

        public String getAppCode() {
            return this.appCode;
        }

        public void setAppCode(String appCode2) {
            this.appCode = appCode2;
        }

        public int getVersionCode() {
            return this.versionCode;
        }

        public void setVersionCode(int versionCode2) {
            this.versionCode = versionCode2;
        }
    }
}
