package com.hacknife.example.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hacknife.example.R;
import com.hacknife.example.adapter.base.RecyclerViewHolder;
import com.hacknife.example.adapter.base.i.BaseRecyclerViewAdapter;
import com.hacknife.iplayer.DataSource;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RecyclerViewAdapter extends BaseRecyclerViewAdapter<DataSource, RecyclerViewHolder> {
    @Inject
    public RecyclerViewAdapter() {
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
    }

}
