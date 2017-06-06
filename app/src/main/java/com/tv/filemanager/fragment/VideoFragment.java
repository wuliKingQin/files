package com.tv.filemanager.fragment;


import android.content.Context;
import android.os.Bundle;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.Sort;
import com.tv.filemanager.other.AbsAsyncTask;
import com.tv.filemanager.other.FileManager;
import com.tv.filemanager.other.FileTypeMgr;
import com.tv.filemanager.other.ScanPictureTask;
import com.tv.filemanager.other.ScanVideoTask;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.utils.SortUtil;
import com.tv.framework.utils.ProgressUtil;

import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：视频碎片
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 15:33
 */

public class VideoFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.video));
        setClassName(getString(R.string.video));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_video);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanVideoTask(path, comparator, getAdapter());
        } else {
            return new ScanVideoTask(comparator,getAdapter());
        }
    }
}
