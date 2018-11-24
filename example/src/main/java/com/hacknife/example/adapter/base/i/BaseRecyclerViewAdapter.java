package com.hacknife.example.adapter.base.i;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public abstract class BaseRecyclerViewAdapter<V, T extends BaseRecyclerViewHolder> extends RecyclerView.Adapter<T> {
    protected List<V> mData;
    protected OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public BaseRecyclerViewAdapter() {
        mData = new ArrayList<>();
    }

    public List<V> getData() {
        return mData;
    }

    public void bindData(List<V> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }

    }

    public void insertData(List<V> data) {
        if (data != null) {
            int start = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(start, data.size());
        }
    }

    public void insertData(List<V> data, int position) {
        if (data != null) {
            mData.addAll(position, data);
            notifyItemRangeInserted(position, data.size());
        }
    }

    public void insertDataBefore(List<V> data) {
        if (data != null) {
            mData.addAll(0, data);
            notifyItemRangeInserted(0, data.size());
        }
    }

    public void clear() {
        mData.clear();
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        holder.setOnRecyclerViewListener(onRecyclerViewListener);
        holder.bindData(mData.get(position), mData.size(), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public interface OnRecyclerViewListener<T> {
        void onItemClick(T entity, int position);

        void onItemLongClick(T entity, int position);
    }
}
