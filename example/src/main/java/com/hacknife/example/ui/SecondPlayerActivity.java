package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.constant.Constant;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.SecondPlayerModule;

import android.os.Bundle;

import com.hacknife.example.ui.viewmodel.i.ISecondPlayerViewModel;

import com.hacknife.example.ui.view.ISecondPlayerView;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerSecondPlayerActivityComponent;
import com.hacknife.iplayer.Player;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_second_player)
public class SecondPlayerActivity extends BaseActivity<ISecondPlayerViewModel, SecondPlayerActivityBriefnessor> implements ISecondPlayerView {

    @Override
    protected void injector() {
        DaggerSecondPlayerActivityComponent.builder()
                .secondPlayerModule(new SecondPlayerModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        briefnessor.toolBar_title.setText("爱播-TinyPlayer+FullPlayer");
        briefnessor.player.setDataSource(Constant.url[0], Constant.title[0], Constant.img[0]);
    }

    public void onTinyClick() {
        briefnessor.player.startTinyPlayer();
    }

    public void onFullClick() {
        briefnessor.player.startFullscreenPlayer();
    }

    @Override
    public void onBackPressed() {
        if (Player.backPress()) return;
        super.onBackPressed();
    }
}
