package com.hacknife.example.adapter;

import com.hacknife.briefness.Briefnessor;
import com.hacknife.example.R;
import com.hacknife.example.adapter.base.RecyclerViewHolder;
import com.hacknife.example.adapter.base.i.BaseRecyclerViewAdapter;
import com.hacknife.example.adapter.base.i.BaseRecyclerViewHolder;
import com.hacknife.iplayer.DataSource;


import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.hacknife.example.adapter.base.RecyclerViewMultiHolderViewHolder;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RecyclerViewMultiHolderAdapter extends BaseRecyclerViewAdapter<DataSource, BaseRecyclerViewHolder> {
    @Inject
    public RecyclerViewMultiHolderAdapter() {
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new RecyclerViewMultiHolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_multi_holder, parent, false));
        else
            return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}
