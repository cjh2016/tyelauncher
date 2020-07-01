package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.iflytek.common.util.data.IniUtils;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.ChapterPracticeResponse;
import com.toycloud.launcher.api.response.StepExamResponse;
import com.toycloud.launcher.api.response.SubjectResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.util.SharepreferenceUtil;
import framework.hz.salmon.retrofit.ApiException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;

public class SegmentsPresenter {
    private static final String TAG = "SegmentsPresenter";
    private Map<String, ChapterPracticeResponse> mChapterCache = new HashMap();
    private Disposable mChapterHandle = null;
    private Context mContext;
    private Map<String, StepExamResponse> mExamCache = new HashMap();
    private Disposable mExamHandle = null;
    private ISegmentsView mSegmentsView;
    private Disposable mWrongHandle = null;
    private SubjectResponse mWrongNotebookCache;

    public interface ISegmentsView {
        void showChapterInfo(String str, ChapterPracticeResponse chapterPracticeResponse);

        void showExamInfo(String str, StepExamResponse stepExamResponse);

        void showWrongNoteBookInfo(SubjectResponse subjectResponse);
    }

    public SegmentsPresenter(Context context) {
        this.mContext = context;
    }

    public void attachView(ISegmentsView view) {
        this.mSegmentsView = view;
    }

    public void detachView() {
        this.mSegmentsView = null;
    }

    public void query(String subjectCode) {
        User user = SharepreferenceUtil.getSharepferenceInstance(this.mContext).getUserInfo();
        if (user != null) {
            String userId = user.getUserid();
            if (!TextUtils.isEmpty(userId)) {
                String token = user.getToken();
                String savedToken = SharepreferenceUtil.getToken();
                if (TextUtils.isEmpty(savedToken) || !TextUtils.equals(token, savedToken)) {
                    Log.w(TAG, "token错误：" + token);
                    if (TextUtils.isEmpty(savedToken)) {
                        Log.w(TAG, "token缺失，returned");
                    } else {
                        Log.w(TAG, "token不一致：user-token=[" + token + "] sp-token=[" + savedToken + IniUtils.PROPERTY_END_TAG);
                    }
                } else {
                    queryChapterPractice(userId, subjectCode);
                    queryExamPractice(userId, subjectCode);
                    queryWrongNoteBookInfo();
                }
            }
        }
    }

