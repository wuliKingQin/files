package com.tv.filemanager.other;

import android.text.TextUtils;

import com.tv.framework.adapter.AbsBaseAdapter;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：扫描apk任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/20- 11:22
 */

public class ScanApkTask extends AbsScanAssignFileTask {

    public ScanApkTask(String path, Comparator comparator, AbsBaseAdapter adapter) {
        super(path, true , comparator, adapter);
    }

    public ScanApkTask(Comparator comparator, AbsBaseAdapter adapter) {
        super(comparator, adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return TextUtils.equals(suffixName,"apk");
    }
}
