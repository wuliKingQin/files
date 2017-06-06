package com.tv.filemanager.other;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：异步读取MimeType值
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/6- 11:20
 */

public class ReadMimeTask extends AbsAsyncTask<Map<String,String>> {

    //运行环境
    private Context mContext;

    public ReadMimeTask(Context context) {
        mContext = context;
    }

    @Override
    protected Map<String, String> doInBackground(Object... params) {
        return readTextFromAssets();
    }

    /**
     * 读取内容
     * @return 返回数据map
     * @throws Exception
     */
    private Map<String,String> readTextFromAssets() {
        final Map<String,String> mimeTypes = new HashMap<>();
        try {
            String str;
            String[] types;
            final InputStream is = mContext.getAssets().open("mime_type.txt");
            final InputStreamReader reader = new InputStreamReader(is);
            final BufferedReader bufferedReader = new BufferedReader(reader);
            while ((str = bufferedReader.readLine()) != null) {
                types = str.split(" ");
                if(types.length > 1) {
                    mimeTypes.put(types[0],types[1]);
                }
            }
        } catch (Exception e) {
        }
        return mimeTypes;
    }
}
