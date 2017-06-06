package com.tv.filemanager.other;

import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.Sort;
import com.tv.filemanager.utils.FileUtil;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：排序比较器
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/18- 15:11
 */

public class SortComparator implements Comparator<CFile> {

    //排序
    private Sort mSort;

    public SortComparator(Sort sort) {
        mSort = sort;
    }

    @Override
    public int compare(CFile fileFirst, CFile fileSecond) {
        switch (mSort.getType()) {
            case MODIFY_TIME:
                return sortByModifyTime(fileFirst,fileSecond);
            case FILE_NAME:
                return sortByFileName(fileFirst,fileSecond);
            case FILE_SIZE:
            default:
                return sortByFileSize(fileFirst,fileSecond);
        }
    }

    /**
     * 按修改时间来排序
     * @param fileFirst 前一个文件
     * @param fileSecond 后一个文件
     * @return 排序值
     */
    private int sortByModifyTime(CFile fileFirst, CFile fileSecond) {
        if (!fileFirst.isFile() && fileSecond.isFile() ){
            return  mSort.getOrder();
        }else if (fileFirst.isFile() && !fileSecond.isFile()){
            return -mSort.getOrder();
        }else {
            return fileFirst.getLastModifiedTime() <= fileSecond.getLastModifiedTime() == true ? -mSort.getOrder():mSort.getOrder();
        }
    }

    /**
     * 按修改文件大小来排序
     * @param fileFirst 前一个文件
     * @param fileSecond 后一个文件
     * @return 排序值
     */
    private int sortByFileSize(CFile fileFirst, CFile fileSecond) {
        if (!fileFirst.isFile() && fileSecond.isFile() ){
            return  mSort.getOrder();
        }else if (fileFirst.isFile() && !fileSecond.isFile() ){
            return -mSort.getOrder();
        }else {
            return fileFirst.getLength() <= fileSecond.getLength() == true ? -mSort.getOrder():mSort.getOrder();
        }
    }


    /**
     * 按修改文件名来排序
     * @param fileFirst 前一个文件
     * @param fileSecond 后一个文件
     * @return 排序值
     */
    private int sortByFileName(CFile fileFirst, CFile fileSecond) {
        if (!fileFirst.isFile() && fileSecond.isFile() ){
            return  mSort.getOrder();
        }else if (fileFirst.isFile() && !fileSecond.isFile() ){
            return -mSort.getOrder();
        }else {
            return fileFirst.getName().compareTo(fileSecond.getName()) >= 0 ? -mSort.getOrder() : mSort.getOrder();
        }
    }

    /**
     * 按其他类型排序，该方法默认实现采用按类型来排序
     * @param fileFirst 前一个文件
     * @param fileSecond 后一个文件
     * @return 排序值
     */
    protected int sortByOther(CFile fileFirst, CFile fileSecond) {
        if (!fileFirst.isFile() && fileSecond.isFile() ){
            return  mSort.getOrder();
        }else if (fileFirst.isFile() && !fileSecond.isFile() ){
            return -mSort.getOrder();
        }else {
            final String suffixFirst = FileUtil.getSuffixName(fileFirst.getName());
            final String suffixSecond = FileUtil.getSuffixName(fileSecond.getName());
            return suffixFirst.compareTo(suffixSecond) >= 0 ? -mSort.getOrder() : mSort.getOrder();
        }
    }
}
