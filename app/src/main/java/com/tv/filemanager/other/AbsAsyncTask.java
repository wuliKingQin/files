package com.tv.filemanager.other;

import android.os.AsyncTask;

/**
 * 功能描述：抽象异步任务执行类，该类继承AsyncTask
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/6- 13:55
 */

public abstract class AbsAsyncTask<T> extends AsyncTask<Object,Integer,T> {

    /**
     * 开始任务
     */
    public void startTask() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , "");
    }
}
