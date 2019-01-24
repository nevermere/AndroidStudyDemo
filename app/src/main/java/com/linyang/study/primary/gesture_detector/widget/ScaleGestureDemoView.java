package com.linyang.study.primary.gesture_detector.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2018/12/24 16:00 星期一
 */
public class ScaleGestureDemoView extends View {

    public static final String TAG = "ScaleGestureDemoView";

    private ScaleGestureDetector mDetector;

    public ScaleGestureDemoView(Context context) {
        this(context, null);
    }

    public ScaleGestureDemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        ScaleGestureDetector.OnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {

            @Override
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                Log.i(TAG, "focusX = " + scaleGestureDetector.getFocusX());       // 缩放中心，x坐标
                Log.i(TAG, "focusY = " + scaleGestureDetector.getFocusY());       // 缩放中心y坐标
                Log.i(TAG, "scale = " + scaleGestureDetector.getScaleFactor());   // 缩放因子
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

            }
        };
        mDetector = new ScaleGestureDetector(context, scaleGestureListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }
}
