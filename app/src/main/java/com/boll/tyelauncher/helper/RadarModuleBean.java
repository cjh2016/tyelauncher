package com.boll.tyelauncher.helper;


import java.util.List;

public class RadarModuleBean {
    private int code;
    private DataBean data;
    private String desc;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public String getDesc() {
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

    @Override
    public String toString() {
        return "RadarModuleBean{code='" + this.code + '\'' + ", desc=" + this.desc + ", data=" + this.data + '}';
    }

    public static class DataBean {
        private List<Float> radarData;

        public List<Float> getRadarData() {
            return this.radarData;
        }

        public void setRadarData(List<Float> radarData2) {
            this.radarData = radarData2;
        }

        @Override
        public String toString() {
            return "DataBean{radarData=" + this.radarData + '}';
        }
    }
}
