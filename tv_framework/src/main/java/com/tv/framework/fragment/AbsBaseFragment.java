package com.tv.framework.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tv.framework.widget.handler.FragmentHandler;
import com.tv.framework.widget.handler.IUIHandler;

/**
 * 功能描述：基本碎片Fragment,该类是抽象类
 *          该碎片抽象出了一个onCreateUI()方法，用来初始化碎片UI控件布局
 *          设置布局时，调用setContentView()方法，和你在Activity里使用一样。
 * 开发状况：基本开发完毕
 * 开发作者：黎丝军
 * 开发时间：2017/3/1- 14:53
 */

public abstract class AbsBaseFragment extends Fragment
        implements IUIHandler {

    //保存跟布局
    private FrameLayout mRootContainer;
    //获取布局实例的句柄
    private LayoutInflater mLayoutInflater;
    //保存碎片处理器的实例
    private FragmentHandler mFragmentHandler;

    /**
     * 在后面子类中必须使用onCreateUi方法来替代,
     * 否则运行或许会出错
     */
    @Deprecated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootContainer == null) {
            initBaseLayout(inflater);
            onCreateView();
            onInitObjects();
            onSetListeners();
            onInitData(savedInstanceState);
        } else {
            removeView(mRootContainer);
        }
        return mRootContainer;
    }

    /**
     * 初始化基本布局，带有自定义头部导航栏
     */
    private void initBaseLayout(LayoutInflater inflater) {
        mLayoutInflater = inflater;
        mFragmentHandler = new FragmentHandler(getFragmentManager());
        mRootContainer = new FrameLayout(getContext());
        mRootContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 通过Id设置Fragment的内容视图
     * @param id 布局Id
     */
    protected void setContentView(int id) {
        final View view = mLayoutInflater.inflate(id,null);
        mRootContainer.addView(view);
    }

    /**
     * 设置碎片内容布局视图
     * @param view 需要设置的视图
     */
    protected void setContentView(View view) {
        mRootContainer.addView(view);
    }

    /**
     * 设置碎片内容布局视图
     * @param view 需要设置的视图
     * @param layoutParams 布局参数
     */
    protected void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        mRootContainer.addView(view,layoutParams);
    }

    /**
     * 根据id获取对应的视图
     * @param resId 资源Id
     * @param <T> 泛型实例
     * @return 返回对应的视图实例
     */
    public <T extends View> T findViewById(int resId) {
        return (T) mRootContainer.findViewById(resId);
    }

    /**
     * 从父视图中移除该视图
     * @param view 需要移除View
     */
    private void removeView(View view) {
        final ViewGroup viewGroup = (ViewGroup) view.getParent();
        if(viewGroup != null) {
            viewGroup.removeView(view);
        }
    }

    public FragmentHandler getFragmentHandler() {
        return mFragmentHandler;
    }
}
