package com.linyang.study.primary.presentation;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.linyang.study.app.util.LogUtil;

/**
 * 描述:
 * Created by fzJiang on 2019/01/08 9:25 星期二
 */
public class PresentationUtil {

    // 获取设备上的屏幕
    private DisplayManager mDisplayManager;// 屏幕管理器
    private Display[] mDisplays;// 屏幕数组
    private UserPresentation mUserPresentation;

    private static PresentationUtil INSTANCE;

    public static PresentationUtil getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PresentationUtil(context);
        }
        return INSTANCE;
    }

    private PresentationUtil(Context context) {
        mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        mDisplays = mDisplayManager.getDisplays();

        LogUtil.i("获取屏幕数目：" + mDisplays.length);

        if (mDisplays.length > 1 && mUserPresentation == null) {
            // displays[1]是副屏
            mUserPresentation = new UserPresentation(context.getApplicationContext(), mDisplays[1]);
            Window window = mUserPresentation.getWindow();
            if (window != null) {
                window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }
    }

    public void setUserContent(String content) {
        if (mUserPresentation != null) {
            mUserPresentation.setUserContent(content);
        }
    }

    public void setCallback(UserPresentationCallback callback) {
        if (mUserPresentation != null && callback != null) {
            mUserPresentation.setCallback(callback);
        }
    }
}
