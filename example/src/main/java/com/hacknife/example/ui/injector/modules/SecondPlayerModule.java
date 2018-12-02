package com.hacknife.example.ui.injector.modules;

import com.hacknife.example.ui.SecondPlayerActivityBriefnessor;

import com.hacknife.example.ui.viewmodel.SecondPlayerViewModel;
import com.hacknife.example.ui.viewmodel.i.ISecondPlayerViewModel;
import com.hacknife.example.ui.view.ISecondPlayerView;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Module
public class SecondPlayerModule {
    private ISecondPlayerView view;
    private SecondPlayerActivityBriefnessor briefnessor;

    public SecondPlayerModule(ISecondPlayerView view, SecondPlayerActivityBriefnessor briefnessor) {
        this.view = view;
        this.briefnessor = briefnessor;
    }

    @Provides
    ISecondPlayerViewModel provideViewModel() {
        return new SecondPlayerViewModel(view, briefnessor);
    }
}
