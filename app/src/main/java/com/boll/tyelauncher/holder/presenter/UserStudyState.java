package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.text.TextUtils;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.MappingInfoResponse;

public class UserStudyState {
    public UserStudySnapshot mChemicalSnapshot = new UserStudySnapshot();
    public UserStudySnapshot mMathSnapshot = new UserStudySnapshot();
    public UserStudySnapshot mPhysicalSnapshot = new UserStudySnapshot();
    public UserStudySnapshot mScienceSnapshot = new UserStudySnapshot();
    public User mUser;
    public UserExeciseCount mUserExeciseCount = new UserExeciseCount();

    public UserStudyState(User user) {
        if (user != null) {
            this.mUser = user.cloneUser();
        }
    }

    public boolean isGradeChanged(User user) {
        if (this.mUser != null && !TextUtils.equals(this.mUser.getGradecode(), user.getGradecode())) {
            return true;
        }
        return false;
    }

    private void doResetUser(User user) {
        this.mUser = user.cloneUser();
        this.mUserExeciseCount.reset();
        this.mMathSnapshot.reset();
        this.mPhysicalSnapshot.reset();
        this.mChemicalSnapshot.reset();
        this.mScienceSnapshot.reset();
    }

    public boolean reset(User user) {
        if (isUserValid()) {
            boolean isMatch = false;
            if (TextUtils.equals(this.mUser.getUserid(), user.getUserid()) && TextUtils.equals(this.mUser.getGradecode(), user.getGradecode())) {
                isMatch = true;
            }
            if (isMatch) {
                this.mUser = user.cloneUser();
                return false;
            }
            doResetUser(user);
            return true;
        }
        doResetUser(user);
        return true;
    }

    public void reset() {
        this.mUser = null;
        this.mUserExeciseCount.reset();
        this.mMathSnapshot.reset();
        this.mPhysicalSnapshot.reset();
        this.mChemicalSnapshot.reset();
        this.mScienceSnapshot.reset();
    }

    public void updateMathMap(CatalogItem catalogInfo, MappingInfoResponse response) {
        this.mUserExeciseCount.resetMathCount();
        UserStudySnapshot userStudySnapshot = this.mMathSnapshot;
        this.mMathSnapshot.mNewCatalog = catalogInfo;
        userStudySnapshot.mCurrentCatalog = catalogInfo;
        this.mMathSnapshot.mMappingInfo = response;
    }

    public void updatePhysicalMap(CatalogItem catalogInfo, MappingInfoResponse response) {
        this.mUserExeciseCount.resetPhysicalCount();
        UserStudySnapshot userStudySnapshot = this.mPhysicalSnapshot;
        this.mPhysicalSnapshot.mNewCatalog = catalogInfo;
        userStudySnapshot.mCurrentCatalog = catalogInfo;
        this.mPhysicalSnapshot.mMappingInfo = response;
    }

    public void updateChemical(CatalogItem catalogInfo, MappingInfoResponse response) {
        this.mUserExeciseCount.resetChemicalCount();
        UserStudySnapshot userStudySnapshot = this.mChemicalSnapshot;
        this.mChemicalSnapshot.mNewCatalog = catalogInfo;
        userStudySnapshot.mCurrentCatalog = catalogInfo;
        this.mChemicalSnapshot.mMappingInfo = response;
    }

    public void updateScience(CatalogItem catalogInfo, MappingInfoResponse response) {
        this.mUserExeciseCount.resetScienceCount();
        UserStudySnapshot userStudySnapshot = this.mScienceSnapshot;
        this.mScienceSnapshot.mNewCatalog = catalogInfo;
        userStudySnapshot.mCurrentCatalog = catalogInfo;
        this.mScienceSnapshot.mMappingInfo = response;
    }

    public String getUserId() {
        if (this.mUser == null) {
            return null;
        }
        return this.mUser.getUserid();
    }

    public int getGrade(int defValue) {
        return this.mUser == null ? defValue : this.mUser.getGradeCodeInt(defValue);
    }

    public String getAreaCode() {
        if (this.mUser == null) {
            return null;
        }
        return this.mUser.getAreacode();
    }

    public String getGradeCode() {
        return this.mUser.getGradecode();
    }

    public boolean isUserValid() {
        if (this.mUser != null && !TextUtils.isEmpty(this.mUser.getToken()) && this.mUser.getGradeCodeInt(-1) >= 0) {
            return true;
        }
        return false;
    }
}
