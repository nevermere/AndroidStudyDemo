package com.linyang.study.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linyang.study.app.inter.IFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述:
 * Created by fzJiang on 2019/01/11 16:36 星期五
 */
public abstract class BaseFragment extends Fragment implements IFragment {

    private boolean isVisible = false;// 界面是否对用户可见
    private boolean isInitView = false;// 界面初始化是否完成
    private boolean isFirstLoad = true;// 是否首次加载

    private Context mContext;
    private Activity mActivity;// 当前界面所处Activity
    private View mRootView;
    private SparseArray<View> mViews;
    private Unbinder mUnBinder;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mContext = getContext();
        // 初始化消息接收器
        if (userHandler()) {
            this.mHandler = new BaseHandler(this);
        }
        this.mViews = new SparseArray<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        // ButterKnife注入
        mUnBinder = ButterKnife.bind(this, mRootView);
        // 界面初始化
        initView();
        // 初始化界面完毕
        isInitView = true;
        // 数据懒加载
        lazyLoad();
        return mRootView;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onResume() {
        super.onResume();
        // 设置当前栈顶显示Fragment
        if (isVisible && mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).onFragmentVisible(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // ButterKnife解除注册
        if (mUnBinder != null && mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
        }
        this.mUnBinder = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (userHandler() && mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoad();
            // 设置当前栈顶显示Fragment
            if (mActivity instanceof BaseActivity) {
                ((BaseActivity) mActivity).onFragmentVisible(this);
            }
        } else {
            isVisible = false;
        }
    }

    /**
     * 数据懒加载(首次加载+界面为显示状态+界面初始化完成)
     */
    private void lazyLoad() {
        if (!isFirstLoad || !isVisible || !isInitView) {
            // 不加载数据
            return;
        }
        // 加载数据
        initData();
        isFirstLoad = false;
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }

    @Nullable
    @Override
    public Activity getAttachActivity() {
        return mActivity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends View> V findView(int viewId) {
        V view = (V) mViews.get(viewId);
        if (view == null && mRootView != null) {
            view = mRootView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    @Override
    public boolean userHandler() {
        return false;
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onHandlerReceive(Message msg) {

    }

    @Override
    public boolean onBackPressed() {
        // 默认不处理返回事件
        return false;
    }
}
