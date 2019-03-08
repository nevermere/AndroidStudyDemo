package com.linyang.study.advanced.android_ipc.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.linyang.study.app.util.L;

import androidx.annotation.Nullable;

/**
 * 描述:Messenger服务端Service
 * Created by fzJiang on 2018-11-08
 */
public class MessengerService extends Service {

    public static final int MESSAGE_FROM_CLIENT = 1;
    public static final int MESSAGE_RECEIVED = 1 >> 1;
    public static final String MESSAGE_DATA = "data";

    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_FROM_CLIENT:
                    L.i("Message from Messenger Client:" + msg.getData().toString());

                    try {
                        // 返回消息到客户端
                        Messenger client = msg.replyTo;
                        Message message = Message.obtain(null, MESSAGE_RECEIVED);
                        Bundle bundle = new Bundle();
                        bundle.putString(MESSAGE_DATA, "data from Messenger Server");
                        message.setData(bundle);
                        client.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
