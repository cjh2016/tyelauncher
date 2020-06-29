package com.boll.tyelauncher.util;

import android.os.Process;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;

public class ProcessUtils {
    private static String sProcessName = null;

    public static String getProcessName() throws Throwable {
        if (sProcessName != null) {
            return sProcessName;
        }
        sProcessName = doGetProcessName();
        return sProcessName;
    }

    private static String doGetProcessName() throws Throwable {
        String str = null;
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            File file = new File("/proc/" + Process.myPid() + "/" + "cmdline");
            if (!file.canRead()) {
                FileUtils.closeObj((Closeable) null);
                FileUtils.closeObj((Closeable) null);
            } else {
                FileReader fileReader2 = new FileReader(file);
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
                    try {
                        String line = bufferedReader2.readLine();
                        int index = 0;
                        int len = line.length();
                        while (index < len && line.charAt(index) != 0) {
                            index++;
                        }
                        str = line.substring(0, index);
                        FileUtils.closeObj(bufferedReader2);
                        FileUtils.closeObj(fileReader2);
                        FileReader fileReader3 = fileReader2;
                    } catch (Exception e) {
                        fileReader = fileReader2;
                        bufferedReader = bufferedReader2;
                        FileUtils.closeObj(bufferedReader);
                        FileUtils.closeObj(fileReader);
                        return str;
                    } catch (Throwable th) {
                        th = th;
                        fileReader = fileReader2;
                        bufferedReader = bufferedReader2;
                        FileUtils.closeObj(bufferedReader);
                        FileUtils.closeObj(fileReader);
                        throw th;
                    }
                } catch (Exception e2) {
                    fileReader = fileReader2;
                    FileUtils.closeObj(bufferedReader);
                    FileUtils.closeObj(fileReader);
                    return str;
                } catch (Throwable th2) {
                    fileReader = fileReader2;
                    FileUtils.closeObj(bufferedReader);
                    FileUtils.closeObj(fileReader);
                    throw th2;
                }
            }
        } catch (Exception e3) {
        } catch (Throwable th3) {
            FileUtils.closeObj(bufferedReader);
            FileUtils.closeObj(fileReader);
            throw th3;
        }
        return str;
    }
}