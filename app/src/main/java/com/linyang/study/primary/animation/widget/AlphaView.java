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
public class AlphaView extends RelativeLayout implements View.OnClickListener {

    private ImageView mImageView;

    private int mTranslationState = 0;

    public AlphaView(Context context) {
        super(context);
    }

    public AlphaView(Context context, AttributeSet attrs) {
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
                mImageView.animate().alpha(0);
                break;

            case 1:
                mImageView.animate().alpha(1);
                break;
        }

        mTranslationState++;
        if (mTranslationState == 2) {
            mTranslationState = 0;
        }
    }
}
