package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import com.toycloud.launcher.R;

@SuppressLint({"AppCompatCustomView"})
public class RoundImageView extends ImageView {
    private static final int BODER_RADIUS_DEFAULT = 10;
    public static final int SCALE_TYPE_CROP = 0;
    public static final int SCALE_TYPE_FIT = 1;
    private static final String STATE_BORDER_RADIUS = "state_border_radius";
    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    private Paint bitmapPaint;
    private BitmapShader bitmapShader;
    private int borderRadius;
    private boolean corners_bottom_left;
    private boolean corners_bottom_right;
    private boolean corners_top_left;
    private boolean corners_top_right;
    private Matrix matrix;
    private int radius;
    private RectF roundRect;
    private int scaleType;
    private int type;
    private int width;

    public RoundImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.corners_top_left = true;
        this.corners_top_right = true;
        this.corners_bottom_left = true;
        this.corners_bottom_right = true;
        this.type = 0;
        this.scaleType = 0;
        this.matrix = new Matrix();
        this.bitmapPaint = new Paint();
        this.bitmapPaint.setAntiAlias(true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        this.type = array.getInt(1, 0);
        this.scaleType = array.getInt(2, 1);
        this.corners_top_left = array.getBoolean(3, true);
        this.corners_top_right = array.getBoolean(4, true);
        this.corners_bottom_left = array.getBoolean(5, true);
        this.corners_bottom_right = array.getBoolean(6, true);
        this.borderRadius = array.getDimensionPixelSize(0, (int) TypedValue.applyDimension(1, 10.0f, getResources().getDisplayMetrics()));
        array.recycle();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.type == 0) {
            this.width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            this.radius = this.width / 2;
            setMeasuredDimension(this.width, this.width);
        }
    }

    private void setUpShader() {
        float scale;
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawableToBitamp(drawable);
            this.bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            if (this.type == 0) {
                float scale2 = (1.0f * ((float) this.width)) / ((float) Math.min(bitmap.getWidth(), bitmap.getHeight()));
                this.matrix.setScale(scale2, scale2);
            } else if (this.type == 1) {
                float scaleWidth = (((float) getWidth()) * 1.0f) / ((float) bitmap.getWidth());
                float scaleHeight = (((float) getHeight()) * 1.0f) / ((float) bitmap.getHeight());
                if (scaleWidth != scaleHeight) {
                    scale = Math.max(scaleWidth, scaleHeight);
                } else {
                    scale = 1.0f;
                }
                if (this.scaleType == 0) {
                    this.matrix.setScale(scale, scale);
                } else if (this.scaleType == 1) {
                    this.matrix.setScale(scaleWidth, scaleHeight);
                }
            }
            this.bitmapShader.setLocalMatrix(this.matrix);
            this.bitmapPaint.setShader(this.bitmapShader);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            setUpShader();
            if (this.type == 1) {
                canvas.drawRoundRect(this.roundRect, (float) this.borderRadius, (float) this.borderRadius, this.bitmapPaint);
                if (!this.corners_top_left) {
                    canvas.drawRect(0.0f, 0.0f, (float) this.borderRadius, (float) this.borderRadius, this.bitmapPaint);
                }
                if (!this.corners_top_right) {
                    canvas.drawRect(this.roundRect.right - ((float) this.borderRadius), 0.0f, this.roundRect.right, (float) this.borderRadius, this.bitmapPaint);
                }
                if (!this.corners_bottom_left) {
                    canvas.drawRect(0.0f, this.roundRect.bottom - ((float) this.borderRadius), (float) this.borderRadius, this.roundRect.bottom, this.bitmapPaint);
                }
                if (!this.corners_bottom_right) {
                    canvas.drawRect(this.roundRect.right - ((float) this.borderRadius), this.roundRect.bottom - ((float) this.borderRadius), this.roundRect.right, this.roundRect.bottom, this.bitmapPaint);
                    return;
                }
                return;
            }
            canvas.drawCircle((float) this.radius, (float) this.radius, (float) this.radius, this.bitmapPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.type == 1) {
            this.roundRect = new RectF(0.0f, 0.0f, (float) w, (float) h);
        }
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, this.type);
        bundle.putInt(STATE_BORDER_RADIUS, this.borderRadius);
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.borderRadius = bundle.getInt(STATE_BORDER_RADIUS);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setBorderRadius(int borderRadius2) {
        this.borderRadius = dp2px(borderRadius2);
        invalidate();
    }

    public void setScaleType(int scaleType2) {
        if (scaleType2 == 0 || scaleType2 == 1) {
            this.scaleType = scaleType2;
            invalidate();
        }
    }

    public void setType(int type2) {
        if (this.type == 1 || this.type == 0) {
            this.type = type2;
            invalidate();
        }
    }

    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(1, (float) dpVal, getResources().getDisplayMetrics());
    }
}
