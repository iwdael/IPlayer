package com.example.iplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.iplayer.R;
import com.aliletter.iplayer.util.MediaQuality;
import com.aliletter.iplayer.widget.IPlayer;

import java.util.List;

public class FullScreenVideoActivity extends AppCompatActivity {
    protected IPlayer ijk_view;
    protected List<MediaQuality> url;
    protected int msec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
 //       setContentView(R.layout.activity_full_screen_video);
        ijk_view = (IPlayer) findViewById(R.id.ijk_view);
        url = getIntent().getParcelableArrayListExtra("url");
        msec = getIntent().getIntExtra("duration", 0);
        ijk_view.setVideoUrl(url.get(0).url);
        ijk_view.seekTo(msec);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ijk_view.onResume();
        ijk_view.start();
        //
    }

    @Override
    protected void onPause() {
        super.onPause();
        ijk_view.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ijk_view.onDestroy();
    }
}
