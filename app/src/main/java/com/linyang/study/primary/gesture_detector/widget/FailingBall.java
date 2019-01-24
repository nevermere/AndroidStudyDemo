package com.linyang.study.primary.gesture_detector.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.linyang.study.R;

import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2018/12/24 11:08 星期一
 */
public class FailingBall extends View {

    private Paint mPaint;
    private Bitmap mBitmap;

    private int mWidth;
    private int mHeight;

    private int mEdgeLength = 200;

    private float mStartX;
    private float mStartY;

    private float mFixedX;// 修正距离X
    private float mFixedY;// 修正距离Y

    private boolean mXFixed;
    private boolean mYFixed;

    private float mSpeedX;
    private float mSpeedY;

    private Rect mBallSrcRect;
    private RectF mBallDestRectF;

    private GestureDetector mGestureDetector;

    private boolean mCanFail;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            // 刷新内容
            mStartX = mStartX + mSpeedX / 30;
            mStartY = mStartY + mSpeedY / 30;
            mSpeedX *= 0.97;
            mSpeedY *= 0.97;

            if (Math.abs(mSpeedX) < 10) {
                mSpeedX = 0;
            }

            if (Math.abs(mSpeedY) < 10) {
                mSpeedY = 0;
            }

            if (refreshRectByCurrentPoint()) {
                // 转向
                if (mXFixed) {
                    mSpeedX = -mSpeedX;
                }
                if (mYFixed) {
                    mSpeedY = -mSpeedY;
                }
            }
            invalidate();
            if (mSpeedX == 0 && mSpeedY == 0) {
                mHandler.removeCallbacks(this);
                return;
            }
            mHandler.postDelayed(this, 33);
        }
    };

    public FailingBall(Context context) {
        this(context, null);
    }

    public FailingBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.failing_ball);
        mBallSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (!mCanFail) return false;
                mSpeedX = velocityX;
                mSpeedY = velocityY;
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 0);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        mGestureDetector = new GestureDetector(context, gestureListener);
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

        mStartX = (w - mEdgeLength) / 2;
        mStartY = (h - mEdgeLength) / 2;
        mBallDestRectF = new RectF(mStartX, mStartY, mStartX + mEdgeLength, mStartY + mEdgeLength);
        refreshRectByCurrentPoint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mBallSrcRect, mBallDestRectF, mPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (contains(event.getX(), event.getY())) {
                    mCanFail = true;
                    mFixedX = event.getX() - mStartX;
                    mFixedY = event.getY() - mStartY;
                    mSpeedX = 0;
                    mSpeedY = 0;
                } else {
                    mCanFail = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mCanFail) {
                    break;
                }
                mStartX = event.getX() - mFixedX;
                mStartY = event.getY() - mFixedY;
                if (refreshRectByCurrentPoint()) {
                    mFixedX = event.getX() - mStartX;
                    mFixedY = event.getY() - mStartY;
                }
                invalidate();
                break;
        }
        return true;
    }

    private void measureWidth(int widthMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                mWidth = size;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int value = mEdgeLength + getPaddingLeft() + getPaddingRight();
                mWidth = Math.min(size, value);
                break;
        }
    }

    private void measureHeight(int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                mHeight = size;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int value = mEdgeLength + getPaddingTop() + getPaddingBottom();
                mHeight = Math.min(size, value);
                break;
        }
    }

    /**
     * 判断点击位置是否位于小球内
     *
     * @param x
     * @param y
     * @return
     */
    private boolean contains(float x, float y) {
        float radius = mEdgeLength / 2;
        float centerX = mBallDestRectF.left + radius;
        float centerY = mBallDestRectF.top + radius;
        return Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius;
    }

    /**
     * 刷新位置
     *
     * @return true 表示修正过位置, false 表示没有修正过位置
     */
    private boolean refreshRectByCurrentPoint() {
        boolean fixed = false;
        mXFixed = false;
        mYFixed = false;
        // 修正坐标
        if (mStartX < 0) {
            mStartX = 0;
            fixed = true;
            mXFixed = true;
        }
        if (mStartY < 0) {
            mStartY = 0;
            fixed = true;
            mYFixed = true;
        }
        if (mStartX + mEdgeLength > mWidth) {
            mStartX = mWidth - mEdgeLength;
            fixed = true;
            mXFixed = true;
        }
        if (mStartY + mEdgeLength > mHeight) {
            mStartY = mHeight - mEdgeLength;
            fixed = true;
            mYFixed = true;
        }
        mBallDestRectF.left = mStartX;
        mBallDestRectF.top = mStartY;
        mBallDestRectF.right = mStartX + mEdgeLength;
        mBallDestRectF.bottom = mStartY + mEdgeLength;
        return fixed;
    }
}
