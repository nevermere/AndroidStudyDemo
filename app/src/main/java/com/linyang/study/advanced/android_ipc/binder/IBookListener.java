package com.linyang.study.advanced.android_ipc.binder;

import android.os.IInterface;

/**
 * 描述:
 * Created by fzJiang on 2019-1-24
 */
public interface IBookListener extends IInterface {

    void onNewBookArrived(Book book);
}
