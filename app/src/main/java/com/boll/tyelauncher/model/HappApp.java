package com.boll.tyelauncher.model;


import java.util.List;

public class HappApp {
    private List<String> data;
    private int status;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public List<String> getData() {
        return this.data;
    }

    public void setData(List<String> data2) {
        this.data = data2;
    }
}
