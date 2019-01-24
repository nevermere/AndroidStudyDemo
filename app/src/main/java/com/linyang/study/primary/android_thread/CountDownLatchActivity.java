package com.linyang.study.primary.android_thread;

import android.view.View;

import com.linyang.study.R;
import com.linyang.study.primary.android_thread.checker.BaseChecker;
import com.linyang.study.primary.android_thread.checker.DriveChecker;
import com.linyang.study.primary.android_thread.checker.FileDiskChecker;
import com.linyang.study.primary.android_thread.checker.MemoryChecker;
import com.linyang.study.primary.android_thread.checker.StartUpTask;
import com.linyang.study.primary.android_thread.metting.Participant;
import com.linyang.study.primary.android_thread.metting.VideoConference;
import com.linyang.study.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

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
                startUpCheck();
                break;
        }
    }


    /**
     * 开机自检
     *
     * @return
     */
    private void startUpCheck() {
        StartUpTask startUpTask = new StartUpTask(3);
        Thread startUpThread = new Thread(startUpTask);
        startUpThread.start();

        List<BaseChecker> checkerList = new ArrayList<>();
        checkerList.add(new DriveChecker("驱动检查服务", startUpTask));
        checkerList.add(new FileDiskChecker("磁盘检查服务", startUpTask));
        checkerList.add(new MemoryChecker("内存检查服务", startUpTask));

        Thread[] threads = new Thread[checkerList.size()];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(checkerList.get(i));
        }

        for (Thread t : threads) {
            t.start();
        }
    }
}
