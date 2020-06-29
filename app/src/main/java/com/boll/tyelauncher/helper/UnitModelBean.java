package com.boll.tyelauncher.helper;


public class UnitModelBean {
    private String code;
    private DataBean data;
    private String desc;
    private String moduleId;
    private String moduleSubTitle;
    private String moduleTitle;

    public String getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(String moduleId2) {
        this.moduleId = moduleId2;
    }

    public String getModuleTitle() {
        return this.moduleTitle;
    }

    public void setModuleTitle(String moduleTitle2) {
        this.moduleTitle = moduleTitle2;
    }

    public String getModuleSubTitle() {
        return this.moduleSubTitle;
    }

    public void setModuleSubTitle(String moduleSubTitle2) {
        this.moduleSubTitle = moduleSubTitle2;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
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

    @Override
    public String toString() {
        return "UnitModelBean{code=" + this.code + ", desc='" + this.desc + '\'' + ", data=" + this.data + ", moduleTitle='" + this.moduleTitle + '\'' + ", moduleSubTitle='" + this.moduleSubTitle + '\'' + '}';
    }

    public static class DataBean {
        private float score;
        private float totalScore;
        private String unitName;

        public String getUnitName() {
            return this.unitName;
        }

        public void setUnitName(String unitName2) {
            this.unitName = unitName2;
        }

        public float getTotalScore() {
            return this.totalScore;
        }

        public void setTotalScore(float totalScore2) {
            this.totalScore = totalScore2;
        }

        public float getScore() {
            return this.score;
        }

        public void setScore(float score2) {
            this.score = score2;
        }

        @Override
        public String toString() {
            return "DataBean{unitName='" + this.unitName + '\'' + ", totalScore=" + this.totalScore + ", score=" + this.score + '}';
        }
    }
}