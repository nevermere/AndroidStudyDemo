package com.linyang.study.advanced.android_ipc.binder;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.LogUtil;

import java.util.List;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:Binder客户端
 * Created by fzJiang on 2019-1-24
 */
public class BinderTestActivity extends BaseActivity {

    @BindView(R.id.btn_start_service)
    AppCompatButton mBtnStartService;
    @BindView(R.id.btn_bind_service)
    AppCompatButton mBtnBindService;
    @BindView(R.id.btn_unbind_service)
    AppCompatButton mBtnUnbindService;
    @BindView(R.id.btn_add_book)
    AppCompatButton mBtnAddBook;
    @BindView(R.id.btn_get_book_list)
    AppCompatButton mBtnGetBookList;
    @BindView(R.id.btn_stop_service)
    AppCompatButton mBtnStopService;
    @BindView(R.id.btn_register_listener)
    AppCompatButton mBtnRegisterListener;
    @BindView(R.id.btn_unregister_listener)
    AppCompatButton mBtnUnregisterListener;

    private Intent mIntent;
    private IBookManager mIBookManager;
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.i("Binder Service 已连接");
            mIBookManager = IBookManagerImpl.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i("Binder Service 连接断开");
            mIBookManager = null;
        }
    };

    private IBookListener mIBookListener = new IBookListener() {

        @Override
        public IBinder asBinder() {
            return null;
        }

        @Override
        public void onNewBookArrived(Book book) {
            LogUtil.i("新书到达:" + book.toString());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIBookManager != null) {
            stopService(mIntent);
            unbindService(mConnection);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_binder_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mIntent = new Intent(this, IBookManagerService.class);
    }

    @OnClick({
            R.id.btn_start_service,
            R.id.btn_bind_service,
            R.id.btn_unbind_service,
            R.id.btn_register_listener,
            R.id.btn_unregister_listener,
            R.id.btn_add_book,
            R.id.btn_get_book_list,
            R.id.btn_stop_service
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_service:
                if (mIntent != null) {
                    startService(mIntent);
                }
                break;

            case R.id.btn_bind_service:
                if (mIntent != null) {
                    bindService(mIntent, mConnection, BIND_AUTO_CREATE);
                }
                break;

            case R.id.btn_unbind_service:
                unbindService(mConnection);
                break;

            case R.id.btn_register_listener:
                if (mIBookManager != null) {
                    try {
                        mIBookManager.registerListener(mIBookListener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_unregister_listener:
                if (mIBookManager != null) {
                    try {
                        mIBookManager.unRegisterListener(mIBookListener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_add_book:
                if (mIBookManager != null) {
                    try {
                        mIBookManager.addBook(new Book(123456L, "Android开发艺术与探索"));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_get_book_list:
                if (mIBookManager != null) {
                    try {
                        List<Book> list = mIBookManager.getBookList();
                        for (Book book : list) {
                            LogUtil.i("获取到的书籍:" + list.toString());
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_stop_service:
                if (mIntent != null) {
                    stopService(mIntent);
                }
                break;
        }
    }
}
