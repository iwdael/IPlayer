package com.hacknife.example.adapter;

import com.hacknife.example.R;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hacknife.example.adapter.base.VideoViewHolder;
import com.hacknife.example.adapter.base.i.BaseRecyclerViewAdapter;
import com.hacknife.example.bean.VideoSource;

import javax.inject.Inject;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class VideoAdapter extends BaseRecyclerViewAdapter<VideoSource, VideoViewHolder> {
    @Inject
    public VideoAdapter() {
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, null));
    }

}
