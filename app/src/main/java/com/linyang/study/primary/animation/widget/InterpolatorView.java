package com.linyang.study.primary.animation.widget;

import android.content.Context;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linyang.study.R;
import com.linyang.study.app.util.DisplayUtil;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

/**
 * 描述:
 * Created by fzJiang on 2018/12/19 14:42 星期三
 */
public class InterpolatorView extends LinearLayout implements View.OnClickListener {

    private AppCompatSpinner mSpinner;
    private ImageView mImageView;

    private Interpolator[] mInterPolators = new Interpolator[13];

    {
        Path interpolatorPath = new Path();
        interpolatorPath.lineTo(0.25f, 0.25f);
        interpolatorPath.moveTo(0.25f, 1.5f);
        interpolatorPath.lineTo(1, 1);
        mInterPolators[0] = new AccelerateDecelerateInterpolator();
        mInterPolators[1] = new LinearInterpolator();
        mInterPolators[2] = new AccelerateInterpolator();
        mInterPolators[3] = new DecelerateInterpolator();
        mInterPolators[4] = new AnticipateInterpolator();
        mInterPolators[5] = new OvershootInterpolator();
        mInterPolators[6] = new AnticipateOvershootInterpolator();
        mInterPolators[7] = new BounceInterpolator();
        mInterPolators[8] = new CycleInterpolator(0.5f);
        mInterPolators[9] = PathInterpolatorCompat.create(interpolatorPath);
        mInterPolators[10] = new FastOutLinearInInterpolator();
        mInterPolators[11] = new FastOutSlowInInterpolator();
        mInterPolators[12] = new LinearOutSlowInInterpolator();
    }

    public InterpolatorView(Context context) {
        super(context);
    }

    public InterpolatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mSpinner = findViewById(R.id.interpolator_spinner);
        mImageView = findViewById(R.id.iv_image);

        Button startBtn = findViewById(R.id.bt_start);
        startBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        mImageView.animate()
                .translationX(DisplayUtil.dip2px(getContext(), 200))
                .setDuration(600)
                .setInterpolator(mInterPolators[mSpinner.getSelectedItemPosition()])
                .withEndAction(() -> mImageView.postDelayed(() -> mImageView.setTranslationX(0), 500));
    }
}
