package com.hacknife.iplayer;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hacknife.iplayer.state.PlayerState;
import com.hacknife.iplayer.util.PlayerUtils;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class OnPlayerAttachStateChangeListener implements RecyclerView.OnChildAttachStateChangeListener {
    @Override
    public void onChildViewAttachedToWindow(View view) {
        BasePlayer player = PlayerUtils.findPlayer(view);
        if (player != null && player.getDataSource().equals(MediaManager.getDataSource()) && player.enableTinyWindow && PlayerManager.getSecondFloor() != null) {
            Player.backPress();
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        BasePlayer video = PlayerUtils.findPlayer(view);
        if (video != null && video.getDataSource().equals(MediaManager.getDataSource())) {
            BasePlayer player = PlayerManager.getCurrentVideo();
            if (player != null && player.enableTinyWindow && player.getPlayerState() == PlayerState.PLAYER_STATE_PLAYING) {
                player.startTinyPlayer();
            } else if (player != null) {
                Player.releaseAllPlayer();
            }
        }
    }
}
