package com.hacknife.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import com.hacknife.example.R;
import com.hacknife.example.adapter.base.ListViewHolder;
import com.hacknife.example.adapter.base.i.BaseListViewAdapter;
import com.hacknife.iplayer.DataSource;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class ListViewAdapter extends BaseListViewAdapter<DataSource, ListViewHolder> {

    @Inject
    public ListViewAdapter() {
    }

    @Override
    protected ListViewHolder onCreateViewHolder(Context context, ViewGroup parent) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
    }
}
