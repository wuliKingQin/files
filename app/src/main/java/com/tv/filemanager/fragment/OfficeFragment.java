package com.tv.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.other.AbsAsyncTask;
import com.tv.filemanager.other.ScanOfficeTask;

import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：用于过滤办公类文件
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/31- 17:21
 */

public class OfficeFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.office));
        setClassName(getString(R.string.office));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_office);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanOfficeTask(path,comparator,getAdapter());
        }
        return new ScanOfficeTask(comparator,getAdapter());
    }

}
