package com.boll.tyelauncher.model;


public class LogInfo {
    private String filePath;
    private boolean isSuccess = false;
    private String name;

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath2) {
        this.filePath = filePath2;
    }
}
