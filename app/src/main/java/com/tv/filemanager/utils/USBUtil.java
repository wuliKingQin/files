package com.tv.filemanager.utils;

import java.io.File;
/**
 * 功能描述：usb工具栏
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/27- 16:33
 */

public class USBUtil {

    //usb路径
    public static String usbPath = null;

    /**
     * 获取扩展存储路径，TF卡、U盘
     * @return 获取挂载路径
     */
    public static String getUsbStorageDir(){
        if(usbPath == null) {
            final File file = new File("/mnt/usb");
            final File[] usbStorage = file.listFiles();
            if(usbStorage != null && usbStorage.length > 0) {
                final File usbFile = usbStorage[0];
                final File[] childFiles = usbFile.listFiles();
                if(usbFile.exists() && childFiles != null && childFiles.length > 0) {
                    usbPath = usbFile.getPath();
                    return usbPath;
                }
            }
        }
        return usbPath;
    }

    /**
     * 设置usb路径
     * @param usbPath 路径
     */
    public static void setUsbPath(String usbPath) {
        USBUtil.usbPath = usbPath;
    }

}
