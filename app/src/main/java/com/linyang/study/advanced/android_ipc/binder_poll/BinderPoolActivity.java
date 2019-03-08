package com.linyang.study.advanced.android_ipc.binder_poll;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.linyang.binder_poll.IComputer;
import com.linyang.binder_poll.ISecurityCenter;
import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.L;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2018-11-08
 */
public class BinderPoolActivity extends BaseActivity {

    @BindView(R.id.btn_init_pool)
    AppCompatButton mBtnInitPool;
    @BindView(R.id.btn_con_1)
    AppCompatButton mBtnCon1;
    @BindView(R.id.btn_con_2)
    AppCompatButton mBtnCon2;

    private BinderPool mBinderPool;
    private ISecurityCenter mISecurityCenter;
    private IComputer mIComputer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinderPool != null) {
            mBinderPool.destroy();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_binder_pool;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_init_pool, R.id.btn_con_1, R.id.btn_con_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_init_pool:
                new InitThread().start();
                break;

            case R.id.btn_con_1:
                IBinder binder = mBinderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
                if (binder != null) {
                    mISecurityCenter = ISecurityCenterImpl.asInterface(binder);
                }

                if (mISecurityCenter != null) {
                    try {
                        String encrypt = mISecurityCenter.encrypt("hello world");
                        L.i("encrypt:" + encrypt);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_con_2:
                IBinder binder2 = mBinderPool.queryBinder(BinderPool.BINDER_COMPUTER);
                if (binder2 != null) {
                    mIComputer = IComputerImpl.asInterface(binder2);
                }

                if (mIComputer != null) {
                    try {
                        int sum = mIComputer.add(1, 2);
                        L.i("sum:" + sum);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    private class InitThread extends Thread {

        @Override
        public void run() {
            super.run();
            mBinderPool = BinderPool.getInstance(BinderPoolActivity.this);
            L.i("初始化Binder池:" + (mBinderPool == null ? "失败" : "成功"));
        }
    }
}
