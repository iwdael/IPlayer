package com.hacknife.example.ui;


import com.hacknife.example.R;
import com.hacknife.example.adapter.VideoAdapter;
import com.hacknife.example.bean.VideoSource;
import com.hacknife.example.engine.IjkEngine;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.ExampleModule;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.hacknife.example.ui.view.IExampleView;
import com.hacknife.example.ui.viewmodel.ExampleViewModel;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerExampleActivityComponent;
import com.hacknife.example.ui.viewmodel.i.IExampleViewModel;
import com.hacknife.iplayer.MediaManager;
import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.PlayerManager;

import java.util.List;

import javax.inject.Inject;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_FULLSCREEN;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_example)
public class ExampleActivity extends BaseActivity<IExampleViewModel, ExampleActivityBriefnessor> implements IExampleView {
    @Inject
    VideoAdapter adapter;

    @Override
    protected void injector() {
        DaggerExampleActivityComponent.builder()
                .exampleModule(new ExampleModule(this, briefnessor))
                .build().inject(this);
    }

    @Override
    protected void initView() {
        Player.setPlayerEngine(new IjkEngine());
        briefnessor.rc_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        briefnessor.rc_view.setAdapter(adapter);
        briefnessor.rc_view.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Player video = view.findViewById(R.id.iplayer);
                if (video != null && video.getDataSource().containsTheUrl(MediaManager.getCurrentUrl())) {
                    Player player = PlayerManager.getCurrentVideo();
                    if (player != null && player.getContainerMode() != CONTAINER_MODE_FULLSCREEN) {
                        Player.releaseAllPlayer();
                    }
                }
            }
        });
    }

    @Override
    public void callbackVideo(List<VideoSource> dataSources) {
        adapter.bindData(dataSources);
    }
}
