package com.tv.framework.http.upload;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 描述：文件上传实现类
 * 创建作者：黎丝军
 * 创建时间：2016/12/24 10:11
 */

public class Upload implements IUpload{

    //默认写时间超出时间为60分钟
    private final static long WRITE_TIME_OUT = 60 * 60 * 1000;
    //保存上传超时时间
    private long mWriteTimeOut = WRITE_TIME_OUT;
    //上传接口回调
    private IUploadCallback mUploadCallback;
    //用于处理线程转换
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(mUploadCallback != null) {
                switch (msg.what) {
                    case START:
                        mUploadCallback.onBefore();
                        break;
                    case PROGRESS:
                        mUploadCallback.onProgress(msg.arg2);
                        break;
                    case SUCCESS:
                        mUploadCallback.onSuccess(String.valueOf(msg.obj));
                        break;
                    case FAIL:
                        mUploadCallback.onFail(msg.arg1,String.valueOf(msg.obj));
                        break;
                    case FINISH:
                    default:
                        mUploadCallback.onFinish();
                        break;
                }
            }
        }
    };

    public Upload() {
    }

    /**
     * 上传文件的方法
     * @param url 上传地址
     * @param uploadFile 文件
     */
    public void upload(String url, File uploadFile, IUploadCallback callback) {
        upload(url,uploadFile,MediaType.parse("application/octet-stream"),callback);
    }

    /**
     * 上传文件的方法
     * @param url 上传地址
     * @param uploadFile 上传文件
     * @param mediaType 媒体类型，如果为null,则默认为application/octet-stream类型
     * @param callback 接口回调
     */
    public void upload(String url, File uploadFile, MediaType mediaType,IUploadCallback callback) {
        mUploadCallback = callback;
        mHandler.obtainMessage(START).sendToTarget();
        final RequestBody requestBody = RequestBody.create(mediaType,uploadFile);
        final Request request = new Request.Builder().url(url).post(new UploadRequestBody(requestBody,mHandler)).build();
        getOkHttpClient().newCall(request).enqueue(this);
    }

    /**
     * 以表单的形式上传文件
     * @param url 上传地址
     * @param uploadParams 上传参数
     * @param callback 回调接口
     */
    public void upload(String url,Map<String,Object> uploadParams,IUploadCallback callback) {
        upload(url,MultipartBody.FORM,uploadParams,callback);
    }

    /**
     * 带参数的上传文件，要上传的文件包含在参数里
     * @param url 上传路径
     * @param mediaType 媒体类型
     * @param uploadParams 上传参数
     * @param callback 上传回调接口
     */
    public void upload(String url,MediaType mediaType,Map<String,Object> uploadParams,IUploadCallback callback) {
        File targetFile;
        Object uploadParam;
        mUploadCallback = callback;
        mHandler.obtainMessage(START).sendToTarget();
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(mediaType);
        for (String key : uploadParams.keySet()) {
            uploadParam = uploadParams.get(key);
            if (uploadParam instanceof File) {
                targetFile = (File) uploadParam;
                if(targetFile != null && targetFile.exists() && targetFile.isFile()) {
                    builder.addFormDataPart(key, targetFile.getName(), RequestBody.create(mediaType,targetFile));
                }
            } else {
                builder.addFormDataPart(key, String.valueOf(uploadParam));
            }
        }
        try {
            final MultipartBody multipartBody = builder.build();
            final Request request = new Request.Builder().url(url).post(new UploadRequestBody(multipartBody,mHandler)).build();
            getOkHttpClient().newCall(request).enqueue(this);
        } catch (Exception e) {
            mHandler.obtainMessage(FAIL,-1,0,e.getMessage()).sendToTarget();
            mHandler.obtainMessage(FINISH).sendToTarget();
        }
    }

    /**
     * 获取客户端
     * @return 客户端
     */
    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .writeTimeout(mWriteTimeOut, TimeUnit.MILLISECONDS)
                .build();
    }


    @Override
    public void onFailure(Call call, IOException e) {
        mHandler.obtainMessage(FAIL,-1,0,e.getMessage()).sendToTarget();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.isSuccessful()) {
            mHandler.obtainMessage(SUCCESS,response.body().string()).sendToTarget();
        } else {
            mHandler.obtainMessage(FAIL,response.code(),0,response.body().string()).sendToTarget();
        }
        mHandler.obtainMessage(FINISH).sendToTarget();
    }

    /**
     * 设置读取超时时间
     * @param writeTimeOut 超时时间
     */
    public void setReadTimeOut(long writeTimeOut) {
        mWriteTimeOut = writeTimeOut;
    }

}
