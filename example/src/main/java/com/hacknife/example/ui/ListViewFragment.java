package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.adapter.ListViewAdapter;
import com.hacknife.example.ui.base.BaseFragment;
import com.hacknife.example.ui.injector.modules.ListViewFragModule;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.hacknife.example.ui.viewmodel.i.IListViewFragViewModel;

import com.hacknife.example.ui.view.IListViewFragView;
import com.hacknife.example.ui.viewmodel.ListViewFragViewModel;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerListViewFragmentComponent;
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
@BindLayout(R.layout.fragment_list_view)
public class ListViewFragment extends BaseFragment<IListViewFragViewModel, ListViewFragmentBriefnessor> implements IListViewFragView {
    @Inject
    ListViewAdapter adapter;

    @Override
    protected void injector() {
        DaggerListViewFragmentComponent.builder()
                .listViewFragModule(new ListViewFragModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_list_view;
    }

    @Override
    protected void initView() {
        briefnessor.listView.setOnScrollListener(new OnPlayerAttachStateChangeListener());
        briefnessor.listView.setAdapter(adapter);
        viewModel.loadDataSource();
    }

    @Override
    public void callbackDataSource(List<DataSource> dataSources) {
        adapter.bindData(dataSources);
    }
}
