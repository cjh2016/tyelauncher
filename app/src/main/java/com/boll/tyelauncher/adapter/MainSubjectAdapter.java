package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.app.Activity;
import android.util.Log;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import com.toycloud.launcher.model.BookInfo;
import java.util.List;

public class MainSubjectAdapter extends BaseQuickAdapter<BookInfo.DataBean, BaseViewHolder> {
    Activity mActivity;

    public MainSubjectAdapter(Activity mActivity2, List<BookInfo.DataBean> data) {
        super(R.layout.layout_item_mainsubject, data);
        this.mActivity = mActivity2;
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder helper, BookInfo.DataBean item) {
        String name = item.getSubject().getSubjectname();
        Log.e("name--->", name);
        if (name.equals("语文")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_chinese);
        } else if (name.equals("数学")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_mathematics);
        } else if (name.equals("英语")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_english);
        } else if (name.equals("物理")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_physical);
        } else if (name.equals("化学")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_chemistry);
        } else if (name.equals("思想政治") || name.equals("政治")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_political);
        } else if (name.equals("历史")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_history);
        } else if (name.equals("生物")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_biological);
        } else if (name.equals("地理")) {
            helper.setImageResource(R.id.iv_subject, R.drawable.subject_geography);
        }
    }
}