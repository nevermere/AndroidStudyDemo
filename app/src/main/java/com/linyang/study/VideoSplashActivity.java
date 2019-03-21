package com.linyang.study;

import android.annotation.SuppressLint;
import android.net.Uri;

import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.ArmsUtils;
import com.linyang.study.primary.custom_view.widget.MyVideoView;

import butterknife.BindView;

/**
 * 描述:
 * Created by fzJiang on 2019/01/22 9:43 星期二
 */
public class VideoSplashActivity extends BaseActivity {

    @BindView(R.id.splash_view)
    MyVideoView splashView;

    @Override
    protected void onDestroy() {
        splashView.stopPlayback();
        splashView.clearAnimation();
        splashView.removeCallbacks(null);
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        // 设置为默认主题
        setTheme(R.style.AppTheme);
        return R.layout.activity_video_splash;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        // 设置播放加载路径
        splashView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video));
        // 播放
        splashView.start();
        // 播放完毕进入主页
        splashView.setOnCompletionListener(mp -> toMainView());
        splashView.setOnTouchListener((v, event) -> {
            toMainView();
            return true;
        });
    }

    @Override
    public void initData() {

    }

    private void toMainView() {
        ArmsUtils.startActivity(VideoSplashActivity.this, MainActivity.class);
        finish();
    }
}
