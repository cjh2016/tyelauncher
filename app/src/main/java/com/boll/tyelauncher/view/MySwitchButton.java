package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.toycloud.launcher.R;

public class MySwitchButton extends RelativeLayout {
    public boolean isOpen = false;
    private RelativeLayout rel_close;
    private RelativeLayout rel_open;

    public MySwitchButton(Context context) {
        super(context);
    }

    public MySwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_myswithc_button, this, true);
    }

    public MySwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.rel_open = (RelativeLayout) findViewById(R.id.rel_open_status);
        this.rel_close = (RelativeLayout) findViewById(R.id.rel_close_status);
    }

    public void setOpenStatus() {
        this.rel_open.setVisibility(0);
        this.rel_close.setVisibility(8);
        this.isOpen = true;
    }

    public void setCloseStatus() {
        this.rel_open.setVisibility(8);
        this.rel_close.setVisibility(0);
        this.isOpen = false;
    }
}