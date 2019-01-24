package com.linyang.study.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.LayoutRes;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 通用适配器
 * Created by fzJiang on 2016-12-30.
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    protected Context mContext;
    private List<T> dataList;
    private Unbinder mUnBinder;

    public BaseAdapter(Context context) {
        this.mContext = context;
        this.dataList = new ArrayList<>();
    }

    public BaseAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.dataList = data;
    }

    public Context getContext() {
        return this.mContext;
    }

    /**
     * 增加某项数据
     *
     * @param data
     */
    public void addData(T data) {
        if (data != null) {
            this.dataList.add(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 增加某集合数据
     *
     * @param data
     */
    public void addData(Collection<T> data) {
        if (data != null && data.size() != 0) {
            this.dataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 增加数据到特定位置
     *
     * @param position
     * @param data
     */
    public void addData(int position, Collection<T> data) {
        if (data != null && data.size() != 0 && position < data.size()) {
            this.dataList.addAll(position, data);
            notifyDataSetChanged();
        }
    }

    /**
     * 增加数据到特定位置
     *
     * @param position
     * @param data
     */
    public void addData(int position, T data) {
        if (data != null) {
            this.dataList.add(position, data);
            notifyDataSetChanged();
        }
    }

    /**
     * 刷新全部数据
     *
     * @param data
     */
    public void refreshData(Collection<T> data) {
        if (data != null && data.size() != 0) {
            this.dataList.clear();
            this.dataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除包含某集合内的数据
     *
     * @param data
     */
    public void removeData(Collection<T> data) {
        this.dataList.removeAll(data);
        notifyDataSetChanged();
    }

    /**
     * 移除所有数据
     */
    public void removeAll() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 移除某项数据
     *
     * @param data
     */
    public void remove(T data) {
        this.dataList.remove(data);
        notifyDataSetChanged();
    }

    /**
     * 移除特定位置的数据
     *
     * @param position
     */
    public void remove(int position) {
        this.dataList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除指定位置之间的数据
     *
     * @param startPosition 起始位置
     * @param endPosition   结束位置
     */
//    public void remove(int startPosition, int endPosition) {
//       // this.dataList.remove(position);
//        //notifyDataSetChanged();
//    }

    /**
     * 获取设置的数据
     *
     * @return
     */
    public List<T> getDataList() {
        return dataList;
    }


    /**
     * 截取特定位置的数据
     *
     * @param index 起始位置
     * @param count 截取数目
     * @return
     */
    public List<T> subData(int index, int count) {
        return this.dataList.subList(index, index + count);
    }

    /**
     * 更新特定位置的数据
     *
     * @param position
     */
    public void updateData(int position, T data) {
        if (this.dataList != null && this.dataList.size() != 0 && position < this.dataList.size()) {
            this.dataList.remove(position);
            addData(position, data);
        }
    }

    /**
     * 获取特定位置的view
     *
     * @param position
     * @param parent
     * @return
     */
    public View getViewAt(int position, ViewGroup parent) {
        return getView(position, null, parent);
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList == null ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        }
        // ButterKnife绑定界面
        if (userButterKnife()) {
            mUnBinder = ButterKnife.bind(this, convertView);
        }
        // 界面设置
        setView(convertView, position, parent.getContext());
        return convertView;
    }

    /**
     * 获取界面id
     *
     * @return
     */
    protected abstract @LayoutRes int getLayoutId();

    /**
     * 界面设置
     *
     * @param convertView
     * @param position
     * @param context
     */
    protected abstract void setView(View convertView, int position, Context context);


    /**
     * 是否使用ButterKnife绑定
     *
     * @return 默认不使用
     */
    protected boolean userButterKnife() {
        return false;
    }

    /**
     * ButterKnife解除注册
     */
    public void unbind() {
        // ButterKnife解除注册
        if (mUnBinder != null && mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
        }
        this.mUnBinder = null;
    }
}
