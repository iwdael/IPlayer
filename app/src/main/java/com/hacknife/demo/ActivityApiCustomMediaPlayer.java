package com.hacknife.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hacknife.demo.CustomMediaPlayer.ExoEngine;
import com.bumptech.glide.Glide;
import com.hacknife.demo.CustomMediaPlayer.CustomEngine;
import com.hacknife.demo.CustomMediaPlayer.IjkEngine;

import java.io.IOException;

import com.hacknife.iplayer.DataSource;
import com.hacknife.iplayer.MediaEngine;
import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.IPlayer;

/**
 * Created by Nathen on 2017/11/23.
 */

public class ActivityApiCustomMediaPlayer extends AppCompatActivity implements View.OnClickListener {
    Button mChangeToIjk, mChangeToSystemMediaPlayer, mChangeToExo;
    IPlayer jzvdStd;
    Handler handler = new Handler();//这里其实并不需要handler，为了防止播放中切换播放器引擎导致的崩溃，实际使用时一般不会遇到，可以随时调用JZVideoPlayer.setMediaInterface();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("CustomMediaPlayer");
        setContentView(R.layout.activity_api_custom_mediaplayer);

        jzvdStd = findViewById(R.id.videoplayer);
        mChangeToIjk = findViewById(R.id.change_to_ijkplayer);
        mChangeToSystemMediaPlayer = findViewById(R.id.change_to_system_mediaplayer);
        mChangeToExo = findViewById(R.id.change_to_exo);

        mChangeToIjk.setOnClickListener(this);
        mChangeToSystemMediaPlayer.setOnClickListener(this);
        mChangeToExo.setOnClickListener(this);

        DataSource jzDataSource = null;
        try {
            jzDataSource = new DataSource(getAssets().openFd("local_video.mp4"));
            jzDataSource.title = "饺子快长大";
        } catch (IOException e) {
            e.printStackTrace();
        }
        jzvdStd.setDataSource(jzDataSource, IPlayer.SCREEN_WINDOW_NORMAL);
        Glide.with(this)
                .load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
                .into(jzvdStd.iv_thumb);

        Player.setPlayerEngine(new CustomEngine());//进入此页面修改MediaInterface，让此页面的jzvd正常工作
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_to_ijkplayer:
                Player.releaseAllVideos();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Player.setPlayerEngine(new IjkEngine());
                    }
                }, 1000);
                Toast.makeText(ActivityApiCustomMediaPlayer.this, "Change to Ijkplayer", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.change_to_system_mediaplayer:
                Player.releaseAllVideos();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Player.setPlayerEngine(new MediaEngine());
                    }
                }, 1000);
                Toast.makeText(this, "Change to MediaPlayer", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.change_to_exo:
                Player.releaseAllVideos();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Player.setPlayerEngine(new ExoEngine());
                    }
                }, 1000);
                Toast.makeText(this, "Change to ExoPlayer", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (Player.backPress()) {
            return;
        }
        Player.releaseAllVideos();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Player.setPlayerEngine(new MediaEngine());
            }
        }, 1000);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Player.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Player.releaseAllVideos();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Player.setPlayerEngine(new MediaEngine());
                    }
                }, 1000);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
