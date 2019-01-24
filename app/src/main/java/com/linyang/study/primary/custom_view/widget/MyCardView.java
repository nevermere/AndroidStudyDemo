package com.linyang.study.primary.custom_view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.linyang.study.R;
import com.linyang.study.app.util.DisplayUtil;

/**
 * 描述:自定义多边形网格属性图
 * Created by fzJiang on 2018-08-22
 */
public class MyCardView extends LinearLayout {

    public static final int MODE_TOP = 0;
    public static final int MODE_BOTTOM = 1;
    public static final int MODE_TOP_BOTTOM = 2;

    private int mMode = MODE_TOP_BOTTOM;// 显示模式
    private int mColor = Color.WHITE;// 小圆圈的颜色
    private float mRadius = 8;// 小圆圈的半径
    private float mGap = 8;// 间距

    private float mWidth;// 界面宽度
    private float mHeight;// 界面高度
    private int mRoundNum;// 需绘制的小圆圈个数
    private float mStartX;// 绘制X轴起始位置,保证居中显示

    private Paint mPaint;

    public MyCardView(Context context) {
        super(context, null, 0);
        init(context, null);
    }

    public MyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            // 获取设置的属性值
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCardView);
            // 获取显示模式
            mMode = typedArray.getInt(R.styleable.MyCardView_card_view_mode, MODE_TOP_BOTTOM);
            // 获取小圆圈的颜色
            mColor = typedArray.getColor(R.styleable.MyCardView_card_view_color, Color.WHITE);
            // 获取小圆圈的半径
            mRadius = DisplayUtil.dip2px(context, typedArray.getDimension(R.styleable.MyCardView_card_view_radius, 8));
            // 获取小圆圈的间距
            mGap = DisplayUtil.dip2px(context, typedArray.getDimension(R.styleable.MyCardView_card_view_gap, 8));
            typedArray.recycle();
        }

        // 初始化画笔
        mPaint = new Paint();
        // 设置画笔颜色
        mPaint.setColor(mColor);
        // 设置画笔模式
        mPaint.setStyle(Paint.Style.FILL);
        // 设置防抖动
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取界面宽高
        mWidth = getWidth();
        mHeight = getHeight();

        // 获取需绘制的小圆圈个数
        mRoundNum = (int) (mWidth / (mRadius * 2 + mGap * 2));
        // 绘制X轴起始位置
        mStartX = (float) ((mWidth - mRoundNum * (mRadius * 2 + mGap * 2)) * 0.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 开始绘制
        for (int i = 1; i <= mRoundNum; i++) {
            if (mMode == MODE_TOP) {
                // 仅绘制上层的小圆圈
                //canvas.drawCircle((mRadius + mGap) * (i * 2 - 1) + mStartX, 0, mRadius, mPaint);
                canvas.drawCircle((mRadius + mGap) * (i * 2 - 1) + mStartX, mRadius, mRadius, mPaint);
            } else if (mMode == MODE_BOTTOM) {
                // 仅绘制下层的小圆圈
                canvas.drawCircle((mRadius + mGap) * (i * 2 - 1) + mStartX, mHeight, mRadius, mPaint);
            } else {
                // 绘制上层的小圆圈
                canvas.drawCircle((mRadius + mGap) * (i * 2 - 1) + mStartX, 0, mRadius, mPaint);
                // 绘制下层的小圆圈
                canvas.drawCircle((mRadius + mGap) * (i * 2 - 1) + mStartX, mHeight, mRadius, mPaint);
            }
        }
    }

    /**
     * 获取界面宽度
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        int size = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                size = measureSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                size = measureSize;
                break;
        }
        return size + getPaddingLeft() + getPaddingRight();
    }

    /**
     * 获取界面高度
     *
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        int size = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                size = measureSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                size = measureSize;
                break;
        }
        return size + getPaddingTop() + getPaddingBottom();
    }
}
