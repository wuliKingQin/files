package com.tv.framework.http.download;

import okhttp3.Callback;

/**
 * 描述：下载接口
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 14:00
 */

public interface IDownload extends Callback {
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
