package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class GetSchoolResponse extends BaseResponse implements Serializable {
    private List<School> data;

    public List<School> getData() {
        return this.data;
    }

    public void setData(List<School> data2) {
        this.data = data2;
    }

    @Override
    public String toString() {
        return "GetSchoolResponse{data=" + this.data + '}';
    }
}