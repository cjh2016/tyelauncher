package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.api.model.User;
import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;

public class SetSubjectPublisherResponse extends BaseResponse implements Serializable {
    private User data;

    public User getData() {
        return this.data;
    }

    public void setData(User data2) {
        this.data = data2;
    }
}
