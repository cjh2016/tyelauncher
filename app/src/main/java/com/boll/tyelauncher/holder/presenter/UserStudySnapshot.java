package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.text.TextUtils;
import com.toycloud.launcher.api.response.MappingInfoResponse;

public class UserStudySnapshot {
    public CatalogItem mCurrentCatalog;
    public MappingInfoResponse mMappingInfo;
    public CatalogItem mNewCatalog;

    public boolean hasNewCatalog() {
        if (this.mNewCatalog != null && this.mCurrentCatalog != null) {
            String newPhaseCode = this.mNewCatalog.phaseCode;
            String curPhaseCode = this.mCurrentCatalog.phaseCode;
            String newChapterCode = this.mNewCatalog.chapterCode;
            String curChapterCode = this.mCurrentCatalog.chapterCode;
            if (!TextUtils.equals(newPhaseCode, curPhaseCode) || !TextUtils.equals(newChapterCode, curChapterCode)) {
                return true;
            }
            return false;
        } else if (this.mMappingInfo != null || this.mCurrentCatalog == null) {
            return false;
        } else {
            return true;
        }
    }

    public void reset() {
        this.mCurrentCatalog = null;
        this.mNewCatalog = null;
        this.mMappingInfo = null;
    }
}
