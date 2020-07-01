package com.boll.tyelauncher.widget;

package com.toycloud.launcher.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.anarchy.classify.simple.ChangeInfo;
import com.toycloud.launcher.R;

public class IReaderGridLayout extends ViewGroup {
    private int mColumnCount;
    private int mGap;
    private int mRowCount;

    public IReaderGridLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public IReaderGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IReaderGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IReaderGridLayout, defStyleAttr, 0);
        this.mRowCount = a.getInt(0, 2);
        this.mColumnCount = a.getInt(1, 2);
        this.mGap = a.getDimensionPixelSize(2, 0);
        a.recycle();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == Integer.MIN_VALUE || heightMode == Integer.MIN_VALUE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int suggestWidth = (((widthSize - getPaddingLeft()) - getPaddingRight()) - ((this.mColumnCount - 1) * this.mGap)) / this.mColumnCount;
        int suggestHeight = (((heightSize - getPaddingTop()) - getPaddingBottom()) - ((this.mRowCount - 1) * this.mGap)) / this.mRowCount;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(View.MeasureSpec.makeMeasureSpec(suggestWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(suggestHeight, 1073741824));
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int left = getPaddingLeft() + ((i % this.mColumnCount) * (child.getMeasuredWidth() + this.mGap));
            int top2 = getPaddingTop() + ((i / this.mRowCount) * (child.getMeasuredHeight() + this.mGap));
            child.layout(left, top2, child.getMeasuredWidth() + left, child.getMeasuredHeight() + top2);
        }
    }

    /* access modifiers changed from: package-private */
    public ChangeInfo getChangeInfo() {
        ChangeInfo info = new ChangeInfo();
        info.targetLeft = getPaddingLeft();
        info.targetTop = getPaddingTop();
        info.targetWidth = (float) ((((getWidth() - getPaddingLeft()) - getPaddingRight()) - ((this.mColumnCount - 1) * this.mGap)) / this.mColumnCount);
        info.targetHeight = (float) ((((getHeight() - getPaddingTop()) - getPaddingBottom()) - ((this.mRowCount - 1) * this.mGap)) / this.mRowCount);
        return info;
    }

    /* access modifiers changed from: package-private */
    public ChangeInfo getSecondItemChangeInfo() {
        ChangeInfo info = new ChangeInfo();
        info.targetWidth = (float) ((((getWidth() - getPaddingLeft()) - getPaddingRight()) - ((this.mColumnCount - 1) * this.mGap)) / this.mColumnCount);
        info.targetHeight = (float) ((((getHeight() - getPaddingTop()) - getPaddingBottom()) - ((this.mRowCount - 1) * this.mGap)) / this.mRowCount);
        info.targetLeft = (int) (((float) getPaddingLeft()) + info.targetWidth + ((float) this.mGap));
        info.targetTop = getPaddingTop();
        return info;
    }
}
