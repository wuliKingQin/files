package com.tv.framework.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：用于http请求json参数
 * 创建作者：黎丝军
 * 创建时间：2016/12/22 15:08
 */

public class JSONParam extends JSONObject {

    /**
     * 添加请求参数
     * @param key 键
     * @param value 值
     */
    public void putParam(String key,Object value) {
        try {
            put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加请求参数，该方法可以连续添加
     * @param key 键
     * @param value 值
     * @return JSONObject实例
     */
    public JSONParam putJsonParam(String key,Object value) {
        try {
            return (JSONParam)put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
