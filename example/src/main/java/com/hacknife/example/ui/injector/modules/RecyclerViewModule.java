package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.RecyclerViewActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.RecyclerViewViewModel;
import com.hacknife.example.ui.viewmodel.i.IRecyclerViewViewModel;
import com.hacknife.example.ui.view.IRecyclerViewView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class RecyclerViewModule {
    private IRecyclerViewView view;
    private RecyclerViewActivityBriefnessor briefnessor;

    public RecyclerViewModule(IRecyclerViewView view, RecyclerViewActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IRecyclerViewViewModel provideViewModel() {
        return new RecyclerViewViewModel(view, briefnessor);
    }
}
