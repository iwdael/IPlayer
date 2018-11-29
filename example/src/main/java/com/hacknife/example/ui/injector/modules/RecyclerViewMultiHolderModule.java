package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.RecyclerViewMultiHolderActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.RecyclerViewMultiHolderViewModel;
import com.hacknife.example.ui.viewmodel.i.IRecyclerViewMultiHolderViewModel;
import com.hacknife.example.ui.view.IRecyclerViewMultiHolderView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class RecyclerViewMultiHolderModule {
    private IRecyclerViewMultiHolderView view;
    private RecyclerViewMultiHolderActivityBriefnessor briefnessor;

    public RecyclerViewMultiHolderModule(IRecyclerViewMultiHolderView view, RecyclerViewMultiHolderActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IRecyclerViewMultiHolderViewModel provideViewModel() {
        return new RecyclerViewMultiHolderViewModel(view, briefnessor);
    }
}
