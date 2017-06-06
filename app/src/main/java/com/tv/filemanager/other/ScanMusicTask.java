package com.tv.filemanager.other;

import com.tv.framework.adapter.AbsBaseAdapter;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：扫描音乐任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/20- 11:19
 */

public class ScanMusicTask extends AbsScanAssignFileTask {

    public ScanMusicTask(String path, Comparator comparator,AbsBaseAdapter adapter) {
        super(path, true , comparator, adapter);
    }

    public ScanMusicTask(Comparator comparator,AbsBaseAdapter adapter) {
        super(comparator, adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return getFileTypeMgr().isMusicFile(suffixName);
    }
}
