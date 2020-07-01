package com.boll.tyelauncher.ui;
package com.toycloud.launcher.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import com.toycloud.launcher.R;
import com.toycloud.launcher.util.LogSaveManager_Util;
import framework.hz.salmon.base.AppManager;

public class CustomErrorActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_custom_error);
        AppManager.getAppManager().addActivity(this);
        TextView errorDetailsText = (TextView) findViewById(R.id.show_detail);
        Button restartButton = (Button) findViewById(R.id.restart_button);
        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        if (config == null) {
            finish();
            return;
        }
        if (!config.isShowRestartButton() || config.getRestartActivityClass() == null) {
            restartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(CustomErrorActivity.this, config);
                }
            });
        } else {
            restartButton.setText("重启应用");
            restartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(CustomErrorActivity.this, config);
                }
            });
        }
        if (config.isShowErrorDetails()) {
            errorDetailsText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    TextView textView = (TextView) new AlertDialog.Builder(CustomErrorActivity.this).setTitle((int) R.string.customactivityoncrash_error_activity_error_details_title).setMessage((CharSequence) CustomActivityOnCrash.getAllErrorDetailsFromIntent(CustomErrorActivity.this, CustomErrorActivity.this.getIntent())).setPositiveButton((CharSequence) "关闭", (DialogInterface.OnClickListener) null).setNeutralButton((CharSequence) "复制", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            CustomErrorActivity.this.copyErrorToClipboard();
                        }
                    }).show().findViewById(16908299);
                    if (textView != null) {
                        textView.setTextSize(0, CustomErrorActivity.this.getResources().getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size));
                    }
                }
            });
        } else {
            errorDetailsText.setVisibility(8);
        }
        LogSaveManager_Util.saveLog(this, CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent()));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    /* access modifiers changed from: private */
    public void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent());
        ClipboardManager clipboard = (ClipboardManager) getSystemService("clipboard");
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText(getString(R.string.customactivityoncrash_error_activity_error_details_clipboard_label), errorInformation));
            Toast.makeText(this, "复制成功", 0).show();
        }
    }
}