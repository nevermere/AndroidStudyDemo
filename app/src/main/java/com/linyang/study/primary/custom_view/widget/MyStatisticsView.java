package com.linyang.study.primary.custom_view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 描述:自定义View——折线统计图
 * Created by fzJiang on 2018-08-22
 */
public class MyStatisticsView extends View {

    //-------------view相关-------------
    private float mWidth;// 界面宽度
    private float mHeight;// 界面高度

    //-------------参数相关-------------
    private String[] mValueStrX = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private String[] mValueStrY = new String[]{"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
    private int[] mValue = new int[]{8, 15, 33, 48, 77, 80, 95};
    private String mTopStr = "项目完成的进度（单位%）";
    private int yMaxValue = Integer.parseInt(mValueStrY[mValueStrY.length - 1]);// 折线表示的最大值,取mValueStrY的最大值

    //-------------统计图相关-------------
    private int mNumX = mValueStrX.length - 1;// x轴的条目数目
    private int mNumY = mValueStrY.length;// y轴的条目数目
    private int mSizeX = 100;// x轴条目之间的距离
    private int mSizeY = 60; // y轴条目之间的距离
    private int yLastSize = (mNumY - 1) * mSizeY; // y轴的长度,11个条目只有10段距离

    //-------------画笔相关-------------
    private Paint mBorderPaint;// 绘制边框画笔
    private Paint mTextPaint;// 绘制文字画笔
    private Paint mLinePaint;// 绘制折线画笔
    private Paint mPointPaint;// 绘制圆点画笔

    //-------------颜色相关-------------
    private int mBorderColor = Color.BLACK;// 边框颜色
    private int mTextColor = Color.BLUE;// 文字颜色
    private int mLineColor = Color.GREEN;// 折线颜色
    private int mPointColor = Color.RED;// 圆点颜色

    public MyStatisticsView(Context context) {
        super(context);
        init();
    }

    public MyStatisticsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyStatisticsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    /**
     * 初始化
     */
    private void init() {
        // 边框画笔
        mBorderPaint = new Paint();
        // 设置抗锯齿
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(1);

        // 文字画笔
        mTextPaint = new Paint();
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);

        //区域画笔
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2);

        //黑点画笔
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setColor(mPointColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画布移到左下角，留出100的空间给予文字填充
        canvas.translate(65, mHeight - 100);
        // 绘制边框
        drawBorder(canvas);
        // 绘制圆点
        drawPoint(canvas);
        // 绘制文字
        drawText(canvas);
        // 绘制折线
        drawLine(canvas);
    }

    /**
     * 绘制边框
     *
     * @param canvas 画笔
     */
    private void drawBorder(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < mNumY; i++) {
            // 绘制一条竖直的边框线
            if (i == 0) {
                path.moveTo(0, -i * mSizeY);
                path.lineTo(0, -(mNumY - 1) * mSizeY);
            }
            // 水平循环线
            path.moveTo(0, -i * mSizeY);
            path.lineTo(mNumX * mSizeX, -i * mSizeY);
            canvas.drawPath(path, mBorderPaint);
        }
    }

    /**
     * 绘制x轴圆点坐标
     *
     * @param canvas 画笔
     */
    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < mNumX; i++) {
            canvas.drawCircle(i * mSizeX, 0, 5, mPointPaint);
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas 画笔
     */
    private void drawText(Canvas canvas) {
        Rect rect;
        float textWidth;
        float textHeight;
        // 绘制x轴的文字
        for (int i = 0, size = mValueStrX.length; i < size; i++) {
            rect = new Rect();
            mTextPaint.getTextBounds(mValueStrX[i], 0, mValueStrX[i].length(), rect);
            textWidth = rect.width();
            textHeight = rect.height();
            canvas.drawText(mValueStrX[i], i * mSizeX - textWidth / 2, textHeight + 20, mTextPaint);
        }

        // 绘制y轴的文字
        for (int i = 0, size = mValueStrY.length; i < size; i++) {
            rect = new Rect();
            mTextPaint.getTextBounds(mValueStrY[i], 0, mValueStrY[i].length(), rect);
            textWidth = rect.width();
            textHeight = rect.height();
            canvas.drawText(mValueStrY[i], -textWidth - 20, i * (-mSizeY) + (textHeight / 2), mTextPaint);
        }
        // 绘制顶部文字
        canvas.drawText(mTopStr, 0, (-mSizeY) * (mValueStrY.length - 1) - 20, mTextPaint);
    }

    /**
     * 绘制折线及数值圆点
     *
     * @param canvas 画笔
     */
    private void drawLine(Canvas canvas) {
        Path path = new Path();
        for (int i = 0, size = mValue.length; i < size; i++) {
            // 计算折线的位置：（当前点的值/最大值）= 百分比percent
            // 用百分比percent乘与y轴总长，就获得了折线的位置
            // 这里拿到的百分比一直为0，所以换一种方法，先乘与总长再除与最大值，而且记得加上负号
            float position = -(mValue[i] * yLastSize / yMaxValue);
            if (position == 0) {
                // 第一个点需要移动
                path.moveTo(i * mSizeX, position);
            } else {
                // 其余的点直接画线
                path.lineTo(i * mSizeX, position);
            }
            // 绘制折线
            canvas.drawPath(path, mLinePaint);
            // 绘制交汇处的圆点
            canvas.drawCircle(i * mSizeX, position, 5, mPointPaint);
        }
    }
}
