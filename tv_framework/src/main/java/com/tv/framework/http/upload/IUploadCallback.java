package com.tv.framework.http.upload;

import com.tv.framework.http.download.IDownloadCallback;

import java.io.File;

/**
 * 描述：上传回调接口，该类实现了和下载一样的方法，所以该类继承IDownCallback接口
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 10:00
 */

public interface IUploadCallback extends IDownloadCallback {

    /**
     * 该方法是下载的方法，不适合上传文件
     * @param file 下载文件
     */
    @Deprecated
    void onSuccess(File file);

    /**
     * 上传成功时将调用该方法
     * @param successInfo 成功信息
     */
    void onSuccess(String successInfo);
}
