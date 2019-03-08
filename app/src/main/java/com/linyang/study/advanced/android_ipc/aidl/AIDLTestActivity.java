package com.linyang.study.advanced.android_ipc.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.linyang.aidl.AIDLService;
import com.linyang.aidl.IResult;
import com.linyang.aidl.IUser;
import com.linyang.aidl.IUserInterface;
import com.linyang.aidl.IUserListener;
import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.L;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:AIDL客户端
 * Created by fzJiang on 2019-1-24
 */
public class AIDLTestActivity extends BaseActivity {

    @BindView(R.id.btn_start_service)
    AppCompatButton mBtnStartService;
    @BindView(R.id.btn_bind_service)
    AppCompatButton mBtnBindService;
    @BindView(R.id.btn_unbind_service)
    AppCompatButton mBtnUnbindService;
    @BindView(R.id.btn_user_register)
    AppCompatButton mBtnUserRegister;
    @BindView(R.id.btn_user_login)
    AppCompatButton mBtnUserLogin;
    @BindView(R.id.btn_get_user_info)
    AppCompatButton mBtnGetUserInfo;
    @BindView(R.id.btn_stop_service)
    AppCompatButton mBtnStopService;
    @BindView(R.id.btn_register_listener)
    AppCompatButton mBtnRegisterListener;
    @BindView(R.id.btn_unregister_listener)
    AppCompatButton mBtnUnregisterListener;

    private Intent mIntent;
    private IUserInterface mIUserInterface;
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mIUserInterface = IUserInterface.Stub.asInterface(service);
                // 设置死亡代理
                mIUserInterface.asBinder().linkToDeath(mDeathRecipient, 0);

                L.i("AIDL远程服务连接成功");
            } catch (RemoteException e) {
                L.e("AIDL远程服务连接失败：" + e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.i("AIDL远程服务连接断开");
            mIUserInterface = null;
        }
    };

    // 监听
    private IUserListener mIUserListener = new IUserListener.Stub() {

        @Override
        public void onUserLogin(IUser user) {
            L.i("用户登录操作:" + user.toString());
        }
    };

    // 死亡连接代理
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {

        @Override
        public void binderDied() {
            L.i("连接已断开");
            if (mIUserInterface == null) {
                return;
            }
            mIUserInterface.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mIUserInterface = null;
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_aidl_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mIntent = new Intent(this, AIDLService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIUserInterface != null) {
            stopService(mIntent);
            unbindService(mConnection);
        }
    }

    @OnClick({R.id.btn_start_service, R.id.btn_bind_service, R.id.btn_register_listener, R.id.btn_user_register, R.id.btn_user_login, R.id.btn_get_user_info, R.id.btn_unregister_listener, R.id.btn_unbind_service, R.id.btn_stop_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_service:
                startService(mIntent);
                break;

            case R.id.btn_bind_service:
                bindService(mIntent, mConnection, BIND_AUTO_CREATE);
                break;

            case R.id.btn_register_listener:
                if (mIUserInterface != null) {
                    try {
                        mIUserInterface.registerListener(mIUserListener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_user_register:
                if (mIUserInterface != null) {
                    try {
                        IResult result = mIUserInterface.registerUser(new IUser("用户001", "12345678"));
                        L.i("用户注册结果：" + result.toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_user_login:
                if (mIUserInterface != null) {
                    try {
                        IResult result = mIUserInterface.login("用户001", "12345678");
                        L.i("用户登录结果:" + result.toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_get_user_info:
                if (mIUserInterface != null) {
                    try {
                        IResult result = mIUserInterface.getUserInfo("000000001");
                        L.i("用户信息：" + result.toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_unregister_listener:
                if (mIUserInterface != null) {
                    try {
                        mIUserInterface.unRegisterListener(mIUserListener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_unbind_service:
                unbindService(mConnection);
                break;

            case R.id.btn_stop_service:
                stopService(mIntent);
                break;
        }
    }
}
