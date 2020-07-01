package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.toycloud.launcher.R;
import com.toycloud.launcher.view.CircularProgressDrawable;

public class CircularProgressBar extends ProgressBar {
    public CircularProgressBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cpbStyle);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            setIndeterminateDrawable(new CircularProgressDrawable.Builder(context, true).build());
            return;
        }
        Resources res = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, defStyle, 0);
        int color = a.getColor(1, res.getColor(R.color.cpb_default_color));
        float strokeWidth = a.getDimension(3, res.getDimension(R.dimen.cpb_default_stroke_width));
        float sweepSpeed = a.getFloat(6, Float.parseFloat(res.getString(R.string.cpb_default_sweep_speed)));
        float rotationSpeed = a.getFloat(7, Float.parseFloat(res.getString(R.string.cpb_default_rotation_speed)));
        int colorsId = a.getResourceId(2, 0);
        int minSweepAngle = a.getInteger(4, res.getInteger(R.integer.cpb_default_min_sweep_angle));
        int maxSweepAngle = a.getInteger(5, res.getInteger(R.integer.cpb_default_max_sweep_angle));
        a.recycle();
        int[] colors = colorsId != 0 ? res.getIntArray(colorsId) : null;
        CircularProgressDrawable.Builder builder = new CircularProgressDrawable.Builder(context).sweepSpeed(sweepSpeed).rotationSpeed(rotationSpeed).strokeWidth(strokeWidth).minSweepAngle(minSweepAngle).maxSweepAngle(maxSweepAngle);
        if (colors == null || colors.length <= 0) {
            builder.color(color);
        } else {
            builder.colors(colors);
        }
        setIndeterminateDrawable(builder.build());
    }

    private CircularProgressDrawable checkIndeterminateDrawable() {
        Drawable ret = getIndeterminateDrawable();
        if (ret != null && (ret instanceof CircularProgressDrawable)) {
            return (CircularProgressDrawable) ret;
        }
        throw new RuntimeException("The drawable is not a CircularProgressDrawable");
    }

    public void progressiveStop() {
        checkIndeterminateDrawable().progressiveStop();
    }

    public void progressiveStop(CircularProgressDrawable.OnEndListener listener) {
        checkIndeterminateDrawable().progressiveStop(listener);
    }
}
