package com.tv.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.ClickPath;
import com.tv.filemanager.other.AbsAsyncTask;
import com.tv.filemanager.other.FileManager;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.utils.SettingUtil;
import com.tv.filemanager.utils.USBUtil;
import com.tv.framework.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：全部文集碎片
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 15:34
 */

public class DiskFragment extends AbsCommonFragment {

    //保存文件管理实例
    private FileManager mFileMgr;
    //保存是否是磁盘数据
    private boolean isDiskData = false;
    //存储扫描盘
    private List<CFile> storages = new ArrayList<>();
    //路径
    private List<String> mPaths = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFileMgr = FileManager.instance();
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.tv_disk);
        setFocusItemPosition(0);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        return mFileMgr.updateFile(path, getAdapter());
    }

    @Override
    protected void onItemClick(CFile file, int position) {
        if(file.isFile()) {
            isFileOpen = true;
            if(file.getType() == CFile.Type.APK) {
                if(SettingUtil.isInstallApk()) {
                    setFocusItemPosition(position);
                    FileUtil.openFileIntent(getContext(),file.getPath());
                } else {
                    ToastUtil.toast(getContext(),R.string.can_not_install_out_app);
                }
            } else {
                setFocusItemPosition(position);
                FileUtil.openFileIntent(getContext(),file.getPath());
            }
        } else {
            isNextDir = true;
            isDiskData = false;
            setPathBarVisibility(View.VISIBLE);
            setFocusItemPosition(-1);
            addClickPath(file.getPath() + File.separator, position);
            setPath(file.getPath() + File.separator);
            mFileMgr.updateFile(file.getPath(), getAdapter()).startTask();
        }
    }

    @Override
    protected AbsAsyncTask getScanTask(Comparator comparator) {
        clearClickPath();
        String usbPath = USBUtil.getUsbStorageDir();
        if(!isDiskData) {
            loadDiskData(usbPath);
        } else {
            isDiskData = true;
            getAdapter().putData(storages);
        }
        setPathBarVisibility(View.GONE);
        return  null;
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
            mFileMgr.updateFile(path.getPath(), getAdapter(),
                    FileUtil.getSortType(getDefaultSort())).startTask();
        } else if(pathCount == 1 && isNextDir){
            final int position = pathCount - 1;
            path = getClickPath(position < 0 ? 0 : position);
            setFocusItemPosition(path.getPosition());
            updateFile();
            isNextDir = false;
            return true;
        }
        return isNextDir;
    }

    /**
     * 更新文件
     * @param usbPath usb存储路径
     */
    public void updateFile(String usbPath) {
        if(usbPath != null) {
            isNextDir = false;
            loadDiskData(usbPath);
        } else {
            if(isDiskData) {
                storages.clear();
                isDiskData = false;
            }
            updateFile();
        }
    }

    /**
     * 导入磁盘数据
     * @param usbPath usb路径
     */
    private void loadDiskData(String usbPath) {
        mPaths.clear();
        isDiskData = true;
        storages.clear();
        setPathBarVisibility(View.GONE);
        final File storeFile = Environment.getExternalStorageDirectory();
        CFile localStorage = new CFile();
        localStorage.setIcon(R.mipmap.ic_disk);
        localStorage.setPath(storeFile.getPath());
        localStorage.setChildFileCount(FileUtil.getFileChildCount(storeFile));
        localStorage.setName(getString(R.string.local_disk) + storeFile.getName());
        storages.add(localStorage);
        if(usbPath != null) {
            File usbFile = new File(usbPath);
            localStorage = new CFile();
            localStorage.setIcon(R.mipmap.ic_disk);
            localStorage.setPath(usbFile.getPath());
            localStorage.setChildFileCount(FileUtil.getFileChildCount(usbFile));
            localStorage.setName(getString(R.string.out_disk) + usbFile.getName());
            storages.add(localStorage);
        }
        getAdapter().putData(storages);
    }

    public boolean isDiskData() {
        return isDiskData;
    }
}
