package com.blackchopper.iplayer.widget.media;

import android.app.Activity;
import android.widget.MediaController;

import com.blackchopper.iplayer.util.MediaQuality;

import java.util.List;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : IPlayer
 */
public interface MediaPlayerControl extends MediaController.MediaPlayerControl {
    List<MediaQuality> getUrl();

    Activity getActivity();
}
