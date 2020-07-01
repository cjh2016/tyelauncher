package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

@SuppressLint({"NewApi"})
public class ParallaxViewPager extends ViewPager {
    private static final float CORRECTION_PERCENTAGE = 0.01f;
    public static final int FIT_HEIGHT = 1;
    public static final int FIT_WIDTH = 0;
    public static final float OVERLAP_FULL = 1.0f;
    public static final float OVERLAP_HALF = 0.5f;
    public static final float OVERLAP_QUARTER = 0.25f;
    public Bitmap bitmap;
    /* access modifiers changed from: private */
    public int chunkWidth;
    /* access modifiers changed from: private */
    public Rect destination;
    private float overlap;
    private int preX = 0;
    /* access modifiers changed from: private */
    public int projectedWidth;
    private int scaleType;
    private boolean scrollable = true;
    /* access modifiers changed from: private */
    public ViewPager.OnPageChangeListener secondOnPageChangeListener;
    /* access modifiers changed from: private */
    public Rect source;
    /* access modifiers changed from: private */
    public ViewPager.OnPageChangeListener thirdOnPageChangeListener;

    public void setScrollable(boolean scrollable2) {
        this.scrollable = scrollable2;
    }

    public ParallaxViewPager(Context context) {
        super(context);
        init();
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.source = new Rect();
        this.destination = new Rect();
        this.scaleType = 1;
        this.overlap = 0.5f;
        setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (ParallaxViewPager.this.bitmap != null) {
                    ParallaxViewPager.this.source.left = (int) Math.floor((double) (((((float) position) + positionOffset) - ParallaxViewPager.CORRECTION_PERCENTAGE) * ((float) ParallaxViewPager.this.chunkWidth)));
                    ParallaxViewPager.this.source.right = (int) Math.ceil((double) (((((float) position) + positionOffset + ParallaxViewPager.CORRECTION_PERCENTAGE) * ((float) ParallaxViewPager.this.chunkWidth)) + ((float) ParallaxViewPager.this.projectedWidth)));
                    ParallaxViewPager.this.destination.left = (int) Math.floor((double) (((((float) position) + positionOffset) - ParallaxViewPager.CORRECTION_PERCENTAGE) * ((float) ParallaxViewPager.this.getWidth())));
                    ParallaxViewPager.this.destination.right = (int) Math.ceil((double) ((((float) position) + positionOffset + 1.0f + ParallaxViewPager.CORRECTION_PERCENTAGE) * ((float) ParallaxViewPager.this.getWidth())));
                    ParallaxViewPager.this.invalidate();
                }
                if (ParallaxViewPager.this.secondOnPageChangeListener != null) {
                    ParallaxViewPager.this.secondOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
                if (ParallaxViewPager.this.thirdOnPageChangeListener != null) {
                    ParallaxViewPager.this.thirdOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            public void onPageSelected(int position) {
                if (ParallaxViewPager.this.secondOnPageChangeListener != null) {
                    ParallaxViewPager.this.secondOnPageChangeListener.onPageSelected(position);
                }
                if (ParallaxViewPager.this.thirdOnPageChangeListener != null) {
                    ParallaxViewPager.this.thirdOnPageChangeListener.onPageSelected(position);
                }
            }

            public void onPageScrollStateChanged(int state) {
                if (ParallaxViewPager.this.secondOnPageChangeListener != null) {
                    ParallaxViewPager.this.secondOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.destination.top = 0;
        this.destination.bottom = h;
        if (getAdapter() != null && this.bitmap != null) {
            calculateParallaxParameters();
        }
    }

    private void calculateParallaxParameters() {
        if (this.bitmap.getWidth() < getWidth() && this.bitmap.getWidth() < this.bitmap.getHeight() && this.scaleType == 1) {
            Log.w(ParallaxViewPager.class.getName(), "Invalid bitmap bounds for the current device, parallax effect will not work.");
        }
        float ratio = ((float) getHeight()) / ((float) this.bitmap.getHeight());
        if (ratio != 1.0f) {
            switch (this.scaleType) {
                case 0:
                    this.source.top = (int) ((((float) this.bitmap.getHeight()) - (((float) this.bitmap.getHeight()) / ratio)) / 2.0f);
                    this.source.bottom = this.bitmap.getHeight() - this.source.top;
                    this.chunkWidth = (int) Math.ceil((double) (((float) this.bitmap.getWidth()) / ((float) getAdapter().getCount())));
                    this.projectedWidth = this.chunkWidth;
                    return;
                default:
                    this.source.top = 0;
                    this.source.bottom = this.bitmap.getHeight();
                    this.projectedWidth = (int) Math.ceil((double) (((float) getWidth()) / ratio));
                    this.chunkWidth = (int) Math.ceil((double) ((((float) (this.bitmap.getWidth() - this.projectedWidth)) / ((float) getAdapter().getCount())) * this.overlap));
                    return;
            }
        }
    }

    public void setBackgroundResource(int resid) {
        this.bitmap = BitmapFactory.decodeResource(getResources(), resid);
    }

    public void setBackground(Drawable background) {
        this.bitmap = ((BitmapDrawable) background).getBitmap();
    }

    public void setBackgroundDrawable(Drawable background) {
        this.bitmap = ((BitmapDrawable) background).getBitmap();
    }

    public ParallaxViewPager setBackground(Bitmap bitmap2) {
        this.bitmap = bitmap2;
        return this;
    }

    public ParallaxViewPager setScaleType(int scaleType2) {
        if (scaleType2 == 0 || scaleType2 == 1) {
            this.scaleType = scaleType2;
            return this;
        }
        throw new IllegalArgumentException("Illegal argument: scaleType must be FIT_WIDTH or FIT_HEIGHT");
    }

    public ParallaxViewPager setOverlapPercentage(float percentage) {
        if (percentage <= 0.0f || percentage >= 1.0f) {
            throw new IllegalArgumentException("Illegal argument: percentage must be between 0 and 1");
        }
        this.overlap = percentage;
        return this;
    }

    public ParallaxViewPager invalidateParallaxParameters() {
        calculateParallaxParameters();
        return this;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.bitmap != null) {
            canvas.drawBitmap(this.bitmap, this.source, this.destination, (Paint) null);
        }
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.secondOnPageChangeListener = listener;
    }

    public void add3rdOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.thirdOnPageChangeListener = listener;
    }

    public boolean onInterceptTouchEvent(MotionEvent even) {
        if (even.getAction() == 0) {
            this.preX = (int) even.getX();
        } else if (Math.abs(((int) even.getX()) - this.preX) > 10) {
            return true;
        } else {
            this.preX = (int) even.getX();
        }
        try {
            return super.onInterceptTouchEvent(even);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.scrollable) {
            return this.scrollable;
        }
        try {
            return super.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}