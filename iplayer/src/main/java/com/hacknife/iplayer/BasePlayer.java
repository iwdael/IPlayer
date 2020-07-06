package com.hacknife.iplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hacknife.iplayer.interfaces.Event;
import com.hacknife.iplayer.state.ContainerMode;
import com.hacknife.iplayer.state.PlayerState;
import com.hacknife.iplayer.state.ScreenType;
import com.hacknife.iplayer.util.PlayerUtils;
import com.hacknife.iplayer.util.PreferenceHelper;
import com.hacknife.iplayer.widget.PlayerTextureView;

import java.lang.reflect.Constructor;
import java.util.Timer;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_FULLSCREEN;
import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_LIST;
import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_NORMAL;
import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_TINY;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_AUTO_COMPLETE;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_ERROR;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_NORMAL;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PAUSE;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PLAYING;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PREPARING;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PREPARING_CHANGING_URL;
import static com.hacknife.iplayer.util.ToolbarHelper.hideSupportActionBar;
import static com.hacknife.iplayer.util.ToolbarHelper.showSupportActionBar;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */

public abstract class BasePlayer extends Player {


    public BasePlayer(Context context) {
        super(context);
    }

    public BasePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        View.inflate(context, attachLayoutRes(), this);
        orientationNormal = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        orientationFullScreen = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        screenType = ScreenType.SCREEN_TYPE_ADAPTER;
        iv_play = findViewById(R.id.iplayer_iv_play);
        iv_fullscreen = findViewById(R.id.iplayer_iv_fullscreen);
        sb_bottom = findViewById(R.id.iplayer_sb_bottom);
        tv_current_time = findViewById(R.id.iplayer_tv_current_time);
        tv_total_time = findViewById(R.id.iplayer_tv_total_time);
        ll_bottom = findViewById(R.id.iplayer_ll_bottom);
        fl_surface = findViewById(R.id.iplayer_fl_surface);
        ll_top = findViewById(R.id.iplayer_ll_top);
        iv_play.setOnClickListener(this);
        iv_fullscreen.setOnClickListener(this);
        sb_bottom.setOnSeekBarChangeListener(this);
        ll_bottom.setOnClickListener(this);
        fl_surface.setOnClickListener(this);
        fl_surface.setOnTouchListener(this);
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        try {
            if (isCurrentPlayer()) {
                orientationNormal = ((AppCompatActivity) context).getRequestedOrientation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDataSource(DataSource dataSource, ContainerMode containerMode) {
        if (this.dataSource != null && dataSource.getCurrentUrl() != null && this.dataSource.equals(dataSource)) {
            return;//recyclerView listView 中复用的问题 ，数据源一致 跳过
        }
        if (isCurrentPlayer() && dataSource.equals(MediaManager.getDataSource())) {
            long position = 0;
            try {
                position = MediaManager.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            if (position != 0) {
                PreferenceHelper.saveProgress(getContext(), MediaManager.getCurrentUrl(), position, enableSaveProgress);
            }

            MediaManager.get().releasePlayerEngine();
        } else if (isCurrentPlayer() && !dataSource.equals(MediaManager.getDataSource())) {
            startTinyPlayer();
        }
        this.dataSource = dataSource;
        this.containerMode = containerMode;
        this.dataSource.setEnableCache(enableCache);
        MediaManager.getImageLoader().onLoadCover(iv_thumb, dataSource.getCover());
        onStateNormal();
        //ListView中，滚动回退
        if (!isCurrentPlayer() && dataSource.equals(MediaManager.getDataSource())) {
            if (PlayerManager.getCurrentPlayer() != null && PlayerManager.getCurrentPlayer().containerMode == CONTAINER_MODE_TINY && enableTinyWindow && containerMode == CONTAINER_MODE_LIST) {
                PlayerManager.setFirstPlayer(this);
                PlayerManager.getFirstPlayer().playOnSelfPlayer();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iplayer_iv_play) {
            if (dataSource.urlsMap().isEmpty() || dataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (playerState == PLAYER_STATE_NORMAL) {//网络播放，且非WIFI
                if (!dataSource.getCurrentUrl().toString().startsWith("file") &&
                        !dataSource.getCurrentUrl().toString().startsWith("/") &&
                        !PlayerUtils.isWifiConnected(getContext()) && enableShowWifiDialog) {
                    showWifiDialog();
                    return;
                }
                startPlayer();
                onEvent(Event.ON_CLICK_START_ICON);//开始的事件应该在播放之后，此处特殊
            } else if (playerState == PLAYER_STATE_PLAYING) {
                onEvent(Event.ON_CLICK_PAUSE);
                MediaManager.pause();
                onStatePause();
            } else if (playerState == PLAYER_STATE_PAUSE) {
                onEvent(Event.ON_CLICK_RESUME);
                MediaManager.start();
                onStatePlaying();
            } else if (playerState == PLAYER_STATE_AUTO_COMPLETE) {
                onEvent(Event.ON_CLICK_START_AUTO_COMPLETE);
                startPlayer();
            }
        } else if (i == R.id.iplayer_iv_fullscreen) {
            if (playerState == PLAYER_STATE_AUTO_COMPLETE) return;
            if (containerMode == CONTAINER_MODE_FULLSCREEN) {
                backPress();
            } else {
                onEvent(Event.ON_ENTER_FULLSCREEN);
                startFullscreenPlayer();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
        if (containerMode == CONTAINER_MODE_TINY) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tinyLp = (LayoutParams) this.getLayoutParams();
                    contentAndroid = (PlayerUtils.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
                    moveX = (int) event.getRawX();
                    moveY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - moveX;
                    int movedY = nowY - moveY;
                    moveX = nowX;
                    moveY = nowY;
                    tinyLp.rightMargin = tinyLp.rightMargin - movedX;
                    tinyLp.bottomMargin = tinyLp.bottomMargin - movedY;
                    contentAndroid.updateViewLayout(this, tinyLp);
                    break;
                default:
                    break;
            }
        }
        if (id == R.id.iplayer_fl_surface) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchingSeekBar = true;
                    changeX = x;
                    changeY = y;
                    changeVolume = false;
                    changePosition = false;
                    changeBrightness = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - changeX;
                    float deltaY = y - changeY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if (containerMode == CONTAINER_MODE_FULLSCREEN) {
                        if (!changePosition && !changeVolume && !changeBrightness) {
                            if (absDeltaX > dragSpeedDiffer || absDeltaY > dragSpeedDiffer) {
                                cancelProgressTimer();
                                if (absDeltaX >= dragSpeedDiffer) {
                                    // 全屏模式下的PLAYER_STATE_ERROR状态下,不响应进度拖动事件.
                                    // 否则会因为mediaplayer的状态非法导致App Crash
                                    if (playerState != PLAYER_STATE_ERROR) {
                                        changePosition = true;
                                        gestureDownPosition = getCurrentPositionWhenPlaying();
                                    }
                                } else {
                                    //如果y轴滑动距离超过设置的处理范围，那么进行滑动事件处理
                                    if (changeX < screenWidth * 0.5f) {//左侧改变亮度
                                        changeBrightness = true;
                                        WindowManager.LayoutParams lp = PlayerUtils.getWindow(getContext()).getAttributes();
                                        if (lp.screenBrightness < 0) {
                                            try {
                                                gestureDownBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                                            } catch (Settings.SettingNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            gestureDownBrightness = lp.screenBrightness * 255;
                                        }
                                    } else {//右侧改变声音
                                        changeVolume = true;
                                        gestureDownVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    }
                                }
                            }
                        }
                    }
                    if (changePosition) {
                        long totalTimeDuration = getDuration();
                        if (dragSpeedType == 1) {
                            seekTimePosition = (int) (gestureDownPosition + deltaX * dragSpeed);
                        } else {
                            seekTimePosition = (int) (gestureDownPosition + deltaX * totalTimeDuration / screenWidth);
                        }
                        if (seekTimePosition > totalTimeDuration)
                            seekTimePosition = totalTimeDuration;
                        String seekTime = PlayerUtils.stringForTime(seekTimePosition);
                        String totalTime = PlayerUtils.stringForTime(totalTimeDuration);

                        showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
                    }
                    if (changeVolume) {
                        deltaY = -deltaY;
                        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int deltaV = (int) (max * deltaY * 3 / screenHeight);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, gestureDownVolume + deltaV, 0);
                        //dialog中显示百分比
                        int volumePercent = (int) (gestureDownVolume * 100 / max + deltaY * 3 * 100 / screenHeight);
                        showVolumeDialog(-deltaY, volumePercent);
                    }

                    if (changeBrightness) {
                        deltaY = -deltaY;
                        int deltaV = (int) (255 * deltaY * 3 / screenHeight);
                        WindowManager.LayoutParams params = PlayerUtils.getWindow(getContext()).getAttributes();
                        if (((gestureDownBrightness + deltaV) / 255) >= 1) {//这和声音有区别，必须自己过滤一下负值
                            params.screenBrightness = 1;
                        } else if (((gestureDownBrightness + deltaV) / 255) <= 0) {
                            params.screenBrightness = 0.01f;
                        } else {
                            params.screenBrightness = (gestureDownBrightness + deltaV) / 255;
                        }
                        PlayerUtils.getWindow(getContext()).setAttributes(params);
                        //dialog中显示百分比
                        int brightnessPercent = (int) (gestureDownBrightness * 100 / 255 + deltaY * 3 * 100 / screenHeight);
                        showBrightnessDialog(brightnessPercent);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    touchingSeekBar = false;
                    dismissProgressDialog();
                    dismissVolumeDialog();
                    dismissBrightnessDialog();
                    if (changePosition) {
                        onEvent(Event.ON_TOUCH_SCREEN_SEEK_POSITION);
                        MediaManager.seekTo(seekTimePosition);
                        long duration = getDuration();
                        int progress = (int) (seekTimePosition * 100 / (duration == 0 ? 1 : duration));
                        sb_bottom.setProgress(progress);
                    }
                    if (changeVolume) {
                        onEvent(Event.ON_TOUCH_SCREEN_SEEK_VOLUME);
                    }
                    startProgressTimer();
                    break;
            }
        }
        return false;
    }

    public void startPlayer() {
        PlayerManager.releaseAllPlayer();
        initTextureView();
        addTextureView();
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        PlayerUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        MediaManager.setDataSource(dataSource);//设置数据源
        MediaManager.get().positionInList = positionInList;//todo
        onStatePreparing();//准备播放
        PlayerManager.setFirstPlayer(this);
    }

    protected void setState(PlayerState state) {
        setState(state, 0, 0);
    }

    protected void setState(PlayerState state, int urlMapIndex, int seekToProgress) {
        switch (state) {
            case PLAYER_STATE_NORMAL:
                onStateNormal();
                break;
            case PLAYER_STATE_PREPARING:
                onStatePreparing();
                break;
            case PLAYER_STATE_PREPARING_CHANGING_URL:
                changeUrl(urlMapIndex, seekToProgress);
                break;
            case PLAYER_STATE_PLAYING:
                onStatePlaying();
                break;
            case PLAYER_STATE_PAUSE:
                onStatePause();
                break;
            case PLAYER_STATE_ERROR:
                onStateError();
                break;
            case PLAYER_STATE_AUTO_COMPLETE:
                onStatePlayComplete();
                break;
        }
    }

    protected void changeUrl(int urlMapIndex, long seekToProgress) {
        playerState = PLAYER_STATE_PREPARING_CHANGING_URL;
        this.seekToProgress = seekToProgress;
        dataSource.setIndex(urlMapIndex);
        MediaManager.setDataSource(dataSource);
        MediaManager.get().prepare();
    }

    protected void changeUrl(DataSource dataSource, long seekToProgress) {
        playerState = PLAYER_STATE_PREPARING_CHANGING_URL;
        this.seekToProgress = seekToProgress;
        this.dataSource = dataSource;
        if (PlayerManager.getSecondPlayer() != null && PlayerManager.getFirstPlayer() != null) {
            PlayerManager.getFirstPlayer().dataSource = dataSource;
        }
        MediaManager.setDataSource(dataSource);
        MediaManager.get().prepare();
    }

    protected void changeUrl(String url, String title, String cover, long seekToProgress) {
        changeUrl(new DataSource(url, title, cover), seekToProgress);
    }

    /**
     * 播放引擎整备完毕后回调
     */
    public void onPrepared() {
        onStatePrepared();
        onStatePlaying();
    }

    protected void onStateNormal() {
        if (onStateChangeListener != null)
            onStateChangeListener.onStateNormal();
        playerState = PLAYER_STATE_NORMAL;
        cancelProgressTimer();
    }

    protected void onStateRePlay() {
        if (onStateChangeListener != null)
            onStateChangeListener.onStateRePlay();
    }

    protected void onStatePlay() {
        if (onStateChangeListener != null)
            onStateChangeListener.onStatePlay();
    }

    protected void onStatePreparing() {
        onStatePlay();
        if (onStateChangeListener != null)
            onStateChangeListener.onStatePreparing();
        playerState = PLAYER_STATE_PREPARING;
        resetProgressAndTime();
    }


    protected void onStatePrepared() {//因为这个紧接着就会进入播放状态，所以不设置state
        if (onStateChangeListener != null)
            onStateChangeListener.onStatePrepared();
        if (seekToProgress != 0) {
            MediaManager.seekTo(seekToProgress);
            seekToProgress = 0;
        } else {
            long position = PreferenceHelper.getSavedProgress(getContext(), dataSource.getCurrentUrl());
            if (position != 0) {
                MediaManager.seekTo(position);
            }
        }
    }

    protected void onStatePlaying() {
        if (onStateChangeListener != null)
            onStateChangeListener.onStatePlaying();
        playerState = PLAYER_STATE_PLAYING;
        startProgressTimer();
    }

    protected void onStatePause() {
        if (onStateChangeListener != null)
            onStateChangeListener.onStatePause();
        playerState = PLAYER_STATE_PAUSE;
        startProgressTimer();
    }

    protected void onStateError() {
        if (onStateChangeListener != null)
            onStateChangeListener.onStateError();
        playerState = PLAYER_STATE_ERROR;
        cancelProgressTimer();
    }

    protected void onStatePlayComplete() {
        if (onStateChangeListener != null)
            onStateChangeListener.onStatePlayComplete();
        playerState = PLAYER_STATE_AUTO_COMPLETE;
        cancelProgressTimer();
        sb_bottom.setProgress(100);
        tv_current_time.setText(tv_total_time.getText());
    }

    public void onInfo(int what, int extra) {
    }

    public void onError(int what, int extra) {
        if (what != 38 && extra != -38 && what != -38 && extra != 38 && extra != -19) {
            onStateError();
            if (isCurrentPlayer()) {
                MediaManager.get().releasePlayerEngine();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (containerMode == CONTAINER_MODE_FULLSCREEN || containerMode == CONTAINER_MODE_TINY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        if (widthRatio != 0 && heightRatio != 0) {
            int specWidth = MeasureSpec.getSize(widthMeasureSpec);
            int specHeight = (int) ((specWidth * (float) heightRatio) / widthRatio);
            setMeasuredDimension(specWidth, specHeight);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(specHeight, MeasureSpec.EXACTLY);
            getChildAt(0).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    public void onPlayCompletion() {
        Runtime.getRuntime().gc();
        onEvent(Event.ON_AUTO_COMPLETE);
        dismissVolumeDialog();
        dismissProgressDialog();
        dismissBrightnessDialog();
        onStatePlayComplete();
        if (containerMode == CONTAINER_MODE_FULLSCREEN || containerMode == CONTAINER_MODE_TINY) {
            backPress();
        }
        MediaManager.get().positionInList = -1;
        MediaManager.get().releasePlayerEngine();
        PreferenceHelper.saveProgress(getContext(), dataSource.getCurrentUrl(), 0, enableSaveProgress);
    }

    public void releasePlayer() {
        if (playerState == PLAYER_STATE_PLAYING || playerState == PLAYER_STATE_PAUSE) {
            long position = getCurrentPositionWhenPlaying();
            PreferenceHelper.saveProgress(getContext(), dataSource.getCurrentUrl(), position, enableSaveProgress);
        }
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateRelease();
        }
        cancelProgressTimer();
        dismissBrightnessDialog();
        dismissProgressDialog();
        dismissVolumeDialog();
        onStateNormal();
        fl_surface.removeView(MediaManager.textureView);
        MediaManager.get().currentVideoWidth = 0;
        MediaManager.get().currentVideoHeight = 0;
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        PlayerUtils.clearFlags(getContext(), WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        clearSecondLayout();
        PlayerUtils.setRequestedOrientation(getContext(), orientationNormal);
        if (MediaManager.surface != null) MediaManager.surface.release();
        if (MediaManager.savedSurfaceTexture != null)
            MediaManager.savedSurfaceTexture.release();
        MediaManager.textureView = null;
        MediaManager.savedSurfaceTexture = null;
    }

    protected void initTextureView() {
        removeTextureView();
        MediaManager.textureView = new PlayerTextureView(getContext());
        MediaManager.textureView.setSurfaceTextureListener(MediaManager.get());
    }

    protected void addTextureView() {
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER);
        fl_surface.addView(MediaManager.textureView, layoutParams);
    }

    protected void removeTextureView() {
        MediaManager.savedSurfaceTexture = null;
        if (MediaManager.textureView != null && MediaManager.textureView.getParent() != null) {
            ((ViewGroup) MediaManager.textureView.getParent()).removeView(MediaManager.textureView);
        }
    }

    protected void clearSecondLayout() {
        if (PlayerUtils.scanForActivity(getContext()) == null) return;
        ViewGroup vp = (PlayerUtils.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
        View full = vp.findViewById(R.id.iplayer_fullscreen_id);
        if (full != null) {
            vp.removeView(full);
        }
        View tiny = vp.findViewById(R.id.iplayer_tiny_id);
        if (tiny != null) {
            vp.removeView(tiny);
        }
        showSupportActionBar(getContext());
    }

    protected void quitFullScreenPlayer() {
        ViewGroup vp = (PlayerUtils.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
        Player player = vp.findViewById(R.id.iplayer_fullscreen_id);
        if (player != null) {
            PlayerUtils.setRequestedOrientation(getContext(), orientationNormal);
            showSupportActionBar(getContext());
            vp.removeView(player);
            if (player.fl_surface != null)
                player.fl_surface.removeView(MediaManager.textureView);
            PlayerManager.setSecondPlayer(null);
        }
    }

    protected void quitTinyPlayer() {
        ViewGroup vp = (PlayerUtils.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
        Player player = vp.findViewById(R.id.iplayer_tiny_id);
        if (player != null) {
            vp.removeView(player);
            if (player.fl_surface != null)
                player.fl_surface.removeView(MediaManager.textureView);
            PlayerManager.setSecondPlayer(null);
        }
    }

    public void onVideoSizeChanged() {
        if (MediaManager.textureView != null) {
            if (screenRotation != 0) {
                MediaManager.textureView.setRotation(screenRotation);
            }
            MediaManager.textureView.setVideoSize(MediaManager.get().currentVideoWidth, MediaManager.get().currentVideoHeight);
        }
    }

    protected void startProgressTimer() {
        cancelProgressTimer();
        progressTimer = new Timer();
        progressTimerTask = new ProgressTimerTask();
        progressTimer.schedule(progressTimerTask, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (progressTimer != null) {
            progressTimer.cancel();
        }
        if (progressTimerTask != null) {
            progressTimerTask.cancel();
        }
    }

    protected void onProgress(int progress, long position, long duration) {
        if (!touchingSeekBar) {
            if (seekToManulPosition != -1) {
                if (seekToManulPosition > progress) {
                    return;
                } else {
                    seekToManulPosition = -1;
                }
            } else {
                if (progress != 0) sb_bottom.setProgress(progress);
            }
        }
        if (position != 0) tv_current_time.setText(PlayerUtils.stringForTime(position));
        tv_total_time.setText(PlayerUtils.stringForTime(duration));
    }

    public void setBufferProgress(int bufferProgress) {
        if (bufferProgress != 0) sb_bottom.setSecondaryProgress(bufferProgress);
    }

    protected void resetProgressAndTime() {
        sb_bottom.setProgress(0);
        sb_bottom.setSecondaryProgress(0);
        tv_current_time.setText(PlayerUtils.stringForTime(0));
        tv_total_time.setText(PlayerUtils.stringForTime(0));
    }

    protected long getCurrentPositionWhenPlaying() {
        long position = 0;
        if (playerState == PLAYER_STATE_PLAYING || playerState == PLAYER_STATE_PAUSE) {
            try {
                position = MediaManager.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    public long getDuration() {
        long duration = 0;
        try {
            duration = MediaManager.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        cancelProgressTimer();
        ViewParent vpdown = getParent();
        while (vpdown != null) {
            vpdown.requestDisallowInterceptTouchEvent(true);
            vpdown = vpdown.getParent();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        onEvent(Event.ON_SEEK_POSITION);
        startProgressTimer();
        ViewParent vpup = getParent();
        while (vpup != null) {
            vpup.requestDisallowInterceptTouchEvent(false);
            vpup = vpup.getParent();
        }
        if (playerState != PLAYER_STATE_PLAYING && playerState != PLAYER_STATE_PAUSE) return;
        long time = seekBar.getProgress() * getDuration() / 100;
        seekToManulPosition = seekBar.getProgress();
        MediaManager.seekTo(time);
    }

    protected int seekToManulPosition = -1;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            long duration = getDuration();
            tv_current_time.setText(PlayerUtils.stringForTime(progress * duration / 100));
        }
    }

    public void startFullscreenPlayer() {
        onEvent(event.ON_ENTER_TINYSCREEN);
        if (playerState == PLAYER_STATE_NORMAL || playerState == PLAYER_STATE_ERROR || playerState == PLAYER_STATE_AUTO_COMPLETE || PlayerManager.getSecondPlayer() != null)
            return;
        hideSupportActionBar(getContext());
        ViewGroup vp = (PlayerUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.iplayer_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        fl_surface.removeView(MediaManager.textureView);
        try {
            Constructor<BasePlayer> constructor = (Constructor<BasePlayer>) BasePlayer.this.getClass().getConstructor(Context.class);
            BasePlayer iplayer = constructor.newInstance(getContext());
            iplayer.setOrientationNormal(orientationNormal);
            iplayer.screenTypeFull = screenTypeFull;
            iplayer.screenTypeNormal = screenTypeNormal;
            iplayer.enableSaveProgress = enableSaveProgress;
            iplayer.dragSpeed = dragSpeed;
            iplayer.dragSpeedDiffer = dragSpeedDiffer;
            iplayer.dragSpeedType = dragSpeedType;
            iplayer.setScreenType(screenTypeFull);
            iplayer.setId(R.id.iplayer_fullscreen_id);
//            Log.d(TAG, "iplayer.dragSpeedType: " + iplayer.dragSpeedType);
//            Log.d(TAG, "iplayer.dragSpeedDiffer: " + iplayer.dragSpeedDiffer);
//            Log.d(TAG, "iplayer.dragSpeed: " + iplayer.dragSpeed);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(iplayer, lp);
            iplayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
            iplayer.setDataSource(dataSource, CONTAINER_MODE_FULLSCREEN);
            iplayer.setState(playerState);
            iplayer.addTextureView();
            PlayerManager.setSecondPlayer(iplayer);
            PlayerUtils.setRequestedOrientation(getContext(), orientationFullScreen);
            onStateNormal();
            iplayer.sb_bottom.setSecondaryProgress(sb_bottom.getSecondaryProgress());
            iplayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startTinyPlayer() {
        onEvent(event.ON_ENTER_TINYSCREEN);
        if (playerState == PLAYER_STATE_NORMAL || playerState == PLAYER_STATE_ERROR || playerState == PLAYER_STATE_AUTO_COMPLETE || PlayerManager.getSecondPlayer() != null)
            return;
        ViewGroup vp = (PlayerUtils.scanForActivity(getContext()))
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.iplayer_tiny_id);
        if (old != null) {
            vp.removeView(old);
        }

        fl_surface.removeView(MediaManager.textureView);
        try {
            Constructor<BasePlayer> constructor = (Constructor<BasePlayer>) BasePlayer.this.getClass().getConstructor(Context.class);
            BasePlayer player = constructor.newInstance(getContext());
            player.setId(R.id.iplayer_tiny_id);

            if (tinyWindowWidth == 0 || tinyWindowHeight == 0) {
                float rat = MediaManager.get().currentVideoHeight / (float) MediaManager.get().currentVideoWidth;
                tinyLp = new FrameLayout.LayoutParams((int) (screenWidth * (2 / 5f)), (int) (screenWidth * (2 / 5f) * rat));
            } else {
                tinyLp = new FrameLayout.LayoutParams(tinyWindowWidth, tinyWindowHeight);
            }
            tinyLp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            vp.addView(player, tinyLp);

            player.setScreenType(screenTypeTiny);
            player.setDataSource(dataSource, CONTAINER_MODE_TINY);
            player.setState(playerState);
            player.addTextureView();
            PlayerManager.setSecondPlayer(player);
            onStateNormal();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isCurrentPlayer() {
        return isCurrentVideo() && dataSource.containsTheUrl(MediaManager.getCurrentUrl());//数据源一致
    }

    protected boolean isCurrentVideo() {
        return PlayerManager.getCurrentPlayer() != null && PlayerManager.getCurrentPlayer() == this;
    }

    public void playOnSelfPlayer() {
        playerState = PlayerManager.getSecondPlayer().playerState;
        quitFullScreenPlayer();
        quitTinyPlayer();
        screenType = screenTypeNormal;
        setState(playerState);
        addTextureView();
    }


    public void onEvent(int type) {
        if (event != null && isCurrentPlayer() && !dataSource.urlsMap().isEmpty()) {
            event.onEvent(type, dataSource.getCurrentUrl(), containerMode);
        }
    }


    public void onSeekComplete() {

    }


    public void setEvent(Event event) {
        this.event = event;
    }

    public Object getCurrentUrl() {
        return dataSource.getCurrentUrl();
    }

    public void setDataSource(Object url, String title, String cover, int position) {
        setDataSource(url, title, cover, CONTAINER_MODE_NORMAL);
        positionInList = position;
    }

    public void setDataSource(Object url, String title, String cover, ContainerMode containerMode, int position) {
        setDataSource(url, title, cover, containerMode);
        positionInList = position;
    }

    public void setDataSource(Object url, String title, String cover) {
        setDataSource(new DataSource(url, title, cover), CONTAINER_MODE_NORMAL);
    }

    public void setDataSource(Object url, String title, String cover, ContainerMode containerMode) {
        setDataSource(new DataSource(url, title, cover), containerMode);
    }

    public void setDataSource(DataSource dataSource) {
        setDataSource(dataSource, CONTAINER_MODE_NORMAL);
    }

    public void setDataSource(DataSource dataSource, int position) {
        setDataSource(dataSource, CONTAINER_MODE_NORMAL);
        positionInList = position;
    }

    public void setDataSource(DataSource dataSource, ContainerMode containerMode, int position) {
        setDataSource(dataSource, containerMode);
        positionInList = position;
    }

    protected void showWifiDialog() {
    }

    protected void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
    }

    protected void dismissProgressDialog() {

    }

    protected void showVolumeDialog(float deltaY, int volumePercent) {

    }

    protected void dismissVolumeDialog() {

    }

    protected void showBrightnessDialog(int brightnessPercent) {

    }

    protected void dismissBrightnessDialog() {

    }

    protected abstract int attachLayoutRes();
}
