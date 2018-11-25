package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.DemoActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.DemoViewModel;
import com.hacknife.example.ui.viewmodel.i.IDemoViewModel;
import com.hacknife.example.ui.view.IDemoView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class DemoModule {
    private IDemoView view;
    private DemoActivityBriefnessor briefnessor;

    public DemoModule(IDemoView view, DemoActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IDemoViewModel provideViewModel() {
        return new DemoViewModel(view, briefnessor);
    }
}
