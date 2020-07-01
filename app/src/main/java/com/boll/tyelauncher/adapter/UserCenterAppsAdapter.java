package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.AppInfo;
import java.util.List;

public class UserCenterAppsAdapter extends BaseQuickAdapter<AppInfo, BaseViewHolder> {
    private static final String TAG = "AppsAdapter";

    public UserCenterAppsAdapter(int layoutResId, @Nullable List<AppInfo> data) {
        super(layoutResId, data);
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder helper, AppInfo item) {
        if (item != null) {
            ((TextView) helper.getView(R.id.tv_app_name)).setText(item.getAppName());
            ((ImageView) helper.getView(R.id.img_app_logo)).setImageDrawable(item.getAppIcon());
        }
    }
}
