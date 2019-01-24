package com.linyang.study.other.jetpack;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * 描述:
 * Created by fzJiang on 2018/11/29 下午 5:24 星期四
 */
public class MyLifeCycleObserver implements LifecycleObserver {

    private static final String TAG = MyLifeCycleObserver.class.getSimpleName();

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onViewCreate() {
        Log.i(TAG, "------------onViewCreate------------");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onViewStart() {
        Log.i(TAG, "------------onViewStart------------");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onViewResume() {
        Log.i(TAG, "------------onViewResume------------");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onViewPause() {
        Log.i(TAG, "------------onViewPause------------");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onViewStop() {
        Log.i(TAG, "------------onViewStop------------");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onViewDestroy() {
        Log.i(TAG, "------------onViewDestroy------------");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onViewAny() {
        Log.i(TAG, "------------onViewAny------------");
    }

}
