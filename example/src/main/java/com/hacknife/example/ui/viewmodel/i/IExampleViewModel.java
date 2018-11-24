package com.hacknife.example.ui.viewmodel.i;


import com.hacknife.example.bean.VideoSource;
import com.hacknife.example.ui.base.IBaseViewModel;

import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public interface IExampleViewModel extends IBaseViewModel {

    void callBackVideo(List<VideoSource> dataSources);

    void initView();
}