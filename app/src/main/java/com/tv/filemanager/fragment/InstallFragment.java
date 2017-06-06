package com.tv.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.other.AbsAsyncTask;
import com.tv.filemanager.other.AbsScanAssignFileTask;
import com.tv.filemanager.other.ScanApkTask;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.utils.SettingUtil;
import com.tv.framework.utils.ToastUtil;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：安装页碎片
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 15:37
 */

public class InstallFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.install));
        setClassName(getString(R.string.install));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_install);
    }

    @Override
    public boolean isLimitCondition() {
        final boolean isInstall = SettingUtil.isInstallApk();
        if(!isInstall) {
            ToastUtil.toast(getContext(),R.string.can_not_install_out_app);
        }
        return !isInstall;
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanApkTask(path,comparator, getAdapter());
        }
        return new ScanApkTask(comparator, getAdapter());
    }
}
