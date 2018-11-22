package com.hacknife.demo;

import android.app.Activity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.IPlayer;

import static com.hacknife.iplayer.ContainerMode.CONTAINER_MODE_NORMAL;

/**
 * Created by Nathen on 2017/9/19.
 */

public class ActivityApiExtendsNormal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends_normal);
        IPlayer jzvdStd = findViewById(R.id.videoplayer);
        jzvdStd.setDataSource(VideoConstant.videoUrlList[0], "饺子不信"
                , CONTAINER_MODE_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbList[0])
                .into(jzvdStd.iv_thumb);
    }

    @Override
    public void onBackPressed() {
        if (Player.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Player.releaseAllVideos();
    }
}
