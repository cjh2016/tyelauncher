package com.boll.tyelauncher.view.wheelview.adapter;

package com.toycloud.launcher.view.wheelview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public abstract class AbstractWheelTextAdapter1 extends AbstractWheelAdapter {
    public static final int DEFAULT_TEXT_COLOR = -15724528;
    public static final int DEFAULT_TEXT_SIZE = 24;
    public static final int LABEL_COLOR = -9437072;
    protected static final int NO_RESOURCE = 0;
    public static final int TEXT_VIEW_ITEM_RESOURCE = -1;
    private static int maxsize = 14;
    private static int minsize = 12;
    private ArrayList<View> arrayList;
    protected Context context;
    private int currentIndex;
    protected int emptyItemResourceId;
    protected LayoutInflater inflater;
    protected int itemResourceId;
    protected int itemTextResourceId;
    private int textColor;
    private int textSize;

    /* access modifiers changed from: protected */
    public abstract CharSequence getItemText(int i);

    protected AbstractWheelTextAdapter1(Context context2) {
        this(context2, -1);
    }

    protected AbstractWheelTextAdapter1(Context context2, int itemResource) {
        this(context2, itemResource, 0, 0, maxsize, minsize);
    }

    protected AbstractWheelTextAdapter1(Context context2, int itemResource, int itemTextResource, int currentIndex2, int maxsize2, int minsize2) {
        this.textColor = -15724528;
        this.textSize = 24;
        this.currentIndex = 0;
        this.arrayList = new ArrayList<>();
        this.context = context2;
        this.itemResourceId = itemResource;
        this.itemTextResourceId = itemTextResource;
        this.currentIndex = currentIndex2;
        maxsize = maxsize2;
        minsize = minsize2;
        this.inflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public ArrayList<View> getTestViews() {
        return this.arrayList;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor2) {
        this.textColor = textColor2;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize2) {
        this.textSize = textSize2;
    }

    public int getItemResource() {
        return this.itemResourceId;
    }

    public void setItemResource(int itemResourceId2) {
        this.itemResourceId = itemResourceId2;
    }

    public int getItemTextResource() {
        return this.itemTextResourceId;
    }

    public void setItemTextResource(int itemTextResourceId2) {
        this.itemTextResourceId = itemTextResourceId2;
    }

    public int getEmptyItemResource() {
        return this.emptyItemResourceId;
    }

    public void setEmptyItemResource(int emptyItemResourceId2) {
        this.emptyItemResourceId = emptyItemResourceId2;
    }

    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        if (convertView == null) {
            convertView = getView(this.itemResourceId, parent);
        }
        TextView textView = getTextView(convertView, this.itemTextResourceId);
        if (!this.arrayList.contains(textView)) {
            this.arrayList.add(textView);
        }
        if (textView != null) {
            CharSequence text = getItemText(index);
            if (text == null) {
                text = "";
            }
            textView.setText(text);
            if (index == this.currentIndex) {
                textView.setTextSize((float) maxsize);
                textView.setTextColor(Color.parseColor("#0578FA"));
            } else {
                textView.setTextSize((float) minsize);
                textView.setTextColor(Color.parseColor("#666666"));
            }
            if (this.itemResourceId == -1) {
                configureTextView(textView);
            }
        }
        return convertView;
    }

    @Override
    public View getEmptyItem(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(this.emptyItemResourceId, parent);
        }
        if (this.emptyItemResourceId == -1 && (convertView instanceof TextView)) {
            configureTextView((TextView) convertView);
        }
        return convertView;
    }

    /* access modifiers changed from: protected */
    public void configureTextView(TextView view) {
        view.setTextColor(this.textColor);
        view.setGravity(17);
        view.setTextSize((float) this.textSize);
        view.setLines(1);
        view.setTypeface(Typeface.SANS_SERIF, 1);
    }

    private TextView getTextView(View view, int textResource) {
        if (textResource == 0) {
            try {
                if (view instanceof TextView) {
                    return (TextView) view;
                }
            } catch (ClassCastException e) {
                Log.e("AbstractWheelAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException("AbstractWheelAdapter requires the resource ID to be a TextView", e);
            }
        }
        if (textResource != 0) {
            return (TextView) view.findViewById(textResource);
        }
        return null;
    }

    private View getView(int resource, ViewGroup parent) {
        switch (resource) {
            case -1:
                return new TextView(this.context);
            case 0:
                return null;
            default:
                return this.inflater.inflate(resource, parent, false);
        }
    }
}
