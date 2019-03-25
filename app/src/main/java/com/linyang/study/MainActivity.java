package com.linyang.study;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.linyang.study.advanced.android_events.AndroidEventActivity;
import com.linyang.study.advanced.android_ipc.AndroidIPCActivity;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.adapter.BaseRecycleAdapter;
import com.linyang.study.app.adapter.MainMenuAdapter;
import com.linyang.study.app.util.ArmsUtils;
import com.linyang.study.other.NotificationActivity;
import com.linyang.study.other.SkinsActivity;
import com.linyang.study.other.WindowManagerActivity;
import com.linyang.study.other.constraint_layout.ConstraintLayoutActivity;
import com.linyang.study.other.glide.GlideTestActivity;
import com.linyang.study.other.jetpack.activity.JetPackMainActivity;
import com.linyang.study.other.moshi.MoshiActivity;
import com.linyang.study.primary.android_multi_thread.AsyncThreadPoolActivity;
import com.linyang.study.primary.android_multi_thread.HandlerTestActivity;
import com.linyang.study.primary.android_multi_thread.async_task.AsyncTaskActivity;
import com.linyang.study.primary.animation.AnimationActivity;
import com.linyang.study.primary.architecture_components.LifeObserverActivity;
import com.linyang.study.primary.cache.DiskLruCacheActivity;
import com.linyang.study.primary.cache.PhotoWallActivity;
import com.linyang.study.primary.custom_view.AppWidgetActivity;
import com.linyang.study.primary.custom_view.BezierRoundActivity;
import com.linyang.study.primary.custom_view.CardViewActivity;
import com.linyang.study.primary.custom_view.ChatViewActivity;
import com.linyang.study.primary.custom_view.CycleViewActivity;
import com.linyang.study.primary.custom_view.GuaGuaKaViewActivity;
import com.linyang.study.primary.custom_view.HorizontalScrollViewExActivity;
import com.linyang.study.primary.custom_view.LeafLoadingActivity;
import com.linyang.study.primary.custom_view.MyScrollViewActivity;
import com.linyang.study.primary.custom_view.NavViewActivity;
import com.linyang.study.primary.custom_view.PolygonViewActivity;
import com.linyang.study.primary.custom_view.StatisticsViewActivity;
import com.linyang.study.primary.custom_view.widget.RecycleViewDivider;
import com.linyang.study.primary.gesture_detector.GestureDetectorActivity;
import com.linyang.study.primary.io.IOActivity;
import com.linyang.study.primary.presentation.PresentationActivity;
import com.linyang.study.primary.webview.WebViewActivity;

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

    private MainMenuAdapter mAdapter;

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
        mAdapter = new MainMenuAdapter(this, menuList);
        rvMenuList.setAdapter(mAdapter);
        // 列表设置点击事件
        mAdapter.setOnItemClickListener(this);
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
            case 24:
                startActivity(new Intent(this, AndroidIPCActivity.class));
                break;
            case 25:
                startActivity(new Intent(this, AsyncTaskActivity.class));
                break;
            case 26:
                startActivity(new Intent(this, HandlerTestActivity.class));
                break;
            case 27:
                startActivity(new Intent(this, GlideTestActivity.class));
                break;
            case 28:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            case 29:
                startActivity(new Intent(this, LifeObserverActivity.class));
                break;
            case 30:
                startActivity(new Intent(this, ConstraintLayoutActivity.class));
                break;
            case 31:
                startActivity(new Intent(this, AndroidEventActivity.class));
                break;
        }
    }
}
