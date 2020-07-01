package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.toycloud.launcher.R;

public class SegmentCardView extends FrameLayout {
    private TextView mDetailView;
    private TextView mProgressView;
    private TextView mTipView;

    public SegmentCardView(@NonNull Context context) {
        super(context);
    }

    public SegmentCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SegmentCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SegmentCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_segment_card, this, true);
        this.mProgressView = (TextView) findViewById(R.id.card_progress);
        this.mDetailView = (TextView) findViewById(R.id.card_detail);
        this.mTipView = (TextView) findViewById(R.id.card_tip);
        this.mProgressView.setVisibility(8);
        this.mDetailView.setVisibility(8);
    }

    private void doSetText(TextView textView, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(8);
            return;
        }
        textView.setVisibility(0);
        textView.setText(text);
    }

    public void setProgress(CharSequence progress) {
        doSetText(this.mProgressView, progress);
    }

    public void setDetail(CharSequence detail) {
        doSetText(this.mDetailView, detail);
    }

    public void setTip(String tip) {
        doSetText(this.mTipView, tip);
    }
}
