package com.linyang.study.primary.gesture_detector.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2018/12/25 10:59 星期二
 */
public class MultiTouchView extends View {

    private static final String TAG = "MultiTouchView";

    private int mWidth;
    private int mHeight;

    private Paint mPaint;

    // 记录手指位置
    private PointF mPointF = new PointF(0, 0);

    private boolean mHasPoint;

    public MultiTouchView(Context context) {
        this(context, null);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureWidth(widthMeasureSpec);
        measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                // 收集触碰位置坐标
                if (event.getPointerId(event.getActionIndex()) == 1) {
                    mPointF.set(event.getY(), event.getX());
                    mHasPoint = true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(event.getActionIndex()) == 1) {
                    mPointF.set(0, 0);
                    mHasPoint = false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mHasPoint) {
                    // 通过 pointerId 来获取 pointerIndex
                    int pointerIndex = event.findPointerIndex(1);
                    // 通过 pointerIndex 来取出对应的坐标
                    mPointF.set(event.getX(pointerIndex), event.getY(pointerIndex));
                }
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制触碰位置
        if (mHasPoint) {
            canvas.drawCircle(mPointF.x, mPointF.y, 50, mPaint);
            canvas.drawCircle(mPointF.x, mPointF.y, 80, mPaint);
        }
    }

    /**
     * 宽度测量
     *
     * @param widthMeasureSpec
     */
    private void measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                mWidth = size;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int value = getPaddingLeft() + getPaddingRight();
                // 取最小值
                mWidth = Math.min(size, value);
                break;
        }
    }

    /**
     * 高度测量
     *
     * @param heightMeasureSpec
     */
    private void measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                mHeight = size;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int value = getPaddingTop() + getPaddingBottom();
                // 取最小值
                mHeight = Math.min(size, value);
                break;
        }
    }
}
