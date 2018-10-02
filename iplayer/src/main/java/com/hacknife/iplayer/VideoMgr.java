package com.hacknife.iplayer;

/**
 * Put JZVideoPlayer into layout
 * From a JZVideoPlayer to another JZVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class VideoMgr {

    public static Video FIRST_FLOOR_JZVD;
    public static Video SECOND_FLOOR_JZVD;

    public static Video getFirstFloor() {
        return FIRST_FLOOR_JZVD;
    }

    public static void setFirstFloor(Video jzvd) {
        FIRST_FLOOR_JZVD = jzvd;
    }

    public static Video getSecondFloor() {
        return SECOND_FLOOR_JZVD;
    }

    public static void setSecondFloor(Video jzvd) {
        SECOND_FLOOR_JZVD = jzvd;
    }

    public static Video getCurrentJzvd() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (SECOND_FLOOR_JZVD != null) {
            SECOND_FLOOR_JZVD.onCompletion();
            SECOND_FLOOR_JZVD = null;
        }
        if (FIRST_FLOOR_JZVD != null) {
            FIRST_FLOOR_JZVD.onCompletion();
            FIRST_FLOOR_JZVD = null;
        }
    }
}
