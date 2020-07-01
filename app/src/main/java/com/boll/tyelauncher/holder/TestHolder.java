package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.R;

public class TestHolder extends BaseHolder {
    private static final String TAG = "TestHolder";
    private TextView mTextView;

    public TestHolder(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.holder_test;
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.mTextView = (TextView) rootView.findViewById(R.id.test);
    }

    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate");
    }

    public void onActivityPause(boolean isCurrentPage) {
        super.onActivityPause(isCurrentPage);
        Logger.d(TAG, "onActivityPause: " + isCurrentPage);
    }

    public void onActivityResume(boolean isCurrentPage) {
        super.onActivityResume(isCurrentPage);
        Logger.d(TAG, "onActivityResume: " + isCurrentPage);
    }

    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy");
    }

    public void onAttachToAdapter(boolean isCurrentPage, boolean isTopActivity) {
        super.onAttachToAdapter(isCurrentPage, isTopActivity);
        Logger.d(TAG, "onAttachToAdapter: " + isCurrentPage);
    }

    public void onDetachFromAdapter(boolean isCurrentPage) {
        super.onDetachFromAdapter(isCurrentPage);
        Logger.d(TAG, "onDetachFromAdapter: " + isCurrentPage);
    }
}
