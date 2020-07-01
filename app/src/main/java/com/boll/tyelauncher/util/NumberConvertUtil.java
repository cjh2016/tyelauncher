package com.boll.tyelauncher.util;


import java.math.BigDecimal;

public class NumberConvertUtil {
    public static int parseInt5(Object src) {
        return (int) (((double) parseFloat(src, 1)) + 0.5d);
    }

    public static float parseFloat(Object obj, int digit) {
        if (obj == null) {
            return 0.0f;
        }
        try {
            return new BigDecimal((double) Float.valueOf(parseFloat(obj + "")).floatValue()).setScale(digit, 4).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static float parseFloat(String attrVal) {
        float result;
        if (attrVal != null) {
            try {
                if (!"".equals(attrVal)) {
                    result = Float.valueOf(attrVal).floatValue();
                    return result;
                }
            } catch (Exception e) {
                result = 0.0f;
            }
        }
        return 0.0f;
    }

    public static float parseFloatD2(Object obj) {
        return parseFloat(obj, 2);
    }

    public static int parseInteger(String integer, int defValue) {
        try {
            return Integer.parseInt(integer);
        } catch (Throwable th) {
            return defValue;
        }
    }
}
