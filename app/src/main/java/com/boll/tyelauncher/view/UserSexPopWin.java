package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.toycloud.launcher.R;

public class UserSexPopWin extends PopupWindow implements View.OnClickListener {
    Button btu_confirm;
    LinearLayout containerPicker;
    private View contentView;
    private RelativeLayout female_layout;
    ImageView iv_sex_boy;
    ImageView iv_sex_girl;
    private Context mContext;
    /* access modifiers changed from: private */
    public OnSexPickListener mListener;
    private RelativeLayout male_layout;
    /* access modifiers changed from: private */
    public int sex = -1;
    private TextView title;
    TextView tv_sex_boy;
    TextView tv_sex_girl;

    public interface OnSexPickListener {
        void onPicked(int i, String str);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public OnSexPickListener listener;
        /* access modifiers changed from: private */
        public int sex;
        private boolean showDayMonthYear = false;

        public Builder(Context context2, OnSexPickListener listener2) {
            this.context = context2;
            this.listener = listener2;
        }

        public Builder setSex(int s) {
            this.sex = s;
            return this;
        }

        public UserSexPopWin build() {
            return new UserSexPopWin(this);
        }
    }

    public UserSexPopWin(Builder builder) {
        this.sex = builder.sex;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_user_sex_newstyle, (ViewGroup) null);
        this.contentView.setOnClickListener(this);
        this.title = (TextView) this.contentView.findViewById(R.id.title);
        this.title.setOnClickListener(this);
        this.male_layout = (RelativeLayout) this.contentView.findViewById(R.id.male_layout);
        this.female_layout = (RelativeLayout) this.contentView.findViewById(R.id.female_layout);
        this.iv_sex_boy = (ImageView) this.contentView.findViewById(R.id.iv_sex_boy);
        this.iv_sex_girl = (ImageView) this.contentView.findViewById(R.id.iv_sex_girl);
        this.tv_sex_boy = (TextView) this.contentView.findViewById(R.id.tv_sex_boy);
        this.tv_sex_girl = (TextView) this.contentView.findViewById(R.id.tv_sex_girl);
        this.btu_confirm = (Button) this.contentView.findViewById(R.id.btu_confirm);
        if (this.sex == 1) {
            this.iv_sex_boy.setImageResource(R.drawable.selsex_boy_pre);
            this.iv_sex_girl.setImageResource(R.drawable.selsex_girl);
            this.tv_sex_boy.setTextColor(Color.parseColor("#03aafe"));
            this.tv_sex_girl.setTextColor(Color.parseColor("#333333"));
        } else if (this.sex == -1) {
            this.iv_sex_boy.setImageResource(R.drawable.selsex_boy);
            this.iv_sex_girl.setImageResource(R.drawable.selsex_girl);
            this.tv_sex_boy.setTextColor(Color.parseColor("#333333"));
            this.tv_sex_girl.setTextColor(Color.parseColor("#333333"));
        } else {
            this.iv_sex_boy.setImageResource(R.drawable.selsex_boy);
            this.iv_sex_girl.setImageResource(R.drawable.selsex_girl_pre);
            this.tv_sex_boy.setTextColor(Color.parseColor("#333333"));
            this.tv_sex_girl.setTextColor(Color.parseColor("#FB547E"));
        }
        this.iv_sex_boy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserSexPopWin.this.iv_sex_boy.setImageResource(R.drawable.selsex_boy_pre);
                UserSexPopWin.this.iv_sex_girl.setImageResource(R.drawable.selsex_girl);
                UserSexPopWin.this.tv_sex_boy.setTextColor(Color.parseColor("#03aafe"));
                UserSexPopWin.this.tv_sex_girl.setTextColor(Color.parseColor("#333333"));
                int unused = UserSexPopWin.this.sex = 1;
                UserSexPopWin.this.mListener.onPicked(1, "男");
                UserSexPopWin.this.dismissPopWin();
            }
        });
        this.iv_sex_girl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserSexPopWin.this.iv_sex_boy.setImageResource(R.drawable.selsex_boy);
                UserSexPopWin.this.iv_sex_girl.setImageResource(R.drawable.selsex_girl_pre);
                UserSexPopWin.this.tv_sex_boy.setTextColor(Color.parseColor("#333333"));
                UserSexPopWin.this.tv_sex_girl.setTextColor(Color.parseColor("#FB547E"));
                int unused = UserSexPopWin.this.sex = 2;
                UserSexPopWin.this.mListener.onPicked(2, "女");
                UserSexPopWin.this.dismissPopWin();
            }
        });
        this.btu_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (UserSexPopWin.this.sex == 1) {
                    UserSexPopWin.this.mListener.onPicked(1, "男");
                    UserSexPopWin.this.dismissPopWin();
                } else if (UserSexPopWin.this.sex == 2) {
                    UserSexPopWin.this.mListener.onPicked(2, "女");
                    UserSexPopWin.this.dismissPopWin();
                } else if (UserSexPopWin.this.sex == -1) {
                    UserSexPopWin.this.mListener.onPicked(-1, "未知");
                    UserSexPopWin.this.dismissPopWin();
                }
            }
        });
        this.containerPicker = (LinearLayout) this.contentView.findViewById(R.id.container_picker);
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true);
        setClippingEnabled(false);
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

    public void onClick(View view) {
        if (view == this.contentView) {
            dismissPopWin();
        } else if (view == this.male_layout) {
            if (this.mListener != null) {
                this.mListener.onPicked(1, "男");
            }
        } else if (view != this.female_layout) {
            if (view == this.title) {
            }
        } else if (this.mListener != null) {
            this.mListener.onPicked(2, "女");
        }
    }
}
