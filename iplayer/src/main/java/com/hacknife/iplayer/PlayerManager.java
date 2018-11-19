package com.hacknife.iplayer;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * desc    : PlayerManager
 */
public class PlayerManager {

    public static Player pFirstVideo;
    public static Player pSecendVideo;

    public static Player getFirstFloor() {
        return pFirstVideo;
    }

    public static void setFirstFloor(Player video) {
        pFirstVideo = video;
    }

    public static Player getSecondFloor() {
        return pSecendVideo;
    }

    public static void setSecondFloor(Player video) {
        pSecendVideo = video;
    }

    public static Player getCurrentVideo() {
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
