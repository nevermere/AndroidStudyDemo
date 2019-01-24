package com.linyang.study.primary.custom_view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.linyang.study.R;
import com.linyang.study.primary.custom_view.widget.LeafLoadingView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2018-10-25
 */
public class LeafLoadingActivity extends AppCompatActivity {

    private static final int REFRESH_PROGRESS = 0x10;


    @BindView(R.id.leaf_loading)
    LeafLoadingView mLeafLoading;
    //    @BindView(R.id.fan_pic)
//    ImageView mFanPic;
    @BindView(R.id.text_ampair)
    TextView mTextAmpair;
    @BindView(R.id.seekBar_ampair)
    SeekBar mSeekBarAmpair;
    @BindView(R.id.text_disparity)
    TextView mTextDisparity;
    @BindView(R.id.seekBar_distance)
    SeekBar mSeekBarDistance;
    @BindView(R.id.text_float_time)
    TextView mTextFloatTime;
    @BindView(R.id.seekBar_float_time)
    SeekBar mSeekBarFloatTime;
    @BindView(R.id.text_rotate_time)
    TextView mTextRotateTime;
    @BindView(R.id.seekBar_rotate_time)
    SeekBar mSeekBarRotateTime;
    @BindView(R.id.clear_progress)
    Button mClearProgress;
    @BindView(R.id.add_progress)
    Button mAddProgress;
    @BindView(R.id.text_progress)
    TextView mTextProgress;

    private MyHandler mHandler = new MyHandler();
    private int mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf_loading);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.clear_progress, R.id.add_progress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_progress:
                mLeafLoading.setProgress(0);
                mHandler.removeCallbacksAndMessages(null);
                mProgress = 0;
                break;
            case R.id.add_progress:
                mHandler.sendEmptyMessage(REFRESH_PROGRESS);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_PROGRESS:
                    if (mProgress < 40) {
                        mProgress += 10;
                        // 随机800ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(800));
                        mLeafLoading.setProgress(mProgress);
                    } else {
                        mProgress += 5;
                        // 随机1200ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(1200));
                        mLeafLoading.setProgress(mProgress);

                    }
                    break;

                default:
                    break;
            }
        }
    }
}
