package com.linyang.study.primary.android_multi_thread.async_task;

import android.view.View;

import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.L;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2019/03/08 9:25 星期五
 */
public class AsyncTaskActivity extends BaseActivity {

    public static final String URL = "http://app-global.pgyer.com/dae456b61b7b2db45db1eaebd039dd09.apk?attname=NLY1220%E5%8D%95%E7%9B%B8%E7%89%A9%E8%81%94%E7%BD%91%E7%94%B5%E8%A1%A8%E7%BB%B4%E6%8A%A4%E5%B7%A5%E5%85%B7%28v1.0.2%29.apk&sign=015603ef30202fa57964d1a0de10ed15&t=5c81ce78";

    @BindView(R.id.bt_start_download)
    AppCompatButton btStartDownload;
    @BindView(R.id.bt_cancel_download)
    AppCompatButton btCancelDownload;
    @BindView(R.id.download_progress)
    ContentLoadingProgressBar downloadProgress;

    private DownloadTask mDownloadTask;

    @Override
    protected void onDestroy() {
        if (mDownloadTask != null && !mDownloadTask.isCancelled()) {
            mDownloadTask.cancel(true);
            mDownloadTask = null;
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_async_task;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.bt_start_download, R.id.bt_cancel_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_start_download:
                if (mDownloadTask == null || mDownloadTask.isCancelled()) {
                    mDownloadTask = new DownloadTask();
                    mDownloadTask.execute(URL, new DownloadListener() {

                        @Override
                        public void onDownloading(int progress) {
                            downloadProgress.setProgress(progress);
                        }

                        @Override
                        public void onDownloadSuccess() {
                            L.i("下载成功");
                        }

                        @Override
                        public void onDownloadFailed(String error) {
                            L.i("下载失败：" + error);
                        }

                        @Override
                        public void onDownloadCancel() {
                            L.i("下载任务取消");
                            downloadProgress.setProgress(0);
                        }
                    });
                }
                break;

            case R.id.bt_cancel_download:
                mDownloadTask.cancel(true);
                break;
        }
    }
}
