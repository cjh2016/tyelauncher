package com.boll.tyelauncher.util;


import android.support.v4.view.ViewPager;
import android.view.View;

public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static float ALPHA_FACTOR = 0.8f;
    private static float DEFAULT_SCALE = 1.0f;
    private static final float MIN_SCALE = 0.75f;
    private static float ROTATION_FACTOR = 20.0f;
    private static float SCALE_FACTOR = 0.4f;

    @Override
    public void transformPage(View view, float position) {
        int width = view.getWidth();
        if (position < -1.0f) {
            view.setAlpha(0.0f);
            view.setScaleX((SCALE_FACTOR * position) + DEFAULT_SCALE);
            view.setScaleY((SCALE_FACTOR * position) + DEFAULT_SCALE);
        } else if (position <= 0.0f) {
            view.setAlpha(1.0f + position);
            view.setTranslationX(0.0f);
            view.setScaleX((SCALE_FACTOR * position) + DEFAULT_SCALE);
            view.setScaleY((SCALE_FACTOR * position) + DEFAULT_SCALE);
        } else if (position <= 1.0f) {
            view.setScaleX((SCALE_FACTOR * (-position)) + DEFAULT_SCALE);
            view.setScaleY((SCALE_FACTOR * (-position)) + DEFAULT_SCALE);
            view.setAlpha(1.0f - position);
        } else {
            view.setAlpha(0.0f);
        }
    }
}