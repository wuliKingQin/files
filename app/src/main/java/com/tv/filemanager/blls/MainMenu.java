package com.tv.filemanager.blls;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.fragment.AbsCommonFragment;
import com.tv.framework.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 功能描述：处理一级菜单
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/16- 13:48
 */
public class MainMenu {

    //保存菜单字体颜色
    private int mTextColor;
    //保存焦点时的DrawableId
    private int mFocusDrawableId;
    //保存菜单底部DrawableId
    private int mBottomDrawableId;
    //保存菜单默认的字体颜色
    private int mDefaultTextColor;
    //保存当前被点击的菜单视图Id
    private int mCurrentClickMenuId;
    //保存菜单视图
    private List<Menu> mMenuViews;

    public MainMenu() {
        mTextColor = Color.WHITE;
        mMenuViews = new ArrayList<>();
        mDefaultTextColor = Color.WHITE;
        mBottomDrawableId = R.mipmap.ic_underline;
        mFocusDrawableId = R.mipmap.ic_underline_focus;
    }

    /**
     * 添加菜单视图
     * @param menuView 菜单视图
     * @param fragment 菜单对应的碎片
     */
    public void putMenuView(TextView menuView,AbsCommonFragment fragment) {
        mMenuViews.add(new Menu(menuView,fragment));
    }

    /**
     * 设置菜单被选中的改变
     * @param menuViewId 菜单Id
     */
    public void setMenuChange(int menuViewId) {
        setMenuChange(menuViewId,null);
    }

    /**
     * 设置菜单被选中的改变
     * @param menuName 菜单名
     */
    public void setMenuChange(String menuName,IChangeCallback changeCallback) {
        final Menu menu = findMenuByName(menuName);
        if(menu != null) {
            setMenuChange(menu.getId(),changeCallback);
        }
    }

    /**
     * 设置菜单被选中的改变
     * @param menuViewId 菜单Id
     */
    public void setMenuChange(int menuViewId, IChangeCallback changeCallback) {
        for(Menu menu:mMenuViews) {
            if(menu.getId() == menuViewId) {
                if(changeCallback != null) {
                    menu.getMenuView().setTextColor(Color.YELLOW);
                    changeCallback.onChange(menu);
                }
            } else {
                menu.getMenuView().setTextColor(Color.WHITE);
                menu.getMenuView().setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
        }
        mCurrentClickMenuId = menuViewId;
    }

    /**
     * 根据名字获取菜单实例
     * @param name 菜单名
     * @return 菜单实例
     */
    public Menu findMenuByName(String name) {
        for (Menu menu:mMenuViews) {
            if(TextUtils.equals(menu.getName(),name)) {
                return menu;
            }
        }
        return null;
    }

    /**
     * 根据菜单Id获取菜单实例
     * @param menuId 菜单Id
     * @return 菜单实例
     */
    public Menu findMenuById(int menuId) {
        for(Menu menu:mMenuViews) {
            if (menu.getId() == menuId) {
                return menu;
            }
        }
        return null;
    }

    /**
     * 获取当前菜单
     * @return Menu实例
     */
    public Menu getCurrentMenu() {
        return findMenuById(mCurrentClickMenuId);
    }

    /**
     * 清除多选模式
     */
    public boolean clearMuselectModel() {
        for (Menu menu : mMenuViews) {
            if(menu.getFragment().isMuselectModel()) {
                menu.getFragment().setAdapterMuselectModel(false);
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是当前菜单Id
     * @param resId 资源id
     * @return true 表示是 false表示不是
     */
    public boolean isCurrentMenuId(int resId) {
        return mCurrentClickMenuId == resId;
    }

    /**
     * 让当前菜单获取到焦点
     */
    public void currentMenuRequestFocus() {
        final Menu menu = findMenuById(mCurrentClickMenuId);
        if(menu != null) {
            menu.getMenuView().requestFocus();
            menu.getMenuView().requestFocusFromTouch();
        }
    }

    /**
     * 设置失去选中菜单的文本颜色
     * @param color 颜色值
     */
    public void setMenuTextColor(int menuId,int color) {
        for (Menu menu:mMenuViews) {
            if(menu.getId() != menuId && menu.getId() != mCurrentClickMenuId) {
                menu.getMenuView().setTextColor(color);
            }
        }
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getFocusDrawableId() {
        return mFocusDrawableId;
    }

    public void setFocusDrawableId(int focusDrawableId) {
        mFocusDrawableId = focusDrawableId;
    }

    public int getBottomDrawableId() {
        return mBottomDrawableId;
    }

    public void setBottomDrawableId(int bottomDrawableId) {
        mBottomDrawableId = bottomDrawableId;
    }

    public int getDefaultTextColor() {
        return mDefaultTextColor;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        mDefaultTextColor = defaultTextColor;
    }

    public int getCurrentClickMenuId() {
        return mCurrentClickMenuId;
    }

    public void setCurrentClickMenuId(int currentClickMenuId) {
        mCurrentClickMenuId = currentClickMenuId;
    }

    /**
     * 菜单改变接口回调
     */
    public interface IChangeCallback {
        /**
         * 菜单改变回调执行方法
         * @param menu 菜单实例
         */
        void onChange(Menu menu);
    }

    /**
     * 菜单
     */
    public class Menu {
        //菜单id
        private int id;
        //菜单名
        private String name;
        //菜单视图
        private TextView menuView;
        //菜单碎片
        private AbsCommonFragment fragment;

        public Menu(TextView menuView, AbsCommonFragment fragment) {
            this.id = menuView.getId();
            this.menuView = menuView;
            this.fragment = fragment;
            name = menuView.getText().toString().trim();
        }

        public int getId() {
            return id;
        }

        public TextView getMenuView() {
            return menuView;
        }

        public AbsCommonFragment getFragment() {
            return fragment;
        }

        public String getName() {
            return name;
        }
    }
}
