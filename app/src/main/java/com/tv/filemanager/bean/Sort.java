package com.tv.filemanager.bean;

/**
 * 功能描述：排序
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/17- 13:49
 */

public class Sort {
    //类型
    private Type type;
    //-1表示降序，1表示升序
    private int order;

    public Sort(Type type, int order) {
        this.type = type;
        this.order = order;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 排序类型
     */
    public enum  Type {
        /**
         * 文件修改时间
         */
        MODIFY_TIME,
        /**
         * 文件名字
         */
        FILE_NAME,
        /**
         * 文件大小
         */
        FILE_SIZE,
        /**
         * 其他排序
         */
        OTHER
    }
}
