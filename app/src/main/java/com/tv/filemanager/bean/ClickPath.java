package com.tv.filemanager.bean;

/**
 * 功能描述：点击进入目录路径
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/7- 14:18
 */

public class ClickPath {

    //文件路径
    private String path;
    //文件所处位置
    private int position = -1;

    public ClickPath(String path,int position) {
        this.path = path;
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
