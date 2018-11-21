package com.hacknife.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;

import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.IPlayer;

/**
 * Created by Nathen on 2017/11/2.
 */

public class ActivityApiRotationVideoSize extends AppCompatActivity implements View.OnClickListener {

    IPlayer myJzvdStd;
    Button mBtnRotation, mBtnFillParent, mBtnFillCrop, mBtnOriginal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("RotationAndVideoSize");
        setContentView(R.layout.activity_api_rotation_videosize);

        myJzvdStd = findViewById(R.id.jz_video);
        myJzvdStd.setDataSource(VideoConstant.videoUrls[0][7], VideoConstant.videoTitles[0][7]
                , IPlayer.CONTAINER_MODE_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][7])
                .into(myJzvdStd.iv_thumb);
        // The Point IS
        myJzvdStd.videoRotation = 180;

        mBtnRotation = findViewById(R.id.rotation_to_90);
        mBtnFillParent = findViewById(R.id.video_image_display_fill_parent);
        mBtnFillCrop = findViewById(R.id.video_image_display_fill_crop);
        mBtnOriginal = findViewById(R.id.video_image_diaplay_original);
        mBtnRotation.setOnClickListener(this);
        mBtnFillParent.setOnClickListener(this);
        mBtnFillCrop.setOnClickListener(this);
        mBtnOriginal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rotation_to_90:
                Player.setTextureViewRotation(90);

                break;
            case R.id.video_image_display_fill_parent:
                Player.setVideoImageDisplayType(Player.SCREEN_TYPE_FILL_PARENT);

                break;
            case R.id.video_image_display_fill_crop:
                Player.setVideoImageDisplayType(Player.SCREEN_TYPE_FILL_SCROP);

                break;
            case R.id.video_image_diaplay_original:
                Player.setVideoImageDisplayType(Player.SCREEN_TYPE_ORIGINAL);

                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Player.releaseAllVideos();
        Player.setVideoImageDisplayType(Player.SCREEN_TYPE_ADAPTER);
    }

    @Override
    public void onBackPressed() {
        if (Player.backPress()) {
            return;
        }
        super.onBackPressed();
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
