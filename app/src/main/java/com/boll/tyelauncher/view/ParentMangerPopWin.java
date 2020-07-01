package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.School;
import framework.hz.salmon.util.GlideImageLoader;
import java.util.List;

public class ParentMangerPopWin extends PopupWindow implements View.OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public String BASE_URL = "https://k12-api.openspeech.cn/";
    public View contentView;
    View footerView;
    private ImageView iv_eria2;
    private Activity mContext;
    private String mSnId;
    public View pickerContainerV;

    public static class Builder {
        /* access modifiers changed from: private */
        public Activity context;
        /* access modifiers changed from: private */
        public String mSnId;

        public Builder(Activity context2, String SnId) {
            this.context = context2;
            this.mSnId = SnId;
        }

        public ParentMangerPopWin build() {
            return new ParentMangerPopWin(this);
        }

        public Builder setData(List<School> list, StringBuffer selectSchool) {
            return this;
        }
    }

    public ParentMangerPopWin(Builder builder) {
        this.mContext = builder.context;
        this.mSnId = builder.mSnId;
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_parent_manger, (ViewGroup) null);
        this.contentView.setOnClickListener(this);
        this.iv_eria2 = (ImageView) this.contentView.findViewById(R.id.iv_eria2);
        GlideImageLoader.getInstance().displayImage_default(this.mContext, this.BASE_URL + "user/auth/qr.png?sn=" + this.mSnId, this.iv_eria2, R.drawable.tu_gray);
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