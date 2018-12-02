package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.ListViewFragmentBriefnessor;

import com.hacknife.example.ui.viewmodel.ListViewFragViewModel;
import com.hacknife.example.ui.viewmodel.i.IListViewFragViewModel;
import com.hacknife.example.ui.view.IListViewFragView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class ListViewFragModule {
    private IListViewFragView view;
    private ListViewFragmentBriefnessor briefnessor;

    public ListViewFragModule(IListViewFragView view, ListViewFragmentBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IListViewFragViewModel provideViewModel() {
        return new ListViewFragViewModel(view, briefnessor);
    }
}
