package com.linyang.study.primary.gesture_detector.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.linyang.study.R;

import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2018/12/26 8:58 星期三
 */
public class DragView extends View {

    private static final String TAG = "DragView";

    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private Bitmap mBitmap;
    private Matrix mBitmapMatrix;   // 控制图片的 matrix
    private RectF mBitmapRectF;// 图片所在区域
    private PointF mLastPoint;

    private boolean mCanDrag;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = 300;
        options.outHeight = 200;

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test, options);

        mBitmapMatrix = new Matrix();
        mBitmapRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mLastPoint = new PointF(0, 0);
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
                // 判断是否是第一个手指 && 是否包含在图片区域内
                if (event.getPointerId(event.getActionIndex()) == 0 && mBitmapRectF.contains(event.getX(), event.getY())) {
                    mLastPoint.set(event.getY(), event.getX());
                    mCanDrag = true;
//                    Log.i(TAG, "mLastPointX:" + mLastPoint.x + ",mLastPointY:" + mLastPoint.y);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                // 判断是否是第一个手指
                if (event.getPointerId(event.getActionIndex()) == 0) {
                    mCanDrag = false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // 如果存在第一个手指，且这个手指的落点在图片区域内
                if (mCanDrag) {
                    int index = event.findPointerIndex(0);
                    float x = event.getX(index);
                    float y = event.getY(index);
                    //Log.i(TAG, "index:" + index + ",X:" + x + ",Y:" + y);

                    mBitmapMatrix.postTranslate(x - mLastPoint.x, y - mLastPoint.y);
                    mLastPoint.set(event.getX(index), event.getY(index));

                    mBitmapRectF = new RectF(0,0,mBitmap.getWidth(), mBitmap.getHeight());
                    mBitmapMatrix.mapRect(mBitmapRectF);
                    invalidate();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, mBitmapMatrix, mPaint);
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
