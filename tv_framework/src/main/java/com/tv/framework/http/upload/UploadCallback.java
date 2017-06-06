package com.tv.framework.http.upload;

import java.io.File;

/**
 * 描述：上传回调接口实现类
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 10:04
 */

public class UploadCallback implements IUploadCallback{

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
    public void onFinish() {

    }

    @Deprecated
    public void onSuccess(File file) {
    }

    @Override
    public void onSuccess(String successInfo) {
    }
}
