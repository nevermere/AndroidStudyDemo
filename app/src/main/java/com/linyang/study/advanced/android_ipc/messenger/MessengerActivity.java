package com.linyang.study.advanced.android_ipc.messenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.LogUtil;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:Messenger客户端
 * Created by fzJiang on 2018-11-08
 */
public class MessengerActivity extends BaseActivity {

    @BindView(R.id.btn_send_message)
    AppCompatButton mBtnSendMessage;

    private Messenger mGetReplyMessenger;
    private Intent mIntent;
    private Messenger mMessenger;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
        unbindService(mConnection);
    }

    @Override
    public boolean userHandler() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_messenger;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mGetReplyMessenger = new Messenger(getHandler());

        mIntent = new Intent(this, MessengerService.class);
        startService(mIntent);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }


    @OnClick(R.id.btn_send_message)
    public void onViewClicked() {
        if (mMessenger != null && mMessenger.getBinder().isBinderAlive()) {
            try {
                Message message = Message.obtain(null, MessengerService.MESSAGE_FROM_CLIENT);
                Bundle bundle = new Bundle();
                bundle.putString(MessengerService.MESSAGE_DATA, "Hello world");
                message.setData(bundle);

                // 设置消息回调
                message.replyTo = mGetReplyMessenger;

                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onHandlerReceive(Message msg) {
        switch (msg.what) {
            case MessengerService.MESSAGE_RECEIVED:
                LogUtil.i("Message from Messenger Server:" + msg.getData().toString());
                break;

            default:
                break;
        }
        super.onHandlerReceive(msg);

    }
}
