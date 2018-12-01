package com.hacknife.iplayer;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class PlayerManager {
    private static PlayerManager sPlayerManager;
    private BasePlayer pFirstPlayer;
    private BasePlayer pSecondPlayer;

    private static PlayerManager get() {
        if (sPlayerManager == null) {
            synchronized (PlayerManager.class) {
                if (sPlayerManager == null)
                    sPlayerManager = new PlayerManager();
            }
        }
        return sPlayerManager;
    }

    static BasePlayer getFirstFloor() {
        return get().pFirstPlayer;
    }

    static void setFirstFloor(BasePlayer video) {
        get().pFirstPlayer = video;
    }

    static BasePlayer getSecondFloor() {
        return get().pSecondPlayer;
    }

    static void setSecondFloor(BasePlayer video) {
        get().pSecondPlayer = video;
    }

    public static BasePlayer getCurrentVideo() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void releaseAllPlayer() {
        if (get().pSecondPlayer != null) {
            get().pSecondPlayer.releasePlayer();
            get().pSecondPlayer = null;
        }
        if (get().pFirstPlayer != null) {
            get().pFirstPlayer.releasePlayer();
            get().pFirstPlayer = null;
        }
    }
}
