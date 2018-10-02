package com.hacknife.demo;

import android.app.Activity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import com.hacknife.iplayer.Video;
import com.hacknife.iplayer.Iplayer;

/**
 * Created by Nathen on 2017/9/19.
 */

public class ActivityApiExtendsNormal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends_normal);
        Iplayer jzvdStd = findViewById(R.id.videoplayer);
        jzvdStd.setUp(VideoConstant.videoUrlList[0], "饺子不信"
                , Iplayer.SCREEN_WINDOW_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbList[0])
                .into(jzvdStd.thumbImageView);
    }

    @Override
    public void onBackPressed() {
        if (Video.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Video.releaseAllVideos();
    }
}
