package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.adapter.ListViewMultiHolderAdapter;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.ListViewMultiHolderModule;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.hacknife.example.ui.viewmodel.i.IListViewMultiHolderViewModel;
import com.hacknife.example.ui.view.IListViewMultiHolderView;
import com.hacknife.example.ui.viewmodel.ListViewMultiHolderViewModel;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerListViewMultiHolderActivityComponent;
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
@BindLayout(R.layout.activity_list_view_multi_holder)
public class ListViewMultiHolderActivity extends BaseActivity<IListViewMultiHolderViewModel, ListViewMultiHolderActivityBriefnessor> implements IListViewMultiHolderView {
    @Inject
    ListViewMultiHolderAdapter adapter;

    @Override
    protected void injector() {
        DaggerListViewMultiHolderActivityComponent.builder()
                .listViewMultiHolderModule(new ListViewMultiHolderModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        briefnessor.toolBar_title.setText("爱播-ListViewMultiHolder");
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
