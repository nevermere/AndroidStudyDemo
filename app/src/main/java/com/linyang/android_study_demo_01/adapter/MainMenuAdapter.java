package com.linyang.android_study_demo_01.adapter;

import android.content.Context;
import android.view.View;

import com.linyang.android_study_demo_01.R;
import com.linyang.android_study_demo_01.holder.BaseHolder;
import com.linyang.android_study_demo_01.holder.MainMenuHolder;

import java.util.List;

/**
 * 描述:
 * Created by fzJiang on 2018/12/17 13:55 星期一
 */
public class MainMenuAdapter extends BaseRecycleAdapter<String> {

    public MainMenuAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    public BaseHolder<String> getHolder(View v, int viewType) {
        return new MainMenuHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_menu_item;
    }
}
