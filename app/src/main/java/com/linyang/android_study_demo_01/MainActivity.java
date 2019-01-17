package com.linyang.android_study_demo_01;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.linyang.android_study_demo_01.adapter.BaseRecycleAdapter;
import com.linyang.android_study_demo_01.adapter.MainMenuAdapter;
import com.linyang.android_study_demo_01.android_presentation.PresentationActivity;
import com.linyang.android_study_demo_01.animation.AnimationActivity;
import com.linyang.android_study_demo_01.app.BaseActivity;
import com.linyang.android_study_demo_01.cache.DiskLruCacheActivity;
import com.linyang.android_study_demo_01.cache.PhotoWallActivity;
import com.linyang.android_study_demo_01.gesture_detector.GestureDetectorActivity;
import com.linyang.android_study_demo_01.io.IOActivity;
import com.linyang.android_study_demo_01.jetpack.activity.JetPackMainActivity;
import com.linyang.android_study_demo_01.android_thread.AsyncThreadPoolActivity;
import com.linyang.android_study_demo_01.ui.AppWidgetActivity;
import com.linyang.android_study_demo_01.ui.BezierRoundActivity;
import com.linyang.android_study_demo_01.ui.CardViewActivity;
import com.linyang.android_study_demo_01.ui.ChatViewActivity;
import com.linyang.android_study_demo_01.ui.CycleViewActivity;
import com.linyang.android_study_demo_01.ui.GuaGuaKaViewActivity;
import com.linyang.android_study_demo_01.ui.HorizontalScrollViewExActivity;
import com.linyang.android_study_demo_01.ui.LeafLoadingActivity;
import com.linyang.android_study_demo_01.ui.MoshiActivity;
import com.linyang.android_study_demo_01.ui.MyScrollViewActivity;
import com.linyang.android_study_demo_01.ui.NavViewActivity;
import com.linyang.android_study_demo_01.ui.NotificationActivity;
import com.linyang.android_study_demo_01.ui.PolygonViewActivity;
import com.linyang.android_study_demo_01.ui.SkinsActivity;
import com.linyang.android_study_demo_01.ui.StatisticsViewActivity;
import com.linyang.android_study_demo_01.ui.WindowManagerActivity;
import com.linyang.android_study_demo_01.widget.ArmsUtils;
import com.linyang.android_study_demo_01.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 描述:自定义view
 * Created by fzJiang on 2018-08-22
 */
public class MainActivity extends BaseActivity implements BaseRecycleAdapter.OnRecyclerViewItemClickListener<String> {

    @BindView(R.id.rv_menu_list)
    RecyclerView rvMenuList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        initMenu();
    }

    @Override
    public void initData() {

    }

    private void initMenu() {
        final String[] menus = getResources().getStringArray(R.array.home_menu_arrays);
        final List<String> menuList = new ArrayList<>(Arrays.asList(menus));

        LayoutAnimationController controller = new LayoutAnimationController(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.example_anim));
        controller.setDelay(0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);

        // 配置RecyclerView
        ArmsUtils.configRecyclerView(rvMenuList,
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false),
                new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        // 设置加载动画
        rvMenuList.setLayoutAnimation(controller);
        // 设置适配器
        MainMenuAdapter adapter = new MainMenuAdapter(this, menuList);
        rvMenuList.setAdapter(adapter);
        // 列表设置点击事件
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int viewType, String data, int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, CardViewActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, PolygonViewActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, StatisticsViewActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, ChatViewActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, GuaGuaKaViewActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, CycleViewActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, NavViewActivity.class));
                break;
            case 7:
                startActivity(new Intent(this, LeafLoadingActivity.class));
                break;
            case 8:
                startActivity(new Intent(this, BezierRoundActivity.class));
                break;
            case 9:
                startActivity(new Intent(this, MyScrollViewActivity.class));
                break;
            case 10:
                startActivity(new Intent(this, HorizontalScrollViewExActivity.class));
                break;
            case 11:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case 12:
                startActivity(new Intent(this, AppWidgetActivity.class));
                break;
            case 13:
                startActivity(new Intent(this, SkinsActivity.class));
                break;
            case 14:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            case 15:
                startActivity(new Intent(this, WindowManagerActivity.class));
                break;
            case 16:
                startActivity(new Intent(this, MoshiActivity.class));
                break;
            case 17:
                startActivity(new Intent(this, JetPackMainActivity.class));
                break;
            case 18:
                startActivity(new Intent(this, GestureDetectorActivity.class));
                break;
            case 19:
                startActivity(new Intent(this, DiskLruCacheActivity.class));
                break;
            case 20:
                startActivity(new Intent(this, PhotoWallActivity.class));
                break;
            case 21:
                startActivity(new Intent(this, IOActivity.class));
                break;
            case 22:
                startActivity(new Intent(this, PresentationActivity.class));
                break;
            case 23:
                startActivity(new Intent(this, AsyncThreadPoolActivity.class));
                break;
        }
    }
}
