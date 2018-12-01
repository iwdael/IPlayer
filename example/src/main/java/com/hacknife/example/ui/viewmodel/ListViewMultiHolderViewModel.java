package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.ListViewMultiHolderActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.ListViewMultiHolderModel;
import com.hacknife.example.ui.view.IListViewMultiHolderView;
import com.hacknife.example.ui.viewmodel.i.IListViewMultiHolderViewModel;
import com.hacknife.example.ui.model.i.IListViewMultiHolderModel;
import com.hacknife.iplayer.DataSource;

import java.util.List;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ListViewMultiHolderViewModel extends BaseViewModel<IListViewMultiHolderView, IListViewMultiHolderModel, ListViewMultiHolderActivityBriefnessor> implements IListViewMultiHolderViewModel {

    public ListViewMultiHolderViewModel(IListViewMultiHolderView view, ListViewMultiHolderActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IListViewMultiHolderModel createModel() {
        return new ListViewMultiHolderModel(this);
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