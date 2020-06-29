package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;
import com.toycloud.launcher.api.model.AnchorPointData;
import com.toycloud.launcher.api.model.StudyAndDrillBean;
import java.util.List;

public class MappingInfoResponse extends BaseResponse {
    private DataBean data;

    @Override
    public boolean isCodeInvalid() {
        int status = getStatus();
        return (status == 0 || status == 3007) ? false : true;
    }

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private List<AnchorPointData> anchorDtoParamBeans;
        private int isInit;
        private LastKGMasteryItemsBean lastKGMasteryItems;
        private int learnStatus;
        private PresentKGMasteryItemsBean presentKGMasteryItems;
        private StudyAndDrillBean studyAndDrill;

        public StudyAndDrillBean getStudyAndDrill() {
            return this.studyAndDrill == null ? new StudyAndDrillBean() : this.studyAndDrill;
        }

        public void setStudyAndDrill(StudyAndDrillBean studyAndDrill2) {
            this.studyAndDrill = studyAndDrill2;
        }

        public List<AnchorPointData> getAnchorDtoParamBeans() {
            return this.anchorDtoParamBeans;
        }

        public void setAnchorDtoParamBeans(List<AnchorPointData> anchorDtoParamBeans2) {
            this.anchorDtoParamBeans = anchorDtoParamBeans2;
        }

        public int getIsInit() {
            return this.isInit;
        }

        public void setIsInit(int isInit2) {
            this.isInit = isInit2;
        }

        public int getLearnStatus() {
            return this.learnStatus;
        }

        public void setLearnStatus(int learnStatus2) {
            this.learnStatus = learnStatus2;
        }

        public PresentKGMasteryItemsBean getPresentKGMasteryItems() {
            return this.presentKGMasteryItems;
        }

        public void setPresentKGMasteryItems(PresentKGMasteryItemsBean presentKGMasteryItems2) {
            this.presentKGMasteryItems = presentKGMasteryItems2;
        }

        public LastKGMasteryItemsBean getLastKGMasteryItems() {
            return this.lastKGMasteryItems;
        }

        public void setLastKGMasteryItems(LastKGMasteryItemsBean lastKGMasteryItems2) {
            this.lastKGMasteryItems = lastKGMasteryItems2;
        }

