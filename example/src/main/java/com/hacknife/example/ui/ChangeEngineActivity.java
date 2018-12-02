package com.hacknife.example.ui;

import android.widget.Toast;

import com.hacknife.example.R;
import com.hacknife.example.engine.ExoEngine;
import com.hacknife.example.engine.IjkEngine;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.ChangeEngineModule;

import com.hacknife.example.ui.viewmodel.i.IChangeEngineViewModel;

import com.hacknife.example.ui.view.IChangeEngineView;

import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerChangeEngineActivityComponent;
import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.engine.MediaEngine;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_change_engine)
public class ChangeEngineActivity extends BaseActivity<IChangeEngineViewModel, ChangeEngineActivityBriefnessor> implements IChangeEngineView {

    @Override
    protected void injector() {
        DaggerChangeEngineActivityComponent.builder()
                .changeEngineModule(new ChangeEngineModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        briefnessor.toolBar_title.setText("爱播-Change Player Engine");
    }

    public void onIjkClick() {
        Player.setPlayerEngine(new IjkEngine());
        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
    }

    public void onExoClick() {
        Player.setPlayerEngine(new ExoEngine());
        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
    }

    public void onMediaClick() {
        Player.setPlayerEngine(new MediaEngine());
        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
    }

}
