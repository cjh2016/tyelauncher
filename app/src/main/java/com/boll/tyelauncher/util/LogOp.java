package com.boll.tyelauncher.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LogOp {
    private static final int MAX_LINE_NUM = 50000;
    private static final String TAG = LogOp.class.getSimpleName();
    private static LogOp sInstance;
    private static String sNewline = System.getProperty("line.separator");
    private int ERR_IOEXCEPTION = -1;
    private int ERR_NO_ERROR = 0;
    /* access modifiers changed from: private */
    public boolean keepRead = false;
    private Context mContext;

    private LogOp(Context context) {
        this.mContext = context;
    }

    public static LogOp getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LogOp(context);
        }
        return sInstance;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r1.write(sNewline.getBytes());
        r1.write("============================================================".getBytes());
        r1.write(sNewline.getBytes());
        r1.write("============================================================".getBytes());
        r1.write(sNewline.getBytes());
        r1.write(sNewline.getBytes());
        writeKernelToFile(r1, r14);
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        return r12.ERR_NO_ERROR;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int writeLogcatToFile(java.lang.String r13, int r14) {
        /*
            r12 = this;
            java.util.Timer r7 = new java.util.Timer
            r7.<init>()
            com.toycloud.launcher.util.LogOp$1 r8 = new com.toycloud.launcher.util.LogOp$1
            r8.<init>()
            long r10 = (long) r14
            r7.schedule(r8, r10)
            r8 = 1
            r12.keepRead = r8
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.lang.String r8 = "logcat"
            r2.add(r8)
            java.lang.String r8 = "-v"
            r2.add(r8)
            java.lang.String r8 = "time"
            r2.add(r8)
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0074 }
            r8 = 0
            r1.<init>(r13, r8)     // Catch:{ IOException -> 0x0074 }
            r12.addVersionInfo(r1)     // Catch:{ IOException -> 0x0074 }
            java.lang.Runtime r9 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0074 }
            int r8 = r2.size()     // Catch:{ IOException -> 0x0074 }
            java.lang.String[] r8 = new java.lang.String[r8]     // Catch:{ IOException -> 0x0074 }
            java.lang.Object[] r8 = r2.toArray(r8)     // Catch:{ IOException -> 0x0074 }
            java.lang.String[] r8 = (java.lang.String[]) r8     // Catch:{ IOException -> 0x0074 }
            java.lang.Process r4 = r9.exec(r8)     // Catch:{ IOException -> 0x0074 }
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0074 }
            java.io.InputStreamReader r8 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0074 }
            java.io.InputStream r9 = r4.getInputStream()     // Catch:{ IOException -> 0x0074 }
            r8.<init>(r9)     // Catch:{ IOException -> 0x0074 }
            r5.<init>(r8)     // Catch:{ IOException -> 0x0074 }
            r6 = 0
            r3 = 0
        L_0x0052:
            java.lang.String r6 = r5.readLine()     // Catch:{ IOException -> 0x0074 }
            if (r6 == 0) goto L_0x007b
            boolean r8 = r12.keepRead     // Catch:{ IOException -> 0x0074 }
            if (r8 == 0) goto L_0x007b
            r8 = 50000(0xc350, float:7.0065E-41)
            if (r3 >= r8) goto L_0x007b
            int r3 = r3 + 1
            byte[] r8 = r6.getBytes()     // Catch:{ IOException -> 0x0074 }
            r1.write(r8)     // Catch:{ IOException -> 0x0074 }
            java.lang.String r8 = sNewline     // Catch:{ IOException -> 0x0074 }
            byte[] r8 = r8.getBytes()     // Catch:{ IOException -> 0x0074 }
            r1.write(r8)     // Catch:{ IOException -> 0x0074 }
            goto L_0x0052
        L_0x0074:
            r0 = move-exception
            r0.printStackTrace()
            int r8 = r12.ERR_IOEXCEPTION
        L_0x007a:
            return r8
        L_0x007b:
            java.lang.String r8 = sNewline     // Catch:{ IOException -> 0x0074 }
            byte[] r8 = r8.getBytes()     // Catch:{ IOException -> 0x0074 }
            r1.write(r8)     // Catch:{ IOException -> 0x0074 }
            java.lang.String r8 = "============================================================"
            byte[] r8 = r8.getBytes()     // Catch:{ IOException -> 0x0074 }
            r1.write(r8)     // Catch:{ IOException -> 0x0074 }
            java.lang.String r8 = sNewline     // Catch:{ IOException -> 0x0074 }
            byte[] r8 = r8.getBytes()     // Catch:{ IOException -> 0x0074 }
            r1.write(r8)     // Catch:{ IOException -> 0x0074 }
            java.lang.String r8 = "============================================================"
            byte[] r8 = r8.getBytes()     // Catch:{ IOException -> 0x0074 }
            r1.write(r8)     // Catch:{ IOException -> 0x0074 }
            java.lang.String r8 = sNewline     // Catch:{ IOException -> 0x0074 }
            byte[] r8 = r8.getBytes()     // Catch:{ IOException -> 0x0074 }
            r1.write(r8)     // Catch:{ IOException -> 0x0074 }
            java.lang.String r8 = sNewline     // Catch:{ IOException -> 0x0074 }
            byte[] r8 = r8.getBytes()     // Catch:{ IOException -> 0x0074 }
            r1.write(r8)     // Catch:{ IOException -> 0x0074 }
            r12.writeKernelToFile(r1, r14)     // Catch:{ IOException -> 0x0074 }
            r1.close()     // Catch:{ IOException -> 0x0074 }
            int r8 = r12.ERR_NO_ERROR     // Catch:{ IOException -> 0x0074 }
            goto L_0x007a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.util.LogOp.writeLogcatToFile(java.lang.String, int):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        return r10.ERR_NO_ERROR;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int writeKernelToFile(java.io.FileOutputStream r11, int r12) {
        /*
            r10 = this;
            java.util.Timer r6 = new java.util.Timer
            r6.<init>()
            com.toycloud.launcher.util.LogOp$2 r7 = new com.toycloud.launcher.util.LogOp$2
            r7.<init>()
            long r8 = (long) r12
            r6.schedule(r7, r8)
            r7 = 1
            r10.keepRead = r7
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.lang.String r7 = "dmesg"
            r1.add(r7)
            r3 = 0
            java.lang.Runtime r8 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0062 }
            int r7 = r1.size()     // Catch:{ IOException -> 0x0062 }
            java.lang.String[] r7 = new java.lang.String[r7]     // Catch:{ IOException -> 0x0062 }
            java.lang.Object[] r7 = r1.toArray(r7)     // Catch:{ IOException -> 0x0062 }
            java.lang.String[] r7 = (java.lang.String[]) r7     // Catch:{ IOException -> 0x0062 }
            java.lang.Process r3 = r8.exec(r7)     // Catch:{ IOException -> 0x0062 }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0062 }
            java.io.InputStreamReader r7 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0062 }
            java.io.InputStream r8 = r3.getInputStream()     // Catch:{ IOException -> 0x0062 }
            r7.<init>(r8)     // Catch:{ IOException -> 0x0062 }
            r4.<init>(r7)     // Catch:{ IOException -> 0x0062 }
            r5 = 0
            r2 = 0
        L_0x0040:
            java.lang.String r5 = r4.readLine()     // Catch:{ IOException -> 0x0062 }
            if (r5 == 0) goto L_0x0069
            boolean r7 = r10.keepRead     // Catch:{ IOException -> 0x0062 }
            if (r7 == 0) goto L_0x0069
            r7 = 50000(0xc350, float:7.0065E-41)
            if (r2 >= r7) goto L_0x0069
            int r2 = r2 + 1
            byte[] r7 = r5.getBytes()     // Catch:{ IOException -> 0x0062 }
            r11.write(r7)     // Catch:{ IOException -> 0x0062 }
            java.lang.String r7 = sNewline     // Catch:{ IOException -> 0x0062 }
            byte[] r7 = r7.getBytes()     // Catch:{ IOException -> 0x0062 }
            r11.write(r7)     // Catch:{ IOException -> 0x0062 }
            goto L_0x0040
        L_0x0062:
            r0 = move-exception
            r0.printStackTrace()
            int r7 = r10.ERR_IOEXCEPTION
        L_0x0068:
            return r7
        L_0x0069:
            int r7 = r10.ERR_NO_ERROR     // Catch:{ IOException -> 0x0062 }
            goto L_0x0068
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.util.LogOp.writeKernelToFile(java.io.FileOutputStream, int):int");
    }

    private void addVersionInfo(FileOutputStream fos) throws IOException {
        fos.write((new PadInfoUtil(this.mContext).getSystemVersionCode() + ":" + getFirmwareVersion()).getBytes());
        fos.write(sNewline.getBytes());
        for (String version : getAppsVersion()) {
            fos.write(version.getBytes());
            fos.write(sNewline.getBytes());
        }
        fos.write(sNewline.getBytes());
        fos.write("============================================================".getBytes());
        fos.write(sNewline.getBytes());
        fos.write("============================================================".getBytes());
        fos.write(sNewline.getBytes());
        fos.write(sNewline.getBytes());
    }

    private String getFirmwareVersion() {
        Class<?>[] paramCls;
        try {
            Method[] methods = Class.forName("android.os.Build").getDeclaredMethods();
            if (methods == null || methods.length <= 0) {
                return "";
            }
            for (Method method : methods) {
                if ("getString".equals(method.getName()) && (paramCls = method.getParameterTypes()) != null && paramCls.length == 1 && paramCls[0] == String.class) {
                    method.setAccessible(true);
                    Object objRet = method.invoke((Object) null, new Object[]{"ro.product.firmware"});
                    if (objRet == null || !(objRet instanceof String)) {
                        return "";
                    }
                    return (String) objRet;
                }
            }
            return "";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "";
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
            return "";
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return "";
        }
    }

    private List<String> getAppsVersion() {
        List<String> ret = new ArrayList<>();
        for (PackageInfo info : this.mContext.getPackageManager().getInstalledPackages(1)) {
            ret.add(info.packageName + "-->" + info.versionName + "," + info.versionCode);
        }
        return ret;
    }
}
