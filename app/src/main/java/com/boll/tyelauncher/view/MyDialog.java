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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.toycloud.launcher.R;

public class MyDialog extends Dialog {
    private Button btu_other;
    private Button button_negative;
    private Button button_positive;
    private String content;
    private ImageView iv_dialog_close;
    private String negative_str;
    private String positive_str;
    private TextView tv_content;

    public MyDialog(@NonNull Context context) {
        super(context);
        super.setContentView(R.layout.layout_mydialog);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawableResource(R.drawable.generic_transparent);
        this.tv_content = (TextView) findViewById(R.id.dialog_content);
        this.button_positive = (Button) findViewById(R.id.btu_positive);
        this.button_negative = (Button) findViewById(R.id.btu_negative);
        this.btu_other = (Button) findViewById(R.id.btu_other);
        this.iv_dialog_close = (ImageView) findViewById(R.id.iv_dialog_close);
        this.iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyDialog.this.dissMissDialog();
            }
        });
    }

    public void showOtherButton() {
        this.btu_other.setVisibility(0);
        this.button_negative.setVisibility(8);
    }

    public void dissMissDialog() {
        if (isShowing()) {
            dismiss();
        }
    }

    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
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

    public void setPositiveButton(String text, final DialogInterface.OnClickListener listener) {
        Button button = this.button_positive;
        if (TextUtils.isEmpty(text)) {
            text = "确定";
        }
        button.setText(text);
        this.button_positive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(MyDialog.this, -1);
                }
            }
        });
    }

    public void setNegativeButton(String text, final DialogInterface.OnClickListener listener) {
        Button button = this.button_negative;
        if (TextUtils.isEmpty(text)) {
            text = "确定";
        }
        button.setText(text);
        this.button_negative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(MyDialog.this, -2);
                }
            }
        });
    }

    public void setOtherButton(String text, final DialogInterface.OnClickListener listener) {
        Button button = this.btu_other;
        if (TextUtils.isEmpty(text)) {
            text = "确定";
        }
        button.setText(text);
        this.btu_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(MyDialog.this, -3);
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
