package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.GradeListAdapter;
import com.toycloud.launcher.api.response.GetGradeResponse;
import com.toycloud.launcher.util.CustomToast;
import java.util.List;

public class UserGradePopWin extends PopupWindow implements View.OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public Button confirmBtn;
    public View contentView;
    /* access modifiers changed from: private */
    public final List<GetGradeResponse.DataBean> data;
    /* access modifiers changed from: private */
    public GradeListAdapter mAdapter;
    private Activity mContext;
    private OnGradePickedListener mListener;
    public View pickerContainerV;
    public RecyclerView rvList;
    StringBuffer selecteGrade = new StringBuffer();
    private GetGradeResponse.DataBean selectedGradeBean;
    private View title;

    public interface OnGradePickedListener {
        void onPicked(GetGradeResponse.DataBean dataBean);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public Activity context;
        /* access modifiers changed from: private */
        public List<GetGradeResponse.DataBean> data;
        /* access modifiers changed from: private */
        public OnGradePickedListener listener;
        /* access modifiers changed from: private */
        public GetGradeResponse.DataBean selectedGradeBean;

        public Builder(Activity context2, OnGradePickedListener listener2) {
            this.context = context2;
            this.listener = listener2;
        }

        public Builder setData(List<GetGradeResponse.DataBean> data2) {
            this.data = data2;
            return this;
        }

        public UserGradePopWin build() {
            return new UserGradePopWin(this);
        }

        public Builder setGrade(GetGradeResponse.DataBean selectedGradeBean2) {
            this.selectedGradeBean = selectedGradeBean2;
            return this;
        }
    }

    public UserGradePopWin(Builder builder) {
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.data = builder.data;
        this.selectedGradeBean = builder.selectedGradeBean;
        initView();
    }

    private void initView() {
        if (this.selectedGradeBean != null) {
            this.selecteGrade.append(this.selectedGradeBean.getGradename());
        }
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_grade_picker, (ViewGroup) null);
        this.title = this.contentView.findViewById(R.id.title);
        this.title.setOnClickListener(this);
        this.confirmBtn = (Button) this.contentView.findViewById(R.id.btn_confirm);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.rvList = (RecyclerView) this.contentView.findViewById(R.id.rv_list);
        this.mAdapter = new GradeListAdapter(this.mContext, this.data, this.selecteGrade);
        this.mAdapter.openLoadAnimation(1);
        this.mAdapter.isFirstOnly(true);
        this.mAdapter.setNotDoAnimationCount(4);
        this.rvList.setHasFixedSize(true);
        this.rvList.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.rvList.setAdapter(this.mAdapter);
        this.rvList.addOnItemTouchListener(new OnItemClickListener() {
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserGradePopWin.this.selecteGrade.delete(0, UserGradePopWin.this.selecteGrade.length());
                UserGradePopWin.this.selecteGrade.append(((GetGradeResponse.DataBean) UserGradePopWin.this.data.get(position)).getGradename());
                UserGradePopWin.this.mAdapter.notifyDataSetChanged();
            }
        });
        this.confirmBtn.setOnClickListener(this);
        this.contentView.setOnClickListener(this);
        setTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setClippingEnabled(false);
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
        } else if (v != this.confirmBtn) {
            if (v == this.title) {
            }
        } else if (this.selecteGrade.length() == 0) {
            CustomToast.showToast((Context) this.mContext, "请选择年级");
        } else {
            for (int i = 0; i < this.data.size(); i++) {
                if (this.data.get(i).getGradename().equals(this.selecteGrade.toString())) {
                    if (this.mListener != null) {
                        this.mListener.onPicked(this.data.get(i));
                    }
                    dismissPopWin();
                }
            }
        }
    }
}
