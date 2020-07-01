package com.boll.tyelauncher.api.service;

package com.toycloud.launcher.api.service;

import com.toycloud.launcher.api.response.LoginResponse;
import com.toycloud.launcher.api.service.bean.LoginRequest;
import io.reactivex.Observable;
import retrofit2.http.IflyBody;
import retrofit2.http.POST;

public interface LoginService {
    @POST("user/auth/userLogin")
    Observable<LoginResponse> login(@IflyBody LoginRequest loginRequest);
}
