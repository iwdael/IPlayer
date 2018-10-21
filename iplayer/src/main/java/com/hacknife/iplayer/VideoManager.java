package com.hacknife.iplayer;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * desc    : VideoManager
 */
public class VideoManager {

    public static Video FIRST_FLOOR_VIDEO;
    public static Video SECOND_FLOOR_VIDEO;

    public static Video getFirstFloor() {
        return FIRST_FLOOR_VIDEO;
    }

    public static void setFirstFloor(Video jzvd) {
        FIRST_FLOOR_VIDEO = jzvd;
    }

    public static Video getSecondFloor() {
        return SECOND_FLOOR_VIDEO;
    }

    public static void setSecondFloor(Video jzvd) {
        SECOND_FLOOR_VIDEO = jzvd;
    }

    public static Video getCurrentVideo() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (SECOND_FLOOR_VIDEO != null) {
            SECOND_FLOOR_VIDEO.onCompletion();
            SECOND_FLOOR_VIDEO = null;
        }
        if (FIRST_FLOOR_VIDEO != null) {
            FIRST_FLOOR_VIDEO.onCompletion();
            FIRST_FLOOR_VIDEO = null;
        }
    }
}
