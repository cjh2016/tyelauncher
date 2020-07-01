package com.boll.tyelauncher.view.wheelview.adapter;

package com.toycloud.launcher.view.wheelview.adapter;

import android.content.Context;
import com.toycloud.launcher.view.wheelview.WheelAdapter;

public class AdapterWheel extends AbstractWheelTextAdapter {
    private WheelAdapter adapter;

    public AdapterWheel(Context context, WheelAdapter adapter2) {
        super(context);
        this.adapter = adapter2;
    }

    public WheelAdapter getAdapter() {
        return this.adapter;
    }

    public int getItemsCount() {
        return this.adapter.getItemsCount();
    }

    /* access modifiers changed from: protected */
    public CharSequence getItemText(int index) {
        return this.adapter.getItem(index);
    }
}