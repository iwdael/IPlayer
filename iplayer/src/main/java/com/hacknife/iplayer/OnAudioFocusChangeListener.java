package com.hacknife.iplayer;

import android.media.AudioManager;


import static com.hacknife.iplayer.Player.releaseAllPlayer;
import static com.hacknife.iplayer.state.PlayerState.PLAYER_STATE_PLAYING;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class OnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                releaseAllPlayer();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                try {
                    Player player = PlayerManager.getCurrentPlayer();
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
