package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import java.util.List;

public class GradeItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Activity mActivity;
    private StringBuffer selectedGrade;

    public GradeItemAdapter(Activity mActivity2, List<String> data, StringBuffer selectedGrade2) {
        super(R.layout.layout_grade_child_item, data);
        this.mActivity = mActivity2;
        this.selectedGrade = selectedGrade2;
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder helper, String item) {
        helper.setText((int) R.id.grade_name, (CharSequence) item);
        if (this.selectedGrade == null || !this.selectedGrade.toString().equals(item)) {
            helper.setTextColor(R.id.grade_name, -13421773);
            ((ImageView) helper.getView(R.id.grade_status)).setImageDrawable((Drawable) null);
            return;
        }
        helper.setTextColor(R.id.grade_name, -16740609);
        ((ImageView) helper.getView(R.id.grade_status)).setImageResource(R.drawable.gx);
    }
}
