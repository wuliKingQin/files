package com.tv.filemanager.other;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：Mime类型集合类，
 *           用于判断文件是否能正常打开
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/6- 10:56
 */

public class MimeType {

    //保存单列实例
    private static MimeType mInstance = new MimeType();
    //用于保存文件后缀对应的mime类型
    private Map<String,String> mimeTypes = new HashMap<>();

    private MimeType() {
    }

    /**
     * 获取实例
     * @return Mimeype实例
     */
    public static MimeType get() {
        return mInstance;
    }

    /**
     * 异步读取数据
     * @param context 运行环境
     */
    public void readData(Context context) {
        final ReadMimeTask task = new ReadMimeTask(context) {
            @Override
            protected void onPostExecute(Map<String, String> stringStringMap) {
                mimeTypes.clear();
                mimeTypes.putAll(stringStringMap);
            }
        };
        task.startTask();
    }

    /**
     * 根据后缀名获取对应的mime值
     * @param suffixName 后缀名
     * @return mime值
     */
    public String getMimeType(String suffixName) {
        final String mimeType = mimeTypes.get(suffixName);
        if(mimeType != null) {
            return mimeType;
        }
        return "application/*";
    }
}
