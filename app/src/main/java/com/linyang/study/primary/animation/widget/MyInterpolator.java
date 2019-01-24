package com.linyang.study.primary.animation.widget;

import android.view.animation.Interpolator;

/**
 * 描述:
 * Created by fzJiang on 2018/11/26 上午 11:09 星期一
 */
public class MyInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float v) {
        return v;
    }
}
