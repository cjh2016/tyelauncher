package com.boll.tyelauncher.model;


public class CityModel {
    private String areacode;
    private String areaname;
    private String id;
    private String lat;
    private String level;
    private String lng;
    private String parentid;
    private String pinyin;
    private String position;
    private String shortname;
    private String sort;
    private String zipcode;

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getAreaname() {
        return this.areaname;
    }

    public void setAreaname(String areaname2) {
        this.areaname = areaname2;
    }

    public String getParentid() {
        return this.parentid;
    }

    public void setParentid(String parentid2) {
        this.parentid = parentid2;
    }

    public String getShortname() {
        return this.shortname;
    }

    public void setShortname(String shortname2) {
        this.shortname = shortname2;
    }

    public String getAreacode() {
        return this.areacode;
    }

    public void setAreacode(String areacode2) {
        this.areacode = areacode2;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public void setZipcode(String zipcode2) {
        this.zipcode = zipcode2;
    }

    public String getPinyin() {
        return this.pinyin;
    }

    public void setPinyin(String pinyin2) {
        this.pinyin = pinyin2;
    }

    public String getLng() {
        return this.lng;
    }

    public void setLng(String lng2) {
        this.lng = lng2;
    }

    public String getLat() {
        return this.lat;
    }

    public void setLat(String lat2) {
        this.lat = lat2;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level2) {
        this.level = level2;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position2) {
        this.position = position2;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort2) {
        this.sort = sort2;
    }
}
