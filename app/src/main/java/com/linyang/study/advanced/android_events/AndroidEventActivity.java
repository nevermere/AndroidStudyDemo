package com.linyang.study.advanced.android_events;

import android.view.MotionEvent;

import com.linyang.study.R;
import com.linyang.study.advanced.android_events.view.EventViewA;
import com.linyang.study.advanced.android_events.view.EventViewB;
import com.linyang.study.advanced.android_events.view_group.EventViewGroupA;
import com.linyang.study.advanced.android_events.view_group.EventViewGroupB;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.L;

import butterknife.BindView;

/**
 * 描述:
 * Created by fzJiang on 2019/03/21 14:46 星期四
 */
public class AndroidEventActivity extends BaseActivity {

    @BindView(R.id.view_a)
    EventViewA viewA;
    @BindView(R.id.view_b)
    EventViewB viewB;
    @BindView(R.id.view_group_b)
    EventViewGroupB viewGroupB;
    @BindView(R.id.view_group_a)
    EventViewGroupA viewGroupA;

    @Override
    public int getLayoutId() {
        return R.layout.activity_android_event;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        L.i("-------------dispatchTouchEvent-------------------AndroidEventActivity");

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.i("-------------onTouchEvent-------------------AndroidEventActivity");

        return super.onTouchEvent(event);
    }
}
