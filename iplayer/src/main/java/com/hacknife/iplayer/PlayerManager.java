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

    public static BasePlayer getFirstPlayer() {
        return get().pFirstPlayer;
    }

    static void setFirstPlayer(BasePlayer video) {
        get().pFirstPlayer = video;
    }

    static BasePlayer getSecondPlayer() {
        return get().pSecondPlayer;
    }

    static void setSecondPlayer(BasePlayer video) {
        get().pSecondPlayer = video;
    }

    public static BasePlayer getCurrentPlayer() {
        if (getSecondPlayer() != null) {
            return getSecondPlayer();
        }
        return getFirstPlayer();
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
