package com.tv.framework.http.download;

import java.io.File;

/**
 * 描述：下载接口回调的实现类，目的在于方便按需实现以下方法
 * 创建作者：黎丝军
 * 创建时间：2016/12/26 16:39
 */

public class DownloadCallback implements IDownloadCallback {

    @Override
    public void onBefore() {

    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onFail(int errorCode, String errorInfo) {

    }

    @Override
    public void onSuccess(File file) {

    }

    @Override
    public void onFinish() {

    }
}
