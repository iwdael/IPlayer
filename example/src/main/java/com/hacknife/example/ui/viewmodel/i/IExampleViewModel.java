package com.hacknife.example.ui.viewmodel.i;


import com.hacknife.example.bean.JsonRootBean;
import com.hacknife.example.ui.base.IBaseViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public interface IExampleViewModel extends IBaseViewModel {

    void loadVideo(int refresh);

    void callbackVideoList(JsonRootBean jsonRootBean, int refresh);
}