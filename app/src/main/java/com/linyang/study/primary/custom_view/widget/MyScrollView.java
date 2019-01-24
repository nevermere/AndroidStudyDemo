package com.linyang.study.primary.custom_view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;

/**
 * 描述:
 * Created by fzJiang on 2018-11-09
 */
public class MyScrollView extends View {

    private Scroller mScroller;

    private int mLastX;
    private int mLastY;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                int translationX = (int) (ViewHelper.getTranslationX(this) + (x - mLastX));
                int translationY = (int) (ViewHelper.getTranslationY(this) + (y - mLastY));

                ViewHelper.setTranslationX(this, translationX);
                ViewHelper.setTranslationY(this, translationY);

                mLastX = x;
                mLastY = y;
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
}
