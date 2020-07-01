package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.School;
import java.util.List;

public class SchoolListAdapter extends BaseQuickAdapter<School, BaseViewHolder> {
    Activity mActivity;
    public int position = 0;
    private StringBuffer selectedGrade;

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position2) {
        this.position = position2;
    }

    public SchoolListAdapter(Activity mActivity2, List<School> data, StringBuffer selectedGrade2) {
        super(R.layout.layout_grade_child_item, data);
        this.mActivity = mActivity2;
        this.selectedGrade = selectedGrade2;
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder helper, School item) {
        helper.setText((int) R.id.grade_name, (CharSequence) item.getSchoolname());
        if (this.selectedGrade == null || !this.selectedGrade.toString().equals(item.getSchoolname())) {
            helper.setTextColor(R.id.grade_name, -13421773);
            ((ImageView) helper.getView(R.id.grade_status)).setImageDrawable((Drawable) null);
            return;
        }
        helper.setTextColor(R.id.grade_name, -16740609);
        ((ImageView) helper.getView(R.id.grade_status)).setImageResource(R.drawable.gx);
        this.position = helper.getAdapterPosition();
        item.setSelectPosition(this.position);
    }
}