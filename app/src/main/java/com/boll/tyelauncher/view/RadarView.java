package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.toycloud.launcher.R;
import java.util.ArrayList;
import java.util.List;

public class RadarView extends View {
    private static final String TAG = "RadarView";
    private static final int s_DEFAULT_BG_CIRCLE_BORDER = 5;
    private static final int s_DEFAULT_BG_CIRCLE_COUNT = 5;
    private static final int s_DEFAULT_BG_CIRCLE_RADIUS = 20;
    private static final int s_DEFAULT_LINE_WIDTH = 5;
    private static final int s_DEFAULT_TEXT_SIZE = 10;
    private static final int s_DEFAULT_VALUE_DOT_RADIUS = 5;
    private int mBgCircleBorder;
    private int mBgCircleColor;
    private int mBgCircleCount;
    private int mBgCircleInnerColor;
    private Paint mBgCircleInnerPaint;
    private Paint mBgCirclePaint;
    private int mCenterCircleRadius;
    private float mCenterX;
    private float mCenterY;
    private Paint mLinePaint;
    private Path mLinePath;
    private int mTextColor;
    private Paint mTextPaint;
    private int mTextSize;
    private int mValueAreaColor;
    private Paint mValueAreaInnerPaint;
    private int mValueAreaLineWidth;
    private int mValueDotRadius;
    private Paint mValuePaint;
    private List<Float> ratioValues;
    private List<Integer> totalValues;

