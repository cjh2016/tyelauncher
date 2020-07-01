package com.boll.tyelauncher.util;


import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.boll.tyelauncher.model.LogInfo;
import com.boll.tyelauncher.model.UploadInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class LogSaveManager_Util {
    public static String FIRLE_PATH = "";
    public static String FIRLE_UPLOAD_PATH = "";
    public static String LOG_LOCAL_PATH = "";
    public static String fileName1 = "tabletLog1.txt";
    public static String fileName2 = "tabletLog2.txt";

    public static void saveLog(Context context, String logStr) {
        try {
            String logStr2 = "\r\n" + getStringTime() + "/ launcher / " + logStr;
            LOG_LOCAL_PATH = Environment.getExternalStorageDirectory().getCanonicalPath() + "/appLogInfo/";
            File dir = new File(LOG_LOCAL_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(LOG_LOCAL_PATH, fileName1);
            if (!file.exists()) {
                file.createNewFile();
            }
            FIRLE_PATH = LOG_LOCAL_PATH + fileName1;
            if (getFileOrFilesSize(FIRLE_PATH) < 1024.0d) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(file.length());
                raf.write(logStr2.getBytes());
                raf.close();
                return;
            }
            FIRLE_PATH = LOG_LOCAL_PATH + fileName2;
            File newfile = new File(LOG_LOCAL_PATH, fileName2);
            if (!newfile.exists()) {
                newfile.createNewFile();
            }
            if (getFileOrFilesSize(FIRLE_PATH) < 1024.0d) {
                RandomAccessFile raf2 = new RandomAccessFile(newfile, "rw");
                raf2.seek(newfile.length());
                raf2.write(logStr2.getBytes());
                raf2.close();
                return;
            }
            deleteUploadLogFile(fileName1);
            renameFile(FIRLE_PATH, LOG_LOCAL_PATH + fileName1);
            FIRLE_PATH = LOG_LOCAL_PATH + fileName2;
            File newfile2 = new File(FIRLE_PATH);
            if (!newfile2.exists()) {
                newfile2.createNewFile();
            }
            RandomAccessFile raf3 = new RandomAccessFile(newfile, "rw");
            raf3.seek(newfile.length());
            raf3.write(logStr2.getBytes());
            raf3.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sunwei", "LogSaveManage error = " + e.toString());
        }
    }

    public static void saveLog(String logStr) {
        try {
            String logStr2 = "\r\n" + getStringTime() + "/ launcher / " + logStr;
            LOG_LOCAL_PATH = Environment.getExternalStorageDirectory().getCanonicalPath() + "/appLogInfo/";
            File dir = new File(LOG_LOCAL_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(LOG_LOCAL_PATH, fileName1);
            if (!file.exists()) {
                file.createNewFile();
            }
            FIRLE_PATH = LOG_LOCAL_PATH + fileName1;
            if (getFileOrFilesSize(FIRLE_PATH) < 1024.0d) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(file.length());
                raf.write(logStr2.getBytes());
                raf.close();
                return;
            }
            FIRLE_PATH = LOG_LOCAL_PATH + fileName2;
            File newfile = new File(LOG_LOCAL_PATH, fileName2);
            if (!newfile.exists()) {
                newfile.createNewFile();
            }
            if (getFileOrFilesSize(FIRLE_PATH) < 1024.0d) {
                RandomAccessFile raf2 = new RandomAccessFile(newfile, "rw");
                raf2.seek(newfile.length());
                raf2.write(logStr2.getBytes());
                raf2.close();
                return;
            }
            deleteUploadLogFile(fileName1);
            renameFile(FIRLE_PATH, LOG_LOCAL_PATH + fileName1);
            FIRLE_PATH = LOG_LOCAL_PATH + fileName2;
            File newfile2 = new File(FIRLE_PATH);
            if (!newfile2.exists()) {
                newfile2.createNewFile();
            }
            RandomAccessFile raf3 = new RandomAccessFile(newfile, "rw");
            raf3.seek(newfile.length());
            raf3.write(logStr2.getBytes());
            raf3.close();
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

    public static String getStringTime_Seconds() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
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

    public static LogInfo ZipFile(Context context) {
        File sdCardDir = Environment.getExternalStorageDirectory();
        LogInfo logInfo = new LogInfo();
        try {
            String path = sdCardDir.getCanonicalPath() + "/Download/";
            String fileName = new PadInfoUtil(context).getSnCode() + "-" + getStringTime_Seconds() + ".zip";
            ZipUtil.zip(LOG_LOCAL_PATH, path + fileName);
            logInfo.setFilePath(path + fileName);
            logInfo.setName(fileName);
            logInfo.setSuccess(true);
        } catch (IOException e) {
            logInfo.setSuccess(false);
        }
        return logInfo;
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
            String fileName = Environment.getExternalStorageDirectory().getCanonicalPath() + "/appLogInfo/" + filename;
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

    public static void upLoadLogTxt(final Context context) {
        final LogInfo logInfo = ZipFile(context);
        if (!logInfo.isSuccess()) {
            Toast.makeText(context, "日志文件不存在", 0).show();
        }
        if (TextUtils.isEmpty(logInfo.getFilePath())) {
            Toast.makeText(context, "日志文件不存在", 0).show();
            return;
        }
        final File file = new File(logInfo.getFilePath());
        UpLoadFileRequest request = new UpLoadFileRequest();
        request.type = "UPLOAD";
        request.fileName = logInfo.getName();
        LauncherHttpHelper.getLauncherService().upLoadLogcat(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<UploadInfo>(context) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                if (NetworkUtil.isNetworkAvailable(context)) {
                    Toast.makeText(this.mContext, "上传失败", 0).show();
                }
                super.onError(e);
            }

            public void onNext(UploadInfo response) {
                super.onNext(response);
                Log.e("UploadInfo-->", response.toString());
                if (response == null || response.getData() == null) {
                    Toast.makeText(context, "数据异常", 1).show();
                } else {
                    LogSaveManager_Util.uploadImageIconToCloud(response, file, context, logInfo.getName());
                }
            }
        });
    }

    public static void uploadImageIconToCloud(UploadInfo response2, File mFile, Context mContext, final String fileName) {
        UploadInfo.DataBean data = response2.getData();
        String str = fileName;
        ApiProvider.getInstance("http://" + data.getDomain() + "/").mUploadLogService.saveFileToCloud(data.getDomain(), data.getContainerName(), str, data.getToken(), data.getExpires(), RequestBody.create(MediaType.parse("application/octet-stream"), mFile)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ResponseBody>(mContext) {
            public void onCompleted() {
                super.onCompleted();
            }

            public void onError(Throwable e) {
                if (NetworkUtil.isNetworkAvailable(this.mContext)) {
                    Toast.makeText(this.mContext, "上传失败", 0).show();
                }
                Log.e("ResponseBody--->", e.getMessage() + ":");
                super.onError(e);
            }

            public void onNext(ResponseBody response) {
                super.onNext(response);
                try {
                    Toast.makeText(this.mContext, "上传成功", 0).show();
                    LogSaveManager_Util.deleteUpdateFile(fileName);
                    Log.e("ResponseBody--->", response.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
