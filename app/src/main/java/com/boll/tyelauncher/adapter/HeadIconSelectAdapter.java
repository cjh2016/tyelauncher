package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import com.toycloud.launcher.util.SharepreferenceUtil;
import java.util.ArrayList;
import java.util.List;

public class HeadIconSelectAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    /* access modifiers changed from: private */
    public clickHeadIcon clickHeadIcon;
    /* access modifiers changed from: private */
    public boolean isFirstShow = true;
    private ArrayList<Boolean> selectstatus = new ArrayList<>();

    public interface clickHeadIcon {
        void headIconClick(int i);
    }

    public HeadIconSelectAdapter(int layoutResId, @Nullable List<Integer> data, clickHeadIcon clickHeadIcon2) {
        super(layoutResId, data);
        initSelectStatus(-1);
        this.isFirstShow = true;
        this.clickHeadIcon = clickHeadIcon2;
    }

    /* access modifiers changed from: private */
    public void initSelectStatus(int selectPosition) {
        this.selectstatus.clear();
        for (int i = 0; i < 6; i++) {
            if (i == selectPosition) {
                this.selectstatus.add(true);
            } else {
                this.selectstatus.add(false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder helper, Integer item) {
        helper.setImageResource(R.id.item_headicon, item.intValue());
        ImageView bg = (ImageView) helper.getView(R.id.item_headicon_bg);
        final int position = helper.getLayoutPosition();
        int selectHeadIcon = SharepreferenceUtil.getSharepferenceInstance(this.mContext).getSelectHeadIcon();
        if (this.isFirstShow) {
            initSelectStatus(selectHeadIcon);
        }
        if (this.selectstatus.get(position).booleanValue()) {
            bg.setImageResource(R.drawable.drawable_headicon_bluebg);
        } else {
            bg.setImageResource(R.drawable.drawable_headicon_greybg);
        }
        helper.getView(R.id.item_headicon).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean unused = HeadIconSelectAdapter.this.isFirstShow = false;
                if (position == 5 || position == 4) {
                    HeadIconSelectAdapter.this.clickHeadIcon.headIconClick(position);
                    return;
                }
                HeadIconSelectAdapter.this.initSelectStatus(position);
                HeadIconSelectAdapter.this.setDataChange();
                HeadIconSelectAdapter.this.clickHeadIcon.headIconClick(position);
            }
        });
    }

    public void setDataChange() {
        notifyDataSetChanged();
    }
}