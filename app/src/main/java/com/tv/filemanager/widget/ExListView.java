package com.tv.filemanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ExpandableListView;

import com.tv.filemanager.other.CleanManager;

/**
 * 功能描述：扩展列表，多级列表，该列表主要处理按键Ok
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/12- 18:03
 */

public class ExListView extends ExpandableListView {

    //接口回调
    private IClickOkCallback mCallback;

    public ExListView(Context context) {
        this(context, null);
    }

    public ExListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getRepeatCount() == 0 )
                || (keyCode == KeyEvent.KEYCODE_ENTER && event.getRepeatCount() == 0)) {
            if(mCallback != null) {
                mCallback.onClickOk(getSelectedItem());
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * 设置点击Ok键的监听回调
     * @param clickOkCallBack 点击回调实例
     */
    public void setClickOkCallBack(IClickOkCallback clickOkCallBack) {
        mCallback = clickOkCallBack;
    }

    /**
     * 选中拦截Ok键时的回调接口
     */
    public interface IClickOkCallback {
        /**
         * 点击Ok键回调方法
         * @param itemBean item实例
         */
        void onClickOk(Object itemBean);
    }
}
