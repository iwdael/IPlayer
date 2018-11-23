package com.hacknife.example.bean;


import com.hacknife.iplayer.DataSource;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class VideoSource {
    String title;
    String url;
    String img;

    public VideoSource(String url, String title, String img) {
        this.url=url;
        this.title=title;
        this.img=img;
    }

    public DataSource getDataSource() {
        return new DataSource(url, title);
    }



    public Object getCover() {
        return img;
    }
}