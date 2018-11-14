package com.hacknife.iplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Nathen on 2017/11/8.
 * 实现系统的播放引擎
 */
public class MediaEngine extends PlayerEngine implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void prepare() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(dataSource.looping);
            mediaPlayer.setOnPreparedListener(MediaEngine.this);
            mediaPlayer.setOnCompletionListener(MediaEngine.this);
            mediaPlayer.setOnBufferingUpdateListener(MediaEngine.this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(MediaEngine.this);
            mediaPlayer.setOnErrorListener(MediaEngine.this);
            mediaPlayer.setOnInfoListener(MediaEngine.this);
            mediaPlayer.setOnVideoSizeChangedListener(MediaEngine.this);
            Class<MediaPlayer> clazz = MediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
            method.invoke(mediaPlayer, dataSource.getCurrentUrl().toString(), dataSource.headerMap);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
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
        try {
            mediaPlayer.seekTo((int) time);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
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
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        if (dataSource.getCurrentUrl().toString().toLowerCase().contains("mp3") ||
                dataSource.getCurrentUrl().toString().toLowerCase().contains("wav")) {
            MediaManager.instance().pMainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (VideoManager.getCurrentVideo() != null) {
                        VideoManager.getCurrentVideo().onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        MediaManager.instance().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoManager.getCurrentVideo() != null) {
                    VideoManager.getCurrentVideo().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
        MediaManager.instance().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoManager.getCurrentVideo() != null) {
                    VideoManager.getCurrentVideo().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        MediaManager.instance().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoManager.getCurrentVideo() != null) {
                    VideoManager.getCurrentVideo().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        MediaManager.instance().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoManager.getCurrentVideo() != null) {
                    VideoManager.getCurrentVideo().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        MediaManager.instance().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoManager.getCurrentVideo() != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        if (VideoManager.getCurrentVideo().currentState == Video.CURRENT_STATE_PREPARING || VideoManager.getCurrentVideo().currentState == Video.CURRENT_STATE_PREPARING_CHANGING_URL) {
                            VideoManager.getCurrentVideo().onPrepared();
                        }
                    } else {
                        VideoManager.getCurrentVideo().onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        MediaManager.instance().currentVideoWidth = width;
        MediaManager.instance().currentVideoHeight = height;
        MediaManager.instance().pMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoManager.getCurrentVideo() != null) {
                    VideoManager.getCurrentVideo().onVideoSizeChanged();
                }
            }
        });
    }
}
