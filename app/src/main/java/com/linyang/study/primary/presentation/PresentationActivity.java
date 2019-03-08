package com.linyang.study.primary.presentation;

import android.os.Bundle;

import com.linyang.study.R;
import com.linyang.study.app.util.L;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2019/01/08 9:13 星期二
 */
public class PresentationActivity extends AppCompatActivity implements UserPresentationCallback {

    @BindView(R.id.bt_open)
    AppCompatButton btOpen;

    private PresentationUtil mUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_open)
    public void onViewClicked() {
        mUtil = PresentationUtil.getInstance(this);
        mUtil.setCallback(this);
        mUtil.setUserContent("发送消息到第二屏幕");
    }

    @Override
    public void onBackPressed() {
        onDestroy();
        // 完全退出应用，取消双屏异显
        finish();

//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
//        System.exit(0);
    }

    @Override
    public void onReceive(String rev) {
        L.i("接收到第二屏幕回复消息");
    }
}
