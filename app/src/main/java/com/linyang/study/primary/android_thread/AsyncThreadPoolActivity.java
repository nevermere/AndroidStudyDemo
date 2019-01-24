package com.linyang.study.primary.android_thread;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;

import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.LogUtil;
import com.linyang.study.app.util.ArmsUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2019/01/16 10:43 星期三
 */
public class AsyncThreadPoolActivity extends BaseActivity {

    public static final int MSG_UPDATE_INFO = 1;

    @BindView(R.id.bt_thread)
    AppCompatButton btThread;
    @BindView(R.id.bt_runnable)
    AppCompatButton btRunnable;
    @BindView(R.id.bt_callable)
    AppCompatButton btCallable;
    @BindView(R.id.bt_handler_thread)
    AppCompatButton btHandlerThread;
    @BindView(R.id.bt_count_down_latch)
    AppCompatButton btCountDownLatch;

    private HandlerThread mHandlerThread;
    private Handler mHandler;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
        if (mHandler != null) {
            mHandler.removeMessages(MSG_UPDATE_INFO);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_async_thread_pool;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.bt_thread, R.id.bt_runnable, R.id.bt_callable, R.id.bt_handler_thread, R.id.bt_count_down_latch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_thread:
                new Thread() {

                    @Override
                    public void run() {
                        super.run();
                        LogUtil.i("run Thread 运行线程:" + Thread.currentThread().getName());
                    }
                }.run();

                new Thread() {

                    @Override
                    public void run() {
                        super.run();
                        LogUtil.i("start Thread 运行线程:" + Thread.currentThread().getName());
                    }
                }.start();
                break;

            case R.id.bt_runnable:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        LogUtil.i("start Runnable 运行线程:" + Thread.currentThread().getName());
                    }
                }).run();

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        LogUtil.i("run Runnable 运行线程:" + Thread.currentThread().getName());
                    }
                }).start();
                break;

            case R.id.bt_callable:
                // 串行
                new MyAsyncTask("MyAsyncTask 01").execute(0);
                new MyAsyncTask("MyAsyncTask 02").execute(1);
                new MyAsyncTask("MyAsyncTask 03").execute(2);

                // 并发
                new MyAsyncTask("MyAsyncTask 04").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
                new MyAsyncTask("MyAsyncTask 05").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
                new MyAsyncTask("MyAsyncTask 06").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2);
                break;

            case R.id.bt_handler_thread:
                if (mHandlerThread == null) {
                    mHandlerThread = new HandlerThread("HandlerThread");
                    mHandlerThread.start();
                }

                if (mHandler == null) {
                    mHandler = new Handler(mHandlerThread.getLooper()) {

                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);

                            if (MSG_UPDATE_INFO == msg.what) {
                                mHandler.removeMessages(MSG_UPDATE_INFO);
                                onHandlerReceive(msg);
                            }
                        }
                    };
                }

                mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_INFO, "fff you"));
                break;

            case R.id.bt_count_down_latch: // 控制多线程并发等待
                ArmsUtils.startActivity(this, CountDownLatchActivity.class);
                break;
        }
    }

    @Override
    public void onHandlerReceive(Message msg) {
        super.onHandlerReceive(msg);
        LogUtil.i("HandlerThread 处理消息:" + msg.obj.toString());

    }

    private static class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {

        private String name;

        MyAsyncTask(String name) {
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.CHINA);
            LogUtil.i(name + "------------" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
