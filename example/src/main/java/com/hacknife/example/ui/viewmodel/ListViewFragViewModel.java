package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.ListViewFragmentBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.ListViewFragModel;
import com.hacknife.example.ui.view.IListViewFragView;
import com.hacknife.example.ui.viewmodel.i.IListViewFragViewModel;
import com.hacknife.example.ui.model.i.IListViewFragModel;
import com.hacknife.iplayer.DataSource;

import java.util.List;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ListViewFragViewModel extends BaseViewModel<IListViewFragView, IListViewFragModel, ListViewFragmentBriefnessor> implements IListViewFragViewModel {

    public ListViewFragViewModel(IListViewFragView view, ListViewFragmentBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IListViewFragModel createModel() {
        return new ListViewFragModel(this);
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