package com.hacknife.iplayer;

import android.media.AudioManager;

import static com.hacknife.iplayer.Player.releaseAllVideos;
import static com.hacknife.iplayer.PlayerState.PLAYER_STATE_PLAYING;

/**
 * Created by Hacknife on 2018/11/22.
 */

public class OnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                releaseAllVideos();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                try {
                    Player player = PlayerManager.getCurrentVideo();
                    if (player != null && player.playerState == PLAYER_STATE_PLAYING) {
                        player.iv_play.performClick();
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                break;
        }
    }
}
