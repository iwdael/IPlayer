package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.ListViewActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.ListViewModel;
import com.hacknife.example.ui.view.IListViewView;
import com.hacknife.example.ui.viewmodel.i.IListViewViewModel;
import com.hacknife.example.ui.model.i.IListViewModel;
import com.hacknife.iplayer.DataSource;

import java.util.List;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ListViewViewModel extends BaseViewModel<IListViewView, IListViewModel, ListViewActivityBriefnessor> implements IListViewViewModel {

    public ListViewViewModel(IListViewView view, ListViewActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IListViewModel createModel() {
        return new ListViewModel(this);
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