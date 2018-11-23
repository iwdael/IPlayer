package com.hacknife.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.hacknife.demo.CustomView.IplayerShowTextureViewAfterAutoComplete;
import com.hacknife.demo.CustomView.IplayerShowTitleAfterFullscreen;

import com.hacknife.iplayer.Player;
import com.hacknife.iplayer.IPlayer;
import com.hacknife.demo.CustomView.IplayerAutoCompleteAfterFullscreen;
import com.hacknife.demo.CustomView.IplayerMp3;
import com.hacknife.demo.CustomView.IplayerShowShareButtonAfterFullscreen;
import com.hacknife.demo.CustomView.IplayerVolumeAfterFullscreen;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_NORMAL;

/**
 * Created by Nathen on 16/7/31.
 */
public class ActivityApiUISmallChange extends AppCompatActivity {
    IplayerShowShareButtonAfterFullscreen jzvdStdWithShareButton;
    IplayerShowTitleAfterFullscreen jzvdStdShowTitleAfterFullscreen;
    IplayerShowTextureViewAfterAutoComplete jzvdStdShowTextureViewAfterAutoComplete;
    IplayerAutoCompleteAfterFullscreen jzvdStdAutoCompleteAfterFullscreen;
    IplayerVolumeAfterFullscreen jzvdStdVolumeAfterFullscreen;
    IplayerMp3 jzvdStdMp3;

    IPlayer jzvdStd_1_1, jzvdStd_16_9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("SmallChangeUI");
        setContentView(R.layout.activity_ui_small_change);

        jzvdStdWithShareButton = findViewById(R.id.custom_videoplayer_standard_with_share_button);
        jzvdStdWithShareButton.setDataSource(VideoConstant.videoUrlList[3], "饺子想呼吸", CONTAINER_MODE_NORMAL
        );
        Glide.with(this)
                .load(VideoConstant.videoThumbList[3])
                .into(jzvdStdWithShareButton.iv_thumb);


        jzvdStdShowTitleAfterFullscreen = findViewById(R.id.custom_videoplayer_standard_show_title_after_fullscreen);
        jzvdStdShowTitleAfterFullscreen.setDataSource(VideoConstant.videoUrlList[4], "饺子想摇头", CONTAINER_MODE_NORMAL
        );
        Glide.with(this)
                .load(VideoConstant.videoThumbList[4])
                .into(jzvdStdShowTitleAfterFullscreen.iv_thumb);

        jzvdStdShowTextureViewAfterAutoComplete = findViewById(R.id.custom_videoplayer_standard_show_textureview_aoto_complete);
        jzvdStdShowTextureViewAfterAutoComplete.setDataSource(VideoConstant.videoUrlList[5], "饺子想旅行", CONTAINER_MODE_NORMAL
        );
        Glide.with(this)
                .load(VideoConstant.videoThumbList[5])
                .into(jzvdStdShowTextureViewAfterAutoComplete.iv_thumb);

        jzvdStdAutoCompleteAfterFullscreen = findViewById(R.id.custom_videoplayer_standard_aoto_complete);
        jzvdStdAutoCompleteAfterFullscreen.setDataSource(VideoConstant.videoUrls[0][1], "饺子没来",  CONTAINER_MODE_NORMAL
        );
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStdAutoCompleteAfterFullscreen.iv_thumb);

        jzvdStd_1_1 = findViewById(R.id.jz_videoplayer_1_1);
        jzvdStd_1_1.setDataSource(VideoConstant.videoUrls[0][1], "饺子有事吗",  CONTAINER_MODE_NORMAL
        );
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStd_1_1.iv_thumb);
        jzvdStd_1_1.setWidthRatio(1);
        jzvdStd_1_1.setHeightRatio(1);

        jzvdStd_16_9 = findViewById(R.id.jz_videoplayer_16_9);
        jzvdStd_16_9.setDataSource(VideoConstant.videoUrls[0][1], "饺子来不了", CONTAINER_MODE_NORMAL
        );
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStd_16_9.iv_thumb);
        jzvdStd_16_9.setWidthRatio(16);
        jzvdStd_16_9.setHeightRatio(9);

        jzvdStdVolumeAfterFullscreen = findViewById(R.id.jz_videoplayer_volume);
        jzvdStdVolumeAfterFullscreen.setDataSource(VideoConstant.videoUrls[0][1], "饺子摇摆",  CONTAINER_MODE_NORMAL
        );
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStdVolumeAfterFullscreen.iv_thumb);

        jzvdStdMp3 = findViewById(R.id.jz_videoplayer_mp3);
        jzvdStdMp3.setDataSource("https://in-20170815011809382-q34ludd68h.oss-cn-shanghai.aliyuncs.com/video/401edae1-16431aa8156-0007-1823-c86-de200.mp3?Expires=1532102862&OSSAccessKeyId=LTAIPZHZDaUNpnca&Signature=apruidffjNeN0O584VJiz8q1mJ4%3D",
                "饺子你听",  CONTAINER_MODE_NORMAL
        );
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStdMp3.iv_thumb);


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
