package com.tv.filemanager.other;

import android.os.Environment;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.Sort;
import com.tv.filemanager.utils.FileUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：文件夹扫描任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/27- 15:33
 */

public class ScanFolderTask extends AbsAsyncTask<List<CFile>>
        implements FileFilter {

    //根文件
    private File mRootFile;
    //排序器
    private Comparator mComparator;
    //文件夹数据
    private List<CFile> mFolders = new ArrayList<>();

    public ScanFolderTask(String filePath) {
        if(filePath == null) {
            mRootFile = Environment.getExternalStorageDirectory();
        } else {
            mRootFile = new File(filePath);
        }
        mComparator = FileUtil.getSortType(new Sort(Sort.Type.FILE_NAME, 1));
    }

    @Override
    protected List<CFile> doInBackground(Object... params) {
        mRootFile.listFiles(this);
        Collections.sort(mFolders, mComparator);
        return mFolders;
    }

    @Override
    public boolean accept(File pathname) {
        if(pathname.isDirectory()) {
            final CFile file = new CFile();
            file.setName(pathname.getName());
            file.setIcon(R.mipmap.ic_select_folder);
            file.setPath(pathname.getPath());
            mFolders.add(file);
        }
        return false;
    }
}
