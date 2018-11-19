package com.hacknife.iplayer;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * desc    : PlayerManager
 */
public class PlayerManager {

    public static AbsPlayer pFirstVideo;
    public static AbsPlayer pSecendVideo;

    public static AbsPlayer getFirstFloor() {
        return pFirstVideo;
    }

    public static void setFirstFloor(AbsPlayer video) {
        pFirstVideo = video;
    }

    public static AbsPlayer getSecondFloor() {
        return pSecendVideo;
    }

    public static void setSecondFloor(AbsPlayer video) {
        pSecendVideo = video;
    }

    public static AbsPlayer getCurrentVideo() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (pSecendVideo != null) {
            pSecendVideo.onCompletion();
            pSecendVideo = null;
        }
        if (pFirstVideo != null) {
            pFirstVideo.onCompletion();
            pFirstVideo = null;
        }
    }
}
