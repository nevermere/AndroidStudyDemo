package com.linyang.android_study_demo_01;

import android.net.Uri;

import com.linyang.android_study_demo_01.app.BaseActivity;
import com.linyang.android_study_demo_01.widget.ArmsUtils;
import com.linyang.android_study_demo_01.widget.MyVideoView;

import butterknife.BindView;

/**
 * 描述:
 * Created by fzJiang on 2019/01/22 9:43 星期二
 */
public class VideoSplashActivity extends BaseActivity {

    @BindView(R.id.splash_view)
    MyVideoView splashView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_splash;
    }

    @Override
    public void initView() {
        // 设置播放加载路径
        splashView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video));
        // 播放
        splashView.start();
        // 播放完毕进入主页
        splashView.setOnCompletionListener(mp -> {
            ArmsUtils.startActivity(VideoSplashActivity.this, MainActivity.class);
            finish();
        });
    }

    @Override
    public void initData() {

    }
}
