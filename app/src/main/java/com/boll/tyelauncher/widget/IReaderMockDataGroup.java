package com.boll.tyelauncher.widget;

package com.toycloud.launcher.widget;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class IReaderMockDataGroup extends IReaderMockData {
    private String mCategory;
    private List<IReaderMockData> mChild = new ArrayList();

    public void addChild(@NonNull IReaderMockData iReaderMockData) {
        iReaderMockData.setParent(this);
        this.mChild.add(iReaderMockData);
    }

    public void addChild(int location, @NonNull IReaderMockData iReaderMockData) {
        iReaderMockData.setParent(this);
        this.mChild.add(location, iReaderMockData);
    }

    public IReaderMockData removeChild(int location) {
        IReaderMockData mockData = this.mChild.remove(location);
        mockData.setParent((IReaderMockDataGroup) null);
        return mockData;
    }

    public boolean removeChild(@NonNull IReaderMockData iReaderMockData) {
        iReaderMockData.setParent((IReaderMockDataGroup) null);
        return this.mChild.remove(iReaderMockData);
    }

    public int getChildCount() {
        return this.mChild.size();
    }

    public IReaderMockData getChild(int position) {
        return this.mChild.get(position);
    }

    public String getCategory() {
        return this.mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public int getCheckedCount() {
        if (this.mChild == null) {
            return 0;
        }
        int i = 0;
        for (IReaderMockData data : this.mChild) {
            if (data.isChecked()) {
                i++;
            }
        }
        return i;
    }
}
