package com.hacknife.example.adapter.base.i;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hacknife.briefness.Briefness;
import com.hacknife.briefness.Briefnessor;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public abstract class BaseRecyclerViewHolder<T, B extends Briefnessor> extends RecyclerView.ViewHolder {
    protected B briefnessor;
    protected T entity;
    protected int position;
    protected int size;
    protected BaseRecyclerViewAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        briefnessor = (B) Briefness.bind(this, itemView);

    }

    public void bindData(T t, int size, int position) {
        entity = t;
        this.size = size;
        this.position = position;
        if (t != null)
            bindData(t);
    }

    public abstract void bindData(T t);

    public void setOnRecyclerViewListener(BaseRecyclerViewAdapter.OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
        if (onRecyclerViewListener != null) {
            itemView.setOnClickListener(v -> {
                onRecyclerViewListener.onItemClick(entity, position);
            });
            itemView.setOnLongClickListener(v -> {
                onRecyclerViewListener.onItemLongClick(entity, position);
                return false;
            });
        }
    }
}
