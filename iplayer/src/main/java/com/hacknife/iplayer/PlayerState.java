package com.hacknife.iplayer;

/**
 * Created by Hacknife on 2018/11/22.
 */

public enum PlayerState {

    PLAYER_STATE_ORIGINAL ,
    PLAYER_STATE_NORMAL,//默认状态
    PLAYER_STATE_PREPARING,//准备中
    PLAYER_STATE_PREPARING_CHANGING_URL,//切换播放源后准备中
    PLAYER_STATE_PLAYING,//播放中
    PLAYER_STATE_PAUSE,//暂停
    PLAYER_STATE_AUTO_COMPLETE,//播放完
    PLAYER_STATE_ERROR,//播放错误
}
