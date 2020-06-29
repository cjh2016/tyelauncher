package com.boll.tyelauncher.model;


import java.io.Serializable;
import java.util.List;

public class ProvinceCityModel implements Serializable {
    private static final long serialVersionUID = -5923003031407801010L;
    public int code;
    public List<CityModel> response;
    public int versionCode;
}