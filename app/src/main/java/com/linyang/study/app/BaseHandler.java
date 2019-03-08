package com.linyang.study.app;

import android.os.Handler;
import android.os.Message;

import com.linyang.study.app.inter.IView;
import com.linyang.study.app.util.L;

import java.lang.ref.WeakReference;

import cn.feng.skin.manager.base.BaseFragment;


/**
 * 描述:消息接收器
 * Created by fzJiang on 2018-07-06
 */
public class BaseHandler extends Handler {

    private WeakReference<IView> mWeakReference;

    BaseHandler(IView view) {
        mWeakReference = new WeakReference<>(view);
    }

    @Override
    public void handleMessage(Message msg) {
        IView view = mWeakReference.get();
        if (view != null && ((view instanceof BaseActivity && !((BaseActivity) view).isFinishing())
                || (view instanceof BaseFragment && !((BaseFragment) view).isRemoving()))) {
            view.onHandlerReceive(msg);
        } else {
            L.i("----------不处理----------");
        }
    }
}
