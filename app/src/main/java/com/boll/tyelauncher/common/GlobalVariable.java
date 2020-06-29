package com.boll.tyelauncher.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.api.model.User;
import com.boll.tyelauncher.easytrans.LogAgent;
import com.boll.tyelauncher.ui.listener.GlobalUserInfoListener;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable {
    public static final String AUTHORITY = "com.iflytek.lanucher.userinfo";
    public static final String ENGLISH_LSP_ENTER_ACTIVITY = "com.iflytek.englishlsp.MainActivity";
    public static final String ENGLISH_LSP_PKG = "com.iflytek.englishlsp";
    public static final String FEEDBACKPACKAGENAME = "com.iflytek.problems_feedback";
    public static final String FEEDBACKPACKAGENAME_ACTIVITY = "com.iflytek.problemsfeedback.ui.activity.ProblemAndAdviceActivity";
    public static final int GRADE_8 = 8;
    public static final int GRADE_9 = 9;
    private static boolean ISLOGIN = false;
    public static final String KEY_SUBJECT_CODE = "sCode";
    public static final String KEY_TAB_CODE = "key.tabCode";
    public static User LOGIN_USER = null;
    public static final String MICROCLASS_ENTER_ACTIVITY = "com.iflytek.microclass.ui.activity.HomePageActivity";
    public static final String MICROCLASS_PKG = "com.iflytek.microclass";
    private static final String MWRONGNOTEBOOK_ENTER_ACTIVITY = "com.iflytek.wrongnotebook.ui.activity.NewSubjectDetailActivity";
    private static final String MWRONGNOTEBOOK_ENTER_ACTIVITY_PRIMARY = "com.iflytek.wrongnotebook.primary.PriMainActivity";
    public static final String MWRONGNOTEBOOK_PKG = "com.iflytek.wrongnotebook";
    public static final Object NULL = new Object();
    public static final String PACKAGE_ENGLISGTOCHINENSE = "com.toycloud.app.knowledgeclassroom";
    public static final String PACKAGE_ENGLISGTOCHINENSE_ACTIIVITY = "com.toycloud.app.knowledgeclassroom.ui.activity.TranslationActivity";
    public static final Uri PERSON_CONTENT_URI = Uri.parse("content://com.iflytek.lanucher.userinfo/userinfo");
    public static final String RECOMMEND_ENTER_ACTIVITY = "com.iflytek.chapterdiagnosis.DiagnosisMainActivity";
    public static final String RECOMMEND_PKG = "com.toyCloud.android.recommend";
    public static final String RECOMMEND_PREPAREFORSCHOOL_MAIN = "com.iflytek.prepareforschool.activity.MainActivity_New";
    public static final String RECOMMEND_PREPAREFORSCHOOL_PKG = "com.iflytek.prepareforschool";
    public static final String RECOMMEND_TOPIC_ACTIVITY = "com.toyCloud.android.recommend.ui.topic.activity.RecommendTopicCardActivity";
    public static final String RECOMMEND_TSP_MAPPING_ACTIVITY = "com.iflytek.module_map.mapping.MappingActivity";
    public static final String RECOMMEND_TSP_PKG = "com.iflytek.recommend_tsp";
    public static final String RECOMMEND_TSP_PRACTICE = "iflytek.com.module_practice.activity.MainPracticeActivity";
    public static final String RECOMMEND_TSP_STUDY = "com.iflytek.module_study.ui.activity.StudyMainActivity";
    public static final String RECOMMEND_TSP_STUDY_SYSTEM__ACTIVITY = "com.iflytek.module_map.studysystem.StudySystemActivity";
    public static final String SEARCHBYPHOTO_ENTER_ACTIVITY = "com.iflytek.searchbyphoto.capture.CameraActivity";
    public static final String SEARCHBYPHOTO_PKG = "com.iflytek.searchbyphoto";
    public static final String STUDY_AUTHORITY = "com.iflytek.study.snapshot";
    public static final Uri STUDY_SNAPSHOT_CONTENT_URI = Uri.parse("content://com.iflytek.study.snapshot/study_snapshot");
    public static final String SUBJECTSTRING = "{\"msg\":\"操作成功!\",\"data\":[{\"subject\":{\"subjectcode\":\"01\",\"subjectname\":\"语文\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}},{\"subject\":{\"subjectcode\":\"02\",\"subjectname\":\"数学\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}},{\"subject\":{\"subjectcode\":\"03\",\"subjectname\":\"英语\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}},{\"subject\":{\"subjectcode\":\"05\",\"subjectname\":\"物理\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}},{\"subject\":{\"subjectcode\":\"06\",\"subjectname\":\"化学\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}},{\"subject\":{\"subjectcode\":\"13\",\"subjectname\":\"生物\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}},{\"subject\":{\"subjectcode\":\"27\",\"subjectname\":\"政治\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}},{\"subject\":{\"subjectcode\":\"12\",\"subjectname\":\"历史\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}},{\"subject\":{\"subjectcode\":\"14\",\"subjectname\":\"地理\"},\"publisher\":{\"publishercode\":\"01\",\"publishername\":\"人民教育出版社\"}}],\"status\":0}";
    public static final String SYNCHRONOUSEXERCISE_ENTER_ACTIVITY = "com.iflytek.synchronousexercise.ui.activity.MainActivity";
    public static final String SYNCHRONOUSEXERCISE_PKG = "com.iflytek.synchronousexercise";
    public static final String TABLE_SNAPSHOT_NAME = "study_snapshot";
    public static final String TAB_CODE_JDC = "jdc";
    public static final String TAB_CODE_JJQ = "jjq";
    public static final String TAB_CODE_ZZL = "zzl";
    public static String TOKEN = "";
    public static final ComponentName WRONG_NOTEBOOK_CN = new ComponentName("com.iflytek.wrongnotebook", MWRONGNOTEBOOK_ENTER_ACTIVITY);
    public static final ComponentName WRONG_NOTEBOOK_CN_PRIMARY = new ComponentName("com.iflytek.wrongnotebook", MWRONGNOTEBOOK_ENTER_ACTIVITY_PRIMARY);
    public static List<ResolveInfo> allapps = new ArrayList();
    public static ArrayList<AppInfo> appInfoList;
    public static ArrayList<String> appInfoList_forbidden = new ArrayList<>();
    public static ArrayList<Activity> list_Activity = new ArrayList<>();
    public static final Handler sUIHandler = new Handler();

    public static final ComponentName createWrongNotebookComponentName() {
        String gradePeriod = LauncherModel.getInstance().getGradePeriod();
        LauncherModel.getInstance();
        if (LauncherModel.isPrimaryGradePeriod(gradePeriod)) {
            return WRONG_NOTEBOOK_CN_PRIMARY;
        }
        return WRONG_NOTEBOOK_CN;
    }

    public static void setTOKEN(String token, boolean mustNotNull) {
        TOKEN = token;
        if (TextUtils.isEmpty(token) && mustNotNull) {
            LogAgent.onError(new InvalidTokenException("设置的token为空: " + token));
        }
    }

    public static String getTOKEN() {
        return TOKEN;
    }

    public static void setLogin(final boolean isLogin, final User user) {
        ISLOGIN = isLogin;
        sUIHandler.post(new Runnable() {
            @Override
            public void run() {
                GlobalUserInfoListener.getInstance().onUserInfoChanged(isLogin, user);
            }
        });
    }

    public static boolean isLogin() {
        return ISLOGIN;
    }
}
