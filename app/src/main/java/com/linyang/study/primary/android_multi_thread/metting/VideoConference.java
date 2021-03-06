package com.linyang.study.primary.android_multi_thread.metting;

import com.linyang.study.app.util.L;

import java.util.concurrent.CountDownLatch;

/**
 * Created by fzJiang on 2019/1/17.
 */
public class VideoConference implements Runnable {

    // CountDownLatch并不是用来保护共享资源同步访问的，而是用来控制并发线程等待的。
    // 并且CountDownLatch只允许进入一次，一旦内部计数器等于0，再调用这个方法将不起作用，如果还有第二次并发等待，你还得创建一个新的CountDownLatch
    private CountDownLatch mCountDownLatch;

    public VideoConference(int count) {
        this.mCountDownLatch = new CountDownLatch(count);
    }

    public void arrive(String name) {
        L.i(name + "号参会人员到达会议室");
        mCountDownLatch.countDown();
        L.i("剩余人数：" + mCountDownLatch.getCount());
    }

    @Override
    public void run() {
        try {
            L.i("召开会议，参会人数：" + mCountDownLatch.getCount());
            mCountDownLatch.await();
            L.i("参会人员到齐，会议开始");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
