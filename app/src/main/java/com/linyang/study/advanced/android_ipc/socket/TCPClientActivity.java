package com.linyang.study.advanced.android_ipc.socket;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.LogUtil;
import com.linyang.study.app.util.RxSchedulers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * Created by fzJiang on 2019/01/24 14:42 星期四
 */
public class TCPClientActivity extends BaseActivity {

    public static final int READ_LEN = 1024;
    private static final String[] MESSAGE = new String[]{
            "你好啊，哈哈",
            "请问你叫什么名字呀？",
            "今天天气不错啊，shy",
            "你知道吗？我可是可以和多个人同时聊天的哦",
            "给你讲个笑话吧：据说爱笑的人运气不会太差，不知道真假。"
    };

    @BindView(R.id.bt_connect)
    AppCompatButton btConnect;
    @BindView(R.id.btn_send_message)
    AppCompatButton btnSendMessage;

    private Intent mIntent;
    private Socket mSocket;
    private boolean isConnected;
    private BufferedReader mBufferedReader;
    private BufferedWriter mBufferedWriter;
    private CompositeDisposable mDisposable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
        clearDisposable();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tcp_client;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mIntent = new Intent(this, TCPServerService.class);
        startService(mIntent);
    }

    @OnClick({R.id.bt_connect, R.id.btn_send_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_connect:
                connect()
                        .subscribe(new Observer<Boolean>() {

                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(Boolean connected) {
                                isConnected = connected;
                                LogUtil.i("连接服务器成功：" + isConnected);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.e("连接服务器失败：" + e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;

            case R.id.btn_send_message:
                final int index = new Random().nextInt(MESSAGE.length);
                sendAndRead(MESSAGE[index])
                        .subscribe(new Observer<String>() {

                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(String response) {
                                LogUtil.i("收到回复：" + response);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.e("发送失败：" + e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
        }
    }

    /**
     * 连接到服务器
     *
     * @return true/false
     */
    private Observable<Boolean> connect() {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            String errorMessage = null;
            try {
                mSocket = new Socket();
                mSocket.connect(new InetSocketAddress(TCPServerService.HOST_ADDR, TCPServerService.POST), 5000);
                boolean isConnected = mSocket.isConnected();
                if (isConnected) {
                    // 连接成功,获取输入输出流
                    mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    mBufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                }
            } catch (UnknownHostException e) {
                errorMessage = "服务器地址错误";
            } catch (IOException e) {
                errorMessage = "访问服务器失败";
            } catch (SecurityException e) {
                errorMessage = "未开启联网权限";
            } catch (IllegalArgumentException e) {
                errorMessage = "参数不合法";
            } catch (Exception e) {
                errorMessage = "其他未知错误";
            }

            if (TextUtils.isEmpty(errorMessage)) {
                emitter.onNext(true);
                emitter.onComplete();
            } else {
                emitter.onError(new Throwable(errorMessage));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 发送数据并接收
     *
     * @param message 发送的消息
     * @return 回复信息
     */
    private Observable<String> sendAndRead(String message) {
        return send(message)
                .skipWhile(success -> { // 如果发送失败,则不进行后续操作
                    LogUtil.i("消息：" + message + "---发送" + (success ? "成功" : "失败"));
                    return !success;
                })
                .compose(read())
                .compose(RxSchedulers.io_main());
    }

    /**
     * 发送消息到服务器(指定在io线程发送数据)
     *
     * @param message 消息
     * @return true/false
     */
    private Observable<Boolean> send(String message) {
        return Observable.just(message)
                .flatMap((Function<String, ObservableSource<Boolean>>) s -> {
                    if (isConnected && mBufferedWriter != null) {
                        try {
                            mBufferedWriter.write(s.concat("\r\n"));
                            mBufferedWriter.flush();
                            return Observable.just(true);
                        } catch (IOException e) {
                            return Observable.error(new Throwable("消息：" + message + "---发送失败，服务器连接已断开：" + e.getMessage()));
                        }
                    } else {
                        return Observable.error(new Throwable("消息：" + message + "---发送失败，服务器连接已断开/输入流为空"));
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 读取服务器回复消息
     *
     * @param <T> 回复消息
     * @return
     */
    private <T> ObservableTransformer<T, String> read() {
        return upstream -> upstream.flatMap((Function<T, ObservableSource<String>>) t -> {
            return Observable.create((ObservableOnSubscribe<String>) emitter -> {

                if (isConnected && mBufferedReader != null) {
                    String message;
                    while ((message = mBufferedReader.readLine()) != null) {
                        emitter.onNext(message);
                    }
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable("读取回复消息失败，连接已断开/输出流为空"));
                }
            }).subscribeOn(Schedulers.io());
        });
    }

    private void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        if (d != null) {
            mDisposable.add(d);
        }
    }

    private void clearDisposable() {
        if (mDisposable != null) {
            mDisposable.clear();
        }
    }
}
