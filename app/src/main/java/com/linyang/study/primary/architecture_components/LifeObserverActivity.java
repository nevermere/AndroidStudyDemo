package com.linyang.study.primary.architecture_components;

import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 * 描述:
 * Created by fzJiang on 2019/02/21 15:48 星期四
 */
public class LifeObserverActivity extends BaseActivity implements LifecycleOwner {

    private LifecycleRegistry mLifecycleRegistry;
    private ActivityLifeObserver mActivityLifeObserver;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if (mLifecycleRegistry != null) {
            //  移除观察者
            mLifecycleRegistry.removeObserver(mActivityLifeObserver);
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lifeobserver;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        mLifecycleRegistry = new LifecycleRegistry(this);
        mActivityLifeObserver = new ActivityLifeObserver();
        // 注册需要监听的 Observer
        mLifecycleRegistry.addObserver(mActivityLifeObserver);
        return mLifecycleRegistry;
    }
}
