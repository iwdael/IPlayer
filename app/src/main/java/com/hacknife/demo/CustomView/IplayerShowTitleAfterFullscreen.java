package com.hacknife.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.hacknife.iplayer.state.ContainerMode;
import com.hacknife.iplayer.DataSource;
import com.hacknife.iplayer.IPlayer;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_FULLSCREEN;

/**
 * Created by Nathen
 * On 2016/04/27 10:49
 */
public class IplayerShowTitleAfterFullscreen extends IPlayer {
    public IplayerShowTitleAfterFullscreen(Context context) {
        super(context);
    }

    public IplayerShowTitleAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setDataSource(DataSource jzDataSource, ContainerMode screen) {
        super.setDataSource(jzDataSource, screen);
        if (containerMode == CONTAINER_MODE_FULLSCREEN) {
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.INVISIBLE);
        }
    }
}
