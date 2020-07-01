package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.toycloud.launcher.R;
import com.toycloud.launcher.util.PadInfoUtil;
import com.toycloud.launcher.view.DialogUtil;

public class Launcher_Welcome_ViewHolder extends BaseHolder {
    private Button bu_welcome_tonext;
    private Context mContext;
    long[] mHints = new long[10];
    /* access modifiers changed from: private */
    public MainClickListener mainClickListener;
    private TextView welcome_tonextt;

    public interface MainClickListener {
        void Next();
    }

    public Launcher_Welcome_ViewHolder(Context context, MainClickListener mainClickListener2) {
        super(context);
        this.mContext = context;
        this.mainClickListener = mainClickListener2;
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.activity_wel_come_new;
    }

    /* access modifiers changed from: protected */
    public void initView(final Context context, View rootView) {
        this.welcome_tonextt = (TextView) rootView.findViewById(R.id.welcome_tonextt);
        this.bu_welcome_tonext = (Button) rootView.findViewById(R.id.bu_welcome_tonext);
        this.bu_welcome_tonext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Launcher_Welcome_ViewHolder.this.mainClickListener.Next();
            }
        });
        this.welcome_tonextt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Launcher_Welcome_ViewHolder.this.isClickThreeTime()) {
                    DialogUtil.showSNDialog(context, new PadInfoUtil(context).getSnCode());
                }
            }
        });
    }

    public boolean isClickThreeTime() {
        System.arraycopy(this.mHints, 1, this.mHints, 0, this.mHints.length - 1);
        this.mHints[this.mHints.length - 1] = SystemClock.uptimeMillis();
        if (SystemClock.uptimeMillis() - this.mHints[0] <= 2000) {
            return true;
        }
        return false;
    }
}
