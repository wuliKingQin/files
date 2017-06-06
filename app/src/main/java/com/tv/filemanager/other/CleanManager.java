package com.tv.filemanager.other;

import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.tv.filemanager.R;
import com.tv.filemanager.activity.CleanActivity;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.CacheItem;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.widget.ExListView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：清理内存，缓存管理器
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/23- 9:02
 */
public class CleanManager {

    //扫描app缓存结束
    private final static int APP_CACHE_END = 0;
    //扫描无用apk文件结束
    private final static int USELESS_APP_END = 1;
    //扫描超大文件结束
    private final static int XL_FILE_END = 2;
    //扫描自己的缓存结束
    private final static int OWN_CACHE_END = 3;
    //清理app缓存结束
    private final static int CLEAN_APP_CACHE_END = 4;
    //清理无用apk结束
    private final static int CLEAN_USELESS_APP_END = 5;
    //清理超大文件结束
    private final static int CLEAN_XL_FILE_END = 6;
    //清理自己的缓存结束
    private final static int CLEAN_OWN_CACHE_END = 7;
    //扫描个数
    private int mScanCount = 0;
    //统计缓存大小
    private long mCacheSize = 0;
    //运行环境
    private Context mContext;
    //将子线程转换到主线程
    private Handler mHandler;
    //用于计算应用计算缓存个数
    private int mCalPackageCount = 0;
    //应用程序缓存大小
    private long mCurrentCacheSize = 0;
    //保存清理缓存大小
    private long mClearCacheSize = 0;
    //清理项
    private List<CacheItem> mCleanItems;
    //清理缓存
    private ICleanCacheCallback mCleanCallback;
    //扫描缓存监听
    private IScanCacheCallback mScanCallback;

