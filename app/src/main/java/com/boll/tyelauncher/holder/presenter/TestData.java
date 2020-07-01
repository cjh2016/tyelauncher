package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import com.iflytek.cbg.aistudy.biz.grade.GradeHelper;

public class TestData {
    public static final CatalogItem catalogMath7() {
        CatalogItem info = new CatalogItem("02", "04");
        info.chapterCode = "01_07020101-001_02_001";
        info.gradeCode = GradeHelper.GRADE_CODE_07;
        info.phaseCode = "04";
        SimpleCatalogBean chapter = new SimpleCatalogBean();
        chapter.code = "001";
        chapter.name = "1.1 正数和负数";
        info.chapter = chapter;
        return info;
    }
}