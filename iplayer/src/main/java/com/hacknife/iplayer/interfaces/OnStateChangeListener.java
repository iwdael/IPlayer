package com.hacknife.iplayer.interfaces;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */

public interface OnStateChangeListener {
    void onStateNormal();

    void onStatePreparing();

    void onStatePrepared();

    void onStatePlay();

    void onStateRePlay();

    void onStatePause();

    void onStatePlayComplete();

    void onStateError();


    void onStatePlaying();
}
