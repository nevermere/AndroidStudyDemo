package com.linyang.study.advanced.android_events.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.linyang.study.app.util.L;

import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2019/03/21 14:48 星期四
 */
public class EventViewA extends View {

    public EventViewA(Context context) {
        super(context);
    }

    public EventViewA(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventViewA(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        L.i("-------------dispatchTouchEvent-------------------EventViewA");

        return super.dispatchTouchEvent(event);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.i("-------------onTouchEvent-------------------EventViewA");

        return super.onTouchEvent(event);
    }
}
