package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;

public class SchoolBean extends BaseResponse implements Serializable {
    private DataBean data;

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private Object alternatefield1;
        private Object alternatefield2;
        private String cityid;
        private Object countryid;
        private String districtid;
        private Object parentorgid;
        private String phaseid;
        private String phasename;
        private String provinceid;
        private String schoolid;
        private String schoolname;

        public String getSchoolid() {
            return this.schoolid;
        }

        public void setSchoolid(String schoolid2) {
            this.schoolid = schoolid2;
        }

        public String getSchoolname() {
            return this.schoolname;
        }

        public void setSchoolname(String schoolname2) {
            this.schoolname = schoolname2;
        }

        public Object getCountryid() {
            return this.countryid;
        }

        public void setCountryid(Object countryid2) {
            this.countryid = countryid2;
        }

        public String getProvinceid() {
            return this.provinceid;
        }

        public void setProvinceid(String provinceid2) {
            this.provinceid = provinceid2;
        }

        public String getCityid() {
            return this.cityid;
        }

        public void setCityid(String cityid2) {
            this.cityid = cityid2;
        }

        public String getDistrictid() {
            return this.districtid;
        }

        public void setDistrictid(String districtid2) {
            this.districtid = districtid2;
        }

        public Object getParentorgid() {
            return this.parentorgid;
        }

        public void setParentorgid(Object parentorgid2) {
            this.parentorgid = parentorgid2;
        }

        public String getPhaseid() {
            return this.phaseid;
        }

        public void setPhaseid(String phaseid2) {
            this.phaseid = phaseid2;
        }

        public String getPhasename() {
            return this.phasename;
        }

        public void setPhasename(String phasename2) {
            this.phasename = phasename2;
        }

        public Object getAlternatefield1() {
            return this.alternatefield1;
        }

        public void setAlternatefield1(Object alternatefield12) {
            this.alternatefield1 = alternatefield12;
        }

        public Object getAlternatefield2() {
            return this.alternatefield2;
        }

        public void setAlternatefield2(Object alternatefield22) {
            this.alternatefield2 = alternatefield22;
        }
    }
}
