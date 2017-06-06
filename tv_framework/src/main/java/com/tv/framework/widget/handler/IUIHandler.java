package com.tv.framework.widget.handler;

import android.os.Bundle;

/**
 * 功能描述：修改接口在于将fragment和activity加载和初始化视图组件的时候统一
 * 开发状况：已经开发完
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 13:35
 */

public interface IUIHandler {

    /**
     * 创建视图
     */
    void onCreateView();

    /**
     * 初始化new对象
     */
    void onInitObjects();

    /**
     * 为视图注册监听器
     */
    void onSetListeners();

    /**
     * 为视图初始化数据
     * @param savedInstanceState 用于保存状态数据
     */
    void onInitData(Bundle savedInstanceState);

    /**
     * 获取碎片处理器
     * @return 碎片处理器实例
     */
    FragmentHandler getFragmentHandler();
}
