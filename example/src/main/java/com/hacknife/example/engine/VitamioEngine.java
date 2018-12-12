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
 * Created by Nathen on 2017/11/18.
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
        mediaPlayer.setOnPreparedListener(VitamioEngine.this);
        mediaPlayer.setOnVideoSizeChangedListener(VitamioEngine.this);
        mediaPlayer.setOnCompletionListener(VitamioEngine.this);
        mediaPlayer.setOnErrorListener(VitamioEngine.this);
        mediaPlayer.setOnInfoListener(VitamioEngine.this);
        mediaPlayer.setOnBufferingUpdateListener(VitamioEngine.this);
        mediaPlayer.setOnSeekCompleteListener(VitamioEngine.this);
        mediaPlayer.setOnTimedTextListener(VitamioEngine.this);

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
        if (mediaPlayer != null)
            return mediaPlayer.isPlaying();
        else return false;
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
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        else return 0;
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null)
            return mediaPlayer.getDuration();
        else return 0;
    }

    @Override
    public void setSurface(Surface surface) {
        if (mediaPlayer != null)
            mediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void setSpeed(float speed) {
        if (mediaPlayer != null)
            mediaPlayer.setPlaybackSpeed(speed);
    }

    @Override
    public void onPrepared(MediaPlayer iMediaPlayer) {
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
    public void onVideoSizeChanged(MediaPlayer iMediaPlayer, int i, int i1) {
        MediaManager.get().currentVideoWidth = iMediaPlayer.getVideoWidth();
        MediaManager.get().currentVideoHeight = iMediaPlayer.getVideoHeight();
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
    public void onCompletion(MediaPlayer iMediaPlayer) {
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
    public boolean onError(MediaPlayer iMediaPlayer, final int what, final int extra) {
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
    public boolean onInfo(MediaPlayer iMediaPlayer, final int what, final int extra) {
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    PlayerManager.getCurrentPlayer().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer iMediaPlayer, final int percent) {
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
    public void onSeekComplete(MediaPlayer iMediaPlayer) {
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
