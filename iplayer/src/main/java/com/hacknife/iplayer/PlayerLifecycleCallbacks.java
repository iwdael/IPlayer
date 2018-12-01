package com.hacknife.iplayer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class PlayerLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    protected int hashCode;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (hashCode == activity.hashCode())
            Player.resume();

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Player.pause();
        hashCode = activity.hashCode();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Player.releaseAllPlayer();
    }
}
