package com.linyang.android_study_demo_01.android_thread;

import android.view.View;

import com.linyang.android_study_demo_01.R;
import com.linyang.android_study_demo_01.android_thread.checker.BaseChecker;
import com.linyang.android_study_demo_01.android_thread.checker.DriveChecker;
import com.linyang.android_study_demo_01.android_thread.checker.FileDiskChecker;
import com.linyang.android_study_demo_01.android_thread.checker.MemoryChecker;
import com.linyang.android_study_demo_01.android_thread.metting.Participant;
import com.linyang.android_study_demo_01.android_thread.metting.VideoConference;
import com.linyang.android_study_demo_01.app.BaseActivity;
import com.linyang.android_study_demo_01.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:CountDownLatch,控制多线程并发等待
 * Created by fzJiang on 2019/01/17 16:49 星期四
 */
public class CountDownLatchActivity extends BaseActivity {

    @BindView(R.id.bt_meeting)
    AppCompatButton btMeeting;
    @BindView(R.id.bt_check)
    AppCompatButton btCheck;

    @Override
    public int getLayoutId() {
        return R.layout.activity_count_down_latch;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.bt_meeting, R.id.bt_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_meeting:
                // 模拟10人会议
                VideoConference videoConference = new VideoConference(10);
                Thread viewThread = new Thread(videoConference);
                viewThread.start();

                // 模拟参会人员
                Thread[] threads = new Thread[10];
                for (int i = 0; i < threads.length; i++) {
                    threads[i] = new Thread(new Participant("" + (i + 1), videoConference));
                }

                for (Thread t : threads) {
                    t.start();
                }
                break;

            case R.id.bt_check:
                if (startUpCheck()) {
                    LogUtil.i("开机自检完成");
                } else {
                    LogUtil.i("开机自检失败");
                }
                break;
        }
    }


    /**
     * 开机自检
     *
     * @return
     */
    private boolean startUpCheck() {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(3);

            List<BaseChecker> checkerList = new ArrayList<>();
            checkerList.add(new DriveChecker("驱动检查服务", countDownLatch));
            checkerList.add(new FileDiskChecker("磁盘检查服务", countDownLatch));
            checkerList.add(new MemoryChecker("内存检查服务", countDownLatch));

            Executor executor = Executors.newFixedThreadPool(3);
            for (BaseChecker checker : checkerList) {
                executor.execute(checker);
            }

            countDownLatch.await();

            for (BaseChecker checker : checkerList) {
                if (!checker.isServiceStart()) {
                    return false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
