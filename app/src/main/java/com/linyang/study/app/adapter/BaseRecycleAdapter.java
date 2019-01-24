package com.linyang.study.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linyang.study.app.holder.BaseHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ================================================
 * 基类 {@link RecyclerView.Adapter} ,如果需要实现非常复杂的 {@link RecyclerView} ,请尽量使用其他优秀的三方库
 * Created by jess on 2015/11/27.
 * ================================================
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {

    protected Context mContext;
    private List<T> mDataList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public BaseRecycleAdapter(Context context) {
        super();
        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    public BaseRecycleAdapter(List<T> dataList) {
        super();
        this.mDataList = dataList;
    }

    public BaseRecycleAdapter(Context context, List<T> dataList) {
        super();
        this.mContext = context;
        this.mDataList = dataList;
    }

    /**
     * 创建 {@link BaseHolder}
     *
     * @param parent
     * @param viewType
     * @return
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public BaseHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        BaseHolder<T> mHolder = getHolder(view, viewType);
        // 设置Item点击事件
        mHolder.setOnItemClickListener((view1, position) -> {
            if (mOnItemClickListener != null && mDataList.size() > 0) {
                mOnItemClickListener.onItemClick(view1, viewType, mDataList.get(position), position);
            }
        });
        return mHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseHolder<T> holder, int position) {
        holder.setData(mDataList.get(position), position);
    }

    /**
     * 返回数据的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 增加某项数据
     *
     * @param data
     */
    public void addData(T data) {
        if (data != null) {
            this.mDataList.add(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 增加某集合数据
     *
     * @param data
     */
    public void addDataList(Collection<T> data) {
        if (data != null && data.size() != 0) {
            this.mDataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 增加数据到特定位置
     *
     * @param position
     * @param data
     */
    public void addDataList(int position, Collection<T> data) {
        if (data != null && data.size() != 0 && position <= this.mDataList.size()) {
            this.mDataList.addAll(position, data);
            notifyItemRangeInserted(position, data.size());
        }
    }

    /**
     * 增加数据到特定位置
     *
     * @param position
     * @param data
     */
    public void addData(int position, T data) {
        if (data != null && position <= this.mDataList.size()) {
            this.mDataList.add(position, data);
            notifyItemInserted(position);
        }
    }

    /**
     * 刷新全部数据
     *
     * @param data
     */
    public void refreshData(Collection<T> data) {
        if (data != null && data.size() != 0) {
            this.mDataList.clear();
            this.mDataList.addAll(data);
            notifyDataSetChanged();
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mDataList, data), true);
//            diffResult.dispatchUpdatesTo(this);
        }
    }

    /**
     * 移除包含某集合内的数据
     *
     * @param data
     */
    public void removeData(Collection<T> data) {
        this.mDataList.removeAll(data);
        notifyDataSetChanged();
    }

    /**
     * 移除所有数据
     */
    public void removeAll() {
        this.mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 移除某项数据
     *
     * @param data
     */
    public void remove(T data) {
        this.mDataList.remove(data);
        notifyDataSetChanged();
    }

    /**
     * 移除特定位置的数据
     *
     * @param position
     */
    public void remove(int position) {
        this.mDataList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 获取设置的数据
     *
     * @return
     */
    public List<T> getDataList() {
        return mDataList;
    }


    /**
     * 截取特定位置的数据
     *
     * @param index 起始位置
     * @param count 截取数目
     * @return
     */
    public List<T> subData(int index, int count) {
        return this.mDataList.subList(index, index + count);
    }

    /**
     * 更新特定位置的数据
     *
     * @param position
     */
    public void updateData(int position, T data) {
        if (this.mDataList != null && this.mDataList.size() != 0 && position < this.mDataList.size() && data != null) {
            this.mDataList.remove(position);
            this.mDataList.add(position, data);
            notifyItemChanged(position);
        }
    }

    /**
     * 获得某个 {@code position} 上的 item 的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mDataList != null && position < getItemCount() ? mDataList.get(position) : null;
    }

    /**
     * 让子类实现用以提供 {@link BaseHolder}
     *
     * @param v
     * @param viewType
     * @return
     */
    public abstract BaseHolder<T> getHolder(View v, int viewType);

    /**
     * 提供用于 {@code item} 布局的 {@code layoutId}
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    /**
     * 遍历所有{@link BaseHolder},释放他们需要释放的资源
     *
     * @param recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof BaseHolder) {
                ((BaseHolder) viewHolder).onRelease();
            }
        }
    }

    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, int viewType, T data, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public class DiffCallBack<D> extends DiffUtil.Callback {

        private List<D> mOldDataList, mNewDataList;

        public DiffCallBack(List<D> mOldDataList, List<D> mNewDataList) {
            this.mOldDataList = mOldDataList;
            this.mNewDataList = mNewDataList;
        }

        @Override
        public int getOldListSize() {
            return mOldDataList != null ? mOldDataList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return mNewDataList != null ? mNewDataList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // 判断相同位置数据是否发生变化
            return mOldDataList.get(oldItemPosition) == mNewDataList.get(newItemPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // 判断两个item是否含有相同的数据
            return mOldDataList.get(oldItemPosition) == mNewDataList.get(newItemPosition);
        }
    }
}
