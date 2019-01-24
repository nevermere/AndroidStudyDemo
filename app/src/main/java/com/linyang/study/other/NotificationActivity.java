package com.linyang.study.other;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.linyang.study.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2018/11/21 上午 10:21  星期三
 */
public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.bt_open_notification)
    AppCompatButton btOpenNotification;
    @BindView(R.id.bt_open_remote_views)
    AppCompatButton btOpenRemoteViews;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @OnClick({R.id.bt_open_notification, R.id.bt_open_remote_views})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_open_notification:
                Notification notification = new NotificationCompat.Builder(this, "my_channel_01")
                        .setSmallIcon(R.mipmap.ic_launcher_round)// 设置图标
                        .setTicker("默认通知栏")// 设置状态栏开始动画的文字
                        .setContentTitle("默认通知栏")// 设置标题
                        .setContentText("点击查看详细内容")// 消息内容
                        .setContentIntent(PendingIntent.getActivity(this, 0,
                                new Intent(this, DemoIntentActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))// 设置打开跳转界面
                        .setDefaults(Notification.DEFAULT_ALL)// 设置默认的提示音，振动方式，灯光
                        .setAutoCancel(true)// 打开程序后图标消失
                        .setOngoing(false)// 设置是否为一个正在进行中的通知，这一类型的通知将无法删除
                        .build();
                mNotificationManager.notify(11111, notification);// 通过通知管理器发送通知
                break;

            case R.id.bt_open_remote_views:
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
                remoteViews.setImageViewResource(R.id.icon_5, R.mipmap.ic_launcher_round);
                remoteViews.setTextViewText(R.id.tv_5_s, "自定义通知栏");
                remoteViews.setTextViewText(R.id.tv_5_e, "点击查看详细内容");
                remoteViews.setOnClickPendingIntent(R.id.tv_5_e, PendingIntent.getActivity(this, 0,
                        new Intent(this, DemoIntentActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

                Notification notification2 = new NotificationCompat.Builder(this, "my_channel_02")
                        .setSmallIcon(R.mipmap.ic_launcher_round)// 设置图标
                        .setTicker("自定义通知栏")// 设置状态栏开始动画的文字
                        .setAutoCancel(true)// 打开程序后图标消失
                        .setOngoing(false)// 设置是否为一个正在进行中的通知，这一类型的通知将无法删除
//                        .setCustomBigContentView(remoteViews)// 设置展开时的自定义界面
                        .setContent(remoteViews)// 设置自定义界面
                        .build();
                mNotificationManager.notify(22222, notification2);// 通过通知管理器发送通知
                break;
        }
    }
}
