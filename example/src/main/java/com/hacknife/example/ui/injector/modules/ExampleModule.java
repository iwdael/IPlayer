package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.ExampleActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.ExampleViewModel;
import com.hacknife.example.ui.viewmodel.i.IExampleViewModel;
import com.hacknife.example.ui.view.IExampleView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class ExampleModule {
    private IExampleView view;
    private ExampleActivityBriefnessor briefnessor;

    public ExampleModule(IExampleView view, ExampleActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IExampleViewModel provideViewModel() {
        return new ExampleViewModel(view, briefnessor);
    }
}
