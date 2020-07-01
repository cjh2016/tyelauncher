package com.boll.tyelauncher.util;

package com.toycloud.launcher.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.boll.tyelauncher.model.LogInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogSaveManager {
    public static String FIRLE_PATH = "";
    public static String FIRLE_UPLOAD_PATH = "";
    public static String LOG_LOCAL_PATH = "";

    public static void saveLog(Context context, String logStr) {
        try {
            String logStr2 = "\r\n" + getStringTime() + "/ launcher / " + logStr;
            LOG_LOCAL_PATH = Environment.getExternalStorageDirectory().getCanonicalPath() + "/toycloudpushLog/";
            File dir = new File(LOG_LOCAL_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(LOG_LOCAL_PATH, "watchLog.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FIRLE_PATH = LOG_LOCAL_PATH + "watchLog.txt";
            if (getFileOrFilesSize(FIRLE_PATH) < 4096.0d) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(file.length());
                raf.write(logStr2.getBytes());
                raf.close();
                return;
            }
            String time = getStringDate();
            FIRLE_UPLOAD_PATH = LOG_LOCAL_PATH + time + ".txt";
            renameFile(FIRLE_PATH, FIRLE_UPLOAD_PATH);
            LogInfo logInfo = new LogInfo();
            logInfo.setName(time + ".zip");
            logInfo.setFilePath(FIRLE_UPLOAD_PATH);
            boolean readAndWriteFile = readAndWriteFile(logInfo);
            File newfile = new File(LOG_LOCAL_PATH, "watchLog.txt");
            newfile.createNewFile();
            RandomAccessFile raf2 = new RandomAccessFile(newfile, "rw");
            raf2.seek(newfile.length());
            raf2.write(logStr2.getBytes());
            raf2.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sunwei", "LogSaveManage error = " + e.toString());
        }
    }

    public static void renameFile(String oldPath, String newPath) {
        new File(oldPath).renameTo(new File(newPath));
    }

    public static double getFileOrFilesSize(String filePath) {
        long blockSize = 0;
        try {
            blockSize = getFileSize(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    private static long getFileSize(File file) throws Exception {
        if (file.exists()) {
            return (long) new FileInputStream(file).available();
        }
        new File(LOG_LOCAL_PATH, "watchLog.txt").createNewFile();
        Log.e("获取文件大小", "文件不存在!");
        return 0;
    }

    private static double FormetFileSize(long fileS) {
        return Double.valueOf(new DecimalFormat("#.00").format(((double) fileS) / 1024.0d)).doubleValue();
    }

    public static String getStringDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String getStringTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
    }

    public static boolean readAndWriteFile(LogInfo logInfo) {
        try {
            String path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/Download/";
            ZipUtil.zip(logInfo.getFilePath(), path + logInfo.getName());
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File targetFile = new File(path + logInfo.getName());
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            FileInputStream is = new FileInputStream(new File(path + logInfo.getName()));
            byte[] b = new byte[is.available()];
            is.read(b);
            FileOutputStream out = new FileOutputStream(targetFile);
            out.write(b);
            out.flush();
            out.close();
            Log.d("sw", targetFile.getPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteUpdateFile(String filename) {
        try {
            String fileName = Environment.getExternalStorageDirectory().getCanonicalPath() + "/Download/" + filename;
            File file = new File(fileName);
            if (!file.exists() || !file.isFile()) {
                Log.d("LogSaveManager", "删除单个文件失败：" + fileName + "不存在！");
            } else if (file.delete()) {
                Log.d("LogSaveManager", "删除单个文件" + fileName + "成功！");
            } else {
                Log.d("LogSaveManager", "删除单个文件" + fileName + "失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUploadLogFile(String filename) {
        try {
            String fileName = (Environment.getExternalStorageDirectory().getCanonicalPath() + "/toycloudLog/" + filename).replace("zip", "txt");
            File file = new File(fileName);
            if (!file.exists() || !file.isFile()) {
                Log.d("LogSaveManager", "删除单个文件失败：" + fileName + "不存在！");
            } else if (file.delete()) {
                Log.d("LogSaveManager", "删除单个文件" + fileName + "成功！");
            } else {
                Log.d("LogSaveManager", "删除单个文件" + fileName + "失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
