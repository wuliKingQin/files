package com.tv.framework.http;

import com.android.volley.Response;

/**
 * 描述：请求回调接口
 * 创建作者：黎丝军
 * 创建时间：2016/12/22 8:48
 */

public interface IRequestCallback<T> extends Response.Listener<T>,Response.ErrorListener{
    /**
     * 请求开始前，该方法一般被用来执行弹出框之类的
     */
    void onBefore();

    /**
     * 结束请求,该方法一般被用来执行弹出框消失之类的
     */
    void onFinish();
}
