package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.RotateActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.RotateViewModel;
import com.hacknife.example.ui.viewmodel.i.IRotateViewModel;
import com.hacknife.example.ui.view.IRotateView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class RotateModule {
    private IRotateView view;
    private RotateActivityBriefnessor briefnessor;

    public RotateModule(IRotateView view, RotateActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    IRotateViewModel provideViewModel() {
        return new RotateViewModel(view, briefnessor);
    }
}
