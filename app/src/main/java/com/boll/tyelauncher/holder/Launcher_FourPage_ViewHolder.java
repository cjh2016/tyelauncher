package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.gson.GsonBuilder;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.MainSubjectAdapter;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.BookInfo;
import java.util.List;

public class Launcher_FourPage_ViewHolder extends BaseHolder implements View.OnClickListener {
    private List<BookInfo.DataBean> data;
    private Context mContext;
    private MainSubjectAdapter mainSubjectAdapter;
    private RecyclerView rv_subject;

    public Launcher_FourPage_ViewHolder(Context context) {
        super(context);
        this.mContext = context;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.fragment_fragment__subject;
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.rv_subject = (RecyclerView) rootView.findViewById(R.id.rv_subject);
        this.data = ((BookInfo) new GsonBuilder().create().fromJson(GlobalVariable.SUBJECTSTRING, BookInfo.class)).getData();
        this.mainSubjectAdapter = new MainSubjectAdapter((Activity) this.mContext, this.data);
        this.rv_subject.setLayoutManager(new GridLayoutManager(this.mContext, 3));
        this.rv_subject.setAdapter(this.mainSubjectAdapter);
    }

    public void onClick(View view) {
    }
}
