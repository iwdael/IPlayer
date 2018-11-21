package com.hacknife.iplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * desc    : Video
 */
public abstract class Player extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    public static final String TAG = "iplayer";
    public static final int THRESHOLD = 80;
    public static final int FULL_SCREEN_NORMAL_DELAY = 300;

    public static final int CONTAINER_MODE_NORMAL = 0;//正常模式
    public static final int CONTAINER_MODE_LIST = 1;//列表
    public static final int CONTAINER_MODE_FULLSCREEN = 2;//全屏
    public static final int CONTAINER_MODE_TINY = 3;//小窗口


    public static final int PLAYER_STATE_NORMAL = 0;//默认状态
    public static final int PLAYER_STATE_PREPARING = 1;//准备中
    public static final int PLAYER_STATE_PREPARING_CHANGING_URL = 2;//切换播放源后准备中
    public static final int PLAYER_STATE_PLAYING = 3;//播放中
    public static final int PLAYER_STATE_PAUSE = 5;//暂停
    public static final int PLAYER_STATE_AUTO_COMPLETE = 6;//播放完
    public static final int PLAYER_STATE_ERROR = 7;//播放错误


    public static final int SCREEN_TYPE_ADAPTER = 0;//default
    public static final int SCREEN_TYPE_FILL_PARENT = 1; //拉伸铺满全屏
    public static final int SCREEN_TYPE_FILL_SCROP = 2;
    public static final int SCREEN_TYPE_ORIGINAL = 3;


    public static boolean ACTION_BAR_EXIST = true;
    public static boolean TOOL_BAR_EXIST = true;
    public static int FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    public static int NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public static boolean SAVE_PROGRESS = true;
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    public static int SCREEN_TYPE = 0;
    public static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    public static long lastAutoFullscreenTime = 0;
    public static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {//是否新建个class，代码更规矩，并且变量的位置也很尴尬
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseAllVideos();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    try {
                        Player player = PlayerManager.getCurrentVideo();
                        if (player != null && player.currentState == Player.PLAYER_STATE_PLAYING) {
                            player.iv_play.performClick();
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };
    protected Event event;
    protected Timer pProgressTimer;
    public int currentState = -1;
    public int currentScreen = -1;
    public long seekToInAdvance = 0;
    protected ImageView iv_play;
    protected SeekBar sb_bottom;
    protected ImageView iv_fullscreen;
    protected TextView tv_current_time, tv_total_time;
    protected ViewGroup fl_surface;
    protected ViewGroup ll_top, ll_bottom;
    public int widthRatio = 0;
    public int heightRatio = 0;
    public DataSource dataSource;
    public int positionInList = -1;
    public int videoRotation = 0;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected AudioManager mAudioManager;
    protected ProgressTimerTask mProgressTimerTask;
    protected boolean mTouchingsb_bottom;
    protected float mDownX;
    protected float mDownY;
    protected boolean mChangeVolume;
    protected boolean mChangePosition;
    protected boolean mChangeBrightness;
    protected long mGestureDownPosition;
    protected int mGestureDownVolume;
    protected float mGestureDownBrightness;
    protected long mSeekTimePosition;
    protected boolean tmp_test_back = false;

    public Player(Context context) {
        super(context);
        init(context,null);
    }

    public Player(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public static void setPlayerEngine(PlayerEngine engine) {
        MediaManager.instance().engine = engine;
    }
    public static void releaseAllVideos() {
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            PlayerManager.completeAll();
            MediaManager.instance().positionInList = -1;
            MediaManager.instance().releaseMediaPlayer();
        }
    }

    public static void startFullscreen(Context context, Class _class, String url, String title) {
        startFullscreen(context, _class, new DataSource(url, title));
    }

    public static void startFullscreen(Context context, Class _class, DataSource dataSource) {
        hideSupportActionBar(context);
        PlayerUtils.setRequestedOrientation(context, FULLSCREEN_ORIENTATION);
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
            video.setDataSource(dataSource, IPlayer.CONTAINER_MODE_FULLSCREEN);
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
                video.onEvent(video.currentScreen == IPlayer.CONTAINER_MODE_FULLSCREEN ?
                        Event.ON_QUIT_FULLSCREEN :
                        Event.ON_QUIT_TINYSCREEN);
                PlayerManager.getFirstFloor().playOnThisVideo();
            } else {
                quitFullscreenOrTinyWindow();
            }
            return true;
        } else if (PlayerManager.getFirstFloor() != null &&
                (PlayerManager.getFirstFloor().currentScreen == CONTAINER_MODE_FULLSCREEN ||
                        PlayerManager.getFirstFloor().currentScreen == CONTAINER_MODE_TINY)) {//以前我总想把这两个判断写到一起，这分明是两个独立是逻辑
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            quitFullscreenOrTinyWindow();
            return true;
        }
        return false;
    }

    public static void quitFullscreenOrTinyWindow() {
        //直接退出全屏和小窗
        PlayerManager.getFirstFloor().clearFloatScreen();
        MediaManager.instance().releaseMediaPlayer();
        PlayerManager.completeAll();
    }

    @SuppressLint("RestrictedApi")
    public static void showSupportActionBar(Context context) {
        if (ACTION_BAR_EXIST && PlayerUtils.getAppCompActivity(context) != null) {
            ActionBar ab = PlayerUtils.getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.show();
            }
        }
        if (TOOL_BAR_EXIST) {
            PlayerUtils.getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @SuppressLint("RestrictedApi")
    public static void hideSupportActionBar(Context context) {
        if (ACTION_BAR_EXIST && PlayerUtils.getAppCompActivity(context) != null) {
            ActionBar ab = PlayerUtils.getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.hide();
            }
        }
        if (TOOL_BAR_EXIST) {
            PlayerUtils.getWindow(context).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void clearSavedProgress(Context context, String url) {
        PlayerUtils.clearSavedProgress(context, url);
    }


    public static void goOnPlayOnResume() {
        if (PlayerManager.getCurrentVideo() != null) {
            AbsPlayer player = PlayerManager.getCurrentVideo();
            if (player.currentState == Player.PLAYER_STATE_PAUSE) {
                if (ON_PLAY_PAUSE_TMP_STATE == PLAYER_STATE_PAUSE) {
                    player.onStatePause();
                    MediaManager.pause();
                } else {
                    player.onStatePlaying();
                    MediaManager.start();
                }
                ON_PLAY_PAUSE_TMP_STATE = 0;
            }
        }
    }

    public static int ON_PLAY_PAUSE_TMP_STATE = 0;

    public static void goOnPlayOnPause() {
        if (PlayerManager.getCurrentVideo() != null) {
            AbsPlayer video = PlayerManager.getCurrentVideo();
            if (video.currentState == Player.PLAYER_STATE_AUTO_COMPLETE || video.currentState == Player.PLAYER_STATE_NORMAL || video.currentState == Player.PLAYER_STATE_ERROR) {
            } else {
                ON_PLAY_PAUSE_TMP_STATE = video.currentState;
                video.onStatePause();
                MediaManager.pause();
            }
        }
    }

    public static void onScrollAutoTiny(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = MediaManager.instance().positionInList;
        if (currentPlayPosition >= 0) {
            if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                if (PlayerManager.getCurrentVideo() != null &&
                        PlayerManager.getCurrentVideo().currentScreen != Player.CONTAINER_MODE_TINY &&
                        PlayerManager.getCurrentVideo().currentScreen != Player.CONTAINER_MODE_FULLSCREEN) {
                    if (PlayerManager.getCurrentVideo().currentState == Player.PLAYER_STATE_PAUSE) {
                        Player.releaseAllVideos();
                    } else {
                        Log.e(TAG, "onScroll: out screen");
                        PlayerManager.getCurrentVideo().startWindowTiny();
                    }
                }
            } else {
                if (PlayerManager.getCurrentVideo() != null &&
                        PlayerManager.getCurrentVideo().currentScreen == Player.CONTAINER_MODE_TINY) {
                    Log.e(TAG, "onScroll: into screen");
                    Player.backPress();
                }
            }
        }
    }

    public static void onScrollReleaseAllVideos(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = MediaManager.instance().positionInList;
        if (currentPlayPosition >= 0) {
            if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                if (PlayerManager.getCurrentVideo().currentScreen != Player.CONTAINER_MODE_FULLSCREEN) {
                    Player.releaseAllVideos();//为什么最后一个视频横屏会调用这个，其他地方不会
                }
            }
        }
    }

    public static void onChildViewAttachedToWindow(View view, int playerId) {
        if (PlayerManager.getCurrentVideo() != null && PlayerManager.getCurrentVideo().currentScreen == Player.CONTAINER_MODE_TINY) {
            Player player = view.findViewById(playerId);
            if (player != null && player.dataSource.containsTheUrl(MediaManager.getCurrentUrl())) {
                Player.backPress();
            }
        }
    }

    public static void onChildViewDetachedFromWindow(View view) {
        if (PlayerManager.getCurrentVideo() != null && PlayerManager.getCurrentVideo().currentScreen != Player.CONTAINER_MODE_TINY) {
            AbsPlayer video = PlayerManager.getCurrentVideo();
            if (((ViewGroup) view).indexOfChild(video) != -1) {
                if (video.currentState == Player.PLAYER_STATE_PAUSE) {
                    Player.releaseAllVideos();
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

    public static void setVideoImageDisplayType(int type) {
        Player.SCREEN_TYPE = type;
        if (MediaManager.textureView != null) {
            MediaManager.textureView.requestLayout();
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
            if (currentState == PLAYER_STATE_PLAYING || currentState == PLAYER_STATE_PAUSE) {
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

    protected abstract void init(Context context, AttributeSet attrs);

    protected abstract void onProgress(int progress, long position, long duration);

    abstract long getDuration();

    abstract long getCurrentPositionWhenPlaying();

}
