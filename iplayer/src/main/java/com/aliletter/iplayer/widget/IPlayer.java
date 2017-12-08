package com.aliletter.iplayer.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.aliletter.iplayer.R;
import com.aliletter.iplayer.util.MediaQuality;
import com.aliletter.iplayer.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Date: 2017/11/16.
 */

public class IPlayer extends IjkVideoView {

    protected boolean initail = true;
    protected boolean fullScreenIconEnable = true;
    private Activity activity;

    public IPlayer(Context context) {
        this(context, null);
    }

    public IPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IPlayer);
        fullScreenIconEnable = ta.getBoolean(R.styleable.IPlayer_fullScreenIconEnable, true);
        ta.recycle();
        _init(context);
    }

    public IPlayer bind(Activity activity) {
        this.activity = activity;
        return this;
    }

    private void _init(Context context) {
        setBackgroundColor(Color.argb(255, 0, 0, 0));
        _initSo();
        _initController(context);
    }

    private void _initController(Context context) {
        setMediaController(new IMediaController(context) {
            @Override
            public void onPause() {
                if (onIPlayerStatusListener != null) {
                    onIPlayerStatusListener.onPause();
                }
            }

            @Override
            public void onStart() {
                if (onIPlayerStatusListener != null) {
                    onIPlayerStatusListener.onStart();
                }
            }
        });
        if (!fullScreenIconEnable) mMediaController.hideFullScreenIcon();
    }

    private void _initSo() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    public void setVideoUrl(String sd, String hd, String ultra, String blu, String _1080) {
        if (sd != null) url.add(new MediaQuality(MediaQuality.SD, sd));
        if (hd != null) url.add(new MediaQuality(MediaQuality.HD, hd));
        if (ultra != null) url.add(new MediaQuality(MediaQuality.ULTRA, ultra));
        if (blu != null) url.add(new MediaQuality(MediaQuality.BLU, blu));
        if (_1080 != null) url.add(new MediaQuality(MediaQuality._1080, _1080));
        setVideoPath(url.get(0).url);
    }


    public void setVideoUrl(String path) {
        setVideoUrl(path, null, null, null, null);
    }

    public void startPlay() {
        start();
        mMediaController.checkPlayer(true);
    }

    public void onResume() {
        resume();
        if (!initail) start();
        initail = false;
    }

    public void onPause() {
        pause();
    }


    public void onDestroy() {
        release(false);
        IjkMediaPlayer.native_profileEnd();
    }

    public IPlayer setCover(Bitmap bitmap) {
        mMediaController.setCover(bitmap);
        return this;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    public IMediaController getMediaController() {
        return (IMediaController) mMediaController;
    }

    public void setOnIPlayerStatusListener(OnIPlayerStatusListener onIPlayerStatusListener) {
        this.onIPlayerStatusListener = onIPlayerStatusListener;
    }
}
