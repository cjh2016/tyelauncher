package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AddSchoolPopWin extends PopupWindow implements View.OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public TextView cancel_btn;
    public TextView confirmBtn;
    public View contentView;
    View footerView;
    private EditText keyWord;
    private Activity mContext;
    private OnAddSchoolInterface mListener;
    public View pickerContainerV;
    StringBuffer selectedSchool = new StringBuffer();

    public interface OnAddSchoolInterface {
        void onPick(String str);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public Activity context;
        /* access modifiers changed from: private */
        public OnAddSchoolInterface listener;

        public Builder(Activity context2, OnAddSchoolInterface listener2) {
            this.context = context2;
            this.listener = listener2;
        }

        public AddSchoolPopWin build() {
            return new AddSchoolPopWin(this);
        }

        public Builder setData(List<School> list, StringBuffer selectSchool) {
            return this;
        }
    }

    public AddSchoolPopWin(Builder builder) {
        this.mContext = builder.context;
        this.mListener = builder.listener;
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_mydialog_add_school, (ViewGroup) null);
        this.confirmBtn = (TextView) this.contentView.findViewById(R.id.btu_positive);
        this.cancel_btn = (TextView) this.contentView.findViewById(R.id.btu_negative);
        this.keyWord = (EditText) this.contentView.findViewById(R.id.dialog_content);
        this.confirmBtn.setText("确定");
        this.cancel_btn.setText("取消");
        this.confirmBtn.setOnClickListener(this);
        this.contentView.setOnClickListener(this);
        this.keyWord.requestFocus();
        ((InputMethodManager) this.keyWord.getContext().getSystemService("input_method")).showSoftInput(this.keyWord, 0);
        setTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(this.contentView);
        setWidth(-1);
        setHeight(-1);
    }

    public void showPopWin(Activity activity) {
        if (activity != null) {
            showAtLocation(activity.getWindow().getDecorView(), 17, 0, 0);
        }
    }

    public void dismissPopWin() {
        dismiss();
    }

    public void onClick(View v) {
        if (v == this.contentView) {
            dismissPopWin();
        } else if (v == this.confirmBtn) {
            String string = this.keyWord.getText().toString();
            if (TextUtils.isEmpty(string)) {
                this.mListener.onPick(string);
                return;
            }
            this.mListener.onPick(string);
            dismissPopWin();
        } else if (v == this.cancel_btn) {
            dismissPopWin();
        }
    }
}