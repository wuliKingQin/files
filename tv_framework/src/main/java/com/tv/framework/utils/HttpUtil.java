package com.tv.framework.utils;

import android.widget.ImageView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.tv.framework.http.IRequestCallback;
import com.tv.framework.http.RequestManager;
import com.tv.framework.http.download.Download;
import com.tv.framework.http.download.IDownloadCallback;
import com.tv.framework.http.upload.IUploadCallback;
import com.tv.framework.http.upload.Upload;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 描述：http工具类，该类工具类有get、post请求，和下载和上传等
 * 创建作者：黎丝军
 * 创建时间：2016/12/21 16:47
 */

public class HttpUtil {

    private HttpUtil() {
    }

    /**
     * GET请求,请求结果返回JsonObject类型
     * @param url 请求地址
     * @param callback 请求回调接口
     */
    public static void getJsonObject(String url, final IRequestCallback<JSONObject> callback) {
        getJsonObject(url,null,callback);
    }

    /**
     * GET请求,请求结果返回JsonObject类型
     * @param url 请求地址
     * @param requestParams 请求参数
     * @param callback 请求回调接口
     */
    public static void getJsonObject(String url, final Map<String,Object> requestParams, final IRequestCallback<JSONObject> callback) {
        callback.onBefore();
        final Request request = new JsonObjectRequest(url,callback,callback) {
            @Override
            protected void onFinish() {
                callback.onFinish();
                super.onFinish();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(requestParams == null) return null;
                String key;
                final Set data = requestParams.entrySet();
                final Iterator<String> keys = data.iterator();
                final Map<String,String> params = new HashMap<>(requestParams.size());
                while (keys.hasNext()) {
                    key = keys.next();
                    params.put(key,String.valueOf(requestParams.get(key)));
                }
                return params;
            }
        };
        RequestManager.instance().addRequest(request,url);
    }

    /**
     * GET请求,请求结果返回JsonArray类型
     * @param url 请求地址
     * @param callback 请求回调接口
     */
    public static void getJsonArray(String url, final IRequestCallback<JSONArray> callback) {
        getJsonArray(url,null,callback);
    }

    /**
     * GET请求,请求结果返回JsonArray类型
     * @param url 请求地址
     * @param requestParams 请求参数
     * @param callback 请求回调接口
     */
    public static void getJsonArray(String url, final Map<String,Object> requestParams, final IRequestCallback<JSONArray> callback) {
        callback.onBefore();
        final Request request = new JsonArrayRequest(url,callback,callback) {
            @Override
            protected void onFinish() {
                callback.onFinish();
                super.onFinish();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(requestParams == null) return null;
                String key;
                final Set data = requestParams.entrySet();
                final Iterator<String> keys = data.iterator();
                final Map<String,String> params = new HashMap<>(requestParams.size());
                while (keys.hasNext()) {
                    key = keys.next();
                    params.put(key,String.valueOf(requestParams.get(key)));
                }
                return params;
            }
        };
        RequestManager.instance().addRequest(request,url);
    }

    /**
     * GET请求,请求结果返回String类型
     * @param url 请求地址
     * @param callback 请求回调接口
     */
    public static void getString(String url, final IRequestCallback<String> callback) {
        getString(url,null,callback);
    }

    /**
     * GET请求,请求结果返回String类型
     * @param url 请求地址
     * @param requestParams 请求参数
     * @param callback 请求回调接口
     */
    public static void getString(String url, final Map<String,Object> requestParams, final IRequestCallback<String> callback) {
        callback.onBefore();
        final Request request = new StringRequest(url,callback,callback) {
            @Override
            protected void onFinish() {
                callback.onFinish();
                super.onFinish();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(requestParams == null) return null;
                String key;
                final Set data = requestParams.entrySet();
                final Iterator<String> keys = data.iterator();
                final Map<String,String> params = new HashMap<>(requestParams.size());
                while (keys.hasNext()) {
                    key = keys.next();
                    params.put(key,String.valueOf(requestParams.get(key)));
                }
                return params;
            }
        };
        RequestManager.instance().addRequest(request,url);
    }

    /**
     * POST请求,请求结果返回JsonObject类型
     * @param url 请求地址
     * @param jsonParam 请求json参数
     * @param callback 接口回调
     */
    public static void postJsonObject(String url, JSONObject jsonParam, final IRequestCallback<JSONObject> callback) {
        callback.onBefore();
        final Request request = new JsonObjectRequest(url,jsonParam,callback,callback) {
            @Override
            protected void onFinish() {
                callback.onFinish();
                super.onFinish();
            }
        };
        RequestManager.instance().addRequest(request,url);
    }

    /**
     * POST请求,请求结果返回JsonArray类型
     * @param url 请求地址
     * @param jsonParam 请求json参数
     * @param callback 接口回调
     */
    public static void postJsonArray(String url, JSONObject jsonParam, final IRequestCallback<JSONArray> callback) {
        callback.onBefore();
        final Request request = new JsonArrayRequest(url,jsonParam,callback,callback) {
            @Override
            protected void onFinish() {
                callback.onFinish();
                super.onFinish();
            }
        };
        RequestManager.instance().addRequest(request,url);
    }

    /**
     * POST请求,请求结果返回String类型
     * @param url 请求地址
     * @param jsonParam json参数
     * @param callback 接口回调
     */
    public static void postString(String url, final JSONObject jsonParam, final IRequestCallback<String> callback) {
        callback.onBefore();
        final Request request = new StringRequest(Request.Method.POST,url,callback,callback) {
            @Override
            protected void onFinish() {
                callback.onFinish();
                super.onFinish();
            }

            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=%s", new Object[]{"utf-8"});
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonParam.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException var2) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", new Object[]{jsonParam.toString(), "utf-8"});
                    return null;
                }
            }
        };
        RequestManager.instance().addRequest(request,url);
    }

    /**
     * 下载文件
     * @param url 下载地址
     * @param fileName 文件名
     * @param callback 接口回调
     */
    public static void downloadFile(String url, String fileName, IDownloadCallback callback) {
        final Download download = new Download();
        download.download(url,fileName,callback);
    }

    /**
     * 上传文件
     * @param url 路径
     * @param targetFile 目标文件
     * @param callback 上传回调接口
     */
    public static void uploadFile(String url, File targetFile, IUploadCallback callback) {
        final Upload uploadFile = new Upload();
        uploadFile.upload(url, targetFile, callback);
    }

    /**
     * 上传文件
     * @param url 路径
     * @param uploadParams 上传参数
     * @param callback 上传回调接口
     */
    public static void uploadFile(String url, Map<String,Object> uploadParams, IUploadCallback callback) {
        final Upload uploadFile = new Upload();
        uploadFile.upload(url, uploadParams, callback);
    }

    /**
     * 根据tag值来取消某个http请求
     * @param tag tag标签
     */
    public static void cancelRequest(Object tag) {
        RequestManager.instance().cancelRequest(tag);
    }

    /**
     * 图片导图器
     * @param targetView 目标视图
     * @param url 下载地址
     * @param defaultResId 默认图片
     */
    public static void imageLoader(ImageView targetView, String url,int defaultResId) {
        RequestManager.instance().imageLoader(targetView,url,defaultResId);
    }
}
