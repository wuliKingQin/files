package com.tv.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.other.AbsAsyncTask;
import com.tv.filemanager.other.AbsScanAssignFileTask;
import com.tv.filemanager.other.ScanMusicTask;
import com.tv.filemanager.utils.FileUtil;

import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：音乐碎片
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 15:32
 */

public class MusicFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.music));
        setClassName(getString(R.string.music));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_music);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanMusicTask(path, comparator, getAdapter());
        }
        return new ScanMusicTask(comparator, getAdapter());
    }
}
