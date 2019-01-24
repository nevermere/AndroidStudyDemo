package com.linyang.study.primary.custom_view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.linyang.study.R;

/**
 * 描述:自定义View——刮刮卡效果
 * Created by fzJiang on 2018-08-24
 */
public class GuaGuaKaView extends View {

    private Canvas mCanvas;// 顶层画布
    private Paint mPaint;// 透明画笔
    private Path mPath;// 路径，记录手指滑动

    private Bitmap mBgBitmap;// 底层图片
    private Bitmap mFgBitmap;// 顶层图片

    private float mLastX;// 记录上一次位置
    private float mSumX;
    private float mDX;

    private int mBgBitmapWidth;// 底层图片宽度
    private boolean isFinish;// 是否滑动完成

    private ClearTopThread mClearTopThread;

    public GuaGuaKaView(Context context) {
        super(context);
        init();
    }

    public GuaGuaKaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuaGuaKaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 透明画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(50);
        mPaint.setAlpha(0);
        // 设置交汇模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.STROKE);
        // 设置圆角
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // 初始化路径
        mPath = new Path();

        // 创建底层图片
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        mBgBitmapWidth = mBgBitmap.getWidth();
        // 创建顶层图片
        mFgBitmap = Bitmap.createBitmap(mBgBitmap.getWidth(),
                mBgBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 初始化顶层画布
        mCanvas = new Canvas(mFgBitmap);
        mCanvas.drawColor(Color.GRAY);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 手指触摸,重新记录触摸位置
                mPath.reset();
                mPath.moveTo(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:// 手指开始滑动，绘制滑动轨迹
                mPath.lineTo(event.getX(), event.getY());
                mCanvas.drawPath(mPath, mPaint);
                invalidate();

                // 计算x轴滑动的距离,判断是否有横向滑动
                mDX = Math.abs(event.getX() - mLastX);
                if (mDX > 0) {
                    mSumX += mDX;
                }
                break;

            case MotionEvent.ACTION_UP:// 滑动结束，等待1s后清除所有顶层涂层
//                if (mClearTopThread == null) {
//                    mClearTopThread = new ClearTopThread();
//                }
//                if (!mClearTopThread.isAlive()) {
//                    mClearTopThread.start();
//                }
                break;
        }
        // 更新位置
        mLastX = event.getX();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 首先绘制背景
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
        // 滑动结束后，绘制顶层
        if (!isFinish) {
            canvas.drawBitmap(mFgBitmap, 0, 0, null);
        }
    }

    /**
     * 清除所有顶层涂层
     */
    private class ClearTopThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                if (mSumX > mBgBitmapWidth * 4) {
                    isFinish = true;
                    Thread.sleep(1000);
                    postInvalidate();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
