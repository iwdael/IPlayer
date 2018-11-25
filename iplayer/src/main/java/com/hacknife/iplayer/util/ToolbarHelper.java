package com.hacknife.iplayer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.WindowManager;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class ToolbarHelper {


    @SuppressLint("RestrictedApi")
    public static void showSupportActionBar(Context context) {
        if (PlayerUtils.getAppCompActivity(context) != null) {
            ActionBar ab = PlayerUtils.getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.show();
            }
        }

        PlayerUtils.getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @SuppressLint("RestrictedApi")
    public static void hideSupportActionBar(Context context) {
        if (PlayerUtils.getAppCompActivity(context) != null) {
            ActionBar ab = PlayerUtils.getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.hide();
            }
        }
        PlayerUtils.getWindow(context).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