    private void queryChapterPractice(String userId, String subjectCode) {
        if (this.mChapterHandle != null && !this.mChapterHandle.isDisposed()) {
            this.mChapterHandle.dispose();
        }
        this.mChapterHandle = null;
        this.mChapterHandle = LauncherHttpHelper.getHttpService().getLastestChapterPractice(userId, subjectCode, (String) null).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SegmentsPresenter$$Lambda$0(this, subjectCode), new SegmentsPresenter$$Lambda$1(this, subjectCode));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$queryChapterPractice$9$SegmentsPresenter(String subjectCode, ChapterPracticeResponse response) throws Exception {
        if (this.mSegmentsView != null) {
            this.mSegmentsView.showChapterInfo(subjectCode, response);
        }
        Log.d(TAG, "queryChapterPractice结果： " + response);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$queryChapterPractice$10$SegmentsPresenter(String subjectCode, Throwable throwable) throws Exception {
        Log.e(TAG, "queryChapterPractice异常", throwable);
        ChapterPracticeResponse response = null;
        if (throwable != null && (throwable instanceof ApiException)) {
            ApiException exp = (ApiException) throwable;
            response = new ChapterPracticeResponse();
            response.setStatus(exp.mErrorCode);
            response.setMsg(exp.getMessage());
        }
        if (this.mSegmentsView != null) {
            this.mSegmentsView.showChapterInfo(subjectCode, response);
        }
    }

    private void queryExamPractice(String userId, String subjectCode) {
        if (this.mExamHandle != null && !this.mExamHandle.isDisposed()) {
            this.mExamHandle.dispose();
        }
        this.mExamHandle = null;
        this.mExamHandle = LauncherHttpHelper.getHttpService().getLastestPhasePractice(userId, subjectCode, (String) null).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SegmentsPresenter$$Lambda$2(this, subjectCode), new SegmentsPresenter$$Lambda$3(this, subjectCode));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$queryExamPractice$11$SegmentsPresenter(String subjectCode, StepExamResponse response) throws Exception {
        if (this.mSegmentsView != null) {
            this.mSegmentsView.showExamInfo(subjectCode, response);
        }
        Log.d(TAG, "queryExamPractice结果： " + response);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$queryExamPractice$12$SegmentsPresenter(String subjectCode, Throwable throwable) throws Exception {
        if (throwable instanceof ApiException) {
            ApiException exp = (ApiException) throwable;
            String message = exp.getMessage();
            int code = exp.mErrorCode;
            StepExamResponse response = new StepExamResponse();
            response.setMsg(message);
            response.setStatus(code);
            if (this.mSegmentsView != null) {
                this.mSegmentsView.showExamInfo(subjectCode, response);
                return;
            }
            return;
        }
        Log.e(TAG, "queryExamPractice异常", throwable);
        StepExamResponse response2 = new StepExamResponse();
        response2.setMsg((String) null);
        response2.setStatus(-1);
        if (this.mSegmentsView != null) {
            this.mSegmentsView.showExamInfo(subjectCode, response2);
        }
    }

    private void queryWrongNoteBookInfo() {
        if (this.mWrongHandle != null && !this.mWrongHandle.isDisposed()) {
            this.mWrongHandle.dispose();
        }
        this.mWrongHandle = null;
        this.mWrongHandle = LauncherHttpHelper.getHttpService().getSubject("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SegmentsPresenter$$Lambda$4(this), new SegmentsPresenter$$Lambda$5(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$queryWrongNoteBookInfo$13$SegmentsPresenter(SubjectResponse response) throws Exception {
        Log.d(TAG, "queryWrongNoteBookInfo结果： " + response);
        if (this.mSegmentsView != null) {
            this.mSegmentsView.showWrongNoteBookInfo(response);
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$queryWrongNoteBookInfo$14$SegmentsPresenter(Throwable throwable) throws Exception {
        Log.e(TAG, "queryWrongNoteBookInfo异常", throwable);
        SubjectResponse response = null;
        if (throwable instanceof ApiException) {
            ApiException exp = (ApiException) throwable;
            response = new SubjectResponse();
            response.setStatus(exp.mErrorCode);
            response.setMsg(exp.getMessage());
        }
        if (this.mSegmentsView != null) {
            this.mSegmentsView.showWrongNoteBookInfo(response);
        }
    }

    public void clearCache() {
        cancel();
        this.mChapterCache.clear();
        this.mExamCache.clear();
        this.mWrongNotebookCache = null;
    }

    public void cancel() {
        if (this.mChapterHandle != null && !this.mChapterHandle.isDisposed()) {
            this.mChapterHandle.dispose();
        }
        this.mChapterHandle = null;
        if (this.mExamHandle != null && !this.mExamHandle.isDisposed()) {
            this.mExamHandle.dispose();
        }
        this.mExamHandle = null;
        if (this.mWrongHandle != null && !this.mWrongHandle.isDisposed()) {
            this.mWrongHandle.dispose();
        }
        this.mWrongHandle = null;
    }

    public void displayCache(String subjectCode) {
        if (this.mSegmentsView != null) {
            this.mSegmentsView.showChapterInfo(subjectCode, this.mChapterCache.get(subjectCode));
            this.mSegmentsView.showExamInfo(subjectCode, this.mExamCache.get(subjectCode));
            this.mSegmentsView.showWrongNoteBookInfo(this.mWrongNotebookCache);
        }
    }
}
