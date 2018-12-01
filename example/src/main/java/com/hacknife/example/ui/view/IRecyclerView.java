package com.hacknife.example.ui.view;

import com.hacknife.example.ui.base.IBaseView;
import com.hacknife.iplayer.DataSource;

import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public interface IRecyclerView extends IBaseView {

    void callbackDataSource(List<DataSource> dataSources);
}