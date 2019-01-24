package com.linyang.study.app.inter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

/**
 * 描述:用户页面,操作页面，对应Activity,Fragment
 * Created by fzJiang on 2018-07-05
 */
public interface IView {

    /**
     * 获取上下文Context
     *
     * @return Context
     */
    Context getAppContext();

    /**
     * 获取界面LayoutResId
     *
     * @return LayoutResId
     */
    @LayoutRes
    int getLayoutId();

    /**
     * 初始化界面
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 根据id获得控件
     * 需要调用在initView()之后,否则会出现NullPointerException
     *
     * @param viewId 控件id
     * @return 控件
     */
    <V extends View> V findView(@IdRes int viewId);

    /**
     * 是否初始化消息处理器
     *
     * @return 默认为false
     */
    boolean userHandler();

    /**
     * 获取消息处理器
     *
     * @return {@link com.linyang.study.app.BaseHandler}
     */
    Handler getHandler();

    /**
     * Message消息处理
     *
     * @param msg 消息
     */
    void onHandlerReceive(Message msg);
}
