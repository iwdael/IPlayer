package com.hacknife.iplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.hacknife.iplayer.engine.PlayerEngine;
import com.hacknife.iplayer.state.ContainerMode;
import com.hacknife.iplayer.state.PlayerState;
import com.hacknife.iplayer.state.ScreenType;
import com.hacknife.iplayer.util.PlayerUtils;
import com.hacknife.iplayer.util.PreferenceHelper;

import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_FULLSCREEN;
import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_TINY;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_AUTO_COMPLETE;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_ERROR;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_NORMAL;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PAUSE;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PLAYING;
import static com.hacknife.iplayer.util.ToolbarHelper.hideSupportActionBar;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * desc    : Video
 */
public abstract class Player extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    public static final String TAG = "IPlayer";
    public static final int THRESHOLD = 80;
    public static final int FULL_SCREEN_NORMAL_DELAY = 300;


    public static ScreenType SCREEN_TYPE = ScreenType.SCREEN_TYPE_ADAPTER;

    protected static boolean WIFI_TIP_DIALOG_SHOWED = false;
    protected static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    protected static long lastAutoFullscreenTime = 0;

    protected Event event;
    protected Timer progressTimer;
    protected PlayerState playerState = PlayerState.PLAYER_STATE_ORIGINAL;
    protected ContainerMode containerMode = ContainerMode.CONTAINER_MODE_ORIGINAL;
    protected DataSource dataSource;
    protected AudioManager audioManager;
    protected ProgressTimerTask progressTimerTask;

    protected ImageView iv_play;
    protected SeekBar sb_bottom;
    protected ImageView iv_fullscreen;
    protected TextView tv_current_time;
    protected TextView tv_total_time;
    protected ViewGroup fl_surface;
    protected ViewGroup ll_top;
    protected ViewGroup ll_bottom;


    protected boolean touchingSeekBar;
    protected float downX;
    protected float downY;
    protected boolean changeVolume;
    protected boolean changePosition;
    protected boolean changeBrightness;
    protected long gestureDownPosition;
    protected int gestureDownVolume;
    protected float gestureDownBrightness;
    protected long seekTimePosition;
    protected int positionInList = -1;
    protected int screenRotation = 0;
    protected int screenWidth;
    protected int screenHeight;
    protected int widthRatio = 0;
    protected int heightRatio = 0;
    protected long seekToProgress = 0;
    protected boolean saveProgress;
    protected int orientationFullScreen;
    protected int orientationNormal;
    protected boolean tmp_test_back = false;

    protected static OnAudioFocusChangeListener onAudioFocusChangeListener = new OnAudioFocusChangeListener();

    public Player(Context context) {
        super(context);
        init(context, null);
    }

    public Player(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public static void setPlayerEngine(PlayerEngine engine) {
        MediaManager.get().engine = engine;
    }

    public static void releaseAllPlayer() {
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            PlayerManager.completeAll();
            MediaManager.get().positionInList = -1;
            MediaManager.get().releaseMediaPlayer();
        }
    }

    public static void openFullscreenPlayer(Context context, Class _class, String url, String title, int orientation) {
        openFullscreenPlayer(context, _class, new DataSource(url, title), orientation);
    }

    public static void openFullscreenPlayer(Context context, Class _class, String url, String title) {
        openFullscreenPlayer(context, _class, url, title, ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public static void openFullscreenPlayer(Context context, Class _class, DataSource dataSource, int orientation) {
        hideSupportActionBar(context);
        PlayerUtils.setRequestedOrientation(context, orientation);
        ViewGroup vp = (PlayerUtils.scanForActivity(context))
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.iplayer_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        try {
            Constructor<AbsPlayer> constructor = _class.getConstructor(Context.class);
            final AbsPlayer video = constructor.newInstance(context);
            video.setId(R.id.iplayer_fullscreen_id);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(video, lp);
            video.setDataSource(dataSource, CONTAINER_MODE_FULLSCREEN);
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            video.iv_play.performClick();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean backPress() {
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) < FULL_SCREEN_NORMAL_DELAY)
            return false;
        if (PlayerManager.getSecondFloor() != null) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            if (PlayerManager.getFirstFloor().dataSource.containsTheUrl(MediaManager.getDataSource().getCurrentUrl())) {
                AbsPlayer video = PlayerManager.getSecondFloor();
                video.onEvent(video.containerMode == CONTAINER_MODE_FULLSCREEN ? Event.ON_QUIT_FULLSCREEN : Event.ON_QUIT_TINYSCREEN);
                PlayerManager.getFirstFloor().playOnThisVideo();
            } else {
                quitFullscreenOrTinyWindow();
            }
            return true;
        } else if (PlayerManager.getFirstFloor() != null &&
                (PlayerManager.getFirstFloor().containerMode == CONTAINER_MODE_FULLSCREEN || PlayerManager.getFirstFloor().containerMode == CONTAINER_MODE_TINY)) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            quitFullscreenOrTinyWindow();
            return true;
        }
        return false;
    }

    protected static void quitFullscreenOrTinyWindow() {
        //直接退出全屏和小窗
        PlayerManager.getFirstFloor().clearFloatScreen();
        MediaManager.get().releaseMediaPlayer();
        PlayerManager.completeAll();
    }

    public static void clearSavedProgress(Context context, String url) {
        PreferenceHelper.clearSavedProgress(context, url);
    }

    public static void onScrollAutoTiny(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = MediaManager.get().positionInList;
        if (currentPlayPosition >= 0) {
            if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                if (PlayerManager.getCurrentVideo() != null &&
                        PlayerManager.getCurrentVideo().containerMode != CONTAINER_MODE_TINY &&
                        PlayerManager.getCurrentVideo().containerMode != CONTAINER_MODE_FULLSCREEN) {
                    if (PlayerManager.getCurrentVideo().playerState == PLAYER_STATE_PAUSE) {
                        Player.releaseAllPlayer();
                    } else {
                        PlayerManager.getCurrentVideo().startWindowTiny();
                    }
                }
            } else if (PlayerManager.getCurrentVideo() != null && PlayerManager.getCurrentVideo().containerMode == CONTAINER_MODE_TINY) {
                Player.backPress();
            }

        }
    }

    public static void onScrollReleaseAllVideos(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = MediaManager.get().positionInList;
        if (currentPlayPosition >= 0) {
            if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                if (PlayerManager.getCurrentVideo().containerMode != CONTAINER_MODE_FULLSCREEN) {
                    Player.releaseAllPlayer();//为什么最后一个视频横屏会调用这个，其他地方不会
                }
            }
        }
    }

    public static void onChildViewAttachedToWindow(View view, int playerId) {
        if (PlayerManager.getCurrentVideo() != null && PlayerManager.getCurrentVideo().containerMode == CONTAINER_MODE_TINY) {
            Player player = view.findViewById(playerId);
            if (player != null && player.dataSource.containsTheUrl(MediaManager.getCurrentUrl())) {
                Player.backPress();
            }
        }
    }

    public static void onChildViewDetachedFromWindow(View view) {
        if (PlayerManager.getCurrentVideo() != null && PlayerManager.getCurrentVideo().containerMode != CONTAINER_MODE_TINY) {
            AbsPlayer video = PlayerManager.getCurrentVideo();
            if (((ViewGroup) view).indexOfChild(video) != -1) {
                if (video.playerState == PLAYER_STATE_PAUSE) {
                    Player.releaseAllPlayer();
                } else {
                    video.startWindowTiny();
                }
            }
        }
    }

    public static void setTextureViewRotation(int rotation) {
        if (MediaManager.textureView != null) {
            MediaManager.textureView.setRotation(rotation);
        }
    }

    public static void setScreenType(ScreenType type) {
        Player.SCREEN_TYPE = type;
        if (MediaManager.textureView != null) {
            MediaManager.textureView.requestLayout();
        }
    }

    public static void resume() {
        if (PlayerManager.getCurrentVideo() != null) {
            AbsPlayer player = PlayerManager.getCurrentVideo();
            if (player.playerState == PLAYER_STATE_PAUSE) {
                if (ON_PLAY_PAUSE_TMP_STATE == PLAYER_STATE_PAUSE) {
                    player.onStatePause();
                    MediaManager.pause();
                } else {
                    player.onStatePlaying();
                    MediaManager.start();
                }
                ON_PLAY_PAUSE_TMP_STATE = PLAYER_STATE_NORMAL;
            }
        }
    }

    protected static PlayerState ON_PLAY_PAUSE_TMP_STATE = PlayerState.PLAYER_STATE_NORMAL;

    public static void pause() {
        if (PlayerManager.getCurrentVideo() != null) {
            AbsPlayer video = PlayerManager.getCurrentVideo();
            if (video.playerState == PLAYER_STATE_AUTO_COMPLETE || video.playerState == PLAYER_STATE_NORMAL || video.playerState == PLAYER_STATE_ERROR) {
            } else {
                ON_PLAY_PAUSE_TMP_STATE = video.playerState;
                video.onStatePause();
                MediaManager.pause();
            }
        }
    }

    public static class AutoFullscreenListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {//可以得到传感器实时测量出来的变化值
            final float x = event.values[SensorManager.DATA_X];
            float y = event.values[SensorManager.DATA_Y];
            float z = event.values[SensorManager.DATA_Z];
            //过滤掉用力过猛会有一个反向的大数值
            if (x < -12 || x > 12) {
                if ((System.currentTimeMillis() - lastAutoFullscreenTime) > 2000) {
                    if (PlayerManager.getCurrentVideo() != null) {
                        PlayerManager.getCurrentVideo().autoFullscreen(x);
                    }
                    lastAutoFullscreenTime = System.currentTimeMillis();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (playerState == PLAYER_STATE_PLAYING || playerState == PLAYER_STATE_PAUSE) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        long position = getCurrentPositionWhenPlaying();
                        long duration = getDuration();
                        int progress = (int) (position * 100 / (duration == 0 ? 1 : duration));
                        onProgress(progress, position, duration);
                    }
                });
            }
        }
    }

    public void setSeekToProgress(long seekToProgress) {
        this.seekToProgress = seekToProgress;
    }

    public void setScreenRotation(int screenRotation) {
        this.screenRotation = screenRotation;
    }

    public void setWidthRatio(int widthRatio) {
        this.widthRatio = widthRatio;
    }

    public void setHeightRatio(int heightRatio) {
        this.heightRatio = heightRatio;
    }

    public void setOrientationFullScreen(int orientationFullScreen) {
        this.orientationFullScreen = orientationFullScreen;
    }

    public void setOrientationNormal(int orientationNormal) {
        this.orientationNormal = orientationNormal;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public ContainerMode getContainerMode() {
        return containerMode;
    }

    protected abstract void init(Context context, AttributeSet attrs);

    protected abstract void onProgress(int progress, long position, long duration);

    abstract long getDuration();

    abstract long getCurrentPositionWhenPlaying();

}
