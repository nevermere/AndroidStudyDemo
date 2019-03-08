package com.linyang.study.advanced.android_ipc.binder_poll;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.linyang.binder_poll.IBinderPool;
import com.linyang.study.app.util.L;

import java.util.concurrent.CountDownLatch;

/**
 * 描述:Binder连接池
 * Created by fzJiang on 2019-1-24
 */
public class BinderPool {

    public static final int BINDER_SECURITY_CENTER = 1;
    public static final int BINDER_COMPUTER = 1 >> 1;

    @SuppressLint("StaticFieldLeak")
    private static volatile BinderPool mBinderPool;

    private Context mContext;
    private IBinderPool mIBinderPool;
    private CountDownLatch mCountDownLatch;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.i("---------onServiceConnected---------");

            try {
                mIBinderPool = IBinderPool.Stub.asInterface(service);
                // 设置死亡代理
                mIBinderPool.asBinder().linkToDeath(mDeathRecipient, 0);
                mCountDownLatch.countDown();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBinderPool = null;
            L.i("---------onServiceDisconnected---------");
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {

        @Override
        public void binderDied() {
            L.i("---------binderDied---------");

            if (mIBinderPool == null) {
                return;
            }
            mIBinderPool.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mIBinderPool = null;
            connectBinderPoolService();
        }
    };


    private BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        synchronized (BinderPool.class) {
            if (mBinderPool == null) {
                mBinderPool = new BinderPool(context);
            }
        }
        return mBinderPool;
    }

    private synchronized void connectBinderPoolService() {
        try {
            mCountDownLatch = new CountDownLatch(1);
            Intent intent = new Intent(mContext, BinderPoolService.class);
            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int bindCode) {
        IBinder binder = null;
        if (mIBinderPool != null) {
            try {
                binder = mIBinderPool.queryBinder(bindCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }

    public static class IBinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_SECURITY_CENTER: {
                    binder = new ISecurityCenterImpl();
                    break;
                }

                case BINDER_COMPUTER: {
                    binder = new IComputerImpl();
                    break;
                }

                default:
                    break;
            }
            return binder;
        }
    }

    public void destroy() {
        if (mContext != null && mConnection != null) {
            mContext.unbindService(mConnection);
            mConnection = null;
        }
        mCountDownLatch = null;
        mIBinderPool = null;
    }
}
