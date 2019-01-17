package com.linyang.android_study_demo_01.android_thread.checker;

import com.linyang.android_study_demo_01.util.LogUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 描述:
 * Created by fzJiang on 2019/01/17 16:57 星期四
 */
public abstract class BaseChecker implements Runnable {

    private String mServiceName;
    private boolean isServiceStart;
    private CountDownLatch mCountDownLatch;

    BaseChecker(String serviceName, CountDownLatch countDownLatch) {
        mServiceName = serviceName;
        mCountDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            LogUtil.i("开启检查任务：" + mServiceName);

            verifyService();
            isServiceStart = true;

            LogUtil.i(mServiceName + " 检查通过");
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            isServiceStart = false;

            LogUtil.i(mServiceName + " 检查不通过");

        } finally {
            if (mCountDownLatch != null) {
                mCountDownLatch.countDown();
            }
        }
    }

    public String getServiceName() {
        return mServiceName;
    }

    public boolean isServiceStart() {
        return isServiceStart;
    }

    public abstract void verifyService();
}
