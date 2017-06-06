package com.tv.filemanager.other;

import com.tv.filemanager.bean.Sort;
import com.tv.framework.adapter.AbsBaseAdapter;
import com.tv.framework.utils.ProgressUtil;
import com.tv.framework.utils.ToastUtil;

import java.util.Collections;

/**
 * 功能描述：排序异步任务
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/5- 9:32
 */

public class SortTask extends AbsAsyncTask {

    //选择的排序内容
    private Sort mSort;
    //保存适配器
    private AbsBaseAdapter mAdapter;

    public SortTask(AbsBaseAdapter adapter, Sort sort) {
        mSort = sort;
        mAdapter = adapter;
        ProgressUtil.showDialog(adapter.getContext(), "排序中……",true,null);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Collections.sort(mAdapter.getData(), new SortComparator(mSort));
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        ProgressUtil.dismissDialog();
        mAdapter.notifyDataSetChanged();
    }
}
