package com.boll.tyelauncher.util;


public class StarUtils {
    public static float getStartCount(float score, float totalScore) {
        int avgProg = (int) (NumberConvertUtil.parseFloatD2(Float.valueOf(score / totalScore)) * 100.0f);
        if (avgProg >= 95) {
            return 5.0f;
        }
        float result = (float) (avgProg / 20);
        if (avgProg % 20 >= 10) {
            return result + 0.5f;
        }
        return result;
    }
}