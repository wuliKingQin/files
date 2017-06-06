package com.tv.filemanager.other;

import android.content.Context;
import android.util.Log;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.utils.DialogUtil;
import com.tv.framework.utils.ProgressUtil;
import com.tv.framework.utils.ToastUtil;
import com.tv.framework.widget.view.HintDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：复制文件操作异步的任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/5- 10:42
 */

public class CopyFileTask extends AbsAsyncTask<Boolean> {

    //新目录
    private String mNewFile;
    //运行环境
    private Context mContext;
    //是否取消
    private boolean isCancel = false;
    //老文件
    private List<CFile> mCopyFiles = new ArrayList<>();

    public CopyFileTask(Context context,CFile copyFiles, String newFile) {
        mContext = context;
        mNewFile = newFile;
        mCopyFiles.add(copyFiles);

    }

    public CopyFileTask(Context context,List<CFile> copyFiles, String newFile) {
        mContext = context;
        mNewFile = newFile;
        mCopyFiles.addAll(copyFiles);
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        boolean isFlag = false;
        for (CFile copyFile: mCopyFiles) {
            if(isCancel) break;
            isFlag = copyFileToOther(copyFile.getPath(),mNewFile);
            if(!isFlag) break;
        }
        return isFlag;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        ProgressUtil.dismissDialog();
        if(aBoolean) {
            ToastUtil.toast(mContext,R.string.copy_success);
        } else {
            ToastUtil.toast(mContext,R.string.copy_fail);
        }

    }

    /**
     * 复制文件到目录
     * @param old 需要复制的文件
     * @param newDir 新的目录
     * @return 成功返回0不成功返回-1
     */
    private boolean copyFileToOther(String old, String newDir) {
        final File old_file = new File(old);
        final File temp_dir = new File(newDir);
        byte[] data = new byte[2048];
        int read;
        if(old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite()){
            String file_name = old.substring(old.lastIndexOf("/"), old.length());
            File cp_file = new File(newDir + file_name);
            try {
                BufferedOutputStream o_stream = new BufferedOutputStream(
                        new FileOutputStream(cp_file));
                BufferedInputStream i_stream = new BufferedInputStream(
                        new FileInputStream(old_file));
                while(!isCancel && (read = i_stream.read(data, 0, 2048)) != -1) {
                    o_stream.write(data, 0, read);
                }
                if(isCancel) {
                    temp_dir.delete();
                }
                o_stream.flush();
                i_stream.close();
                o_stream.close();
            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.getMessage());
                return false;
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                return false;
            }
        } else if(old_file.isDirectory() && temp_dir.isDirectory() && temp_dir.canWrite()) {
            String files[] = old_file.list();
            String dir = newDir + old.substring(old.lastIndexOf("/"), old.length());
            int len = files.length;
            if(!new File(dir).mkdir()) {
                return false;
            }
            for(int i = 0; i < len; i++) {
                copyFileToOther(old + "/" + files[i], dir);
            }
        } else if(!temp_dir.canWrite()) {
            return false;
        }
        return true;
    }

    /**
     * 显示进度
     */
    private void showProgress() {
        ProgressUtil.showDialog(mContext, R.string.copy_hint, new ProgressUtil.ICancelCallback() {
            @Override
            public void onCancel() {
                cancelCopy();
            }
        });
    }

    @Override
    public void startTask() {
        showProgress();
        super.startTask();
    }

    /**
     * 取消复制
     */
    public void cancelCopy() {
        isCancel = true;
        cancel(isCancel);
    }

    /**
     * 检查大小
     * @return true表示太大
     */
    public boolean checkSize() {
        int length = 0;
        for (CFile copyFile: mCopyFiles) {
            length += copyFile.getLength();
        }
        if(length >= 524288000) {
           return true;
        }
        return false;
    }
}
