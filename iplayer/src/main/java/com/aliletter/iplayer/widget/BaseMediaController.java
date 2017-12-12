package com.aliletter.iplayer.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;


import com.aliletter.iplayer.R;
import com.aliletter.iplayer.util.MediaQuality;
import com.aliletter.iplayer.widget.media.IMediaController;
import com.aliletter.iplayer.widget.media.IjkVideoView;
import com.aliletter.iplayer.widget.media.MediaPlayerControl;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Data: 2017/11/17.
 */

public abstract class BaseMediaController extends FrameLayout implements IMediaController {
    protected final static int SHOW_CONTROLLER = 1;
    protected final static int SHOW_VIDDEO_TIME = 2;
    protected boolean currentIsSliding = false;
    protected boolean currentIsShowing = false;
    protected Timer timer;
    protected MediaPlayerControl mPlayer;
    protected LinearLayout ll_controller;
    protected boolean inited = false;
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case SHOW_CONTROLLER:
                    hide();
                    break;
            }
        }
    };
    public ArrayList<MediaQuality> url;


    public BaseMediaController(@NonNull Context context) {
        this(context, null);
    }

    public BaseMediaController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseMediaController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _initController(context);
    }

    private void _initController(Context context) {
        timer = new Timer();
        View.inflate(context, R.layout.layout_iplayer_mediacontroller, this);
        ll_controller = findViewById(R.id.ll_controller);
    }


    @Override
    public void hide() {
        setVisibility(ll_controller, GONE);
        currentIsShowing = false;
    }


    @Override
    public boolean isShowing() {
        return currentIsShowing;
    }

    @Override
    public void setAnchorView(View view) {
        if (!inited) {
            ((IjkVideoView) view).addView(this);
        }
        setVisibility(ll_controller, GONE);
    }

    @Override
    public void setUrl(ArrayList<MediaQuality> url) {
        this.url = url;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        mPlayer = (MediaPlayerControl) player;
    }


    @Override
    public void show(int timeout) {
        show();
        Message msg = Message.obtain();
        msg.arg1 = SHOW_CONTROLLER;
        handle.sendMessageDelayed(msg, 1000 * timeout);
    }

    @Override
    public void show() {
        setVisibility(ll_controller, View.VISIBLE);
        currentIsShowing = true;
    }

    @Override
    public void showOnce(View view) {

    }


    public void setVisibility(View view, int visibility) {
        view.setVisibility(visibility);
    }
}
