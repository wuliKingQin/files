package com.tv.framework.widget.handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * 功能描述：碎片处理器
 * 开发状况：已开发完
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 13:11
 */

public class FragmentHandler {

    //保存碎片管理器实例
    private FragmentManager mFragmentMgr;

    public FragmentHandler(FragmentManager fragmentMgr) {
        mFragmentMgr = fragmentMgr;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentMgr;
    }

    /**
     * 根据指定视图容器Id来添加碎片
     * @param containId      视图容器Id
     * @param targetFragment 需要添加的碎片对象
     */
    public void addFragment(int containId, Fragment targetFragment) {
        addFragment(containId, targetFragment, null);
    }

    /**
     * 根据指定视图容器Id来添加碎片
     * @param containId      视图容器Id
     * @param targetFragment 需要添加的碎片对象
     * @param tagName        碎片标签名
     */
    public void addFragment(int containId, Fragment targetFragment, String tagName) {
        if (tagName != null) {
            getFragmentManager().beginTransaction().add(containId, targetFragment, tagName).commit();
        } else {
            getFragmentManager().beginTransaction().add(containId, targetFragment).commit();
        }
    }

    /**
     * 隐藏指定的碎片
     * @param targetFragment 需要隐藏的碎片对象
     */
    public void hideFragment(Fragment targetFragment) {
        getFragmentManager().beginTransaction().hide(targetFragment).commit();
    }

    /**
     * 移除指定碎片
     * @param targetFragment 需要移除的碎片对象
     */
    public void removeFragment(Fragment targetFragment) {
        getFragmentManager().beginTransaction().remove(targetFragment).commit();
    }

    /**
     * 替换某个视图Id对应位置的碎片
     * @param containId      需要替换的视图容器Id
     * @param targetFragment 需要替换的碎片对象
     */
    public void replaceFragment(int containId, Fragment targetFragment) {
        replaceFragment(containId, targetFragment, null);
    }

    /**
     * 显示目标碎片
     * @param targetFragment 目标碎片
     */
    public void showFragment(Fragment targetFragment) {
        getFragmentManager().beginTransaction().show(targetFragment).commit();
    }

    /**
     * 替换某个视图Id对应位置的碎片
     * @param containId      需要替换的视图容器Id
     * @param targetFragment 需要替换的碎片对象
     * @param tagName        碎片标签名
     */
    public void replaceFragment(int containId, Fragment targetFragment, String tagName) {
        if (tagName != null) {
            getFragmentManager().beginTransaction().replace(containId, targetFragment, tagName).commit();
        } else {
            getFragmentManager().beginTransaction().replace(containId, targetFragment).commit();
        }
    }

    /**
     * 设置Fragment进入或退出的方式
     * @param enterResId 进入动画资源Id
     * @param exitResId  退出时需要播放的资源Id
     */
    public void setCustomAnimations(int enterResId, int exitResId) {
        getFragmentManager().beginTransaction().setCustomAnimations(enterResId, exitResId).commit();
    }

    /**
     * 添加碎片，此方法在添加碎片时会把原来的删除
     *
     * @param targetFragment 目标碎片
     */
    public void detach(Fragment targetFragment) {
        getFragmentManager().beginTransaction().detach(targetFragment).commit();
    }
}
