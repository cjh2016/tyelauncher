package com.boll.tyelauncher.biz.evalhint.view;

package com.toycloud.launcher.biz.evalhint.view;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class HighlightTextView extends TextView {
    private String mFlatText;
    private List<Highlight> mHighlightList;

    public HighlightTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public HighlightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(CharSequence text, TextView.BufferType type) {
        super.setText(text, type);
        if (text instanceof String) {
            this.mFlatText = (String) text;
            clearHighlightInternal();
        } else if (text == null) {
            this.mFlatText = null;
            clearHighlightInternal();
        }
    }

    private void clearHighlightInternal() {
        if (this.mHighlightList != null) {
            this.mHighlightList.clear();
            this.mHighlightList = null;
        }
    }

    public void clearHighlight() {
        clearHighlightInternal();
        setText(this.mFlatText);
    }

    public void addHighlight(int startPos, int endPos, int color) {
        addHighlight(startPos, endPos, color, false);
    }

    public void addHighlight(int startPos, int endPos, int color, boolean immediately) {
        if (!TextUtils.isEmpty(this.mFlatText)) {
            int endPos2 = endPos + 1;
            if (startPos < 0) {
                startPos = 0;
            }
            int maxEnd = this.mFlatText.length();
            if (endPos2 > maxEnd) {
                endPos2 = maxEnd;
            }
            if (startPos < endPos2) {
                if (this.mHighlightList == null) {
                    this.mHighlightList = new ArrayList();
                }
                this.mHighlightList.add(new Highlight(startPos, endPos2, color));
                if (immediately) {
                    invalidateHighlights();
                }
            }
        }
    }

    public void invalidateHighlights() {
        if (!TextUtils.isEmpty(this.mFlatText) && this.mHighlightList != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder(this.mFlatText);
            for (Highlight highlight : this.mHighlightList) {
                builder.setSpan(new ForegroundColorSpan(highlight.mColor), highlight.mStart, highlight.mEnd, 33);
            }
            setText(builder);
        }
    }

    public void setHighlight(int startPos, int endPos, int color) {
        clearHighlight();
        addHighlight(startPos, endPos, color, true);
    }

    private static class Highlight {
        int mColor;
        int mEnd;
        int mStart;

        Highlight(int start, int end, int color) {
            this.mStart = start;
            this.mEnd = end;
            this.mColor = color;
        }
    }
}