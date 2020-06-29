package com.boll.tyelauncher.api.response;


import com.boll.tyelauncher.framework.retrofit.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class GetVideosResponse extends BaseResponse implements Serializable {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String area;
        private int auditStatus;
        private String book;
        private String catalog;
        private String city;
        private int commentCount;
        private String content;
        private long createTime;
        private String creator;
        private String description;
        private String district;
        private String documentScore;
        private int down;
        private int downloadCount;
        private String edition;
        private int exchangeCredit;
        private String extension;
        private String favtimes;
        private String grade;
        private int hasAnalysis;
        private int hasAnswer;
        private String id;
        private String identity;
        private String keywords;
        private String knowledge;
        private String lecturer;
        private int length;
        private int lifeStatus;
        private int likeCount;
        private String md5;
        private long modifyTime;
        private String origin;
        private int pageCount;
        private int payModel;
        private String phase;
        private String platform;
        private String preview;
        private String previewForMobile;
        private int price;
        private String productId;
        private String province;
        private String publisher;
        private String quality;
        private int rank;
        private String school;
        private int score;
        private int scoreCount;
        private int securityStatus;
        private String segments;
        private String sensitiveWords;
        private String source;
        private String stage;
        private String subject;
        private String tag;
        private String testType;
        private String thumbnail;
        private String title;
        private String topic;
        private List<?> topicKnowledge;
        private int transferredCount;
        private String type;
        private String unit;
        private String unit1;
        private String unit2;
        private String unit3;
        private String unit4;
        private int up;
        private long uploadTime;
        private String uploader;
        private int viewCount;
        private String volume;
        private int webmasterScore;
        private String year;

        public String getKeywords() {
            return this.keywords;
        }

        public void setKeywords(String keywords2) {
            this.keywords = keywords2;
        }

        public String getYear() {
            return this.year;
        }

        public void setYear(String year2) {
            this.year = year2;
        }

        public String getCatalog() {
            return this.catalog;
        }

        public void setCatalog(String catalog2) {
            this.catalog = catalog2;
        }

        public String getSubject() {
            return this.subject;
        }

        public void setSubject(String subject2) {
            this.subject = subject2;
        }

        public int getExchangeCredit() {
            return this.exchangeCredit;
        }

        public void setExchangeCredit(int exchangeCredit2) {
            this.exchangeCredit = exchangeCredit2;
        }

        public String getUnit1() {
            return this.unit1;
        }

        public void setUnit1(String unit12) {
            this.unit1 = unit12;
        }

        public String getUnit2() {
            return this.unit2;
        }

        public void setUnit2(String unit22) {
            this.unit2 = unit22;
        }

        public int getLikeCount() {
            return this.likeCount;
        }

        public void setLikeCount(int likeCount2) {
            this.likeCount = likeCount2;
        }

        public String getSource() {
            return this.source;
        }

        public void setSource(String source2) {
            this.source = source2;
        }

        public String getUnit3() {
            return this.unit3;
        }

        public void setUnit3(String unit32) {
            this.unit3 = unit32;
        }

        public int getSecurityStatus() {
            return this.securityStatus;
        }

        public void setSecurityStatus(int securityStatus2) {
            this.securityStatus = securityStatus2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public int getDown() {
            return this.down;
        }

        public void setDown(int down2) {
            this.down = down2;
        }

        public int getScore() {
            return this.score;
        }

        public void setScore(int score2) {
            this.score = score2;
        }

        public long getModifyTime() {
            return this.modifyTime;
        }

        public void setModifyTime(long modifyTime2) {
            this.modifyTime = modifyTime2;
        }

        public String getProvince() {
            return this.province;
        }

        public void setProvince(String province2) {
            this.province = province2;
        }

        public String getUnit4() {
            return this.unit4;
        }

        public void setUnit4(String unit42) {
            this.unit4 = unit42;
        }

        public String getSchool() {
            return this.school;
        }

        public void setSchool(String school2) {
            this.school = school2;
        }

        public int getPrice() {
            return this.price;
        }

        public void setPrice(int price2) {
            this.price = price2;
        }

        public int getRank() {
            return this.rank;
        }

        public void setRank(int rank2) {
            this.rank = rank2;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getTag() {
            return this.tag;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public int getUp() {
            return this.up;
        }

        public void setUp(int up2) {
            this.up = up2;
        }

        public String getKnowledge() {
            return this.knowledge;
        }

        public void setKnowledge(String knowledge2) {
            this.knowledge = knowledge2;
        }

        public int getWebmasterScore() {
            return this.webmasterScore;
        }

        public void setWebmasterScore(int webmasterScore2) {
            this.webmasterScore = webmasterScore2;
        }

        public String getArea() {
            return this.area;
        }

        public void setArea(String area2) {
            this.area = area2;
        }

        public String getPhase() {
            return this.phase;
        }

        public void setPhase(String phase2) {
            this.phase = phase2;
        }

        public String getThumbnail() {
            return this.thumbnail;
        }

        public void setThumbnail(String thumbnail2) {
            this.thumbnail = thumbnail2;
        }

        public String getProductId() {
            return this.productId;
        }

        public void setProductId(String productId2) {
            this.productId = productId2;
        }

        public int getCommentCount() {
            return this.commentCount;
        }

        public void setCommentCount(int commentCount2) {
            this.commentCount = commentCount2;
        }

        public String getQuality() {
            return this.quality;
        }

        public void setQuality(String quality2) {
            this.quality = quality2;
        }

        public String getVolume() {
            return this.volume;
        }

        public void setVolume(String volume2) {
            this.volume = volume2;
        }

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String unit5) {
            this.unit = unit5;
        }

        public String getDistrict() {
            return this.district;
        }

        public void setDistrict(String district2) {
            this.district = district2;
        }

        public String getGrade() {
            return this.grade;
        }

        public void setGrade(String grade2) {
            this.grade = grade2;
        }

        public String getPublisher() {
            return this.publisher;
        }

        public void setPublisher(String publisher2) {
            this.publisher = publisher2;
        }

        public String getTopic() {
            return this.topic;
        }

        public void setTopic(String topic2) {
            this.topic = topic2;
        }

        public int getDownloadCount() {
            return this.downloadCount;
        }

        public void setDownloadCount(int downloadCount2) {
            this.downloadCount = downloadCount2;
        }

        public String getPreview() {
            return this.preview;
        }

        public void setPreview(String preview2) {
            this.preview = preview2;
        }

        public String getExtension() {
            return this.extension;
        }

        public void setExtension(String extension2) {
            this.extension = extension2;
        }

        public int getHasAnswer() {
            return this.hasAnswer;
        }

        public void setHasAnswer(int hasAnswer2) {
            this.hasAnswer = hasAnswer2;
        }

        public int getLifeStatus() {
            return this.lifeStatus;
        }

        public void setLifeStatus(int lifeStatus2) {
            this.lifeStatus = lifeStatus2;
        }

        public String getCity() {
            return this.city;
        }

        public void setCity(String city2) {
            this.city = city2;
        }

        public int getScoreCount() {
            return this.scoreCount;
        }

        public void setScoreCount(int scoreCount2) {
            this.scoreCount = scoreCount2;
        }

        public String getBook() {
            return this.book;
        }

        public void setBook(String book2) {
            this.book = book2;
        }

        public String getOrigin() {
            return this.origin;
        }

        public void setOrigin(String origin2) {
            this.origin = origin2;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }

        public String getEdition() {
            return this.edition;
        }

        public void setEdition(String edition2) {
            this.edition = edition2;
        }

        public String getTestType() {
            return this.testType;
        }

        public void setTestType(String testType2) {
            this.testType = testType2;
        }

        public int getPayModel() {
            return this.payModel;
        }

        public void setPayModel(int payModel2) {
            this.payModel = payModel2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content2) {
            this.content = content2;
        }

        public String getPlatform() {
            return this.platform;
        }

        public void setPlatform(String platform2) {
            this.platform = platform2;
        }

        public int getTransferredCount() {
            return this.transferredCount;
        }

        public void setTransferredCount(int transferredCount2) {
            this.transferredCount = transferredCount2;
        }

        public String getSegments() {
            return this.segments;
        }

        public void setSegments(String segments2) {
            this.segments = segments2;
        }

        public int getHasAnalysis() {
            return this.hasAnalysis;
        }

        public void setHasAnalysis(int hasAnalysis2) {
            this.hasAnalysis = hasAnalysis2;
        }

        public String getIdentity() {
            return this.identity;
        }

        public void setIdentity(String identity2) {
            this.identity = identity2;
        }

        public String getPreviewForMobile() {
            return this.previewForMobile;
        }

        public void setPreviewForMobile(String previewForMobile2) {
            this.previewForMobile = previewForMobile2;
        }

        public String getUploader() {
            return this.uploader;
        }

        public void setUploader(String uploader2) {
            this.uploader = uploader2;
        }

        public int getViewCount() {
            return this.viewCount;
        }

        public void setViewCount(int viewCount2) {
            this.viewCount = viewCount2;
        }

        public String getCreator() {
            return this.creator;
        }

        public void setCreator(String creator2) {
            this.creator = creator2;
        }

        public int getPageCount() {
            return this.pageCount;
        }

        public void setPageCount(int pageCount2) {
            this.pageCount = pageCount2;
        }

        public String getDocumentScore() {
            return this.documentScore;
        }

        public void setDocumentScore(String documentScore2) {
            this.documentScore = documentScore2;
        }

        public int getLength() {
            return this.length;
        }

        public void setLength(int length2) {
            this.length = length2;
        }

        public String getLecturer() {
            return this.lecturer;
        }

        public void setLecturer(String lecturer2) {
            this.lecturer = lecturer2;
        }

        public String getSensitiveWords() {
            return this.sensitiveWords;
        }

        public void setSensitiveWords(String sensitiveWords2) {
            this.sensitiveWords = sensitiveWords2;
        }

        public long getUploadTime() {
            return this.uploadTime;
        }

        public void setUploadTime(long uploadTime2) {
            this.uploadTime = uploadTime2;
        }

        public String getFavtimes() {
            return this.favtimes;
        }

        public void setFavtimes(String favtimes2) {
            this.favtimes = favtimes2;
        }

        public String getStage() {
            return this.stage;
        }

        public void setStage(String stage2) {
            this.stage = stage2;
        }

        public long getCreateTime() {
            return this.createTime;
        }

        public void setCreateTime(long createTime2) {
            this.createTime = createTime2;
        }

        public int getAuditStatus() {
            return this.auditStatus;
        }

        public void setAuditStatus(int auditStatus2) {
            this.auditStatus = auditStatus2;
        }

        public String getMd5() {
            return this.md5;
        }

        public void setMd5(String md52) {
            this.md5 = md52;
        }

        public List<?> getTopicKnowledge() {
            return this.topicKnowledge;
        }

        public void setTopicKnowledge(List<?> topicKnowledge2) {
            this.topicKnowledge = topicKnowledge2;
        }
    }
}
