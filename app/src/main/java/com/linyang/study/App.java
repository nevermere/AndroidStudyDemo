package com.linyang.study;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.Stack;

import androidx.multidex.MultiDex;

/**
 * 描述:
 * Created by fzJiang on 2018/12/19 14:19 星期三
 */
public class App extends Application {

    private static App mApp;
    private Stack<Activity> mStack;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mStack = new Stack<>();
        // 注册生命周期监听
        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }

    public static App getApp() {
        return mApp;
    }

    private class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mStack.add(activity);
//            L.i("onActivityCreated:" + activity.toString());
        }

        @Override
        public void onActivityStarted(Activity activity) {
//            L.i("onActivityStarted:" + activity.toString());
        }

        @Override
        public void onActivityResumed(Activity activity) {
//            L.i("onActivityResumed:" + activity.toString());
        }

        @Override
        public void onActivityPaused(Activity activity) {
//            L.i("onActivityPaused:" + activity.toString());
        }

        @Override
        public void onActivityStopped(Activity activity) {
//            L.i("onActivityStopped:" + activity.toString());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//            L.i("onActivitySaveInstanceState:" + activity.toString());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
//            L.i("onActivityDestroyed:" + activity.toString());
            mStack.remove(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        // 返回栈顶Activity
        return mStack.lastElement();
    }
}
