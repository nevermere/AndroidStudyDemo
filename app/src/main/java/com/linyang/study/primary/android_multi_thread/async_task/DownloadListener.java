package com.linyang.study.primary.android_multi_thread.async_task;

/**
 * 描述:
 * Created by fzJiang on 2019/03/08 9:58 星期五
 */
public interface DownloadListener {

    void onDownloading(int progress);

    void onDownloadSuccess();

    void onDownloadFailed(String error);

    void onDownloadCancel();
}
