package com.tv.framework.http.upload;


import okhttp3.Callback;

/**
 * 描述：上传文件接口
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 13:56
 */

public interface IUpload extends Callback {
    //开始上传
    int START = 0;
    //成功
    int SUCCESS = 1;
    //失败
    int FAIL = -2;
    //进度
    int PROGRESS = 3;
    //结束上传
    int FINISH = 4;
}
