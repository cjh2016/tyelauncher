package com.boll.tyelauncher.widget;

package com.toycloud.launcher.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.anarchy.classify.simple.ChangeInfo;
import com.anarchy.classify.simple.PrimitiveSimpleAdapter;
import com.anarchy.classify.simple.widget.CanMergeView;
import com.toycloud.launcher.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class IReaderFolder extends RelativeLayout implements CanMergeView {
    private static final int CHECK_BOX_ID = 2131689984;
    private static final int CONTAINER_GRID_ID = 2131689979;
    private static final int CONTENT_ID = 2131689980;
    private static final int FOLDER_ID = 2131689978;
    public static final int STATE_AUTO = 0;
    public static final int STATE_FOLDER = 1;
    private static final int TAG_ID = 2131689981;
    /* access modifiers changed from: private */
    public FrameLayout mContent;
    /* access modifiers changed from: private */
    public View mFolderBg;
    /* access modifiers changed from: private */
    public IReaderGridLayout mGridLayout;
    private PrimitiveSimpleAdapter mSimpleAdapter;
    /* access modifiers changed from: private */
    public int mState = 0;
    private TextView mTagView;

    @Retention(RetentionPolicy.SOURCE)
    @interface State {
    }

    public IReaderFolder(Context context) {
        super(context);
    }

    public IReaderFolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IReaderFolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public IReaderFolder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ensureViewFound();
        switch (this.mState) {
        }
    }

    public void setState(int state) {
        this.mState = state;
    }

    public void onMergeStart() {
        this.mFolderBg.setVisibility(0);
        this.mFolderBg.setPivotX((float) (this.mFolderBg.getWidth() / 2));
        this.mFolderBg.setPivotY((float) (this.mFolderBg.getHeight() / 2));
        this.mFolderBg.animate().scaleX(1.2f).scaleY(1.1f).setDuration(200).start();
    }

    public void onMergeCancel() {
        this.mFolderBg.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                IReaderFolder.this.mFolderBg.animate().setListener((Animator.AnimatorListener) null);
                switch (IReaderFolder.this.mState) {
                    case 0:
                        if (IReaderFolder.this.getChildCount() <= 1) {
                            IReaderFolder.this.mFolderBg.setVisibility(8);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        });
    }

    public void onMerged() {
        this.mFolderBg.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
    }

    public void startMergeAnimation(final int duration) {
        if (this.mContent.getVisibility() == 0) {
            ChangeInfo info = this.mGridLayout.getSecondItemChangeInfo();
            float scaleX = info.targetWidth / ((float) this.mContent.getWidth());
            float scaleY = info.targetHeight / ((float) this.mContent.getHeight());
            this.mContent.setPivotX(0.0f);
            this.mContent.setPivotY(0.0f);
            this.mContent.animate().scaleX(scaleX).scaleY(scaleY).translationX((float) info.targetLeft).translationY((float) info.targetTop).setDuration((long) duration).setListener(new AnimatorListenerAdapter() {
                public void onAnimationCancel(Animator animation) {
                    IReaderFolder.this.restoreViewDelayed(IReaderFolder.this.mContent, duration);
                }

                public void onAnimationEnd(Animator animation) {
                    IReaderFolder.this.restoreViewDelayed(IReaderFolder.this.mContent, duration);
                }
            }).start();
            return;
        }
        final View dummyView = new View(getContext());
        this.mGridLayout.getLayoutTransition().setDuration((long) duration);
        this.mGridLayout.getLayoutTransition().addTransitionListener(new LayoutTransition.TransitionListener() {
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
            }

            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                IReaderFolder.this.mGridLayout.removeView(dummyView);
            }
        });
        this.mGridLayout.addView(dummyView, 0);
    }

    /* access modifiers changed from: private */
    public void restoreViewDelayed(final View view, int delayTime) {
        postDelayed(new Runnable() {
            public void run() {
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setTranslationX(0.0f);
                view.setTranslationY(0.0f);
            }
        }, (long) delayTime);
    }

    public ChangeInfo prepareMerge() {
        ChangeInfo changeInfo = this.mGridLayout.getChangeInfo();
        int left = getLeft();
        int top2 = getTop();
        changeInfo.targetLeft += this.mGridLayout.getLeft() + left;
        changeInfo.targetTop += this.mGridLayout.getTop() + top2;
        changeInfo.sourceLeft = this.mContent.getLeft() + left;
        changeInfo.sourceTop = this.mContent.getTop() + top2;
        changeInfo.sourceWidth = (float) this.mContent.getWidth();
        changeInfo.sourceHeight = (float) this.mContent.getHeight();
        return changeInfo;
    }

    public void setAdapter(PrimitiveSimpleAdapter primitiveSimpleAdapter) {
        this.mSimpleAdapter = primitiveSimpleAdapter;
    }

    public void initOrUpdateMain(int parentIndex, int requestCount) {
        if (this.mGridLayout == null) {
            ensureViewFound();
        }
        if (this.mGridLayout != null && requestCount > 0) {
            int childCount = this.mGridLayout.getChildCount();
            if (childCount > requestCount) {
                this.mGridLayout.removeViews(requestCount, childCount - requestCount);
            }
            int childCount2 = this.mGridLayout.getChildCount();
            for (int i = 0; i < requestCount; i++) {
                View convertView = null;
                if (i < childCount2) {
                    convertView = this.mGridLayout.getChildAt(i);
                }
                View adapterChild = this.mSimpleAdapter.getView(this, convertView, parentIndex, i);
                if (!(adapterChild == null || adapterChild == convertView)) {
                    if (i < childCount2) {
                        this.mGridLayout.removeViewAt(i);
                        this.mGridLayout.addView(adapterChild, i);
                    } else {
                        this.mGridLayout.addView(adapterChild, i);
                    }
                }
            }
        }
    }

    public void initOrUpdateSub(int parentIndex, int subIndex) {
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void ensureViewFound() {
        boolean z = true;
        boolean z2 = (this.mTagView == null) | (this.mContent == null) | (this.mFolderBg == null);
        if (this.mGridLayout != null) {
            z = false;
        }
        if (z2 || z) {
            this.mFolderBg = findViewById(R.id.i_reader_folder_bg);
            this.mContent = (FrameLayout) findViewById(R.id.i_reader_folder_content);
            this.mTagView = (TextView) findViewById(R.id.i_reader_folder_tag);
            this.mGridLayout = (IReaderGridLayout) findViewById(R.id.i_reader_folder_grid);
            this.mGridLayout.setLayoutTransition(new LayoutTransition());
        }
    }
}
