package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.RecyclerViewMultiHolderActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.RecyclerViewMultiHolderModel;
import com.hacknife.example.ui.view.IRecyclerViewMultiHolderView;
import com.hacknife.example.ui.viewmodel.i.IRecyclerViewMultiHolderViewModel;
import com.hacknife.example.ui.model.i.IRecyclerViewMultiHolderModel;
import com.hacknife.iplayer.DataSource;

import java.util.List;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RecyclerViewMultiHolderViewModel extends BaseViewModel<IRecyclerViewMultiHolderView, IRecyclerViewMultiHolderModel, RecyclerViewMultiHolderActivityBriefnessor> implements IRecyclerViewMultiHolderViewModel {

    public RecyclerViewMultiHolderViewModel(IRecyclerViewMultiHolderView view, RecyclerViewMultiHolderActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IRecyclerViewMultiHolderModel createModel() {
        return new RecyclerViewMultiHolderModel(this);
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