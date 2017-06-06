package com.tv.framework.http.download;

import android.os.Handler;
import android.os.Message;

import com.tv.framework.http.RequestManager;
import com.tv.framework.utils.StorageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 描述：下载实现类
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 10:10
 */

public class Download implements IDownload{

    //默认读时间超出时间为60分钟
    private final static long READ_TIME_OUT = 60 * 60 * 1000;
    //保存下载超时时间
    private long mReadTimeOut = READ_TIME_OUT;
    //下载的文件名
    private String mFileName;
    //保存下载文件保存路径
    private File mDownloadFile;
    //下载接口回调
    private IDownloadCallback mCallback;
    //用于线程转换
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(mCallback != null) {
                switch (msg.what) {
                    case START:
                        mCallback.onBefore();
                        break;
                    case PROGRESS:
                        mCallback.onProgress(msg.arg2);
                        break;
                    case SUCCESS:
                        mCallback.onSuccess((File)msg.obj);
                        break;
                    case FAIL:
                        mCallback.onFail(msg.arg1,String.valueOf(msg.obj));
                        break;
                    case FINISH:
                    default:
                        mCallback.onFinish();
                        break;
                }
            }
        }
    };

    public Download() {
        mDownloadFile = StorageUtil.getDownloadCacheDir(RequestManager.instance().getContext());
    }

    /**
     * 下载方法
     * @param url 连接地址
     * @param fileName 文件名
     * @param callback 接口回调
     */
    public void download(String url,String fileName,IDownloadCallback callback) {
        mFileName = fileName;
        mCallback = callback;
        final Request request = new Request.Builder().url(url).build();
        getOkHttpClient().newCall(request).enqueue(this);
    }

    /**
     * 获取OkHttpClient
     * @return OkHttpClient
     */
    private OkHttpClient getOkHttpClient() {
        final Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new DownloadResponseBody(originalResponse.body(),mHandler))
                        .build();
            }
        };
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .readTimeout(mReadTimeOut, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        mHandler.obtainMessage(FAIL,-1,0,e.getMessage()).sendToTarget();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.isSuccessful()) {
            try {
                writeToSDCard(response.body());
            } catch (Exception e) {
                final File targetFile = new File(mDownloadFile,mFileName);
                if(targetFile != null && targetFile.exists()) {
                    targetFile.delete();
                }
                mHandler.obtainMessage(FAIL,response.code(),0,e.getMessage()).sendToTarget();
            }
        } else {
            mHandler.obtainMessage(FAIL,response.code(),0,response.body().string()).sendToTarget();
        }
        mHandler.obtainMessage(FINISH).sendToTarget();
    }

    /**
     * 将文件写到SDCard
     * @param response 响应
     * @throws Exception 异常
     */
    private void writeToSDCard(ResponseBody response) throws Exception{
        int len;
        final byte[] buf = new byte[2048];
        final InputStream is = response.byteStream();
        final File targetFile = new File(mDownloadFile,mFileName);
        final FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        while ((len = is.read(buf)) != -1) {
            fileOutputStream.write(buf, 0, len);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        is.close();
        mHandler.obtainMessage(SUCCESS,targetFile).sendToTarget();
    }

    /**
     * 设置读取超时时间
     * @param readTimeOut 超时时间
     */
    public void setReadTimeOut(long readTimeOut) {
        mReadTimeOut = readTimeOut;
    }
}
