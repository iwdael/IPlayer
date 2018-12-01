package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.RecyclerFragmentBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.RecyclerModel;
import com.hacknife.example.ui.view.IRecyclerView;
import com.hacknife.example.ui.viewmodel.i.IRecyclerViewModel;
import com.hacknife.example.ui.model.i.IRecyclerModel;
import com.hacknife.iplayer.DataSource;

import java.util.List;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RecyclerViewModel extends BaseViewModel<IRecyclerView, IRecyclerModel, RecyclerFragmentBriefnessor> implements IRecyclerViewModel {

    public RecyclerViewModel(IRecyclerView view, RecyclerFragmentBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IRecyclerModel createModel() {
        return new RecyclerModel(this);
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