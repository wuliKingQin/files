package com.tv.framework.http;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.ImageLoader;
import com.tv.framework.utils.StorageUtil;

import java.io.File;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;

/**
 * 描述：请求管理器，该管理器主要管理一个请求队列和一个图片导入器
 * 创建作者：黎丝军
 * 创建时间：2016/12/21 14:40
 */
public class RequestManager {

    //实例
    private static RequestManager ourInstance = new RequestManager();
    //获取实例
    public static RequestManager instance() {
        return ourInstance;
    }
    //保存app唯一的http请求队列
    private RequestQueue mHttpRequestQueue;
    //保存OkHttpClient
    private OkHttpClient mOkHttpClient;
    //用于图片下载缓存
    private LruBitmapCache mLruBitmapCache;
    //用于图片下载导入
    private ImageLoader mImageLoader;
    //弱类型运行环境
    private WeakReference<Context> mContext;

    private RequestManager() {
    }

    /**
     * 初始化Http请求,该方法需要在Application的onCreate()方法中被调用初始化
     * @param context 运行环境
     */
    public void initRequest(Context context) {
        if(mHttpRequestQueue == null) {
            mContext = new WeakReference<>(context);
            mLruBitmapCache = new LruBitmapCache(context);
            mHttpRequestQueue = newRequestQueue(context);
            mImageLoader = new ImageLoader(mHttpRequestQueue,mLruBitmapCache);
        }
    }

    /**
     * 创建请求队列
     * @param context 运行环境
     * @return 请求队列实例
     */
    private  RequestQueue newRequestQueue(Context context) {
        mOkHttpClient = new OkHttpClient();
        final File cacheDir = StorageUtil.getCacheDir(context);
        final HttpStack stack = new OkHttpStack(mOkHttpClient);
        final BasicNetwork basicNetwork = new BasicNetwork(stack);
        final RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), basicNetwork, 4);
        queue.start();
        return queue;
    }


    /**
     * 添加请求
     * @param request 请求实例
     */
    public void addRequest(Request request) {
        addRequest(request,request.getClass());
    }

    /**
     * 添加请求
     * @param request 请求实例
     * @param tag 标签
     */
    public void addRequest(Request request, Object tag) {
        if(mHttpRequestQueue != null) {
            request.setTag(tag);
            mHttpRequestQueue.add(request);
        } else {
            throw new NullPointerException("mHttpRequestQueue is null");
        }
    }

    /**
     * 使用tag来取消http请求
     * @param requestTag 标签
     */
    public void cancelRequest(Object requestTag) {
        if(mHttpRequestQueue != null) {
            mHttpRequestQueue.cancelAll(requestTag);
        }
    }

    /**
     * 获取请求队列
     * @return RequestQueue实例
     */
    public RequestQueue getRequestQueue() {
        return mHttpRequestQueue;
    }

    /**
     * 获取ImageLoader实例，用于方法来实现图片的导入
     * @return ImageLoader实例
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 根据网络url来导入图片，该方法一般用在头像的视图
     * @param targetView 视图目标
     * @param url url
     * @param defaultResId 默认图片
     */
    public void imageLoader(final ImageView targetView, String url, final int defaultResId) {
        if(mImageLoader != null) {
            mImageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean isCache) {
                    if(imageContainer.getBitmap() != null) {
                        targetView.setImageBitmap(imageContainer.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    targetView.setImageResource(defaultResId);
                }
            });
        }
    }

    /**
     * 获取运行环境实例
     * @return Context实例
     */
    public Context getContext() {
        return mContext.get();
    }

    /**
     * 获取OkHttpClient实例
     * @return OkHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

}
