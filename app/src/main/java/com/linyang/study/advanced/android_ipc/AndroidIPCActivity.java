package com.linyang.study.advanced.android_ipc;

import android.view.View;

import com.linyang.study.R;
import com.linyang.study.advanced.android_ipc.aidl.AIDLTestActivity;
import com.linyang.study.advanced.android_ipc.binder.BinderTestActivity;
import com.linyang.study.advanced.android_ipc.binder_poll.BinderPoolActivity;
import com.linyang.study.advanced.android_ipc.content_provider.ContentProviderActivity;
import com.linyang.study.advanced.android_ipc.messenger.MessengerActivity;
import com.linyang.study.advanced.android_ipc.socket.TCPClientActivity;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.ArmsUtils;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:安卓进程间通讯
 * Created by fzJiang on 2019/01/23 15:37 星期三
 */
public class AndroidIPCActivity extends BaseActivity {

    @BindView(R.id.btn_aidl)
    AppCompatButton btnAidl;
    @BindView(R.id.btn_binder)
    AppCompatButton btnBinder;
    @BindView(R.id.btn_binder_pool)
    AppCompatButton btnBinderPool;
    @BindView(R.id.btn_messenger)
    AppCompatButton btnMessenger;
    @BindView(R.id.btn_content_provider)
    AppCompatButton btnContentProvider;
    @BindView(R.id.btn_file)
    AppCompatButton btnFile;
    @BindView(R.id.btn_socket)
    AppCompatButton btnSocket;

    @Override
    public int getLayoutId() {
        return R.layout.activity_android_ipc;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_aidl, R.id.btn_binder, R.id.btn_binder_pool, R.id.btn_messenger, R.id.btn_content_provider, R.id.btn_file, R.id.btn_socket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_aidl:
                ArmsUtils.startActivity(this, AIDLTestActivity.class);
                break;

            case R.id.btn_binder:
                ArmsUtils.startActivity(this, BinderTestActivity.class);
                break;

            case R.id.btn_binder_pool:
                ArmsUtils.startActivity(this, BinderPoolActivity.class);
                break;

            case R.id.btn_messenger:
                ArmsUtils.startActivity(this, MessengerActivity.class);
                break;

            case R.id.btn_content_provider:
                ArmsUtils.startActivity(this, ContentProviderActivity.class);
                break;

            case R.id.btn_socket:
                ArmsUtils.startActivity(this, TCPClientActivity.class);
                break;

            case R.id.btn_file:
                break;
        }
    }
}
