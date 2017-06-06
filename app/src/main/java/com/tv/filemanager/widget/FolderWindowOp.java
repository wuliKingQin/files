package com.tv.filemanager.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.adapter.FolderAdapter;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.other.CopyFileTask;
import com.tv.filemanager.other.ScanFolderTask;
import com.tv.filemanager.utils.DialogUtil;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.utils.USBUtil;
import com.tv.framework.adapter.AbsBaseAdapter;
import com.tv.framework.utils.ProgressUtil;
import com.tv.framework.utils.ToastUtil;
import com.tv.framework.widget.view.HintDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：文件选择弹出框
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/27- 14:50
 */

public class FolderWindowOp {

    //保存需要复制文件
    private CFile mCopyFile;
    //保存当前路经
    private String mSelectedPath;
    //选项视图
    private View mOptionView;
    //用于弹出选择窗体
    private PopupWindow mWindow;
    //确认路径
    private TextView mSurePathBtn;
    //选择的路径
    private TextView mSelectedPathTv;
    //返回父文件
    private TextView mBackParentTv;
    //适配器
    private FolderAdapter mAdapter;
    //文件夹视图
    private RecyclerView mFolderView;
    //需要复制的文件
    private List<CFile> mFiles = null;
    //复制到
    private String mCopyToStr;
    //运行环境
    private Context mContext;
    //路径
    private List<String> mPaths = new ArrayList<>();

    public FolderWindowOp(Context context) {
        mContext = context;
        mOptionView = LayoutInflater.from(context).inflate(R.layout.window_file_select,null);
        mWindow = new PopupWindow(mOptionView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mFolderView = (RecyclerView) mOptionView.findViewById(R.id.rv_file_select);
        mBackParentTv = (TextView) mOptionView.findViewById(R.id.tv_back_parent);
        mSelectedPathTv = (TextView) mOptionView.findViewById(R.id.tv_select_path);
        mSurePathBtn = (TextView) mOptionView.findViewById(R.id.tv_sure);

        mFolderView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new FolderAdapter(mFolderView);
        mFolderView.setAdapter(mAdapter);

        mBackParentTv.setOnFocusChangeListener(mFocusChangeListener);
        mBackParentTv.setOnClickListener(mBackParentListener);
        mSurePathBtn.setOnClickListener(mClickSureListener);
        mAdapter.setOnItemClickListener(mItemClickListener);

        mCopyToStr = context.getString(R.string.copy_to);
        mSelectedPathTv.setText(mCopyToStr + context.getString(R.string.not_select_path));

        initData();

        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mOptionView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    //焦点改变监听器
    private View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
                v.setBackgroundColor(v.getResources().getColor(R.color.color5));
            } else {
                v.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    };

    //返回父路径监听器
    private View.OnClickListener mBackParentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pathCount = mPaths.size();
            if (pathCount > 1){
                mPaths.remove(mPaths.get(pathCount - 1));
                pathCount = mPaths.size() - 1;
                mSelectedPath = mPaths.get(pathCount);
                updateFolder(mSelectedPath);
            } else {
                initData();
            }
        }
    };

    /**
     * 点击确认按钮
     */
    private View.OnClickListener mClickSureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CopyFileTask task;
            if(mCopyFile != null && mSelectedPath != null) {
                task = new CopyFileTask(mContext,mCopyFile,mSelectedPath);
                if(task.checkSize()) {
                    DialogUtil.fileTooLengthDialog(mContext, new HintDialog.IDialogCallback() {
                        @Override
                        public void onCancel() {
                           task.cancelCopy();
                        }

                        @Override
                        public void onOk(String text) {
                            task.startTask();
                        }
                    });
                } else {
                    task.startTask();
                }
            } else {
                if(mFiles != null && mSelectedPath != null) {
                    task = new CopyFileTask(mContext, mFiles,mSelectedPath);
                    if(task.checkSize()) {
                        DialogUtil.fileTooLengthDialog(mContext, new HintDialog.IDialogCallback() {
                            @Override
                            public void onCancel() {
                                task.cancelCopy();
                            }

                            @Override
                            public void onOk(String text) {
                                task.startTask();
                            }
                        });
                    } else {
                        task.startTask();
                    }
                } else if(mFiles == null || mCopyFile == null){
                    ToastUtil.toast(v.getContext(),v.getContext().getString(R.string.not_select_file));
                } else {
                    ToastUtil.toast(v.getContext(),v.getContext().getString(R.string.not_select_dir));
                }
            }
            dismiss();
        }
    };

    //设置监听器
    private AbsBaseAdapter.OnItemClickListener mItemClickListener = new AbsBaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(AbsBaseAdapter adapter, int position) {
            final CFile file = mAdapter.getItem(position);
            mSelectedPath = file.getPath();
            mPaths.add(mSelectedPath);
            mSelectedPathTv.setText(mCopyToStr + file.getPath());
            updateFolder(file.getPath());
            final int pathCount = mPaths.size();
            if(pathCount > 0) {
                mBackParentTv.setVisibility(View.VISIBLE);
            } else {
                mBackParentTv.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        mPaths.clear();
        mBackParentTv.setVisibility(View.GONE);
        final String usbPath = USBUtil.getUsbStorageDir();
        final File storeFile = Environment.getExternalStorageDirectory();
        if(usbPath != null) {
            final List<CFile> storages = new ArrayList<>();
            CFile newFile = new CFile();
            newFile.setIcon(R.mipmap.ic_menu_disk);
            newFile.setPath(storeFile.getPath());
            newFile.setName(mContext.getString(R.string.local_disk) + storeFile.getName());
            storages.add(newFile);
            final File usbFile = new File(usbPath);
            newFile = new CFile();
            newFile.setIcon(R.mipmap.ic_menu_disk);
            newFile.setPath(usbFile.getPath());
            newFile.setName(mContext.getString(R.string.out_disk) + usbFile.getName());
            storages.add(newFile);
            mAdapter.putData(storages);
        } else {
            updateFolder(null);
            mSelectedPath = storeFile.getPath();
            mSelectedPathTv.setText(mCopyToStr + mSelectedPath);
        }
    }

    /**
     * 更新文件夹列表
     * @param filePath 文件路径
     */
    private void updateFolder(String filePath) {
        final ScanFolderTask folderTask = new ScanFolderTask(filePath) {
            @Override
            protected void onPostExecute(List<CFile> cFiles) {
                mAdapter.putData(cFiles);
            }
        };
        folderTask.startTask();
    }

    /**
     * 设置需要复制的文件路径
     * @param copyFile 文件路经
     */
    public void setCopyFile(CFile copyFile) {
        mCopyFile = copyFile;
    }

    /**
     * 设置要复制的文件
     * @param file 文件列表
     */
    public void setCopyFiles(List<CFile> file) {
        mFiles = file;
    }

    /**
     * 弹出选择窗体
     * @param supportView 支持视图
     */
    public void windowOption(View supportView) {
        if(mWindow != null)  {
            initData();
            mWindow.showAsDropDown(supportView);
        }
    }

    /**
     * 取消弹出框
     */
    public void dismiss() {
        if(mWindow != null) {
            mWindow.dismiss();
        }
    }
}
