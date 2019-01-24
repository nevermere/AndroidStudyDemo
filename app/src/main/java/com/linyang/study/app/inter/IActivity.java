package com.linyang.study.app.inter;

import androidx.fragment.app.Fragment;

/**
 * 描述:封装了BaseActivity一些基本方法
 * Created by fzJiang on 2018-02-06
 */
public interface IActivity extends IView {

    /**
     * 使用Fragment时，设置当前栈顶显示界面
     *
     * @param fragment 当前显示Fragment
     */
    void onFragmentVisible(Fragment fragment);
}
