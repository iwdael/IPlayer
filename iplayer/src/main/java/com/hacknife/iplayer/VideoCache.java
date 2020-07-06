package com.hacknife.iplayer;

import com.hacknife.iplayer.interfaces.PlayerCache;

public class VideoCache implements PlayerCache {
    @Override
    public Object convertCacheFromUrl(Object url) {
        return url;
    }
}
