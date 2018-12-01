package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.adapter.ListViewAdapter;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.ListViewModule;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.AbsListView;

import com.hacknife.example.ui.viewmodel.i.IListViewViewModel;

import com.hacknife.example.ui.view.IListViewView;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerListViewActivityComponent;
import com.hacknife.iplayer.DataSource;
import com.hacknife.iplayer.OnPlayerAttachStateChangeListener;
import com.hacknife.iplayer.Player;

import java.util.List;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_list_view)
public class ListViewActivity extends BaseActivity<IListViewViewModel, ListViewActivityBriefnessor> implements IListViewView {

    @Inject
    ListViewAdapter adapter;

    @Override
    protected void injector() {
        DaggerListViewActivityComponent.builder()
                .listViewModule(new ListViewModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        briefnessor.toolBar_title.setText("爱播-ListView");


        briefnessor.listView.setOnScrollListener(new OnPlayerAttachStateChangeListener());
        briefnessor.listView.setAdapter(adapter);
        viewModel.loadDataSource();
    }

    @Override
    public void callbackDataSource(List<DataSource> dataSources) {
        adapter.bindData(dataSources);
    }


    @Override
    public void onBackPressed() {
        if (Player.backPress()) return;
        super.onBackPressed();
    }
}
