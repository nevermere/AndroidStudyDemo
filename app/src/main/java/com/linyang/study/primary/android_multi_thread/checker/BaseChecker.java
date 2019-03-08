package com.linyang.study.primary.android_multi_thread.checker;

/**
 * 描述:
 * Created by fzJiang on 2019/01/17 16:57 星期四
 */
public abstract class BaseChecker implements Runnable {

    private String mServiceName;
    private StartUpTask mStartUpTask;

    public BaseChecker(String serviceName, StartUpTask startUpTask) {
        mServiceName = serviceName;
        mStartUpTask = startUpTask;
    }

    @Override
    public void run() {
        boolean isServiceSuccess;
        String error = null;
        try {
            verifyService();
            isServiceSuccess = true;
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            error = t.getMessage();
            isServiceSuccess = false;
        }
        if (mStartUpTask != null) {
            mStartUpTask.finish(mServiceName, isServiceSuccess, error);
        }
    }

    public abstract void verifyService();
}
