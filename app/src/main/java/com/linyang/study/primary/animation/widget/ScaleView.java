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
public class ScaleView extends RelativeLayout implements View.OnClickListener {

    private ImageView mImageView;

    private int mTranslationState = 0;

    public ScaleView(Context context) {
        super(context);
    }

    public ScaleView(Context context, AttributeSet attrs) {
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
                mImageView.animate().scaleX(1.5f);
                break;

            case 1:
                mImageView.animate().scaleX(1.0f);
                break;

            case 2:
                mImageView.animate().scaleY(1.5f);
                break;

            case 3:
                mImageView.animate().scaleY(1.0f);
                break;
        }

        mTranslationState++;
        if (mTranslationState == 4) {
            mTranslationState = 0;
        }
    }
}
