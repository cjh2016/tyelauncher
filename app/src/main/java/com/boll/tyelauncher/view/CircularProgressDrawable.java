package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.toycloud.launcher.R;

public class CircularProgressDrawable extends Drawable implements Animatable {
    /* access modifiers changed from: private */
    public static final ArgbEvaluator COLOR_EVALUATOR = new ArgbEvaluator();
    /* access modifiers changed from: private */
    public static final Interpolator DEFAULT_ROTATION_INTERPOLATOR = new LinearInterpolator();
    /* access modifiers changed from: private */
    public static final Interpolator DEFAULT_SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private static final int END_ANIMATOR_DURATION = 200;
    public static final Interpolator END_INTERPOLATOR = new LinearInterpolator();
    private static final int ROTATION_ANIMATOR_DURATION = 2000;
    private static final int SWEEP_ANIMATOR_DURATION = 600;
    private final RectF fBounds;
    private Interpolator mAngleInterpolator;
    private float mBorderWidth;
    /* access modifiers changed from: private */
    public int[] mColors;
    /* access modifiers changed from: private */
    public int mCurrentColor;
    private float mCurrentEndRatio;
    /* access modifiers changed from: private */
    public int mCurrentIndexColor;
    private float mCurrentRotationAngle;
    private float mCurrentRotationAngleOffset;
    private float mCurrentSweepAngle;
    /* access modifiers changed from: private */
    public ValueAnimator mEndAnimator;
    /* access modifiers changed from: private */
    public boolean mFirstSweepAnimation;
    /* access modifiers changed from: private */
    public int mMaxSweepAngle;
    /* access modifiers changed from: private */
    public int mMinSweepAngle;
    /* access modifiers changed from: private */
    public boolean mModeAppearing;
    /* access modifiers changed from: private */
    public OnEndListener mOnEndListener;
    /* access modifiers changed from: private */
    public Paint mPaint;
    private ValueAnimator mRotationAnimator;
    private float mRotationSpeed;
    private boolean mRunning;
    /* access modifiers changed from: private */
    public ValueAnimator mSweepAppearingAnimator;
    /* access modifiers changed from: private */
    public ValueAnimator mSweepDisappearingAnimator;
    private Interpolator mSweepInterpolator;
    private float mSweepSpeed;

    public interface OnEndListener {
        void onEnd(CircularProgressDrawable circularProgressDrawable);
    }

    public enum Style {
        NORMAL,
        ROUNDED
    }

