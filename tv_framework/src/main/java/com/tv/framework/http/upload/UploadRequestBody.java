package com.tv.framework.http.upload;

import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * 描述：上传请求体实现类
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 14:29
 */

public class UploadRequestBody extends RequestBody {

    //用于线程转换
    private Handler mHandler;
    //保存原始的请求体
    private RequestBody mRequestBody;
    //资源文件缓存器
    private BufferedSink  mBufferedSink;
    //保存总长度
    private long mContentLength = -1;

    public UploadRequestBody(RequestBody requestBody,Handler handler) {
        mHandler = handler;
        mRequestBody = requestBody;
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        if(mBufferedSink == null){
            //开始包装
            mBufferedSink = Okio.buffer(sink(bufferedSink));
        }
        //写入
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    /**
     * 写入封装
     * @param sink 传入需要写入的行道
     * @return Sink实例
     */
    private Sink sink(Sink sink){
        return new ForwardingSink(sink) {

            //当前写进度
            double writeProgress = 0L;
            //当前总进度
            int currentProgress = 0;
            //之前变化的总进度
            int oldCurrentProgress = 0;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                writeProgress += byteCount;
                if(mContentLength <= 0) mContentLength = contentLength();
                currentProgress = (int)(writeProgress / mContentLength * 100);
                if(currentProgress -  oldCurrentProgress >= 1) {
                    oldCurrentProgress = currentProgress;
                    mHandler.obtainMessage(IUpload.PROGRESS,(int)writeProgress,currentProgress).sendToTarget();
                } else {
                    Log.d("LiSiJun","currentProgress=" + currentProgress);
                }
            }
        };
    }
}
