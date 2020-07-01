package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.toycloud.launcher.R;
import com.toycloud.launcher.helper.UnitModelBean;
import com.toycloud.launcher.util.NumberConvertUtil;
import com.toycloud.launcher.util.StarUtils;
import java.util.List;

public class EnglishModelAdapter extends BaseQuickAdapter<UnitModelBean, BaseViewHolder> {
    private static final String TAG = "EnglishModelAdapter";

    public EnglishModelAdapter(int layoutResId, @Nullable List<UnitModelBean> data) {
        super(layoutResId, data);
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder helper, UnitModelBean item) {
        if (item != null) {
            ((TextView) helper.getView(R.id.tv_title)).setText(item.getModuleTitle());
            ((TextView) helper.getView(R.id.tv_sub_title)).setText(item.getModuleSubTitle());
            View viewLine = helper.getView(R.id.view3);
            if (item.getData() != null) {
                ((TextView) helper.getView(R.id.tv_unit)).setText(item.getData().getUnitName());
                float score = item.getData().getScore();
                float totalScore = item.getData().getTotalScore();
                if (totalScore > 0.0f) {
                    ((TextView) helper.getView(R.id.tv_score)).setText(Html.fromHtml(String.format(this.mContext.getResources().getString(R.string.score_style), new Object[]{Integer.toString(NumberConvertUtil.parseInt5(Float.valueOf(score))), Integer.toString(NumberConvertUtil.parseInt5(Float.valueOf(totalScore)))})));
                    SimpleRatingBar simpleRatingBar = (SimpleRatingBar) helper.getView(R.id.srb_score);
                    if (simpleRatingBar.getVisibility() == 4) {
                        simpleRatingBar.setVisibility(0);
                    }
                    if (viewLine.getVisibility() == 4) {
                        viewLine.setVisibility(0);
                    }
                    ((SimpleRatingBar) helper.getView(R.id.srb_score)).setRating(StarUtils.getStartCount(score, totalScore));
                    return;
                }
                SimpleRatingBar simpleRatingBar2 = (SimpleRatingBar) helper.getView(R.id.srb_score);
                simpleRatingBar2.setRating(0.0f);
                if (simpleRatingBar2.getVisibility() == 0) {
                    simpleRatingBar2.setVisibility(4);
                }
                if (viewLine.getVisibility() == 0) {
                    viewLine.setVisibility(4);
                }
                ((TextView) helper.getView(R.id.tv_score)).setText(this.mContext.getString(R.string.default_text));
                ((TextView) helper.getView(R.id.tv_unit)).setText(this.mContext.getString(R.string.default_unit));
                return;
            }
            ((TextView) helper.getView(R.id.tv_unit)).setText(this.mContext.getString(R.string.default_unit));
            ((TextView) helper.getView(R.id.tv_score)).setText(this.mContext.getString(R.string.default_text));
            SimpleRatingBar simpleRatingBar3 = (SimpleRatingBar) helper.getView(R.id.srb_score);
            simpleRatingBar3.setRating(0.0f);
            if (simpleRatingBar3.getVisibility() == 0) {
                simpleRatingBar3.setVisibility(4);
            }
            if (viewLine.getVisibility() == 0) {
                viewLine.setVisibility(4);
            }
        }
    }
}