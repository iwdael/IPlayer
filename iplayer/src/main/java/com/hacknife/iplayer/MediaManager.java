package com.hacknife.iplayer;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.hacknife.iplayer.engine.MediaEngine;
import com.hacknife.iplayer.engine.PlayerEngine;
import com.hacknife.iplayer.widget.PlayerTextureView;


public class MediaManager implements TextureView.SurfaceTextureListener {

    public static final String TAG = "MediaManager";
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_RELEASE = 2;

    public static PlayerTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public static Surface surface;
    public static MediaManager sMediaManager;
    public int positionInList = -1;
    public PlayerEngine engine;
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    protected HandlerThread thread;
    public MediaHandler pMediaHandler;
    public Handler pMainThreadHandler;
    public ImageLoader loader;

    public MediaManager() {
        thread = new HandlerThread(TAG);
        thread.start();
        pMediaHandler = new MediaHandler(thread.getLooper());
        pMainThreadHandler = new Handler();
        if (engine == null)
            engine = new MediaEngine();
    }

    public static MediaManager get() {
        if (sMediaManager == null) {
            synchronized (MediaManager.class) {
                if (sMediaManager == null) {
                    sMediaManager = new MediaManager();
                }
            }
        }
        return sMediaManager;
    }

    public static void setDataSource(DataSource dataSource) {
        get().engine.dataSource = dataSource;
    }

    public static ImageLoader getImageLoader() {
        return get().loader;
    }

    public static void setImageLoader(ImageLoader loader) {
        get().loader = loader;
    }

    public static DataSource getDataSource() {
        return get().engine.dataSource;
    }


    public static Object getCurrentUrl() {
        return get().engine.dataSource == null ? null : get().engine.dataSource.getCurrentUrl();
    }


    public static long getCurrentPosition() {
        return get().engine.getCurrentPosition();
    }

    public static long getDuration() {
        return get().engine.getDuration();
    }

    public static void seekTo(long time) {
        get().engine.seekTo(time);
    }

    public static void pause() {
        get().engine.pause();
    }

    public static void start() {
        get().engine.start();
    }

    public static boolean isPlaying() {
        return get().engine.isPlaying();
    }

    public void releaseMediaPlayer() {
        pMediaHandler.removeCallbacksAndMessages(null);
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        pMediaHandler.sendMessage(msg);
    }

    public void prepare() {
        releaseMediaPlayer();
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        pMediaHandler.sendMessage(msg);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
         if (PlayerManager.getCurrentVideo() == null) return;
        if (savedSurfaceTexture == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare();
        } else {
            textureView.setSurfaceTexture(savedSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    currentVideoWidth = 0;
                    currentVideoHeight = 0;
                    engine.prepare();
                    if (savedSurfaceTexture != null) {
                        if (surface != null) {
                            surface.release();
                        }
                        surface = new Surface(savedSurfaceTexture);
                        engine.setSurface(surface);
                    }
                    break;
                case HANDLER_RELEASE:
                    engine.release();
                    break;
            }
        }
    }
}
