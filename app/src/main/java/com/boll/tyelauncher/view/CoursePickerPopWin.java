package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.PublisherListAdapter;
import com.toycloud.launcher.api.response.GetPublisherResponse;
import com.toycloud.launcher.util.CustomToast;
import java.util.ArrayList;
import java.util.List;

public class CoursePickerPopWin extends PopupWindow implements View.OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public Button confirmBtn;
    public View contentView;
    public EditText keyWord;
    /* access modifiers changed from: private */
    public PublisherListAdapter mAdapter;
    private Activity mContext;
    private OnPublicsherPickListener mListener;
    /* access modifiers changed from: private */
    public List<GetPublisherResponse.DataBean> orginalData = new ArrayList();
    public View pickerContainerV;
    public RecyclerView rvList;
    LinearLayout search_layout;
    StringBuffer selectedPublisher = new StringBuffer();
    /* access modifiers changed from: private */
    public List<GetPublisherResponse.DataBean> tempData = new ArrayList();
    private String title;
    public TextView titleView;

    public interface OnPublicsherPickListener {
        void onPicked(GetPublisherResponse.DataBean dataBean);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public String code;
        /* access modifiers changed from: private */
        public Activity context;
        /* access modifiers changed from: private */
        public List<GetPublisherResponse.DataBean> data;
        /* access modifiers changed from: private */
        public OnPublicsherPickListener listener;
        public String title;

        public Builder(Activity context2, OnPublicsherPickListener listener2) {
            this.context = context2;
            this.listener = listener2;
        }

        public String getTitle() {
            return this.title;
        }

        public Builder setTitle(String title2) {
            this.title = title2;
            return this;
        }

        public Builder setData(List<GetPublisherResponse.DataBean> data2) {
            this.data = data2;
            return this;
        }

        public Builder setSelected(String code2) {
            this.code = code2;
            return this;
        }

        public CoursePickerPopWin build() {
            return new CoursePickerPopWin(this);
        }
    }

    public CoursePickerPopWin(Builder builder) {
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.title = builder.title;
        this.tempData = builder.data;
        this.orginalData.addAll(this.tempData);
        Log.e("TextChanged--->5", this.tempData.size() + ":?:" + this.orginalData.size());
        if (builder.code != null) {
            this.selectedPublisher.append(builder.code);
        }
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_publish_picker, (ViewGroup) null);
        this.confirmBtn = (Button) this.contentView.findViewById(R.id.btn_confirm);
        this.search_layout = (LinearLayout) this.contentView.findViewById(R.id.search_layout);
        this.search_layout.setOnClickListener(this);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.keyWord = (EditText) this.contentView.findViewById(R.id.keyWord);
        this.keyWord.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("TextChanged--->1", s.toString() + ":?:" + CoursePickerPopWin.this.orginalData.size());
                if (TextUtils.isEmpty(s)) {
                    CoursePickerPopWin.this.tempData.clear();
                    CoursePickerPopWin.this.tempData.addAll(CoursePickerPopWin.this.orginalData);
                    CoursePickerPopWin.this.mAdapter.notifyDataSetChanged();
                    return;
                }
                Log.e("TextChanged--->6", s.toString() + ":?:" + CoursePickerPopWin.this.orginalData.size());
                CoursePickerPopWin.this.tempData.clear();
                Log.e("TextChanged--->2", s.toString() + ":?:" + CoursePickerPopWin.this.orginalData.hashCode() + "::" + CoursePickerPopWin.this.tempData.hashCode());
                for (int i = 0; i < CoursePickerPopWin.this.orginalData.size(); i++) {
                    Log.e("TextChanged--->3", s.toString() + ":?:" + ((GetPublisherResponse.DataBean) CoursePickerPopWin.this.orginalData.get(i)).getPublishername());
                    if (((GetPublisherResponse.DataBean) CoursePickerPopWin.this.orginalData.get(i)).getPublishername().contains(s.toString())) {
                        CoursePickerPopWin.this.tempData.add(CoursePickerPopWin.this.orginalData.get(i));
                        Log.e("TextChanged--->4", s.toString() + ":?:" + ((GetPublisherResponse.DataBean) CoursePickerPopWin.this.orginalData.get(i)).getPublishername());
                    }
                }
                CoursePickerPopWin.this.mAdapter.notifyDataSetChanged();
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.rvList = (RecyclerView) this.contentView.findViewById(R.id.rv_list);
        this.titleView = (TextView) this.contentView.findViewById(R.id.title);
        this.titleView.setOnClickListener(this);
        this.titleView.setText(this.title);
        this.mAdapter = new PublisherListAdapter(this.mContext, this.tempData, this.selectedPublisher);
        this.mAdapter.openLoadAnimation(1);
        this.mAdapter.isFirstOnly(true);
        this.mAdapter.setNotDoAnimationCount(4);
        this.rvList.setHasFixedSize(true);
        this.rvList.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.rvList.setAdapter(this.mAdapter);
        this.rvList.addOnItemTouchListener(new OnItemClickListener() {
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                CoursePickerPopWin.this.selectedPublisher.delete(0, CoursePickerPopWin.this.selectedPublisher.length());
                CoursePickerPopWin.this.selectedPublisher.append(((GetPublisherResponse.DataBean) CoursePickerPopWin.this.tempData.get(position)).getPublishercode());
                CoursePickerPopWin.this.mAdapter.notifyDataSetChanged();
            }
        });
        this.confirmBtn.setOnClickListener(this);
        this.contentView.setOnClickListener(this);
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

    public void onClick(View v) {
        if (v == this.contentView) {
            dismissPopWin();
        } else if (v != this.confirmBtn) {
            if (v == this.titleView || v != this.search_layout) {
            }
        } else if (this.mListener == null) {
        } else {
            if (this.selectedPublisher.toString().length() == 0) {
                CustomToast.showToast((Context) this.mContext, "请选择出版社", 0);
                return;
            }
            for (int i = 0; i < this.tempData.size(); i++) {
                if (this.tempData.get(i).getPublishercode().equals(this.selectedPublisher.toString())) {
                    this.mListener.onPicked(this.tempData.get(i));
                    dismissPopWin();
                }
            }
        }
    }
}