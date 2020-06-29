package com.boll.tyelauncher.model;


public class UpdateInfo {
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

    public static class DataBean {
        private String appIcon;
        private String filePath;
        private int isOrNotForceUpdate;
        private int versionCode;

        public String getAppIcon() {
            return this.appIcon;
        }

        public void setAppIcon(String appIcon2) {
            this.appIcon = appIcon2;
        }

        public int getIsOrNotForceUpdate() {
            return this.isOrNotForceUpdate;
        }

        public void setIsOrNotForceUpdate(int isOrNotForceUpdate2) {
            this.isOrNotForceUpdate = isOrNotForceUpdate2;
        }

        @Override
        public String toString() {
            return "DataBean{appIcon='" + this.appIcon + '\'' + ", isOrNotForceUpdate=" + this.isOrNotForceUpdate + ", filePath='" + this.filePath + '\'' + ", versionCode=" + this.versionCode + '}';
        }

        public String getFilePath() {
            return this.filePath;
        }

        public void setFilePath(String filePath2) {
            this.filePath = filePath2;
        }

        public int getVersionCode() {
            return this.versionCode;
        }

        public void setVersionCode(int versionCode2) {
            this.versionCode = versionCode2;
        }
    }

    @Override
    public String toString() {
        return "UpdateInfo{msg='" + this.msg + '\'' + ", data=" + this.data + ", status=" + this.status + '}';
    }
}