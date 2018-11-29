package com.hacknife.iplayer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hacknife.iplayer.state.PlayerState;
import com.hacknife.iplayer.util.PlayerUtils;

import java.util.List;

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

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.firstVisibleItem == firstVisibleItem && this.lastVisibleItem == firstVisibleItem + visibleItemCount)
            return;

        if (this.firstVisibleItem < firstVisibleItem) {
            onChildViewDetachedFromWindow(view.getAdapter().getView(this.firstVisibleItem, view.getChildAt(this.firstVisibleItem), view));
        } else if (this.firstVisibleItem > firstVisibleItem) {
            onChildViewAttachedToWindow(view.getAdapter().getView(firstVisibleItem, view.getChildAt(firstVisibleItem), view));
        }
        if (this.lastVisibleItem < firstVisibleItem + visibleItemCount) {
            onChildViewAttachedToWindow(view.getAdapter().getView(firstVisibleItem + visibleItemCount, view.getChildAt(firstVisibleItem + visibleItemCount), view));
        } else if (this.lastVisibleItem > firstVisibleItem + visibleItemCount) {
            onChildViewDetachedFromWindow(view.getAdapter().getView(lastVisibleItem, view.getChildAt(lastVisibleItem), view));
        }
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
    }
}
