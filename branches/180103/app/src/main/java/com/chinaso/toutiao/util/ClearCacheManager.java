package com.chinaso.toutiao.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

public class ClearCacheManager {

    /**
     * 获取缓存大小
     * @param context
     * @return
     */
    public static String getTotalCacheSize(Context context){
        long cacheSize=getFolderSize(context.getCacheDir());
        return getFormatSize(cacheSize);
    }

    /**
     * 清除内部缓存
     */
    public static void cleanInternalCache(Context context){
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除所有的缓存
     * @param context
     * @return
     */
    public static void clearAllCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }
    //根据路径删除
    private static boolean deleteFilesByDirectory(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child: children) {
                boolean success = deleteFilesByDirectory(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();

    }
    //获取文件夹的大小
    private static long getFolderSize(File file) {
        long size = 0;
        try {
            if(file!=null){
                File[] fileList = file.listFiles();
                for (File fileItem: fileList) {
                    if (fileItem.isDirectory()) {
                        size = size + getFolderSize(fileItem);//递归调用
                    } else {
                        size = size + fileItem.length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    //格式化单位
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
