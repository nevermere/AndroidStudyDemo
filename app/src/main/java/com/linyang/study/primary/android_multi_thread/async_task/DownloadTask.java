package com.linyang.study.primary.android_multi_thread.async_task;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;


/**
 * 描述:
 * Created by fzJiang on 2019/03/08 9:56 星期五
 */
public class DownloadTask extends AsyncTask<Object, Integer, Integer> {

    private static final int DOWN_LOAD_SUCCESS = 0;
    private static final int DOWN_LOAD_FAILURE = 1;
    private static final int DOWN_LOAD_CANCELED = 2;

    private String mDownloadUrl;
    private DownloadListener mDownloadListener;

    /**
     * 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 这里的Object参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法在后台线程运行，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress(Progress…)方法触发onProgressUpdate(),对UI进行操作
     */
    @Override
    protected Integer doInBackground(Object... objects) {
        if (objects.length > 1) {
            mDownloadUrl = (String) objects[0];
            mDownloadListener = (DownloadListener) objects[1];

            // 模拟耗时任务
            int value = 0;
            while (value < 100) {

                // 用户主动取消任务
                if (isCancelled()) {
                    return DOWN_LOAD_SUCCESS;
                }

                value += 1;
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(value);
            }
            return DOWN_LOAD_SUCCESS;
        }
        return DOWN_LOAD_FAILURE;
    }

    /**
     * 这里的Integer参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        // 在主线程执行，用于显示任务执行的进度。
        if (mDownloadListener != null) {
            mDownloadListener.onDownloading(values[0]);
        }
    }

    /**
     * 相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。 此方法在主线程执行，任务执行的结果作为此方法的参数返回
     */
    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        switch (result) {
            case DOWN_LOAD_SUCCESS:
                if (mDownloadListener != null) {
                    mDownloadListener.onDownloadSuccess();
                }
                break;

            case DOWN_LOAD_FAILURE:
                if (mDownloadListener != null) {
                    mDownloadListener.onDownloadFailed("发生错误，下载失败");
                }
                break;

            case DOWN_LOAD_CANCELED:
                if (mDownloadListener != null) {
                    mDownloadListener.onDownloadCancel();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mDownloadListener != null) {
            mDownloadListener.onDownloadCancel();
        }
    }
}
