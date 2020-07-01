package com.boll.tyelauncher.view.wheelview;

package com.toycloud.launcher.view.wheelview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import com.toycloud.launcher.R;
import com.toycloud.launcher.view.wheelview.WheelScroller;
import com.toycloud.launcher.view.wheelview.adapter.WheelViewAdapter;
import java.util.LinkedList;
import java.util.List;

public class WheelView extends View {
    private static final int DEF_VISIBLE_ITEMS = 5;
    private static final int ITEM_OFFSET_PERCENT = 0;
    private static final int PADDING = 10;
    private int[] SHADOWS_COLORS = {-1, ViewCompat.MEASURED_SIZE_MASK, ViewCompat.MEASURED_SIZE_MASK};
    private GradientDrawable bottomShadow;
    private Drawable centerDrawable;
    private List<OnWheelChangedListener> changingListeners = new LinkedList();
    private List<OnWheelClickedListener> clickingListeners = new LinkedList();
    private int currentItem = 0;
    private DataSetObserver dataObserver = new DataSetObserver() {
        public void onChanged() {
            WheelView.this.invalidateWheel(false);
        }

        public void onInvalidated() {
            WheelView.this.invalidateWheel(true);
        }
    };
    private boolean drawShadows = true;
    private int firstItem;
    boolean isCyclic = false;
    /* access modifiers changed from: private */
    public boolean isScrollingPerformed;
    private int itemHeight = 0;
    private LinearLayout itemsLayout;
    String label = "";
    private WheelRecycle recycle = new WheelRecycle(this);
    /* access modifiers changed from: private */
    public WheelScroller scroller;
    WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
        public void onStarted() {
            boolean unused = WheelView.this.isScrollingPerformed = true;
            WheelView.this.notifyScrollingListenersAboutStart();
        }

        public void onScroll(int distance) {
            WheelView.this.doScroll(distance);
            int height = WheelView.this.getHeight();
            if (WheelView.this.scrollingOffset > height) {
                int unused = WheelView.this.scrollingOffset = height;
                WheelView.this.scroller.stopScrolling();
            } else if (WheelView.this.scrollingOffset < (-height)) {
                int unused2 = WheelView.this.scrollingOffset = -height;
                WheelView.this.scroller.stopScrolling();
            }
        }

        public void onFinished() {
            if (WheelView.this.isScrollingPerformed) {
                WheelView.this.notifyScrollingListenersAboutEnd();
                boolean unused = WheelView.this.isScrollingPerformed = false;
            }
            int unused2 = WheelView.this.scrollingOffset = 0;
            WheelView.this.invalidate();
        }

