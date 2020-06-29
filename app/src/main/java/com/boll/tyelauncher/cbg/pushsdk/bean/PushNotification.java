package com.boll.tyelauncher.cbg.pushsdk.bean;

package com.iflytek.cbg.pushsdk.bean;

import android.support.annotation.Keep;
import java.io.Serializable;
import java.util.List;

@Keep
public class PushNotification implements Serializable {
    private String action;
    private int allowCurrentPage;
    private int blink;
    private String content;
    private int currentPageDuration;
    private List<PushNotificationOption> extras;
    private String modelName;
    private String picture;
    private String pkgName;
    private int ring;
    private int setTop;
    private int showDuration;
    private int showLockScreen;
    private int slideDelete;
    private String title;
    private String url;
    private int vibrate;

    public String toString() {
        return "{title=" + this.title + ",content=" + this.content + ",picture=" + this.picture + ",ring=" + this.ring + ",vibrate=" + this.vibrate + ",blink=" + this.blink + ",setTop=" + this.setTop + ",slideDelete=" + this.slideDelete + ",showDuration=" + this.showDuration + ",allowCurrentPage=" + this.allowCurrentPage + ",currentPageDuration=" + this.currentPageDuration + ",showLockScreen=" + this.showLockScreen + ",action=" + this.action + ",url=" + this.url + ",pkgName=" + this.pkgName + ",modelName=" + this.modelName + ",extras=" + this.extras + "}";
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setContent(String content2) {
        this.content = content2;
    }

    public String getContent() {
        return this.content;
    }

    public void setPicture(String picture2) {
        this.picture = picture2;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setRing(int ring2) {
        this.ring = ring2;
    }

    public int getRing() {
        return this.ring;
    }

    public void setVibrate(int vibrate2) {
        this.vibrate = vibrate2;
    }

    public int getVibrate() {
        return this.vibrate;
    }

    public void setBlink(int blink2) {
        this.blink = blink2;
    }

    public int getBlink() {
        return this.blink;
    }

    public void setSetTop(int setTop2) {
        this.setTop = setTop2;
    }

    public int getSetTop() {
        return this.setTop;
    }

    public void setSlideDelete(int slideDelete2) {
        this.slideDelete = slideDelete2;
    }

    public int getSlideDelete() {
        return this.slideDelete;
    }

    public void setShowDuration(int showDuration2) {
        this.showDuration = showDuration2;
    }

    public int getShowDuration() {
        return this.showDuration;
    }

    public void setAllowCurrentPage(int allowCurrentPage2) {
        this.allowCurrentPage = allowCurrentPage2;
    }

    public int getAllowCurrentPage() {
        return this.allowCurrentPage;
    }

    public void setCurrentPageDuration(int currentPageDuration2) {
        this.currentPageDuration = currentPageDuration2;
    }

    public int getCurrentPageDuration() {
        return this.currentPageDuration;
    }

    public void setShowLockScreen(int showLockScreen2) {
        this.showLockScreen = showLockScreen2;
    }

    public int getShowLockScreen() {
        return this.showLockScreen;
    }

    public void setAction(String action2) {
        this.action = action2;
    }

    public String getAction() {
        return this.action;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setPkgName(String pkgName2) {
        this.pkgName = pkgName2;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setModelName(String modelName2) {
        this.modelName = modelName2;
    }

    public String getModelName() {
        return this.modelName;
    }

    public void setExtras(List<PushNotificationOption> extras2) {
        this.extras = extras2;
    }

    public List<PushNotificationOption> getExtras() {
        return this.extras;
    }
}