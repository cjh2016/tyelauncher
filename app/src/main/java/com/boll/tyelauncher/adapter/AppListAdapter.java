package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.app.Activity;
import android.view.View;
import com.anarchy.classify.simple.AppInfo;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import java.util.List;

public class AppListAdapter extends BaseItemDraggableAdapter<AppInfo, BaseViewHolder> {
    /* access modifiers changed from: private */
    public DeletePhotoLister lister;
    private final Activity mActivity;
    private int status;
    private int type = this.type;

    public interface DeletePhotoLister {
        void onDelete(int i);
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public AppListAdapter(Activity activity, int status2, List<AppInfo> authors, DeletePhotoLister lister2) {
        super(R.layout.layout_app_item, authors);
        this.lister = lister2;
        this.mActivity = activity;
        this.status = status2;
    }

    public void setOnItemLongClickListener(BaseQuickAdapter.OnItemLongClickListener listener) {
        super.setOnItemLongClickListener(listener);
    }

    /* access modifiers changed from: protected */
    public void convert(final BaseViewHolder helper, AppInfo item) {
        helper.setImageDrawable(R.id.iv_appicon, item.getIcon());
        helper.setText((int) R.id.tv_appname, (CharSequence) item.getAppName());
        if (this.status == 0) {
            helper.getView(R.id.uninstall).setVisibility(8);
        } else {
            helper.getView(R.id.uninstall).setVisibility(0);
        }
        helper.getView(R.id.uninstall).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AppListAdapter.this.lister != null) {
                    AppListAdapter.this.lister.onDelete(helper.getAdapterPosition());
                }
            }
        });
    }
}
