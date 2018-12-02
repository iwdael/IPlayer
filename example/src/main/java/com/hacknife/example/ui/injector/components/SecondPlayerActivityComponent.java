package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.SecondPlayerModule;
import com.hacknife.example.ui.SecondPlayerActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = SecondPlayerModule.class)
public interface SecondPlayerActivityComponent {
    void inject(SecondPlayerActivity activity);
}