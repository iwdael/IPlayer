package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.constant.Constant;
import com.hacknife.example.ui.RecyclerViewActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.RecyclerViewModel;
import com.hacknife.example.ui.view.IRecyclerViewView;
import com.hacknife.example.ui.viewmodel.i.IRecyclerViewViewModel;
import com.hacknife.example.ui.model.i.IRecyclerViewModel;
import com.hacknife.iplayer.DataSource;

import java.util.ArrayList;
import java.util.List;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RecyclerViewViewModel extends BaseViewModel<IRecyclerViewView, IRecyclerViewModel, RecyclerViewActivityBriefnessor> implements IRecyclerViewViewModel {

    public RecyclerViewViewModel(IRecyclerViewView view, RecyclerViewActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IRecyclerViewModel createModel() {
        return new RecyclerViewModel(this);
    }


    @Override
    public void loadDataSource() {
        model.loadDataSource();

    }

    @Override
    public void callbackDataSource(List<DataSource> dataSources) {
        view.callbackDataSource(dataSources);
    }
}