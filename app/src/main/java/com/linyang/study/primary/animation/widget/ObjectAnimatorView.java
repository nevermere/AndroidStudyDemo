package com.linyang.study.primary.animation.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.linyang.study.app.util.DisplayUtil;

import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2018/12/20 10:19 星期四
 */
public class ObjectAnimatorView extends View {

    private Paint mPaint;
    private int mRadius;
    private int mProgress;

    private RectF mRectF;

    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;

    public ObjectAnimatorView(Context context) {
        this(context, null);
    }

    public ObjectAnimatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureWidth(widthMeasureSpec);
        measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mRadius = DisplayUtil.dip2px(getContext(), 80);
        mRectF = new RectF();
    }

    /**
     * 宽度测量
     *
     * @param widthMeasureSpec
     */
    private void measureWidth(int widthMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                mWidth = size;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int value = getPaddingLeft() + getPaddingRight();
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
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                mHeight = size;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int value = getPaddingTop() + getPaddingBottom();
                mHeight = Math.min(size, value);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制圆形进度条
        mPaint.setColor(Color.parseColor("#E91C64"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 15));
        mRectF.set(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
        canvas.drawArc(mRectF, 0, mProgress, false, mPaint);

        // 绘制进度文字
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DisplayUtil.dip2px(getContext(), 40));
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mProgress + "%", mCenterX, mCenterY - (mPaint.ascent() + mPaint.descent()) / 2, mPaint);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                break;
//
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }
}
