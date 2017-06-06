package com.tv.filemanager.bean;

import java.util.List;

/**
 * 功能描述：清理项目，用于扫码换成并清理器内容
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/22- 18:13
 */

public class CacheItem {

    //清理名字
    private String name;
    //清理内容
    private String content;
    //清理单位
    private String unit;
    //清理内容路径
    private String path;
    //文件
    private List<CFile> files;
    //用于判断是否被选中
    private boolean isSelected = false;

    public CacheItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<CFile> getFiles() {
        return files;
    }

    public void setFiles(List<CFile> files) {
        this.files = files;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
