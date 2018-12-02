package com.hacknife.example.ui.viewmodel.i;


import com.hacknife.example.ui.base.IBaseViewModel;
import com.hacknife.iplayer.DataSource;

import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public interface IListViewFragViewModel extends IBaseViewModel {

    void loadDataSource();

    void callbackDataSource(List<DataSource> dataSources);

}