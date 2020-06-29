package com.boll.tyelauncher.api.model;


import android.telephony.PhoneNumberUtils;
import java.io.Serializable;
import java.util.List;

public class User implements Serializable, Cloneable {
    private static final long serialVersionUID = -5630875157847547878L;
    private String address;
    private String areacode;
    private String birthday;
    public String className;
    private String createtime;
    private String email;
    private String gradeName;
    private String gradecode;
    private int id;
    private int mGradeInt = -1;
    private String mobilephone;
    private String parentid;
    private String phasecode;
    private String publisher;
    private String realname;
    private String resid;
    private List<String> roles;
    private String schoolName;
    private String schoolid;
    private String sex;
    private int state;
    public String studentNumber;
    private String subject;
    private String token;
    private String usericonpath;
    private String userid;
    private String username;

    public String getSchoolName() {
        return this.schoolName;
    }

    public void setSchoolName(String schoolName2) {
        this.schoolName = schoolName2;
    }

    public String getGradeName() {
        return this.gradeName;
    }

    public void setGradeName(String gradeName2) {
        this.gradeName = gradeName2;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className2) {
        this.className = className2;
    }

    public String getStudentNumber() {
        return this.studentNumber;
    }

    public void setStudentNumber(String studentNumber2) {
        this.studentNumber = studentNumber2;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday2) {
        this.birthday = birthday2;
    }

    public String getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(String createtime2) {
        this.createtime = createtime2;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject2) {
        this.subject = subject2;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public void setRoles(List<String> roles2) {
        this.roles = roles2;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex2) {
        this.sex = sex2;
    }

    public String getGradecode() {
        return this.gradecode;
    }

    public int getGradeCodeInt(int defValue) {
        try {
            if (this.mGradeInt < 0) {
                this.mGradeInt = Integer.parseInt(this.gradecode);
            }
            return this.mGradeInt;
        } catch (Throwable th) {
            return defValue;
        }
    }

    public boolean hasGradeCode() {
        return getGradeCodeInt(-1) != -1;
    }

    public void setGradecode(String gradecode2) {
        this.mGradeInt = -1;
        this.gradecode = gradecode2;
    }

    public String getPhasecode() {
        return this.phasecode;
    }

    public void setPhasecode(String phasecode2) {
        this.phasecode = phasecode2;
    }

    public String getAreacode() {
        return this.areacode;
    }

    public void setAreacode(String areacode2) {
        this.areacode = areacode2;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid2) {
        this.userid = userid2;
    }

    public String getParentid() {
        return this.parentid;
    }

    public void setParentid(String parentid2) {
        this.parentid = parentid2;
    }

    public String getRealname() {
        return this.realname;
    }

    public void setRealname(String realname2) {
        this.realname = realname2;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token2) {
        this.token = token2;
    }

    public String getMobilephone() {
        return this.mobilephone;
    }

    public void setMobilephone(String mobilephone2) {
        this.mobilephone = mobilephone2;
    }

    public String getSchoolid() {
        return this.schoolid;
    }

    public void setSchoolid(String schoolid2) {
        this.schoolid = schoolid2;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher2) {
        this.publisher = publisher2;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state2) {
        this.state = state2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSecurityUserPhone() {
        if (!PhoneNumberUtils.isGlobalPhoneNumber(this.username) || this.username.length() < 11) {
            return this.username;
        }
        int length = this.username.length();
        return this.username.substring(0, length - 8) + "****" + this.username.substring(length - 4);
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    @Override
    public String toString() {
        return "User{birthday='" + this.birthday + '\'' + ", createtime='" + this.createtime + '\'' + ", address='" + this.address + '\'' + ", subject='" + this.subject + '\'' + ", roles=" + this.roles + ", sex='" + this.sex + '\'' + ", gradecode='" + this.gradecode + '\'' + ", phasecode='" + this.phasecode + '\'' + ", areacode='" + this.areacode + '\'' + ", userid='" + this.userid + '\'' + ", parentid='" + this.parentid + '\'' + ", realname='" + this.realname + '\'' + ", token='" + this.token + '\'' + ", mobilephone='" + this.mobilephone + '\'' + ", schoolid='" + this.schoolid + '\'' + ", publisher='" + this.publisher + '\'' + ", state=" + this.state + ", id=" + this.id + ", email='" + this.email + '\'' + ", username='" + this.username + '\'' + ", usericonpath='" + this.usericonpath + '\'' + ", resid='" + this.resid + '\'' + '}';
    }

    public String getUsericonpath() {
        return this.usericonpath;
    }

    public String getResid() {
        return this.resid;
    }

    public void setUsericonpath(String usericonpath2) {
        this.usericonpath = usericonpath2;
    }

    public void setResid(String resid2) {
        this.resid = resid2;
    }

    public User cloneUser() {
        try {
            return (User) clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean isZheJiang() {
        return this.areacode != null && this.areacode.startsWith("33");
    }
}