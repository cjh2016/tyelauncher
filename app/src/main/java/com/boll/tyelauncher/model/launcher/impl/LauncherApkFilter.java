package com.boll.tyelauncher.model.launcher.impl;


import android.text.TextUtils;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.model.AppContants;
import com.boll.tyelauncher.model.launcher.interfaces.AppFilter;
import com.toycloud.launcher.model.EtsConstant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LauncherApkFilter implements AppFilter {
    private static final Set<String> HIDE_ICON_APPS = new HashSet();
    private static final Set<String> HIDE_ICON_APPS_MIDDLE_SCHOOL = new HashSet();
    private static final Set<String> HIDE_ICON_APPS_PRIMARY_SCHOOL = new HashSet();
    private static final Set<String> MIDDLE_SCHOOL_ONLY_APPS = new HashSet();
    private static final Set<String> PRIMARY_SCHOOL_ONLY_APPS = new HashSet();

    static {
        init();
        initMiddleSchoolApps();
        initPrimarySchoolApps();
    }

    private static final void init() {
        HIDE_ICON_APPS.add("com.android.calendar");
        HIDE_ICON_APPS.add("com.cyanogenmod.filemanager");
        HIDE_ICON_APPS.add("com.android.dialer");
        HIDE_ICON_APPS.add("com.android.music");
        HIDE_ICON_APPS.add("com.qualcomm.qti.logkit");
        HIDE_ICON_APPS.add("com.iflytek.ChineseStroke");
        HIDE_ICON_APPS.add("com.guo2");
        HIDE_ICON_APPS.add("corg.chromium.webview_shell");
        HIDE_ICON_APPS.add("com.iflytek.xiri");
        HIDE_ICON_APPS.add("com.iflytek.inputmethod");
        HIDE_ICON_APPS.add("com.toycloud.app.myskill");
        HIDE_ICON_APPS.add("com.toycloud.updateservice");
        HIDE_ICON_APPS.add("com.android.inputmethod.latin");
        HIDE_ICON_APPS.add("com.iflytek.recommend_tsp");
        HIDE_ICON_APPS.add("com.iflytek.dictatewords");
        HIDE_ICON_APPS.add(GlobalVariable.ENGLISH_LSP_PKG);
        HIDE_ICON_APPS.add(GlobalVariable.MICROCLASS_PKG);
        HIDE_ICON_APPS.add("com.iflytek.k12aitstatistics");
        HIDE_ICON_APPS.add("com.ets100.study");
        HIDE_ICON_APPS.add(BuildConfig.APPLICATION_ID);
        HIDE_ICON_APPS.add("com.iflytek.recommend_tsp");
        HIDE_ICON_APPS.add(EtsConstant.ETS_LITE_PACKAGE);
        HIDE_ICON_APPS.add(AppContants.THINKING_TRAIN);
        HIDE_ICON_APPS.add(AppContants.POETRY_WORLD);
        HIDE_ICON_APPS.add(AppContants.MICRO_ENGLISH);
        HIDE_ICON_APPS.add(AppContants.CN_CHARACTERS);
        HIDE_ICON_APPS.add(EtsConstant.ETS_LITE_PACKAGE);
        HIDE_ICON_APPS.add(AppContants.RECITING_TEXT);
        HIDE_ICON_APPS.add(AppContants.ORAL_ENGLISH);
        HIDE_ICON_APPS.add(AppContants.WORD_DICTATION);
        HIDE_ICON_APPS.add(AppContants.PRIMARY_ENGLISH_RECITE_BOOK);
        HIDE_ICON_APPS.add(AppContants.OTA);
        HIDE_ICON_APPS.add("com.iflytek.pushservices");
        HIDE_ICON_APPS_MIDDLE_SCHOOL.add("com.iflytek.antonym");
        HIDE_ICON_APPS_MIDDLE_SCHOOL.add("com.iflytek.idiominterpretation");
        HIDE_ICON_APPS_MIDDLE_SCHOOL.add(GlobalVariable.SEARCHBYPHOTO_PKG);
        HIDE_ICON_APPS_MIDDLE_SCHOOL.add("com.iflytek.wrongnotebook");
        HIDE_ICON_APPS_PRIMARY_SCHOOL.add(AppContants.COMPOSITION);
        HIDE_ICON_APPS_PRIMARY_SCHOOL.add("com.iflytek.sceneenglish");
        HIDE_ICON_APPS_PRIMARY_SCHOOL.add(GlobalVariable.SYNCHRONOUSEXERCISE_PKG);
        HIDE_ICON_APPS_PRIMARY_SCHOOL.add(GlobalVariable.RECOMMEND_PREPAREFORSCHOOL_PKG);
    }

    private static void initMiddleSchoolApps() {
        MIDDLE_SCHOOL_ONLY_APPS.add(GlobalVariable.MICROCLASS_PKG);
        MIDDLE_SCHOOL_ONLY_APPS.add("com.iflytek.sceneenglish");
        MIDDLE_SCHOOL_ONLY_APPS.add(GlobalVariable.SYNCHRONOUSEXERCISE_PKG);
        MIDDLE_SCHOOL_ONLY_APPS.add(GlobalVariable.RECOMMEND_PREPAREFORSCHOOL_PKG);
    }

    private static void initPrimarySchoolApps() {
        PRIMARY_SCHOOL_ONLY_APPS.add(EtsConstant.ETS_LITE_PACKAGE);
        PRIMARY_SCHOOL_ONLY_APPS.add(AppContants.THINKING_TRAIN);
        PRIMARY_SCHOOL_ONLY_APPS.add(AppContants.POETRY_WORLD);
        PRIMARY_SCHOOL_ONLY_APPS.add(AppContants.MICRO_ENGLISH);
        PRIMARY_SCHOOL_ONLY_APPS.add(AppContants.CN_CHARACTERS);
        PRIMARY_SCHOOL_ONLY_APPS.add(AppContants.RECITING_TEXT);
        PRIMARY_SCHOOL_ONLY_APPS.add(AppContants.ORAL_ENGLISH);
        PRIMARY_SCHOOL_ONLY_APPS.add(AppContants.WORD_DICTATION);
        PRIMARY_SCHOOL_ONLY_APPS.add("com.iflytek.antonym");
        PRIMARY_SCHOOL_ONLY_APPS.add("com.iflytek.idiominterpretation");
        PRIMARY_SCHOOL_ONLY_APPS.add(AppContants.PRIMARY_ENGLISH_RECITE_BOOK);
    }

    @Override
    public boolean filter(String gradePeriod, AppInfo appInfo) {
        String pkg = appInfo.getPakageName();
        if (HIDE_ICON_APPS.contains(pkg)) {
            return false;
        }
        if (TextUtils.equals(gradePeriod, "03")) {
            if (!HIDE_ICON_APPS_PRIMARY_SCHOOL.contains(pkg)) {
                return true;
            }
            return false;
        } else if (HIDE_ICON_APPS_MIDDLE_SCHOOL.contains(pkg)) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getBuildInHideApps() {
        List<String> apps = new ArrayList<>(HIDE_ICON_APPS);
        apps.addAll(HIDE_ICON_APPS_PRIMARY_SCHOOL);
        apps.addAll(HIDE_ICON_APPS_MIDDLE_SCHOOL);
        return apps;
    }

    public static final boolean isOnlyMiddleSchoolApp(String pkg) {
        return MIDDLE_SCHOOL_ONLY_APPS.contains(pkg);
    }

    public static final boolean isOnlyPrimarySchoolApp(String pkg) {
        return PRIMARY_SCHOOL_ONLY_APPS.contains(pkg);
    }
}