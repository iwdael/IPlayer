package com.aliletter.iplayer.widget;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/8
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
