package com.tv.filemanager.other;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：文件类型管理器
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/17- 18:32
 */
public class FileTypeMgr {

    //视频后缀
    private String[] mVideoSuffix;
    //音乐后缀
    private String[] mMusicSuffix;
    //图片后缀
    private String[] mPictureSuffix;
    //保存类型数据
    private Map<String,SuffixType> mTypes = new HashMap<>();
    //保存实例
    private static FileTypeMgr ourInstance = new FileTypeMgr();
    //获取实例
    public static FileTypeMgr instance() {
        return ourInstance;
    }

    private FileTypeMgr() {
    }

    /**
     * 初始化文件类型数据
     * @param context 运行环境
     */
    public void initFileType(Context context) {
        putSuffix("txt",R.mipmap.ic_txt, CFile.Type.TXT);
        putSuffix("zip",R.mipmap.ic_zip, CFile.Type.ZIP);
        putSuffix("apk",R.mipmap.ic_apk, CFile.Type.APK);
        putSuffix("png",R.mipmap.ic_png, CFile.Type.PNG);
        putSuffix("jpg",R.mipmap.ic_jpg, CFile.Type.JPG);
        putSuffix("gif",R.mipmap.ic_gif, CFile.Type.GIF);
        putSuffix("mp3",R.mipmap.ic_mp3, CFile.Type.MP3);
        putSuffix("mp4",R.mipmap.ic_mp4, CFile.Type.MP4);
        putSuffix("aac",R.mipmap.ic_aac, CFile.Type.AAC);
        putSuffix("pdf",R.mipmap.ic_pdf, CFile.Type.PDF);
        putSuffix("avi",R.mipmap.ic_avi, CFile.Type.AVI);
        putSuffix("bt",R.mipmap.ic_bt, CFile.Type.BT);
        putSuffix("xls",R.mipmap.ic__excel, CFile.Type.EXCEL);
        putSuffix("mid",R.mipmap.ic_mid, CFile.Type.MID);
        putSuffix("mkv",R.mipmap.ic_mkv, CFile.Type.MKV);
        putSuffix("flv",R.mipmap.ic_flv, CFile.Type.FLV);
        putSuffix("flac",R.mipmap.ic_flac, CFile.Type.FLAC);
        putSuffix("rmvb",R.mipmap.ic_rmvb, CFile.Type.RMVB);
        putSuffix("rar",R.mipmap.ic_rar, CFile.Type.RAR);
        putSuffix("wmw",R.mipmap.ic_wmw, CFile.Type.WMW);
        putSuffix("wma",R.mipmap.ic_wma, CFile.Type.WMA);
        putSuffix("wav",R.mipmap.ic_wav, CFile.Type.WAV);
        putSuffix("doc",R.mipmap.ic_doc, CFile.Type.WORD);
        putSuffix("ppt",R.mipmap.ic_ppt, CFile.Type.PPT);
        mVideoSuffix = context.getResources().getStringArray(R.array.video_suffix);
        mMusicSuffix = context.getResources().getStringArray(R.array.music_suffix);
        mPictureSuffix = context.getResources().getStringArray(R.array.picture_suffix);
    }

    /**
     * 通过名字获取相应的文件类型
     * @param suffixName 后缀名
     * @return 类型值
     */
    public CFile.Type getType(String suffixName) {
        if(mTypes.containsKey(suffixName)) {
            return mTypes.get(suffixName).type;
        }
        return CFile.Type.OTHER;
    }

    /**
     * 通过后缀名获取对应图标资源Id
     * @param suffixName 后缀名
     * @return 资源Id
     */
    public int getIcon(String suffixName) {
        if(mTypes.containsKey(suffixName)) {
            return mTypes.get(suffixName).icon;
        }
        return R.mipmap.ic_other;
    }

    /**
     * 判断是否是视频文件
     * @param suffixName 后缀名
     * @return true表示是，否则表示不是
     */
    public boolean isVideoFile(String suffixName) {
        return isAssignSuffix(mVideoSuffix,suffixName);
    }

    /**
     * 判断是否是音频文件
     * @param suffixName 后缀名
     * @return true表示是，否则表示不是
     */
    public boolean isMusicFile(String suffixName) {
        return isAssignSuffix(mMusicSuffix,suffixName);
    }

    /**
     * 判断是否是图片文件
     * @param suffixName 后缀名
     * @return true表示是，否则表示不是
     */
    public boolean isPictureFile(String suffixName) {
        return isAssignSuffix(mPictureSuffix,suffixName);
    }

    /**
     * 是否时办公类文件
     * @param suffixName 后缀名
     * @return true表示是，否则表示不是
     */
    public boolean isOfficeFile(String suffixName) {
        final String[] officeFiles = {"doc","ppt","exl","pdf"};
        return isAssignSuffix(officeFiles,suffixName);
    }

    /**
     * 判断是否是指定的文件格式
     * @param assignSuffixs 指定的文件后缀名
     * @return true表示存在，false表示不存在
     */
    private boolean isAssignSuffix(String[] assignSuffixs, String suffixName) {
        for(String suffix:assignSuffixs) {
            if(suffixName.contains(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加后缀对应的所有数据
     * @param suffix 后缀名
     * @param iconId 图标资源Id
     * @param type 后缀类型
     */
    private void putSuffix(String suffix,int iconId, CFile.Type type) {
        mTypes.put(suffix,new SuffixType(iconId,type));
    }

    /**
     * 后缀类型
     */
    class SuffixType {

        //类型图标
        int icon;
        //类型实例
        CFile.Type type;

        SuffixType(int icon, CFile.Type type) {
            this.icon = icon;
            this.type = type;
        }

    }
}
