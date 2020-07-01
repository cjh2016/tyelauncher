package com.boll.tyelauncher.api.service;

package com.toycloud.launcher.api.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface originalHttpService {
    @GET
    Call<ResponseBody> downloadFileSync(@Url String str);
}