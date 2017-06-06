package com.tv.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能描述：时间工具类
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/27- 13:41
 */

public class TimeUtil {

    //时间格式为HH:mm
    public static final String HH_SS = "HH:mm";
    //时间格式为yyyy-mm-dd
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    //时间格式为yyyy-mm-dd hh:ss
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    private TimeUtil() {
    }

    /**
     * 获取日期字符串
     * @param millisecond 毫秒时间
     * @param dateFormat 日期格式
     * @return 时间字符串
     */
    public static String getDateStr(long millisecond, String dateFormat) {
        final Date date = new Date(millisecond);
        final SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    /**
     * 获取时间字符串
     * @param millisecond 毫秒时间
     * @return 时间字符串
     */
    public static String getTimeStr(long millisecond) {
        return getDateStr(millisecond,HH_SS);
    }

    /**
     * 获取日期字符串
     * @param millisecond 毫秒时间
     * @return 时间字符串
     */
    public static String getDateStr(long millisecond) {
        return getDateStr(millisecond, YYYY_MM_DD_HH_MM);
    }
}
