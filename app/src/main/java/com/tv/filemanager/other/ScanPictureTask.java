package com.tv.filemanager.other;

import com.tv.filemanager.utils.SettingUtil;
import com.tv.framework.adapter.AbsBaseAdapter;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：扫描图片任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/20- 11:21
 */

public class ScanPictureTask extends AbsScanAssignFileTask {

    public ScanPictureTask(Comparator comparator, AbsBaseAdapter adapter) {
        super(comparator, adapter);
    }

    public ScanPictureTask(String path, Comparator comparator, AbsBaseAdapter adapter) {
        super(path, true , comparator, adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return getFileTypeMgr().isPictureFile(suffixName);
    }

    @Override
    protected boolean filterByCondition(File file) {
        if(SettingUtil.getPicSetting()) {
            if (file.length() < 10240) {
                return true;
            }
            return false;
        }
        return super.filterByCondition(file);
    }
}
