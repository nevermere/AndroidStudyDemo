package com.linyang.study.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.linyang.study.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feng.skin.manager.util.L;

/**
 * 描述:
 * Created by fzJiang on 2018/11/26 下午 2:17 星期一
 */
public class WindowManagerActivity extends AppCompatActivity implements View.OnTouchListener {

    @BindView(R.id.bt_add_window)
    AppCompatButton btAddWindow;
    @BindView(R.id.bt_update_window)
    AppCompatButton btUpdateWindow;
    @BindView(R.id.bt_delete_window)
    AppCompatButton btDeleteWindow;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private TextView mTextView;

    private boolean isAdded;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_manager);
        ButterKnife.bind(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        mTextView = new TextView(this);
        mTextView.setText("WindowManager");
        mTextView.setTextColor(Color.WHITE);
        mTextView.setBackgroundResource(R.drawable.ic_shape_drawable);
        mTextView.setOnTouchListener(this);

        mLayoutParams = new WindowManager.LayoutParams();
        // 系统提示window,兼容8.0以下
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mLayoutParams.format = PixelFormat.TRANSLUCENT;// 支持透明
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mLayoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        mLayoutParams.width = 500;// 窗口的宽和高
        mLayoutParams.height = 200;
        mLayoutParams.x = 0;// 窗口位置的偏移量
        mLayoutParams.y = 300;
    }

    @OnClick({R.id.bt_add_window, R.id.bt_update_window, R.id.bt_delete_window})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_add_window:
                if (!isAdded) {
                    mWindowManager.addView(mTextView, mLayoutParams);
                    isAdded = true;
                }
                break;
            case R.id.bt_update_window:
                if (isAdded) {
                    mTextView.setText("WindowManager Update");
                    mWindowManager.updateViewLayout(mTextView, mLayoutParams);
                }
                break;
            case R.id.bt_delete_window:
                if (mTextView.getParent() != null) {
                    mWindowManager.removeView(mTextView);
                    isAdded = false;
                }
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();

        L.i("rawX:" + rawX + ",rawY:" + rawY);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mLayoutParams.x = rawX;
                mLayoutParams.y = rawY;
                mWindowManager.updateViewLayout(mTextView, mLayoutParams);
                break;
            default:
                break;
        }
        return true;
    }
}
