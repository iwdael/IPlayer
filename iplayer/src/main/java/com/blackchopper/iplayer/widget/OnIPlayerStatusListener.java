package com.blackchopper.iplayer.widget;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : IPlayer
 */
public interface OnIPlayerStatusListener {
    /**
     * 视频准备完成
     */
    void onPrepareComplete();

    /**
     * 用户点击暂停
     */
    void onPause();

    /**
     * 用户点击播放
     */
    void onStart();

    /**
     * 播放结束
     */
    void onPlayComplete();

    void onError(int framework_err, int impl_err);
}
