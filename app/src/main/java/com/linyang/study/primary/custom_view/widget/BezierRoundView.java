package com.linyang.study.primary.custom_view.widget;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.linyang.study.app.util.DisplayUtil;

import java.util.Arrays;

/**
 * 描述:自定义贝塞尔曲线
 * Created by fzJiang on 2018-11-02
 */
public class BezierRoundView extends View {

    private static final String TAG = BezierRoundView.class.getSimpleName();

    private static final float BEZ_FACTOR = 0.551915024494f;

    private static final int COLOR_BEZ = 0xfffe626d;
    private static final int COLOR_STROKE = Color.GRAY;
    private static final int COLOR_TOUCH = 0xfffe626d;

    //-------------view相关-------------
    private int mWidth;// 界面宽度
    private int mHeight;// 界面高度

    //-------------画笔相关-------------
    private Paint mBezPaint;
    private Paint mRoundStokePaint;
    private Paint mTouchPaint;


    //-------------参数-------------
    private int mRadius;// 圆的半径
    private int mRoundCount = 4;// 圆数目

    private PointF[] mPoints;// 数据点
    private Path mBezPath;// 绘制曲线路径

    private float[] mBezPos;// 记录各圆心x轴的位置
    private float[] mXPivotPos;// 根据圆心x轴+mRadius，划分成不同的区域 ,主要为了判断触摸x轴的位置

    private PorterDuffXfermode mClearXfermode;

    private int mCurPos = 0;// 当前圆的位置
    private int mNextPos = 0;// 圆要到达的下一个位置

    private float rRadio = 1;// P2,3,4 x轴倍数
    private float lRadio = 1;// P8,9,10倍数
    private float tbRadio = 1;// y轴缩放倍数

    private float disL = 0.5f;// 离开圆的阈值
    private float disM = 0.8f;// 最大值的阈值
    private float disA = 0.9f;// 到达下个圆框的阈值


    private ViewPager mViewPage;

    //-------------动画-------------

    private int mAnimatorTime = 600;// 动画时间

    private float mAnimatedValue;
    private float mAnimatedTouchValue;

    private boolean mDirection; // 方向 , true是位置向右(0->1)
    private boolean isAniming;
    private boolean isTouchAniming = false;

    private Matrix mBounceLMatrix;// 将向右弹的动画改为向左

    private ValueAnimator mAnimatorStart;
    private ValueAnimator mAnimatorTouch;
    private TimeInterpolator mTimeInterpolator = new DecelerateInterpolator();

    private RectF mTouchRectF = new RectF();  //触摸反馈范围


    public BezierRoundView(Context context) {
        this(context, null);
    }

    public BezierRoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mBezPaint = new Paint();
        mBezPaint.setColor(COLOR_BEZ);
        mBezPaint.setStyle(Paint.Style.FILL);
        mBezPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mRoundStokePaint = new Paint();
        mRoundStokePaint.setColor(COLOR_STROKE);
        mRoundStokePaint.setStyle(Paint.Style.STROKE);
        mRoundStokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mRoundStokePaint.setStrokeWidth(2);

        mTouchPaint = new Paint();
        mTouchPaint.setColor(COLOR_TOUCH);
        mTouchPaint.setStyle(Paint.Style.FILL);
        mTouchPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mClearXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mTouchPaint.setXfermode(mClearXfermode);


        // 圆半径
        mRadius = DisplayUtil.dip2px(getContext(), 15);

        // 绘制数据点
        mPoints = new PointF[12];
        mPoints[0] = new PointF(0, -mRadius);//mRadius圆的半径
        mPoints[6] = new PointF(0, mRadius);

        mPoints[1] = new PointF(mRadius * BEZ_FACTOR, -mRadius);//BEZ_FACTOR即0.5519...
        mPoints[5] = new PointF(mRadius * BEZ_FACTOR, mRadius);

        mPoints[2] = new PointF(mRadius, -mRadius * BEZ_FACTOR);
        mPoints[4] = new PointF(mRadius, mRadius * BEZ_FACTOR);

        mPoints[3] = new PointF(mRadius, 0);
        mPoints[9] = new PointF(-mRadius, 0);

        mPoints[11] = new PointF(-mRadius * BEZ_FACTOR, -mRadius);
        mPoints[7] = new PointF(-mRadius * BEZ_FACTOR, mRadius);

        mPoints[10] = new PointF(-mRadius, -mRadius * BEZ_FACTOR);
        mPoints[8] = new PointF(-mRadius, mRadius * BEZ_FACTOR);

        mBezPath = new Path();
        mBounceLMatrix = new Matrix();
        mBounceLMatrix.preScale(-1, 1);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();

