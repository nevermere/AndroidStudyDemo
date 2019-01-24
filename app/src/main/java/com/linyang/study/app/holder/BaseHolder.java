package com.linyang.study.app.holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ================================================
 * 基类 {@link RecyclerView.ViewHolder}
 * Created by JessYan on 2015/11/24.
 * ================================================
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnViewClickListener mOnViewClickListener = null;
    private Unbinder mUnBinder;

    public BaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);//点击事件
        // ButterKnife绑定界面
        if (userButterKnife()) {
            mUnBinder = ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 设置数据
     *
     * @param data
     * @param position
     */
    public abstract void setData(T data, int position);


    /**
     * 释放资源
     */
    public void onRelease() {
        unbind();
    }

    /**
     * ButterKnife解除注册
     */
    private void unbind() {
        // ButterKnife解除注册
        if (mUnBinder != null && mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
        }
        this.mUnBinder = null;
    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, getAdapterPosition());
        }
    }

    /**
     * 是否使用ButterKnife绑定
     *
     * @return 默认使用ButterKnife绑定
     */
    protected boolean userButterKnife() {
        return true;
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }
}
