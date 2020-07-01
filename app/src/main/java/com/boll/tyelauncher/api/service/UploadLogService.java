package com.boll.tyelauncher.api.service;

package com.toycloud.launcher.api.service;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UploadLogService {
    @PUT("{dir}/{file}")
    Observable<ResponseBody> saveFileToCloud(@Header("Host") String str, @Path(encoded = true, value = "dir") String str2, @Path(encoded = true, value = "file") String str3, @Query("token") String str4, @Query("e") String str5, @Body RequestBody requestBody);
}