                // 先判断y，如果y点击是在圆y轴的范围
                if (y <= mHeight / 2 + mRadius && y >= mHeight / 2 - mRadius && !isAniming) {
                    int pos = -Arrays.binarySearch(mXPivotPos, x) - 1;
                    if (pos >= 0 && pos < mRoundCount && x + mRadius >= mBezPos[pos]) {
                        mNextPos = pos;

                        // 与之绑定的ViewPager不为空，则开启滑动动画
                        if (mViewPage != null && mCurPos != mNextPos) {
                            mViewPage.setCurrentItem(pos);
                            isAniming = true;

                            startAnimator();
                            //startTouchAnimator();
                        }
                    }
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 开启动画
     */
    private void startAnimator() {
        if (mAnimatorStart == null) {
            mAnimatorStart = ValueAnimator.ofFloat(0, 1f).setDuration(mAnimatorTime);
            mAnimatorStart.setInterpolator(mTimeInterpolator);
            mAnimatorStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    isAniming = true;
                    mAnimatedValue = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimatorStart.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    isAniming = true;
                    if (mViewPage != null) {
//                        mViewPage.setTouchable(false);
                        mViewPage.setEnabled(false);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        } else {

        }
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
        // 计算各圆心位置
        initCountPos();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(0, mHeight / 2);

        mBezPath.reset();

        // 绘制圆框
        for (int i = 0; i < mRoundCount; i++) {
            canvas.drawCircle(mBezPos[i], 0, mRadius - 2, mRoundStokePaint);
        }

        if (mAnimatedValue == 1) {
            canvas.drawCircle(mBezPos[mNextPos], 0, mRadius, mBezPaint);
            return;
        }


        // 实现触摸反馈
//        if (isTouchAniming) {
//            int count = canvas.saveLayer(rectF_touch, mTouchPaint, Canvas.ALL_SAVE_FLAG);
//
//            // 先画一个白色的圆 [0,mRadius*1.5]
//            canvas.drawCircle(mBezPos[mNextPos], 0, animatedTouchValue, mTouchPaint);
//
//            mTouchPaint.setXfermode(mClearXfermode);
//
//            // 从 0.7-1.4   效果会更好看！
//            canvas.drawCircle(mBezPos[mNextPos], 0, mRadius * 0.7f, mTouchPaint);
//
//            // 如果白色的圆半径>=mRadis ，就开始绘制透明的圆
//            if (animatedTouchValue >= mRadius) {
//                canvas.drawCircle(mBezPos[mNextPos], 0, (animatedTouchValue - mRadius) / 0.5f * 1.4f, mTouchPaint);
//            }
//            mTouchPaint.setXfermode(null);
//
//            canvas.restoreToCount(count);
//        }
//
//        // 开始绘制曲线
//        canvas.translate(mBezPos[mCurPos], 0);
//
//        if (0 < mAnimatedValue && mAnimatedValue <= disL) {
//            rRadio = 1f + mAnimatedValue * 2;                         //  [1,2]
//            lRadio = 1f;
//            tbRadio = 1f;
//        }
//        if (disL < mAnimatedValue && mAnimatedValue <= disM) {
//            rRadio = 2 - range0Until1(disL, disM) * 0.5f;          //  [2,1.5]
//            lRadio = 1 + range0Until1(disL, disM) * 0.5f;          // [1,1.5]
//            tbRadio = 1 - range0Until1(disL, disM) / 3;           // [1 , 2/3]
//        }
//        if (disM < mAnimatedValue && mAnimatedValue <= disA) {
//            rRadio = 1.5f - range0Until1(disM, disA) * 0.5f;     //  [1.5,1]
//            lRadio = 1.5f - range0Until1(disM, disA) * (1.5f - boundRadio);      //反弹效果，进场 内弹boundRadio
//            tbRadio = (range0Until1(disM, disA) + 2) / 3;        // [ 2/3,1]
//        }
//        if (disA < mAnimatedValue && mAnimatedValue <= 1f) {
//            rRadio = 1;
//            tbRadio = 1;
//            lRadio = boundRadio + range0Until1(disA, 1) * (1 - boundRadio);     //反弹效果，饱和
//        }
//        if (mAnimatedValue == 1 || mAnimatedValue == 0) {  //防止极其粗暴的滑动
//            rRadio = 1f;
//            lRadio = 1f;
//            tbRadio = 1f;
//        }
//
//        // 绘制动画
//        bounce2RightRound();
//        canvas.drawPath(mBezPath, mBezPaint);
    }

    /**
     * 通过 path 将向右弹射的动画绘制出来
     * 如果要绘制向左的动画，只要设置path的transform(matrix)即可
     */
    private void bounce2RightRound() {
        mBezPath.moveTo(mPoints[0].x, mPoints[0].y * tbRadio);
        mBezPath.cubicTo(mPoints[1].x, mPoints[1].y * tbRadio, mPoints[2].x * rRadio, mPoints[2].y, mPoints[3].x * rRadio, mPoints[3].y);
        mBezPath.cubicTo(mPoints[4].x * rRadio, mPoints[3].y, mPoints[5].x, mPoints[5].y * tbRadio, mPoints[6].x, mPoints[6].y * tbRadio);
        mBezPath.cubicTo(mPoints[7].x, mPoints[7].y * tbRadio, mPoints[8].x * lRadio, mPoints[8].y, mPoints[9].x * lRadio, mPoints[9].y);
        mBezPath.cubicTo(mPoints[10].x * lRadio, mPoints[10].y, mPoints[11].x, mPoints[11].y * tbRadio, mPoints[0].x, mPoints[0].y * tbRadio);
        mBezPath.close();
    }

    /**
     * 将animatedValue值域转化为[0,1]
     *
     * @param minValue 大于等于
     * @param maxValue 小于等于
     * @return 根据当前 animatedValue,返回 [0,1] 对应的数值
     */
    private float range0Until1(float minValue, float maxValue) {
        return (mAnimatedValue - minValue) / (maxValue - minValue);
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


    /**
     * 计算各圆心位置
     */
    private void initCountPos() {
        mBezPos = new float[mRoundCount];
        mXPivotPos = new float[mRoundCount];
        for (int i = 0; i < mRoundCount; i++) {
            mBezPos[i] = mWidth / (mRoundCount + 1) * (i + 1);
            mXPivotPos[i] = mWidth / (mRoundCount + 1) * (i + 1) + mRadius;
        }
    }


}
