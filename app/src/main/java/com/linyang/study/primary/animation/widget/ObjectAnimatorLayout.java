package com.linyang.study.primary.animation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.linyang.study.R;

import java.util.Random;

import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2018/12/20 10:56 星期四
 */
public class ObjectAnimatorLayout extends RelativeLayout implements View.OnClickListener {

    private ObjectAnimatorView mAnimatorView;

    public ObjectAnimatorLayout(Context context) {
        super(context);
    }

    public ObjectAnimatorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mAnimatorView = findViewById(R.id.animator_view);
        Button startBtn = findViewById(R.id.bt_start);
        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int progress = new Random().nextInt(360);
        mAnimatorView.setProgress(progress);
    }
}
