package com.tv.filemanager.other;

import android.os.Environment;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.Sort;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.utils.SortUtil;
import com.tv.framework.adapter.AbsBaseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：文件管理器
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/18- 13:55
 */
public class FileManager {

    //保存根文件路径
    private String mRootPath;
    //当前目录文件
    private File mCurrCatalogFile;
    //文件更新任务
    private ScanAllFileTask mFileUpdateTask;
    //保存单列唯一实例
    private static FileManager ourInstance = new FileManager();
    //获取单列实例
    public static FileManager instance() {
        return ourInstance;
    }

    private FileManager() {
        mRootPath = getLocalRootPath();
    }

    /**
     * 使用路径来更新文件
     * @param path 文件路径
     * @param fileAdapter 文件适配器
     */
    public AbsAsyncTask updateFile(String path, final AbsBaseAdapter fileAdapter) {
        return updateFile(path,fileAdapter, FileUtil.getSortType(SortUtil.getSetSort()));
    }

    /**
     * 使用路径来更新文件
     * @param path 文件路径
     * @param fileAdapter 文件适配器
     * @param comparator 排序类型
     */
    public AbsAsyncTask updateFile(String path, final AbsBaseAdapter fileAdapter, Comparator comparator) {
        if (path == null) {
            path = mRootPath;
        } else {
            mRootPath = path;
        }
        mCurrCatalogFile = new File(path + File.separator);
        mFileUpdateTask = new ScanAllFileTask(mCurrCatalogFile.getPath(),comparator, fileAdapter);
        return mFileUpdateTask;
    }

    /**
     * 再当前目录下创建一个新的文件或者文件夹
     * @param fileName 文件
     * @throws IOException 抛出一个IO异常
     * @return 新建成功返回true,否则返回false
     */
    public boolean createNewFile(String fileName) throws IOException{
        final File newFile = new File(mCurrCatalogFile,fileName);
        if(newFile != null && !newFile.exists()) {
            if(newFile.isFile()) {
                return newFile.createNewFile();
            } else {
                newFile.mkdirs();
                return true;
            }
        }
        return false;
    }

    /**
     * 删除指定的文件
     * @param path 文件名
     * @return 成功返回true,否则返回false
     */
    public boolean deleteFile(String path) {
        final File oldFile = new File(path);
        if(oldFile != null && oldFile.exists()) {
            return oldFile.delete();
        }
        return false;
    }

    /**
     * 获取本地存储根目录路径
     * @return 根目录路径
     */
    public String getLocalRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 设置根路径
     * @param rootPath 根路径
     */
    public void setRootPath(String rootPath) {
        mRootPath = rootPath;
    }

    /**
     * 获取根路径
     * @return 路径字符串
     */
    public String getRootPath() {
        return mRootPath;
    }

    /**
     * 得到当前目录文件
     * @return 文件夹
     */
    public File getCurrCatalogFile() {
        return mCurrCatalogFile;
    }
}
