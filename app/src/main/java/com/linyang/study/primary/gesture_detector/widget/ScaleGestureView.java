package com.linyang.study.primary.gesture_detector.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.linyang.study.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2018/12/25 8:42 星期二
 */
public class ScaleGestureView extends View {

    private static final String TAG = "ScaleGestureView";

    //--- 限制缩放比例 ---
    private static final float MAX_SCALE = 4.0f;    //最大缩放比例
    private static final float MIN_SCALE = 0.5f;    // 最小缩放比例

    private static final int MSCALE_X = 0, MSKEW_X = 1, MTRANS_X = 2;
    private static final int MSKEW_Y = 3, MSCALE_Y = 4, MTRANS_Y = 5;
    private static final int MPERSP_0 = 6, MPERSP_1 = 7, MPERSP_2 = 8;

    private Paint mPaint;

    private Bitmap mBitmap;

    private int mWidth;
    private int mHeight;

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    // 画布当前的 Matrix， 用于获取当前画布的一些状态信息，例如缩放大小，平移距离等
    private Matrix mCanvasMatrix = new Matrix();

    // 将用户触摸的坐标转换为画布上坐标所需的 Matrix， 以便找到正确的缩放中心位置
    private Matrix mInvertMatrix = new Matrix();

    // 所有用户触发的缩放、平移等操作都通过下面的 Matrix 直接作用于画布上，
    // 将系统计算的一些初始缩放平移信息与用户操作的信息进行隔离，让操作更加直观
    private Matrix mUserMatrix = new Matrix();

    // 基础的缩放和平移信息，该信息与用户的手势操作无关
    private float mBaseScale;
    private float mBaseTranslateX;
    private float mBaseTranslateY;

    //--- 获取 Matrix 中的属性 ---
    private float[] matrixValues = new float[9];

    public ScaleGestureView(Context context) {
        this(context, null);
    }

