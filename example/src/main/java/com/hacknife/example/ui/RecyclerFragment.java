package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.adapter.RecyclerViewAdapter;
import com.hacknife.example.ui.base.BaseFragment;
import com.hacknife.example.ui.injector.modules.RecyclerModule;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;


import com.hacknife.example.ui.model.i.IRecyclerFragmentModel;

import com.hacknife.example.ui.view.IRecyclerView;
import com.hacknife.example.ui.viewmodel.RecyclerViewModel;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerRecyclerFragmentComponent;
import com.hacknife.example.ui.viewmodel.i.IRecyclerViewModel;
import com.hacknife.iplayer.DataSource;
import com.hacknife.iplayer.OnPlayerAttachStateChangeListener;

import java.util.List;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.fragment_recycler)
public class RecyclerFragment extends BaseFragment<IRecyclerViewModel, RecyclerFragmentBriefnessor> implements IRecyclerView {
    @Inject
    RecyclerViewAdapter adapter;

    @Override
    protected void injector() {
        DaggerRecyclerFragmentComponent.builder()
                .recyclerModule(new RecyclerModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void initView() {
        briefnessor.rc_view.addOnChildAttachStateChangeListener(new OnPlayerAttachStateChangeListener());
        briefnessor.rc_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        briefnessor.rc_view.setAdapter(adapter);
        viewModel.loadDataSource();
    }

    @Override
    public void callbackDataSource(List<DataSource> dataSources) {
        adapter.bindData(dataSources);
    }
}