        public void onJustify() {
            if (Math.abs(WheelView.this.scrollingOffset) > 1) {
                WheelView.this.scroller.scroll(WheelView.this.scrollingOffset, 0);
            }
        }
    };
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList();
    /* access modifiers changed from: private */
    public int scrollingOffset;
    private GradientDrawable topShadow;
    private WheelViewAdapter viewAdapter;
    private int visibleItems = 5;
    private int wheelBackground = R.drawable.wheel_bg;
    private int wheelForeground = R.drawable.wheel_val;

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    private void initData(Context context) {
        this.scroller = new WheelScroller(getContext(), this.scrollingListener);
    }

    public void setInterpolator(Interpolator interpolator) {
        this.scroller.setInterpolator(interpolator);
    }

    public int getVisibleItems() {
        return this.visibleItems;
    }

    public void setVisibleItems(int count) {
        this.visibleItems = count;
    }

    public WheelViewAdapter getViewAdapter() {
        return this.viewAdapter;
    }

    public void setViewAdapter(WheelViewAdapter viewAdapter2) {
        if (this.viewAdapter != null) {
            this.viewAdapter.unregisterDataSetObserver(this.dataObserver);
        }
        this.viewAdapter = viewAdapter2;
        if (this.viewAdapter != null) {
            this.viewAdapter.registerDataSetObserver(this.dataObserver);
        }
        invalidateWheel(true);
    }

    public void addChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.add(listener);
    }

    public void removeChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.remove(listener);
    }

    /* access modifiers changed from: protected */
    public void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : this.changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    public void addScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.add(listener);
    }

    public void removeScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.remove(listener);
    }

    /* access modifiers changed from: protected */
    public void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    public void addClickingListener(OnWheelClickedListener listener) {
        this.clickingListeners.add(listener);
    }

    public void removeClickingListener(OnWheelClickedListener listener) {
        this.clickingListeners.remove(listener);
    }

    /* access modifiers changed from: protected */
    public void notifyClickListenersAboutClick(int item) {
        for (OnWheelClickedListener listener : this.clickingListeners) {
            listener.onItemClicked(this, item);
        }
    }

    public int getCurrentItem() {
        return this.currentItem;
    }

    public void setCurrentItem(int index, boolean animated) {
        int scroll;
        if (this.viewAdapter != null && this.viewAdapter.getItemsCount() != 0) {
            int itemCount = this.viewAdapter.getItemsCount();
            if (index < 0 || index >= itemCount) {
                if (this.isCyclic) {
                    while (index < 0) {
                        index += itemCount;
                    }
                    index %= itemCount;
                } else {
                    return;
                }
            }
            if (index == this.currentItem) {
                return;
            }
            if (animated) {
                int itemsToScroll = index - this.currentItem;
                if (this.isCyclic && (scroll = (Math.min(index, this.currentItem) + itemCount) - Math.max(index, this.currentItem)) < Math.abs(itemsToScroll)) {
                    itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
                }
                scroll(itemsToScroll, 0);
                return;
            }
            this.scrollingOffset = 0;
            int old = this.currentItem;
            this.currentItem = index;
            notifyChangingListeners(old, this.currentItem);
            invalidate();
        }
    }

    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    public boolean isCyclic() {
        return this.isCyclic;
    }

    public void setCyclic(boolean isCyclic2) {
        this.isCyclic = isCyclic2;
        invalidateWheel(false);
    }

    public boolean drawShadows() {
        return this.drawShadows;
    }

    public void setDrawShadows(boolean drawShadows2) {
        this.drawShadows = drawShadows2;
    }

    public void setShadowColor(int start, int middle, int end) {
        this.SHADOWS_COLORS = new int[]{start, middle, end};
    }

    public void setWheelBackground(int resource) {
        this.wheelBackground = resource;
        setBackgroundResource(this.wheelBackground);
    }

    public void setWheelForeground(int resource) {
        this.wheelForeground = resource;
        this.centerDrawable = getContext().getResources().getDrawable(this.wheelForeground);
    }

    public void invalidateWheel(boolean clearCaches) {
        if (clearCaches) {
            this.recycle.clearAll();
            if (this.itemsLayout != null) {
                this.itemsLayout.removeAllViews();
            }
            this.scrollingOffset = 0;
        } else if (this.itemsLayout != null) {
            this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
        }
        invalidate();
    }

    private void initResourcesIfNecessary() {
        if (this.centerDrawable == null) {
            this.centerDrawable = getContext().getResources().getDrawable(this.wheelForeground);
        }
        if (this.topShadow == null) {
            this.topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, this.SHADOWS_COLORS);
        }
        if (this.bottomShadow == null) {
            this.bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, this.SHADOWS_COLORS);
        }
        setBackgroundResource(this.wheelBackground);
    }

    private int getDesiredHeight(LinearLayout layout) {
        if (!(layout == null || layout.getChildAt(0) == null)) {
            this.itemHeight = layout.getChildAt(0).getMeasuredHeight();
        }
        return Math.max((this.itemHeight * this.visibleItems) - ((this.itemHeight * 0) / 50), getSuggestedMinimumHeight());
    }

    private int getItemHeight() {
        if (this.itemHeight != 0) {
            return this.itemHeight;
        }
        if (this.itemsLayout == null || this.itemsLayout.getChildAt(0) == null) {
            return getHeight() / this.visibleItems;
        }
        this.itemHeight = this.itemsLayout.getChildAt(0).getHeight();
        return this.itemHeight;
    }

    private int calculateLayoutWidth(int widthSize, int mode) {
        int width;
        initResourcesIfNecessary();
        this.itemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(widthSize, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        int width2 = this.itemsLayout.getMeasuredWidth();
        if (mode == 1073741824) {
            width = widthSize;
        } else {
            width = Math.max(width2 + 20, getSuggestedMinimumWidth());
            if (mode == Integer.MIN_VALUE && widthSize < width) {
                width = widthSize;
            }
        }
        this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(width - 20, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        return width;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        buildViewForMeasuring();
        int width = calculateLayoutWidth(widthSize, widthMode);
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = getDesiredHeight(this.itemsLayout);
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        layout(r - l, b - t);
    }

    private void layout(int width, int height) {
        this.itemsLayout.layout(0, 0, width - 20, height);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.viewAdapter != null && this.viewAdapter.getItemsCount() > 0) {
            updateView();
            drawItems(canvas);
            drawCenterRect(canvas);
        }
        if (this.drawShadows) {
            drawShadows(canvas);
        }
    }

    private void drawShadows(Canvas canvas) {
        int itemHeight2 = getItemHeight() * 3;
        this.topShadow.setBounds(0, 0, getWidth(), getHeight());
        this.topShadow.draw(canvas);
        this.bottomShadow.setBounds(0, 0, getWidth(), getHeight());
        this.bottomShadow.draw(canvas);
    }

    private void drawItems(Canvas canvas) {
        canvas.save();
        canvas.translate(10.0f, (float) ((-(((this.currentItem - this.firstItem) * getItemHeight()) + ((getItemHeight() - getHeight()) / 2))) + this.scrollingOffset));
        this.itemsLayout.draw(canvas);
        canvas.restore();
    }

    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = (int) (((double) (getItemHeight() / 2)) * 1.2d);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.province_line_border));
        paint.setStrokeWidth(2.0f);
        canvas.drawLine(0.0f, (float) (center - offset), (float) getWidth(), (float) (center - offset), paint);
        canvas.drawLine(0.0f, (float) (center + offset), (float) getWidth(), (float) (center + offset), paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int distance;
        if (!isEnabled() || getViewAdapter() == null) {
            return true;
        }
        switch (event.getAction()) {
            case 1:
                if (!this.isScrollingPerformed) {
                    int distance2 = ((int) event.getY()) - (getHeight() / 2);
                    if (distance2 > 0) {
                        distance = distance2 + (getItemHeight() / 2);
                    } else {
                        distance = distance2 - (getItemHeight() / 2);
                    }
                    int items = distance / getItemHeight();
                    if (items != 0 && isValidItemIndex(this.currentItem + items)) {
                        notifyClickListenersAboutClick(this.currentItem + items);
                        break;
                    }
                }
                break;
            case 2:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                }
                break;
        }
        return this.scroller.onTouchEvent(event);
    }

    /* access modifiers changed from: private */
    public void doScroll(int delta) {
        this.scrollingOffset += delta;
        int itemHeight2 = getItemHeight();
        int count = this.scrollingOffset / itemHeight2;
        int pos = this.currentItem - count;
        int itemCount = this.viewAdapter.getItemsCount();
        int fixPos = this.scrollingOffset % itemHeight2;
        if (Math.abs(fixPos) <= itemHeight2 / 2) {
            fixPos = 0;
        }
        if (this.isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--;
                count++;
            } else if (fixPos < 0) {
                pos++;
                count--;
            }
            while (pos < 0) {
                pos += itemCount;
            }
            pos %= itemCount;
        } else if (pos < 0) {
            count = this.currentItem;
            pos = 0;
        } else if (pos >= itemCount) {
            count = (this.currentItem - itemCount) + 1;
            pos = itemCount - 1;
        } else if (pos > 0 && fixPos > 0) {
            pos--;
            count++;
        } else if (pos < itemCount - 1 && fixPos < 0) {
            pos++;
            count--;
        }
        int offset = this.scrollingOffset;
        if (pos != this.currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }
        this.scrollingOffset = offset - (count * itemHeight2);
        if (this.scrollingOffset > getHeight()) {
            this.scrollingOffset = (this.scrollingOffset % getHeight()) + getHeight();
        }
    }

    public void scroll(int itemsToScroll, int time) {
        this.scroller.scroll((getItemHeight() * itemsToScroll) - this.scrollingOffset, time);
    }

    private ItemsRange getItemsRange() {
        if (getItemHeight() == 0) {
            return null;
        }
        int first = this.currentItem;
        int count = 1;
        while (getItemHeight() * count < getHeight()) {
            first--;
            count += 2;
        }
        if (this.scrollingOffset != 0) {
            if (this.scrollingOffset > 0) {
                first--;
            }
            int emptyItems = this.scrollingOffset / getItemHeight();
            first -= emptyItems;
            count = (int) (((double) (count + 1)) + Math.asin((double) emptyItems));
        }
        return new ItemsRange(first, count);
    }

    private boolean rebuildItems() {
        boolean updated;
        ItemsRange range = getItemsRange();
        if (this.itemsLayout != null) {
            int first = this.recycle.recycleItems(this.itemsLayout, this.firstItem, range);
            if (this.firstItem != first) {
                updated = true;
            } else {
                updated = false;
            }
            this.firstItem = first;
        } else {
            createItemsLayout();
            updated = true;
        }
        if (!updated) {
            if (this.firstItem == range.getFirst() && this.itemsLayout.getChildCount() == range.getCount()) {
                updated = false;
            } else {
                updated = true;
            }
        }
        if (this.firstItem <= range.getFirst() || this.firstItem > range.getLast()) {
            this.firstItem = range.getFirst();
        } else {
            int i = this.firstItem - 1;
            while (i >= range.getFirst() && addViewItem(i, true)) {
                this.firstItem = i;
                i--;
            }
        }
        int first2 = this.firstItem;
        for (int i2 = this.itemsLayout.getChildCount(); i2 < range.getCount(); i2++) {
            if (!addViewItem(this.firstItem + i2, false) && this.itemsLayout.getChildCount() == 0) {
                first2++;
            }
        }
        this.firstItem = first2;
        return updated;
    }

    private void updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(getWidth(), 1073741824);
            layout(getWidth(), getHeight());
        }
    }

    private void createItemsLayout() {
        if (this.itemsLayout == null) {
            this.itemsLayout = new LinearLayout(getContext());
            this.itemsLayout.setOrientation(1);
        }
    }

    private void buildViewForMeasuring() {
        if (this.itemsLayout != null) {
            this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
        } else {
            createItemsLayout();
        }
        int addItems = this.visibleItems / 2;
        for (int i = this.currentItem + addItems; i >= this.currentItem - addItems; i--) {
            if (addViewItem(i, true)) {
                this.firstItem = i;
            }
        }
    }

    private boolean addViewItem(int index, boolean first) {
        View view = getItemView(index);
        if (view == null) {
            return false;
        }
        if (first) {
            this.itemsLayout.addView(view, 0);
        } else {
            this.itemsLayout.addView(view);
        }
        return true;
    }

    private boolean isValidItemIndex(int index) {
        return this.viewAdapter != null && this.viewAdapter.getItemsCount() > 0 && (this.isCyclic || (index >= 0 && index < this.viewAdapter.getItemsCount()));
    }

    private View getItemView(int index) {
        if (this.viewAdapter == null || this.viewAdapter.getItemsCount() == 0) {
            return null;
        }
        int count = this.viewAdapter.getItemsCount();
        if (!isValidItemIndex(index)) {
            return this.viewAdapter.getEmptyItem(this.recycle.getEmptyItem(), this.itemsLayout);
        }
        while (index < 0) {
            index += count;
        }
        return this.viewAdapter.getItem(index % count, this.recycle.getItem(), this.itemsLayout);
    }

    public void stopScrolling() {
        this.scroller.stopScrolling();
    }
}
