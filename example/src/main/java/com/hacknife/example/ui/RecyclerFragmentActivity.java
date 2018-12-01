package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.adapter.FragmentAdapter;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.FragmentAdapterModule;
import com.hacknife.example.ui.injector.modules.RecyclerFragmentModule;

import android.os.Bundle;

import com.hacknife.example.ui.viewmodel.i.IRecyclerFragmentViewModel;

import com.hacknife.example.ui.view.IRecyclerFragmentView;
import com.hacknife.example.ui.viewmodel.RecyclerFragmentViewModel;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerRecyclerFragmentActivityComponent;
import com.hacknife.iplayer.OnPlayerAttachStateChangeListener;
import com.hacknife.iplayer.Player;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_recycler_fragment)
public class RecyclerFragmentActivity extends BaseActivity<IRecyclerFragmentViewModel, RecyclerFragmentActivityBriefnessor> implements IRecyclerFragmentView {
    @Inject
    FragmentAdapter adapter;

    @Override
    protected void injector() {
        DaggerRecyclerFragmentActivityComponent.builder()
                .recyclerFragmentModule(new RecyclerFragmentModule(this, briefnessor))
                .fragmentAdapterModule(new FragmentAdapterModule(getSupportFragmentManager()))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        briefnessor.tabLayout.addTab(briefnessor.tabLayout.newTab().setText("推荐"));
        briefnessor.tabLayout.addTab(briefnessor.tabLayout.newTab().setText("本地"));
        adapter.bindData(new RecyclerFragment(), new RecyclerFragment());
        briefnessor.viewPager.setAdapter(adapter);
        briefnessor.tabLayout.setupWithViewPager(briefnessor.viewPager);
        briefnessor.tabLayout.setTabsFromPagerAdapter(adapter);
        briefnessor.viewPager.addOnPageChangeListener(new OnPlayerAttachStateChangeListener());
    }

    @Override
    public void onBackPressed() {
        if (Player.backPress()) return;
        super.onBackPressed();
    }
}
