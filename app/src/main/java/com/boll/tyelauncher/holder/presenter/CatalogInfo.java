package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import com.toycloud.launcher.model.EtsConstant;
import java.io.Serializable;

public class CatalogInfo implements Serializable {
    private int catalogErrorType;
    private String chapterCode;
    private String gradeCode;
    private boolean isNotDiagnosis;
    private String phaseCode;
    private CatalogBean selectedBean;
    private String selectedBookCode;
    private int selectedChapterPosition;
    private int selectedPartPosition;
    private String subjectCode;
    private String versionTitle;

    public int getCatalogErrorType() {
        return this.catalogErrorType;
    }

    public void setCatalogErrorType(int catalogErrorType2) {
        this.catalogErrorType = catalogErrorType2;
    }

    public boolean isNotDiagnosis() {
        return this.isNotDiagnosis;
    }

    public void setNotDiagnosis(boolean notDiagnosis) {
        this.isNotDiagnosis = notDiagnosis;
    }

    public String getGradeCode() {
        this.gradeCode = (this.gradeCode == null || this.gradeCode.isEmpty()) ? EtsConstant.SUCCESS : this.gradeCode;
        int grade = Integer.parseInt(this.gradeCode);
        if (grade == 10 || grade == 11 || grade == 12) {
            this.gradeCode = "19";
        }
        return this.gradeCode;
    }

    public void setGradeCode(String gradeCode2) {
        this.gradeCode = gradeCode2;
    }

    public String getVersionTitle() {
        return this.versionTitle;
    }

    public void setVersionTitle(String versionTitle2) {
        this.versionTitle = versionTitle2;
    }

    public String getSelectedBookCode() {
        return this.selectedBookCode;
    }

    public void setSelectedBookCode(String selectedBookCode2) {
        this.selectedBookCode = selectedBookCode2;
    }

    public int getSelectedChapterPosition() {
        return this.selectedChapterPosition;
    }

    public void setSelectedChapterPosition(int selectedChapterPosition2) {
        this.selectedChapterPosition = selectedChapterPosition2;
    }

    public int getSelectedPartPosition() {
        return this.selectedPartPosition;
    }

    public void setSelectedPartPosition(int selectedPartPosition2) {
        this.selectedPartPosition = selectedPartPosition2;
    }

    public CatalogInfo(String subjectCode2, String phaseCode2) {
        this.subjectCode = subjectCode2;
        this.phaseCode = phaseCode2;
    }

    public String getSubjectCode() {
        return this.subjectCode;
    }

    public void setSubjectCode(String subjectCode2) {
        this.subjectCode = subjectCode2;
    }

    public String getPhaseCode() {
        return this.phaseCode;
    }

    public void setPhaseCode(String phaseCode2) {
        this.phaseCode = phaseCode2;
    }

    public String getChapterCode() {
        return this.chapterCode;
    }

    public void setChapterCode(String chapterCode2) {
        this.chapterCode = chapterCode2;
    }

    public void setSelectedBean(CatalogBean selectedBean2) {
        this.selectedBean = selectedBean2;
    }

    public CatalogBean getSelectedBean() {
        return this.selectedBean;
    }
}
