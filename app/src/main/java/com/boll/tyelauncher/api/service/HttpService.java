package com.boll.tyelauncher.api.service;

package com.toycloud.launcher.api.service;

import com.toycloud.launcher.api.response.ChapterPracticeResponse;
import com.toycloud.launcher.api.response.FeedbackMessageResponse;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.api.response.RadarReportResponse;
import com.toycloud.launcher.api.response.StepExamResponse;
import com.toycloud.launcher.api.response.StudyReportResponse;
import com.toycloud.launcher.api.response.SubjectResponse;
import framework.hz.salmon.retrofit.BaseResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.IflyEncoded;
import retrofit2.http.POST;

public interface HttpService {
    @POST("tsp/study/measure")
    @IflyEncoded
    Observable<BaseResponse> findWeakness(@Field("subjectCode") String str, @Field("phaseCode") String str2, @Field("chapterCode") String str3);

    @POST("user/problem/isHaveRead")
    @IflyEncoded
    Observable<FeedbackMessageResponse> getFeedbackMessage(@Field("sn") String str, @Field("userId") String str2);

    @POST("tsp/chapter/getLastestChapterPractice")
    @IflyEncoded
    Observable<ChapterPracticeResponse> getLastestChapterPractice(@Field("userId") String str, @Field("subjectCode") String str2, @Field("gradeCode") String str3);

    @POST("tsp/phase/getLastestPhasePractice")
    @IflyEncoded
    Observable<StepExamResponse> getLastestPhasePractice(@Field("userId") String str, @Field("subjectCode") String str2, @Field("gradeCode") String str3);

    @POST("tsp/study/portrait")
    @IflyEncoded
    Observable<MappingInfoResponse> getMappingInfo(@Field("subjectCode") String str, @Field("phaseCode") String str2, @Field("chapterCode") String str3);

    @POST("report/major/getLatestReport")
    @IflyEncoded
    Observable<StudyReportResponse> getStudyReportInfo(@Field("sn") String str, @Field("day") long j);

    @POST("wtb/wrongtopic/getSubAndReviewsByGrade")
    @IflyEncoded
    Observable<SubjectResponse> getSubject(@Field("gradeCode") String str);

    @POST("tsp/study/study")
    @IflyEncoded
    Observable<BaseResponse> gotoMicroClass(@Field("subjectCode") String str, @Field("phaseCode") String str2, @Field("chapterCode") String str3);

    @POST("elspt/report/uploadRadarScore")
    @IflyEncoded
    Observable<RadarReportResponse> reportRadarData(@Field("gradeCode") String str, @Field("listen") int i, @Field("speak") int i2, @Field("read") int i3, @Field("write") int i4);
}
