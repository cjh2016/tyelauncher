package com.boll.tyelauncher.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;

import com.alibaba.android.arouter.utils.Consts;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Stack;
import java.util.zip.ZipFile;

public class FileUtils {
    public static final long MIN_STORAGE_SPACE = 102400;
    private static ThreadLocal<byte[]> reuseBuff;

    private static byte[] getBuffer() {
        if (reuseBuff == null) {
            synchronized (FileUtils.class) {
                reuseBuff = new ThreadLocal<byte[]>() {
                    @Override
                    public byte[] initialValue() {
                        return new byte[8192];
                    }
                };
            }
        }
        return reuseBuff.get();
    }

    public static boolean delete(String dirPath) {
        return delete(new File(dirPath));
    }

    public static boolean hasEnoughStorageSpace() {
        return checkStorageSpace(MIN_STORAGE_SPACE);
    }

    public static final boolean checkStorageSpace(long size) {
        return getAvailableSize() > size;
    }

    public static boolean delete(File dir) {
        if (dir == null) {
            throw new IllegalArgumentException("dir can not be null");
        } else if (!dir.exists()) {
            return true;
        } else {
            if (!dir.isDirectory()) {
                return dir.delete();
            }
            File[] subFiles = dir.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                for (File file : subFiles) {
                    delete(file);
                }
            }
            return dir.delete();
        }
    }

    public static void createFile(String path, long size) throws Throwable {
        RandomAccessFile file = null;
        try {
            File oldFile = new File(path);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            RandomAccessFile file2 = new RandomAccessFile(path, "rw");
            try {
                file2.setLength(size);
                closeObj(file2);
                RandomAccessFile randomAccessFile = file2;
            } catch (Exception e) {
                file = file2;
                closeObj(file);
            } catch (Throwable th) {
                th = th;
                file = file2;
                closeObj(file);
                throw th;
            }
        } catch (Exception e2) {
            closeObj(file);
        } catch (Throwable th2) {
            closeObj(file);
            throw th2;
        }
    }

    public static boolean writeSafe(File file, byte[] data, int offset, int length, boolean append) throws Throwable {
        if (file == null) {
            return false;
        }
        FileOutputStream os = null;
        try {
            FileOutputStream os2 = new FileOutputStream(file, append);
            try {
                os2.write(data, offset, length);
                closeObj(os2);
                return true;
            } catch (IOException e) {
                os = os2;
                closeObj(os);
                return false;
            } catch (Throwable th) {
                th = th;
                os = os2;
                closeObj(os);
                throw th;
            }
        } catch (IOException e2) {
            closeObj(os);
            return false;
        } catch (Throwable th2) {
            closeObj(os);
            throw th2;
        }
    }

    public static boolean writeSafe(File file, byte[] data, int offset, int length) throws Throwable {
        return writeSafe(file, data, offset, length, false);
    }

    public static boolean writeSafe(File file, String text) throws Throwable {
        if (text == null || text.length() == 0) {
            return false;
        }
        byte[] data = text.getBytes();
        return writeSafe(file, data, 0, data.length);
    }

    public static boolean appendSafe(File file, byte[] data, int offset, int length) throws Throwable {
        return writeSafe(file, data, offset, length, true);
    }

    public static boolean appendSafe(File file, String text) throws Throwable {
        if (text == null || text.length() == 0) {
            return false;
        }
        byte[] data = text.getBytes();
        return appendSafe(file, data, 0, data.length);
    }

    public static byte[] read(InputStream in, boolean autoClose) throws IOException {
        ByteArrayOutputStream os = null;
        try {
            byte[] buffer = getBuffer();
            ByteArrayOutputStream os2 = new ByteArrayOutputStream();
            while (true) {
                try {
                    int len = in.read(buffer);
                    if (len <= 0) {
                        break;
                    }
                    os2.write(buffer, 0, len);
                } catch (Throwable th) {
                    th = th;
                    os = os2;
                }
            }
            byte[] byteArray = os2.toByteArray();
            if (autoClose) {
                closeObj(os2);
            }
            return byteArray;
        } catch (Throwable th2) {
            if (autoClose) {
                closeObj(os);
            }
            throw th2;
        }
    }

    public static String readString(InputStream in, boolean autoClose) throws IOException {
        return new String(read(in, autoClose));
    }

    public static String readStringSafe(String filePath) throws Throwable {
        byte[] data = readFile(new File(filePath));
        if (data == null) {
            return null;
        }
        return new String(data);
    }

    public static byte[] readFile(File file) throws Throwable {
        ByteArrayOutputStream os;
        FileInputStream in = null;
        ByteArrayOutputStream os2 = null;
        try {
            FileInputStream in2 = new FileInputStream(file);
            try {
                os = new ByteArrayOutputStream();
            } catch (Throwable th) {
                in = in2;
                closeObj(in);
                closeObj(os2);
                throw th;
            }
            try {
                byte[] buffer = getBuffer();
                while (true) {
                    int len = in2.read(buffer);
                    if (len > 0) {
                        os.write(buffer, 0, len);
                    } else {
                        byte[] byteArray = os.toByteArray();
                        closeObj(in2);
                        closeObj(os);
                        ByteArrayOutputStream byteArrayOutputStream = os;
                        FileInputStream fileInputStream = in2;
                        return byteArray;
                    }
                }
            } catch (IOException e2) {
                os2 = os;
                in = in2;
                closeObj(in);
                closeObj(os2);
                return null;
            } catch (Throwable th2) {
                os2 = os;
                in = in2;
                closeObj(in);
                closeObj(os2);
                throw th2;
            }
        } catch (IOException e3) {
            closeObj(in);
            closeObj(os2);
            return null;
        } catch (Throwable th3) {
            closeObj(in);
            closeObj(os2);
            throw th3;
        }
    }

    /* JADX INFO: finally extract failed */
    public static byte[] readDataFromAssetsSilent(Context context, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            closeObj(is);
            return buffer;
        } catch (Exception e) {
            closeObj(is);
            return null;
        } catch (Throwable th) {
            closeObj(is);
            throw th;
        }
    }

    public static String readStringFromAssetsSilent(Context context, String fileName) {
        byte[] data = readDataFromAssetsSilent(context, fileName);
        if (data == null || data.length == 0) {
            return "";
        }
        return new String(data);
    }

    public static String getExtension(String filepath) {
        int index = filepath.lastIndexOf(Consts.DOT);
        if (index == -1 || index >= filepath.length() - 1) {
            return null;
        }
        return filepath.substring(index + 1);
    }

    public static Intent getOpenFileIntent(String filePath, String mimeType) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(filePath)), mimeType);
        return intent;
    }

    public static long getVolumeUesdSize(String dirPath) {
        long blockSize;
        long availableBlocks;
        long totalBlocks;
        File path = new File(dirPath);
        if (!path.exists()) {
            throw new IllegalArgumentException("directory " + dirPath + " do not exist");
        } else if (!path.isDirectory()) {
            throw new IllegalArgumentException("file " + dirPath + " is not a directory");
        } else {
            StatFs statFs = new StatFs(path.getPath());
            if (Build.VERSION.SDK_INT > 18) {
                blockSize = statFs.getBlockSizeLong();
                availableBlocks = statFs.getAvailableBlocksLong();
                totalBlocks = statFs.getBlockCountLong();
            } else {
                blockSize = (long) statFs.getBlockSize();
                availableBlocks = (long) statFs.getAvailableBlocks();
                totalBlocks = (long) statFs.getBlockCount();
            }
            return (totalBlocks - availableBlocks) * blockSize;
        }
    }

    public static long getAvailableSize() {
        long blockSize;
        long availableBlocks;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = (long) stat.getBlockSize();
            availableBlocks = (long) stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    public static long getVolumeAvailableSize(String dirPath) {
        long blockSize;
        long availableBlocks;
        File path = new File(dirPath);
        if (!path.exists()) {
            throw new IllegalArgumentException("directory " + dirPath + " do not exist");
        } else if (!path.isDirectory()) {
            throw new IllegalArgumentException("file " + dirPath + " is not a directory");
        } else {
            StatFs statFs = new StatFs(path.getPath());
            if (Build.VERSION.SDK_INT > 18) {
                blockSize = statFs.getBlockSizeLong();
                availableBlocks = statFs.getAvailableBlocksLong();
            } else {
                blockSize = (long) statFs.getBlockSize();
                availableBlocks = (long) statFs.getAvailableBlocks();
            }
            return blockSize * availableBlocks;
        }
    }

    public static long getCacheSize(Context context) {
        long totalSize = getFileSize(context.getCacheDir());
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return totalSize + getFileSize(context.getExternalCacheDir());
        }
        return totalSize;
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (file == null) {
            return 0;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length <= 0) {
                return 0;
            }
            for (File f : files) {
                size += getFileSize(f);
            }
        } else {
            size = 0 + file.length();
        }
        return size;
    }

    public static void closeObj(Closeable obj) {
        if (obj != null) {
            try {
                obj.close();
            } catch (Exception e) {
            }
        }
    }

    public static void closeParcelFileDescriptor(ParcelFileDescriptor obj) {
        if (obj != null) {
            try {
                obj.close();
            } catch (Exception e) {
            }
        }
    }

    public static void closeObjs(Closeable... objs) {
        if (objs != null && objs.length != 0) {
            for (Closeable obj : objs) {
                closeObj(obj);
            }
        }
    }

    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception e) {
            }
        }
    }

    public static void closeZipFile(ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (Exception e) {
            }
        }
    }

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8192];
        while (true) {
            int len = is.read(buffer);
            if (len > 0) {
                os.write(buffer, 0, len);
            } else {
                return;
            }
        }
    }

    public static void copyToMany(InputStream is, OutputStream... oss) throws IOException {
        if (oss.length != 0) {
            byte[] buffer = new byte[8192];
            while (true) {
                int len = is.read(buffer);
                if (len > 0) {
                    for (OutputStream os : oss) {
                        os.write(buffer, 0, len);
                    }
                } else {
                    return;
                }
            }
        }
    }

    public static void copy(File src, File dest) throws Throwable {
        FileOutputStream os;
        FileInputStream is = null;
        FileOutputStream os2 = null;
        try {
            FileInputStream is2 = new FileInputStream(src);
            try {
                os = new FileOutputStream(dest);
            } catch (Throwable th) {
                th = th;
                is = is2;
                closeObj(is);
                closeObj(os2);
                throw th;
            }
            try {
                byte[] b = getBuffer();
                while (true) {
                    int len = is2.read(b);
                    if (len > 0) {
                        os.write(b, 0, len);
                    } else {
                        closeObj(is2);
                        closeObj(os);
                        return;
                    }
                }
            } catch (Throwable th2) {
                os2 = os;
                is = is2;
                closeObj(is);
                closeObj(os2);
                throw th2;
            }
        } catch (Throwable th3) {
            closeObj(is);
            closeObj(os2);
            throw th3;
        }
    }

    public static boolean copySafe(File src, File dest) throws Throwable {
        try {
            copy(src, dest);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static final String readResSilent(Context context, int resId) {
        try {
            return readRes(context, resId);
        } catch (IOException e) {
            return null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static final String readRes(Context context, int resId) throws Throwable {
        StringBuilder s = new StringBuilder();
        BufferedReader reader = null;
        try {
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resId)));
            while (true) {
                try {
                    String line = reader2.readLine();
                    if (line != null) {
                        s.append(line);
                        s.append("\n");
                    } else {
                        closeObj(reader2);
                        return s.toString();
                    }
                } catch (Throwable th) {
                    th = th;
                    reader = reader2;
                    closeObj(reader);
                    throw th;
                }
            }
        } catch (Throwable th2) {
            closeObj(reader);
            throw th2;
        }
    }

    public static boolean isSameFile(File file1, File file2) throws IOException {
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }
        return file1.getCanonicalPath().equals(file2.getCanonicalPath());
    }

    public static boolean isSameFile(File file1, File file2, boolean exceptionResult) {
        try {
            return isSameFile(file1, file2);
        } catch (IOException e) {
            return exceptionResult;
        }
    }

    public static boolean isSameFileSimple(String p1, String p2) {
        if (p1.equals(p2)) {
            return true;
        }
        Stack<String> s1 = new Stack<>();
        Stack<String> s2 = new Stack<>();
        split(p1, s1);
        split(p2, s2);
        return s1.equals(s2);
    }

    public static boolean isSameFileSimple(File file1, File file2) {
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }
        String p1 = file1.getAbsolutePath();
        String p2 = file2.getAbsolutePath();
        if (p1.equals(p2)) {
            return true;
        }
        Stack<String> s1 = new Stack<>();
        Stack<String> s2 = new Stack<>();
        split(p1, s1);
        split(p2, s2);
        return s1.equals(s2);
    }

    public static final String getFileSimplePath(String filePath) {
        Stack<String> s = new Stack<>();
        try {
            split(filePath, s);
            if (s.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            int size = s.size();
            for (int i = 0; i < size; i++) {
                sb.append((String) s.elementAt(i));
                if (i != size - 1) {
                    sb.append(File.separator);
                }
            }
            return sb.toString();
        } catch (Throwable th) {
            return null;
        }
    }

    private static void split(String str, Stack<String> stack) {
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '/') {
                if (i == index) {
                    index = i + 1;
                } else {
                    String item = str.substring(index, i);
                    if (item.equals(Consts.DOT)) {
                        index = i + 1;
                    } else {
                        if (item.equals("..")) {
                            stack.pop();
                        } else {
                            stack.push(item);
                        }
                        index = i + 1;
                    }
                }
            }
        }
        if (index < str.length()) {
            stack.push(str.substring(index));
        }
    }

    public static String formatFileSize(File file) {
        return formatSize(file.length());
    }

    public static String formatSize(long fileSize) {
        String fileSizeString;
        DecimalFormat df = new DecimalFormat("#.0");
        if (fileSize == 0) {
            return "0B";
        }
        if (fileSize <1024) {
            fileSizeString = String.valueOf(fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format(((double) fileSize) / 1024.0d) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format(((double) fileSize) / 1048576.0d) + "MB";
        } else {
            fileSizeString = df.format(((double) fileSize) / 1.073741824E9d) + "GB";
        }
        return fileSizeString;
    }

    public static boolean isSpecifyFormatFile(File file, String... suffix) {
        String fileName = file.getName();
        if (fileName.startsWith(Consts.DOT)) {
            return false;
        }
        try {
            String fileEndName = fileName.substring(fileName.lastIndexOf(Consts.DOT) + 1);
            for (String s : suffix) {
                if (fileEndName.equalsIgnoreCase(s)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getFileName(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.indexOf(Consts.DOT);
        return dotIndex < 0 ? fileName : fileName.substring(0, dotIndex);
    }
}
