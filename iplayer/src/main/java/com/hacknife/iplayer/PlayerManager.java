package com.hacknife.iplayer;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class PlayerManager {

    private static AbsPlayer pFirstVideo;
    private static AbsPlayer pSecendVideo;

    static AbsPlayer getFirstFloor() {
        return pFirstVideo;
    }

    static void setFirstFloor(AbsPlayer video) {
        pFirstVideo = video;
    }

    static AbsPlayer getSecondFloor() {
        return pSecendVideo;
    }

    static void setSecondFloor(AbsPlayer video) {
        pSecendVideo = video;
    }

    public static AbsPlayer getCurrentVideo() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void releaseAllPlayer() {
        if (pSecendVideo != null) {
            pSecendVideo.releasePlayer();
            pSecendVideo = null;
        }
        if (pFirstVideo != null) {
            pFirstVideo.releasePlayer();
            pFirstVideo = null;
        }
    }
}
