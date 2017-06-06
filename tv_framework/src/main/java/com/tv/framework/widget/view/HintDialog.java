package com.tv.framework.widget.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.tv.framework.R;

/**
 * 描述：提示输入框或者只是提示框
 * 创建作者：黎丝军
 * 创建时间：2017/1/9 10:36
 */

public class HintDialog extends Dialog {

    //标题
    private TextView mTitleTv;
    //内容
    private TextView mContentTv;
    //输入空
    private EditText mInputEdt;
    //取消
    private TextView mCancelBtn;
    //确认
    private TextView mSureBtn;
    //取消和确认的分割线
    private View mGapLineView;
    //点击确认按钮回调接口
    private IOkCallback mCallback;
    //点击dialog接口回调
    private IDialogCallback mDialogCallback;

    public  HintDialog(Context context) {
        super(context);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.dialog_hint_view);
        mTitleTv = (TextView) findViewById(R.id.tv_dialog_title);
        mContentTv = (TextView) findViewById(R.id.tv_dialog_content);
        mInputEdt = (EditText) findViewById(R.id.edt_dialog_input);
        mCancelBtn = (TextView) findViewById(R.id.btn_dialog_cancel);
        mSureBtn = (TextView) findViewById(R.id.btn_dialog_sure);
        mGapLineView = findViewById(R.id.v_dialog_line);

        mSureBtn.setOnClickListener(mClickListener);
        mCancelBtn.setOnClickListener(mClickListener);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        getWindow().setBackgroundDrawableResource(R.drawable.shape_background_dialog);
    }

    /**
     * 获取输入框文本值
     * @return 文本
     */
    public String getInputText() {
        return mInputEdt.getText().toString().trim();
    }

    /**
     * 设置输入类型
     * @param inputType 类型输入设置
     */
    public void setInputType(int inputType) {
        mInputEdt.setInputType(inputType);
    }

    /**
     * 设置输入的最大长度
     * @param maxLength 最大长度
     */
    public void setInputMaxLength(int maxLength) {
        mInputEdt.setMaxEms(maxLength);
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    /**
     * 设置标题颜色
     * @param color 颜色值
     */
    public void setTitleColor(int color) {
        mTitleTv.setTextColor(color);
    }

    /**
     * 设置提示内容文本
     * @param text 文本
     */
    public void setHintText(String text) {
        mContentTv.setText(text);
    }

    /**
     * 设置文本显示位置
     * @param gravity 位置
     */
    public void setHintTextGravity(int gravity) {
        mContentTv.setGravity(gravity);
    }

    /**
     * 设置取消按钮文本和文本颜色
     * @param text 文本值
     * @param color 文本颜色
     */
    public void setCancelTextAndColor(String text,int color) {
        if(!TextUtils.isEmpty(text)) {
            mCancelBtn.setText(text);
        }
        mCancelBtn.setTextColor(color);
    }

    /**
     * 设置Ok文本和文本颜色
     * @param text 文本值
     * @param color 文本颜色
     */
    public void setOkTextAndColor(String text,int color) {
        if(!TextUtils.isEmpty(text)) {
            mSureBtn.setText(text);
        }
        mSureBtn.setTextColor(color);
    }

    /**
     * 设置Ok按钮是否可见
     * @param visible 是否可见值
     */
    public void setOkVisible(int visible) {
        if(visible == View.GONE) {
            mGapLineView.setVisibility(visible);
        }
        mSureBtn.setVisibility(visible);
    }

    /**
     * 设置取消按钮是否可见
     * @param visible 是否可见值
     */
    public void setCancelVisible(int visible) {
        if(visible == View.GONE) {
            mGapLineView.setVisibility(visible);
        }
        mCancelBtn.setVisibility(visible);
    }

    /**
     * 设置显示内容是提示框还是输入框
     * @param isHint true表示提示框，false表示输入框
     */
    public void setInputOrHint(boolean isHint) {
        if(isHint) {
            setHintVisible(View.VISIBLE);
            setInputVisible(View.GONE);
        } else {
            setHintVisible(View.GONE);
            setInputVisible(View.VISIBLE);
        }
    }

    /**
     * 设置输入框是否显示
     * @param visible 显示
     */
    public void setInputVisible(int visible) {
        mInputEdt.setVisibility(visible);
    }

    /**
     * 设置提示内容视图是否显示
     * @param visible 显示
     */
    public void setHintVisible(int visible) {
        mContentTv.setVisibility(visible);
    }

    /**
     * 设置输入框的值
     * @param text 文本
     */
    public void setInputText(String text) {
        mInputEdt.setText(text);
    }

    /**
     * 设置提示文本信息
     * @param text 文本
     */
    public void setInputHintText(String text) {
        mInputEdt.setHint(text);
    }

    /**
     * 设置点击确认按钮时的接口回调
     * @param callback 接口回调实例
     */
    public void setIOkCallback(IOkCallback callback) {
        mCallback = callback;
    }

    /**
     * 设置Dialog回调接口
     * @param callback 回调接口
     */
    public void setDialogCallback(IDialogCallback callback) {
        mDialogCallback = callback;
    }

    /**
     * 监听事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_dialog_sure) {
                if(mCallback != null) {
                    mCallback.onOk(mInputEdt.getText().toString().trim());
                }
                if(mDialogCallback != null) {
                    mDialogCallback.onOk(mInputEdt.getText().toString().trim());
                }
            } else {
                if(mDialogCallback != null) {
                    mDialogCallback.onCancel();
                }
            }
            dismiss();
        }
    };

    /**
     * 点击确认是的回调接口
     */
    public interface IOkCallback{
        /**
         * 点击确认执行方法
         * @param text 文本
         */
        void onOk(String text);
    }

    /**
     * dialog点击回调接口
     */
    public interface IDialogCallback extends IOkCallback {
        /**
         * 点击取消Dialog
         */
        void onCancel();
    }
}
