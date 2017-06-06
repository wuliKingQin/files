package com.tv.framework.http;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;

/**
 * 描述：OkHttpStack
 * 创建作者：黎丝军
 * 创建时间：2016/12/23 15:25
 */

public class OkHttpStack extends HurlStack {

    private final OkUrlFactory okUrlFactory;

    public OkHttpStack(OkHttpClient okHttpClient) {
        this(new OkUrlFactory(okHttpClient));
    }

    public OkHttpStack(OkUrlFactory okUrlFactory) {
        if(okUrlFactory == null) {
            throw new NullPointerException("Client must not be null.");
        } else {
            this.okUrlFactory = okUrlFactory;
        }
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        return this.okUrlFactory.open(url);
    }
}
