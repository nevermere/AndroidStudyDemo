package com.linyang.study.primary.android_multi_thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.L;

import java.util.concurrent.CountDownLatch;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2019/03/07 13:12 星期四
 */
public class HandlerTestActivity extends BaseActivity {

    @BindView(R.id.btn_01)
    AppCompatButton btn01;
    @BindView(R.id.btn_02)
    AppCompatButton btn02;
    @BindView(R.id.btn_03)
    AppCompatButton btn03;

    private MyHandler mHandler;// 主线程的Handler
    private MyHandler mThreadHandler;// 子线程的Handler

    @Override

    public int getLayoutId() {
        return R.layout.activity_handler_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_01, R.id.btn_02, R.id.btn_03})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_01:// 子线程——>主线程
                if (mHandler == null) {
                    mHandler = new MyHandler();
                }
                // 从子线程通过主线程创建的Handler，发送消息到主线程
                new Thread() {

                    @Override
                    public void run() {
                        mHandler.sendMessage(mHandler.obtainMessage(0x01, getId() + ":" + getName()));
                    }
                }.start();
                break;

            case R.id.btn_02:// 子线程——>子线程
                // 在子线程中创建一个子线程的Handler
                new Thread() {

                    @Override
                    public void run() {
                        Looper.prepare();
                        if (mThreadHandler == null) {
                            mThreadHandler = new MyHandler();
                        }
                        Looper.loop();
                    }
                }.start();

                // 在另一子线程中通过上一线程创建的Handler，发送消息到该子线程
                new Thread() {

                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        mThreadHandler.sendMessage(mThreadHandler.obtainMessage(0x02, getId() + ":" + getName()));
                    }
                }.start();
                break;

            case R.id.btn_03:// 控制子线程并发,按顺序执行
                final String[] printData = new String[]{"H", "E", "L", "L", "O", " ", "W", "O", "R", "L", "D"};

                for (String data : printData) {
                    // 建立子线程控制管理
                    TaskManager taskManager = new TaskManager(5);
                    new Thread(taskManager).start();

                    // 新建子线程
                    Thread[] threads = new Thread[5];
                    for (int j = 0; j < threads.length; j++) {
                        threads[j] = new Thread(new PrintTask(data, taskManager));
                    }
                    for (Thread t : threads) {
                        t.start();
                    }
                }
                break;
        }
    }

    private static final class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    L.i("子线程——>主线程:" + msg.obj.toString());
                    break;

                case 0x02:
                    L.i("子线程——>子线程:" + msg.obj.toString());
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 通过CountDownLatch，控制线程并发
     */
    private static final class TaskManager implements Runnable {

        private CountDownLatch mCountDownLatch;

        public TaskManager(int count) {
            this.mCountDownLatch = new CountDownLatch(count);
        }

        public void printFinish() {
            mCountDownLatch.countDown();
        }

        @Override
        public void run() {
            try {
                mCountDownLatch.await();
                L.i("打印服务完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单个打印任务
     */
    private static final class PrintTask implements Runnable {

        private String data;
        private TaskManager mTaskManager;

        public PrintTask(String data, TaskManager taskManager) {
            this.data = data;
            this.mTaskManager = taskManager;
        }

        @Override
        public void run() {
            L.i("正在打印：" + data);
            if (mTaskManager != null) {
                mTaskManager.printFinish();
            }
        }
    }
}
