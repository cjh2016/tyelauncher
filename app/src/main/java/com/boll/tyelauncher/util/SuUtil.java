package com.boll.tyelauncher.util;


import java.io.IOException;
import java.io.OutputStream;

public class SuUtil {
    private static Process process;

    public static void kill(String packageName) {
        initProcess();
        killProcess(packageName);
        close();
    }

    private static void initProcess() {
        if (process == null) {
            try {
                process = Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void killProcess(String packageName) {
        OutputStream out = process.getOutputStream();
        try {
            out.write(("am force-stop " + packageName + " \n").getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close() {
        if (process != null) {
            try {
                process.getOutputStream().close();
                process = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}