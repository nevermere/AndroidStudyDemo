package com.linyang.study.primary.custom_view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:Android自定义View——实现字母导航栏
 * Created by fzJiang on 2018-08-27
 */
public class MyNavView extends View {

    //-------------view相关-------------
    private int mWidth;// 界面宽度
    private int mHeight;// 界面高度

    //-------------画笔相关-------------
    private Paint mTextPaint;// 绘制画笔

    //-------------参数-------------
    private String[] mStrings = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private int mChoose = -1;// 鼠标点击、滑动时选择的字母
    private TextView mTextView;

    ConcurrentHashMap mConcurrentHashMap;

    public MyNavView(Context context) {
        super(context);
        init();
    }

    public MyNavView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyNavView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(20);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getWidth();
        mHeight = getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 开始绘制
        drawText(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 计算选中字母
                int index = (int) (event.getY() / mHeight * mStrings.length);
                if (index < 0) {
                    index = 0;
                } else if (index >= mStrings.length - 1) {
                    index = mStrings.length - 1;
                }
                // 更新当前选中位置并显示提示
                mChoose = index;
                setBackgroundColor(Color.GRAY);
                if (mTextView != null) {
                    mTextView.setVisibility(VISIBLE);
                    mTextView.setText(mStrings[mChoose]);
                }
                invalidate();
                break;

            default:
                mChoose = -1;
                setBackgroundColor(Color.WHITE);
                if (mTextView != null) {
                    mTextView.setVisibility(GONE);
                }
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 绘制字母
     */
    private void drawText(Canvas canvas) {
        // 获取绘制每个字母所需的高度
        int singleHeight = mHeight / mStrings.length;
        for (int i = 0; i < mStrings.length; i++) {
            // 选中的字母颜色
            mTextPaint.setColor(mChoose == i ? Color.RED : Color.BLACK);
            // 计算每个字母的坐标
            float textX = (mWidth - mTextPaint.measureText(mStrings[i])) / 2;
            float textY = (i + 1) * singleHeight;
            canvas.drawText(mStrings[i], textX, textY, mTextPaint);
            //mTextPaint.reset();
        }
    }

    public void setTextView(TextView textView) {
        mTextView = textView;
    }
}
