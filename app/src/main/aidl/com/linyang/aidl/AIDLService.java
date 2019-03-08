package com.linyang.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.linyang.study.app.util.GsonUtil;
import com.linyang.study.app.util.L;

import androidx.annotation.Nullable;

/**
 * 描述:远端AIDL 服务
 * Created by fzJiang on 2019-1-24
 */
public class AIDLService extends Service {

    private static final String ACCESS_AIDL_SERVICE = "com.linyang.permission.ACCESS_AIDL_SERVICE";

    private RemoteCallbackList<IUserListener> mCallbackList = new RemoteCallbackList<>();

    private Binder mBinder = new IUserInterface.Stub() {

        @Override
        public IResult registerUser(IUser user) {
            L.i("新用户注册:" + user.toString());
            user.setUserID("00000000001");
            IResult result = new IResult();
            result.setCode(0);
            result.setResult(GsonUtil.GsonString(user));
            return result;
        }

        @Override
        public IResult login(String userName, String password) throws RemoteException {
            L.i("新用户登录:" + userName + ":" + password);
            IUser user = new IUser("00000000001", userName, password, "2019-1-24 12:00:00");
            IResult result = new IResult();
            result.setCode(0);
            result.setResult(GsonUtil.GsonString(user));

            // 监听回调
            final int index = mCallbackList.beginBroadcast();
            for (int i = 0; i < index; i++) {
                IUserListener listener = mCallbackList.getBroadcastItem(i);
                if (listener != null) {
                    listener.onUserLogin(user);
                }
            }
            mCallbackList.finishBroadcast();
            return result;
        }

        @Override
        public IResult getUserInfo(String userID) {
            L.i("获取用户信息:" + userID);
            IUser user = new IUser(userID, "用户001", "12345678", "2019-1-24 12:00:00");
            IResult result = new IResult();
            result.setCode(0);
            result.setResult(GsonUtil.GsonString(user));
            return result;
        }

        @Override
        public void registerListener(IUserListener listener) {
            L.i("注册监听:" + mCallbackList.getRegisteredCallbackCount());
            mCallbackList.register(listener);
        }

        @Override
        public void unRegisterListener(IUserListener listener) {
            L.i("解除注册监听:" + mCallbackList.getRegisteredCallbackCount());
            mCallbackList.unregister(listener);
        }
    };

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
        int check = checkCallingOrSelfPermission(ACCESS_AIDL_SERVICE);
        L.i("权限检查：" + check);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mBinder;
    }
}
