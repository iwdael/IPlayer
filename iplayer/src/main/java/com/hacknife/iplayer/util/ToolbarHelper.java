package com.hacknife.iplayer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.WindowManager;

/**
 * Created by Hacknife on 2018/11/23.
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
