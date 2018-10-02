package com.hacknife.demo.CustomMediaPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import java.io.IOException;

import com.hacknife.iplayer.MediaInterface;
import com.hacknife.iplayer.MediaManager;
import com.hacknife.iplayer.VideoMgr;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by Nathen on 2017/11/18.
 */

public class IplayerMediaIjkplayer extends MediaInterface implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnTimedTextListener {
    IjkMediaPlayer ijkMediaPlayer;

    @Override
    public void start() {
        ijkMediaPlayer.start();
    }

    @Override
    public void prepare() {
        ijkMediaPlayer = new IjkMediaPlayer();
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024 * 1024);

        ijkMediaPlayer.setOnPreparedListener(IplayerMediaIjkplayer.this);
        ijkMediaPlayer.setOnVideoSizeChangedListener(IplayerMediaIjkplayer.this);
        ijkMediaPlayer.setOnCompletionListener(IplayerMediaIjkplayer.this);
        ijkMediaPlayer.setOnErrorListener(IplayerMediaIjkplayer.this);
        ijkMediaPlayer.setOnInfoListener(IplayerMediaIjkplayer.this);
        ijkMediaPlayer.setOnBufferingUpdateListener(IplayerMediaIjkplayer.this);
        ijkMediaPlayer.setOnSeekCompleteListener(IplayerMediaIjkplayer.this);
        ijkMediaPlayer.setOnTimedTextListener(IplayerMediaIjkplayer.this);

        try {
            ijkMediaPlayer.setDataSource(jzDataSource.getCurrentUrl().toString());
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
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        ijkMediaPlayer.start();
        if (jzDataSource.getCurrentUrl().toString().toLowerCase().contains("mp3")) {
            MediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (VideoMgr.getCurrentJzvd() != null) {
                        VideoMgr.getCurrentJzvd().onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        MediaManager.instance().currentVideoWidth = iMediaPlayer.getVideoWidth();
        MediaManager.instance().currentVideoHeight = iMediaPlayer.getVideoHeight();
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoMgr.getCurrentJzvd() != null) {
                    VideoMgr.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoMgr.getCurrentJzvd() != null) {
                    VideoMgr.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoMgr.getCurrentJzvd() != null) {
                    VideoMgr.getCurrentJzvd().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoMgr.getCurrentJzvd() != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        VideoMgr.getCurrentJzvd().onPrepared();
                    } else {
                        VideoMgr.getCurrentJzvd().onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, final int percent) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoMgr.getCurrentJzvd() != null) {
                    VideoMgr.getCurrentJzvd().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoMgr.getCurrentJzvd() != null) {
                    VideoMgr.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

    }
}
