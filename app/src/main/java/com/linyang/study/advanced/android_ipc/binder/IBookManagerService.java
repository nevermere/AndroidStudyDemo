package com.linyang.study.advanced.android_ipc.binder;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * 描述:Binder Service
 * Created by fzJiang on 2019-1-24
 */
public class IBookManagerService extends Service {

    private static final String ACCESS_BOOK_SERVICE = "com.linyang.permission.ACCESS_BOOK_SERVICE";

    private IBookManager mIBookManager = new IBookManagerImpl();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission(ACCESS_BOOK_SERVICE);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mIBookManager.asBinder();
    }
}
