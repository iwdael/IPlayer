package com.hacknife.example.ui.view;

import com.hacknife.example.bean.VideoSource;
import com.hacknife.example.ui.base.IBaseView;

import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public interface IExampleView extends IBaseView {

    void callbackVideo(List<VideoSource> dataSources);

}