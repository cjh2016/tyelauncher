package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DialogToLogin extends Dialog {
    public DialogToLogin(@NonNull Context context) {
        super(context);
    }

    public DialogToLogin(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogToLogin(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
