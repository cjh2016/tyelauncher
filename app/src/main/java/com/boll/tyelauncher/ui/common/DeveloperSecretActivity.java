package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import framework.hz.salmon.base.BaseFragmentActivity;

public class DeveloperSecretActivity extends BaseFragmentActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_secret);
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
        findViewById(R.id.rel_to_call).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent_tel = new Intent("android.intent.action.DIAL", Uri.parse("tel:"));
                    intent_tel.setFlags(268435456);
                    DeveloperSecretActivity.this.startActivity(intent_tel);
                } catch (Throwable th) {
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }

    public void onClick(View v) {
    }
}