    private CircularProgressDrawable(int[] colors, float borderWidth, float sweepSpeed, float rotationSpeed, int minSweepAngle, int maxSweepAngle, Style style, Interpolator angleInterpolator, Interpolator sweepInterpolator) {
        this.fBounds = new RectF();
        this.mCurrentRotationAngleOffset = 0.0f;
        this.mCurrentRotationAngle = 0.0f;
        this.mCurrentEndRatio = 1.0f;
        this.mSweepInterpolator = sweepInterpolator;
        this.mAngleInterpolator = angleInterpolator;
        this.mBorderWidth = borderWidth;
        this.mCurrentIndexColor = 0;
        this.mColors = colors;
        this.mCurrentColor = this.mColors[0];
        this.mSweepSpeed = sweepSpeed;
        this.mRotationSpeed = rotationSpeed;
        this.mMinSweepAngle = minSweepAngle;
        this.mMaxSweepAngle = maxSweepAngle;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(borderWidth);
        this.mPaint.setStrokeCap(style == Style.ROUNDED ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        this.mPaint.setColor(this.mColors[0]);
        setupAnimations();
    }

    private void reinitValues() {
        this.mFirstSweepAnimation = true;
        this.mCurrentEndRatio = 1.0f;
        this.mPaint.setColor(this.mCurrentColor);
    }

    public void draw(Canvas canvas) {
        float startAngle = this.mCurrentRotationAngle - this.mCurrentRotationAngleOffset;
        float sweepAngle = this.mCurrentSweepAngle;
        if (!this.mModeAppearing) {
            startAngle += 360.0f - sweepAngle;
        }
        float startAngle2 = startAngle % 360.0f;
        if (this.mCurrentEndRatio < 1.0f) {
            float newSweepAngle = sweepAngle * this.mCurrentEndRatio;
            startAngle2 = ((sweepAngle - newSweepAngle) + startAngle2) % 360.0f;
            sweepAngle = newSweepAngle;
        }
        canvas.drawArc(this.fBounds, startAngle2, sweepAngle, false, this.mPaint);
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return -3;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.fBounds.left = ((float) bounds.left) + (this.mBorderWidth / 2.0f) + 0.5f;
        this.fBounds.right = (((float) bounds.right) - (this.mBorderWidth / 2.0f)) - 0.5f;
        this.fBounds.top = ((float) bounds.top) + (this.mBorderWidth / 2.0f) + 0.5f;
        this.fBounds.bottom = (((float) bounds.bottom) - (this.mBorderWidth / 2.0f)) - 0.5f;
    }

    /* access modifiers changed from: private */
    public void setAppearing() {
        this.mModeAppearing = true;
        this.mCurrentRotationAngleOffset += (float) this.mMinSweepAngle;
    }

    /* access modifiers changed from: private */
    public void setDisappearing() {
        this.mModeAppearing = false;
        this.mCurrentRotationAngleOffset += (float) (360 - this.mMaxSweepAngle);
    }

    private void setupAnimations() {
        this.mRotationAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 360.0f});
        this.mRotationAnimator.setInterpolator(this.mAngleInterpolator);
        this.mRotationAnimator.setDuration((long) (2000.0f / this.mRotationSpeed));
        this.mRotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                CircularProgressDrawable.this.setCurrentRotationAngle(animation.getAnimatedFraction() * 360.0f);
            }
        });
        this.mRotationAnimator.setRepeatCount(-1);
        this.mRotationAnimator.setRepeatMode(1);
        this.mSweepAppearingAnimator = ValueAnimator.ofFloat(new float[]{(float) this.mMinSweepAngle, (float) this.mMaxSweepAngle});
        this.mSweepAppearingAnimator.setInterpolator(this.mSweepInterpolator);
        this.mSweepAppearingAnimator.setDuration((long) (600.0f / this.mSweepSpeed));
        this.mSweepAppearingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle;
                float animatedFraction = animation.getAnimatedFraction();
                if (CircularProgressDrawable.this.mFirstSweepAnimation) {
                    angle = animatedFraction * ((float) CircularProgressDrawable.this.mMaxSweepAngle);
                } else {
                    angle = ((float) CircularProgressDrawable.this.mMinSweepAngle) + (((float) (CircularProgressDrawable.this.mMaxSweepAngle - CircularProgressDrawable.this.mMinSweepAngle)) * animatedFraction);
                }
                CircularProgressDrawable.this.setCurrentSweepAngle(angle);
            }
        });
        this.mSweepAppearingAnimator.addListener(new Animator.AnimatorListener() {
            boolean cancelled = false;

            public void onAnimationStart(Animator animation) {
                this.cancelled = false;
                boolean unused = CircularProgressDrawable.this.mModeAppearing = true;
            }

            public void onAnimationEnd(Animator animation) {
                if (!this.cancelled) {
                    boolean unused = CircularProgressDrawable.this.mFirstSweepAnimation = false;
                    CircularProgressDrawable.this.setDisappearing();
                    CircularProgressDrawable.this.mSweepDisappearingAnimator.start();
                }
            }

            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.mSweepDisappearingAnimator = ValueAnimator.ofFloat(new float[]{(float) this.mMaxSweepAngle, (float) this.mMinSweepAngle});
        this.mSweepDisappearingAnimator.setInterpolator(this.mSweepInterpolator);
        this.mSweepDisappearingAnimator.setDuration((long) (600.0f / this.mSweepSpeed));
        this.mSweepDisappearingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                CircularProgressDrawable.this.setCurrentSweepAngle(((float) CircularProgressDrawable.this.mMaxSweepAngle) - (((float) (CircularProgressDrawable.this.mMaxSweepAngle - CircularProgressDrawable.this.mMinSweepAngle)) * animation.getAnimatedFraction()));
                float fraction = ((float) animation.getCurrentPlayTime()) / ((float) animation.getDuration());
                if (CircularProgressDrawable.this.mColors.length > 1 && fraction > 0.7f) {
                    CircularProgressDrawable.this.mPaint.setColor(((Integer) CircularProgressDrawable.COLOR_EVALUATOR.evaluate((fraction - 0.7f) / 0.3f, Integer.valueOf(CircularProgressDrawable.this.mCurrentColor), Integer.valueOf(CircularProgressDrawable.this.mColors[(CircularProgressDrawable.this.mCurrentIndexColor + 1) % CircularProgressDrawable.this.mColors.length]))).intValue());
                }
            }
        });
        this.mSweepDisappearingAnimator.addListener(new Animator.AnimatorListener() {
            boolean cancelled;

            public void onAnimationStart(Animator animation) {
                this.cancelled = false;
            }

            public void onAnimationEnd(Animator animation) {
                if (!this.cancelled) {
                    CircularProgressDrawable.this.setAppearing();
                    int unused = CircularProgressDrawable.this.mCurrentIndexColor = (CircularProgressDrawable.this.mCurrentIndexColor + 1) % CircularProgressDrawable.this.mColors.length;
                    int unused2 = CircularProgressDrawable.this.mCurrentColor = CircularProgressDrawable.this.mColors[CircularProgressDrawable.this.mCurrentIndexColor];
                    CircularProgressDrawable.this.mPaint.setColor(CircularProgressDrawable.this.mCurrentColor);
                    CircularProgressDrawable.this.mSweepAppearingAnimator.start();
                }
            }

            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.mEndAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        this.mEndAnimator.setInterpolator(END_INTERPOLATOR);
        this.mEndAnimator.setDuration(200);
        this.mEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                CircularProgressDrawable.this.setEndRatio(1.0f - animation.getAnimatedFraction());
            }
        });
        this.mEndAnimator.addListener(new Animator.AnimatorListener() {
            private boolean cancelled;

            public void onAnimationStart(Animator animation) {
                this.cancelled = false;
            }

            public void onAnimationEnd(Animator animation) {
                CircularProgressDrawable.this.setEndRatio(0.0f);
                if (!this.cancelled) {
                    CircularProgressDrawable.this.stop();
                }
            }

            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void start() {
        if (!isRunning()) {
            this.mRunning = true;
            reinitValues();
            this.mRotationAnimator.start();
            this.mSweepAppearingAnimator.start();
            invalidateSelf();
        }
    }

    public void stop() {
        if (isRunning()) {
            this.mRunning = false;
            stopAnimators();
            invalidateSelf();
        }
    }

    private void stopAnimators() {
        this.mRotationAnimator.cancel();
        this.mSweepAppearingAnimator.cancel();
        this.mSweepDisappearingAnimator.cancel();
        this.mEndAnimator.cancel();
    }

    public void progressiveStop(OnEndListener listener) {
        if (isRunning() && !this.mEndAnimator.isRunning()) {
            this.mOnEndListener = listener;
            this.mEndAnimator.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    CircularProgressDrawable.this.mEndAnimator.removeListener(this);
                    if (CircularProgressDrawable.this.mOnEndListener != null) {
                        CircularProgressDrawable.this.mOnEndListener.onEnd(CircularProgressDrawable.this);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            this.mEndAnimator.start();
        }
    }

    public void progressiveStop() {
        progressiveStop((OnEndListener) null);
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public void setCurrentRotationAngle(float currentRotationAngle) {
        this.mCurrentRotationAngle = currentRotationAngle;
        invalidateSelf();
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        this.mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    /* access modifiers changed from: private */
    public void setEndRatio(float ratio) {
        this.mCurrentEndRatio = ratio;
        invalidateSelf();
    }

    public static class Builder {
        private Interpolator mAngleInterpolator;
        private int[] mColors;
        private int mMaxSweepAngle;
        private int mMinSweepAngle;
        private float mRotationSpeed;
        private float mStrokeWidth;
        private Style mStyle;
        private Interpolator mSweepInterpolator;
        private float mSweepSpeed;

        public Builder(Context context) {
            this(context, false);
        }

        public Builder(Context context, boolean editMode) {
            this.mSweepInterpolator = CircularProgressDrawable.DEFAULT_SWEEP_INTERPOLATOR;
            this.mAngleInterpolator = CircularProgressDrawable.DEFAULT_ROTATION_INTERPOLATOR;
            initValues(context, editMode);
        }

        private void initValues(Context context, boolean editMode) {
            this.mStrokeWidth = context.getResources().getDimension(R.dimen.cpb_default_stroke_width);
            this.mSweepSpeed = 1.0f;
            this.mRotationSpeed = 1.0f;
            if (editMode) {
                this.mColors = new int[]{-16776961};
                this.mMinSweepAngle = 20;
                this.mMaxSweepAngle = 300;
            } else {
                this.mColors = new int[]{context.getResources().getColor(R.color.cpb_default_color)};
                this.mMinSweepAngle = context.getResources().getInteger(R.integer.cpb_default_min_sweep_angle);
                this.mMaxSweepAngle = context.getResources().getInteger(R.integer.cpb_default_max_sweep_angle);
            }
            this.mStyle = Style.ROUNDED;
        }

        public Builder color(int color) {
            this.mColors = new int[]{color};
            return this;
        }

        public Builder colors(int[] colors) {
            CircularProgressBarUtils.checkColors(colors);
            this.mColors = colors;
            return this;
        }

        public Builder sweepSpeed(float sweepSpeed) {
            CircularProgressBarUtils.checkSpeed(sweepSpeed);
            this.mSweepSpeed = sweepSpeed;
            return this;
        }

        public Builder rotationSpeed(float rotationSpeed) {
            CircularProgressBarUtils.checkSpeed(rotationSpeed);
            this.mRotationSpeed = rotationSpeed;
            return this;
        }

        public Builder minSweepAngle(int minSweepAngle) {
            CircularProgressBarUtils.checkAngle(minSweepAngle);
            this.mMinSweepAngle = minSweepAngle;
            return this;
        }

        public Builder maxSweepAngle(int maxSweepAngle) {
            CircularProgressBarUtils.checkAngle(maxSweepAngle);
            this.mMaxSweepAngle = maxSweepAngle;
            return this;
        }

        public Builder strokeWidth(float strokeWidth) {
            CircularProgressBarUtils.checkPositiveOrZero(strokeWidth, "StrokeWidth");
            this.mStrokeWidth = strokeWidth;
            return this;
        }

        public Builder style(Style style) {
            CircularProgressBarUtils.checkNotNull(style, "Style");
            this.mStyle = style;
            return this;
        }

        public Builder sweepInterpolator(Interpolator interpolator) {
            CircularProgressBarUtils.checkNotNull(interpolator, "Sweep interpolator");
            this.mSweepInterpolator = interpolator;
            return this;
        }

        public Builder angleInterpolator(Interpolator interpolator) {
            CircularProgressBarUtils.checkNotNull(interpolator, "Angle interpolator");
            this.mAngleInterpolator = interpolator;
            return this;
        }

        public CircularProgressDrawable build() {
            return new CircularProgressDrawable(this.mColors, this.mStrokeWidth, this.mSweepSpeed, this.mRotationSpeed, this.mMinSweepAngle, this.mMaxSweepAngle, this.mStyle, this.mAngleInterpolator, this.mSweepInterpolator);
        }
    }
}
