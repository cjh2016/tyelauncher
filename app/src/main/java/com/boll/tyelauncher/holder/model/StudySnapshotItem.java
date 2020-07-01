package com.boll.tyelauncher.holder.model;

package com.toycloud.launcher.holder.model;

import com.iflytek.cbg.common.utils.NumberUtils;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.holder.presenter.CatalogItem;
import java.util.Map;

public class StudySnapshotItem {
    public final CatalogItem mCatalogItem;
    public final MappingInfoResponse mMappingInfo;
    public final String mSubjectCode;
    public final String mUserId;
    public final long mVersion;

    public StudySnapshotItem(String userId, String subjectCode, CatalogItem item, MappingInfoResponse response, long version) {
        this.mUserId = userId;
        this.mSubjectCode = subjectCode;
        this.mCatalogItem = item;
        this.mMappingInfo = response;
        this.mVersion = version;
    }

    public static StudySnapshotItem findBySubject(Map<String, StudySnapshotItem> items, String subject) {
        if (items == null) {
            return null;
        }
        return items.get(subject);
    }

    public boolean isMiddleSchool() {
        if (this.mCatalogItem != null && NumberUtils.parseInt(this.mCatalogItem.gradeCode, -1) >= 7) {
            return true;
        }
        return false;
    }
}
