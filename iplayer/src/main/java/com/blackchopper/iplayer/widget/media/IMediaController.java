/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blackchopper.iplayer.widget.media;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.MediaController;

import com.blackchopper.iplayer.util.MediaQuality;

import java.util.ArrayList;

public interface IMediaController {
    void hide();

    boolean isShowing();

    void setAnchorView(View view);

    void setEnabled(boolean enabled);

    void setMediaPlayer(MediaController.MediaPlayerControl player);

    void show(int timeout);

    void show();

    //----------
    // Extends
    //----------
    void showOnce(View view);

    void checkPlayer(boolean b);


    void setUrl(ArrayList<MediaQuality> url);

    void hideFullScreenIcon();

    void setCover(Bitmap bitmap);

    void restart();

    void error(int framework_err, int impl_err);

    void playerComplete();

    void onDestroy();
}
