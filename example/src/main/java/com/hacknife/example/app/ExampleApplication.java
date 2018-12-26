package com.hacknife.example.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.hacknife.example.engine.CoverLoader;
import com.hacknife.example.engine.IPlayerChache;
import com.hacknife.example.engine.IjkEngine;
import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.PlayerLifecycleCallbacks;
import com.hacknife.refresh.core.RefreshLayout;
import com.hacknife.refresh.core.footer.ClassicsFooter;
import com.hacknife.refresh.core.header.ClassicsHeader;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Player.setPlayerEngine(new IjkEngine());
        Player.setImageLoader(new CoverLoader());
        Player.setPlayerCache(new IPlayerChache(getApplicationContext()));
        RefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context));
        RefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context));
        registerActivityLifecycleCallbacks(new PlayerLifecycleCallbacks());
    }
}
