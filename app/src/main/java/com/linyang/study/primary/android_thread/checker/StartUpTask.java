package com.linyang.study.primary.android_thread.checker;

import android.text.TextUtils;

import com.linyang.study.app.util.LogUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 描述:开机自检任务
 * Created by fzJiang on 2019/01/18 8:39 星期五
 */
public class StartUpTask implements Runnable {

    private CountDownLatch mCountDownLatch;

    public StartUpTask(int count) {
        mCountDownLatch = new CountDownLatch(count);
    }

    public void finish(String serviceName, boolean isSuccess, String error) {
        LogUtil.i("任务:" + serviceName + "----" + (isSuccess ? "自检通过" : "自检不通过"));
        if (!TextUtils.isEmpty(error)) {
            LogUtil.i("发生错误：" + error);
        }
        if (mCountDownLatch != null) {
            mCountDownLatch.countDown();
            LogUtil.i("剩余自检任务:" + mCountDownLatch.getCount());
        }
    }

    @Override
    public void run() {
        try {
            LogUtil.i("开机自检任务开启，自检任务数目：" + mCountDownLatch.getCount());
            mCountDownLatch.await();
            LogUtil.i("开机自检任务结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
