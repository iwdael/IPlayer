package com.hacknife.iplayer;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hacknife.iplayer.util.PlayerUtils;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_FULLSCREEN;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class OnPlayerAttachStateChangeListener implements RecyclerView.OnChildAttachStateChangeListener {
    @Override
    public void onChildViewAttachedToWindow(View view) {

    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        AbsPlayer video = PlayerUtils.findPlayer(view);
        if (video != null && video.getDataSource().containsTheUrl(MediaManager.getCurrentUrl())) {
            Player player = PlayerManager.getCurrentVideo();
            if (player != null && player.getContainerMode() != CONTAINER_MODE_FULLSCREEN) {
                Player.releaseAllPlayer();
            }
        }
    }
}
