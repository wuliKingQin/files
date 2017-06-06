package com.tv.filemanager.other;


import android.graphics.Bitmap;
import android.text.TextUtils;
import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.utils.FileUtil;
import com.tv.framework.adapter.AbsBaseAdapter;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：扫描指定文件任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/20- 9:56
 */

public abstract class AbsScanAssignFileTask extends ScanAllFileTask {

    //保存当前扫描的目录
    private String mCurrentDirName;
    //保存文件类型管理器实例
    private FileTypeMgr mFileTypeMgr;
    //保存是否默认扫描还是点击进入
    private boolean isClick = false;

    public AbsScanAssignFileTask(String file, boolean isClick, Comparator comparator, AbsBaseAdapter adapter) {
        super(file, comparator, adapter);
        this.isClick = isClick;
        mFileTypeMgr = FileTypeMgr.instance();
    }

    public AbsScanAssignFileTask(Comparator comparator, AbsBaseAdapter adapter) {
        super(comparator, adapter);
        isClick = false;
        mFileTypeMgr = FileTypeMgr.instance();
    }

    @Override
    public boolean accept(File file) {
        String suffixName = FileUtil.getSuffixName(file.getName());
        if(isClick) {
            if(file.isFile() && isAssignFile(suffixName)) {
                if (!filterByCondition(file)) {
                    getAssignFile(file, suffixName);
                }
            }
        } else {
            if(file.isFile() && isAssignFile(suffixName)) {
                if(!filterByCondition(file)) {
                    final File parentFile = file.getParentFile();
                    suffixName = FileUtil.getSuffixName(parentFile.getName());
                    if(!TextUtils.equals(mCurrentDirName, parentFile.getName())) {
                        mCurrentDirName = parentFile.getName();
                        scanDir(parentFile,suffixName);
                    }
                }
            } else if(file.isDirectory()) {
                getAssignFiles(file);
            }
        }
        return false;
    }

    /**
     * 扫描目录
     */
    protected void scanDir(File file, String suffixName) {
        final CFile dir = new CFile();
        dir.setName(file.getName());
        dir.setIcon(R.mipmap.ic_folder);
        dir.setFile(file.isFile());
        dir.setSuffixName(suffixName);
        dir.setType(FileUtil.getType(file.getName()));
        dir.setChildFileCount(findChildCount(file));
        dir.setPath(file.getPath());
        dir.setLastModifiedTime(file.lastModified());
        putFileData(dir);
    }

    /**
     * 查找符合条件文件个数
     * @param file 目标文件夹
     * @return 文件个数
     */
    private int findChildCount(File file) {
        int count = 0;
        for(File childFile:file.listFiles()) {
            String suffixName = FileUtil.getSuffixName(childFile.getName());
            if(childFile.isFile() && isAssignFile(suffixName)) {
                if(!filterByCondition(childFile)) {
                    count ++;
                }
            }
        }
        return count;
    }

    /**
     * 添加文件到列表
     * @param file 文件
     * @param suffixName 后缀名
     */
    private void getAssignFile(File file,String suffixName) {
        final CFile assign = new CFile();
        assign.setName(file.getName());
        assign.setFile(file.isFile());
        if(mFileTypeMgr.isPictureFile(suffixName)) {
            final Bitmap image = FileUtil.getImageThumbnail(file.getPath(),219,219);
            if(image != null) {
                assign.setThumbnail(image);
            } else {
                assign.setIcon(R.mipmap.ic_pic_failed);
            }
        } else if(mFileTypeMgr.isVideoFile(suffixName)) {
            final Bitmap videoImage = FileUtil.getVideoThumbnail(file.getPath(),219,219);
            if(videoImage != null) {
                assign.setThumbnail(videoImage);
            } else {
                assign.setIcon(R.mipmap.ic_video);
            }
        } else if(mFileTypeMgr.isMusicFile(suffixName)) {
            assign.setIcon(R.mipmap.ic_music);
        } else if(mFileTypeMgr.isOfficeFile(suffixName)) {
            if(suffixName.contains("doc")) {
                assign.setIcon(R.mipmap.ic_doc);
            } else if(suffixName.contains("exl")) {
                assign.setIcon(R.mipmap.ic__excel);
            } else if(suffixName.contains("ppt")) {
                assign.setIcon(R.mipmap.ic_ppt);
            } else if(suffixName.contains("pdf")) {
                assign.setIcon(R.mipmap.ic_pdf);
            }
        } else if(suffixName.contains("apk")) {
            assign.setIcon(R.mipmap.ic_apk);
        }
        assign.setSuffixName(suffixName);
        assign.setType(FileUtil.getType(file.getName()));
        assign.setLength(file.length());
        assign.setPath(file.getPath());
        assign.setLastModifiedTime(file.lastModified());
        putFileData(assign);
    }

    /**
     * 根据后缀名判断是否是指定的文件
     * @param suffixName 后缀名
     * @return true表示，否则表示不是
     */
    protected abstract boolean isAssignFile(String suffixName);

    /**
     * 通过指定的条件来是否过滤调该文件
     * @param file 文件
     * @return true表示需要过滤，否则不需要
     */
    protected  boolean filterByCondition(File file) {
        return false;
    }

    /**
     * 得到文件类型管理器实例
     * @return FileTypeMgr实例
     */
    protected FileTypeMgr getFileTypeMgr() {
        return mFileTypeMgr;
    }
}
