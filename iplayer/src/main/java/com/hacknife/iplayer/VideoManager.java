package com.hacknife.iplayer;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * desc    : VideoManager
 */
public class VideoManager {

    public static Video pFirstVideo;
    public static Video pSecendVideo;

    public static Video getFirstFloor() {
        return pFirstVideo;
    }

    public static void setFirstFloor(Video video) {
        pFirstVideo = video;
    }

    public static Video getSecondFloor() {
        return pSecendVideo;
    }

    public static void setSecondFloor(Video video) {
        pSecendVideo = video;
    }

    public static Video getCurrentVideo() {
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
