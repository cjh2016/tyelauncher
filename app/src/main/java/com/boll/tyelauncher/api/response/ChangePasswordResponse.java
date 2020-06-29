package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;

public class ChangePasswordResponse extends BaseResponse implements Serializable {
    private Object data;

    public Object getData() {
        return this.data;
    }

    public void setData(Object data2) {
        this.data = data2;
    }
}