package com.linyang.study.primary.animation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.linyang.study.R;
import com.linyang.study.app.util.DisplayUtil;

import androidx.appcompat.widget.AppCompatSeekBar;

/**
 * 描述:
 * Created by fzJiang on 2018/12/19 14:42 星期三
 */
public class DurationView extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TextView mDurationValue;
    private ImageView mImageView;

    private int mDuration = 300;
    private boolean mState;

    public DurationView(Context context) {
        super(context);
    }

    public DurationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mDurationValue = findViewById(R.id.tv_duration_value);
        mDurationValue.setText(getContext().getString(R.string.ms_with_value, mDuration));

        AppCompatSeekBar durationSeekBar = findViewById(R.id.sb_duration);
        durationSeekBar.setMax(10);
        durationSeekBar.setProgress(1);
        durationSeekBar.setOnSeekBarChangeListener(this);

        mImageView = findViewById(R.id.iv_image);

        Button startBtn = findViewById(R.id.bt_start);
        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mState) {
            mImageView.animate().translationX(DisplayUtil.dip2px(getContext(), 200)).setDuration(mDuration);
        } else {
            mImageView.animate().translationX(0).setDuration(mDuration);
        }
        mState = !mState;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        mDuration = progress * 300;
        mDurationValue.setText(getContext().getString(R.string.ms_with_value, mDuration));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
