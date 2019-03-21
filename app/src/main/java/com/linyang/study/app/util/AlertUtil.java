package com.linyang.study.app.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 描述:
 * Created by fzJiang on 2019/03/12 16:42 星期二
 */
public class AlertUtil {

    private static Handler mHandler;

    public static void alert() {

        // 判断当前进程是否为UI线程,是UI线程则直接显示弹框；否则通过Handler将消息发送到循环队列中
        if (Looper.myLooper() == Looper.getMainLooper()) {
            alertDialog("更新提示", "发现新版本，是否更新？");
        } else {
            if (mHandler == null) {
                mHandler = new Handler(Looper.getMainLooper());
            }
            mHandler.post(() -> alertDialog("更新提示", "发现新版本，是否更新？"));
        }
    }

    private static void alertDialog(String title, String message) {
        Context appContext = null;
        try {
            // 利用反射获取当前进程的context
            @SuppressLint("PrivateApi") Class<?> clazz = Class.forName("android.app.ActivityThread");
            Method method = clazz.getDeclaredMethod("currentApplication");
            appContext = (Context) method.invoke(null, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // 构建AlertDialog
        if (appContext != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
            builder.setTitle(title == null ? "标题" : title)
                    .setMessage(message == null ? "提示内容" : message)
                    .setCancelable(true)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            if (window != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    window.setType(WindowManager.LayoutParams.TYPE_TOAST);
                } else {
                    window.setType(WindowManager.LayoutParams.TYPE_PHONE);
                }
                dialog.show();
            }
        }
    }
}
