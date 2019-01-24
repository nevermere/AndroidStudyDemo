package com.linyang.study.primary.animation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linyang.study.R;
import com.linyang.study.app.util.DisplayUtil;

/**
 * 描述:
 * Created by fzJiang on 2018/12/19 14:42 星期三
 */
public class MultiPropertiesView extends RelativeLayout implements View.OnClickListener {

    private ImageView mImageView;

    private boolean mAnimated;

    public MultiPropertiesView(Context context) {
        super(context);
    }

    public MultiPropertiesView(Context context, AttributeSet attrs) {
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
        if (mAnimated) {
            mImageView.animate()
                    .translationX(0)
                    .rotation(0)
                    .scaleX(0)
                    .scaleY(0)
                    .alpha(0);
        } else {
            mImageView.animate()
                    .translationX(DisplayUtil.dip2px(getContext(), 200))
                    .rotation(360)
                    .scaleX(1)
                    .scaleY(1)
                    .alpha(1);
        }
        mAnimated = !mAnimated;
    }
}
