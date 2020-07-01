package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.text.TextUtils;
import android.util.Log;
import com.toycloud.launcher.util.GsonUtils;
import java.util.ArrayList;
import java.util.List;

public class CatalogItem {
    private static final String TAG = "CatalogItem";
    public SimpleCatalogBean chapter;
    public String chapterCode;
    public String gradeCode;
    public String phaseCode;
    public String selectedBookCode;
    public String subjectCode;
    public String versionTitle;

    public CatalogItem() {
    }

    public String toJson() {
        return GsonUtils.toJson(this);
    }

    public static CatalogItem fromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return (CatalogItem) GsonUtils.changeJsonToBean(json, CatalogItem.class);
        } catch (Throwable exp) {
            Log.e(TAG, "从json转化成CatalogItem失败", exp);
            return null;
        }
    }

    public static CatalogItem fromCatalogInfo(CatalogInfo catalogInfo) {
        if (catalogInfo == null || catalogInfo.getSelectedBean() == null) {
            return null;
        }
        CatalogItem instance = new CatalogItem();
        instance.subjectCode = catalogInfo.getSubjectCode();
        instance.phaseCode = catalogInfo.getPhaseCode();
        instance.chapterCode = catalogInfo.getChapterCode();
        instance.gradeCode = catalogInfo.getGradeCode();
        instance.versionTitle = catalogInfo.getVersionTitle();
        instance.selectedBookCode = catalogInfo.getSelectedBookCode();
        SimpleCatalogBean child = null;
        SimpleCatalogBean section = null;
        for (CatalogBean bean = catalogInfo.getSelectedBean(); bean != null; bean = bean.getParent()) {
            section = new SimpleCatalogBean();
            section.code = bean.getCode();
            section.name = bean.getName();
            section.course = child;
            child = section;
        }
        instance.chapter = section;
        return instance;
    }

    public CatalogItem(String subjectCode2, String phaseCode2) {
        this.subjectCode = subjectCode2;
        this.phaseCode = phaseCode2;
    }

    public List<String> getFullName() {
        List<String> result = new ArrayList<>();
        for (SimpleCatalogBean bean = this.chapter; bean != null; bean = bean.course) {
            if (bean.name == null) {
                result.add("");
            } else {
                result.add(bean.name);
            }
        }
        return result;
    }

    public static boolean isEquals(CatalogItem a, CatalogItem b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null || !TextUtils.equals(a.subjectCode, b.subjectCode) || !TextUtils.equals(a.chapterCode, b.chapterCode)) {
            return false;
        }
        return SimpleCatalogBean.isEquals(a.chapter, b.chapter);
    }
}
