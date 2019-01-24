package com.linyang.study.primary.custom_view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 描述:自定义View——彩色圆环统计图
 * Created by fzJiang on 2018-08-23
 */
public class MyChatView extends View {

    //-------------参数-------------
    private String[] mValueStr = new String[]{"娱乐", "服饰", "旅游", "生活缴费", "饮食", "充值缴费"};
    private String mTotalValueStr;
    private double[] mValue = new double[]{100.00, 255.23, 188.45, 410.00, 255.01, 512.89};
    private List<DataBean> mDataBeanList;

    private int mRadius = 400;// 圆直径
    private int mStrokeWidth = 40;// 圆粗细
    private int mTextSize = 20;// 字体大小
    private int mTextAlign = 40;// 文字与文字的间距
    private int mTextLeftAlign = 60;// 文字与边框间距
    private int mLabelRadius = 20;// 标注小圆点直径

    //-------------view相关-------------
    private float mWidth;// 界面宽度
    private float mCanvasX;
    private float mCanvasY;


    //-------------画笔相关-------------
    private Paint mCyclePaint;// 绘制圆圈画笔
    private Paint mCenterCyclePaint;// 绘制中心圆圈画笔
    private Paint mTextPaint;// 绘制文字画笔
    private Paint mLabelPaint;// 绘制标注画笔

    //-------------颜色相关-------------
    private int mTextColor = Color.BLACK;// 文字颜色
    private int mCenterCycleColor = 0xFF0B84DD;// 中心圆圈颜色
    private int[] mColor = new int[]{0xFFF06292, 0xFF9575CD, 0xFFE57373, 0xFFFFF176, 0xFF81C784};// 边框颜色和标注颜色


    public MyChatView(Context context) {
        this(context, null);
    }

    public MyChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //边框画笔
        mCyclePaint = new Paint();
        mCyclePaint.setAntiAlias(true);
        mCyclePaint.setStyle(Paint.Style.STROKE);
        mCyclePaint.setStrokeWidth(mStrokeWidth);

        // 中心圆圈画笔
        mCenterCyclePaint = new Paint();
        mCenterCyclePaint.setAntiAlias(true);
        mCenterCyclePaint.setColor(mCenterCycleColor);
        mCenterCyclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setTextSize(mTextSize);

        //标注画笔
        mLabelPaint = new Paint();
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setStyle(Paint.Style.FILL);
        mLabelPaint.setStrokeWidth(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = Math.min(w, h);
        mCanvasX = mCanvasY = (mWidth - mRadius) / 2;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 移动画布到圆环的左上角
        canvas.translate(mCanvasX, mCanvasY);
        // 计算需绘制的数据
        calculateDatas();
        // 绘制大圆
        drawCycle(canvas);
        // 绘制文字和标注
        drawTextAndLabel(canvas);
    }

    /**
     * 计算需绘制的数据
     */
    private void calculateDatas() {
        // 计算绘制总数
        double totalValue = 0d;
        for (double value : mValue) {
            totalValue += value;
        }
        mTotalValueStr = MessageFormat.format("总花费:{0} 元", totalValue);

        // 计算需绘制扇形的角度
        float tempAngle = 0;
        float startAngel;
        float sweepAngel;
        mDataBeanList = new ArrayList<>();
        DataBean dataBean;
        for (int i = 0; i < mValue.length; i++) {
            dataBean = new DataBean();
            dataBean.setColor(mColor[i % mColor.length]);

            startAngel = tempAngle;
            sweepAngel = (float) (mValue[i] * 100 / totalValue * 3.6);
            tempAngle += sweepAngel;

            dataBean.setStartAngel(startAngel);
            dataBean.setSweepAngel(sweepAngel);
            dataBean.setValueName(mValueStr[i]);

            mDataBeanList.add(dataBean);
        }
    }

    /**
     * 绘制大圆
     * 使用drawArc方法画出一个直径为mRadius的圆环，从初始角度开始，扫过多少角度。这里使用初始角度的递增方法使圆环一段接上一段的画出来。
     * 如果想让圆环旋转起来，就修改startPercent的值即可。
     *
     * @param canvas
     */
    private void drawCycle(Canvas canvas) {
        // 绘制中心圆
        canvas.drawCircle(mCanvasX + mStrokeWidth, mCanvasY + mStrokeWidth, (mRadius - mStrokeWidth) / 2, mCenterCyclePaint);
        // 绘制外圈扇形
        RectF rectF = new RectF(0, 0, mRadius, mRadius);
        for (DataBean data : mDataBeanList) {
            mCyclePaint.setColor(data.getColor());
            canvas.drawArc(rectF, data.getStartAngel(), data.getSweepAngel(), false, mCyclePaint);
        }
    }

    /**
     * 绘制文字和标注
     *
     * @param canvas
     */
    private void drawTextAndLabel(Canvas canvas) {
        // 绘制数据列表项
        for (int i = 0; i < mDataBeanList.size(); i++) {
            DataBean data = mDataBeanList.get(i);
            // 文字离右边环边距，文字与文字之间的距离
            canvas.drawText(data.getValueName(), mRadius + mTextLeftAlign, i * mTextAlign, mTextPaint);
            //画标注,标注离右边环边距,y轴则要减去半径的一半才能对齐文字
            mLabelPaint.setColor(data.getColor());
            canvas.drawCircle(mRadius + mTextAlign, i * mTextAlign - mLabelRadius / 4, mLabelRadius / 2, mLabelPaint);
        }

        // 绘制中心总数文字提示
        Rect rect = new Rect();
        mTextPaint.getTextBounds(mTotalValueStr, 0, mTotalValueStr.length(), rect);

        float textWidth = rect.width();
        float textHeight = rect.height();

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(30);
        canvas.drawText(mTotalValueStr, mCanvasX - textWidth / 2, mCanvasY + textHeight * 2, mTextPaint);
    }


    /**
     * 绘制数据实体类
     */
    private class DataBean {

        private String valueName;// 数据项名称
        private float startAngel;// 起始角度
        private float sweepAngel;// 绘制的角度
        private int color;// 绘制的颜色

        public String getValueName() {
            return valueName;
        }

        public void setValueName(String valueName) {
            this.valueName = valueName;
        }

        public float getStartAngel() {
            return startAngel;
        }

        public void setStartAngel(float startAngel) {
            this.startAngel = startAngel;
        }

        public float getSweepAngel() {
            return sweepAngel;
        }

        public void setSweepAngel(float sweepAngel) {
            this.sweepAngel = sweepAngel;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
