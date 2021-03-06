package com.linyang.study.advanced.android_ipc.binder_poll;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2019-1-24
 */
public class BinderPoolService extends Service {

    private Binder mIBinderPool = new BinderPool.IBinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinderPool;
    }
}
