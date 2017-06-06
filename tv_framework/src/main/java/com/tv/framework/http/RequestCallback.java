package com.tv.framework.http;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * 描述：请求回调接口实现
 * 创建作者：黎丝军
 * 创建时间：2016/12/22 9:26
 */

public class RequestCallback<T> implements IRequestCallback<T> {

    @Deprecated
    @Override
    public void onErrorResponse(VolleyError volleyError) {
        final NetworkResponse response = volleyError.networkResponse;
        onFail(response != null ? response.statusCode:-1,volleyError.getMessage());
    }

    @Deprecated
    @Override
    public void onResponse(T responseInfo) {
        onSuccess(responseInfo);
    }

    @Override
    public void onBefore() {
    }

    /**
     * 请求失败
     * @param errorCode 错误代码
     * @param errorInfo 错误信息
     */
    public void onFail(int errorCode,String errorInfo) {
    }

    /**
     * 请求成功
     * @param successInfo 请求成功消息
     */
    public void onSuccess(T successInfo) {
    }

    @Override
    public void onFinish() {
    }
}
