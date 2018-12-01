package com.hacknife.iplayer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hacknife.iplayer.state.PlayerState;
import com.hacknife.iplayer.util.PlayerUtils;

import java.util.List;

import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_FULLSCREEN;
import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_LIST;
import static com.hacknife.iplayer.state.ContainerMode.CONTAINER_MODE_TINY;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PAUSE;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PLAYING;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class OnPlayerAttachStateChangeListener implements RecyclerView.OnChildAttachStateChangeListener, AbsListView.OnScrollListener {


    private int firstVisibleItem;
    private int lastVisibleItem;

    @Override
    public void onChildViewAttachedToWindow(View view) {
        BasePlayer player = PlayerUtils.findPlayer(view);
        if (player != null && player.getDataSource().equals(MediaManager.getDataSource()) && player.enableTinyWindow && PlayerManager.getSecondFloor() != null) {
            PlayerManager.getFirstFloor().playOnSelfPlayer();
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

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.firstVisibleItem == firstVisibleItem && this.lastVisibleItem == firstVisibleItem + visibleItemCount)
            return;
        int currentPlayPosition = MediaManager.get().positionInList;
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        if (currentPlayPosition == -1) return;
        if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
            if (PlayerManager.getCurrentVideo() != null && PlayerManager.getSecondFloor() == null && PlayerManager.getCurrentVideo().containerMode == CONTAINER_MODE_LIST) {
                if (PlayerManager.getCurrentVideo().enableTinyWindow && PlayerManager.getCurrentVideo().getPlayerState() == PLAYER_STATE_PLAYING) {
                    PlayerManager.getCurrentVideo().startTinyPlayer();
                } else {
                    Player.releaseAllPlayer();
                }
            }
        } else if (PlayerManager.getFirstFloor()!=null&&PlayerManager.getFirstFloor().enableTinyWindow && PlayerManager.getSecondFloor() != null && PlayerManager.getCurrentVideo().containerMode == CONTAINER_MODE_TINY) {
            PlayerManager.getFirstFloor().playOnSelfPlayer();
        }
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
    }


}
