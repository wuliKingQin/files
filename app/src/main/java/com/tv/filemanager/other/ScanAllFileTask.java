package com.tv.filemanager.other;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.utils.USBUtil;
import com.tv.framework.adapter.AbsBaseAdapter;
import com.tv.framework.utils.ProgressUtil;
import com.tv.framework.utils.ToastUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：扫描整理文件任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/6- 14:00
 */

public class ScanAllFileTask extends AbsAsyncTask<List<CFile>>
        implements FileFilter{

    //扫描到的指定文件列表
    private List<CFile> mFiles;
    //保存排序器
    private Comparator mComparator;
    //用于存放扫描路径文件
    private List<File> mScanPath;
    //适配器
    private AbsBaseAdapter mAdapter;

    public ScanAllFileTask(String dirFile, Comparator comparator, AbsBaseAdapter adapter) {
        mAdapter = adapter;
        mComparator = comparator;
        mFiles = new ArrayList<>();
        mScanPath = new ArrayList<>();
        mScanPath.add(new File(dirFile));
    }

    public ScanAllFileTask(Comparator comparator, AbsBaseAdapter adapter) {
        this(FileManager.instance().getLocalRootPath(),comparator,adapter);
        final String usbPath = USBUtil.getUsbStorageDir();
        if(usbPath != null) {
            mScanPath.add(new File(usbPath));
        }
    }

    @Override
    public boolean accept(File file) {
        if(!file.isHidden()) {
            CFile targetFile = new CFile();
            targetFile.setFile(file.isFile());
            targetFile.setName(file.getName());
            targetFile.setPath(file.getAbsolutePath());
            targetFile.setLastModifiedTime(file.lastModified());
            if (file.isDirectory()) {
                targetFile.setType(CFile.Type.DIR);
                targetFile.setChildFileCount(FileUtil.getFileChildCount(file));
                if(targetFile.getChildFileCount() > 0) {
                    targetFile.setIcon(R.mipmap.ic_folder);
                } else {
                    targetFile.setIcon(R.mipmap.ic_folder_empty);
                }
            } else {
                targetFile.setLength(file.length());
                targetFile.setType(FileUtil.getType(file.getName()));
                targetFile.setSuffixName(FileUtil.getSuffixName(file.getName()));
                targetFile.setIcon(FileUtil.getIcon(file.getName()));
            }
            putFileData(targetFile);
        }
        return false;
    }

    @Override
    protected List<CFile> doInBackground(Object... params) {
        mFiles.clear();
        for(File file:mScanPath) {
            getAssignFiles(file);
        }
        if(mComparator != null) {
            Collections.sort(mFiles,mComparator);
        }
        return mFiles;
    }

    @Override
    protected void onPostExecute(List<CFile> cFiles) {
        if(mAdapter != null) {
            mAdapter.putData(cFiles);
            ProgressUtil.dismissDialog();
        }
    }

    /**
     * 获取指定的文件
     * @param file 目标文件
     */
    protected void getAssignFiles(File file) {
        file.listFiles(this);
    }

    /**
     * 获取比较器
     * @return 获取比较器
     */
    public Comparator getComparator() {
        return mComparator;
    }

    /**
     * 添加文件数据
     * @param file 扫描到的数据
     */
    protected void putFileData(CFile file) {
        if(!mFiles.contains(file)) {
            mFiles.add(file);
        }
    }
}
