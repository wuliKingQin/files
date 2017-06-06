package com.tv.framework.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tv.framework.R;

/**
 * 文件描述：任何简单进度弹出框
 * 创建作者：黎丝军
 * 创建时间：16/8/8 PM12:19
 */
public class ProgressUtil {

    //弹出框
    private static Dialog dialog = null;
    //视图
    private static View dialogView = null;

    private ProgressUtil() {
    }

    /**
     * 显示弹出框
     */
    public static void showDialog(Context context) {
        showDialog(context,null,false,0,null);
    }

    /**
     * 显示弹出框
     */
    public static void showDialog(Context context, String info,ICancelCallback callBack) {
        showDialog(context,info,false,0,callBack);
    }

    /**
     * 显示弹出框
     */
    public static void showDialog(Context context, int resId,ICancelCallback callBack) {
        showDialog(context,context.getString(resId),true,0,callBack);
    }

    /**
     * 显示弹出框
     * @param context 运行环境
     * @param cancelable 点击返回是否能被取消
     */
    public static void showDialog(Context context, boolean cancelable,ICancelCallback callBack) {
        showDialog(context,null,cancelable,0,callBack);
    }

    /**
     * 显示弹出框
     * @param message 提示信息
     */
    public static void showDialog(Context context, String message, boolean cancelable,ICancelCallback callBack) {
        showDialog(context,message,cancelable,0,callBack);
    }

    /**
     * 显示弹出框
     * @param message 提示信息
     */
    public static void showDialog(Context context, String message, final boolean cancelable, int resBgId, final ICancelCallback callBack) {
        dialogView = ResUtil.findViewById(context, R.layout.dialog_progress);
        if(resBgId > 0) dialogView.setBackgroundDrawable(context.getResources().getDrawable(resBgId));
        final TextView tvMessage = (TextView) dialogView.findViewById(R.id.tv_dialog_msg);
        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        } else {
        }
        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        if(callBack != null) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        callBack.onCancel();
                        dismissDialog();
                        return true;
                    }
                    return false;
                }
            });
        }
        dialog.show();
        dialog.setContentView(dialogView);
    }

    /**
     * 取消弹出框
     */
    public static void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 取消回调接口
     */
    public interface ICancelCallback {
        /**
         * 取消回调方法
         */
        void onCancel();
    }
}