    public RadarView(Context context) {
        this(context, (AttributeSet) null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ratioValues = new ArrayList();
        this.totalValues = new ArrayList();
        readCustomAttrs(context, attrs);
        init();
    }

    private void init() {
        this.mBgCirclePaint = new Paint();
        this.mBgCirclePaint.setDither(true);
        this.mBgCirclePaint.setAntiAlias(true);
        this.mBgCirclePaint.setStyle(Paint.Style.STROKE);
        this.mBgCirclePaint.setStrokeWidth((float) this.mBgCircleBorder);
        this.mBgCirclePaint.setColor(this.mBgCircleColor);
        this.mBgCircleInnerPaint = new Paint();
        this.mBgCircleInnerPaint.setDither(true);
        this.mBgCircleInnerPaint.setAntiAlias(true);
        this.mBgCircleInnerPaint.setStyle(Paint.Style.FILL);
        this.mBgCircleInnerPaint.setColor(this.mBgCircleInnerColor);
        this.mLinePaint = new Paint();
        this.mLinePaint.setDither(true);
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setColor(this.mValueAreaColor);
        this.mLinePaint.setStyle(Paint.Style.STROKE);
        this.mLinePaint.setStrokeWidth((float) this.mValueAreaLineWidth);
        this.mLinePath = new Path();
        this.mValuePaint = new Paint();
        this.mValuePaint.setDither(true);
        this.mValuePaint.setAntiAlias(true);
        this.mValuePaint.setColor(this.mValueAreaColor);
        this.mValueAreaInnerPaint = new Paint();
        this.mValueAreaInnerPaint.setDither(true);
        this.mValueAreaInnerPaint.setAntiAlias(true);
        this.mTextPaint = new Paint();
        this.mTextPaint.setDither(true);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize((float) this.mTextSize);
    }

    private void readCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
        this.mBgCircleCount = typedArray.getInteger(0, 5);
        if (this.mBgCircleCount <= 0) {
            throw new IllegalArgumentException("mBgCircleCount must be >= 0");
        }
        this.mBgCircleColor = typedArray.getColor(2, getResources().getColor(R.color.colorCircle));
        this.mBgCircleInnerColor = typedArray.getColor(1, getResources().getColor(R.color.colorTransparent));
        this.mBgCircleBorder = typedArray.getDimensionPixelSize(3, 5);
        if (this.mBgCircleBorder < 0) {
            throw new IllegalArgumentException("mBgCircleBorder must be >= 0");
        }
        this.mCenterCircleRadius = typedArray.getDimensionPixelSize(4, 20);
        if (this.mCenterCircleRadius <= 0) {
            throw new IllegalArgumentException("mCenterCircleRadius must be > 0");
        }
        this.mValueAreaColor = typedArray.getColor(5, getResources().getColor(R.color.colorWhite));
        this.mValueDotRadius = typedArray.getDimensionPixelSize(7, 5);
        this.mTextColor = typedArray.getColor(8, getResources().getColor(R.color.colorWhite));
        this.mTextSize = typedArray.getDimensionPixelSize(9, 10);
        this.mValueAreaLineWidth = typedArray.getDimensionPixelSize(6, 5);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(resolveMeasureSpec(widthMeasureSpec, getPaddingStart() + getPaddingEnd()), resolveMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom()));
    }

    private int resolveMeasureSpec(int measureSpec, int padding) {
        int newMeasureSpec = measureSpec;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
                return View.MeasureSpec.makeMeasureSpec((this.mBgCircleCount * this.mCenterCircleRadius) + padding, specMode);
            case 0:
            case 1073741824:
                return measureSpec;
            default:
                return newMeasureSpec;
        }
    }

    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        this.mCenterX = ((float) ((getWidth() - getPaddingStart()) - getPaddingEnd())) / 2.0f;
        this.mCenterY = ((float) ((getHeight() - getPaddingTop()) - getPaddingBottom())) / 2.0f;
    }

    public void resetView() {
        this.ratioValues.clear();
        this.totalValues.clear();
        invalidate();
    }

    public void setRatioValues(List<Float> values) {
        this.ratioValues.clear();
        if (values != null && values.size() == 4) {
            this.ratioValues.addAll(values);
            invalidate();
        }
    }

    public void setTotalValues(List<Integer> values) {
        this.totalValues.clear();
        if (values != null && values.size() == 4) {
            this.totalValues.addAll(values);
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRadarBg(canvas);
        drawValueText(canvas);
        drawValueArea(canvas);
    }

    private void drawValueText(Canvas canvas) {
        if (this.ratioValues.size() == 4 && this.totalValues.size() == 4) {
            Paint.FontMetrics fontMetrics = this.mTextPaint.getFontMetrics();
            float fontHeight = fontMetrics.descent - fontMetrics.ascent;
            int areaRadius = this.mBgCircleCount * this.mCenterCircleRadius;
            Float doubleTop = this.ratioValues.get(0);
            float x1 = this.mCenterX;
            float y1 = this.mCenterY - (doubleTop.floatValue() * ((float) areaRadius));
            String textTop = Integer.toString(Math.round(doubleTop.floatValue() * ((float) this.totalValues.get(0).intValue())));
            Log.d(TAG, "textTop:" + textTop);
            canvas.drawText(textTop, x1 - (this.mTextPaint.measureText(textTop) / 2.0f), (y1 - (fontHeight / 5.0f)) - ((float) this.mValueDotRadius), this.mTextPaint);
            Float doubleLeft = this.ratioValues.get(1);
            float x2 = this.mCenterX - (((float) areaRadius) * doubleLeft.floatValue());
            float y2 = this.mCenterY;
            String textLeft = Integer.toString(Math.round(doubleLeft.floatValue() * ((float) this.totalValues.get(1).intValue())));
            Log.d(TAG, "textLeft:" + textLeft);
            canvas.drawText(textLeft, (x2 - this.mTextPaint.measureText(textLeft)) - ((float) this.mValueDotRadius), (fontHeight / 5.0f) + y2, this.mTextPaint);
            Float doubleBottom = this.ratioValues.get(2);
            float x3 = this.mCenterX;
            float y3 = this.mCenterY + (((float) areaRadius) * doubleBottom.floatValue());
            String textBottom = Integer.toString(Math.round(doubleBottom.floatValue() * ((float) this.totalValues.get(2).intValue())));
            Log.d(TAG, "textBottom:" + textBottom);
            canvas.drawText(textBottom, x3 - (this.mTextPaint.measureText(textBottom) / 2.0f), y3 + fontHeight + ((float) this.mValueDotRadius), this.mTextPaint);
            Float doubleRight = this.ratioValues.get(3);
            float x4 = this.mCenterX + (((float) areaRadius) * doubleRight.floatValue());
            float y4 = this.mCenterY;
            String textRight = Integer.toString(Math.round(doubleRight.floatValue() * ((float) this.totalValues.get(3).intValue())));
            Log.d(TAG, "textRight:" + textRight);
            float measureText = this.mTextPaint.measureText(textRight);
            canvas.drawText(textRight, ((float) this.mValueDotRadius) + x4, (fontHeight / 5.0f) + y4, this.mTextPaint);
        }
    }

    private void drawRadarBg(Canvas canvas) {
        for (int i = 0; i < this.mBgCircleCount; i++) {
            canvas.drawCircle(this.mCenterX, this.mCenterY, (float) (this.mCenterCircleRadius * (i + 1)), this.mBgCirclePaint);
            canvas.drawCircle(this.mCenterX, this.mCenterY, (float) (this.mCenterCircleRadius * (i + 1)), this.mBgCircleInnerPaint);
        }
    }

    private void drawValueArea(Canvas canvas) {
        if (this.ratioValues.size() == 4) {
            this.mLinePath.rewind();
            int areaRadius = this.mBgCircleCount * this.mCenterCircleRadius;
            float x1 = this.mCenterX;
            float y1 = this.mCenterY - (((float) areaRadius) * this.ratioValues.get(0).floatValue());
            this.mLinePath.moveTo(x1, y1);
            Canvas canvas2 = canvas;
            float f = y1;
            canvas2.drawCircle(x1, f, (float) this.mValueDotRadius, this.mValuePaint);
            float x2 = this.mCenterX - (((float) areaRadius) * this.ratioValues.get(1).floatValue());
            float y2 = this.mCenterY;
            this.mLinePath.lineTo(x2, y2);
            Canvas canvas3 = canvas;
            float f2 = x2;
            float f3 = y2;
            canvas3.drawCircle(f2, f3, (float) this.mValueDotRadius, this.mValuePaint);
            float x3 = this.mCenterX;
            float y3 = this.mCenterY + (((float) areaRadius) * this.ratioValues.get(2).floatValue());
            this.mLinePath.lineTo(x3, y3);
            Canvas canvas4 = canvas;
            float f4 = x3;
            float f5 = y3;
            canvas4.drawCircle(f4, f5, (float) this.mValueDotRadius, this.mValuePaint);
            float x4 = this.mCenterX + (((float) areaRadius) * this.ratioValues.get(3).floatValue());
            float y4 = this.mCenterY;
            this.mLinePath.lineTo(x4, y4);
            Canvas canvas5 = canvas;
            float f6 = x4;
            float f7 = y4;
            canvas5.drawCircle(f6, f7, (float) this.mValueDotRadius, this.mValuePaint);
            this.mLinePath.close();
            canvas.drawPath(this.mLinePath, this.mLinePaint);
            this.mValueAreaInnerPaint.setShader(new RadialGradient(this.mCenterX, this.mCenterY, (float) areaRadius, Color.parseColor("#55ffffff"), this.mValueAreaColor, Shader.TileMode.CLAMP));
            canvas.drawPath(this.mLinePath, this.mValueAreaInnerPaint);
        }
    }
}