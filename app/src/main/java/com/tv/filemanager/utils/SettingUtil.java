package com.tv.filemanager.utils;

import android.content.Context;

import com.tv.filemanager.other.MediaReceiver;
import com.tv.framework.utils.PreferencesUtil;

/**
 * 功能描述：设置工具类
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/29- 11:09
 */

public class SettingUtil {

    //保存是否能安装apk
    private static boolean mInstallApk = false;

    private SettingUtil() {
    }

    /**
     * 获取图片的设置
     * @return true表示低于10k的照片不显示，否则就相反
     */
    public static boolean getPicSetting() {
        return PreferencesUtil.getBoolean("settingPic", false);
    }

    /**
     * 获取USB的设置
     * @return true表示插入U盘自动打开，false表示不打开
     */
    public static boolean getUsbSetting(Context context) {
        PreferencesUtil.initContext(context);
        return PreferencesUtil.getBoolean("settingUsb", true);
    }

    /**
     * 设置usb是否插入
     * @param context 运行环境
     * @param isAttached true表示插入，false表示拔出
     */
    public static void setUsbAttached(Context context, boolean isAttached) {
        PreferencesUtil.initContext(context);
        PreferencesUtil.putBoolean("isUsbAttached", isAttached);
    }

    /**
     * 获取USB的设置
     * @return true表示插入U盘自动打开，false表示不打开
     */
    public static boolean getUsbSetting() {
        return PreferencesUtil.getBoolean("settingUsb",true);
    }

    /**
     * 设置是否能安装apk
     * @param isInstallApk true表示能，false表示不能
     */
    public static void setInstallAkp(boolean isInstallApk) {
        mInstallApk = isInstallApk;
    }

    /**
     * 是否能安装apk
     * @return rue表示能，false表示不能
     */
    public static boolean isInstallApk() {
        return mInstallApk;
    }
}
