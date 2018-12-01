package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.RecyclerFragmentActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.RecyclerFragmentViewModel;
import com.hacknife.example.ui.viewmodel.i.IRecyclerFragmentViewModel;
import com.hacknife.example.ui.view.IRecyclerFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class RecyclerFragmentModule {
    private IRecyclerFragmentView view;
    private RecyclerFragmentActivityBriefnessor briefnessor;

    public RecyclerFragmentModule(IRecyclerFragmentView view, RecyclerFragmentActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IRecyclerFragmentViewModel provideViewModel() {
        return new RecyclerFragmentViewModel(view, briefnessor);
    }
}
