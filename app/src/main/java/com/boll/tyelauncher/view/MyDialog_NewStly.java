package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.toycloud.launcher.R;

public class MyDialog_NewStly extends Dialog {
    private TextView button_negative;
    private TextView button_positive;
    private String content;
    private ImageView iv_dialog_close;
    private String negative_str;
    private String positive_str;
    private TextView tv_content;
    private View view_line;

    public MyDialog_NewStly(@NonNull Context context) {
        super(context);
        super.setContentView(R.layout.layout_mydialog_style);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawableResource(R.drawable.generic_transparent);
        this.tv_content = (TextView) findViewById(R.id.dialog_content);
        this.button_positive = (TextView) findViewById(R.id.btu_positive);
        this.button_negative = (TextView) findViewById(R.id.btu_negative);
        this.view_line = findViewById(R.id.view_line);
    }

    public void dissMissDialog() {
        if (isShowing()) {
            dismiss();
        }
    }

    public MyDialog_NewStly(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog_NewStly(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content2) {
        if (!TextUtils.isEmpty(content2)) {
            this.tv_content.setText(content2);
            this.content = content2;
        }
    }

    public String getPositive_str() {
        return this.positive_str;
    }

    public void setPositive_str(String positive_str2) {
        this.positive_str = positive_str2;
    }

    public String getNegative_str() {
        return this.negative_str;
    }

    public void setNegative_str(String negative_str2) {
        this.negative_str = negative_str2;
    }

    public void setHideOtherButton() {
        this.button_negative.setVisibility(8);
        this.view_line.setVisibility(8);
        this.button_positive.setBackgroundResource(R.drawable.white_bottom_corner);
    }

    public void setPositiveButton(String text, final DialogInterface.OnClickListener listener) {
        TextView textView = this.button_positive;
        if (TextUtils.isEmpty(text)) {
            text = "确定";
        }
        textView.setText(text);
        this.button_positive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(MyDialog_NewStly.this, -1);
                }
            }
        });
    }

    public void setNegativeButton(String text, final DialogInterface.OnClickListener listener) {
        TextView textView = this.button_negative;
        if (TextUtils.isEmpty(text)) {
            text = "确定";
        }
        textView.setText(text);
        this.button_negative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(MyDialog_NewStly.this, -2);
                }
            }
        });
    }

    public void setNegativeVisable(boolean visable) {
        if (visable) {
            this.button_negative.setVisibility(0);
        } else {
            this.button_negative.setVisibility(8);
        }
    }
}