package com.linyang.study.primary.android_multi_thread.metting;


import java.util.concurrent.TimeUnit;

/**
 * 描述:
 * Created by fzJiang on 2019/01/17 15:18 星期四
 */
public class Participant implements Runnable {

    private String mName;
    private VideoConference mVideoConference;

    public Participant(String name, VideoConference videoConference) {
        mName = name;
        mVideoConference = videoConference;
    }

    @Override
    public void run() {
        long duration = (long) (Math.random() * 10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mVideoConference != null) {
            mVideoConference.arrive(mName);
        }
    }
}
