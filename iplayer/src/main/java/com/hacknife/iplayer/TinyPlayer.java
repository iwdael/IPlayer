package com.hacknife.iplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_TINY;
import static java.security.AccessController.getContext;

/**
 * Created by Hacknife on 2018/11/26.
 */


public class TinyPlayer extends Service {
    public static final String DATASOURCE = "BUNLDE_DATASOURCE";
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        int width;
        int height;
        if (PlayerManager.getCurrentVideo() != null) {
            if (PlayerManager.getCurrentVideo().tinyWindowHeight == 0 || PlayerManager.getCurrentVideo().tinyWindowWidth == 0) {
                width = 480;
                height = 270;
            } else {
                width = (int) (MediaManager.get().currentVideoWidth * (2f / 5f));
                height = (int) (MediaManager.get().currentVideoHeight * (2f / 5f));
            }
        } else {
            width = 480;
            height = 270;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.x = 20;
        layoutParams.y = 20;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DataSource dataSource = intent.getParcelableExtra(DATASOURCE);
        if (dataSource == null && PlayerManager.getCurrentVideo() == null) {
            throw new RuntimeException("TinyWindowPlayer need datasource !");
        }
        if (dataSource != null) {
        } else {
            try {
                Constructor<AbsPlayer> constructor = (Constructor<AbsPlayer>) PlayerManager.getCurrentVideo().getClass().getConstructor(Context.class);
                AbsPlayer player = constructor.newInstance(getApplicationContext());
                player.setId(R.id.iplayer_tiny_id);
                windowManager.addView(player, layoutParams);
                player.setOnTouchListener(new OnTouchListener());
                player.setDataSource(PlayerManager.getCurrentVideo().getDataSource(), CONTAINER_MODE_TINY);
                player.setState(PlayerManager.getCurrentVideo().playerState);
                player.addTextureView();
                player.setOnStateChangeListener(new OnStateChangeListener() {
                    @Override
                    public void onStatePlayComplete() {
                        TinyPlayer.this.stopSelf();
                    }

                    @Override
                    public void onStateRelease() {
                        TinyPlayer.this.stopSelf();
                    }
                });
                PlayerManager.setSecondFloor(player);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class OnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}
