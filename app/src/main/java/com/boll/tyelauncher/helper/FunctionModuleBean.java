package com.boll.tyelauncher.helper;


import java.util.List;

public class FunctionModuleBean {
    private int code;
    private DataBean data;
    private String desc;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public Object getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public String toString() {
        return "FunctionModuleBean{code='" + this.code + '\'' + ", desc=" + this.desc + ", data=" + this.data + '}';
    }

    public static class DataBean {
        private List<String> functionModuleData;

        public List<String> getFunctionModuleData() {
            return this.functionModuleData;
        }

        public void setFunctionModuleData(List<String> functionModuleData2) {
            this.functionModuleData = functionModuleData2;
        }

        public String toString() {
            return "DataBean{functionModuleData=" + this.functionModuleData + '}';
        }
    }
}
