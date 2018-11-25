package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.adapter.RecyclerViewAdapter;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.RecyclerViewModule;

import android.support.v7.widget.LinearLayoutManager;

import com.hacknife.example.ui.viewmodel.i.IRecyclerViewViewModel;

import com.hacknife.example.ui.view.IRecyclerViewView;

import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerRecyclerViewActivityComponent;
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
@BindLayout(R.layout.activity_recycler_view)
public class RecyclerViewActivity extends BaseActivity<IRecyclerViewViewModel, RecyclerViewActivityBriefnessor> implements IRecyclerViewView {
    @Inject
    RecyclerViewAdapter adapter;

    @Override
    protected void injector() {
        DaggerRecyclerViewActivityComponent.builder()
                .recyclerViewModule(new RecyclerViewModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected void initView() {
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
