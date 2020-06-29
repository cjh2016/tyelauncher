package com.boll.tyelauncher.util;

/**
 * @author: caijianhui
 * @date: 2020/6/29 15:39
 * @description:
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.boll.tyelauncher.api.model.User;
import com.boll.tyelauncher.application.LauncherApplication;
import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.db.DBHelper;
import com.boll.tyelauncher.easytrans.LogAgent;
import com.boll.tyelauncher.model.launcher.LauncherModel;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharepreferenceUtil {
    private static String ACTIVETIME = "activetime";
    private static String ALLAPPINFO = "allappinfo";
    private static String APPLIST_CHANGED = "applist_changed";
    private static String APPVIESSIZE = "appviessize";
    private static String APP_LIST_TEMP = "app_list_temp";
    private static String APP_LIST_TEMP_REMOVE = "app_list_temp_remove";
    private static String ETS_LOGIN = "ets_login";
    private static String ETS_LOGIN_EXTRA = "ets_login_extra";
    private static String ISDIAGNOSIS = "isdiagnosis";
    private static String ISFIRSTRUN = "isfirstrun";
    public static String ISFIRSTSTART = "isfirststart";
    private static String ISFORBIDDENHAPPYAPP = "isforbiddenhappyapp";
    private static String IS_APP_EDITABALE = "is_app_editabale";
    private static String IS_FIRST_COME_USERCENTER = "isfirstcomeusercenter";
    private static String LATESTAPPINFO = "latestappinfo";
    private static String RECITE_BOOK_NEED_SHOW = "recite_book_need_show";
    private static String RECITE_BOOK_SHOWED = "recite_book_showed";
    private static String RECITE_BOOK_SHOW_STRATEGY_REQUESTED = "recite_book_show_strategy_requested";
    private static String RECITE_BOOK_UNINSTALLED = "recite_book_uninstalled";
    private static String REGION = "region";
    public static String SHAREPREFERENCE_NAME = "mylanuchinfo";
    private static String SHOOCLNAME = "shooclname";
    private static String SNCODE = "sncode";
    public static String USERINFO = DBHelper.USER_TABLE_NAME;
    private static String USER_PROFILE_CHANGED_FLAG_FOR_ETS = "user_profile_changed_flag_for_ets";
    private static String USER_PROFILE_CHANGED_FLAG_FOR_ETS_PRI = "user_profile_changed_flag_for_ets_pri";
    public static String WHICHHEADICONSELECT = "whichheadiconselect";
    private static String ZXW_LOGIN = "zxw_login";
    private static String ZXW_LOGIN_EXTRA = "zxw_login_extra";
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;
    public static SharepreferenceUtil sharepreferenceUtil = null;
    private static SharedPreferences sp = LauncherApplication.getContext().getSharedPreferences("config", 0);
    private String SCHOOLSELECTPOSITION = "schoolSelectPosition";
    private String SUBJECT_INFO = "subject_info";
    private Context context;
    private User mUser = null;

    private SharepreferenceUtil() {
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    public static SharepreferenceUtil getSharepferenceInstance(Context context2) {
        sharedPreferences = context2.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
        editor = sharedPreferences.edit();
        if (sharepreferenceUtil != null) {
            return sharepreferenceUtil;
        }
        sharepreferenceUtil = new SharepreferenceUtil();
        return sharepreferenceUtil;
    }

    public boolean isFirstStart() {
        return sharedPreferences.getBoolean(ISFIRSTSTART, true);
    }

    public boolean isAppEditTable() {
        return sharedPreferences.getBoolean(IS_APP_EDITABALE, false);
    }

    public String getAllAppInfo(int position) {
        return sharedPreferences.getString(ALLAPPINFO + "_" + position, "");
    }

    public void setAllAppInfo(String appInfo, int position) {
        editor.putString(ALLAPPINFO + "_" + position, appInfo);
        editor.commit();
    }

    public void setAppEditAble(boolean isEdit) {
        editor.putBoolean(IS_APP_EDITABALE, isEdit);
        editor.commit();
    }

    public boolean isForbiddenHappyApp() {
        return sharedPreferences.getBoolean(ISFORBIDDENHAPPYAPP, false);
    }

    public void setForbiddenApp(boolean isforbidden) {
        editor.putBoolean(ISFORBIDDENHAPPYAPP, isforbidden);
        editor.commit();
    }

    public int getAppViewSizes() {
        return sharedPreferences.getInt(APPVIESSIZE, 1);
    }

    public void setAppViewSize(int size) {
        editor.putInt(APPVIESSIZE, size);
        editor.commit();
    }

    public void setAlreadyStart(boolean isHaveStart) {
        editor.putBoolean(ISFIRSTSTART, isHaveStart);
        editor.commit();
    }

    public static String getGradeCode() {
        User user;
        String gradeCode = "";
        if (!(sharepreferenceUtil == null || (user = sharepreferenceUtil.getUserInfo()) == null)) {
            gradeCode = user.getGradecode();
        }
        if (!TextUtils.isEmpty(gradeCode)) {
            return gradeCode;
        }
        String userinfo = LauncherApplication.getContext().getSharedPreferences(SHAREPREFERENCE_NAME, 0).getString(USERINFO, "");
        if (!TextUtils.isEmpty(userinfo)) {
            return ((User) new GsonBuilder().create().fromJson(userinfo, User.class)).getGradecode();
        }
        return gradeCode;
    }

    public static String getToken() {
        User user;
        String token = "";
        String userinfo = LauncherApplication.getContext().getSharedPreferences(SHAREPREFERENCE_NAME, 0).getString(USERINFO, "");
        if (!TextUtils.isEmpty(userinfo)) {
            token = ((User) new GsonBuilder().create().fromJson(userinfo, User.class)).getToken();
        }
        if (!(!TextUtils.isEmpty(token) || sharepreferenceUtil == null || (user = sharepreferenceUtil.getUserInfo()) == null)) {
            token = user.getToken();
        }
        if (TextUtils.isEmpty(token)) {
            return GlobalVariable.getTOKEN();
        }
        return token;
    }

    public void setAppListTempRemove(String templist) {
        editor.putString(APP_LIST_TEMP_REMOVE, templist);
        editor.commit();
    }

    public ArrayList<String> getAppListTempRemove() {
        return (ArrayList) new GsonBuilder().create().fromJson(sharedPreferences.getString(APP_LIST_TEMP_REMOVE, ""), new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    private void setUserInfo(String userInfo) {
        Log.e("userInfo--->", userInfo);
        editor.putString(USERINFO, userInfo);
        editor.commit();
    }

    public void setUserInfo(User user) {
        String message;
        synchronized (this) {
            this.mUser = user;
            HttpBaseHelper.setUserInfo(this.mUser);
        }
        if (this.mUser == null || TextUtils.isEmpty(this.mUser.getToken())) {
            if (this.mUser == null) {
                message = "设置的user信息为空";
            } else {
                message = "设置的user的token为空";
            }
            LogAgent.onError(new InvalidTokenException(message));
        }
        editor.putString(USERINFO, new GsonBuilder().create().toJson((Object) user, (Type) User.class));
        editor.commit();
        if (user != null) {
            LauncherModel.getInstance().convertGradeCode(user.getGradecode());
        }
    }

    public void setUserInfo(User user, String userinfo) {
        String message;
        synchronized (this) {
            this.mUser = user;
            HttpBaseHelper.setUserInfo(this.mUser);
        }
        if (this.mUser == null || TextUtils.isEmpty(this.mUser.getToken())) {
            if (this.mUser == null) {
                message = "设置的user信息为空";
            } else {
                message = "设置的user的token为空";
            }
            LogAgent.onError(new InvalidTokenException(message));
        }
        editor.putString(USERINFO, userinfo);
        editor.commit();
        if (user != null) {
            LauncherModel.getInstance().convertGradeCode(user.getGradecode());
        }
    }

    public User getUserInfo() {
        User user;
        synchronized (this) {
            if (this.mUser != null) {
                HttpBaseHelper.setUserInfo(this.mUser);
                user = this.mUser;
            } else {
                String userinfo = sharedPreferences.getString(USERINFO, "");
                Log.e("userinfo--->", userinfo);
                this.mUser = (User) new GsonBuilder().create().fromJson(userinfo, User.class);
                HttpBaseHelper.setUserInfo(this.mUser);
                user = this.mUser;
            }
        }
        return user;
    }

    public void Logout() {
        synchronized (this) {
            this.mUser = null;
            HttpBaseHelper.setUserId((String) null);
        }
        clearContentProvide();
        setUserInfo("");
        GlobalVariable.setLogin(false, (User) null);
        GlobalVariable.LOGIN_USER = null;
        GlobalVariable.setTOKEN((String) null, false);
        LauncherModel.getInstance().logout();
    }

    public void clearContentProvide() {
        new DBHelper(LauncherApplication.getContext()).getWritableDatabase();
        Uri uri = LanucherContentProvider.PERSON_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudyCPHelper.COLUMN_NAME_UNIQUE_FLAG, 1);
        contentValues.put("storageType", "");
        contentValues.put("username", "");
        contentValues.put("realname", "");
        contentValues.put(StudyCPHelper.COLUMN_NAME_GRADE_CODE, "");
        contentValues.put("areacode", "");
        contentValues.put("userid", "");
        contentValues.put("schoolid", "");
        contentValues.put("sex", "");
        contentValues.put("token", "");
        LauncherApplication.getContext().getContentResolver().update(uri, contentValues, "flag=?", new String[]{"1"});
    }

    public void setUserIsDiagnosis(int count) {
        if (getUserInfo() != null) {
            editor.putInt(ISDIAGNOSIS + "_" + getUserInfo().getUserid(), count);
            editor.commit();
        }
    }

    public boolean getIsDoDiagnosis() {
        if (getUserInfo() == null || sharedPreferences.getInt(ISDIAGNOSIS + "_" + getUserInfo().getUserid(), 0) <= 0) {
            return false;
        }
        return true;
    }

    public void setHeadIconSelect(int whichpositin) {
        editor.putInt(WHICHHEADICONSELECT, whichpositin);
        editor.commit();
    }

    public int getSelectHeadIcon() {
        return sharedPreferences.getInt(WHICHHEADICONSELECT, -1);
    }

    public void setSchoolSelectPosition(int schoolSelectPosition) {
        editor.putInt(this.SCHOOLSELECTPOSITION, schoolSelectPosition);
        editor.commit();
    }

    public int getSchoolSelectPosition() {
        return sharedPreferences.getInt(this.SCHOOLSELECTPOSITION, 0);
    }

    public String getSubJectString() {
        return sharedPreferences.getString(this.SUBJECT_INFO, "");
    }

    public void setSubjectString(String subjectString) {
        editor.putString(this.SUBJECT_INFO, subjectString);
        editor.commit();
    }

    public String getSchoolName() {
        return sharedPreferences.getString(SHOOCLNAME, "");
    }

    public void setSchoolName(String schoolName) {
        editor.putString(SHOOCLNAME, schoolName);
        editor.commit();
    }

    public String getRegionName() {
        return sharedPreferences.getString(REGION, "");
    }

    public void setRegion(String region) {
        editor.putString(REGION, region);
        editor.commit();
    }

    public static void putInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public void putLong(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    public long getLong(String key) {
        return sp.getLong(key, 0);
    }

    public void putString(String key, String value) {
        if (sp != null) {
            sp.edit().putString(key, value).apply();
        }
    }

    public String getString(String key) {
        if (sp == null) {
            return "";
        }
        return sp.getString(key, "");
    }

    public void setLatestAppInfo(String latestAppInfo) {
        editor.putString(LATESTAPPINFO, latestAppInfo);
        editor.commit();
    }

    public String getLatestAppInfo() {
        return sharedPreferences.getString(LATESTAPPINFO, "");
    }

    public String getAppUpdateState(String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return "";
        }
        return sharedPreferences.getString(pkgName + "app_update_state", "");
    }

    public void setAppUpdateState(String pkgName, String appState) {
        if (!TextUtils.isEmpty(pkgName) && !TextUtils.isEmpty(appState)) {
            editor.putString(pkgName + "app_update_state", appState);
            editor.commit();
        }
    }

    public String getActiveTime() {
        return sharedPreferences.getString(ACTIVETIME, "");
    }

    public void setActiveTime(String time) {
        editor.putString(ACTIVETIME, time);
        editor.commit();
    }

    public void setIsFirstComeUserCenter(boolean isFirstComeUserCenter) {
        editor.putBoolean(IS_FIRST_COME_USERCENTER, isFirstComeUserCenter);
        editor.commit();
    }

    public boolean getIsFirstComeUserCenter() {
        return sharedPreferences.getBoolean(IS_FIRST_COME_USERCENTER, true);
    }

    public boolean hasAppChanged() {
        return sharedPreferences.getBoolean(APPLIST_CHANGED, true);
    }

    public void setAppChanged(boolean isChanged) {
        editor.putBoolean(APPLIST_CHANGED, isChanged);
        editor.commit();
    }

    public void setAppListTemp(String templist) {
        editor.putString(APP_LIST_TEMP, templist);
        editor.commit();
    }

    public ArrayList<String> getAppListTemp() {
        return (ArrayList) new GsonBuilder().create().fromJson(sharedPreferences.getString(APP_LIST_TEMP, ""), new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public void setUserProfileChangedForEts(boolean changed) {
        sharedPreferences.edit().putBoolean(USER_PROFILE_CHANGED_FLAG_FOR_ETS, changed).apply();
    }

    public boolean isUserProfileChangedForEts() {
        return sharedPreferences.getBoolean(USER_PROFILE_CHANGED_FLAG_FOR_ETS, false);
    }

    public void setUserProfileChangedForEtsPri(boolean changed) {
        sharedPreferences.edit().putBoolean(USER_PROFILE_CHANGED_FLAG_FOR_ETS_PRI, changed).apply();
    }

    public boolean isUserProfileChangedForEtsPri() {
        return sharedPreferences.getBoolean(USER_PROFILE_CHANGED_FLAG_FOR_ETS_PRI, false);
    }

    public boolean isReciteBookShowStrategyRequested() {
        return sharedPreferences.getBoolean(RECITE_BOOK_SHOW_STRATEGY_REQUESTED, false);
    }

    public void setReciteBookShowStrategyRequested(boolean is) {
        sharedPreferences.edit().putBoolean(RECITE_BOOK_SHOW_STRATEGY_REQUESTED, is).apply();
    }

    public boolean isReciteBookUninstalled() {
        return sharedPreferences.getBoolean(RECITE_BOOK_UNINSTALLED, false);
    }

    public void setReciteBookUninstalled(boolean is) {
        sharedPreferences.edit().putBoolean(RECITE_BOOK_UNINSTALLED, is).apply();
    }

    public boolean isReciteBookNeedShow() {
        return sharedPreferences.getBoolean(RECITE_BOOK_NEED_SHOW, false);
    }

    public void setReciteBookNeedShow(boolean is) {
        sharedPreferences.edit().putBoolean(RECITE_BOOK_NEED_SHOW, is).apply();
    }

    public boolean isReciteBookShowed() {
        return sharedPreferences.getBoolean(RECITE_BOOK_SHOWED, false);
    }

    public void setReciteBookShowed(boolean is) {
        sharedPreferences.edit().putBoolean(RECITE_BOOK_SHOWED, is).apply();
    }

    public boolean isETSLogin() {
        return sharedPreferences.getBoolean(ETS_LOGIN, false);
    }

    public void setETSLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean(ETS_LOGIN, isLogin).apply();
    }

    public boolean isETSLoginExtra() {
        return sharedPreferences.getBoolean(ETS_LOGIN_EXTRA, false);
    }

    public void setEtsLoginExtra(boolean extra) {
        sharedPreferences.edit().putBoolean(ETS_LOGIN_EXTRA, extra).apply();
    }

    public boolean isZXWLogin() {
        return sharedPreferences.getBoolean(ZXW_LOGIN, false);
    }

    public void setZxwLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean(ZXW_LOGIN, isLogin).commit();
    }

    public boolean isZXWLoginExtra() {
        return sharedPreferences.getBoolean(ZXW_LOGIN_EXTRA, false);
    }

    public void setZxwLoginExtra(boolean extra) {
        sharedPreferences.edit().putBoolean(ZXW_LOGIN_EXTRA, extra).commit();
    }

    public String getSNcode() {
        return sharedPreferences.getString(SNCODE, "");
    }

    public void setSNcode(String code) {
        editor.putString(SNCODE, code);
        editor.commit();
    }

    public void setKV(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void setKV(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
}
