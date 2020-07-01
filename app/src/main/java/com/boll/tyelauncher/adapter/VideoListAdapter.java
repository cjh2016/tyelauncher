package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.app.Activity;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.response.GetVideosResponse;
import framework.hz.salmon.util.GlideImageLoader;
import java.util.List;

public class VideoListAdapter extends BaseQuickAdapter<GetVideosResponse.DataBean, BaseViewHolder> {
    Activity mActivity;

    public VideoListAdapter(Activity mActivity2, List<GetVideosResponse.DataBean> data) {
        super(R.layout.layout_video_item, data);
        this.mActivity = mActivity2;
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder helper, GetVideosResponse.DataBean item) {
        helper.setText((int) R.id.video_name, (CharSequence) item.getTitle());
        GlideImageLoader.getInstance().displayImage(this.mActivity, item.getThumbnail(), (ImageView) helper.getView(R.id.icon));
    }
}