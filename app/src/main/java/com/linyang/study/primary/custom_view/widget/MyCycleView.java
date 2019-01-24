package com.linyang.study.primary.custom_view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.linyang.study.R;


import static android.graphics.Bitmap.createScaledBitmap;

/**
 * 描述:自定义View——QQ音乐中圆形旋转碟子
 * Created by fzJiang on 2018-08-24
 */
public class MyCycleView extends View {

    //-------------view相关-------------
    private int mWidth;// 界面宽度
    private int mHeight;// 界面高度

    //-------------参数-------------
    private int mRouteDegrees;// 旋转角度
    private int mRadius;// 旋转圆形的半径
    private Matrix mMatrix;// 旋转动画矩形
    private Bitmap mBitmap;// 旋转的圆形图片

    private boolean isRunning;


    private Handler mHandler;
    private RouteRunnable mRouteRunnable;

    //-------------画笔相关-------------
    private Paint mPaint;// 绘制画笔

    public MyCycleView(Context context) {
        this(context, null);
    }

    public MyCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mMatrix = new Matrix();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);

        // 画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mHandler = new Handler();
        mRouteRunnable = new RouteRunnable();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量并设置显示尺寸
        measureWidth(widthMeasureSpec);
        measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
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
                int value = getPaddingLeft() + getPaddingRight() + mBitmap.getWidth();
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
                int value = getPaddingTop() + getPaddingBottom() + mBitmap.getHeight();
                // 取最小值
                mHeight = Math.min(size, value);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.concat(mMatrix);
        // 真实的半径必须是View的宽高最小值
        mRadius = Math.min(mWidth, mHeight);
        // 如果图片本身宽高太大，进行相应的缩放
        mBitmap = createScaledBitmap(mBitmap, mRadius, mRadius, false);
        // 开始绘制
        canvas.drawBitmap(createCircleImage(), 0, 0, null);
        mMatrix.reset();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isRunning) {
                    mHandler.post(mRouteRunnable);
                }
                break;
        }
        return true;
    }

    /**
     * 生成圆形图片
     */
    private Bitmap createCircleImage() {
        Bitmap target = Bitmap.createBitmap(mRadius, mRadius, Bitmap.Config.ARGB_8888);
        // 初始化一个同样大小的画布
        Canvas canvas = new Canvas(target);
        // 绘制外层圆形
        canvas.drawCircle(mRadius / 2, mRadius / 2, mRadius / 2, mPaint);
        // 使用SRC_IN模式显示后画图的交集处
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // 绘制内层图片
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        return target;
    }

    private class RouteRunnable implements Runnable {

        @Override
        public void run() {
            isRunning = true;
            mMatrix.postRotate(mRouteDegrees++, mRadius / 2, mRadius / 2);
            invalidate();
            mHandler.postDelayed(mRouteRunnable, 50);
        }
    }
}
