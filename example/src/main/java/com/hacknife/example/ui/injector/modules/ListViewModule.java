package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.ListViewActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.ListViewViewModel;
import com.hacknife.example.ui.viewmodel.i.IListViewViewModel;
import com.hacknife.example.ui.view.IListViewView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class ListViewModule {
    private IListViewView view;
    private ListViewActivityBriefnessor briefnessor;

    public ListViewModule(IListViewView view, ListViewActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IListViewViewModel provideViewModel() {
        return new ListViewViewModel(view, briefnessor);
    }
}
