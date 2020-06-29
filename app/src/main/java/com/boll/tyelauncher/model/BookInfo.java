package com.boll.tyelauncher.model;

package com.toycloud.launcher.model;

import framework.hz.salmon.retrofit.BaseResponse;
import java.util.List;

public class BookInfo extends BaseResponse {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public String toString() {
        return "BookInfo{data=" + this.data + '}';
    }

    public static class DataBean {
        private PublisherBean publisher;
        private SubjectBean subject;

        @Override
        public String toString() {
            return "DataBean{subject=" + this.subject + ", publisher=" + this.publisher + '}';
        }

        public SubjectBean getSubject() {
            return this.subject;
        }

        public void setSubject(SubjectBean subject2) {
            this.subject = subject2;
        }

        public PublisherBean getPublisher() {
            return this.publisher;
        }

        public void setPublisher(PublisherBean publisher2) {
            this.publisher = publisher2;
        }

        public static class SubjectBean {
            private String subjectcode;
            private String subjectname;

            @Override
            public String toString() {
                return "SubjectBean{subjectcode='" + this.subjectcode + '\'' + ", subjectname='" + this.subjectname + '\'' + '}';
            }

            public String getSubjectcode() {
                return this.subjectcode;
            }

            public void setSubjectcode(String subjectcode2) {
                this.subjectcode = subjectcode2;
            }

            public String getSubjectname() {
                return this.subjectname;
            }

            public void setSubjectname(String subjectname2) {
                this.subjectname = subjectname2;
            }
        }

        public static class PublisherBean {
            private String publishercode;
            private String publishername;

            @Override
            public String toString() {
                return "PublisherBean{publishercode='" + this.publishercode + '\'' + ", publishername='" + this.publishername + '\'' + '}';
            }

            public String getPublishercode() {
                return this.publishercode;
            }

            public void setPublishercode(String publishercode2) {
                this.publishercode = publishercode2;
            }

            public String getPublishername() {
                return this.publishername;
            }

            public void setPublishername(String publishername2) {
                this.publishername = publishername2;
            }
        }
    }
}