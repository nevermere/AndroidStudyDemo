package com.linyang.study.primary.android_thread.checker;

import java.util.concurrent.TimeUnit;

/**
 * 描述:内存检查服务
 * Created by fzJiang on 2019/01/17 17:10 星期四
 */
public class MemoryChecker extends BaseChecker {

    public MemoryChecker(String serviceName, StartUpTask startUpTask) {
        super(serviceName, startUpTask);
    }

    @Override
    public void verifyService() {
        long duration = (long) (Math.random() * 20);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
