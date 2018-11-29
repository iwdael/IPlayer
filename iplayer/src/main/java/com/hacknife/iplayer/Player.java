package com.hacknife.iplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.hacknife.iplayer.engine.PlayerEngine;
import com.hacknife.iplayer.interfaces.*;
import com.hacknife.iplayer.state.ContainerMode;
import com.hacknife.iplayer.state.PlayerState;
import com.hacknife.iplayer.state.ScreenType;
import com.hacknife.iplayer.util.PlayerUtils;
import com.hacknife.iplayer.util.PreferenceHelper;
import com.hacknife.iplayer.interfaces.OnStateChangeListener;

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
 * project : IPlayer
 */
public abstract class Player extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    public static final String TAG = "IPlayer";
    public static final int THRESHOLD = 80;
    public static final int FULL_SCREEN_NORMAL_DELAY = 300;
    protected static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    protected static long lastAutoFullscreenTime = 0;
    protected static OnAudioFocusChangeListener onAudioFocusChangeListener = new OnAudioFocusChangeListener();
    protected OnStateChangeListener onStateChangeListener;
    protected Event event;
    protected Timer progressTimer;
    protected PlayerState playerState = PlayerState.PLAYER_STATE_ORIGINAL;
    protected ContainerMode containerMode = ContainerMode.CONTAINER_MODE_ORIGINAL;
    protected DataSource dataSource;
    protected AudioManager audioManager;
    protected ProgressTimerTask progressTimerTask;
    protected ScreenType screenType;
    protected LayoutParams tinyLp;
    protected ViewGroup contentAndroid; //根布局
    protected ImageView iv_play;
    protected SeekBar sb_bottom;
    protected ImageView iv_fullscreen;
    protected TextView tv_current_time;
    protected TextView tv_total_time;
    protected ViewGroup fl_surface;
    protected ViewGroup ll_top;
    protected ViewGroup ll_bottom;
    protected ImageView iv_thumb;


    protected boolean touchingSeekBar;
    protected float changeX;
    protected float changeY;
    protected int moveX;
    protected int moveY;
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
    //自定义属性
    protected boolean enableTitleBar;
    protected boolean enableBottomBar;
    protected boolean enableBottomProgressBar;
    protected boolean enableEnlarge;
    protected boolean enableClarity;
    protected boolean enableShowWifiDialog;
    protected boolean enableCache;
    protected boolean enableTinyWindow;
    protected int tinyWindowWidth;
    protected int tinyWindowHeight;
    protected ScreenType screenTypeFull;
    protected ScreenType screenTypeNormal;
    protected ScreenType screenTypeTiny;

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
            PlayerManager.releaseAllPlayer();
            MediaManager.get().positionInList = -1;
            MediaManager.get().releasePlayerEngine();
        }
    }

    public static BasePlayer openFullPlayer(Context context, Class<? extends BasePlayer> _class, String url, String title, String cover, int orientation, ScreenType type) {
        return openFullPlayer(context, _class, new DataSource(url, title, cover), orientation, type);
    }

    public static BasePlayer openFullPlayer(Context context, Class<? extends BasePlayer> _class, String url, String title, String cover, int orientation) {
        return openFullPlayer(context, _class, new DataSource(url, title, cover), orientation);
    }

    public static BasePlayer openFullPlayer(Context context, Class<? extends BasePlayer> _class, String url, String title, String cover) {
        return openFullPlayer(context, _class, url, title, cover, ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public static BasePlayer openFullPlayer(Context context, Class<? extends BasePlayer> _class, DataSource dataSource, int orientation) {
        return openFullPlayer(context, _class, dataSource, orientation, ScreenType.SCREEN_TYPE_ADAPTER);
    }

    public static BasePlayer openFullPlayer(Context context, Class<? extends BasePlayer> _class, DataSource dataSource, int orientation, ScreenType type) {
        hideSupportActionBar(context);
        PlayerUtils.setRequestedOrientation(context, orientation);
        ViewGroup vp = (PlayerUtils.scanForActivity(context))
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.iplayer_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        try {
            Constructor<BasePlayer> constructor = (Constructor<BasePlayer>) _class.getConstructor(Context.class);
            final BasePlayer player = constructor.newInstance(context);
            player.setId(R.id.iplayer_fullscreen_id);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(player, lp);
            player.setScreenType(type);
            player.setDataSource(dataSource, CONTAINER_MODE_FULLSCREEN);
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            player.iv_play.performClick();
            return player;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BasePlayer openTinyPlayer(Context context, Class<? extends BasePlayer> _class, String url, String title, String cover) {
        return openTinyPlayer(context, _class, url, title, cover, ScreenType.SCREEN_TYPE_ADAPTER);
    }

    public static BasePlayer openTinyPlayer(Context context, Class<? extends BasePlayer> _class, String url, String title, String cover, ScreenType type) {
        return openTinyPlayer(context, _class, url, title, cover, type, 0, 0);
    }

    public static BasePlayer openTinyPlayer(Context context, Class<? extends BasePlayer> _class, String url, String title, String cover, ScreenType type, int tinyWindowWidth, int tinyWindowHeight) {
        ViewGroup vp = (PlayerUtils.scanForActivity(context))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.iplayer_tiny_id);
        if (old != null) {
            vp.removeView(old);
        }

        try {
            Constructor<BasePlayer> constructor = (Constructor<BasePlayer>) _class.getConstructor(Context.class);
            BasePlayer player = constructor.newInstance(context);
            player.setId(R.id.iplayer_tiny_id);
            FrameLayout.LayoutParams tinyLp;
            if (tinyWindowWidth == 0 || tinyWindowHeight == 0) {
                tinyLp = new FrameLayout.LayoutParams(480, 270);
            } else {
                tinyLp = new FrameLayout.LayoutParams(tinyWindowWidth, tinyWindowHeight);
            }
            tinyLp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            vp.addView(player, tinyLp);
            player.setScreenType(type);
            player.setDataSource(new DataSource(url, title, cover), CONTAINER_MODE_TINY);
            player.iv_play.performClick();
            return player;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean backPress() {
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) < FULL_SCREEN_NORMAL_DELAY)
            return false;
        if (PlayerManager.getSecondFloor() != null) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            BasePlayer video = PlayerManager.getSecondFloor();
            video.onEvent(video.containerMode == CONTAINER_MODE_FULLSCREEN ? Event.ON_QUIT_FULLSCREEN : Event.ON_QUIT_TINYSCREEN);
            PlayerManager.getFirstFloor().playOnSelfPlayer();
            return true;
        } else if (PlayerManager.getFirstFloor() != null &&
                (PlayerManager.getFirstFloor().containerMode == CONTAINER_MODE_FULLSCREEN || PlayerManager.getFirstFloor().containerMode == CONTAINER_MODE_TINY)) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            quitFullscreenOrFloatWindow();
            return true;
        }
        return false;
    }

    protected static void quitFullscreenOrFloatWindow() {
        //退出全屏
        PlayerManager.getFirstFloor().quitFullScreenPlayer();
        PlayerManager.getFirstFloor().quitTinyPlayer();
        MediaManager.get().releasePlayerEngine();
        PlayerManager.releaseAllPlayer();
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
                        PlayerManager.getCurrentVideo().startTinyPlayer();
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


    public static void setTextureRotation(int rotation) {
        if (MediaManager.textureView != null) {
            MediaManager.textureView.setRotation(rotation);
        }
    }

    public static void setImageLoader(ImageLoader loader) {
        MediaManager.setImageLoader(loader);
    }

    public static void resume() {
        if (PlayerManager.getCurrentVideo() != null) {
            BasePlayer player = PlayerManager.getCurrentVideo();
            if (player.playerState == PLAYER_STATE_PAUSE) {
                if (ON_PLAY_PAUSE_TMP_STATE == PLAYER_STATE_PAUSE) {
                    player.onStatePause();
                    MediaManager.pause();
                } else {
                    player.onStatePlay();
                    MediaManager.start();
                }
                ON_PLAY_PAUSE_TMP_STATE = PLAYER_STATE_NORMAL;
            }
        }
    }

    protected static PlayerState ON_PLAY_PAUSE_TMP_STATE = PlayerState.PLAYER_STATE_NORMAL;

    public static void pause() {
        if (PlayerManager.getCurrentVideo() != null) {
            BasePlayer video = PlayerManager.getCurrentVideo();
            if (video.playerState == PLAYER_STATE_AUTO_COMPLETE || video.playerState == PLAYER_STATE_NORMAL || video.playerState == PLAYER_STATE_ERROR) {
            } else {
                ON_PLAY_PAUSE_TMP_STATE = video.playerState;
                video.onStatePause();
                MediaManager.pause();
            }
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

    public void setScreenType(ScreenType type) {
        screenType = type;
        if (MediaManager.textureView != null) {
            MediaManager.textureView.requestLayout();
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public ScreenType getScreenType() {
        return screenType;
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
