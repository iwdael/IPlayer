package com.hacknife.example.engine;

import android.content.Context;
import android.media.AudioManager;
import android.view.Surface;

import com.hacknife.iplayer.MediaManager;
import com.hacknife.iplayer.PlayerManager;
import com.hacknife.iplayer.engine.PlayerEngine;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;

/**
 * Created by Hacknife on 2018/12/11.
 */

public class VitamioEngine extends PlayerEngine implements MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnTimedTextListener {
    MediaPlayer mediaPlayer;
    Context context;

    public VitamioEngine(Context context) {
        this.context = context;
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void prepare() {
        mediaPlayer = new MediaPlayer(context);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnTimedTextListener(this);
        try {
            mediaPlayer.setDataSource(dataSource.getCurrentUrl().toString());
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        mediaPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (mediaPlayer != null)
            mediaPlayer.reset();
    }

    @Override
    public long getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public void setSurface(Surface surface) {
        mediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void setSpeed(float speed) {
        mediaPlayer.setPlaybackSpeed(speed);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    PlayerManager.getCurrentPlayer().onPrepared();
                }
            }
        });
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        MediaManager.get().currentVideoWidth = mp.getVideoWidth();
        MediaManager.get().currentVideoHeight = mp.getVideoHeight();
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    PlayerManager.getCurrentPlayer().onVideoSizeChanged();
                }
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    PlayerManager.getCurrentPlayer().onPlayCompletion();
                }
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    PlayerManager.getCurrentPlayer().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    if (what == android.media.MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        PlayerManager.getCurrentPlayer().onPrepared();
                    } else {
                        PlayerManager.getCurrentPlayer().onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    PlayerManager.getCurrentPlayer().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    PlayerManager.getCurrentPlayer().onSeekComplete();
                }
            }
        });
    }

    @Override
    public void onTimedText(String text) {

    }

    @Override
    public void onTimedTextUpdate(byte[] pixels, int width, int height) {

    }
}
