package com.boll.tyelauncher.model.launcher.impl;


import android.content.Context;
import android.text.TextUtils;

import com.boll.tyelauncher.AppInfo;
import com.boll.tyelauncher.model.launcher.LauncherModel;
import com.boll.tyelauncher.model.launcher.bean.AppListModel;
import com.boll.tyelauncher.model.launcher.bean.AppPos;
import com.boll.tyelauncher.model.launcher.interfaces.IAppPosMgr;
import com.boll.tyelauncher.util.GsonUtils;
import com.boll.tyelauncher.util.ListUtils;
import com.boll.tyelauncher.util.SharepreferenceUtil;
import com.iflytek.utils.LauncherExecutors;
import com.iflytek.utils.WorkThreadExecutor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LauncherAppPosMgr implements IAppPosMgr, LauncherModel.IPrepareWork {
    private static final String KEY = "use_oldversion_sort";
    private static final String POS_KEY = "app.pos.key";
    private static final String PRIMARY_POS_KEY = "primary.app.pos.key";
    private final List<List<AppPos>> mAppPosModel = new ArrayList();
    private final Context mContext;
    private final WorkThreadExecutor mExecutor = LauncherExecutors.sAppLoaderExecutor;
    private final List<List<AppPos>> mPrimaryModel = new ArrayList();
    /* access modifiers changed from: private */
    public final SharepreferenceUtil mShareUtils;

    public LauncherAppPosMgr(Context context) {
        this.mContext = context.getApplicationContext();
        this.mShareUtils = SharepreferenceUtil.getSharepferenceInstance(this.mContext);
    }

    @Override
    public List<List<AppInfo>> buildPages(String gradePeriod, List<AppInfo> appInfos) {
        List<List<AppPos>> posList;
        if (TextUtils.equals(gradePeriod, "03")) {
            posList = this.mPrimaryModel;
        } else {
            posList = this.mAppPosModel;
        }
        if (posList.isEmpty()) {
            return null;
        }
        LinkedList<AppInfo> tmp = new LinkedList<>(appInfos);
        List<List<AppInfo>> result = new ArrayList<>();
        int pages = posList.size();
        for (int i = 0; i < pages; i++) {
            List<AppPos> curPage = posList.get(i);
            List<AppInfo> resultPage = new ArrayList<>(curPage.size());
            for (AppPos pos : curPage) {
                AppInfo appInfo = removeByPkg(tmp, pos.mPkgName);
                if (appInfo != null) {
                    resultPage.add(appInfo);
                }
            }
            if (i == pages - 1 && ListUtils.isNotEmpty(tmp)) {
                resultPage.addAll(tmp);
                tmp.clear();
            }
            if (!ListUtils.isEmpty((List) resultPage)) {
                result.add(resultPage);
            }
        }
        if (ListUtils.isEmpty((List) result)) {
            return null;
        }
        return result;
    }

    private AppInfo removeByPkg(LinkedList<AppInfo> list, String pkg) {
        Iterator<AppInfo> iter = list.iterator();
        while (iter.hasNext()) {
            AppInfo appInfo = iter.next();
            if (TextUtils.equals(appInfo.getPakageName(), pkg)) {
                iter.remove();
                return appInfo;
            }
        }
        return null;
    }

    @Override
    public void prepareOnWorkThread() {
        this.mAppPosModel.clear();
        boolean useOldVersion = this.mShareUtils.getBoolean(KEY, true);
        if (useOldVersion) {
            this.mShareUtils.setKV(KEY, false);
        }
        if (useOldVersion) {
            loadAppPositionsOld();
        } else {
            loadAppPositionsNew(POS_KEY, this.mAppPosModel);
        }
        loadAppPositionsNew(PRIMARY_POS_KEY, this.mPrimaryModel);
    }

    private void loadAppPositionsOld() {
        int pageCount = SharepreferenceUtil.getSharepferenceInstance(this.mContext).getAppViewSizes();
        int realPageIndex = 0;
        for (int i = 0; i < pageCount; i++) {
            ArrayList<AppInfo> appList = AppSaveHelp.getAppList(this.mContext, i);
            if (!ListUtils.isEmpty((List) appList)) {
                List<AppPos> posList = new ArrayList<>();
                int count = appList.size();
                int realCellIndex = 0;
                for (int cell = 0; cell < count; cell++) {
                    String pkg = appList.get(cell).getPakageName();
                    if (!TextUtils.isEmpty(pkg)) {
                        AppPos pos = new AppPos();
                        pos.mPkgName = pkg;
                        pos.mPageIndex = realPageIndex;
                        pos.mCellIndex = realCellIndex;
                        posList.add(pos);
                        realCellIndex++;
                    }
                }
                if (!ListUtils.isEmpty((List) posList)) {
                    this.mAppPosModel.add(posList);
                    realPageIndex++;
                }
            }
        }
        doSave(this.mAppPosModel);
    }

    private void loadAppPositionsNew(String key, List<List<AppPos>> appModel) {
        AppListModel model;
        String json = this.mShareUtils.getString(key, (String) null);
        if (!TextUtils.isEmpty(json) && (model = (AppListModel) GsonUtils.changeJsonToBean(json, AppListModel.class)) != null && ListUtils.isNotEmpty(model.mListModel)) {
            appModel.addAll(model.mListModel);
        }
    }

    private void doSave(List<List<AppPos>> data) {
        AppListModel model = new AppListModel();
        model.mListModel = data;
        new SaveTask(POS_KEY, model).run();
    }

    @Override
    public void save(String gradePeriod, List<List<AppInfo>> list) {
        String key;
        int size = list.size();
        List<List<AppPos>> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int pageIndex = i;
            List<AppInfo> apps = list.get(i);
            int pageSize = apps.size();
            List<AppPos> page = new ArrayList<>(pageSize);
            for (int cellIndex = 0; cellIndex < pageSize; cellIndex++) {
                AppPos pos = new AppPos();
                pos.mPkgName = apps.get(cellIndex).getPakageName();
                pos.mPageIndex = pageIndex;
                pos.mCellIndex = cellIndex;
                page.add(pos);
            }
            data.add(page);
        }
        AppListModel model = new AppListModel();
        model.mListModel = data;
        if (TextUtils.equals(gradePeriod, "03")) {
            key = PRIMARY_POS_KEY;
        } else {
            key = POS_KEY;
        }
        this.mExecutor.execute(new SaveTask(key, model));
    }

    private class SaveTask implements Runnable {
        private final AppListModel mAppListModel;
        private final String mKey;

        public SaveTask(String key, AppListModel model) {
            this.mKey = key;
            this.mAppListModel = model;
        }

        @Override
        public void run() {
            LauncherAppPosMgr.this.mShareUtils.setKV(this.mKey, GsonUtils.toJson(this.mAppListModel));
        }
    }
}
