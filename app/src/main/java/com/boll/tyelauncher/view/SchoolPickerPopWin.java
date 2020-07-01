package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.SchoolListAdapter;
import com.toycloud.launcher.api.model.School;
import com.toycloud.launcher.util.CustomToast;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;

public class SchoolPickerPopWin extends PopupWindow implements View.OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public TextView confirmBtn;
    public View contentView;
    /* access modifiers changed from: private */
    public List<School> data;
    View footerView;
    private EditText keyWord;
    /* access modifiers changed from: private */
    public SchoolListAdapter mAdapter;
    /* access modifiers changed from: private */
    public Activity mContext;
    /* access modifiers changed from: private */
    public OnSchoolPickedListener mListener;
    public View pickerContainerV;
    public RecyclerView rvList;
    LinearLayout search_layout;
    StringBuffer selectedSchool = new StringBuffer();
    /* access modifiers changed from: private */
    public List<School> tempData = new ArrayList();
    private TextView title;

    public interface OnSchoolPickedListener {
        void onPick(School school, String str);
    }

    public SchoolPickerPopWin setData(List<School> data2) {
        this.data = data2;
        this.tempData.clear();
        this.tempData.addAll(data2);
        return this;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public Activity context;
        /* access modifiers changed from: private */
        public List<School> data;
        /* access modifiers changed from: private */
        public OnSchoolPickedListener listener;
        /* access modifiers changed from: private */
        public StringBuffer selectedSchool;
        private List<School> tempData = new ArrayList();

        public Builder(Activity context2, OnSchoolPickedListener listener2) {
            this.context = context2;
            this.listener = listener2;
        }

        public SchoolPickerPopWin build() {
            return new SchoolPickerPopWin(this);
        }

        public Builder setData(List<School> data2, StringBuffer selectSchool) {
            this.data = data2;
            this.selectedSchool = selectSchool;
            this.tempData.clear();
            this.tempData.addAll(data2);
            return this;
        }
    }

    public SchoolPickerPopWin(Builder builder) {
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.data = builder.data;
        this.selectedSchool = builder.selectedSchool;
        this.tempData.clear();
        this.tempData.addAll(this.data);
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_scholl_picker, (ViewGroup) null);
        this.title = (TextView) this.contentView.findViewById(R.id.title);
        this.title.setOnClickListener(this);
        this.search_layout = (LinearLayout) this.contentView.findViewById(R.id.search_layout);
        this.search_layout.setOnClickListener(this);
        this.confirmBtn = (TextView) this.contentView.findViewById(R.id.btn_confirm);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.rvList = (RecyclerView) this.contentView.findViewById(R.id.rv_list);
        this.keyWord = (EditText) this.contentView.findViewById(R.id.keyWord);
        this.keyWord.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    SchoolPickerPopWin.this.tempData.clear();
                    SchoolPickerPopWin.this.tempData.addAll(SchoolPickerPopWin.this.data);
                    SchoolPickerPopWin.this.mAdapter.notifyDataSetChanged();
                    return;
                }
                SchoolPickerPopWin.this.tempData.clear();
                for (int i = 0; i < SchoolPickerPopWin.this.data.size(); i++) {
                    if (((School) SchoolPickerPopWin.this.data.get(i)).getSchoolname().contains(s.toString())) {
                        SchoolPickerPopWin.this.tempData.add(SchoolPickerPopWin.this.data.get(i));
                    }
                }
                SchoolPickerPopWin.this.mAdapter.notifyDataSetChanged();
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.mAdapter = new SchoolListAdapter(this.mContext, this.tempData, this.selectedSchool);
        this.rvList.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.footerView = this.mContext.getLayoutInflater().inflate(R.layout.layout_addschool_foot, (ViewGroup) null);
        this.footerView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        this.mAdapter.addFooterView(this.footerView);
        this.rvList.setAdapter(this.mAdapter);
        this.rvList.scrollToPosition(SharepreferenceUtil.getSharepferenceInstance(this.mContext).getSchoolSelectPosition());
        Log.e("selectedSchool-->", SharepreferenceUtil.getSharepferenceInstance(this.mContext).getSchoolSelectPosition() + "");
        this.rvList.addOnItemTouchListener(new OnItemClickListener() {
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SchoolPickerPopWin.this.selectedSchool.delete(0, SchoolPickerPopWin.this.selectedSchool.length());
                SchoolPickerPopWin.this.selectedSchool.append(((School) SchoolPickerPopWin.this.tempData.get(position)).getSchoolname());
                SchoolPickerPopWin.this.mAdapter.notifyDataSetChanged();
            }
        });
        this.footerView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SchoolPickerPopWin.this.dismissPopWin();
                MyDialog_AddSchool showDilogToRegs_AddSchool = DialogUtil.showDilogToRegs_AddSchool(SchoolPickerPopWin.this.mContext, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String schoolName = ((MyDialog_AddSchool) dialog).getTv_content().getText().toString();
                        if (TextUtils.isEmpty(schoolName)) {
                            ToastUtils.showLong((CharSequence) "请输入学校名称");
                            return;
                        }
                        SchoolPickerPopWin.this.mListener.onPick((School) null, schoolName);
                        dialog.dismiss();
                    }
                });
            }
        });
        this.confirmBtn.setOnClickListener(this);
        this.contentView.setOnClickListener(this);
        setClippingEnabled(false);
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
        } else if (v != this.confirmBtn) {
            if (v == this.title || v != this.search_layout) {
            }
        } else if (this.selectedSchool.length() == 0) {
            if (this.keyWord.getText().toString().length() != 0) {
                CustomToast.showToast((Context) this.mContext, "未找到该学校", 0);
            } else {
                CustomToast.showToast((Context) this.mContext, "请选择学校", 0);
            }
            dismissPopWin();
        } else {
            for (int i = 0; i < this.data.size(); i++) {
                if (this.data.get(i).getSchoolname().equals(this.selectedSchool.toString())) {
                    if (this.mListener != null) {
                        this.mListener.onPick(this.data.get(i), "");
                    }
                    dismissPopWin();
                }
            }
        }
    }
}
