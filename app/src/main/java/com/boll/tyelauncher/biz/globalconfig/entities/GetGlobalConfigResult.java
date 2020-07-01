package com.boll.tyelauncher.biz.globalconfig.entities;

package com.toycloud.launcher.biz.globalconfig.entities;

import java.util.List;

public class GetGlobalConfigResult {
    private List<Config> data;
    private String msg;
    private int status;

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

    public List<Config> getData() {
        return this.data;
    }

    public void setData(List<Config> data2) {
        this.data = data2;
    }

    public static class Config {
        private String key;
        private String value;

        public String getKey() {
            return this.key;
        }

        public void setKey(String key2) {
            this.key = key2;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value2) {
            this.value = value2;
        }
    }
}