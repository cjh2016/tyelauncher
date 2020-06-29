package com.boll.tyelauncher.api.service;

package com.toycloud.launcher.api.service;

import com.boll.tyelauncher.framework.retrofit.BaseResponse;
import com.boll.tyelauncher.model.ActiveModel;
import com.boll.tyelauncher.model.AppVersionInfo;
import com.boll.tyelauncher.model.BookInfo;
import com.boll.tyelauncher.model.DiagnosisInofo;
import com.boll.tyelauncher.model.ForbiddenAppListBean;
import com.boll.tyelauncher.model.ForbiddenAppTimeList;
import com.boll.tyelauncher.model.HappApp;
import com.boll.tyelauncher.model.HeadInfo;
import com.boll.tyelauncher.model.ReciteBookShowStrategyResult;
import com.boll.tyelauncher.model.UpdateInfo;
import com.boll.tyelauncher.model.UploadInfo;
import com.boll.tyelauncher.model.UploadLocalAppBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface LauncherService {
    @POST("user/info/updateUserPassword")
    Observable<ChangePasswordResponse> changePassword(@IflyBody ChangePasswordRequest changePasswordRequest);

    @POST("user/subject/getSubjectAndPublisher")
    @IflyEncoded
    Observable<BookInfo> getBookInfo(@Field("grade") String str);

    @POST("/config/app/getConfig")
    @IflyEncoded
    Observable<GetGlobalConfigResult> getConfig(@Field("configName") String str);

    @POST("tsp/diagnose/getDiagnoseByUser")
    @IflyEncoded
    Observable<DiagnosisInofo> getDiagnosisInfo();

    @POST("user/control/getAllApp")
    @IflyEncoded
    Observable<ForbiddenAppListBean> getForbiddenAPPList(@Field("sn") String str);

    @POST("user/control/getAllTimeControl")
    @IflyEncoded
    Observable<ForbiddenAppTimeList> getForbiddenAPPTime(@Field("sn") String str);

    @POST("appstore/apps/getCodeByTimeControl")
    @IflyEncoded
    Observable<HappApp> getForbiddenTimeApp(@Field("sn") String str);

    @POST("user/grade/getAllGrades")
    @IflyEncoded
    Observable<GetGradeResponse> getGrades();

    @POST("user/grade/getGradesByPhase")
    @IflyEncoded
    Observable<GetGradeResponse> getGradesByPhase(@Field("phase") String str);

    @POST("user/info/updateUserIcon")
    @IflyEncoded
    Observable<HeadInfo> getHeadIcon(@Field("resid") String str);

    @POST("appstore/update/forceUpdateSelfApp")
    @IflyEncoded
    Observable<AppVersionInfo> getLatestVersion();

    @POST("user/publisher/getPublisherByGsub")
    @IflyEncoded
    Observable<GetPublisherResponse> getPublisher(@Field("grade") String str, @Field("subject") String str2);

    @POST("/user/auth/isBeforeActiveTime")
    @IflyEncoded
    Observable<ReciteBookShowStrategyResult> getReciteBookShowStrategy(@Field("sn") String str);

    @POST("user/area/getAreaTree")
    @IflyEncoded
    Observable<ResponseAreaMessage> getRegionPath(@Field("areacode") String str);

    @POST("user/school/getSchool")
    @IflyEncoded
    Observable<GetSchoolResponse> getSchool(@Field("areacode") String str, @Field("gradecode") String str2);

    @POST("user/school/getSchoolById")
    @IflyEncoded
    Observable<SchoolBean> getSchoolById(@Field("schoolid") String str);

    @POST("user/subject/getSubjectByGrade")
    @IflyEncoded
    Observable<GetSubjectResponse> getSubject(@Field("grade") String str);

    @POST("appstore/update/forceUpdateApp")
    @IflyEncoded
    Observable<UpdateInfo> getUpdateInfo(@Field("appCode") String str, @Field("versionCode") int i);

    @POST("user/auth/getVerCodeByMobile")
    @IflyEncoded
    Observable<GetVercodeResponse> getVerCode(@Field("mobile") String str, @Field("type") String str2);

    @POST("user/control/insertApp")
    @IflyEncoded
    Observable<ResponseBody> inStallAPP(@Field("sn") String str, @Field("appCode") String str2, @Field("appName") String str3, @Field("appIcon") String str4);

    @POST("user/info/getNewPassword")
    @IflyEncoded
    Observable<BaseResponse> lostPassword(@Field("newpassword") String str, @Field("mobile") String str2, @Field("verCode") String str3);

    @POST("user/auth/getActiveTimeInfo")
    @IflyEncoded
    Observable<ActiveModel> setActiveTime(@Field("sn") String str, @Field("loginIp") String str2, @Field("systemVersionCode") String str3, @Field("macAddress") String str4);

    @POST("user/info/updateUserSubPubInfo")
    Observable<SetSubjectPublisherResponse> setSubjectPublisher(@IflyBody SetSubjectPublisherRequest setSubjectPublisherRequest);

    @POST("user/info/updateUserInfo")
    Observable<StudentChangeInfoResponse> studentChangeInfo(@IflyBody UpdateUserInfoRequest updateUserInfoRequest);

    @POST("user/auth/registerStudent")
    @IflyEncoded
    Observable<StudentRegistResponse> studentRegist(@Field("username") String str, @Field("password") String str2, @Field("verCode") String str3, @Field("sn") String str4, @Field("loginIp") String str5, @Field("systemVersionCode") String str6);

    @POST("user/control/deleteApp")
    @IflyEncoded
    Observable<ResponseBody> unInstallApp(@Field("sn") String str, @Field("appCode") String str2);

    @POST("user/info/getCloudToken")
    Observable<UploadInfo> upLoadHeadIcon(@IflyBody UpLoadImageFileRequest upLoadImageFileRequest);

    @POST("user/control/refreshControlApp")
    Observable<UploadLocalAppBean> upLoadLocalApps(@IflyBody UploadLocalAppsRequest uploadLocalAppsRequest);

    @POST("user/info/getLogCloudToken")
    Observable<UploadInfo> upLoadLogcat(@IflyBody UpLoadFileRequest upLoadFileRequest);

    @POST("user/sn/insertSn")
    @IflyEncoded
    Observable<ResponseBody> uploadDid(@Field("sn") String str, @Field("did") String str2);
}
