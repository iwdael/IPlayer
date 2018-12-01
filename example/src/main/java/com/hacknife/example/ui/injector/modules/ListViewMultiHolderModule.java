package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.ListViewMultiHolderActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.ListViewMultiHolderViewModel;
import com.hacknife.example.ui.viewmodel.i.IListViewMultiHolderViewModel;
import com.hacknife.example.ui.view.IListViewMultiHolderView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class ListViewMultiHolderModule {
    private IListViewMultiHolderView view;
    private ListViewMultiHolderActivityBriefnessor briefnessor;

    public ListViewMultiHolderModule(IListViewMultiHolderView view, ListViewMultiHolderActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IListViewMultiHolderViewModel provideViewModel() {
        return new ListViewMultiHolderViewModel(view, briefnessor);
    }
}
