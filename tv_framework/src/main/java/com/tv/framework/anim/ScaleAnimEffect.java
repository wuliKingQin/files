package com.tv.framework.anim;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * 功能描述：比例动画效果设置
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/29- 16:23
 */

public class ScaleAnimEffect {

    //x开始比例
    private float fromXScale;
    //结束比例
    private float toXScale;
    //y开始比例
    private float fromYScale;
    //结束比例
    private float toYScale;
    //持续时间
    private long duration;

    /**
     * 设置缩放参数
     * @param fromXScale 初始X轴缩放比例
     * @param toXScale 目标X轴缩放比例
     * @param fromYScale 初始Y轴缩放比例
     * @param toYScale 目标Y轴缩放比例
     * @param duration 动画持续时间
     */
    public void setAttributs(float fromXScale, float toXScale, float fromYScale, float toYScale, long duration) {
        this.fromXScale = fromXScale;
        this.fromYScale = fromYScale;
        this.toXScale = toXScale;
        this.toYScale = toYScale;
        this.duration = duration;
    }

    /**
     * 创建动画
     * @return 返回动画实例
     */
    public Animation createAnimation(float fromXScale, float toXScale, float fromYScale, float toYScale, long duration) {
        ScaleAnimation anim = new ScaleAnimation(fromXScale, toXScale, fromYScale, toYScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(duration);
        return anim;
    }
}
