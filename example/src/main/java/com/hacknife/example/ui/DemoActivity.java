package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.constant.Constant;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.DemoModule;

import android.content.Intent;

import com.hacknife.example.ui.viewmodel.i.IDemoViewModel;

import com.hacknife.example.ui.view.IDemoView;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerDemoActivityComponent;
import com.hacknife.iplayer.IPlayer;
import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.state.ContainerMode;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_demo)
public class DemoActivity extends BaseActivity<IDemoViewModel, DemoActivityBriefnessor> implements IDemoView {

    @Override
    protected void injector() {
        DaggerDemoActivityComponent.builder()
                .demoModule(new DemoModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        briefnessor.toolBar_title.setText(R.string.app_name);
        briefnessor.player.setDataSource(Constant.url[0], Constant.title[0], Constant.img[0], ContainerMode.CONTAINER_MODE_NORMAL);
    }

    public void onRecyclerViewClick() {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (Player.backPress()) return;
        super.onBackPressed();
    }


    public void onListViewClick() {
        startActivity(new Intent(this, ListViewActivity.class));
    }

    public void onRecyclerViewMultiHolderClick() {
        startActivity(new Intent(this, RecyclerViewMultiHolderActivity.class));
    }

    public void onListViewMultiHolderClick() {
        startActivity(new Intent(this, ListViewMultiHolderActivity.class));

    }

    public void onRecyclerViewFragmentClick() {
        startActivity(new Intent(this, RecyclerFragmentActivity.class));
    }
    public void onTinyPlayClick() {
        Player.openTinyPlayer(this, IPlayer.class, Constant.url[0], Constant.title[0], Constant.img[0]);
    }

    public void onFullPlayClick() {
        Player.openFullPlayer(this, IPlayer.class, Constant.url[0], Constant.title[0], Constant.img[0]);
    }

}
