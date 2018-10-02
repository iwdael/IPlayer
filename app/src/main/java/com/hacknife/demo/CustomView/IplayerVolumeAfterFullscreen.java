package com.hacknife.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;

import com.hacknife.iplayer.MediaManager;
import com.hacknife.iplayer.Iplayer;

/**
 * Created by pc on 2018/1/17.
 */

public class IplayerVolumeAfterFullscreen extends Iplayer {
    public IplayerVolumeAfterFullscreen(Context context) {
        super(context);
    }

    public IplayerVolumeAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            MediaManager.instance().jzMediaInterface.setVolume(1f, 1f);
        } else {
            MediaManager.instance().jzMediaInterface.setVolume(0f, 0f);
        }
    }

    /**
     * 进入全屏模式的时候关闭静音模式
     */
    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        MediaManager.instance().jzMediaInterface.setVolume(1f, 1f);
    }

    /**
     * 退出全屏模式的时候开启静音模式
     */
    @Override
    public void playOnThisJzvd() {
        super.playOnThisJzvd();
        MediaManager.instance().jzMediaInterface.setVolume(0f, 0f);
    }
}
