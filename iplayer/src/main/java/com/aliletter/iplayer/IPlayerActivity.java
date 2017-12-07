package com.aliletter.iplayer;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.aliletter.iplayer.util.MediaQuality;
import com.aliletter.iplayer.widget.IPlayer;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class IPlayerActivity extends AppCompatActivity {
    protected IPlayer iPlayer;
    protected List<MediaQuality> url;
    protected int msec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_iplayer);
        iPlayer = (IPlayer) findViewById(R.id.iPlayer);
        url = getIntent().getParcelableArrayListExtra("url");
        msec = getIntent().getIntExtra("duration", 0);
        iPlayer.setVideoUrl(url.get(0).url);
        iPlayer.seekTo(msec);
    }


    @Override
    protected void onResume() {
        super.onResume();
        iPlayer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        iPlayer.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPlayer.onDestroy();
    }
}
