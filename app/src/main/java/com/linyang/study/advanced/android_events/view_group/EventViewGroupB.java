package com.linyang.study.advanced.android_events.view_group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.linyang.study.app.util.L;

/**
 * 描述:
 * Created by fzJiang on 2019/03/21 15:33 星期四
 */
public class EventViewGroupB extends LinearLayout {

    public EventViewGroupB(Context context) {
        super(context);
    }

    public EventViewGroupB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventViewGroupB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        L.i("-------------onInterceptTouchEvent-------------------EventViewGroupB");

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        L.i("-------------dispatchTouchEvent-------------------EventViewGroupB");

        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.i("-------------onTouchEvent-------------------EventViewGroupB");

        return super.onTouchEvent(event);
    }
}
