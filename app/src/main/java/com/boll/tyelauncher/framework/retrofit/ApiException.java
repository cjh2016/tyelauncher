package com.boll.tyelauncher.framework.retrofit;


public class ApiException extends RuntimeException {
    public int mErrorCode;
    public String mResp;

    public ApiException(int errorCode, String errorMessage, String resp) {
        super(errorMessage);
        this.mErrorCode = errorCode;
        this.mResp = resp;
    }
}