    public CleanManager() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case APP_CACHE_END:
                        final CacheItem cleanItem = formatFileSize(mCurrentCacheSize);
                        cleanItem.setName(getString(R.string.app_cache));
                        mCleanItems.add(cleanItem);
                        mCurrentCacheSize = 0;
                        scanCacheEnd(cleanItem);
                        scanUselessApk();
                        break;
                    case USELESS_APP_END:
                        mCurrentCacheSize = 0;
                        scanXLFile();
                        break;
                    case XL_FILE_END:
                        mCurrentCacheSize = 0;
                        scanOwnCache();
                        break;
                    case OWN_CACHE_END:
                        if(mScanCallback != null) {
                            mScanCallback.onScanEnd(mScanCount);
                        }
                        mCacheSize = 0;
                        mScanCount = 0;
                        mClearCacheSize = 0;
                        break;
                    case CLEAN_APP_CACHE_END:
                        cleanCacheEnd(getString(R.string.app_cache));
                        cleanUselessApks();
                        break;
                    case CLEAN_USELESS_APP_END:
                        cleanXLFiles();
                        break;
                    case CLEAN_XL_FILE_END:
                        cleanOwnCache();
                        break;
                    case CLEAN_OWN_CACHE_END:
                        if(mCleanCallback != null) {
                            final CacheItem cacheItem = formatFileSize(mClearCacheSize);
                            mCleanCallback.onCleanEnd(cacheItem.getContent(),cacheItem.getUnit());
                        }
                        mCleanItems.clear();
                        break;
                    default:
                        break;
                }
            }
        };
        mCleanItems = new ArrayList<>();
    }

    /**
     * 开始扫描
     * @param context 运行环境
     */
    public void startScanCache(Context context,IScanCacheCallback callback) {
        mContext = context;
        mScanCallback = callback;
        calSystemCacheSize();
    }

    /**
     * 开始清理缓存
     * @param callback 清理缓存接口
     */
    public void startCleanCache(ICleanCacheCallback callback) {
        mCleanCallback = callback;
        clearSystemCacheSize();
    }

    /**
     * 扫描缓存结果
     * @param cleanItem 当前扫描的item
     */
    private void scanCacheEnd(CacheItem cleanItem) {
        if(mScanCallback != null) {
            mScanCount ++;
            final CacheItem allCache = formatFileSize(mCacheSize);
            mScanCallback.onScanCache(cleanItem,mScanCount,allCache.getContent(),allCache.getUnit());
        }
    }

    /**
     * 清理缓存结束
     * @param name 项名称
     */
    private void cleanCacheEnd(String name) {
        if(mCleanCallback != null) {
            mCleanCallback.onCleanCache(getCacheItemByName(name));
        }
    }


    /**
     * 计算每个应用程序的包名
     */
    private void calSystemCacheSize() {
        try {
            final List<String> packageNames = getAllAppPackageInfo();
            final int packageCount = packageNames.size();
            for (String packageName:getAllAppPackageInfo()) {
                final Method packageSizeInfo = mContext.getPackageManager().getClass().
                        getMethod("getPackageSizeInfo",String.class, IPackageStatsObserver.class);
                packageSizeInfo.invoke(mContext.getPackageManager(), packageName, new IPackageStatsObserver.Stub() {
                    @Override
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                        mCurrentCacheSize += pStats.cacheSize;
                        mCalPackageCount ++;
                        if(mCalPackageCount >= packageCount) {
                            mCacheSize += mCurrentCacheSize;
                            mHandler.sendEmptyMessage(APP_CACHE_END);
                            mCalPackageCount = 0;
                        }
                    }
                });
            }
        } catch (Exception e) {
            mHandler.sendEmptyMessage(APP_CACHE_END);
        }
    }

    /**
     *  清除第三方应用程序缓存
     */
    private void clearSystemCacheSize() {
        try {
            final Method clearCache =  PackageManager.class.
                    getMethod("freeStorageAndNotify",Long.TYPE, IPackageDataObserver.class);
            final StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
            final Long localLong = Long.valueOf(localStatFs.getBlockSizeLong() * localStatFs.getBlockCountLong() - 1L);
            clearCache.invoke(mContext.getPackageManager(),localLong, new IPackageDataObserver.Stub() {
                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                    final CacheItem cacheItem = getCacheItemByName(getString(R.string.app_cache));
                    if(cacheItem != null) {
                        try{
                            mClearCacheSize += Long.valueOf(cacheItem.getContent());
                            cacheItem.setContent("0.0");
                        } catch (Exception e) {
                        }
                    }
                    mHandler.sendEmptyMessage(CLEAN_APP_CACHE_END);
                }
            });
        } catch (Exception e) {
            mHandler.sendEmptyMessage(CLEAN_APP_CACHE_END);
        }
    }

    /**
     * 获取包名列表
     * @return 包名列表
     */
    private List<String> getAllAppPackageInfo() {
        final List<PackageInfo> packageInfos = mContext.getPackageManager().
                getInstalledPackages(PackageManager.GET_PERMISSIONS);
        final List<String> packageNames = new ArrayList<>(packageInfos.size());
        if(packageInfos != null) {
            for (PackageInfo packageInfo:packageInfos) {
                packageNames.add(packageInfo.packageName);
            }
        }
        return packageNames;
    }

    /**
     * 扫码无用apk
     */
    private void scanUselessApk() {
        final ScanApkTask scanApkTask = new ScanApkTask(null,null) {
            @Override
            public boolean accept(File file) {
                final String suffixName = FileUtil.getSuffixName(file.getName());
                if(file.isFile() && isAssignFile(suffixName)) {
                    mCurrentCacheSize += file.length();
                    final CFile assign = new CFile();
                    assign.setName(file.getName());
                    assign.setLength(file.length());
                    assign.setPath(file.getPath());
                    putFileData(assign);
                } else if(file.isDirectory()) {
                    getAssignFiles(file);
                }
                return false;
            }

            @Override
            protected void onPostExecute(List<CFile> cFiles) {
                final CacheItem cleanItem = formatFileSize(mCurrentCacheSize);
                cleanItem.setName(getString(R.string.not_use_apk));
                cleanItem.setFiles(cFiles);
                mCleanItems.add(cleanItem);
                mCacheSize += mCurrentCacheSize;
                mHandler.sendEmptyMessage(USELESS_APP_END);
                scanCacheEnd(cleanItem);
            }
        };
        scanApkTask.startTask();
    }

    /**
     * 清除无用apk
     */
    private void cleanUselessApks() {
        final CacheItem cacheItem = getCacheItemByName(getString(R.string.not_use_apk));
        if(cacheItem != null) {
            clearFiles(cacheItem);
        }
        cleanCacheEnd(getString(R.string.not_use_apk));
        mHandler.sendEmptyMessage(CLEAN_USELESS_APP_END);
    }

    /**
     * 扫描超大文件
     */
    private void scanXLFile() {
        final ScanAllFileTask scanAllFile = new ScanAllFileTask(null,null) {
            @Override
            public boolean accept(File file) {
                if(!file.isHidden()) {
                    if(file.isDirectory()) {
                        getAssignFiles(file);
                    } else {
                        if(file.length() > 524288000) {
                            mCurrentCacheSize += file.length();
                            final CFile assign = new CFile();
                            assign.setName(file.getName());
                            assign.setLength(file.length());
                            assign.setPath(file.getPath());
                            putFileData(assign);
                        }
                    }
                }
                return false;
            }

            @Override
            protected void onPostExecute(List<CFile> cFiles) {
                final CacheItem cleanItem = formatFileSize(mCurrentCacheSize);
                cleanItem.setName(getString(R.string.supper_file));
                cleanItem.setFiles(cFiles);
                mCleanItems.add(cleanItem);
                mCacheSize += mCurrentCacheSize;
                scanCacheEnd(cleanItem);
                mHandler.sendEmptyMessage(XL_FILE_END);
            }
        };
        scanAllFile.startTask();
    }

    /**
     * 清除超大文件
     */
    private void cleanXLFiles() {
        final CacheItem cacheItem = getCacheItemByName(getString(R.string.supper_file));
        if(cacheItem != null) {
            clearFiles(cacheItem);
        }
        cleanCacheEnd(getString(R.string.supper_file));
        mHandler.sendEmptyMessage(CLEAN_XL_FILE_END);
    }

    /**
     * 清除文件数据
     * @param cacheItem 清除项
     * @return 返回不需要清除的数据
     */
    private void clearFiles(CacheItem cacheItem) {
        File deleteFile;
        final List<CFile> saveFiles = new ArrayList<>();
        for(CFile file:cacheItem.getFiles()) {
            if(!file.isSelected()) {
                deleteFile = new File(file.getPath());
                if(deleteFile.exists()) {
                    if(!deleteFile.delete()) {
                        mCurrentCacheSize += file.getLength();
                        file.setSelected(false);
                        saveFiles.add(file);
                    } else {
                        mClearCacheSize += file.getLength();
                    }
                }
            } else {
                file.setSelected(false);
                saveFiles.add(file);
                mCurrentCacheSize += file.getLength();
            }
        }
        if(saveFiles.size() <= 0) {
            cacheItem.setContent("0.0");
        }
        cacheItem.setFiles(saveFiles);
    }

    /**
     * 扫描自己软件缓存
     */
    private void scanOwnCache() {
        final File cacheFile = mContext.getCacheDir();
        final CacheItem cleanItem = formatFileSize(cacheFile.length());
        cleanItem.setName(getString(R.string.system_cache));
        mCleanItems.add(cleanItem);
        mCacheSize += cacheFile.length();
        scanCacheEnd(cleanItem);
        mHandler.sendEmptyMessage(OWN_CACHE_END);
    }

    /**
     * 清除自己的缓存
     */
    private void cleanOwnCache() {
        final CacheItem cacheItem = getCacheItemByName(getString(R.string.system_cache));
        final File cacheFile = mContext.getCacheDir();
        if(cacheFile != null && cacheFile.exists()) {
            if(cacheFile.delete()) {
                mClearCacheSize += cacheFile.length();
            } else {
                if(cacheItem != null) {
                    cacheItem.setContent("0.0");
                }
            }
        }
        cleanCacheEnd(getString(R.string.system_cache));
        mHandler.sendEmptyMessage(CLEAN_OWN_CACHE_END);
    }

    /**
     * 格式化文件大小
     * @param fileSize 文件大小
     * @return CleanItem实例
     */
    private CacheItem formatFileSize(long fileSize) {
        final String[] fileSizes= Formatter.formatFileSize(mContext,fileSize).split(" ");
        final CacheItem cleanItem = new CacheItem();
        cleanItem.setContent(fileSizes[0]);
        cleanItem.setUnit(fileSizes[1]);
        return cleanItem;
    }

    /**
     * 通过名字获取CacheItem实例
     * @param name 项名称
     * @return CacheItem实例
     */
    private CacheItem getCacheItemByName(String name) {
        for (CacheItem cacheItem : mCleanItems) {
            if(TextUtils.equals(cacheItem.getName(), name)) {
                return cacheItem;
            }
        }
        return null;
    }

    /**
     * 清除数据
     */
    public void clear() {
        mCleanItems.clear();
    }

    //清理缓存回调接口
    public interface ICleanCacheCallback {
        /**
         * 清除缓存
         * @param cleanItem 清除的项
         */
        void onCleanCache(CacheItem cleanItem);

        /**
         * 清理结束
         */
        void onCleanEnd(String clearSize,String unit);
    }

    /**
     * 扫描缓存接口
     */
    public interface IScanCacheCallback {
        /**
         * 扫描需要清理的缓存
         * @param scanItem 扫描项
         * @param cacheSize 扫描的缓存总大小
         * @param unit 单位
         */
        void onScanCache(CacheItem scanItem,int scanCount, String cacheSize, String unit);

        /**
         * 扫描结束
         */
        void onScanEnd(int scanCount);
    }

    /**
     * 获取资源字符串
     * @param resId 资源Id
     * @return 资源字符串
     */
    private String getString(int resId) {
        return mContext.getString(resId);
    }
}
