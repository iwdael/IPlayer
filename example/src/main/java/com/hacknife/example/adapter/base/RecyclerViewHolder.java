package com.hacknife.example.adapter.base;


import android.view.View;

import com.hacknife.briefness.BindLayout;
import com.hacknife.example.R;
import com.hacknife.example.adapter.base.i.BaseRecyclerViewHolder;
import com.hacknife.iplayer.DataSource;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.item_recycler_view)
public class RecyclerViewHolder extends BaseRecyclerViewHolder<DataSource, RecyclerViewHolderBriefnessor> {


    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(DataSource entity) {
        briefnessor.iplayer.setDataSource(entity);
    }


}