        public String toString() {
            return "DataBean{isInit=" + this.isInit + ", learnStatus=" + this.learnStatus + ", presentKGMasteryItems=" + this.presentKGMasteryItems + ", lastKGMasteryItems=" + this.lastKGMasteryItems + ", anchorDtoParamBeans=" + this.anchorDtoParamBeans + ", studyAndDrill=" + this.studyAndDrill + '}';
        }
    }

    public static class LastKGMasteryItemsBean {
        private List<KGMasteryItemsBeanX> kGMasteryItems;
        private double masterDegree;
        private int measureStatus;

        public int getMeasureStatus() {
            return this.measureStatus;
        }

        public void setMeasureStatus(int measureStatus2) {
            this.measureStatus = measureStatus2;
        }

        public double getMasterDegree() {
            return this.masterDegree;
        }

        public void setMasterDegree(double masterDegree2) {
            this.masterDegree = masterDegree2;
        }

        public List<KGMasteryItemsBeanX> getKGMasteryItems() {
            return this.kGMasteryItems;
        }

        public void setKGMasteryItems(List<KGMasteryItemsBeanX> kGMasteryItems2) {
            this.kGMasteryItems = kGMasteryItems2;
        }
    }

    public static class KGMasteryItemsBeanX {
        private String anchorId;
        private Object graphExplain;
        private long lastDoneTime;
        private double mastery;
        private double predictMastery;
        private double realMastery;
        private Object similarExplain;
        private String userId;
        private Object uuid2topicInfo;

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId2) {
            this.userId = userId2;
        }

        public String getAnchorId() {
            return this.anchorId;
        }

        public void setAnchorId(String anchorId2) {
            this.anchorId = anchorId2;
        }

        public double getRealMastery() {
            return this.realMastery;
        }

        public void setRealMastery(double realMastery2) {
            this.realMastery = realMastery2;
        }

        public double getPredictMastery() {
            return this.predictMastery;
        }

        public void setPredictMastery(double predictMastery2) {
            this.predictMastery = predictMastery2;
        }

        public double getMastery() {
            return this.mastery;
        }

        public void setMastery(double mastery2) {
            this.mastery = mastery2;
        }

        public long getLastDoneTime() {
            return this.lastDoneTime;
        }

        public void setLastDoneTime(long lastDoneTime2) {
            this.lastDoneTime = lastDoneTime2;
        }

        public Object getSimilarExplain() {
            return this.similarExplain;
        }

        public void setSimilarExplain(Object similarExplain2) {
            this.similarExplain = similarExplain2;
        }

        public Object getGraphExplain() {
            return this.graphExplain;
        }

        public void setGraphExplain(Object graphExplain2) {
            this.graphExplain = graphExplain2;
        }

        public Object getUuid2topicInfo() {
            return this.uuid2topicInfo;
        }

        public void setUuid2topicInfo(Object uuid2topicInfo2) {
            this.uuid2topicInfo = uuid2topicInfo2;
        }
    }

    public static class PresentKGMasteryItemsBean {
        private List<KGMasteryItemsBean> kGMasteryItems;
        private double masterDegree;
        private int measureStatus;

        public int getMeasureStatus() {
            return this.measureStatus;
        }

        public void setMeasureStatus(int measureStatus2) {
            this.measureStatus = measureStatus2;
        }

        public double getMasterDegree() {
            return this.masterDegree;
        }

        public void setMasterDegree(double masterDegree2) {
            this.masterDegree = masterDegree2;
        }

        public List<KGMasteryItemsBean> getKGMasteryItems() {
            return this.kGMasteryItems;
        }

        public void setKGMasteryItems(List<KGMasteryItemsBean> kGMasteryItems2) {
            this.kGMasteryItems = kGMasteryItems2;
        }

        public String toString() {
            return "PresentKGMasteryItemsBean{masterDegree=" + this.masterDegree + ", measureStatus=" + this.measureStatus + ", kGMasteryItems=" + this.kGMasteryItems + '}';
        }
    }

    public static class KGMasteryItemsBean {
        private String anchorId;
        private Object graphExplain;
        private long lastDoneTime;
        private double mastery;
        private double predictMastery;
        private double realMastery;
        private Object similarExplain;
        private String userId;
        private Object uuid2topicInfo;

        public double getRealMastery() {
            return this.realMastery;
        }

        public void setRealMastery(double realMastery2) {
            this.realMastery = realMastery2;
        }

        public long getLastDoneTime() {
            return this.lastDoneTime;
        }

        public void setLastDoneTime(long lastDoneTime2) {
            this.lastDoneTime = lastDoneTime2;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId2) {
            this.userId = userId2;
        }

        public String getAnchorId() {
            return this.anchorId;
        }

        public void setAnchorId(String anchorId2) {
            this.anchorId = anchorId2;
        }

        public double getPredictMastery() {
            return this.predictMastery;
        }

        public void setPredictMastery(double predictMastery2) {
            this.predictMastery = predictMastery2;
        }

        public double getMastery() {
            return this.mastery;
        }

        public void setMastery(double mastery2) {
            this.mastery = mastery2;
        }

        public Object getSimilarExplain() {
            return this.similarExplain;
        }

        public void setSimilarExplain(Object similarExplain2) {
            this.similarExplain = similarExplain2;
        }

        public Object getGraphExplain() {
            return this.graphExplain;
        }

        public void setGraphExplain(Object graphExplain2) {
            this.graphExplain = graphExplain2;
        }

        public Object getUuid2topicInfo() {
            return this.uuid2topicInfo;
        }

        public void setUuid2topicInfo(Object uuid2topicInfo2) {
            this.uuid2topicInfo = uuid2topicInfo2;
        }

        public String toString() {
            return "KGMasteryItemsBean{userId='" + this.userId + '\'' + ", anchorId='" + this.anchorId + '\'' + ", realMastery=" + this.realMastery + ", predictMastery=" + this.predictMastery + ", mastery=" + this.mastery + ", lastDoneTime=" + this.lastDoneTime + ", similarExplain=" + this.similarExplain + ", graphExplain=" + this.graphExplain + ", uuid2topicInfo=" + this.uuid2topicInfo + '}';
        }
    }
}
