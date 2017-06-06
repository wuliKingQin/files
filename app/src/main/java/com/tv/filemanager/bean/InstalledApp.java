package com.tv.filemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * 功能描述：已经安装的app
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/23- 18:35
 */

public class InstalledApp {

    //app图标
    private Drawable icon;
    //app名字
    private String name;
    //app包名
    private String packageName;

    public InstalledApp() {
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
