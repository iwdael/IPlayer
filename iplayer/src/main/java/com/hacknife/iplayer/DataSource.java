package com.hacknife.iplayer;

import java.util.HashMap;

import java.util.Map;
import java.util.UUID;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class DataSource {
    private String uuid;
    private static final String URL_KEY_DEFAULT = "URL_KEY_DEFAULT";
    private int index;
    private Map<String, Object> urlsMap = new HashMap<>();
    private String title = "";
    private HashMap<String, String> headerMap = new HashMap<>();
    private boolean loop = false;
    private Object cover;
    private boolean enableCache;

    public DataSource() {
        uuid = UUID.randomUUID().toString();
    }

    public DataSource(Object url, String title, Object cover) {
        if (enableCache) {
            urlsMap.put(URL_KEY_DEFAULT, MediaManager.getPlayCache().convertCacheFromUrl(url));
        } else {
            urlsMap.put(URL_KEY_DEFAULT, url);
        }
        this.title = title;
        index = 0;
        this.cover = cover;
        uuid = UUID.randomUUID().toString();
    }

    public int index() {
        return index;
    }

    public String title() {
        return title;
    }

    public HashMap<String, String> heanderMap() {
        return headerMap;
    }

    public Map<String, Object> urlsMap() {
        return urlsMap;
    }

    public boolean isLoop() {
        return loop;
    }

    public Object getCurrentUrl() {
        return getValue(index);
    }

    public String getCurrentKey() {
        return getKey(index);
    }

    public String getKey(int index) {
        int currentIndex = 0;
        for (String key : urlsMap.keySet()) {
            if (currentIndex == index) {
                return key;
            }
            currentIndex++;
        }
        return null;
    }

    public Object getValue(int index) {
        int currentIndex = 0;
        for (Object key : urlsMap.keySet()) {
            if (currentIndex == index) {
                return urlsMap.get(key);
            }
            currentIndex++;
        }
        return null;
    }


    public boolean containsTheUrl(Object url) {
        if (url != null) {
            return urlsMap.containsValue(url);
        }
        return false;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getCover() {
        return cover;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataSource) {
            if (this.uuid.equalsIgnoreCase(((DataSource) obj).uuid))
                return true;
            else
                return false;
        } else {
            return false;
        }

    }

    public void setEnableCache(boolean cache) {
        if (cache && (!this.enableCache)) {
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, Object> entry : urlsMap.entrySet()) {
                if (entry.getValue() instanceof String) {
                    map.put(entry.getKey(), MediaManager.get().videoCache.convertCacheFromUrl((String) entry.getValue()));
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            urlsMap = map;
        }
        this.enableCache = cache;
    }

    public static class Builder {
        DataSource dataSource;

        public Builder() {
            dataSource = new DataSource();
            dataSource.index = 0;
            dataSource.enableCache = false;
        }

        public Builder url(String clarity, Object url) {
            if (dataSource.enableCache) {
                dataSource.urlsMap.put(clarity, MediaManager.getPlayCache().convertCacheFromUrl(url));
            } else {
                dataSource.urlsMap.put(clarity, url);
            }
            return this;
        }

        public Builder header(String key, String val) {

            dataSource.headerMap.put(key, val);
            return this;
        }

        public Builder enableCache(boolean cache) {
            dataSource.enableCache = cache;
            return this;
        }

        public Builder title(String title) {
            dataSource.title = title;
            return this;
        }

        public Builder cover(Object cover) {
            dataSource.cover = cover;
            return this;
        }

        public Builder loop(boolean loop) {
            dataSource.loop = loop;
            return this;
        }

        public Builder index(int index) {
            if (index == 0)
                dataSource.index = index;
            return this;
        }

        public DataSource build() {
            return dataSource;
        }
    }
}
