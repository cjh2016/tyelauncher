package com.boll.tyelauncher.util;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boll.tyelauncher.R;

import java.lang.ref.WeakReference;

public final class ToastUtils {
    private static final int DEFAULT_COLOR = 301989888;
    private static int backgroundColor = DEFAULT_COLOR;
    private static int bgResource = -1;
    private static int gravity = 81;
    private static int messageColor = DEFAULT_COLOR;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static Toast sToast;
    private static WeakReference<View> sViewWeakReference;
    private static int xOffset = 0;
    private static int yOffset = ((int) (((double) (64.0f * Utils.getContext().getResources().getDisplayMetrics().density)) + 0.5d));

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void setGravity(int gravity2, int xOffset2, int yOffset2) {
        gravity = gravity2;
        xOffset = xOffset2;
        yOffset = yOffset2;
    }

    public static void setView(@LayoutRes int layoutId) {
        sViewWeakReference = new WeakReference<>(((LayoutInflater) Utils.getContext().getSystemService("layout_inflater")).inflate(layoutId, (ViewGroup) null));
    }

    public static void setView(@Nullable View view) {
        sViewWeakReference = view == null ? null : new WeakReference<>(view);
    }

    public static View getView() {
        View view;
        if (sViewWeakReference != null && (view = (View) sViewWeakReference.get()) != null) {
            return view;
        }
        if (sToast != null) {
            return sToast.getView();
        }
        return null;
    }

    public static void setBackgroundColor(@ColorInt int backgroundColor2) {
        backgroundColor = backgroundColor2;
    }

    public static void setBgResource(@DrawableRes int bgResource2) {
        bgResource = bgResource2;
    }

    public static void setMessageColor(@ColorInt int messageColor2) {
        messageColor = messageColor2;
    }

    public static void showShortSafe(final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(text, 0);
            }
        });
    }

    public static void showShortSafe(@StringRes final int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(resId, 0);
            }
        });
    }

    public static void showShortSafe(@StringRes final int resId, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(resId, 0, args);
            }
        });
    }

    public static void showShortSafe(final String format, final Object... args) {
        sHandler.post(new Runnable() {
            public void run() {
                ToastUtils.show(format, 0, args);
            }
        });
    }

    public static void showLongSafe(final CharSequence text) {
        sHandler.post(new Runnable() {
            public void run() {
                ToastUtils.show(text, 1);
            }
        });
    }

    public static void showLongSafe(@StringRes final int resId) {
        sHandler.post(new Runnable() {
            public void run() {
                ToastUtils.show(resId, 1);
            }
        });
    }

    public static void showLongSafe(@StringRes final int resId, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(resId, 1, args);
            }
        });
    }

    public static void showLongSafe(final String format, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(format, 1, args);
            }
        });
    }

    public static void showShort(CharSequence text) {
        show(text, 0);
    }

    public static void showShort(@StringRes int resId) {
        show(resId, 0);
    }

    public static void showShort(@StringRes int resId, Object... args) {
        show(resId, 0, args);
    }

    public static void showShort(String format, Object... args) {
        show(format, 0, args);
    }

    public static void showLong(CharSequence text) {
        show(text, 1);
    }

    public static void showLong(@StringRes int resId) {
        show(resId, 1);
    }

    public static void showLong(@StringRes int resId, Object... args) {
        show(resId, 1, args);
    }

    public static void showLong(String format, Object... args) {
        show(format, 1, args);
    }

    public static void showCustomShortSafe() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show((CharSequence) "", 0);
            }
        });
    }

    public static void showCustomLongSafe() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show((CharSequence) "", 1);
            }
        });
    }

    public static void showCustomShort() {
        show((CharSequence) "", 0);
    }

    public static void showCustomLong() {
        show((CharSequence) "", 1);
    }

    /* access modifiers changed from: private */
    public static void show(@StringRes int resId, int duration) {
        show((CharSequence) Utils.getContext().getResources().getText(resId).toString(), duration);
    }

    /* access modifiers changed from: private */
    public static void show(@StringRes int resId, int duration, Object... args) {
        show((CharSequence) String.format(Utils.getContext().getResources().getString(resId), args), duration);
    }

    /* access modifiers changed from: private */
    public static void show(String format, int duration, Object... args) {
        show((CharSequence) String.format(format, args), duration);
    }

    /* access modifiers changed from: private */
    public static void show(CharSequence text, int duration) {
        View view;
        cancel();
        setBgAndTextColor();
        boolean isCustom = false;
        if (!(sViewWeakReference == null || (view = (View) sViewWeakReference.get()) == null)) {
            sToast = new Toast(Utils.getContext());
            sToast.setView(view);
            sToast.setDuration(duration);
            isCustom = true;
        }
        if (!isCustom) {
            if (messageColor != DEFAULT_COLOR) {
                SpannableString spannableString = new SpannableString(text);
                spannableString.setSpan(new ForegroundColorSpan(messageColor), 0, spannableString.length(), 33);
                sToast = Toast.makeText(Utils.getContext(), spannableString, duration);
            } else {
                sToast = Toast.makeText(Utils.getContext(), text, duration);
            }
        }
        View view2 = sToast.getView();
        if (bgResource != -1) {
            view2.setBackgroundResource(bgResource);
        } else if (backgroundColor != DEFAULT_COLOR) {
            view2.setBackgroundColor(backgroundColor);
        }
        sToast.setGravity(gravity, xOffset, yOffset);
        sToast.show();
    }

    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    private static void setBgAndTextColor() {
        setBgResource(R.drawable.bg_custom_toast);
        setMessageColor(ContextCompat.getColor(Utils.getContext(), R.color.white));
    }
}
