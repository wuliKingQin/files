package com.tv.framework.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 描述：文件存储工具类
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 8:35
 */

public class StorageUtil {

    //存储TAG
    public static final String TAG = "StorageUtil";
    //写文件权限
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    //缓存根目录
    public static final String CACHE_DIR_ROOT = "/robot";
    //缓存照片
    public static final String CACHE_DIR_PHOTO = "/robot/photo";
    //缓存数据
    public static final String CACHE_DIR_DATA = "/robot/data";
    //缓存分享数据
    public static final String CACHE_DIR_SHARE = "/robot/share";
    //缓存下载数据
    public static final String CACHE_DIR_DOWNLOAD = "/robot/download";

    private StorageUtil() {
    }

    /**
     * 获取缓存路径
     * @param context 运行时
     * @return 缓存文件
     */
    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    /**
     * 获取缓存路径
     * @param context 运行环境实例
     * @param preferExternal 是否外部存储
     * @return 缓存路径文件
     */
    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException var5) {
            externalStorageState = "";
        }
        if(preferExternal && "mounted".equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if(appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if(appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    /**
     * 获取自己的缓存路径
     * @param context 运行环境
     * @param cacheDir 缓存路径
     * @return 文件
     */
    public static File getCacheDir(Context context, String cacheDir) {
        File appCacheDir = null;
        if("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if(appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }
        noMediaForCacheDir(appCacheDir);
        return appCacheDir;
    }

    /**
     * 获取根目录缓存路径文件
     * @param ctx 运行环境
     * @return 文件
     */
    public static File getCacheDir(Context ctx) {
        return getCacheDir(ctx, CACHE_DIR_ROOT);
    }

    /**
     * 获取图片缓存路径
     * @param ctx 运行环境
     * @return 文件
     */
    public static File getPhotoCacheDir(Context ctx) {
        return getCacheDir(ctx, CACHE_DIR_PHOTO);
    }

    /**
     * 获取数据缓存路径
     * @param ctx 运行环境
     * @return 文件
     */
    public static File getDataCacheDir(Context ctx) {
        return getCacheDir(ctx, CACHE_DIR_DATA);
    }

    /**
     * 获取分享缓存路径
     * @param ctx 运行环境
     * @return 文件
     */
    public static File getShareCacheDir(Context ctx) {
        return getCacheDir(ctx, CACHE_DIR_SHARE);
    }

    /**
     * 获取下载文件夹
     * @param ctx 运行环境
     * @return 文件
     */
    public static File getDownloadCacheDir(Context ctx) {
        return getCacheDir(ctx,CACHE_DIR_DOWNLOAD);
    }

    /**
     * 创建nomedia的缓存路径
     * @param appCacheDir 缓存文件
     */
    public static void noMediaForCacheDir(File appCacheDir) {
        if(!appCacheDir.exists()) {
            if(!appCacheDir.mkdirs()) {
                Log.w(TAG, "Unable to create external cache directory");
                return;
            }
            try {
                (new File(appCacheDir, ".nomedia")).createNewFile();
            } catch (IOException var2) {
                Log.i(TAG, "Can\'t create \".nomedia\" file in application external cache directory");
            }
        }

    }

    /**
     * 获取扩建的缓存路径
     * @param context 运行环境
     * @return 文件
     */
    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        noMediaForCacheDir(appCacheDir);
        return appCacheDir;
    }

    /**
     * 是否写的有权限
     * @param context 运行环境
     * @return true表示有，false表示没有
     */
    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == 0;
    }
}
