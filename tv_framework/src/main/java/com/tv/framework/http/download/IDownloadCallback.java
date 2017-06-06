package com.tv.framework.http.download;

import java.io.File;
/**
 * 描述：下载回调接口，包含下载开始，下载进度，下载失败，下载成功，下载完成等方法
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 9:50
 */

public interface IDownloadCallback {

    /**
     * 下载之前,比如添加等待弹出框
     */
    void onBefore();

    /**
     * 下载进度
     * @param progress 下载总进度值
     */
    void onProgress(int progress);

    /**
     * 下载失败
     * @param errorCode 失败码
     * @param errorInfo 失败信息
     */
    void onFail(int errorCode, String errorInfo);

    /**
     * 下载成功
     * @param file 下载文件
     */
    void onSuccess(File file);

    /**
     * 下载完成
     */
    void onFinish();

}
