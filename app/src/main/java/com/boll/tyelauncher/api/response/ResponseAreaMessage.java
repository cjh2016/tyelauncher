package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;

public class ResponseAreaMessage extends BaseResponse implements Serializable {
    private DataBean data;

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private CityBeanBean cityBean;
        private DistrictBeanBean districtBean;
        private ProvinceBeanBean provinceBean;

        @Override
        public String toString() {
            return "DataBean{provinceBean=" + this.provinceBean + ", cityBean=" + this.cityBean + ", districtBean=" + this.districtBean + '}';
        }

        public ProvinceBeanBean getProvinceBean() {
            return this.provinceBean;
        }

        public void setProvinceBean(ProvinceBeanBean provinceBean2) {
            this.provinceBean = provinceBean2;
        }

        public CityBeanBean getCityBean() {
            return this.cityBean;
        }

        public void setCityBean(CityBeanBean cityBean2) {
            this.cityBean = cityBean2;
        }

        public DistrictBeanBean getDistrictBean() {
            return this.districtBean;
        }

        public void setDistrictBean(DistrictBeanBean districtBean2) {
            this.districtBean = districtBean2;
        }

        public static class ProvinceBeanBean {
            private String areacode;
            private int level;
            private String name;
            private Object parentcode;

            @Override
            public String toString() {
                return "ProvinceBeanBean{areacode='" + this.areacode + '\'' + ", name='" + this.name + '\'' + ", level=" + this.level + ", parentcode=" + this.parentcode + '}';
            }

            public String getAreacode() {
                return this.areacode;
            }

            public void setAreacode(String areacode2) {
                this.areacode = areacode2;
            }

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public int getLevel() {
                return this.level;
            }

            public void setLevel(int level2) {
                this.level = level2;
            }

            public Object getParentcode() {
                return this.parentcode;
            }

            public void setParentcode(Object parentcode2) {
                this.parentcode = parentcode2;
            }
        }

        public static class CityBeanBean {
            private String areacode;
            private int level;
            private String name;
            private Object parentcode;

            @Override
            public String toString() {
                return "CityBeanBean{areacode='" + this.areacode + '\'' + ", name='" + this.name + '\'' + ", level=" + this.level + ", parentcode=" + this.parentcode + '}';
            }

            public String getAreacode() {
                return this.areacode;
            }

            public void setAreacode(String areacode2) {
                this.areacode = areacode2;
            }

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public int getLevel() {
                return this.level;
            }

            public void setLevel(int level2) {
                this.level = level2;
            }

            public Object getParentcode() {
                return this.parentcode;
            }

            public void setParentcode(Object parentcode2) {
                this.parentcode = parentcode2;
            }
        }

        public static class DistrictBeanBean {
            private String areacode;
            private int level;
            private String name;
            private Object parentcode;

            @Override
            public String toString() {
                return "DistrictBeanBean{areacode='" + this.areacode + '\'' + ", name='" + this.name + '\'' + ", level=" + this.level + ", parentcode=" + this.parentcode + '}';
            }

            public String getAreacode() {
                return this.areacode;
            }

            public void setAreacode(String areacode2) {
                this.areacode = areacode2;
            }

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public int getLevel() {
                return this.level;
            }

            public void setLevel(int level2) {
                this.level = level2;
            }

            public Object getParentcode() {
                return this.parentcode;
            }

            public void setParentcode(Object parentcode2) {
                this.parentcode = parentcode2;
            }
        }
    }

    @Override
    public String toString() {
        return "ResponseAreaMessage{data=" + this.data + '}';
    }
}