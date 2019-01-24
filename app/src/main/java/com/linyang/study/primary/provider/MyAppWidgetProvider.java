package com.linyang.study.primary.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 描述:
 * Created by fzJiang on 2018/11/21 下午 3:45  星期三
 */
public class MyAppWidgetProvider extends AppWidgetProvider {

    public static final String TAG = MyAppWidgetProvider.class.getSimpleName();

    public static final String CLICK_ACTION = "com.linyang.custiomviewdemo.CLICK_APP_WIDGET";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "-------onReceive---------");
        if (CLICK_ACTION.equals(intent.getAction())) {
            Log.i(TAG, "-------点击事件---------");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "-------onUpdate---------");
    }
}
