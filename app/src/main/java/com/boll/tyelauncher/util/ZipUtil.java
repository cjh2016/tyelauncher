package com.boll.tyelauncher.util;


public class ZipUtil {
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0045 A[SYNTHETIC, Splitter:B:23:0x0045] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0051 A[SYNTHETIC, Splitter:B:29:0x0051] */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void zip(java.lang.String r9, java.lang.String r10) throws java.io.IOException {
        /*
            r4 = 0
            java.io.File r6 = new java.io.File     // Catch:{ IOException -> 0x003f }
            r6.<init>(r10)     // Catch:{ IOException -> 0x003f }
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x003f }
            r2.<init>(r9)     // Catch:{ IOException -> 0x003f }
            java.util.zip.ZipOutputStream r5 = new java.util.zip.ZipOutputStream     // Catch:{ IOException -> 0x003f }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x003f }
            r7.<init>(r6)     // Catch:{ IOException -> 0x003f }
            r5.<init>(r7)     // Catch:{ IOException -> 0x003f }
            boolean r7 = r2.isFile()     // Catch:{ IOException -> 0x005d, all -> 0x005a }
            if (r7 == 0) goto L_0x0027
            java.lang.String r7 = ""
            zipFileOrDirectory(r5, r2, r7)     // Catch:{ IOException -> 0x005d, all -> 0x005a }
        L_0x0020:
            if (r5 == 0) goto L_0x0060
            r5.close()     // Catch:{ IOException -> 0x0039 }
            r4 = r5
        L_0x0026:
            return
        L_0x0027:
            java.io.File[] r0 = r2.listFiles()     // Catch:{ IOException -> 0x005d, all -> 0x005a }
            r3 = 0
        L_0x002c:
            int r7 = r0.length     // Catch:{ IOException -> 0x005d, all -> 0x005a }
            if (r3 >= r7) goto L_0x0020
            r7 = r0[r3]     // Catch:{ IOException -> 0x005d, all -> 0x005a }
            java.lang.String r8 = ""
            zipFileOrDirectory(r5, r7, r8)     // Catch:{ IOException -> 0x005d, all -> 0x005a }
            int r3 = r3 + 1
            goto L_0x002c
        L_0x0039:
            r1 = move-exception
            r1.printStackTrace()
            r4 = r5
            goto L_0x0026
        L_0x003f:
            r1 = move-exception
        L_0x0040:
            r1.printStackTrace()     // Catch:{ all -> 0x004e }
            if (r4 == 0) goto L_0x0026
            r4.close()     // Catch:{ IOException -> 0x0049 }
            goto L_0x0026
        L_0x0049:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0026
        L_0x004e:
            r7 = move-exception
        L_0x004f:
            if (r4 == 0) goto L_0x0054
            r4.close()     // Catch:{ IOException -> 0x0055 }
        L_0x0054:
            throw r7
        L_0x0055:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0054
        L_0x005a:
            r7 = move-exception
            r4 = r5
            goto L_0x004f
        L_0x005d:
            r1 = move-exception
            r4 = r5
            goto L_0x0040
        L_0x0060:
            r4 = r5
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.util.ZipUtil.zip(java.lang.String, java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0040 A[SYNTHETIC, Splitter:B:15:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0086 A[SYNTHETIC, Splitter:B:35:0x0086] */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void zipFileOrDirectory(java.util.zip.ZipOutputStream r11, java.io.File r12, java.lang.String r13) throws java.io.IOException {
        /*
            r6 = 0
            boolean r8 = r12.isDirectory()     // Catch:{ IOException -> 0x0092 }
            if (r8 != 0) goto L_0x0053
            r8 = 4096(0x1000, float:5.74E-42)
            byte[] r0 = new byte[r8]     // Catch:{ IOException -> 0x0092 }
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0092 }
            r7.<init>(r12)     // Catch:{ IOException -> 0x0092 }
            java.util.zip.ZipEntry r3 = new java.util.zip.ZipEntry     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            r8.<init>()     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            java.lang.StringBuilder r8 = r8.append(r13)     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            java.lang.String r9 = r12.getName()     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            java.lang.String r8 = r8.toString()     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            r3.<init>(r8)     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            r11.putNextEntry(r3)     // Catch:{ IOException -> 0x0039, all -> 0x008f }
        L_0x002d:
            int r1 = r7.read(r0)     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            r8 = -1
            if (r1 == r8) goto L_0x0044
            r8 = 0
            r11.write(r0, r8, r1)     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            goto L_0x002d
        L_0x0039:
            r4 = move-exception
            r6 = r7
        L_0x003b:
            r4.printStackTrace()     // Catch:{ all -> 0x0083 }
            if (r6 == 0) goto L_0x0043
            r6.close()     // Catch:{ IOException -> 0x007e }
        L_0x0043:
            return
        L_0x0044:
            r11.closeEntry()     // Catch:{ IOException -> 0x0039, all -> 0x008f }
            r6 = r7
        L_0x0048:
            if (r6 == 0) goto L_0x0043
            r6.close()     // Catch:{ IOException -> 0x004e }
            goto L_0x0043
        L_0x004e:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0043
        L_0x0053:
            java.io.File[] r2 = r12.listFiles()     // Catch:{ IOException -> 0x0092 }
            r5 = 0
        L_0x0058:
            int r8 = r2.length     // Catch:{ IOException -> 0x0092 }
            if (r5 >= r8) goto L_0x0048
            r8 = r2[r5]     // Catch:{ IOException -> 0x0092 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0092 }
            r9.<init>()     // Catch:{ IOException -> 0x0092 }
            java.lang.StringBuilder r9 = r9.append(r13)     // Catch:{ IOException -> 0x0092 }
            java.lang.String r10 = r12.getName()     // Catch:{ IOException -> 0x0092 }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ IOException -> 0x0092 }
            java.lang.String r10 = "/"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ IOException -> 0x0092 }
            java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x0092 }
            zipFileOrDirectory(r11, r8, r9)     // Catch:{ IOException -> 0x0092 }
            int r5 = r5 + 1
            goto L_0x0058
        L_0x007e:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0043
        L_0x0083:
            r8 = move-exception
        L_0x0084:
            if (r6 == 0) goto L_0x0089
            r6.close()     // Catch:{ IOException -> 0x008a }
        L_0x0089:
            throw r8
        L_0x008a:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0089
        L_0x008f:
            r8 = move-exception
            r6 = r7
            goto L_0x0084
        L_0x0092:
            r4 = move-exception
            goto L_0x003b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.util.ZipUtil.zipFileOrDirectory(java.util.zip.ZipOutputStream, java.io.File, java.lang.String):void");
    }
}