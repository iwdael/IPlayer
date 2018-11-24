package com.hacknife.iplayer;

import android.widget.ImageView;

/**
 * Created by Hacknife on 2018/11/24.
 */

public interface ImageLoader {
    void onLoadCover(ImageView cover, String coverUrl);
}
