package com.hacknife.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.hacknife.iplayer.DataSource;
import com.hacknife.iplayer.Iplayer;

/**
 * Created by Nathen
 * On 2016/04/27 10:49
 */
public class IplayerShowTitleAfterFullscreen extends Iplayer {
    public IplayerShowTitleAfterFullscreen(Context context) {
        super(context);
    }

    public IplayerShowTitleAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setDataSource(DataSource jzDataSource, int screen) {
        super.setDataSource(jzDataSource, screen);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.INVISIBLE);
        }
    }
}
