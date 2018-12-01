package com.hacknife.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hacknife.example.R;
import com.hacknife.example.adapter.base.ListViewHolder;
import com.hacknife.example.adapter.base.ListViewMultiHolderViewHolder;
import com.hacknife.example.adapter.base.i.BaseListViewAdapter;
import com.hacknife.example.adapter.base.i.BaseListViewHolder;
import com.hacknife.iplayer.DataSource;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class ListViewMultiHolderAdapter extends BaseListViewAdapter<DataSource, BaseListViewHolder> {

    @Inject
    public ListViewMultiHolderAdapter() {
    }

    @Override
    protected BaseListViewHolder onCreateViewHolder(Context context, ViewGroup parent, int itemType) {
        if (itemType == 0)
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
        else
            return new ListViewMultiHolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_multi_holder, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}
