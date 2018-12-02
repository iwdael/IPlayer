package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.ListVieFragmentActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.ListVieFragmentViewModel;
import com.hacknife.example.ui.viewmodel.i.IListVieFragmentViewModel;
import com.hacknife.example.ui.view.IListVieFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class ListVieFragmentModule {
    private IListVieFragmentView view;
    private ListVieFragmentActivityBriefnessor briefnessor;

    public ListVieFragmentModule(IListVieFragmentView view, ListVieFragmentActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IListVieFragmentViewModel provideViewModel() {
        return new ListVieFragmentViewModel(view, briefnessor);
    }
}
