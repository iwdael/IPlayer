package com.hacknife.example.ui;

import com.hacknife.example.R;
import com.hacknife.example.constant.Constant;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.RotateModule;

import android.os.Bundle;
import android.widget.SeekBar;

import com.hacknife.example.ui.viewmodel.i.IRotateViewModel;

import com.hacknife.example.ui.view.IRotateView;
import com.hacknife.example.ui.viewmodel.RotateViewModel;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerRotateActivityComponent;
import com.hacknife.iplayer.Player;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_rotate)
public class RotateActivity extends BaseActivity<IRotateViewModel, RotateActivityBriefnessor> implements IRotateView {

    @Override
    protected void injector() {
        DaggerRotateActivityComponent.builder()
                .rotateModule(new RotateModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        briefnessor.toolBar_title.setText("爱播-Rotate");
        briefnessor.player.setDataSource(Constant.url[0], Constant.title[0], Constant.img[0]);
        briefnessor.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                briefnessor.player.setScreenRotation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }    @Override
    public void onBackPressed() {
        if (Player.backPress()) return;
        super.onBackPressed();
    }
}