    public ScaleGestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test);

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 用户滑动屏幕，根据当前缩放比例进行画面位移
                float scale = getMatrixValue(MSCALE_X, mCanvasMatrix);
                mUserMatrix.preTranslate(-velocityX / scale, -velocityY / scale);
                // fixTranslate();   // 在用户滚动时不进行修正，保证用户滚动时也有响应， 在用户抬起手指后进行修正
                invalidate();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // 用户双击界面，进行还原或者放大操作
                if (mUserMatrix.isIdentity()) {
                    // 未经变换，则进行放大操作
                    float[] points = mapPoint(e.getX(), e.getY(), mInvertMatrix);
                    mUserMatrix.postScale(MAX_SCALE, MAX_SCALE, points[0], points[1]);
                } else {
                    // 还原
                    mUserMatrix.reset();
                }
                fixTranslate();
                invalidate();
                return true;
            }
        });

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {

            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                // 根据手势缩放比例及缩放中心点，进行缩放操作
                float scaleFactor = scaleGestureDetector.getScaleFactor();
                float focusX = scaleGestureDetector.getFocusX();
                float focusY = scaleGestureDetector.getFocusY();

                float[] points = mapPoint(focusX, focusY, mInvertMatrix);
                scaleFactor = getRealScaleFactor(scaleFactor);
                mUserMatrix.preScale(scaleFactor, scaleFactor, points[0], points[1]);
                fixTranslate();
                invalidate();

                return false;
            }
        });
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

        // 比较缩放图片与界面的宽高比,计算初始缩放比例及需移动位置
        if (mBitmap.getWidth() / mBitmap.getHeight() > w / h) {
            mBaseScale = w / mBitmap.getWidth();
            mBaseTranslateX = 0;
            mBaseTranslateY = (h - mBitmap.getHeight() * mBaseScale) / 2;
        } else {
            mBaseScale = h / mBitmap.getHeight();
            mBaseTranslateX = (w - mBitmap.getWidth() * mBaseScale) / 2;
            mBaseTranslateY = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.scale(mBaseScale, mBaseScale);
        canvas.save();
        canvas.concat(mUserMatrix);

        mCanvasMatrix = canvas.getMatrix();
        mCanvasMatrix.invert(mInvertMatrix);

        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.restore();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        // 拦截到手指抬起事件，修正缩放及位置
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            fixTranslate();
        }
        return true;
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

    /**
     * 对 Matrix 进行预计算，并根据计算结果进行修正
     */
    private void fixTranslate() {
        // 获取当前控件的Matrix
        Matrix viewMatrix = new Matrix(getMatrix());
        viewMatrix.preTranslate(mBaseTranslateX, mBaseTranslateY);
        viewMatrix.preScale(mBaseScale, mBaseScale);
        viewMatrix.preConcat(mUserMatrix);

        Matrix invert = new Matrix();
        viewMatrix.invert(invert);
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);

        float userScale = getMatrixValue(MSCALE_X, mUserMatrix);
        float scale = getMatrixValue(MSCALE_X, viewMatrix);

        float[] center = mapPoint(mBitmap.getWidth() / 2.0f, mBitmap.getHeight() / 2.0f, viewMatrix);
        float distanceX = center[0] - getWidth() / 2.0f;
        float distanceY = center[1] - getHeight() / 2.0f;
        float[] wh = mapVectors(mBitmap.getWidth(), mBitmap.getHeight(), viewMatrix);

        if (userScale > 1.0f) {
            float[] leftTop = mapPoint(0, 0, viewMatrix);
            float[] rightBottom = mapPoint(mBitmap.getWidth(), mBitmap.getHeight(), viewMatrix);

            // 如果宽度小于总宽度，则水平居中
            if (wh[0] < getWidth()) {
                mUserMatrix.preTranslate(distanceX / scale, 0);
            } else {
                if (leftTop[0] > 0) {
                    mUserMatrix.preTranslate(-leftTop[0] / scale, 0);
                } else if (rightBottom[0] < getWidth()) {
                    mUserMatrix.preTranslate((getWidth() - rightBottom[0]) / scale, 0);
                }

            }
            // 如果高度小于总高度，则垂直居中
            if (wh[1] < getHeight()) {
                mUserMatrix.preTranslate(0, -distanceY / scale);
            } else {
                if (leftTop[1] > 0) {
                    mUserMatrix.preTranslate(0, -leftTop[1] / scale);
                } else if (rightBottom[1] < getHeight()) {
                    mUserMatrix.preTranslate(0, (getHeight() - rightBottom[1]) / scale);
                }
            }
        } else {
            mUserMatrix.preTranslate(-distanceX / scale, -distanceY / scale);
        }
        invalidate();
    }

    /**
     * 将坐标转换为画布坐标
     *
     * @param x
     * @param y
     * @param matrix
     * @return
     */
    private float[] mapPoint(float x, float y, Matrix matrix) {
        float[] temp = new float[2];
        temp[0] = x;
        temp[1] = y;
        matrix.mapPoints(temp);
        return temp;
    }

    /**
     * 将坐标转换为画布坐标(不受位移影响)
     *
     * @param x
     * @param y
     * @param matrix
     * @return
     */
    private float[] mapVectors(float x, float y, Matrix matrix) {
        float[] temp = new float[2];
        temp[0] = x;
        temp[1] = y;
        matrix.mapVectors(temp);
        return temp;
    }

    /**
     * 计算需进行缩放比例
     *
     * @param currentScaleFactor
     * @return
     */
    private float getRealScaleFactor(float currentScaleFactor) {
        float realScale;
        float userScale = getMatrixValue(MSCALE_X, mUserMatrix);    // 用户当前的缩放比例
        float theoryScale = userScale * currentScaleFactor;           // 理论缩放数值

        // 如果用户在执行放大操作并且理论缩放数据大于4.0
        if (currentScaleFactor > 1.0f && theoryScale > MAX_SCALE) {
            realScale = MAX_SCALE / userScale;
        } else if (currentScaleFactor < 1.0f && theoryScale < MIN_SCALE) {
            realScale = MIN_SCALE / userScale;
        } else {
            realScale = currentScaleFactor;
        }
        return realScale;
    }

    /**
     * 获取当前Matrix 中的属性值
     *
     * @param name
     * @param matrix
     * @return
     */
    private float getMatrixValue(@MatrixName int name, Matrix matrix) {
        matrix.getValues(matrixValues);
        return matrixValues[name];
    }

    @IntDef({MSCALE_X, MSKEW_X, MTRANS_X, MSKEW_Y, MSCALE_Y, MTRANS_Y, MPERSP_0, MPERSP_1, MPERSP_2})
    @Retention(RetentionPolicy.SOURCE)
    private @interface MatrixName {

    }
}
