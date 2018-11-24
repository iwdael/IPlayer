package com.hacknife.example.adapter.base;


import android.view.View;

import com.hacknife.example.R;
import com.hacknife.example.bean.VideoSource;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.adapter.base.i.BaseRecyclerViewHolder;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.item_video)
public class VideoViewHolder extends BaseRecyclerViewHolder<VideoSource, VideoViewHolderBriefnessor> {


    public VideoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(VideoSource entity) {
        briefnessor.setVideo(entity);
    }


}