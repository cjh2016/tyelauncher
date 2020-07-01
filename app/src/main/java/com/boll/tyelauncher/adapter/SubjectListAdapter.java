package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.app.Activity;
import android.text.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import com.toycloud.launcher.model.BookInfo;
import java.util.List;

public class SubjectListAdapter extends BaseQuickAdapter<BookInfo.DataBean, BaseViewHolder> {
    Activity mActivity;

    public SubjectListAdapter(Activity mActivity2, List<BookInfo.DataBean> data) {
        super(R.layout.layout_subject_item, data);
        this.mActivity = mActivity2;
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder helper, BookInfo.DataBean item) {
        String subjectname = item.getSubject().getSubjectname();
        String publishname = item.getPublisher().getPublishername();
        helper.setText((int) R.id.subject_name, (CharSequence) subjectname);
        if (TextUtils.isEmpty(publishname)) {
            helper.setText((int) R.id.is_setted, (CharSequence) "未设置");
        } else {
            helper.setText((int) R.id.is_setted, (CharSequence) publishname);
        }
        if (!TextUtils.isEmpty(subjectname)) {
            if (subjectname.equals("语文")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_yw);
            } else if (subjectname.equals("数学")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_sx);
            } else if (subjectname.equals("英语")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_yy);
            } else if (subjectname.equals("物理")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_wl);
            } else if (subjectname.equals("化学")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_hx);
            } else if (subjectname.equals("生物")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_sw);
            } else if (subjectname.equals("政治") || subjectname.equals("思想政治")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_zz);
            } else if (subjectname.equals("历史")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_ls);
            } else if (subjectname.equals("地理")) {
                helper.setImageResource(R.id.jc_icon, R.drawable.icon_user_dl);
            }
        }
    }
}