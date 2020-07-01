package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.toycloud.launcher.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LoopView extends View {
    public static final int MSG_INVALIDATE = 1000;
    public static final int MSG_SCROLL_LOOP = 2000;
    public static final int MSG_SELECTED_ITEM = 3000;
    /* access modifiers changed from: private */
    public static final String TAG = LoopView.class.getSimpleName();
    public static final int TYPEDAY = 3;
    public static final int TYPEMONTH = 2;
    public static final int TYPEYEAR = 1;
    /* access modifiers changed from: private */
    public float lineSpacingMultiplier;
    private int mBottomLineY;
    /* access modifiers changed from: private */
    public boolean mCanLoop;
    private int mCenterLineColor;
    private Paint mCenterLinePaint;
    private int mCenterTextColor;
    private Paint mCenterTextPaint;
    private int mCircularDiameter;
    private int mCircularRadius;
    private Context mContext;
    private int mCurrentIndex;
    /* access modifiers changed from: private */
    public ArrayList mDataList;
    private int mDrawItemsCount;
    private ScheduledExecutorService mExecutor;
    private GestureDetector mGestureDetector;
    public Handler mHandler;
    /* access modifiers changed from: private */
    public int mInitPosition;
    /* access modifiers changed from: private */
    public float mItemHeight;
    /* access modifiers changed from: private */
    public LoopScrollListener mLoopListener;
    /* access modifiers changed from: private */
    public int mMaxTextHeight;
    private int mMaxTextWidth;
    private GestureDetector.SimpleOnGestureListener mOnGestureListener;
    private int mPaddingLeftRight;
    private int mPaddingTopBottom;
    private ScheduledFuture<?> mScheduledFuture;
    private int mSelectedItem;
    private int mTextSize;
    private int mTopBottomTextColor;
    private Paint mTopBottomTextPaint;
    private int mTopLineY;
    /* access modifiers changed from: private */
    public int mTotalScrollY;
    private int mWidgetHeight;
    private int mWidgetWidth;
    private int type;

    public LoopView(Context context) {
        this(context, (AttributeSet) null);
    }

    public LoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mHandler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                if (msg.what == 1000) {
                    LoopView.this.invalidate();
                }
                if (msg.what == 2000) {
                    LoopView.this.startSmoothScrollTo();
                    return false;
                } else if (msg.what != 3000) {
                    return false;
                } else {
                    LoopView.this.itemSelected();
                    return false;
                }
            }
        });
        initView(context, attrs);
    }

    @TargetApi(21)
    public LoopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mHandler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                if (msg.what == 1000) {
                    LoopView.this.invalidate();
                }
                if (msg.what == 2000) {
                    LoopView.this.startSmoothScrollTo();
                    return false;
                } else if (msg.what != 3000) {
                    return false;
                } else {
                    LoopView.this.itemSelected();
                    return false;
                }
            }
        });
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoopView);
        if (array != null) {
            this.mTopBottomTextColor = array.getColor(1, -5263441);
            this.mCenterTextColor = array.getColor(2, -16740610);
            this.mCenterLineColor = array.getColor(0, -1);
            this.mCanLoop = array.getBoolean(4, false);
            this.mInitPosition = array.getInt(5, -1);
            this.mTextSize = array.getDimensionPixelSize(3, sp2px(context, 16.0f));
            this.mDrawItemsCount = array.getInt(6, 7);
            this.type = array.getInt(7, 1);
            array.recycle();
        }
        this.lineSpacingMultiplier = 2.0f;
        this.mContext = context;
        this.mOnGestureListener = new LoopViewGestureListener();
        this.mTopBottomTextPaint = new Paint();
        this.mCenterTextPaint = new Paint();
        this.mCenterLinePaint = new Paint();
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, (Paint) null);
        }
        this.mGestureDetector = new GestureDetector(context, this.mOnGestureListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
    }

    private void initData() {
        if (this.mDataList == null) {
            throw new IllegalArgumentException("data list must not be null!");
        }
        this.mTopBottomTextPaint.setColor(this.mTopBottomTextColor);
        this.mTopBottomTextPaint.setAntiAlias(true);
        this.mTopBottomTextPaint.setTypeface(Typeface.MONOSPACE);
        this.mTopBottomTextPaint.setTextSize((float) this.mTextSize);
        this.mCenterTextPaint.setColor(this.mCenterTextColor);
        this.mCenterTextPaint.setAntiAlias(true);
        this.mCenterTextPaint.setTextScaleX(1.05f);
        this.mCenterTextPaint.setTypeface(Typeface.MONOSPACE);
        this.mCenterTextPaint.setTextSize((float) this.mTextSize);
        this.mCenterLinePaint.setColor(this.mCenterLineColor);
        this.mCenterLinePaint.setAntiAlias(true);
        this.mCenterLinePaint.setTypeface(Typeface.MONOSPACE);
        this.mCenterLinePaint.setTextSize((float) this.mTextSize);
        measureTextWidthHeight();
        int mHalfCircumference = (int) (((float) this.mMaxTextHeight) * this.lineSpacingMultiplier * ((float) (this.mDrawItemsCount - 1)));
        this.mCircularDiameter = (int) (((double) (mHalfCircumference * 2)) / 3.141592653589793d);
        this.mCircularRadius = (int) (((double) mHalfCircumference) / 3.141592653589793d);
        if (this.mInitPosition == -1) {
            if (this.mCanLoop) {
                this.mInitPosition = (this.mDataList.size() + 1) / 2;
            } else {
                this.mInitPosition = 0;
            }
        }
        this.mCurrentIndex = this.mInitPosition;
        invalidate();
    }

    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < this.mDataList.size(); i++) {
            String s1 = (String) this.mDataList.get(i);
            this.mCenterTextPaint.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > this.mMaxTextWidth) {
                this.mMaxTextWidth = textWidth + 10;
            }
            int textHeight = rect.height();
            Log.e("mMaxTextHeight--->1", "mMaxTextHeight:" + this.mMaxTextHeight + "textHeight:" + textHeight);
            this.mMaxTextHeight = 25;
            Log.e("mMaxTextHeight--->2", "mMaxTextHeight:" + this.mMaxTextHeight + "textHeight:" + textHeight);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidgetWidth = getMeasuredWidth();
        this.mWidgetHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure -> heightMode:" + View.MeasureSpec.getMode(heightMeasureSpec));
        this.mItemHeight = this.lineSpacingMultiplier * ((float) this.mMaxTextHeight);
        this.mPaddingLeftRight = this.mWidgetWidth - this.mMaxTextWidth;
        this.mPaddingTopBottom = (this.mWidgetHeight - this.mCircularDiameter) / 2;
        this.mTopLineY = ((int) ((((float) this.mCircularDiameter) - this.mItemHeight) / 2.0f)) + this.mPaddingTopBottom;
        this.mBottomLineY = ((int) ((((float) this.mCircularDiameter) + this.mItemHeight) / 2.0f)) + this.mPaddingTopBottom;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDataList == null) {
            super.onDraw(canvas);
            return;
        }
        super.onDraw(canvas);
        this.mCurrentIndex = this.mInitPosition + (((int) (((float) this.mTotalScrollY) / this.mItemHeight)) % this.mDataList.size());
        if (!this.mCanLoop) {
            if (this.mCurrentIndex < 0) {
                this.mCurrentIndex = 0;
            }
            if (this.mCurrentIndex > this.mDataList.size() - 1) {
                this.mCurrentIndex = this.mDataList.size() - 1;
            }
        } else {
            if (this.mCurrentIndex < 0) {
                this.mCurrentIndex = this.mDataList.size() + this.mCurrentIndex;
            }
            if (this.mCurrentIndex > this.mDataList.size() - 1) {
                this.mCurrentIndex -= this.mDataList.size();
            }
        }
        String[] itemCount = new String[this.mDrawItemsCount];
        for (int count = 0; count < this.mDrawItemsCount; count++) {
            int templateItem = this.mCurrentIndex - ((this.mDrawItemsCount / 2) - count);
            if (this.mCanLoop) {
                if (templateItem < 0) {
                    templateItem += this.mDataList.size();
                }
                if (templateItem > this.mDataList.size() - 1) {
                    templateItem -= this.mDataList.size();
                }
                itemCount[count] = (String) this.mDataList.get(templateItem);
            } else if (templateItem < 0) {
                itemCount[count] = "";
            } else if (templateItem > this.mDataList.size() - 1) {
                itemCount[count] = "";
            } else {
                itemCount[count] = (String) this.mDataList.get(templateItem);
            }
        }
        canvas.drawLine(0.0f, (float) this.mTopLineY, (float) this.mWidgetWidth, (float) this.mTopLineY, this.mCenterLinePaint);
        canvas.drawLine(0.0f, (float) this.mBottomLineY, (float) this.mWidgetWidth, (float) this.mBottomLineY, this.mCenterLinePaint);
        int changingLeftY = (int) (((float) this.mTotalScrollY) % this.mItemHeight);
        for (int count2 = 0; count2 < this.mDrawItemsCount; count2++) {
            canvas.save();
            float itemHeight = ((float) this.mMaxTextHeight) * this.lineSpacingMultiplier;
            double radian = (double) (((((float) count2) * itemHeight) - ((float) changingLeftY)) / ((float) this.mCircularRadius));
            float angle = (float) ((180.0d * radian) / 3.141592653589793d);
            if (angle >= 180.0f || angle <= 0.0f) {
                canvas.restore();
            } else {
                int translateY = ((int) ((((double) this.mCircularRadius) - (Math.cos(radian) * ((double) this.mCircularRadius))) - ((Math.sin(radian) * ((double) this.mMaxTextHeight)) / 2.0d))) + this.mPaddingTopBottom;
                canvas.translate(0.0f, (float) translateY);
                canvas.scale(1.0f, (float) Math.sin(radian));
                if (translateY <= this.mTopLineY) {
                    canvas.save();
                    canvas.clipRect(0, 0, this.mWidgetWidth, this.mTopLineY - translateY);
                    canvas.drawText(itemCount[count2], (float) this.mPaddingLeftRight, (float) this.mMaxTextHeight, this.mTopBottomTextPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, this.mTopLineY - translateY, this.mWidgetWidth, (int) itemHeight);
                    canvas.drawText(itemCount[count2], (float) this.mPaddingLeftRight, (float) this.mMaxTextHeight, this.mCenterTextPaint);
                    canvas.restore();
                } else if (this.mMaxTextHeight + translateY >= this.mBottomLineY) {
                    canvas.save();
                    canvas.clipRect(0, 0, this.mWidgetWidth, this.mBottomLineY - translateY);
                    canvas.drawText(itemCount[count2], (float) this.mPaddingLeftRight, (float) this.mMaxTextHeight, this.mCenterTextPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, this.mBottomLineY - translateY, this.mWidgetWidth, (int) itemHeight);
                    canvas.drawText(itemCount[count2], (float) this.mPaddingLeftRight, (float) this.mMaxTextHeight, this.mTopBottomTextPaint);
                    canvas.restore();
                } else if (translateY >= this.mTopLineY && this.mMaxTextHeight + translateY <= this.mBottomLineY) {
                    canvas.clipRect(0, 0, this.mWidgetWidth, (int) itemHeight);
                    canvas.drawText(itemCount[count2], (float) this.mPaddingLeftRight, (float) this.mMaxTextHeight, this.mCenterTextPaint);
                    this.mSelectedItem = this.mDataList.indexOf(itemCount[count2]);
                }
                canvas.restore();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionevent) {
        motionevent.getAction();
        if (this.mGestureDetector.onTouchEvent(motionevent)) {
            return true;
        }
        startSmoothScrollTo();
        return true;
    }

    public final void setCanLoop(boolean canLoop) {
        this.mCanLoop = canLoop;
        invalidate();
    }

    public final void setTextSize(float size) {
        if (size > 0.0f) {
            this.mTextSize = sp2px(this.mContext, size);
        }
    }

    public void setInitPosition(int initPosition) {
        this.mInitPosition = initPosition;
        invalidate();
    }

    public void setLoopListener(LoopScrollListener LoopListener) {
        this.mLoopListener = LoopListener;
    }

    public final void setDataList(List<String> list) {
        this.mDataList = (ArrayList) list;
        initData();
    }

    public int getSelectedItem() {
        return this.mSelectedItem;
    }

    /* access modifiers changed from: private */
    public void itemSelected() {
        if (this.mLoopListener != null) {
            postDelayed(new SelectedRunnable(), 200);
        }
    }

    /* access modifiers changed from: private */
    public void cancelSchedule() {
        if (this.mScheduledFuture != null && !this.mScheduledFuture.isCancelled()) {
            this.mScheduledFuture.cancel(true);
            this.mScheduledFuture = null;
        }
    }

    /* access modifiers changed from: private */
    public void startSmoothScrollTo() {
        int offset = (int) (((float) this.mTotalScrollY) % this.mItemHeight);
        cancelSchedule();
        this.mScheduledFuture = this.mExecutor.scheduleWithFixedDelay(new HalfHeightRunnable(offset), 0, 10, TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: private */
    public void startSmoothScrollTo(float velocityY) {
        cancelSchedule();
        this.mScheduledFuture = this.mExecutor.scheduleWithFixedDelay(new FlingRunnable(velocityY), 0, (long) 20, TimeUnit.MILLISECONDS);
    }

    class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        LoopViewGestureListener() {
        }

        public final boolean onDown(MotionEvent motionevent) {
            LoopView.this.cancelSchedule();
            Log.i(LoopView.TAG, "LoopViewGestureListener->onDown");
            return true;
        }

        public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LoopView.this.startSmoothScrollTo(velocityY);
            Log.i(LoopView.TAG, "LoopViewGestureListener->onFling");
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(LoopView.TAG, "LoopViewGestureListener->onScroll");
            int unused = LoopView.this.mTotalScrollY = (int) (((float) LoopView.this.mTotalScrollY) + distanceY);
            if (!LoopView.this.mCanLoop) {
                int initPositionStartY = ((int) (((float) LoopView.this.mInitPosition) * LoopView.this.mItemHeight)) * -1;
                if (LoopView.this.mTotalScrollY < initPositionStartY) {
                    int unused2 = LoopView.this.mTotalScrollY = initPositionStartY;
                }
                int circleLength = (int) (((float) ((LoopView.this.mDataList.size() - 1) - LoopView.this.mInitPosition)) * LoopView.this.mItemHeight);
                if (LoopView.this.mTotalScrollY >= circleLength) {
                    int unused3 = LoopView.this.mTotalScrollY = circleLength;
                }
            }
            LoopView.this.invalidate();
            return true;
        }
    }

    public int sp2px(Context context, float spValue) {
        return (int) ((spValue * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    class SelectedRunnable implements Runnable {
        SelectedRunnable() {
        }

        public final void run() {
            LoopScrollListener listener = LoopView.this.mLoopListener;
            int selectedItem = LoopView.this.getSelectedItem();
            LoopView.this.mDataList.get(selectedItem);
            listener.onItemSelect(selectedItem);
        }
    }

    class HalfHeightRunnable implements Runnable {
        int offset;
        int realOffset = 0;
        int realTotalOffset = Integer.MAX_VALUE;

        public HalfHeightRunnable(int offset2) {
            this.offset = offset2;
        }

        public void run() {
            if (this.realTotalOffset == Integer.MAX_VALUE) {
                if (((float) this.offset) > LoopView.this.mItemHeight / 2.0f) {
                    this.realTotalOffset = (int) (LoopView.this.mItemHeight - ((float) this.offset));
                } else {
                    this.realTotalOffset = -this.offset;
                }
            }
            this.realOffset = (int) (((float) this.realTotalOffset) * 0.1f);
            if (this.realOffset == 0) {
                if (this.realTotalOffset < 0) {
                    this.realOffset = -1;
                } else {
                    this.realOffset = 1;
                }
            }
            if (Math.abs(this.realTotalOffset) <= 0) {
                LoopView.this.cancelSchedule();
                LoopView.this.mHandler.sendEmptyMessage(3000);
                return;
            }
            int unused = LoopView.this.mTotalScrollY = LoopView.this.mTotalScrollY + this.realOffset;
            LoopView.this.mHandler.sendEmptyMessage(1000);
            this.realTotalOffset -= this.realOffset;
        }
    }

    class FlingRunnable implements Runnable {
        float velocity = 2.14748365E9f;
        final float velocityY;

        FlingRunnable(float velocityY2) {
            this.velocityY = velocityY2;
        }

        public void run() {
            if (this.velocity == 2.14748365E9f) {
                if (Math.abs(this.velocityY) <= 2000.0f) {
                    this.velocity = this.velocityY;
                } else if (this.velocityY > 0.0f) {
                    this.velocity = 2000.0f;
                } else {
                    this.velocity = -2000.0f;
                }
            }
            Log.i(LoopView.TAG, "velocity->" + this.velocity);
            if (Math.abs(this.velocity) < 0.0f || Math.abs(this.velocity) > 20.0f) {
                int unused = LoopView.this.mTotalScrollY = LoopView.this.mTotalScrollY - ((int) ((this.velocity * 10.0f) / 1000.0f));
                if (!LoopView.this.mCanLoop) {
                    float itemHeight = LoopView.this.lineSpacingMultiplier * ((float) LoopView.this.mMaxTextHeight);
                    if (LoopView.this.mTotalScrollY <= ((int) (((float) (-LoopView.this.mInitPosition)) * itemHeight))) {
                        this.velocity = 40.0f;
                        int unused2 = LoopView.this.mTotalScrollY = (int) (((float) (-LoopView.this.mInitPosition)) * itemHeight);
                    } else if (LoopView.this.mTotalScrollY >= ((int) (((float) ((LoopView.this.mDataList.size() - 1) - LoopView.this.mInitPosition)) * itemHeight))) {
                        int unused3 = LoopView.this.mTotalScrollY = (int) (((float) ((LoopView.this.mDataList.size() - 1) - LoopView.this.mInitPosition)) * itemHeight);
                        this.velocity = -40.0f;
                    }
                }
                if (this.velocity < 0.0f) {
                    this.velocity += 20.0f;
                } else {
                    this.velocity -= 20.0f;
                }
                LoopView.this.mHandler.sendEmptyMessage(1000);
                return;
            }
            LoopView.this.cancelSchedule();
            LoopView.this.mHandler.sendEmptyMessage(LoopView.MSG_SCROLL_LOOP);
        }
    }
}
