package com.boll.tyelauncher.view.wheelview.adapter;

package com.toycloud.launcher.view.wheelview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NumericWheelAdapter extends AbstractWheelTextAdapter {
    public static final int DEFAULT_MAX_VALUE = 9;
    private static final int DEFAULT_MIN_VALUE = 0;
    private String format;
    private String label;
    private int maxValue;
    private int minValue;

    public NumericWheelAdapter(Context context) {
        this(context, 0, 9);
    }

    public NumericWheelAdapter(Context context, int minValue2, int maxValue2) {
        this(context, minValue2, maxValue2, (String) null);
    }

    public NumericWheelAdapter(Context context, int minValue2, int maxValue2, String format2) {
        super(context);
        this.minValue = minValue2;
        this.maxValue = maxValue2;
        this.format = format2;
    }

    public CharSequence getItemText(int index) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        int value = this.minValue + index;
        if (this.format == null) {
            return Integer.toString(value);
        }
        return String.format(this.format, new Object[]{Integer.valueOf(value)});
    }

    public int getItemsCount() {
        return (this.maxValue - this.minValue) + 1;
    }

    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        if (convertView == null) {
            convertView = getView(this.itemResourceId, parent);
        }
        TextView textView = getTextView(convertView, this.itemTextResourceId);
        if (textView != null) {
            CharSequence text = getItemText(index);
            if (text == null) {
                text = "";
            }
            textView.setText(text + this.label);
            if (this.itemResourceId == -1) {
                configureTextView(textView);
            }
        }
        return convertView;
    }

    public void setLabel(String label2) {
        this.label = label2;
    }
}
