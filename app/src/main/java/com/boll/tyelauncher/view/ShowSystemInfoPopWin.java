package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.School;
import java.util.List;

public class ShowSystemInfoPopWin extends PopupWindow implements View.OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public String BASE_URL = "https://k12-api.openspeech.cn/";
    public View contentView;
    View footerView;
    private ImageView iv_eria2;
    private Activity mContext;
    public View pickerContainerV;
    RelativeLayout rel_to_call;

    public static class Builder {
        /* access modifiers changed from: private */
        public Activity context;

        public Builder(Activity context2) {
            this.context = context2;
        }

        public ShowSystemInfoPopWin build() {
            return new ShowSystemInfoPopWin(this);
        }

        public Builder setData(List<School> list, StringBuffer selectSchool) {
            return this;
        }
    }

    public ShowSystemInfoPopWin(Builder builder) {
        this.mContext = builder.context;
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_show_systeminfo, (ViewGroup) null);
        this.contentView.setOnClickListener(this);
        this.rel_to_call = (RelativeLayout) this.contentView.findViewById(R.id.rel_to_call);
        setTouchable(true);
        setFocusable(true);
        setClippingEnabled(false);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.PopupWindow_Parent_Manger);
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
        } else if (v == this.rel_to_call) {
            try {
                Intent intent_tel = new Intent("android.intent.action.DIAL", Uri.parse("tel:"));
                intent_tel.setFlags(268435456);
                this.mContext.startActivity(intent_tel);
            } catch (Throwable th) {
            }
        }
    }
}
