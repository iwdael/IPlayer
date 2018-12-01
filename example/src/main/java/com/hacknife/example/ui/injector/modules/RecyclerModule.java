package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.RecyclerFragmentBriefnessor;

import com.hacknife.example.ui.viewmodel.RecyclerViewModel;
import com.hacknife.example.ui.viewmodel.i.IRecyclerViewModel;
import com.hacknife.example.ui.view.IRecyclerView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class RecyclerModule {
    private IRecyclerView view;
    private RecyclerFragmentBriefnessor briefnessor;

    public RecyclerModule(IRecyclerView view, RecyclerFragmentBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IRecyclerViewModel provideViewModel() {
        return new RecyclerViewModel(view, briefnessor);
    }
}
