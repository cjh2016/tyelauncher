package com.boll.tyelauncher.model;


public class BaseForbidden {
    private String data;
    private String type;

    public String getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data2) {
        this.data = data2;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    @Override
    public String toString() {
        return "BaseForbidden{type='" + this.type + '\'' + ", data='" + this.data + '\'' + '}';
    }
}
