package com.hacknife.iplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class PreferenceHelper {
    public static final String PRE_NAME = "progress";

    public static void saveProgress(Context context, Object url, long progress, boolean save) {
        if (!save) return;
        SharedPreferences spn = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spn.edit();
        editor.putLong(url.toString(), progress).apply();
    }

    public static long getSavedProgress(Context context, Object url) {
        SharedPreferences spn = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        return spn.getLong(url.toString(), 0);
    }


    public static void clearSavedProgress(Context context, Object url) {
        if (url == null) {
            SharedPreferences spn = context.getSharedPreferences(PRE_NAME,
                    Context.MODE_PRIVATE);
            spn.edit().clear().apply();
        } else {
            SharedPreferences spn = context.getSharedPreferences(PRE_NAME,
                    Context.MODE_PRIVATE);
            spn.edit().putLong(url.toString(), 0).apply();
        }
    }
}
