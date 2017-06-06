package com.tv.framework.http.download;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 描述：下载请求体，该类带进度
 * 创建作者：黎丝军
 * 创建时间：2016/12/26 18:03
 */

public class DownloadResponseBody extends ResponseBody {

    //用于线程转换进度
    private Handler mHandler;
    //下载资源
    private BufferedSource mSource;
    //保存原来的请求响应
    private ResponseBody mOriginResponse;
    //保存下载的内容总长度
    private long mContentLength = -1;


    public DownloadResponseBody(ResponseBody originResponse,Handler handler) {
        mHandler = handler;
        mOriginResponse = originResponse;
        mContentLength = originResponse.contentLength();
    }

    @Override
    public MediaType contentType() {
        return mOriginResponse.contentType();
    }

    @Override
    public long contentLength() {
        return mContentLength;
    }

    @Override
    public BufferedSource source() {
        if(mSource == null) {
            mSource = Okio.buffer(source(mOriginResponse.source()));
        }
        return mSource;
    }

    /**
     * 下载的数据源
     * @param source 源实例
     * @return Source实例
     */
    private Source source(Source source) {
        return new ForwardingSource(source) {
            double progress = 0L;
            long readCount = 0;
            int currentProgress = 0;
            int oldCurrentProgress = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                readCount = super.read(sink, byteCount);
                if(readCount > 0) {
                    progress += readCount;
                    currentProgress = (int)(progress / mContentLength * 100);
                    if(currentProgress -  oldCurrentProgress >= 1) {
                        oldCurrentProgress = currentProgress;
                        mHandler.obtainMessage(IDownload.PROGRESS,(int)progress,currentProgress,mContentLength).sendToTarget();
                    }
                } else {
                    Log.d("LiSiJun","readCount=" + readCount);
                }
                return readCount;
            }
        };
    }
}
