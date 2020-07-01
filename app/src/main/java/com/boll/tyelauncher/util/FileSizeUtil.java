package com.boll.tyelauncher.util;


import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

public class FileSizeUtil {
    public static final int SIZETYPE_B = 1;
    public static final int SIZETYPE_GB = 4;
    public static final int SIZETYPE_KB = 2;
    public static final int SIZETYPE_MB = 3;

    public static boolean getFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return formatFileSize(blockSize);
    }

    private static long getFileSize(File file) throws Exception {
        if (file.exists()) {
            return (long) new FileInputStream(file).available();
        }
        file.createNewFile();
        Log.e("获取文件大小", "文件不存在!");
        return 0;
    }

    private static long getFileSizes(File f) throws Exception {
        long fileSize;
        long size = 0;
        File[] flist = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                fileSize = getFileSizes(flist[i]);
            } else {
                fileSize = getFileSize(flist[i]);
            }
            size += fileSize;
        }
        return size;
    }

    private static String FormetFileSize(long fileS) {
        String fileSizeString;
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileS == 0) {
            return "0B";
        }
        if (fileS < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            fileSizeString = df.format(((double) fileS) / 1024.0d) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(((double) fileS) / 1048576.0d) + "MB";
        } else {
            fileSizeString = df.format(((double) fileS) / 1.073741824E9d) + "GB";
        }
        return fileSizeString;
    }

    private static boolean formatFileSize(long fileS) {
        new DecimalFormat("#.00");
        if (fileS > 819200) {
            return false;
        }
        return true;
    }
}