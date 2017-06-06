package com.tv.filemanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.Sort;
import com.tv.filemanager.other.CopyFileTask;
import com.tv.filemanager.other.FileTypeMgr;
import com.tv.filemanager.other.MimeType;
import com.tv.filemanager.other.SortComparator;
import com.tv.framework.utils.ProgressUtil;
import com.tv.framework.utils.ToastUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：文件操作工具类
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/3- 17:28
 */

public class FileUtil {

    /**
     * 获取文件后缀名
     * @param fileName 文件名
     * @return 后缀字符串
     */
    public static String getSuffixName(String fileName) {
        final int index = fileName.lastIndexOf(".") + 1;
        try {
            return fileName.substring(index);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取文件类型
     * @param fileName 文件名
     * @return 返回文件类型
     */
    public static CFile.Type getType(String fileName){
        final String suffixName = getSuffixName(fileName);
        return FileTypeMgr.instance().getType(suffixName);
    }

    /**
     * 获取文件图标
     * @param fileName 文件名
     * @return 图标资源Id
     */
    public static int getIcon(String fileName) {
        final String suffixName = getSuffixName(fileName);
        return FileTypeMgr.instance().getIcon(suffixName);
    }

    /**
     * 获取排序类型
     * @param sort 排序类型
     * @return Comparator实例
     */
    public static Comparator getSortType(Sort sort) {
       return new SortComparator(sort);
    }

    /**
     * 获取文件的子文件个数，除掉了隐藏文件
     * @param file 文件
     * @return 子文件个数
     */
    public static int getFileChildCount(File file) {
        int count = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null) {
                for (File f : files) {
                    if (f.isHidden()) continue;
                    count ++ ;
                }
            }
        }
        return count;
    }

    /**
     * 开发任意文件
     * @param context 运行环境
     * @param path 路径
     */
    public static void openFileIntent(Context context, String  path) {
        final File file = new File(path);
        if(!file.exists()) {
            ToastUtil.toast(context, R.string.open_file_fail);
            return;
        }
        final String suffixName = getSuffixName(file.getName());
        final String mimeType = MimeType.get().getMimeType(suffixName);
        final boolean isOpen = isOpenFile(context,file,mimeType);
        if(!isOpen) {
            ToastUtil.toast(context,R.string.can_not_open_file);
            return;
        }
        openApplicationIntent(context,file,mimeType);
    }

    /**
     * 判断文件是否能正常打开
     * @param context 运行环境
     * @param file 需要打开的文件
     * @param mimeType 文件类型
     * @return true表示能打开，false表示不能打开
     */
    public static boolean isOpenFile(Context context,File file, String mimeType) {
        final Intent mIntent = new Intent(Intent.ACTION_VIEW);
        mIntent.setDataAndType(Uri.fromFile(file), mimeType);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(mIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null;
    }

    /**
     * 打开所有能打开应用资源
     * @param context 运行环境
     * @param file 目标文件
     */
    public static void openApplicationIntent(Context context , File file,String mimeType){
        Uri path = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(path, mimeType);
        context.startActivity(intent);
    }

    /**
     * 发送文件给第三方app
     * @param context 运行环境
     * @param file 目标文件
     */
    public static void sendFile(Context context , File file ){
        final Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(file));
        share.setType("*/*");//此处可发送多种文件
        context.startActivity(Intent.createChooser(share, "Share"));
    }

    /**
     * 根据图片路径获取图片的缩略图
     * @param imagePath 图片路径
     * @param width 宽度
     * @param height 高度
     * @return 缩略图实例
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取视频的缩略图
     * @param videoPath 视频路径
     * @param width 宽度
     * @param height 高度
     * @return 缩略图实例
     */
    public static Bitmap getVideoThumbnail(String videoPath,int width,int height) {
        Bitmap bitmap;
        //参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
        //其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
                MediaStore.Video.Thumbnails.MICRO_KIND);
        if(bitmap != null){  //如果视频已损坏或者格式不支持可能返回null
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
