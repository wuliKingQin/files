package com.tv.filemanager.bean;

import android.graphics.Bitmap;

/**
 * 功能描述：文件实例
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/17- 16:39
 */

public class CFile {

    //文件图标
    private int icon;
    //文件类型
    private Type type;
    //文件名
    private String name;
    //文件大小
    private long length;
    //文件路径
    private String path;
    //是否是文件
    private boolean isFile;
    //保存音视频的缩略图
    private Bitmap thumbnail;
    //文件名左边的图标
    private int nameLeftIcon;
    //文件后缀名
    private String suffixName;
    //子文件个数
    private int childFileCount;
    //最后修改时间
    private long lastModifiedTime;
    //是否被选中
    private boolean isSelected = false;
    //是否被删除
    private boolean isDeleted = false;

    public CFile() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getNameLeftIcon() {
        return nameLeftIcon;
    }

    public void setNameLeftIcon(int nameLeftIcon) {
        this.nameLeftIcon = nameLeftIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getChildFileCount() {
        return childFileCount;
    }

    public void setChildFileCount(int childFileCount) {
        this.childFileCount = childFileCount;
    }

    public String getSuffixName() {
        return suffixName;
    }

    public void setSuffixName(String suffixName) {
        this.suffixName = suffixName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * 文件类型
     */
    public enum Type {
        /**
         * 目录
         */
        DIR,
        /**
         * 文本
         */
        TXT,
        /**
         * zip
         */
        ZIP,
        /**
         * apk
         */
        APK,
        /**
         * png
         */
        PNG,
        /**
         * jpeg
         */
        JPG,
        /**
         * gif
         */
        GIF,
        /**
         * ppt
         */
        PPT,
        /**
         * word,doc
         */
        WORD,
        /**
         * pdf
         */
        PDF,
        /**
         * aac
         */
        AAC,
        /**
         * avi
         */
        AVI,
        /**
         * bt
         */
        BT,
        /**
         * excel
         */
        EXCEL,
        /**
         * mid
         */
        MID,
        /**
         * mkv
         */
        MKV,
        /**
         * flv
         */
        FLV,
        /**
         * flac
         */
        FLAC,
        /**
         * mp3
         */
        MP3,
        /**
         * mp4
         */
        MP4,
        /**
         * rmvb
         */
        RMVB,
        /**
         * rar
         */
        RAR,
        /**
         * wmw
         */
        WMW,
        /**
         * wma
         */
        WMA,
        /**
         * wav
         */
        WAV,
        /**
         * 其他文件
         */
        OTHER
    }
}
