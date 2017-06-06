package com.tv.framework.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 功能描述：资源处理器
 * 开发状况：目前只写了获取颜色、字符串和图片的方法，后续在补充
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 14:06
 */
public class ResUtil {

    //保存全局运行环境
    private static Context mContext;
    //保存全局资源实例
    private static Resources mResources;
    //layout资源类型
    public static final String RES_TYPE_LAYOUT = "layout";
    //drawable资源类型
    public static final String RES_TYPE_DRAWABLE = "drawable";
    //mipmap资源类型
    public static final String RES_YTPE_MIPMAP = "mipmap";
    //字符串资源类型
    public static final String RES_TYPE_STRING = "string";
    //颜色资源类型
    public static final String RES_TYPE_COLOR = "color";
    //id资源类型
    public static final String RES_TYPE_ID = "id";
    //style资源类型
    public static final String RES_TYPE_STYLE = "style";

    private ResUtil() {
    }

    /**
     * 初始化全局资源实例
     * @param context 运行环境
     */
    public static void initResource(Context context) {
        mContext = context;
        mResources = context.getResources();
    }

    /**
     * 获取资源颜色值
     * @param resId 资源id
     * @return 颜色值
     */
    public static int getColor(int resId) {
        return mResources.getColor(resId);
    }

    /**
     * 获取资源字符串
     * @param resId 资源Id
     * @return 字符串
     */
    public static String getString(int resId) {
        return mResources.getString(resId);
    }

    /**
     * 获取资源图片
     * @param resId 资源Id
     * @return 图片
     */
    public static Drawable getDrawable(int resId) {
        return mResources.getDrawable(resId);
    }

    /**
     * 获取mipmap的资源Id
     * @param name 资源名
     * @return 资源Id
     */
    public static int getMipmapResId(String name) {
        return getResourceID(RES_YTPE_MIPMAP,name);
    }

    /**
     * 获取layout的资源Id
     * @param name 资源名
     * @return 资源Id
     */
    public static int getLayoutResId(String name) {
        return getResourceID(RES_TYPE_LAYOUT, name);
    }

    /**
     * 获取drawable的资源Id
     * @param name 资源名
     * @return 资源Id
     */
    public static int getDrawableResId(String name) {
        return getResourceID(RES_TYPE_DRAWABLE, name);
    }

    /**
     * 获取String的资源Id
     * @param name 资源名
     * @return 资源Id
     */
    public static int getStringResId(String name) {
        return getResourceID(RES_TYPE_STRING, name);
    }

    /**
     * 获取Color的资源Id
     * @param name 资源名
     * @return 资源Id
     */
    public static int getColorResId(String name) {
        return getResourceID(RES_TYPE_COLOR, name);
    }

    /**
     * 获取Id的资源Id
     * @param name 资源名
     * @return 资源Id
     */
    public static int getIdResId(String name) {
        return getResourceID(RES_TYPE_ID, name);
    }

    /**
     * 获取Style的资源Id
     * @param name 资源名
     * @return 资源Id
     */
    public static int getStyleResId(String name) {
        return getResourceID(RES_TYPE_STYLE, name);
    }

    /**
     * 获取Resource资源Id号
     * @param type 类型
     * @param name 文件名
     * @return 资源Id
     */
    public static int getResourceID(String type, String name) {
        String pkgName = mContext.getPackageName();
        return mResources.getIdentifier(name, type, pkgName);
    }

    /**
     * 获取渐变drawable
     * @return Drawable
     */
    public static Drawable getShadeDrawable(int color) {
        final Drawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, new int[] {
                Color.TRANSPARENT,
                color
        });
        return drawable;
    }

    /**
     * 通过Id获取视图实例对象
     * @param context 运行时
     * @param resId   资源Id
     * @return View
     */
    public static <T extends View> T findViewById(Context context, int resId) {
        return (T) LayoutInflater.from(context).inflate(resId, null);
    }
}
