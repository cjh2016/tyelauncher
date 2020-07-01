package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.toycloud.launcher.R;

public class EditableMenuTitleView extends FrameLayout implements View.OnClickListener {
    private TextView mEditTv;
    private OnClickEditListener mListener;
    private TextView mTitleTv;

    public interface OnClickEditListener {
        void onClickEdit(int i);
    }

    public EditableMenuTitleView(Context context) {
        super(context);
        initView(context, (AttributeSet) null);
    }

    public EditableMenuTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public void seTitle(String title) {
        this.mTitleTv.setText(title);
    }

    public void changeEditable(boolean editable) {
        this.mEditTv.setVisibility(editable ? 0 : 8);
    }

    public void setOnEditClickListener(OnClickEditListener listener) {
        this.mListener = listener;
    }

    private void initView(Context context, AttributeSet attrs) {
        int i;
        View childView = inflate(context, R.layout.view_editable_title, this);
        this.mTitleTv = (TextView) findViewById(R.id.title_tv);
        this.mEditTv = (TextView) findViewById(R.id.edit_tv);
        this.mEditTv.setOnClickListener(this);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditableMenuTitleView);
            if (array != null) {
                this.mTitleTv.setText(array.getString(0));
                TextView textView = this.mEditTv;
                if (array.getBoolean(1, false)) {
                    i = 0;
                } else {
                    i = 8;
                }
                textView.setVisibility(i);
                childView.setBackgroundColor(array.getColor(2, -1));
                array.recycle();
                return;
            }
            return;
        }
        this.mEditTv.setVisibility(8);
    }

    public void onClick(View v) {
        if (v == this.mEditTv && this.mListener != null) {
            this.mListener.onClickEdit(getId());
        }
    }
}