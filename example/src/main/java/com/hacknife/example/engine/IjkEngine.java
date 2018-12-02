package com.hacknife.example.engine;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import com.hacknife.iplayer.MediaManager;
import com.hacknife.iplayer.PlayerManager;
import com.hacknife.iplayer.engine.PlayerEngine;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by Nathen on 2017/11/18.
 */

public class IjkEngine extends PlayerEngine implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnTimedTextListener {
    IjkMediaPlayer ijkMediaPlayer;

    @Override
    public void start() {
        ijkMediaPlayer.start();
    }

    @Override
    public void prepare() {
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOnPreparedListener(IjkEngine.this);
        ijkMediaPlayer.setOnVideoSizeChangedListener(IjkEngine.this);
        ijkMediaPlayer.setOnCompletionListener(IjkEngine.this);
        ijkMediaPlayer.setOnErrorListener(IjkEngine.this);
        ijkMediaPlayer.setOnInfoListener(IjkEngine.this);
        ijkMediaPlayer.setOnBufferingUpdateListener(IjkEngine.this);
        ijkMediaPlayer.setOnSeekCompleteListener(IjkEngine.this);
        ijkMediaPlayer.setOnTimedTextListener(IjkEngine.this);

        try {
            ijkMediaPlayer.setDataSource(dataSource.getCurrentUrl().toString());
            ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
            ijkMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        ijkMediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return ijkMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        ijkMediaPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (ijkMediaPlayer != null)
            ijkMediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        return ijkMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return ijkMediaPlayer.getDuration();
    }

    @Override
    public void setSurface(Surface surface) {
        ijkMediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        ijkMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void setSpeed(float speed) {
        ijkMediaPlayer.setSpeed(speed);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        ijkMediaPlayer.start();
        if (dataSource.getCurrentUrl().toString().toLowerCase().contains("mp3")) {
            MediaManager.get().pMainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (PlayerManager.getCurrentPlayer() != null) {
                        PlayerManager.getCurrentPlayer().onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
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
    public void onCompletion(IMediaPlayer iMediaPlayer) {
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
    public boolean onError(IMediaPlayer iMediaPlayer, final int what, final int extra) {
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
    public boolean onInfo(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        MediaManager.get().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (PlayerManager.getCurrentPlayer() != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
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
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, final int percent) {
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
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
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
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

    }
}
