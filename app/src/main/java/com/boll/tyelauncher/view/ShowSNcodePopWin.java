package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.School;
import com.toycloud.launcher.util.PadInfoUtil;
import java.util.List;

public class ShowSNcodePopWin extends PopupWindow implements View.OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public String BASE_URL = "https://k12-api.openspeech.cn/";
    public View contentView;
    View footerView;
    private Activity mContext;
    private String mSnId;
    public View pickerContainerV;
    private TextView tv_eria2;

    public static class Builder {
        /* access modifiers changed from: private */
        public Activity context;
        /* access modifiers changed from: private */
        public String mSnId;

        public Builder(Activity context2, String SnId) {
            this.context = context2;
            this.mSnId = SnId;
        }

        public ShowSNcodePopWin build() {
            return new ShowSNcodePopWin(this);
        }

        public Builder setData(List<School> list, StringBuffer selectSchool) {
            return this;
        }
    }

    public ShowSNcodePopWin(Builder builder) {
        this.mContext = builder.context;
        this.mSnId = builder.mSnId;
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_show_sncode, (ViewGroup) null);
        this.contentView.setOnClickListener(this);
        this.tv_eria2 = (TextView) this.contentView.findViewById(R.id.tv_sn_code);
        this.tv_eria2.setText(new PadInfoUtil(this.mContext).getSnCode());
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
        }
    }
}
