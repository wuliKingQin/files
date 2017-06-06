package com.tv.filemanager.other;

import com.tv.framework.adapter.AbsBaseAdapter;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：扫描视频文件任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/20- 11:15
 */

public class ScanVideoTask extends AbsScanAssignFileTask {

    public ScanVideoTask(String path, Comparator comparator, AbsBaseAdapter adapter) {
        super(path, true , comparator, adapter);
    }

    public ScanVideoTask(Comparator comparator, AbsBaseAdapter adapter) {
        super(comparator, adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return getFileTypeMgr().isVideoFile(suffixName);
    }

    @Override
    protected boolean filterByCondition(File file) {
        return false;
    }
}
