package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.adapter.RecyclerViewMultiHolderAdapter;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.RecyclerViewMultiHolderModule;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.hacknife.example.ui.viewmodel.i.IRecyclerViewMultiHolderViewModel;

import com.hacknife.example.ui.view.IRecyclerViewMultiHolderView;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerRecyclerViewMultiHolderActivityComponent;
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
@BindLayout(R.layout.activity_recycler_view_multi_holder)
public class RecyclerViewMultiHolderActivity extends BaseActivity<IRecyclerViewMultiHolderViewModel, RecyclerViewMultiHolderActivityBriefnessor> implements IRecyclerViewMultiHolderView {

    @Inject
    RecyclerViewMultiHolderAdapter adapter;

    @Override
    protected void injector() {
        DaggerRecyclerViewMultiHolderActivityComponent.builder()
                .recyclerViewMultiHolderModule(new RecyclerViewMultiHolderModule(this, briefnessor))
                .build().inject(this);
    }
    @Override
    protected void initView() {
        briefnessor.toolBar_title.setText("爱播-RecyclerViewMultiHolder");
        briefnessor.rc_view.addOnChildAttachStateChangeListener(new OnPlayerAttachStateChangeListener());
        briefnessor.rc_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        briefnessor.rc_view.setAdapter(adapter);
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
