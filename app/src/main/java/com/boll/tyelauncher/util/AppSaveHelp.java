package com.boll.tyelauncher.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.R;
import com.boll.tyelauncher.application.LauncherApplication;
import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.model.AppInfoSave;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppSaveHelp {
    public static ArrayList<AppInfo> saveInstallApp(ArrayList<AppInfo> list, AppInfo appInfo) {
        if (!isHaveSameApp(list, appInfo)) {
            int i = 0;
            while (true) {
                if (i >= list.size()) {
                    break;
                } else if (TextUtils.isEmpty(list.get(i).getPakageName())) {
                    AppInfo appInfo1 = list.get(i);
                    appInfo1.setIcon(appInfo.getIcon());
                    appInfo1.setAppStatus(appInfo.getAppStatus());
                    appInfo1.setSystemApp(appInfo.isSystemApp());
                    appInfo1.setSort(appInfo.getSort());
                    appInfo1.setPakageName(appInfo.getPakageName());
                    appInfo1.setAppName(appInfo.getAppName());
                    break;
                } else {
                    i++;
                }
            }
        }
        return list;
    }

    public static boolean isHaveSameApp(ArrayList<AppInfo> list, AppInfo appInfo) {
        for (int i = 0; i < list.size(); i++) {
            String pakageName = list.get(i).getPakageName();
            if (!TextUtils.isEmpty(pakageName) && pakageName.equals(appInfo.getPakageName())) {
                return true;
            }
        }
        return false;
    }

    public static List<AppInfoSave> appInfoToAppInfoSave(List<AppInfo> appInfos) {
        List<AppInfoSave> appInfoSaveList = new ArrayList<>();
        if (!appInfos.isEmpty()) {
            for (int i = 0; i < appInfos.size(); i++) {
                AppInfoSave appInfoSave = new AppInfoSave();
                AppInfo appInfo = appInfos.get(i);
                appInfoSave.setAppName(appInfo.getAppName());
                appInfoSave.setAppStatus(appInfo.getAppStatus());
                appInfoSave.setSort(appInfo.getSort());
                appInfoSave.setSystemApp(appInfo.isSystemApp());
                appInfoSave.setPakageName(appInfo.getPakageName());
                appInfoSaveList.add(appInfoSave);
            }
        }
        return appInfoSaveList;
    }

    public static ArrayList<AppInfo> appInfoSaveToAppInfo(List<AppInfoSave> appInfos, Context context) {
        ArrayList<AppInfo> appInfoSaveList = new ArrayList<>();
        if (!appInfos.isEmpty()) {
            for (int i = 0; i < appInfos.size(); i++) {
                AppInfo appInfo = new AppInfo();
                AppInfoSave appInfoSave = appInfos.get(i);
                appInfo.setAppName(appInfoSave.getAppName());
                appInfo.setAppStatus(appInfoSave.getAppStatus());
                appInfo.setSort(appInfoSave.getmSort());
                appInfo.setSystemApp(appInfoSave.isSystemApp());
                appInfo.setPakageName(appInfoSave.getPakageName());
                if (EtsConstant.ETS_PHONETIC_VIRTUAL_PACKAGE.equals(appInfoSave.getPakageName())) {
                    appInfo.setIcon(LauncherApplication.getContext().getDrawable(R.drawable.ets_phonetic_study_logo));
                } else {
                    appInfo.setIcon(getAppIcon(appInfoSave.getPakageName(), context));
                }
                appInfoSaveList.add(appInfo);
            }
        }
        return appInfoSaveList;
    }

    public static Drawable getAppIcon(String pakageName, Context context) {
        if (TextUtils.isEmpty(pakageName) || GlobalVariable.allapps.isEmpty()) {
            return null;
        }
        PackageManager pkgmanager = context.getPackageManager();
        try {
            return pkgmanager.getApplicationIcon(pkgmanager.getApplicationInfo(pakageName, 128));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<AppInfo> getAppList(Context context, int position) {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        ArrayList<AppInfoSave> appInfoSaves = new ArrayList<>();
        String allAppInfo = SharepreferenceUtil.getSharepferenceInstance(context).getAllAppInfo(position);
        if (TextUtils.isEmpty(allAppInfo)) {
            return appInfos;
        }
        Log.e("allAppInfo-->", allAppInfo);
        Gson gson = new GsonBuilder().create();
        ArrayList<JsonObject> jsonObjects = (ArrayList) gson.fromJson(allAppInfo, new TypeToken<ArrayList<JsonObject>>() {
        }.getType());
        if (jsonObjects == null) {
            return appInfos;
        }
        Iterator<JsonObject> it = jsonObjects.iterator();
        while (it.hasNext()) {
            appInfoSaves.add(gson.fromJson((JsonElement) it.next(), AppInfoSave.class));
        }
        if (allAppInfo.contains(GlobalVariable.MICROCLASS_PKG)) {
            for (int i = 0; i < appInfoSaves.size(); i++) {
                String packageName = appInfoSaves.get(i).getPakageName();
                if (!TextUtils.isEmpty(packageName) && packageName.equals(GlobalVariable.MICROCLASS_PKG)) {
                    appInfoSaves.remove(i);
                }
            }
        }
        return appInfoSaveToAppInfo(appInfoSaves, context);
    }

    public static void saveAppList(Context context, ArrayList<AppInfo> appInfoList, int position) {
        String applist = new GsonBuilder().create().toJson((Object) appInfoToAppInfoSave(appInfoList));
        Log.e("saveAppList", applist + ":");
        LogSaveManager_Util.saveLog(context, applist);
        SharepreferenceUtil.getSharepferenceInstance(context).setAllAppInfo(applist, position);
    }

    public static boolean isCantainIngnorAPP(String packageName) {
        if (!GlobalVariable.appInfoList_forbidden.isEmpty() && GlobalVariable.appInfoList_forbidden.contains(packageName)) {
            return true;
        }
        return false;
    }

    public static boolean isCantainXXAPP(String packageName, Context context) {
        int pageSize = SharepreferenceUtil.getSharepferenceInstance(context).getAppViewSizes();
        boolean isContain = false;
        for (int i = 0; i < pageSize; i++) {
            ArrayList<AppInfo> appList = getAppList(context, i);
            int j = 0;
            while (true) {
                if (j >= appList.size()) {
                    break;
                }
                String pakageName1 = appList.get(j).getPakageName();
                if (!TextUtils.isEmpty(pakageName1) && pakageName1.equals(packageName)) {
                    isContain = true;
                    break;
                }
                j++;
            }
            if (isContain) {
                return isContain;
            }
        }
        return isContain;
    }

    public static boolean isHaveNullApp(ArrayList<AppInfo> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (TextUtils.isEmpty(arrayList.get(i).getPakageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContainShowApp(String packageName, Context context) {
        SharepreferenceUtil sharepferenceInstance = SharepreferenceUtil.getSharepferenceInstance(context);
        int appViewSizes = sharepferenceInstance.getAppViewSizes();
        for (int i = 0; i < appViewSizes; i++) {
            if (sharepferenceInstance.getAllAppInfo(i).contains(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getLeftAppList(ArrayList<AppInfo> appInfoList, Context mContext) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (!(appInfoList == null || appInfoList.size() == 0)) {
            ArrayList<String> list_system = new ArrayList<>();
            ArrayList<String> list_save = new ArrayList<>();
            int pageSize = SharepreferenceUtil.getSharepferenceInstance(mContext).getAppViewSizes();
            for (int i = 0; i < pageSize; i++) {
                Iterator<AppInfo> it = getAppList(mContext, i).iterator();
                while (it.hasNext()) {
                    AppInfo appInfo = it.next();
                    if (!TextUtils.isEmpty(appInfo.getPakageName())) {
                        list_save.add(appInfo.getPakageName());
                    }
                }
            }
            Iterator<AppInfo> it2 = appInfoList.iterator();
            while (it2.hasNext()) {
                list_system.add(it2.next().getPakageName());
            }
            arrayList.clear();
            if (list_save.size() == 0) {
                arrayList.addAll(list_system);
            } else if (list_system.removeAll(list_save)) {
                arrayList.addAll(list_system);
            }
        }
        return arrayList;
    }

    public static ArrayList<String> removeMoreApp(ArrayList<AppInfo> appInfoList, Context mContext) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (!(appInfoList == null || appInfoList.size() == 0)) {
            ArrayList<String> list_system = new ArrayList<>();
            ArrayList<String> list_save = new ArrayList<>();
            int pageSize = SharepreferenceUtil.getSharepferenceInstance(mContext).getAppViewSizes();
            for (int i = 0; i < pageSize; i++) {
                Iterator<AppInfo> it = getAppList(mContext, i).iterator();
                while (it.hasNext()) {
                    AppInfo appInfo = it.next();
                    if (!TextUtils.isEmpty(appInfo.getPakageName())) {
                        list_save.add(appInfo.getPakageName());
                    }
                }
            }
            Iterator<AppInfo> it2 = appInfoList.iterator();
            while (it2.hasNext()) {
                list_system.add(it2.next().getPakageName());
            }
            for (int i2 = 0; i2 < list_save.size(); i2++) {
                String saveName = list_save.get(i2);
                if (!list_system.contains(saveName)) {
                    arrayList.add(saveName);
                }
            }
        }
        return arrayList;
    }

    public static boolean isSystemApp(PackageInfo pInfo) {
        return (pInfo.applicationInfo.flags & 1) != 0;
    }
}
