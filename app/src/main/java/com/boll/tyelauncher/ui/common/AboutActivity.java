package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.toycloud.launcher.R;
import framework.hz.salmon.base.BaseActivity;

public class AboutActivity extends BaseActivity {
    @BindView(2131689661)
    ImageView iv_back;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_newstlye);
        ButterKnife.bind((Activity) this);
    }

    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({2131689661})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                return;
            default:
                return;
        }
    }

    public String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void onClick(View v) {
    }
}