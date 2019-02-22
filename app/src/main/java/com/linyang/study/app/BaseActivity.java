package com.linyang.study.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;

import com.linyang.study.app.inter.IActivity;
import com.linyang.study.app.inter.IFragment;
import com.linyang.study.app.util.ActivityUtils;
import com.linyang.study.app.util.ToastUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.feng.skin.manager.base.BaseFragment;

/**
 * 描述:
 * Created by fzJiang on 2019/01/11 16:20 星期五
 */
public abstract class BaseActivity extends AppCompatActivity implements IActivity {

    private Context mContext;
    private Fragment mFragment;
    private Unbinder mUnBinder;
    private SparseArray<View> mViews;
    private Handler mHandler;
    private ActivityUtils mActivityUtils;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            mContext = this;
            // 注入ButterKnife
            this.mUnBinder = ButterKnife.bind(this);
            this.mViews = new SparseArray<>();
            // 初始化消息接收器
            if (userHandler()) {
                this.mHandler = new BaseHandler(this);
            }
            // 初始化Activity管理工具类并将当前界面入栈
            this.mActivityUtils = ActivityUtils.getInstance();
            this.mActivityUtils.pushActivity(this);
            // 初始化界面
            initView();
            // 初始化数据
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除ButterKnife绑定
        if (this.mUnBinder != null && this.mUnBinder != Unbinder.EMPTY) {
            this.mUnBinder.unbind();
        }
        this.mUnBinder = null;
        // 当前界面出栈
        if (this.mActivityUtils != null) {
            this.mActivityUtils.removeActivity(this);
        }
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends View> V findView(int viewId) {
        V view = (V) mViews.get(viewId);
        if (null == view) {
            view = findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    @Override
    public boolean userHandler() {
        return false;
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onHandlerReceive(Message msg) {

    }

    @Override
    public void onFragmentVisible(Fragment fragment) {
        if (fragment instanceof BaseFragment) {
            this.mFragment = fragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 返回按键处理
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 拦截返回键,解决部分机型不触发onBackPressed问题
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 判断触摸UP事件才会进行返回事件处理
            if (event.getAction() == KeyEvent.ACTION_UP) {
                onBackPressed();
            }
            // 只要是返回事件，直接返回true，表示消费掉
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        // 栈中无Fragment处理返回事件或者栈中已无可处理返回事件的Fragment,则交由Activity处理
        if (mFragment == null || mFragment instanceof IFragment
                && mFragment.isResumed()
                && mFragment.isVisible()
                && mFragment.getUserVisibleHint()
                && !((IFragment) mFragment).onBackPressed()) {
            // Fragment栈中无可返回,退出
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                exit();
            } else {
                // Fragment出栈
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    /**
     * 退出处理
     */
    private void exit() {
        // 当前界面非位于栈底
        if (mActivityUtils != null && mActivityUtils.getSize() > 1) {
            mActivityUtils.popCurrentActivity();
            return;
        }
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtil.showToast(getApplicationContext(), "再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
