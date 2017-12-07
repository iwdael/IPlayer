package com.aliletter.iplayer.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.aliletter.iplayer.IPlayerActivity;
import com.aliletter.iplayer.R;
import com.aliletter.iplayer.util.MediaUtil;

import java.util.TimerTask;

/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Data: 2017/11/17.
 */

public class IMediaController extends BaseMediaController implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    protected CheckBox cb_play;
    protected SeekBar seek_bar;
    protected TextView tv_current_time, tv_video_duration;
    protected ImageView iv_fullscreen, iv_cover;
    @SuppressLint("HandlerLeak")
    protected Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case SHOW_VIDDEO_TIME:
                    VideoDuration duration = (VideoDuration) msg.obj;
                    tv_current_time.setText(duration.currentTime);
                    tv_video_duration.setText(duration.duration);
                    seek_bar.setProgress(msg.arg2);
                    break;
            }
        }
    };

    public IMediaController(Context context) {
        this(context, null);
    }

    public IMediaController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _initController();
        _initTimer();
    }

    private void _initTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mPlayer == null) return;
                Message msg = Message.obtain();
                msg.arg1 = SHOW_VIDDEO_TIME;
                msg.obj = new VideoDuration(MediaUtil.duration2Time(mPlayer.getCurrentPosition()), MediaUtil.duration2Time(mPlayer.getDuration()));
                msg.arg2 = (int) ((((float) mPlayer.getCurrentPosition() / mPlayer.getDuration())) * 100);
                handle.sendMessage(msg);
            }
        }, 1000, 1000);
    }

    private void _initController() {
        cb_play = findViewById(R.id.cb_video_play);
        seek_bar = findViewById(R.id.seek_bar);
        tv_current_time = findViewById(R.id.tv_current_time);
        tv_video_duration = findViewById(R.id.tv_video_duration);
        iv_fullscreen = findViewById(R.id.iv_fullscreen);
        iv_cover = findViewById(R.id.iv_cover);
        cb_play.setOnCheckedChangeListener(this);
        seek_bar.setOnSeekBarChangeListener(this);
        iv_fullscreen.setOnClickListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        float length = mPlayer.getDuration();
        float progress = seekBar.getProgress();
        float max = seekBar.getMax();
        int duration = (int) ((progress / max) * length);
        if ((duration > mPlayer.getCurrentPosition() & mPlayer.canSeekForward()) | (duration < mPlayer.getCurrentPosition() & mPlayer.canSeekBackward()))
            mPlayer.seekTo(duration);
        else
            Toast.makeText(getContext(), R.string.video_can_not_SeekForward, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        iv_cover.setVisibility(GONE);
        if (!mPlayer.canPause() && b == false) {
            cb_play.setChecked(true);
            return;
        }
        if (b)
            mPlayer.start();
        else
            mPlayer.pause();
    }

    @Override
    public void checkPlayer(boolean b) {

        cb_play.setChecked(b);
    }

    @Override
    public void hideFullScreenIcon() {
        iv_fullscreen.setVisibility(GONE);
    }

    @Override
    public void setCover(Bitmap bitmap) {
        iv_cover.setImageBitmap(bitmap);
    }


    @Override
    public void onClick(View view) {
        onCheckedChanged(null, false);
        Intent intent = new Intent(getContext(), IPlayerActivity.class);
        intent.putParcelableArrayListExtra("url", url);
        intent.putExtra("duration", mPlayer.getCurrentPosition());
        if (mPlayer.getActivity()!=null){
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mPlayer.getActivity(), this, "iplayer");
            getContext().startActivity(intent, options.toBundle());
        }else {
            getContext().startActivity(intent);
        }
//        PlayerDialog dialog = new PlayerDialog(getContext(), mPlayer.getUrl(), mPlayer.getCurrentPosition());
//        dialog.show();
    }


    class VideoDuration {
        String currentTime;
        String duration;

        public VideoDuration(String s, String s1) {
            currentTime = s;
            duration = s1;
        }
    }
}
