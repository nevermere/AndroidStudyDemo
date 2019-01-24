package com.linyang.study.primary.custom_view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.linyang.study.R;
import com.linyang.study.app.util.DisplayUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 描述:
 * Created by fzJiang on 2018-10-25
 */
public class LeafLoadingView extends View {

    private static final int MIDDLE_AMPLITUDE = 13;// 中等振幅大小
    private static final int AMPLITUDE_DISPARITY = 5;// 不同类型之间的振幅差距

    private static final int LOAD_COLOR = 0xffffa800;// 橙色
    private static final int UN_LOAD_COLOR = 0xfffde399;// 淡白色

    private static final int LEFT_MARGIN = 9;// 用于控制绘制的进度条距离左／上／下的距离
    private static final int RIGHT_MARGIN = 25; // 用于控制绘制的进度条距离右的距离

    private static final int TOTAL_PROGRESS = 100; // 总进度
    private static final long LEAF_FLOAT_TIME = 3000;// 叶子飘动一个周期所花的时间
    private static final long LEAF_ROTATE_TIME = 2000; // 叶子旋转一周需要的时间

    private Paint mBitmapPaint;// 绘制图片画笔
    private Paint mOrangePaint;// 绘制已加载区域画笔
    private Paint mWhitePaint;// 绘制未加载区域画笔

    private RectF mOrangeRectF;// 已加载区域

    private RectF mWhiteRectF;// 未加载左边区域

    private RectF mArcRectF;// 未加载右边区域
    private int mArcRightLocation;

    private Bitmap mOuterBitmap;// 边框
    private Rect mOuterSrcRect;
    private Rect mOuterDestRect;
    private int mOuterWidth;
    private int mOuterHeight;

    private Bitmap mLeafBitmap;// 叶子
    private Bitmap mLoadingBitmap;// 加载风扇

    private int mLoadColor;// 已加载区域颜色
    private int mUnLoadColor;// 未加载区域颜色

    private int mViewWidth;// 界面宽度
    private int mViewHeight;// 界面高度

    private int mLeftMargin;
    private int mRightMargin;


    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;// 中等振幅大小
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY; // 振幅差

    // 叶子飘动一个周期所花的时间
    private long mLeafFloatTime = LEAF_FLOAT_TIME;
    // 叶子旋转一周需要的时间
    private long mLeafRotateTime = LEAF_ROTATE_TIME;

    private int mLeafWidth;
    private int mLeafHeight;

    private int mProgress;// 当前进度
    private int mProgressWidth;// 所绘制的进度条部分的宽度
    private int mCurrentProgressPosition;// 当前所在的绘制的进度条的位置
    private int mArcRadius; // 弧形的半径

    private List<Leaf> mLeafList;

    public LeafLoadingView(Context context) {
        super(context);
        // 初始化
        init(context, null);
    }

