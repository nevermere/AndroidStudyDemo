package com.linyang.study.app.inter;


import android.app.Activity;

/**
 * 描述:封装了BaseFragment一些基本方法
 * Created by fzJiang on 2018-02-06
 */
public interface IFragment extends IView {

    /**
     * 获取当前Fragment所依赖的Activity
     *
     * @return 所依赖的Activity
     */
    Activity getAttachActivity();

    /**
     * 所有继承的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    boolean onBackPressed();
}
