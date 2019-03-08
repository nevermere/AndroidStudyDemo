package com.linyang.study.primary.android_multi_thread.checker;

import java.util.concurrent.TimeUnit;

/**
 * 描述:磁盘检查服务
 * Created by fzJiang on 2019/01/17 17:10 星期四
 */
public class FileDiskChecker extends BaseChecker {


    public FileDiskChecker(String serviceName, StartUpTask startUpTask) {
        super(serviceName, startUpTask);
    }

    @Override
    public void verifyService() {
        long duration = (long) (Math.random() * 10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
