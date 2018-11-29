package com.hacknife.iplayer;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class PlayerManager {

    private static BasePlayer pFirstVideo;
    private static BasePlayer pSecendVideo;

    static BasePlayer getFirstFloor() {
        return pFirstVideo;
    }

    static void setFirstFloor(BasePlayer video) {
        pFirstVideo = video;
    }

    static BasePlayer getSecondFloor() {
        return pSecendVideo;
    }

    static void setSecondFloor(BasePlayer video) {
        pSecendVideo = video;
    }

    public static BasePlayer getCurrentVideo() {
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
