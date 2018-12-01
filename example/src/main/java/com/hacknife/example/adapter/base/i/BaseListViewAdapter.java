package com.hacknife.example.adapter.base.i;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hacknife.example.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public abstract class BaseListViewAdapter<T, V extends BaseListViewHolder> extends BaseAdapter {
    List<T> data;

    public BaseListViewAdapter() {
        data = new ArrayList<>();
    }

    @Override


    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V viewHolder;
        if (convertView == null) {
            viewHolder = onCreateViewHolder(parent.getContext(), parent);
            convertView = viewHolder.getView();
            convertView.setTag(R.id.id_list_view_item, viewHolder);
        } else {
            viewHolder = (V) convertView.getTag(R.id.id_list_view_item);
        }
        viewHolder.bindData(data.get(position),position);
        return convertView;
    }

    public void bindData(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    protected abstract V onCreateViewHolder(Context context, ViewGroup parent);
}