    public LeafLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 初始化
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LeafLoadingView);
            mLoadColor = array.getColor(R.styleable.LeafLoadingView_load_color, LOAD_COLOR);
            mUnLoadColor = array.getColor(R.styleable.LeafLoadingView_un_load_color, UN_LOAD_COLOR);
            array.recycle();
        }

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setDither(true);

        mOrangePaint = new Paint();
        mOrangePaint.setAntiAlias(true);
        mOrangePaint.setColor(mLoadColor);

        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(mUnLoadColor);

        Resources resources = context.getResources();
        // 背景图片
        mOuterBitmap = BitmapFactory.decodeResource(resources, R.drawable.leaf_kuang);
        mOuterWidth = mOuterBitmap.getWidth();
        mOuterHeight = mOuterBitmap.getHeight();
        mOuterSrcRect = new Rect(0, 0, mOuterWidth, mOuterHeight);

        // 叶子图片
        mLeafBitmap = BitmapFactory.decodeResource(resources, R.drawable.leaf);
        mLeafWidth = mLeafBitmap.getWidth();
        mLeafHeight = mLeafBitmap.getHeight();

        // 加载风扇图片
        mLoadingBitmap = BitmapFactory.decodeResource(resources, R.drawable.fengshan);

        mLeftMargin = DisplayUtil.dip2px(context, LEFT_MARGIN);
        mRightMargin = DisplayUtil.dip2px(context, RIGHT_MARGIN);

        // 初始化叶子
        mLeafList = new LeafFactory().generateLeafs();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量并设置显示尺寸
        measureWidth(widthMeasureSpec);
        measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mProgressWidth = mViewWidth - mLeftMargin - mRightMargin;
        mArcRadius = (mViewHeight - 2 * mLeftMargin) / 2;

        mOuterDestRect = new Rect(0, 0, mViewWidth, mViewHeight);

        mWhiteRectF = new RectF(mLeftMargin + mCurrentProgressPosition, mLeftMargin, mViewWidth
                - mRightMargin,
                mViewHeight - mLeftMargin);

        mOrangeRectF = new RectF(mLeftMargin + mArcRadius, mLeftMargin,
                mCurrentProgressPosition
                , mViewHeight - mLeftMargin);

        mArcRectF = new RectF(mLeftMargin, mLeftMargin, mLeftMargin + 2 * mArcRadius,
                mViewHeight - mLeftMargin);

        mArcRightLocation = mLeftMargin + mArcRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制进度条及叶子
        drawProgressAndLeafs(canvas);
        // 绘制背景
        canvas.drawBitmap(mOuterBitmap, mOuterSrcRect, mOuterDestRect, mBitmapPaint);
        // 绘制右侧风扇


        postInvalidate();
    }

    /**
     * 绘制进度条及叶子
     *
     * @param canvas 绘制对象
     */
    private void drawProgressAndLeafs(Canvas canvas) {
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = 0;
        }
        // mProgressWidth为进度条的宽度，根据当前进度算出进度条的位置
        mCurrentProgressPosition = mProgressWidth * mProgress / TOTAL_PROGRESS;

        if (mCurrentProgressPosition < mArcRadius) { // 加载进度未超过左侧半圆(绘制白色弧形 + 绘制白色矩形 + 绘制棕色弧形)
            // 1.绘制白色ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mWhitePaint);

            // 2.绘制白色矩形
            mWhiteRectF.left = mArcRightLocation;
            canvas.drawRect(mWhiteRectF, mWhitePaint);

            // 绘制叶子
            drawLeafs(canvas);

            // 3.绘制棕色 ARC
            // 单边角度
            int angle = (int) Math.toDegrees(Math.acos((mArcRadius - mCurrentProgressPosition) / (float) mArcRadius));
            // 起始角度
            int startAngle = 180 - angle;
            // 扫过的角度= 2 *  单边角度
            int sweepAngle = 2 * angle;
            canvas.drawArc(mArcRectF, startAngle, sweepAngle, false, mOrangePaint);
        } else { // 加载进度超过左侧半圆(绘制白色矩形 + 绘制棕色弧形 + 绘制棕色矩形)

            // 这个层级进行绘制能让叶子感觉是融入棕色进度条中

            // 1.绘制白色矩形
            mWhiteRectF.left = mCurrentProgressPosition;
            canvas.drawRect(mWhiteRectF, mWhitePaint);

            // 绘制叶子
            drawLeafs(canvas);

            // 2.绘制棕色弧形
            canvas.drawArc(mArcRectF, 90, 180, false, mOrangePaint);

            // 3.绘制棕色矩形
            mOrangeRectF.left = mArcRightLocation;
            mOrangeRectF.right = mCurrentProgressPosition;
            canvas.drawRect(mOrangeRectF, mOrangePaint);
        }
    }

    /**
     * 绘制叶子
     *
     * @param canvas 绘制对象
     */
    private void drawLeafs(Canvas canvas) {
        mLeafRotateTime = mLeafRotateTime <= 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        long currentTime = System.currentTimeMillis();
        for (Leaf leaf : mLeafList) {
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(leaf, currentTime);
                // 根据时间计算旋转角度
                canvas.save();
                // 通过Matrix控制叶子旋转
                Matrix matrix = new Matrix();
                float transX = mLeftMargin + leaf.x;
                float transY = mLeftMargin + leaf.y;

                matrix.postTranslate(transX, transY);
                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                float rotateFraction = ((currentTime - leaf.startTime) % mLeafRotateTime)
                        / (float) mLeafRotateTime;
                int angle = (int) (rotateFraction * 360);
                // 根据叶子旋转方向确定叶子旋转角度
                int rotate = leaf.rotateDirection == Direction.LEFT ? angle + leaf.rotateAngle : -angle
                        + leaf.rotateAngle;
                matrix.postRotate(rotate, transX
                        + mLeafWidth / 2, transY + mLeafHeight / 2);
                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint);
                canvas.restore();
            }
        }
    }

    /**
     * 获取当前叶子位置
     *
     * @param leaf
     * @param currentTime
     */
    private void getLeafLocation(Leaf leaf, long currentTime) {
        long intervalTime = currentTime - leaf.startTime;
        mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        if (intervalTime < 0) {
            return;
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis()
                    + new Random().nextInt((int) mLeafFloatTime);
        }
        float fraction = (float) intervalTime / mLeafFloatTime;
        leaf.x = (int) (mProgressWidth - mProgressWidth * fraction);
        leaf.y = getLocationY(leaf);
    }

    /**
     * 通过叶子信息获取当前叶子的Y坐标
     *
     * @param leaf
     * @return
     */
    private int getLocationY(Leaf leaf) {
        // y = A(wx+Q)+h
        float w = (float) ((float) 2 * Math.PI / mProgressWidth);
        float a = mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                // 小振幅 ＝ 中等振幅 + 振幅差
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }
        return (int) (a * Math.sin(w * leaf.x)) + mArcRadius * 2 / 3;
    }

    private void measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                mViewWidth = size;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int value = getPaddingLeft() + getPaddingRight();
                // 取最小值
                mViewWidth = Math.min(size, value);
                break;
        }
    }

    private void measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                mViewHeight = size;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int value = getPaddingTop() + getPaddingBottom();
                // 取最小值
                mViewHeight = Math.min(size, value);
                break;
        }
    }

    /**
     * 叶子对象
     */
    private class Leaf {
        float x; // 在绘制部分的位置
        float y; // 在绘制部分的位置
        StartType type;  // 控制叶子飘动的幅度
        Direction rotateDirection;// 旋转方向--0代表顺时针，1代表逆时针
        int rotateAngle; // 旋转角度
        long startTime;// 起始时间(ms)
    }

    /**
     * 叶子飘动的幅度
     */
    private enum StartType {
        LITTLE, MIDDLE, BIG
    }

    /**
     * 旋转方向(顺时针，逆时针)
     */
    private enum Direction {
        LEFT, RIGHT
    }

    /**
     * 生成叶子列表
     */
    private class LeafFactory {

        private static final int MAX_LEAFS = 0;

        private Random mRandom = new Random();

        /**
         * 生成一个叶子
         *
         * @return {@link Leaf}
         */
        private Leaf generateLeaf() {
            Leaf leaf = new Leaf();

            // 设置随机振幅
            int type = mRandom.nextInt(3);
            StartType startType = null;
            switch (type) {
                case 0:
                    startType = StartType.LITTLE;
                    break;
                case 1:
                    startType = StartType.MIDDLE;
                    break;
                case 2:
                    startType = StartType.BIG;
                    break;
                default:
                    break;
            }
            leaf.type = startType;

            // 设置随机旋转角度(0-360)
            leaf.rotateAngle = mRandom.nextInt(360);
            // 设置随机旋转反向
            type = mRandom.nextInt(2);
            Direction direction = null;
            switch (type) {
                case 0:
                    direction = Direction.LEFT;
                    break;
                case 1:
                    direction = Direction.RIGHT;
                    break;
                default:
                    break;
            }
            leaf.rotateDirection = direction;

            // 设置随机旋转起始时间(ms)
            mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
            int time = mRandom.nextInt((int) (mLeafFloatTime * 2));
            leaf.startTime = System.currentTimeMillis() + time;
            return leaf;
        }

        /**
         * 根据最大叶子数产生叶子信息
         *
         * @return 随机产生的叶子列表
         */
        private List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

        /**
         * 根据传入的叶子数量产生叶子信息
         *
         * @param leafSize 需生成的叶子数量
         * @return 随机产生的叶子列表
         */
        private List<Leaf> generateLeafs(int leafSize) {
            List<Leaf> leafs = new LinkedList<>();
            for (int i = 0; i < leafSize; i++) {
                leafs.add(generateLeaf());
            }
            return leafs;
        }
    }

    /**
     * 设置中等振幅
     *
     * @param amplitude
     */
    public void setMiddleAmplitude(int amplitude) {
        this.mMiddleAmplitude = amplitude;
    }

    /**
     * 设置振幅差
     *
     * @param disparity
     */
    public void setMplitudeDisparity(int disparity) {
        this.mAmplitudeDisparity = disparity;
    }

    /**
     * 获取中等振幅
     */
    public int getMiddleAmplitude() {
        return mMiddleAmplitude;
    }

    /**
     * 获取振幅差
     */
    public int getMplitudeDisparity() {
        return mAmplitudeDisparity;
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    /**
     * 设置叶子飘完一个周期所花的时间
     *
     * @param time
     */
    public void setLeafFloatTime(long time) {
        this.mLeafFloatTime = time;
    }

    /**
     * 设置叶子旋转一周所花的时间
     *
     * @param time
     */
    public void setLeafRotateTime(long time) {
        this.mLeafRotateTime = time;
    }

    /**
     * 获取叶子飘完一个周期所花的时间
     */
    public long getLeafFloatTime() {
        mLeafFloatTime = mLeafFloatTime == 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        return mLeafFloatTime;
    }

    /**
     * 获取叶子旋转一周所花的时间
     */
    public long getLeafRotateTime() {
        mLeafRotateTime = mLeafRotateTime == 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        return mLeafRotateTime;
    }
}
