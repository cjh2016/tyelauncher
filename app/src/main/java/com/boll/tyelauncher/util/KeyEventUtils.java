package com.boll.tyelauncher.util;


import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.view.InputEvent;
import android.view.KeyEvent;
import java.lang.reflect.InvocationTargetException;

public class KeyEventUtils {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void sendKeyEvent(Context context, int inputSource, int keyCode, boolean longpress) {
        long now = SystemClock.uptimeMillis();
        injectKeyEvent(context, new KeyEvent(now, now, 0, keyCode, 0, 0, -1, 0, 0, inputSource));
        if (longpress) {
            injectKeyEvent(context, new KeyEvent(now, now, 0, keyCode, 1, 0, -1, 0, 128, inputSource));
        }
        injectKeyEvent(context, new KeyEvent(now, now, 1, keyCode, 0, 0, -1, 0, 0, inputSource));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void injectKeyEvent(Context context, KeyEvent event) {
        InputManager im = (InputManager) context.getSystemService("input");
        Class<InputManager> cls = InputManager.class;
        try {
            cls.getMethod("injectInputEvent", new Class[]{InputEvent.class, Integer.TYPE}).invoke(im, new Object[]{event, 2});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
        }
    }
}