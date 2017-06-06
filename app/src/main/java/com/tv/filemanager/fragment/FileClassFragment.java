package com.tv.filemanager.fragment;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.ClickPath;
import com.tv.filemanager.other.AbsAsyncTask;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.utils.SortUtil;
import com.tv.framework.utils.ProgressUtil;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：文件分类碎片
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/1- 10:18
 */

public abstract class FileClassFragment extends AbsCommonFragment {

    //保存分类名字
    private String mClassName;

    @Override
    protected void onItemClick(CFile file, int position) {
        if(file.isFile()) {
            if(!isLimitCondition()) {
                setFocusItemPosition(position);
                FileUtil.openFileIntent(getContext(), file.getPath());
            }
        } else {
            addClickPath(file.getPath() + File.separator, position);
            setFocusItemPosition(-1);
            setPath(file.getPath() + File.separator);
            nextFileDir(file);
        }
    }

    /**
     * 进入下一个目录
     * @param file 目录文件
     */
    protected void nextFileDir(CFile file) {
        isNextDir = true;
        ProgressUtil.showDialog(getContext(), getString(R.string.scanning) + ",……",true,null);
        getScanFileTask(file.getPath(), FileUtil.getSortType(SortUtil.getSetSort())).startTask();
    }

    @Override
    protected AbsAsyncTask getScanTask(Comparator comparator) {
        clearClickPath();
        return getScanFileTask(null,comparator);
    }

    @Override
    public boolean onKeyDown() {
        if(mActivity != null) {
            mActivity.getMainMenu().currentMenuRequestFocus();
        }
        int pathCount = clickPathSize();
        ClickPath path;
        if (pathCount > 1){
            path = getClickPath(pathCount - 1);
            setFocusItemPosition(path.getPosition());
            removeClickPath(pathCount - 1);
            pathCount = clickPathSize() - 1;
            path = getClickPath(pathCount);
            mCurrentPath = path.getPath();
            setPath(path.getPath());
            getScanFileTask(path.getPath(), FileUtil.getSortType(SortUtil.getSetSort())).startTask();
        } else if(pathCount <= 1 && isNextDir){
            setPath(mClassName);
            path = getClickPath(pathCount - 1);
            setFocusItemPosition(path.getPosition());
            updateFile();
            isNextDir = false;
            return true;
        }
        return isNextDir;
    }

    /**
     * 设置分类名
     * @param name 分类名
     */
    protected void setClassName(String name) {
        mClassName = name;
    }

    /**
     * 限制条件，默认是false
     * @return true表示有，false表示无
     */
    public boolean isLimitCondition() {
        return false;
    }
}
