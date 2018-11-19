package com.hacknife.iplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nathen
 * On 2016/04/18 16:15
 */
public class IPlayer extends AbsPlayer implements SettingView.OnSettingListener {


    public ImageView iv_back;
    public ProgressBar pro_bottom, pro_loading;
    public TextView tv_title;
    public ImageView iv_thumb;
    public ImageView iv_back_tiny;
    public LinearLayout ll_battery_time;
    public ImageView setting;
    public ImageView iv_battery;
    public TextView tv_system_time;
    public TextView tv_replay;
    public TextView tv_clarity;
    public PopupWindow tv_clarityPopWindow;
    public TextView tv_retry;
    public LinearLayout ll_retry;

    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected Timer dismissControlViewTimer;

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

    protected boolean enableBottomProgressBar = true;
    protected boolean enableFullScreen = true;
    protected boolean enableTitleBar = true;

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
    public void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IPlayer);
            enableBottomProgressBar = ta.getBoolean(R.styleable.IPlayer_enableBottomProgressBar, true);
            enableFullScreen = ta.getBoolean(R.styleable.IPlayer_enableFullScreen, true);
            enableTitleBar = ta.getBoolean(R.styleable.IPlayer_enableTitleBar, true);
            ta.recycle();
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
        if (!enableTitleBar) {
            ll_top.setVisibility(INVISIBLE);
        }
        if (!enableBottomProgressBar) {
            pro_bottom.setVisibility(INVISIBLE);
        }
        if (!enableFullScreen) {
            iv_fullscreen.setVisibility(INVISIBLE);
        }

    }

    public void setDataSource(DataSource dataSource, int screen) {
        super.setDataSource(dataSource, screen);
        tv_title.setText(dataSource.title);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            iv_fullscreen.setImageResource(R.drawable.iplayer_shrink);
            iv_back.setVisibility(View.VISIBLE);
            iv_back_tiny.setVisibility(View.INVISIBLE);
            ll_battery_time.setVisibility(View.VISIBLE);
            setting.setVisibility(View.VISIBLE);
            if (dataSource.urlsMap.size() == 1) {
                tv_clarity.setVisibility(GONE);
            } else {
                tv_clarity.setText(dataSource.getCurrentKey().toString());
                tv_clarity.setVisibility(View.VISIBLE);
            }
            changePlaySize((int) getResources().getDimension(R.dimen.iplayer_start_button_w_h_fullscreen));
        } else if (currentScreen == SCREEN_WINDOW_NORMAL
                || currentScreen == SCREEN_WINDOW_LIST) {
            iv_fullscreen.setImageResource(R.drawable.iplayer_enlarge);
            iv_back.setVisibility(View.GONE);
            iv_back_tiny.setVisibility(View.INVISIBLE);
            changePlaySize((int) getResources().getDimension(R.dimen.iplayer_start_button_w_h_normal));
            ll_battery_time.setVisibility(View.GONE);
            setting.setVisibility(View.GONE);
            tv_clarity.setVisibility(View.GONE);
        } else if (currentScreen == SCREEN_WINDOW_TINY) {
            iv_back_tiny.setVisibility(View.VISIBLE);
            setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
            ll_battery_time.setVisibility(View.GONE);
            setting.setVisibility(View.GONE);
            tv_clarity.setVisibility(View.GONE);
        }
        setSystemTimeAndBattery();
        if (tmp_test_back) {
            tmp_test_back = false;
            PlayerManager.setFirstFloor(this);
            backPress();
        }
    }

    public void changePlaySize(int size) {
        ViewGroup.LayoutParams lp = iv_play.getLayoutParams();
        lp.height = size;
        lp.width = size;
        lp = pro_loading.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }

    @Override
    public int getLayoutId() {
        return R.layout.iplayer_layout_standard;
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
    }

    @Override
    public void changeUrl(int urlMapIndex, long seekToInAdvance) {
        super.changeUrl(urlMapIndex, seekToInAdvance);
        pro_loading.setVisibility(VISIBLE);
        iv_play.setVisibility(INVISIBLE);
    }

    @Override
    public void changeUrl(DataSource dataSource, long seekToInAdvance) {
        super.changeUrl(dataSource, seekToInAdvance);
        tv_title.setText(dataSource.title);
        pro_loading.setVisibility(VISIBLE);
        iv_play.setVisibility(INVISIBLE);
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingClear();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
        pro_bottom.setProgress(100);
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
                    startDismissControlViewTimer();
                    if (mChangePosition) {
                        long duration = getDuration();
                        int progress = (int) (mSeekTimePosition * 100 / (duration == 0 ? 1 : duration));
                        pro_bottom.setProgress(progress);
                    }
                    if (!mChangePosition && !mChangeVolume) {
                        onEvent(Event.ON_CLICK_BLANK);
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == R.id.iplayer_sb_bottom) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
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
            if (dataSource.urlsMap.isEmpty() || dataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentState == CURRENT_STATE_NORMAL) {
                if (!dataSource.getCurrentUrl().toString().startsWith("file") &&
                        !dataSource.getCurrentUrl().toString().startsWith("/") &&
                        !PlayerUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startVideo();
                onEvent(Event.ON_CLICK_START_THUMB);//开始的事件应该在播放之后，此处特殊
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onClickUiToggle();
            }
        } else if (i == R.id.iplayer_fl_surface) {
            startDismissControlViewTimer();
        } else if (i == R.id.iplayer_iv_back) {
            backPress();
        } else if (i == R.id.iplayer_iv_back_tiny) {
            if (PlayerManager.getFirstFloor().currentScreen == Player.SCREEN_WINDOW_LIST) {
                quitFullscreenOrTinyWindow();
            } else {
                backPress();
            }
        } else if (i == R.id.iplayer_tv_clarity) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.iplayer_layout_clarity, null);

            OnClickListener mQualityListener = new OnClickListener() {
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    changeUrl(index, getCurrentPositionWhenPlaying());
                    tv_clarity.setText(dataSource.getCurrentKey().toString());
                    for (int j = 0; j < layout.getChildCount(); j++) {//设置点击之后的颜色
                        if (j == dataSource.currentUrlIndex) {
                            ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#fff85959"));
                        } else {
                            ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                    if (tv_clarityPopWindow != null) {
                        tv_clarityPopWindow.dismiss();
                    }
                }
            };

            for (int j = 0; j < dataSource.urlsMap.size(); j++) {
                String key = dataSource.getKeyFromDataSource(j);
                TextView tv_clarityItem = (TextView) View.inflate(getContext(), R.layout.iplayer_layout_clarity_item, null);
                tv_clarityItem.setText(key);
                tv_clarityItem.setTag(j);
                layout.addView(tv_clarityItem, j);
                tv_clarityItem.setOnClickListener(mQualityListener);
                if (j == dataSource.currentUrlIndex) {
                    tv_clarityItem.setTextColor(Color.parseColor("#fff85959"));
                }
            }

            tv_clarityPopWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
            tv_clarityPopWindow.setContentView(layout);
            tv_clarityPopWindow.showAsDropDown(tv_clarity);
            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int offsetX = tv_clarity.getMeasuredWidth() / 3;
            int offsetY = tv_clarity.getMeasuredHeight() / 3;
            tv_clarityPopWindow.update(tv_clarity, -offsetX, -offsetY, Math.round(layout.getMeasuredWidth() * 2), layout.getMeasuredHeight());
        } else if (i == R.id.iplayer_tv_retry) {
            if (dataSource.urlsMap.isEmpty() || dataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!dataSource.getCurrentUrl().toString().startsWith("file") && !
                    dataSource.getCurrentUrl().toString().startsWith("/") &&
                    !PlayerUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                showWifiDialog();
                return;
            }
            initTextureView();//和开始播放的代码重复
            addTextureView();
            MediaManager.setDataSource(dataSource);
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
    public void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onEvent(Event.ON_CLICK_START_WIFIDIALOG);
                startVideo();
                WIFI_TIP_DIALOG_SHOWED = true;
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                clearFloatScreen();
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
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        startDismissControlViewTimer();
    }

    public void onClickUiToggle() {
        if (ll_bottom.getVisibility() != View.VISIBLE) {
            setSystemTimeAndBattery();
            tv_clarity.setText(dataSource.getCurrentKey().toString());
        }
        if (currentState == CURRENT_STATE_PREPARING) {
            changeUiToPreparing();
            if (ll_bottom.getVisibility() == View.VISIBLE) {
            } else {
                setSystemTimeAndBattery();
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (ll_bottom.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (ll_bottom.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }

    public void setSystemTimeAndBattery() {
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

    public void setBattery() {
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

    public void onCLickUiToggleToClear() {
        if (currentState == CURRENT_STATE_PREPARING) {
            if (ll_bottom.getVisibility() == View.VISIBLE) {
                changeUiToPreparing();
            } else {
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (ll_bottom.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (ll_bottom.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
            }
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            if (ll_bottom.getVisibility() == View.VISIBLE) {
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
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        pro_bottom.setProgress(0);
        pro_bottom.setSecondaryProgress(0);
    }

    public void changeUiToNormal() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToPreparing() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPauseShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToPauseClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToComplete() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToError() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro,
                                        int thumbImg, int bottomPro, int retryLayout) {
        ll_top.setVisibility(enableTitleBar ? topCon : INVISIBLE);
        ll_bottom.setVisibility(bottomCon);
        iv_play.setVisibility(startBtn);
        pro_loading.setVisibility(loadingPro);
        iv_thumb.setVisibility(thumbImg);
        pro_bottom.setVisibility(enableBottomProgressBar ? bottomPro : INVISIBLE);
        ll_retry.setVisibility(retryLayout);
    }

    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            iv_play.setVisibility(VISIBLE);
            iv_play.setImageResource(R.drawable.iplayer_click_pause_selector);
            tv_replay.setVisibility(INVISIBLE);
        } else if (currentState == CURRENT_STATE_ERROR) {
            iv_play.setVisibility(INVISIBLE);
            tv_replay.setVisibility(INVISIBLE);
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            iv_play.setVisibility(VISIBLE);
            iv_play.setImageResource(R.drawable.iplayer_click_replay_selector);
            tv_replay.setVisibility(VISIBLE);
        } else {
            iv_play.setImageResource(R.drawable.iplayer_click_play_selector);
            tv_replay.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
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
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
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
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (volumeDialog != null) {
            volumeDialog.dismiss();
        }
    }

    @Override
    public void showBrightnessDialog(int brightnessPercent) {
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
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        if (brightnessDialog != null) {
            brightnessDialog.dismiss();
        }
    }

    public Dialog createDialogWithView(View localView) {
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

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        dismissControlViewTimer = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        dismissControlViewTimer.schedule(mDismissControlViewTimerTask, 2500);
    }

    public void cancelDismissControlViewTimer() {
        if (dismissControlViewTimer != null) {
            dismissControlViewTimer.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
        }

    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        cancelDismissControlViewTimer();
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        cancelDismissControlViewTimer();
        if (tv_clarityPopWindow != null) {
            tv_clarityPopWindow.dismiss();
        }
    }

    public void dissmissControlView() {
        if (currentState != CURRENT_STATE_NORMAL
                && currentState != CURRENT_STATE_ERROR
                && currentState != CURRENT_STATE_AUTO_COMPLETE) {
            post(new Runnable() {
                @Override
                public void run() {
                    ll_bottom.setVisibility(View.INVISIBLE);
                    ll_top.setVisibility(View.INVISIBLE);
                    iv_play.setVisibility(View.INVISIBLE);
                    if (tv_clarityPopWindow != null) {
                        tv_clarityPopWindow.dismiss();
                    }
                    if (currentScreen != SCREEN_WINDOW_TINY) {
                        pro_bottom.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onRatate(int angle) {
        Player.setTextureViewRotation(angle);
    }

    @Override
    public void onSize(int size) {

    }

    public class DismissControlViewTimerTask extends TimerTask {
        @Override
        public void run() {
            dissmissControlView();
        }
    }
}
