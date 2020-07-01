package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.iflytek.utils.SpannableStringUtils;
import com.iflytek.utils.UIUtils;
import com.toycloud.launcher.R;
import java.text.DecimalFormat;

public class RiseNumberTextView extends AppCompatTextView {
    private static final int RUNNING = 1;
    private static final int STOPPED = 0;
    private long duration;
    /* access modifiers changed from: private */
    public DecimalFormat fnum;
    private float fromNumber;
    /* access modifiers changed from: private */
    public int mPlayingState;
    /* access modifiers changed from: private */
    public boolean mRestart;
    private int numberType;
    /* access modifiers changed from: private */
    public String suffix;
    private float toNumber;

    public RiseNumberTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public RiseNumberTextView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mPlayingState = 0;
        this.duration = 200;
        this.numberType = 2;
        this.fnum = new DecimalFormat("##0.00");
        this.suffix = "";
    }

    public RiseNumberTextView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.mPlayingState = 0;
        this.duration = 200;
        this.numberType = 2;
        this.fnum = new DecimalFormat("##0.00");
        this.suffix = "";
    }

    public boolean isRunning() {
        return this.mPlayingState == 1;
    }

    private void runFloat() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{this.fromNumber, this.toNumber});
        valueAnimator.setDuration(this.duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                RiseNumberTextView.this.setText(RiseNumberTextView.this.fnum.format((double) Float.parseFloat(animation.getAnimatedValue().toString())));
                if (animation.getAnimatedFraction() >= 1.0f) {
                    int unused = RiseNumberTextView.this.mPlayingState = 0;
                }
            }
        });
        valueAnimator.start();
    }

    private void runInt() {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[]{(int) this.fromNumber, (int) this.toNumber});
        valueAnimator.setDuration((long) (((float) this.duration) * Math.abs(this.toNumber - this.fromNumber)));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (RiseNumberTextView.this.mPlayingState == 0) {
                    valueAnimator.cancel();
                    if (RiseNumberTextView.this.mRestart) {
                        boolean unused = RiseNumberTextView.this.mRestart = false;
                        RiseNumberTextView.this.start();
                        return;
                    }
                    return;
                }
                Context context = RiseNumberTextView.this.getContext();
                RiseNumberTextView.this.setText(new SpannableStringUtils(context).append(animation.getAnimatedValue().toString()).append("/").append(RiseNumberTextView.this.suffix).setFontSize(UIUtils.getDimens(context, R.dimen.sp_14)).create());
                if (animation.getAnimatedFraction() >= 1.0f) {
                    int unused2 = RiseNumberTextView.this.mPlayingState = 0;
                }
            }
        });
        valueAnimator.start();
    }

    public void start() {
        if (!isRunning()) {
            this.mPlayingState = 1;
            if (this.numberType == 1) {
                runInt();
            } else {
                runFloat();
            }
        } else {
            this.mRestart = true;
            this.mPlayingState = 0;
        }
    }

    public void setFloat(float fromNum, float toNum) {
        this.toNumber = toNum;
        this.numberType = 2;
        this.fromNumber = fromNum;
    }

    public void setInteger(int fromNum, int toNum) {
        setInteger(fromNum, toNum, true);
    }

    public void setInteger(int fromNum, int toNum, boolean anim) {
        this.toNumber = (float) toNum;
        this.numberType = 1;
        this.fromNumber = (float) fromNum;
        if (!anim) {
            Context context = getContext();
            setText(new SpannableStringUtils(context).append(String.valueOf(toNum)).append("/").append(this.suffix).setFontSize(UIUtils.getDimens(context, R.dimen.sp_14)).create());
        }
    }

    public void setSuffix(String suffix2) {
        this.suffix = suffix2;
    }

    public void setDuration(long duration2) {
        this.duration = duration2;
    }

    public void showText() {
        this.mPlayingState = 0;
        Context context = getContext();
        setText(new SpannableStringUtils(context).append(((int) this.fromNumber) + "/").append(this.suffix).setFontSize(UIUtils.getDimens(context, R.dimen.sp_14)).create());
    }
}
