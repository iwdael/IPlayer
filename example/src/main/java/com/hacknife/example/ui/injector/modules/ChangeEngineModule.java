package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.ChangeEngineActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.ChangeEngineViewModel;
import com.hacknife.example.ui.viewmodel.i.IChangeEngineViewModel;
import com.hacknife.example.ui.view.IChangeEngineView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class ChangeEngineModule {
    private IChangeEngineView view;
    private ChangeEngineActivityBriefnessor briefnessor;

    public ChangeEngineModule(IChangeEngineView view, ChangeEngineActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IChangeEngineViewModel provideViewModel() {
        return new ChangeEngineViewModel(view, briefnessor);
    }
}
