package com.aliletter.iplayer.widget.media;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ListAdapter;
import android.widget.MediaController;

import com.aliletter.iplayer.util.MediaQuality;

import java.util.List;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/7
 */

public interface MediaPlayerControl extends MediaController.MediaPlayerControl {
    List<MediaQuality> getUrl();

    Activity getActivity();
}
