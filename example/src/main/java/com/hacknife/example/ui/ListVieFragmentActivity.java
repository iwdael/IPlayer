package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.adapter.FragmentAdapter;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.FragmentAdapterModule;
import com.hacknife.example.ui.injector.modules.ListVieFragmentModule;

import android.os.Bundle;

import com.hacknife.example.ui.viewmodel.i.IListVieFragmentViewModel;

import com.hacknife.example.ui.view.IListVieFragmentView;
import com.hacknife.example.ui.viewmodel.ListVieFragmentViewModel;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerListVieFragmentActivityComponent;
import com.hacknife.iplayer.OnPlayerAttachStateChangeListener;
import com.hacknife.iplayer.Player;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_list_vie_fragment)
public class ListVieFragmentActivity extends BaseActivity<IListVieFragmentViewModel, ListVieFragmentActivityBriefnessor> implements IListVieFragmentView {
    @Inject
    FragmentAdapter adapter;

    @Override
    protected void injector() {
        DaggerListVieFragmentActivityComponent.builder()
                .listVieFragmentModule(new ListVieFragmentModule(this, briefnessor))
                .fragmentAdapterModule(new FragmentAdapterModule(getSupportFragmentManager()))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        briefnessor.toolBar_title.setText("爱播-ListView+ViewPager+Fragment");
        adapter.bindData(new RecyclerFragment(), new RecyclerFragment(), new RecyclerFragment(), new RecyclerFragment());
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
