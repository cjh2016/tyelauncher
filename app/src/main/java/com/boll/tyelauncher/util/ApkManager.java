package com.boll.tyelauncher.util;

import android.util.Log;

public class ApkManager {
    private static final String INSTALL_CMD = "install";
    private static final String TAG = "ApkManager";
    private static final String UNINSTALL_CMD = "uninstall";

    public static boolean install(String apkPath) {
        String result = apkProcess(new String[]{"pm", INSTALL_CMD, "-r", apkPath});
        Log.e(TAG, "install log:" + result);
        if (result == null || (!result.endsWith("Success") && !result.endsWith("Success\n"))) {
            return false;
        }
        return true;
    }

    public static boolean uninstall(String packageName) {
        String result = apkProcess(new String[]{"pm", UNINSTALL_CMD, packageName});
        Log.e(TAG, "uninstall log:" + result);
        if (result == null || (!result.endsWith("Success") && !result.endsWith("Success\n"))) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String apkProcess(java.lang.String[] r12) {
        /*
            r11 = -1
            r8 = 0
            java.lang.ProcessBuilder r6 = new java.lang.ProcessBuilder
            r6.<init>(r12)
            r5 = 0
            r3 = 0
            r4 = 0
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0022 }
            r0.<init>()     // Catch:{ Exception -> 0x0022 }
            r7 = -1
            java.lang.Process r5 = r6.start()     // Catch:{ Exception -> 0x0022 }
            java.io.InputStream r3 = r5.getErrorStream()     // Catch:{ Exception -> 0x0022 }
        L_0x0018:
            int r7 = r3.read()     // Catch:{ Exception -> 0x0022 }
            if (r7 == r11) goto L_0x0036
            r0.write(r7)     // Catch:{ Exception -> 0x0022 }
            goto L_0x0018
        L_0x0022:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ all -> 0x0049 }
            if (r3 == 0) goto L_0x002b
            r3.close()     // Catch:{ Exception -> 0x0079 }
        L_0x002b:
            if (r4 == 0) goto L_0x0030
            r4.close()     // Catch:{ Exception -> 0x0079 }
        L_0x0030:
            if (r5 == 0) goto L_0x0035
            r5.destroy()
        L_0x0035:
            return r8
        L_0x0036:
            r10 = 10
            r0.write(r10)     // Catch:{ Exception -> 0x0022 }
            java.io.InputStream r4 = r5.getInputStream()     // Catch:{ Exception -> 0x0022 }
        L_0x003f:
            int r7 = r4.read()     // Catch:{ Exception -> 0x0022 }
            if (r7 == r11) goto L_0x005a
            r0.write(r7)     // Catch:{ Exception -> 0x0022 }
            goto L_0x003f
        L_0x0049:
            r10 = move-exception
            if (r3 == 0) goto L_0x004f
            r3.close()     // Catch:{ Exception -> 0x007e }
        L_0x004f:
            if (r4 == 0) goto L_0x0054
            r4.close()     // Catch:{ Exception -> 0x007e }
        L_0x0054:
            if (r5 == 0) goto L_0x0059
            r5.destroy()
        L_0x0059:
            throw r10
        L_0x005a:
            byte[] r1 = r0.toByteArray()     // Catch:{ Exception -> 0x0022 }
            java.lang.String r9 = new java.lang.String     // Catch:{ Exception -> 0x0022 }
            r9.<init>(r1)     // Catch:{ Exception -> 0x0022 }
            if (r3 == 0) goto L_0x0068
            r3.close()     // Catch:{ Exception -> 0x0074 }
        L_0x0068:
            if (r4 == 0) goto L_0x006d
            r4.close()     // Catch:{ Exception -> 0x0074 }
        L_0x006d:
            if (r5 == 0) goto L_0x0083
            r5.destroy()
            r8 = r9
            goto L_0x0035
        L_0x0074:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x006d
        L_0x0079:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0030
        L_0x007e:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0054
        L_0x0083:
            r8 = r9
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.util.ApkManager.apkProcess(java.lang.String[]):java.lang.String");
    }
}
