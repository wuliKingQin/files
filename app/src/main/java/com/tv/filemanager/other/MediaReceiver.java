package com.tv.filemanager.other;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.pgyersdk.crash.PgyCrashManager;
import com.tv.filemanager.R;
import com.tv.filemanager.activity.MainActivity;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.utils.SettingUtil;
import com.tv.framework.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 功能描述：媒体接收器
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/24- 14:36
 */

public class MediaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        final CFile file = new CFile();
        if(Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
            try {
                if(SettingUtil.getUsbSetting(context)) {
                    final String usbPath = intent.getData().getPath();
                    if(usbPath != null) {
                        if(isForeground(context,MainActivity.class)) {
                            file.setPath(usbPath);
                            EventBus.getDefault().post(file);
                        } else {
                            final Intent activity = new Intent(context,MainActivity.class);
                            activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.putExtra("usbPath",usbPath);
                            context.startActivity(activity);
                        }
                        ToastUtil.toast(context, R.string.out_disk_insert);
                    }
                }
            } catch (Exception e) {
                PgyCrashManager.reportCaughtException(context,e);
            }
        } else {
            if(SettingUtil.getUsbSetting()) {
                EventBus.getDefault().post(file);
            }
            ToastUtil.toast(context, R.string.out_disk_pull_out);
        }
    }

    /**
     * 判断每个界面是否在前台
     * @param activity 运行环境
     * @param cl 需要判断的界面
     * @return true表示在，false表示不在
     */
    public boolean isForeground(Context activity,Class<?> cl) {
        return isForeground(activity, cl.getName());
    }

    /**
     * 判断某个界面是否在前台
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
