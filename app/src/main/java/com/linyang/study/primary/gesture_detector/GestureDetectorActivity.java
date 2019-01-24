package com.linyang.study.primary.gesture_detector;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.stetho.common.LogUtil;
import com.linyang.study.R;
import com.linyang.study.app.util.ArmsUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2018/12/21 15:20 星期五
 */
public class GestureDetectorActivity extends AppCompatActivity {

    @BindView(R.id.btn_click_view)
    AppCompatButton btnClickView;
    @BindView(R.id.btn_simple_gesture)
    AppCompatButton btnSimpleGesture;
    @BindView(R.id.btn_simple_gesture_two)
    AppCompatButton btnSimpleGestureTwo;
    @BindView(R.id.btn_click_view_two)
    AppCompatButton btnClickViewTwo;
    @BindView(R.id.btn_failing_ball)
    AppCompatButton btnFailingBall;
    @BindView(R.id.btn_scale)
    AppCompatButton btnScale;
    @BindView(R.id.btn_multi_touch)
    AppCompatButton btnMultiTouch;
    @BindView(R.id.btn_draw_view)
    AppCompatButton btnDrawView;

    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener mGestureListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);
        ButterKnife.bind(this);

        // 1.创建一个监听回调
        mGestureListener = new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                LogUtil.i("--------------onSingleTapUp-------------");
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                LogUtil.i("--------------onLongPress-------------");
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                LogUtil.i("--------------onScroll-------------");
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                LogUtil.i("--------------onFling-------------");
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                LogUtil.i("--------------onShowPress-------------");
                super.onShowPress(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                LogUtil.i("--------------onDown-------------");
                return super.onDown(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                LogUtil.i("--------------onDoubleTap-------------");
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                LogUtil.i("--------------onDoubleTapEvent-------------");
                return super.onDoubleTapEvent(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                LogUtil.i("--------------onSingleTapConfirmed-------------");
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                LogUtil.i("--------------onContextClick-------------");
                return super.onContextClick(e);
            }
        };
    }

    @OnClick({R.id.btn_simple_gesture, R.id.btn_simple_gesture_two, R.id.btn_failing_ball, R.id.btn_scale, R.id.btn_multi_touch, R.id.btn_draw_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_simple_gesture:
                addGestureListener();
                break;

            case R.id.btn_simple_gesture_two:
                addGestureListenerTwo();
                break;

            case R.id.btn_failing_ball:
                ArmsUtils.startActivity(this, FailingBallActivity.class);
                break;

            case R.id.btn_scale:
                ArmsUtils.startActivity(this, ScaleGestureDetectorActivity.class);
                break;

            case R.id.btn_multi_touch:
                ArmsUtils.startActivity(this, MultiTouchActivity.class);
                break;

            case R.id.btn_draw_view:
                ArmsUtils.startActivity(this, DragViewActivity.class);
                break;
        }
    }

    /**
     * 构造函数一,设置手势检测--方式一
     */
    @SuppressLint("ClickableViewAccessibility")
    private void addGestureListener() {
        // 2.创建一个检测器
        mGestureDetector = new GestureDetector(this, mGestureListener);
        // 3.给监听器设置数据源
        btnClickView.setOnTouchListener((view, motionEvent) -> mGestureDetector.onTouchEvent(motionEvent));
    }

    /**
     * 构造函数二,设置手势检测
     */
    @SuppressLint("ClickableViewAccessibility")
    private void addGestureListenerTwo() {
        // 方式一、在主线程创建 Handler
        final Handler handler1 = new Handler();
        new Thread(() -> {
            mGestureDetector = new GestureDetector(GestureDetectorActivity.this, mGestureListener, handler1);
            btnClickViewTwo.setOnTouchListener((view, motionEvent) -> mGestureDetector.onTouchEvent(motionEvent));
        }).start();

        // 方式二、在子线程创建 Handler，并且指定 Looper
        new Thread(() -> {
            final Handler handler2 = new Handler(Looper.getMainLooper());
            mGestureDetector = new GestureDetector(GestureDetectorActivity.this, mGestureListener, handler2);
            btnClickViewTwo.setOnTouchListener((view, motionEvent) -> mGestureDetector.onTouchEvent(motionEvent));
        }).start();
//
//        new Thread(() -> {
//            // 假如子线程准备了 Looper 那么可以直接使用第 1 种构造函数进行创建
//            Looper.prepare();
//            mGestureDetector = new GestureDetector(GestureDetectorActivity.this, mGestureListener);
//            btnClickViewTwo.setOnTouchListener((view, motionEvent) -> mGestureDetector.onTouchEvent(motionEvent));
//        }).start();
    }
}
