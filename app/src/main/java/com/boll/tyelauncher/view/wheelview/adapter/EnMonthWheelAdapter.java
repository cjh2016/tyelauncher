package com.boll.tyelauncher.view.wheelview.adapter;

package com.toycloud.launcher.view.wheelview.adapter;

import android.content.Context;

public class EnMonthWheelAdapter extends AbstractWheelTextAdapter {
    public static final int DEFAULT_MAX_VALUE = 9;
    private static final int DEFAULT_MIN_VALUE = 0;
    private String format;
    private String label;
    private int maxValue;
    private int minValue;

    protected EnMonthWheelAdapter(Context context) {
        this(context, 0, 9);
    }

    public EnMonthWheelAdapter(Context context, int minValue2, int maxValue2) {
        this(context, minValue2, maxValue2, (String) null);
    }

    public EnMonthWheelAdapter(Context context, int minValue2, int maxValue2, String format2) {
        super(context);
        this.minValue = minValue2;
        this.maxValue = maxValue2;
        this.format = format2;
    }

    /* access modifiers changed from: protected */
    public CharSequence getItemText(int index) {
        return null;
    }

    public int getItemsCount() {
        return 0;
    }
}