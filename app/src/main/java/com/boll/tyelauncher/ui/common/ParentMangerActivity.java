package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import framework.hz.salmon.base.BaseFragmentActivity;

public class ParentMangerActivity extends BaseFragmentActivity {
    ImageView iv_back;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_manger);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().getDecorView().setSystemUiVisibility(9216);
        }
        this.iv_back = (ImageView) findViewById(R.id.iv_back);
        this.iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParentMangerActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    public void onClick(View v) {
    }
}
