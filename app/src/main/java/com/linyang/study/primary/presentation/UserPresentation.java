package com.linyang.study.primary.presentation;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.linyang.study.R;

import java.text.MessageFormat;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2019/01/08 9:15 星期二
 */
public class UserPresentation extends Presentation {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.bt_user_send)
    AppCompatButton btUserSend;

    private Context mContext;

    private UserPresentationCallback mCallback;

    public UserPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保持屏幕常亮
        Window window = getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        setContentView(R.layout.view_user_presentation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_user_send)
    public void onViewClicked() {
        if (mCallback != null) {
            mCallback.onReceive("第二屏幕回复消息");
        }
    }

    public void setUserContent(String content) {
        if (tvContent != null && !TextUtils.isEmpty(content)) {
            tvContent.setText(MessageFormat.format("接收到消息:{0}", content));
        }
    }

    public void setCallback(UserPresentationCallback callback) {
        mCallback = callback;
    }
}
