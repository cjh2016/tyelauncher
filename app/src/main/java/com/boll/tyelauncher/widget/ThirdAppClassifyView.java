package com.boll.tyelauncher.widget;

package com.toycloud.launcher.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.anarchy.classify.ClassifyView;
import com.toycloud.launcher.R;

public class ThirdAppClassifyView extends ClassifyView {
    public ThirdAppClassifyView(Context context) {
        super(context);
    }

    public ThirdAppClassifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThirdAppClassifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* access modifiers changed from: protected */
    public Dialog createSubDialog() {
        Dialog dialog = new Dialog(getContext(), R.style.SubDialogStyle);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().addFlags(2);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.gravity = 17;
        layoutParams.height = getHeight();
        layoutParams.width = getWidth();
        layoutParams.dimAmount = 0.6f;
        layoutParams.type = 1000;
        layoutParams.format = -2;
        layoutParams.windowAnimations = R.style.BottomDialogAnimation;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        return dialog;
    }

    /* access modifiers changed from: protected */
    public View getSubContent() {
        return inflate(getContext(), R.layout.extra_ireader_sub_content, (ViewGroup) null);
    }
}