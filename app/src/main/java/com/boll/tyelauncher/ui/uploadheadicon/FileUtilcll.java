package com.boll.tyelauncher.ui.uploadheadicon;


import android.content.Context;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class FileUtilcll {
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        return saveFile(c, filePath, fileName, bitmapToBytes(bitmap));
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0021, code lost:
        if (r12.trim().length() == 0) goto L_0x0023;
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x009b A[SYNTHETIC, Splitter:B:21:0x009b] */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String saveFile(android.content.Context r11, java.lang.String r12, java.lang.String r13, byte[] r14) {
        /*
            java.lang.String r3 = ""
            r4 = 0
            java.text.SimpleDateFormat r8 = new java.text.SimpleDateFormat
            java.lang.String r9 = "yyyyMMddHHmmss"
            java.util.Locale r10 = java.util.Locale.CHINA
            r8.<init>(r9, r10)
            java.util.Date r9 = new java.util.Date
            r9.<init>()
            java.lang.String r0 = r8.format(r9)
            java.lang.String r7 = ""
            if (r12 == 0) goto L_0x0023
            java.lang.String r8 = r12.trim()     // Catch:{ Exception -> 0x0096 }
            int r8 = r8.length()     // Catch:{ Exception -> 0x0096 }
            if (r8 != 0) goto L_0x0044
        L_0x0023:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0096 }
            r8.<init>()     // Catch:{ Exception -> 0x0096 }
            java.io.File r9 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r9 = "/iflytek2/"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r8 = r8.append(r0)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r9 = "/"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r12 = r8.toString()     // Catch:{ Exception -> 0x0096 }
        L_0x0044:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0096 }
            r2.<init>(r12)     // Catch:{ Exception -> 0x0096 }
            boolean r8 = r2.exists()     // Catch:{ Exception -> 0x0096 }
            if (r8 != 0) goto L_0x0052
            r2.mkdirs()     // Catch:{ Exception -> 0x0096 }
        L_0x0052:
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0096 }
            r8.<init>()     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r8 = r8.append(r13)     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r8 = r8.append(r7)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x0096 }
            r6.<init>(r12, r8)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r3 = r6.getPath()     // Catch:{ Exception -> 0x0096 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0096 }
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0096 }
            r9.<init>()     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r9 = r9.append(r13)     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r9 = r9.append(r7)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x0096 }
            r8.<init>(r12, r9)     // Catch:{ Exception -> 0x0096 }
            r5.<init>(r8)     // Catch:{ Exception -> 0x0096 }
            r5.write(r14)     // Catch:{ Exception -> 0x00b1, all -> 0x00ae }
            if (r5 == 0) goto L_0x00b4
            r5.close()     // Catch:{ IOException -> 0x0091 }
            r4 = r5
        L_0x0090:
            return r3
        L_0x0091:
            r1 = move-exception
            java.lang.String r3 = ""
            r4 = r5
            goto L_0x0090
        L_0x0096:
            r1 = move-exception
        L_0x0097:
            java.lang.String r3 = ""
            if (r4 == 0) goto L_0x0090
            r4.close()     // Catch:{ IOException -> 0x009f }
            goto L_0x0090
        L_0x009f:
            r1 = move-exception
            java.lang.String r3 = ""
            goto L_0x0090
        L_0x00a3:
            r8 = move-exception
        L_0x00a4:
            if (r4 == 0) goto L_0x00a9
            r4.close()     // Catch:{ IOException -> 0x00aa }
        L_0x00a9:
            throw r8
        L_0x00aa:
            r1 = move-exception
            java.lang.String r3 = ""
            goto L_0x00a9
        L_0x00ae:
            r8 = move-exception
            r4 = r5
            goto L_0x00a4
        L_0x00b1:
            r1 = move-exception
            r4 = r5
            goto L_0x0097
        L_0x00b4:
            r4 = r5
            goto L_0x0090
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.ui.uploadheadicon.FileUtilcll.saveFile(android.content.Context, java.lang.String, java.lang.String, byte[]):java.lang.String");
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        } else if (file.delete()) {
            System.out.println("删除单个文件" + fileName + "成功！");
            return true;
        } else {
            System.out.println("删除单个文件" + fileName + "失败！");
            return false;
        }
    }
}
