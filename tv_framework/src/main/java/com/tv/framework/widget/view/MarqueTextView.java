package com.tv.framework.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 功能描述：让其具有跑马灯效果的显示文本
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/5- 15:58
 */

public class MarqueTextView extends TextView {

    public MarqueTextView(Context context) {
        super(context);
    }

    public MarqueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override

    public boolean isFocused() {
        return true;
    }
}
