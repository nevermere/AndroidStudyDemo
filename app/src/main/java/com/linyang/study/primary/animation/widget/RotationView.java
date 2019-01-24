package com.linyang.study.primary.animation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linyang.study.R;

/**
 * 描述:
 * Created by fzJiang on 2018/12/19 14:42 星期三
 */
public class RotationView extends RelativeLayout implements View.OnClickListener {

    private ImageView mImageView;

    private int mTranslationState = 0;

    public RotationView(Context context) {
        super(context);
    }

    public RotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mImageView = findViewById(R.id.iv_image);

        Button startBtn = findViewById(R.id.bt_start);
        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (mTranslationState) {
            case 0:
                mImageView.animate().rotation(360);
                break;

            case 1:
                mImageView.animate().rotation(0);
                break;

            case 2:
                mImageView.animate().rotationX(180);
                break;

            case 3:
                mImageView.animate().rotationX(0);
                break;

            case 4:
                mImageView.animate().rotationY(180);
                break;

            case 5:// z轴 100 -> 0
                mImageView.animate().rotationY(0);
                break;
        }

        mTranslationState++;
        if (mTranslationState == 6) {
            mTranslationState = 0;
        }
    }
}
