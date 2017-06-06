package com.tv.filemanager;

import android.app.Application;
import android.content.Intent;

import com.pgyersdk.crash.PgyCrashManager;
import com.tv.filemanager.other.FileService;
import com.tv.filemanager.other.FileTypeMgr;
import com.tv.filemanager.other.MimeType;
import com.tv.framework.AppHelper;

/**
 * 功能描述：文件app
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/17- 14:04
 */

public class FileApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MimeType.get().readData(this);
        PgyCrashManager.register(this);
        AppHelper.instance().initCoreApp(this);
        FileTypeMgr.instance().initFileType(this);
        startService(new Intent(this, FileService.class));
    }

}
