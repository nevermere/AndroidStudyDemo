package com.linyang.study.primary.custom_view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 描述:自定义拦截横向滑动ScrollView
 * Created by fzJiang on 2018/11/16 下午 2:39  星期五
 */
public class HorizontalScrollViewEx extends ViewGroup {

    // 子控件的相关参数
    private int mChildrenSize;
    private int mChildrenWidth;
    private int mChildrenIndex;

    // 分别记录上次滑动的坐标
    private int mLastX;

    // 分别记录上次滑动的坐标
    private int mLastInterceptX;
    private int mLastInterceptY;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontalScrollViewEx(Context context) {
        this(context, null);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth;
        int measuredHeight;
        final int childCount = getChildCount();

        // 测量每个View的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (childCount == 0) {
            setMeasuredDimension(0, 0);

        } else {
            final View childView = getChildAt(0);

            if (MeasureSpec.AT_MOST == widthMode && MeasureSpec.AT_MOST == heightMode) {
                measuredWidth = childView.getMeasuredWidth() * childCount;
                measuredHeight = childView.getMeasuredHeight();
                setMeasuredDimension(measuredWidth, measuredHeight);

            } else if (MeasureSpec.AT_MOST == widthMode) {
                measuredWidth = childView.getMeasuredWidth() * childCount;
                setMeasuredDimension(measuredWidth, heightSize);

            } else if (MeasureSpec.AT_MOST == heightMode) {
                measuredHeight = childView.getMeasuredHeight();
                setMeasuredDimension(widthSize, measuredHeight);
            }
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int childLeft = 0;
        final int childCount = getChildCount();
        mChildrenSize = childCount;
        for (int index = 0; index < childCount; index++) {
            final View childView = getChildAt(index);
            if (View.GONE != childView.getVisibility()) {
                final int childWidth = childView.getMeasuredWidth();
                mChildrenWidth = childWidth;
                childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercepted = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int delaX = x - mLastInterceptX;
                int delaY = y - mLastInterceptY;
                // 横向滑动距离大于纵向滑动距离，则进行拦截
                intercepted = Math.abs(delaX) > Math.abs(delaY);
                break;

            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;

            default:
                break;
        }

        mLastX = x;
        mLastInterceptX = x;
        mLastInterceptY = y;
        return intercepted;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);

        int x = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 点击时，停止滑动
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;

            case MotionEvent.ACTION_MOVE:// 只做横向滑动
                int deltaX = x - mLastX;
                scrollBy(-deltaX, 0);
                break;

            case MotionEvent.ACTION_UP:// 手指滑动屏幕完毕，根据滑动速率，判断是否进行滑动
                int scrollX = getScrollX();
                // 计算手指滑动速率
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) >= 50) {
                    // 滑动有效,判断是左滑还是右滑
                    mChildrenIndex = xVelocity > 0 ? mChildrenIndex - 1 : mChildrenIndex + 1;
                } else {
                    // 滑动无效.回弹处理(1/2界面宽度)
                    mChildrenIndex = (scrollX + mChildrenWidth / 2) / mChildrenWidth;
                }
                // 当前显示的子控件
                mChildrenIndex = Math.max(0, Math.min(mChildrenIndex, mChildrenSize - 1));
                // 回弹距离(总长度 - 已滑动的距离)
                smoothScrollX(mChildrenIndex * mChildrenWidth - scrollX);
                mVelocityTracker.clear();
                break;

            default:
                break;
        }
        mLastX = x;
        return true;
    }

    /**
     * x轴滑动处理
     *
     * @param dx
     */
    private void smoothScrollX(int dx) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
        super.onDetachedFromWindow();
    }
}
