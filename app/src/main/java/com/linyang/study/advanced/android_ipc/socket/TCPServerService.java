package com.linyang.study.advanced.android_ipc.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.linyang.study.app.util.LogUtil;
import com.linyang.study.app.util.StringUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * Created by fzJiang on 2019/01/29 9:59 星期二
 */
public class TCPServerService extends Service {

    private static final String[] MESSAGE = new String[]{
            "你好啊，哈哈",
            "请问你叫什么名字呀？",
            "今天天气不错啊，shy",
            "你知道吗？我可是可以和多个人同时聊天的哦",
            "给你讲个笑话吧：据说爱笑的人运气不会太差，不知道真假。"
    };

    public static final String HOST_ADDR = "localhost";
    public static final int POST = 5073;

    private ExecutorService mExecutorService;
    private static ServerSocket mServerSocket;
    private boolean isServerStarted;
    private CompositeDisposable mDisposable;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化服务,监听客户端消息并返回消息
        init().subscribe(new Observer<Boolean>() {

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                LogUtil.i("初始化服务成功：" + aBoolean);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.i("初始化服务失败：" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭服务
        closeServer();
        clearDisposable();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 初始化
     *
     * @return true/false
     */
    private Observable<Boolean> init() {
        return startServer()
                .skipWhile(aBoolean -> {
                    // 如果服务初始化失败，则不继续后续步骤
                    isServerStarted = aBoolean;
                    // 初始化线程池
                    if (isServerStarted && mExecutorService == null) {
                        mExecutorService = Executors.newFixedThreadPool(10);
                    }
                    return !isServerStarted;
                })
                .delay(500, TimeUnit.MILLISECONDS)
                .doOnNext(isServerStarted -> listenClient())// 初始化成功后开始监听客户端连接
                .compose(io_main());

    }

    /**
     * 初始化Socket服务
     *
     * @return true/false
     */
    private Observable<Boolean> startServer() {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            try {
                mServerSocket = new ServerSocket(POST);
                emitter.onNext(true);
                LogUtil.i("初始化服务成功：" + HOST_ADDR + ":" + POST);
            } catch (IOException e) {
                LogUtil.e("初始化服务失败：" + HOST_ADDR + ":" + POST + "-----" + e.getMessage());
                emitter.onNext(false);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 监听客户端消息并返回消息
     */
    private void listenClient() {
        while (isServerStarted) {
            try {
                Socket socket = mServerSocket.accept();
                if (socket.isConnected()) {
                    mExecutorService.execute(new ListenerClient(socket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭Socket服务
     *
     * @return true/false
     */
    private void closeServer() {
        if (isServerStarted) {
            try {
                mServerSocket.close();
                isServerStarted = false;
                LogUtil.i("服务已关闭：" + HOST_ADDR + ":" + POST);
            } catch (IOException e) {
                LogUtil.e("服务关闭失败：" + HOST_ADDR + ":" + POST + "-----" + e.getMessage());
            }
        }
        if (mExecutorService != null) {
            mExecutorService.shutdown();
        }
    }

    private class ListenerClient implements Runnable {

        private Socket mSocket;

        ListenerClient(Socket socket) {
            mSocket = socket;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;
            try {
                LogUtil.i("客户端连接到服务：" + mSocket.toString() + "，开始监听：" + StringUtil.simpleDateFormat(System.currentTimeMillis()));

                bufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));

                String message;
                int index;
                while ((message = bufferedReader.readLine()) != null) {
                    index = new Random().nextInt(MESSAGE.length);
                    bufferedWriter.write(MESSAGE[index].concat("\r\n"));
                    bufferedWriter.flush();
                    LogUtil.i("收到客户端消息：" + message + "，回复消息：" + MESSAGE[index]);
                }
            } catch (IOException e) {
                LogUtil.i("客户端：" + mSocket.toString() + "，监听异常：" + e.getMessage());

            } finally {
                try {
                    if (mSocket != null) {
                        mSocket.close();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 线程切换
     */
    private static <T> ObservableTransformer<T, T> io_main() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
