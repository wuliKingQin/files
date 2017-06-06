package com.tv.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tv.filemanager.R;
import com.tv.filemanager.other.AbsAsyncTask;
import com.tv.filemanager.other.ScanAllFileTask;
import java.util.Comparator;

/**
 * 功能描述：全部文件，包含本地和磁盘
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/5- 10:06
 */

public class AllFileFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.all_file));
        setClassName(getString(R.string.all_file));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_all);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanAllFileTask(path,comparator,getAdapter());
        }
        return new ScanAllFileTask(comparator, getAdapter());
    }
}
