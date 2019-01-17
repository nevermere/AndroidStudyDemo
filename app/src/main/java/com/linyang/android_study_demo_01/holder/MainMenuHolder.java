package com.linyang.android_study_demo_01.holder;

import android.view.View;
import android.widget.TextView;

import com.linyang.android_study_demo_01.R;

import butterknife.BindView;

/**
 * 描述:
 * Created by fzJiang on 2018/12/17 13:57 星期一
 */
public class MainMenuHolder extends BaseHolder<String> {

    @BindView(R.id.item_name)
    TextView itemName;

    public MainMenuHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(String data, int position) {
        itemName.setText(data);
        itemName.setOnClickListener(this);
    }
}
