package com.hacknife.example.engine;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.hacknife.iplayer.interfaces.PlayerCache;

public class IPlayerChache implements PlayerCache {
    HttpProxyCacheServer server;

    public IPlayerChache(Context context) {
        server = new HttpProxyCacheServer(context);
    }

    @Override
    public String convertCacheFromUrl(String url) {
        return server.getProxyUrl(url);
    }
}
