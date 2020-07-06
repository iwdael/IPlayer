package com.hacknife.iplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hacknife.iplayer.interfaces.Event;
import com.hacknife.iplayer.state.ContainerMode;
import com.hacknife.iplayer.state.ScreenType;
import com.hacknife.iplayer.util.PlayerUtils;
import com.hacknife.iplayer.widget.SettingView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class IPlayer extends BasePlayer implements SettingView.OnSettingListener {


    protected ImageView iv_back;
    protected ProgressBar pro_bottom, pro_loading;
    protected TextView tv_title;
    protected ImageView iv_back_tiny;
    protected LinearLayout ll_battery_time;
    protected ImageView setting;
    protected ImageView iv_battery;
    protected TextView tv_system_time;
    protected TextView tv_replay;
    protected TextView tv_clarity;
    protected PopupWindow clarityPopWindow;
    protected TextView tv_retry;
    protected LinearLayout ll_retry;

    protected controlViewTimerTask controlViewTimerTask;
    protected Timer controlViewTimer;

    protected Dialog progressDialog;
    protected ProgressBar dialogProgressBar;
    protected TextView dialogSeekTime;
    protected TextView dialogTotalTime;
    protected ImageView dialogIcon;
    protected Dialog volumeDialog;
    protected ProgressBar dialogVolumeProgressBar;
    protected TextView dialogVolumeTextView;
    protected ImageView dialogVolumeImageView;
    protected Dialog brightnessDialog;
    protected ProgressBar dialogBrightnessProgressBar;
    protected TextView dialogBrightnessTextView;
    protected SettingView settingView;
    protected long lastGetBatteryTime = 0;
    protected int lastGetBatteryPercent = 70;


    private BroadcastReceiver battertReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int percent = level * 100 / scale;
                lastGetBatteryPercent = percent;
                setBattery();
                getContext().unregisterReceiver(battertReceiver);
            }
        }
    };

    public IPlayer(Context context) {
        super(context);
    }

    public IPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IPlayer);
            enableBottomProgressBar = ta.getBoolean(R.styleable.IPlayer_enableBottomProgressBar, true);
            enableTitleBar = ta.getBoolean(R.styleable.IPlayer_enableTitleBar, true);
            enableBottomBar = ta.getBoolean(R.styleable.IPlayer_enableBottomBar, true);
            enableClarity = ta.getBoolean(R.styleable.IPlayer_enableClarity, true);
            enableEnlarge = ta.getBoolean(R.styleable.IPlayer_enableEnlarge, true);
            enableShowWifiDialog = ta.getBoolean(R.styleable.IPlayer_enableShowWifiDialog, true);
            enableCache = ta.getBoolean(R.styleable.IPlayer_enableCache, false);
            screenTypeNormal = PlayerUtils.integer2ScreenType(ta.getInt(R.styleable.IPlayer_screenType, 1));
            screenTypeFull = PlayerUtils.integer2ScreenType(ta.getInt(R.styleable.IPlayer_screenTypeFull, 1));
            screenTypeTiny = PlayerUtils.integer2ScreenType(ta.getInt(R.styleable.IPlayer_screenTypeTiny, 1));
            orientationFullScreen = ta.getInt(R.styleable.IPlayer_orientationFullScreen, -1) == -1 ? ActivityInfo.SCREEN_ORIENTATION_SENSOR : (ta.getInt(R.styleable.IPlayer_orientationFullScreen, -1) == 1 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            enableTinyWindow = ta.getBoolean(R.styleable.IPlayer_enableTinyWindow, false);
            enableSaveProgress = ta.getBoolean(R.styleable.IPlayer_enableSaveProgress, false);
            tinyWindowWidth = ta.getDimensionPixelSize(R.styleable.IPlayer_tinyWindowWidth, 0);
            tinyWindowHeight = ta.getDimensionPixelSize(R.styleable.IPlayer_tinyWindowHeight, 0);
            dragSpeed = ta.getFloat(R.styleable.IPlayer_dragSpeed, 1);
            dragSpeedDiffer = ta.getFloat(R.styleable.IPlayer_dragSpeedDiffer, 60);
            dragSpeedType = ta.getInt(R.styleable.IPlayer_dragSpeedType, 1);
            screenType = screenTypeNormal;
            ta.recycle();
        } else {
            enableBottomProgressBar = true;
            enableTitleBar = true;
            enableBottomBar = true;
            enableClarity = true;
            enableEnlarge = true;
            enableShowWifiDialog = true;
            enableCache = false;
            screenTypeNormal = ScreenType.SCREEN_TYPE_ADAPTER;
            screenTypeFull = ScreenType.SCREEN_TYPE_ADAPTER;
            screenTypeTiny = ScreenType.SCREEN_TYPE_ADAPTER;
            orientationFullScreen = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
            enableTinyWindow = false;
            enableSaveProgress = false;
            tinyWindowWidth = 0;
            tinyWindowHeight = 0;
            dragSpeed = 1;
            dragSpeedDiffer = 60;
            dragSpeedType = 1;
        }
        ll_battery_time = findViewById(R.id.iplayer_ll_battery_time);
        setting = findViewById(R.id.iplayer_iv_setting);
        pro_bottom = findViewById(R.id.iplayer_pro_bottom);
        tv_title = findViewById(R.id.iplayer_tv_title);
        iv_back = findViewById(R.id.iplayer_iv_back);
        iv_thumb = findViewById(R.id.iplayer_iv_thumb);
        pro_loading = findViewById(R.id.iplayer_pro_loading);
        iv_back_tiny = findViewById(R.id.iplayer_iv_back_tiny);
        iv_battery = findViewById(R.id.iplayer_iv_battery);
        tv_system_time = findViewById(R.id.iplayer_tv_system_time);
        tv_replay = findViewById(R.id.iplayer_tv_replay);
        tv_clarity = findViewById(R.id.iplayer_tv_clarity);
        tv_retry = findViewById(R.id.iplayer_tv_retry);
        ll_retry = findViewById(R.id.iplayer_ll_retry);
        settingView = findViewById(R.id.iplayer_setting);
        iv_thumb.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_back_tiny.setOnClickListener(this);
        tv_clarity.setOnClickListener(this);
        tv_retry.setOnClickListener(this);
        setting.setOnClickListener(this);
        settingView.setOnSettingListener(this);
        if (!enableEnlarge) {
            iv_fullscreen.setVisibility(GONE);
        }
        if (!enableBottomProgressBar) {
            pro_bottom.setVisibility(INVISIBLE);
        }
        if (!enableTitleBar) {
            ll_top.setVisibility(INVISIBLE);
        }
        if (!enableBottomBar) {
            ll_bottom.setVisibility(INVISIBLE);
        }
    }

    public void setDataSource(DataSource dataSource, ContainerMode mode) {
        super.setDataSource(dataSource, mode);
        tv_title.setText(dataSource.title());
        if (containerMode == CONTAINER_MODE_FULLSCREEN) {
            iv_fullscreen.setImageResource(R.drawable.iplayer_shrink);
            iv_back.setVisibility(View.VISIBLE);
            iv_back_tiny.setVisibility(View.INVISIBLE);
            ll_battery_time.setVisibility(View.VISIBLE);
//            setting.setVisibility(View.VISIBLE);
            if (dataSource.urlsMap().size() == 1) {
                tv_clarity.setVisibility(GONE);
            } else {
                tv_clarity.setText(dataSource.getCurrentKey());
                tv_clarity.setVisibility(View.VISIBLE);
            }
            changePlaySize((int) getResources().getDimension(R.dimen.iplayer_start_button_w_h_fullscreen));
        } else if (containerMode == CONTAINER_MODE_NORMAL
                || containerMode == CONTAINER_MODE_LIST) {
            iv_fullscreen.setImageResource(R.drawable.iplayer_enlarge);
            iv_back.setVisibility(View.GONE);
            iv_back_tiny.setVisibility(View.INVISIBLE);
            changePlaySize((int) getResources().getDimension(R.dimen.iplayer_start_button_w_h_normal));
            ll_battery_time.setVisibility(View.GONE);
            setting.setVisibility(View.GONE);
            if (dataSource.urlsMap().size() == 1 || (!enableClarity)) {
                tv_clarity.setVisibility(GONE);
            } else {
                tv_clarity.setText(dataSource.getCurrentKey().toString());
                tv_clarity.setVisibility(View.VISIBLE);
            }
        } else if (containerMode == CONTAINER_MODE_TINY) {
            iv_back_tiny.setVisibility(View.VISIBLE);
            setControlVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
            ll_battery_time.setVisibility(View.GONE);
            setting.setVisibility(View.GONE);
            tv_clarity.setVisibility(View.GONE);
        }
        setSystemTimeAndBattery();
    }

    protected void changePlaySize(int size) {
        ViewGroup.LayoutParams lp = iv_play.getLayoutParams();
        lp.height = size;
        lp.width = size;
        lp = pro_loading.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.iplayer_layout_standard;
    }

    @Override
    protected void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    @Override
    protected void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
    }


    @Override
    protected void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingClear();
    }

    @Override
    protected void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelControlViewTimer();
    }

    @Override
    protected void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    @Override
    protected void onStatePlayComplete() {
        super.onStatePlayComplete();
        changeUiToComplete();
        cancelControlViewTimer();
        pro_bottom.setProgress(100);
    }


    @Override
    protected void changeUrl(int urlMapIndex, long seekToInAdvance) {
        super.changeUrl(urlMapIndex, seekToInAdvance);
        pro_loading.setVisibility(VISIBLE);
        iv_play.setVisibility(INVISIBLE);
    }

    @Override
    protected void changeUrl(DataSource dataSource, long seekToInAdvance) {
        super.changeUrl(dataSource, seekToInAdvance);
        tv_title.setText(dataSource.title());
        pro_loading.setVisibility(VISIBLE);
        iv_play.setVisibility(INVISIBLE);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.iplayer_fl_surface) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startControlViewTimer();
                    if (changePosition) {
                        long duration = getDuration();
                        int progress = (int) (seekTimePosition * 100 / (duration == 0 ? 1 : duration));
                        pro_bottom.setProgress(progress);
                    }
                    if (!changePosition && !changeVolume) {
                        onEvent(Event.ON_CLICK_BLANK);
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == R.id.iplayer_sb_bottom) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startControlViewTimer();
                    break;
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iplayer_iv_thumb) {
            if (dataSource.urlsMap().isEmpty() || dataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (playerState == PLAYER_STATE_NORMAL) {
                if (!dataSource.getCurrentUrl().toString().startsWith("file") &&
                        !dataSource.getCurrentUrl().toString().startsWith("/") &&
                        !PlayerUtils.isWifiConnected(getContext()) && enableShowWifiDialog) {
                    showWifiDialog();
                    return;
                }
                startPlayer();
                onEvent(Event.ON_CLICK_START_THUMB);//开始的事件应该在播放之后，此处特殊
            } else if (playerState == PLAYER_STATE_AUTO_COMPLETE) {
                onClickUiToggle();
            }
        } else if (i == R.id.iplayer_fl_surface) {
            startControlViewTimer();
        } else if (i == R.id.iplayer_iv_back) {
            backPress();
        } else if (i == R.id.iplayer_iv_back_tiny) {
            if (PlayerManager.getSecondPlayer() != null && PlayerManager.getFirstPlayer() != null && PlayerManager.getFirstPlayer().getContainerMode() == CONTAINER_MODE_NORMAL) {
                backPress();
            } else {
                quitFullscreenOrFloatWindow();
            }
        } else if (i == R.id.iplayer_tv_clarity) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.iplayer_layout_clarity, null);

            OnClickListener mQualityListener = new OnClickListener() {
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    changeUrl(index, getCurrentPositionWhenPlaying());
                    tv_clarity.setText(dataSource.getCurrentKey());
                    for (int j = 0; j < layout.getChildCount(); j++) {//设置点击之后的颜色
                        if (j == dataSource.index()) {
                            TypedValue color = new TypedValue();
                            getContext().getTheme().resolveAttribute(R.attr.iplayer_primary_color, color, true);
                            ((TextView) layout.getChildAt(j)).setTextColor(color.data);
                        } else {
                            ((TextView) layout.getChildAt(j)).setTextColor(getResources().getColor(R.color.iplayer_setting_text_color));
                        }
                    }
                    if (clarityPopWindow != null) {
                        clarityPopWindow.dismiss();
                    }
                }
            };

            for (int j = 0; j < dataSource.urlsMap().size(); j++) {
                String key = dataSource.getKey(j);
                TextView tv_clarityItem = (TextView) View.inflate(getContext(), R.layout.iplayer_layout_clarity_item, null);
                tv_clarityItem.setText(key);
                tv_clarityItem.setTag(j);
                layout.addView(tv_clarityItem, j);
                tv_clarityItem.setOnClickListener(mQualityListener);
                if (j == dataSource.index()) {
                    TypedValue color = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.iplayer_primary_color, color, true);
                    tv_clarityItem.setTextColor(color.data);
                }
            }

            clarityPopWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
            clarityPopWindow.setContentView(layout);
            clarityPopWindow.showAsDropDown(tv_clarity);
            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int offsetX = tv_clarity.getMeasuredWidth() / 2 + tv_clarity.getPaddingLeft() / 2 + tv_clarity.getPaddingRight() / 2;
            int offsetY = tv_clarity.getMeasuredHeight() + layout.getMeasuredHeight();
            clarityPopWindow.update(tv_clarity, -offsetX, -offsetY, Math.round(layout.getMeasuredWidth() * 2), layout.getMeasuredHeight());
        } else if (i == R.id.iplayer_tv_retry) {
            if (dataSource.urlsMap().isEmpty() || dataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!dataSource.getCurrentUrl().toString().startsWith("file") && !
                    dataSource.getCurrentUrl().toString().startsWith("/") &&
                    !PlayerUtils.isWifiConnected(getContext()) && enableShowWifiDialog) {
                showWifiDialog();
                return;
            }
            initTextureView();//和开始播放的代码重复
            addTextureView();
            MediaManager.setDataSource(dataSource);
            onStateRePlay();
            onStatePreparing();
            onEvent(Event.ON_CLICK_START_ERROR);
        } else if (R.id.iplayer_iv_setting == v.getId()) {
            if (settingView.getVisibility() == View.VISIBLE) {
                settingView.setVisibility(View.GONE);
            } else {
                settingView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onEvent(Event.ON_CLICK_START_WIFIDIALOG);
                startPlayer();
                enableShowWifiDialog = false;
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                quitFullScreenPlayer();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        startControlViewTimer();
    }

    protected void onClickUiToggle() {
        if (iv_play.getVisibility() != View.VISIBLE) {
            setSystemTimeAndBattery();
            tv_clarity.setText(dataSource.getCurrentKey());
        }
        if (playerState == PLAYER_STATE_PREPARING) {
            changeUiToPreparing();
            if (iv_play.getVisibility() == View.VISIBLE) {
            } else {
                setSystemTimeAndBattery();
            }
        } else if (playerState == PLAYER_STATE_PLAYING) {
            if (iv_play.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (playerState == PLAYER_STATE_PAUSE) {
            if (iv_play.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }

    protected void setSystemTimeAndBattery() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        tv_system_time.setText(dateFormater.format(date));
        if ((System.currentTimeMillis() - lastGetBatteryTime) > 30000) {
            lastGetBatteryTime = System.currentTimeMillis();
            getContext().registerReceiver(
                    battertReceiver,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            );
        } else {
            setBattery();
        }
    }

    protected void setBattery() {
        int percent = lastGetBatteryPercent;
        if (percent < 15) {
            iv_battery.setBackgroundResource(R.drawable.iplayer_battery_level_10);
        } else if (percent >= 15 && percent < 40) {
            iv_battery.setBackgroundResource(R.drawable.iplayer_battery_level_30);
        } else if (percent >= 40 && percent < 60) {
            iv_battery.setBackgroundResource(R.drawable.iplayer_battery_level_50);
        } else if (percent >= 60 && percent < 80) {
            iv_battery.setBackgroundResource(R.drawable.iplayer_battery_level_70);
        } else if (percent >= 80 && percent < 95) {
            iv_battery.setBackgroundResource(R.drawable.iplayer_battery_level_90);
        } else if (percent >= 95 && percent <= 100) {
            iv_battery.setBackgroundResource(R.drawable.iplayer_battery_level_100);
        }
    }

    protected void onClickUiToggleToClear() {
        if (playerState == PLAYER_STATE_PREPARING) {
            if (iv_play.getVisibility() == View.VISIBLE) {
                changeUiToPreparing();
            } else {
            }
        } else if (playerState == PLAYER_STATE_PLAYING) {
            if (iv_play.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
            }
        } else if (playerState == PLAYER_STATE_PAUSE) {
            if (iv_play.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
            }
        } else if (playerState == PLAYER_STATE_AUTO_COMPLETE) {
            if (iv_play.getVisibility() == View.VISIBLE) {
                changeUiToComplete();
            } else {
            }
        }
    }

    @Override
    public void onProgress(int progress, long position, long duration) {
        super.onProgress(progress, position, duration);
        if (progress != 0) pro_bottom.setProgress(progress);
    }

    @Override
    public void setBufferProgress(int bufferProgress) {
        super.setBufferProgress(bufferProgress);
        if (bufferProgress != 0) pro_bottom.setSecondaryProgress(bufferProgress);
    }

    @Override
    protected void resetProgressAndTime() {
        super.resetProgressAndTime();
        pro_bottom.setProgress(0);
        pro_bottom.setSecondaryProgress(0);
    }

    protected void changeUiToNormal() {
        switch (containerMode) {
            case CONTAINER_MODE_NORMAL:
            case CONTAINER_MODE_LIST:
                setControlVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_FULLSCREEN:
                setControlVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_TINY:
                break;
        }
    }

    protected void changeUiToPreparing() {
        switch (containerMode) {
            case CONTAINER_MODE_NORMAL:
            case CONTAINER_MODE_LIST:
                setControlVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_FULLSCREEN:
                setControlVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_TINY:
                break;
        }

    }

    protected void changeUiToPlayingShow() {
        switch (containerMode) {
            case CONTAINER_MODE_NORMAL:
            case CONTAINER_MODE_LIST:
                setControlVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_FULLSCREEN:
                setControlVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_TINY:
                break;
        }

    }

    protected void changeUiToPlayingClear() {
        switch (containerMode) {
            case CONTAINER_MODE_NORMAL:
            case CONTAINER_MODE_LIST:
                setControlVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case CONTAINER_MODE_FULLSCREEN:
                setControlVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case CONTAINER_MODE_TINY:
                break;
        }

    }

    protected void changeUiToPauseShow() {
        switch (containerMode) {
            case CONTAINER_MODE_NORMAL:
            case CONTAINER_MODE_LIST:
                setControlVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_FULLSCREEN:
                setControlVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_TINY:
                break;
        }
    }

    protected void changeUiToPauseClear() {
        switch (containerMode) {
            case CONTAINER_MODE_NORMAL:
            case CONTAINER_MODE_LIST:
                setControlVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case CONTAINER_MODE_FULLSCREEN:
                setControlVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case CONTAINER_MODE_TINY:
                break;
        }

    }

    protected void changeUiToComplete() {
        switch (containerMode) {
            case CONTAINER_MODE_NORMAL:
            case CONTAINER_MODE_LIST:
                setControlVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_FULLSCREEN:
                setControlVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_TINY:
                break;
        }

    }

    protected void changeUiToError() {
        switch (containerMode) {
            case CONTAINER_MODE_NORMAL:
            case CONTAINER_MODE_LIST:
                setControlVisiblity(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_FULLSCREEN:
                setControlVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case CONTAINER_MODE_TINY:
                break;
        }

    }

    protected void setControlVisiblity(int ll_top, int ll_bottom, int iv_play, int pro_loading,
                                       int iv_thumb, int pro_bottom, int ll_retry) {
        this.ll_top.setVisibility(enableTitleBar ? ll_top : INVISIBLE);
        this.ll_bottom.setVisibility(enableBottomBar ? ll_bottom : INVISIBLE);
        this.iv_play.setVisibility(iv_play);
        this.pro_loading.setVisibility(pro_loading);
        this.iv_thumb.setVisibility(iv_thumb);
        this.pro_bottom.setVisibility(enableBottomProgressBar ? pro_bottom : INVISIBLE);
        this.ll_retry.setVisibility(ll_retry);
    }

    protected void updateStartImage() {
        if (playerState == PLAYER_STATE_PLAYING) {
            iv_play.setVisibility(VISIBLE);
            iv_play.setImageResource(R.drawable.iplayer_click_pause_selector);
            tv_replay.setVisibility(INVISIBLE);
        } else if (playerState == PLAYER_STATE_ERROR) {
            iv_play.setVisibility(INVISIBLE);
            tv_replay.setVisibility(INVISIBLE);
        } else if (playerState == PLAYER_STATE_AUTO_COMPLETE) {
            iv_play.setVisibility(VISIBLE);
            iv_play.setImageResource(R.drawable.iplayer_click_replay_selector);
            tv_replay.setVisibility(VISIBLE);
        } else {
            iv_play.setImageResource(R.drawable.iplayer_click_play_selector);
            tv_replay.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        if (progressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.iplayer_dialog_progress, null);
            dialogProgressBar = localView.findViewById(R.id.iplayer_duration_progressbar);
            dialogSeekTime = localView.findViewById(R.id.iplayer_tv_current);
            dialogTotalTime = localView.findViewById(R.id.iplayer_tv_duration);
            dialogIcon = localView.findViewById(R.id.iplayer_duration_image_tip);
            progressDialog = createDialogWithView(localView);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        dialogSeekTime.setText(seekTime);
        dialogTotalTime.setText(" / " + totalTime);
        dialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (int) (seekTimePosition * 100 / totalTimeDuration));
        if (deltaX > 0) {
            dialogIcon.setBackgroundResource(R.drawable.iplayer_forward_icon);
        } else {
            dialogIcon.setBackgroundResource(R.drawable.iplayer_backward_icon);
        }
        onClickUiToggleToClear();
    }

    @Override
    protected void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        if (volumeDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.iplayer_dialog_volume, null);
            dialogVolumeImageView = localView.findViewById(R.id.iplayer_volume_image_tip);
            dialogVolumeTextView = localView.findViewById(R.id.iplayer_tv_volume);
            dialogVolumeProgressBar = localView.findViewById(R.id.iplayer_volume_progressbar);
            volumeDialog = createDialogWithView(localView);
        }
        if (!volumeDialog.isShowing()) {
            volumeDialog.show();
        }
        if (volumePercent <= 0) {
            dialogVolumeImageView.setBackgroundResource(R.drawable.iplayer_close_volume);
        } else {
            dialogVolumeImageView.setBackgroundResource(R.drawable.iplayer_add_volume);
        }
        if (volumePercent > 100) {
            volumePercent = 100;
        } else if (volumePercent < 0) {
            volumePercent = 0;
        }
        dialogVolumeTextView.setText(volumePercent + "%");
        dialogVolumeProgressBar.setProgress(volumePercent);
        onClickUiToggleToClear();
    }

    @Override
    protected void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (volumeDialog != null) {
            volumeDialog.dismiss();
        }
    }

    @Override
    protected void showBrightnessDialog(int brightnessPercent) {
        super.showBrightnessDialog(brightnessPercent);
        if (brightnessDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.iplayer_dialog_brightness, null);
            dialogBrightnessTextView = localView.findViewById(R.id.iplayer_tv_brightness);
            dialogBrightnessProgressBar = localView.findViewById(R.id.iplayer_brightness_progressbar);
            brightnessDialog = createDialogWithView(localView);
        }
        if (!brightnessDialog.isShowing()) {
            brightnessDialog.show();
        }
        if (brightnessPercent > 100) {
            brightnessPercent = 100;
        } else if (brightnessPercent < 0) {
            brightnessPercent = 0;
        }
        dialogBrightnessTextView.setText(brightnessPercent + "%");
        dialogBrightnessProgressBar.setProgress(brightnessPercent);
        onClickUiToggleToClear();
    }

    @Override
    protected void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        if (brightnessDialog != null) {
            brightnessDialog.dismiss();
        }
    }

    protected Dialog createDialogWithView(View localView) {
        Dialog dialog = new Dialog(getContext(), R.style.iplayer_style_dialog_progress);
        dialog.setContentView(localView);
        Window window = dialog.getWindow();
        window.addFlags(Window.FEATURE_ACTION_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(localLayoutParams);
        return dialog;
    }

    protected void startControlViewTimer() {
        cancelControlViewTimer();
        controlViewTimer = new Timer();
        controlViewTimerTask = new controlViewTimerTask();
        controlViewTimer.schedule(controlViewTimerTask, 2500);
    }

    protected void cancelControlViewTimer() {
        if (controlViewTimer != null) {
            controlViewTimer.cancel();
        }
        if (controlViewTimerTask != null) {
            controlViewTimerTask.cancel();
        }

    }

    @Override
    public void onPlayCompletion() {
        super.onPlayCompletion();
        cancelControlViewTimer();
    }

    @Override
    public void releasePlayer() {
        super.releasePlayer();
        cancelControlViewTimer();
        if (clarityPopWindow != null) {
            clarityPopWindow.dismiss();
        }
    }

    protected void dissmissControlView() {
        if (playerState != PLAYER_STATE_NORMAL
                && playerState != PLAYER_STATE_ERROR
                && playerState != PLAYER_STATE_AUTO_COMPLETE) {
            post(new Runnable() {
                @Override
                public void run() {
                    ll_bottom.setVisibility(View.INVISIBLE);
                    ll_top.setVisibility(View.INVISIBLE);
                    iv_play.setVisibility(View.INVISIBLE);
                    if (clarityPopWindow != null) {
                        clarityPopWindow.dismiss();
                    }
                    if (containerMode != CONTAINER_MODE_TINY) {
                        pro_bottom.setVisibility(enableBottomProgressBar ? View.VISIBLE : INVISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onRatate(int angle) {
        Player.setTextureRotation(angle);
    }

    @Override
    public void onSize(int size) {

    }

    protected class controlViewTimerTask extends TimerTask {
        @Override
        public void run() {
            dissmissControlView();
        }
    }
}
