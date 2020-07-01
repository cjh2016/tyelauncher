package com.boll.tyelauncher.util;


import java.util.Collection;

public class CollectionUtils {
    public static boolean isNullOrEmpty(Collection c) {
        if (c == null || c.isEmpty()) {
            return true;
        }
        return false;
    }
}
