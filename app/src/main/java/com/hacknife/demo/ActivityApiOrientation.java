package com.hacknife.demo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.IPlayer;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_NORMAL;

/**
 * Created by Nathen on 2016/12/30.
 */
public class ActivityApiOrientation extends AppCompatActivity {
    IPlayer iPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("Orientation");
        setContentView(R.layout.activity_orientation);
        iPlayer = findViewById(R.id.jz_video);
        iPlayer.setDataSource(VideoConstant.videoUrlList[0], "饺子不信"
                , CONTAINER_MODE_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbList[0])
                .into(iPlayer.iv_thumb);
        iPlayer.setOrientationNormal(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        iPlayer.setOrientationFullScreen(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
        Player.releaseAllPlayer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
