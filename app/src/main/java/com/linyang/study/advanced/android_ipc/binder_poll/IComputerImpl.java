package com.linyang.study.advanced.android_ipc.binder_poll;


import com.linyang.binder_poll.IComputer;

/**
 * 描述:
 * Created by fzJiang on 2019-1-24
 */
public class IComputerImpl extends IComputer.Stub {

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
