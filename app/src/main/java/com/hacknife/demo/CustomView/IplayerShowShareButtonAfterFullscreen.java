package com.hacknife.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hacknife.iplayer.state.ContainerMode;
import com.hacknife.iplayer.DataSource;
import com.hacknife.iplayer.IPlayer;
import com.hacknife.demo.R;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_FULLSCREEN;

/**
 * Created by Nathen
 * On 2016/04/22 00:54
 */
public class IplayerShowShareButtonAfterFullscreen extends IPlayer {

    public ImageView shareButton;

    public IplayerShowShareButtonAfterFullscreen(Context context) {
        super(context);
    }

    public IplayerShowShareButtonAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(this);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_standard_with_share_button;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.share) {
            Toast.makeText(getContext(), "Whatever the icon means", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setDataSource(DataSource jzDataSource, ContainerMode screen) {
        super.setDataSource(jzDataSource, screen);
        if (containerMode == CONTAINER_MODE_FULLSCREEN) {
            shareButton.setVisibility(View.VISIBLE);
        } else {
            shareButton.setVisibility(View.INVISIBLE);
        }
    }
}
