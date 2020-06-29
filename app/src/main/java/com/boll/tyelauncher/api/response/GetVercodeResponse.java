package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;

public class GetVercodeResponse extends BaseResponse implements Serializable {
    private String data;

    public String getData() {
        return this.data;
    }

    public void setData(String data2) {
        this.data = data2;
    }
}
