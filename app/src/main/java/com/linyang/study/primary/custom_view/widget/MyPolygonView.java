package com.linyang.study.primary.custom_view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 描述:自定义多边形网格界面
 * Created by fzJiang on 2018-08-22
 */
public class MyPolygonView extends View {

    //-------------参数-------------
    private int mPolygonNumber = 6;// 多边形边数
    private String[] mText = new String[]{"A", "B", "C", "D", "E", "F"};// 文字
    private int[] mArea = new int[]{4, 3, 3, 2, 4, 1}; // 区域等级，值不能超过n边形的个数

    //-------------view相关-------------
    private float mWidth;// 界面宽度
    private float mHeight;// 界面高度

    //-------------画笔相关-------------
    private Paint mBorderPaint;// 绘制边框画笔
    private Paint mTextPaint;// 绘制文字画笔
    private Paint mAreaPaint;// 绘制区域画笔

    //-------------多边形相关-------------
    private int mPolygonCount = 5;// 多边形个数
    private int mRadius = 50; //两个多边形之间的半径

    private float mTopX;// 多边形顶点坐标x
    private float mTopY;// 多边形顶点坐标y
    private float mAngle = (float) ((2 * Math.PI) / mPolygonNumber);// 多边形角度

    private int mTextAlign = 5;// 文字与边框的边距等级，值越大边距越小

    //-------------颜色相关-------------
    private int mBorderColor = Color.BLUE;// 边框颜色
    private int mTextColor = Color.BLACK;// 文字颜色
    private int mAreaColor = Color.CYAN;// 区域颜色


    public MyPolygonView(Context context) {
        super(context);
        init();
    }

    public MyPolygonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyPolygonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
        mBorderPaint.setStrokeWidth(3);

        // 文字画笔
        mTextPaint = new Paint();
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);

        // 区域画笔
        mAreaPaint = new Paint();
        // 设置抗锯齿
        mAreaPaint.setAntiAlias(true);
        mAreaPaint.setColor(mAreaColor);
        mAreaPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mAreaPaint.setAlpha(155);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取宽高
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画笔位置位移到界面中心
        canvas.translate(mWidth / 2, mHeight / 2);
        // 绘制多边形
        drawPolygon(canvas);
        // 绘制多边形的中点到顶点的线
        drawLine(canvas);
        // 绘制多边形的文字提示
        drawText(canvas);
        // 绘制多边形的区域
        drawArea(canvas);
    }

    /**
     * 绘制多边形
     *
     * @param canvas 画笔
     */
    private void drawPolygon(Canvas canvas) {
        // 使用path进行绘制
        Path path = new Path();
        // 1.绘制多边形的数目
        for (int i = 1; i <= mPolygonCount; i++) {
            // 获取绘制偏移的角度
            float radius = i * mRadius;
            // path的起点回到（0，0）
            path.reset();
            // 2.绘制多边形，首先通过角度（angle）可以找出我们n边形的顶点。接着让Path移到（path.moveTo）某一顶点开始，然后连接下一个顶点（path.lineTo）作为直线
            for (int j = 1; j <= mPolygonNumber; j++) {
                // 获取多边形顶点坐标x,y
                mTopX = (float) (Math.cos(j * mAngle) * radius);
                mTopY = (float) (Math.sin(j * mAngle) * radius);
                if (j == 1) {
                    path.moveTo(mTopX, mTopY);
                } else {
                    path.lineTo(mTopX, mTopY);
                }
            }
            // 3.关闭当前轮廓,用（path.close）会自动把最后一条边自动合上.如果当前点不等于第一个点的轮廓,一条线段是自动添加的
            path.close();
            canvas.drawPath(path, mBorderPaint);
        }
    }

    /**
     * 绘制多边形的中点到顶点的线
     *
     * @param canvas 画笔
     */
    private void drawLine(Canvas canvas) {
        Path path = new Path();
        float radius = mPolygonCount * mRadius;
        for (int i = 1; i <= mPolygonNumber; i++) {
            path.reset();
            mTopX = (float) (Math.cos(i * mAngle) * radius);
            mTopY = (float) (Math.sin(i * mAngle) * radius);
            path.lineTo(mTopX, mTopY);
            canvas.drawPath(path, mBorderPaint);
        }
    }

    /**
     * 绘制多边形的文字提示
     *
     * @param canvas 画笔
     */
    private void drawText(Canvas canvas) {
        float radius = mPolygonCount * mRadius;
        for (int i = 1; i <= mPolygonNumber; i++) {
            // 测量出每个字符串的宽高
            Rect rect = new Rect();
            mTextPaint.getTextBounds(mText[i - 1], 0, mText[i - 1].length(), rect);

            float textWidth = rect.width();
            float textHeight = rect.height();

            // 将字符串的某个顶点移到n边形的顶点重合
            mTopX = (float) (Math.cos(i * mAngle) * radius);
            mTopY = (float) (Math.sin(i * mAngle) * radius);

            // 文字位置微调，防止占用网格位置
            if (mTopX < 0) {
                mTopX = mTopX - textWidth;
            }
            if (mTopY > 25) {
                mTopY = mTopY + textHeight;
            }

            // 调文字与边框的边距
            float LastX = mTopX + mTopX / mPolygonCount / mTextAlign;
            float LastY = mTopY + mTopY / mPolygonCount / mTextAlign;
            canvas.drawText(mText[i - 1], LastX, LastY, mTextPaint);
        }
    }

    /**
     * 绘制多边形的区域
     *
     * @param canvas 画笔
     */
    private void drawArea(Canvas canvas) {
        Path path = new Path();
        for (int i = 1; i <= mPolygonNumber; i++) {
            float radius = mArea[i - 1] * mRadius;
            mTopX = (float) (Math.cos(i * mAngle) * radius);
            mTopY = (float) (Math.sin(i * mAngle) * radius);
            if (i == 1) {
                path.moveTo(mTopX, mTopY);
            } else {
                path.lineTo(mTopX, mTopY);
            }
        }
        // 关闭当前轮廓。如果当前点不等于第一个点的轮廓,一条线段是自动添加的
        path.close();
        canvas.drawPath(path, mAreaPaint);
    }
}
